package com.ruoyi.system.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Component
public class PythonProcessManager {

    private static final Logger logger = LoggerFactory.getLogger(PythonProcessManager.class);

    private Process pythonProcess;
    private Thread outputReaderThread;

    @Value("${python.service.port:5000}")
    private int port;

    @Value("${python.service.startup-timeout:30}")
    private int startupTimeout; // 秒

    // 新增：Python 解释器命令，可通过配置修改（例如 conda 环境中的 python.exe 路径）
    @Value("${python.service.command:python}")
    private String pythonCommand;

    @PostConstruct
    public void startPythonService() {
        try {
            // 获取资源目录下的 Python 脚本路径
            String scriptPath = getPythonScriptPath();
            String modelPath = getModelPath();

            // 解析最终使用的 Python 命令（支持环境变量覆盖）
            String effectivePythonCommand = resolvePythonCommand();

            logger.info("启动 Python 模型服务，脚本: {}, 模型: {}, Python 命令: {}",
                    scriptPath, modelPath, effectivePythonCommand);

            // 构建启动命令
            ProcessBuilder pb = new ProcessBuilder(
                    effectivePythonCommand,   // 使用配置的 Python 解释器
                    scriptPath
            );
            // 设置工作目录为脚本所在目录（方便模型加载）
            pb.directory(new File(scriptPath).getParentFile());

            // 环境变量（可选）
            pb.environment().put("PYTHONUNBUFFERED", "1");

            // 合并错误流和输出流
            pb.redirectErrorStream(true);

            pythonProcess = pb.start();

            // 启动线程读取输出（避免缓冲区阻塞）
            startOutputReader(pythonProcess.getInputStream());

            // 等待服务启动完成
            waitForServiceReady();

            logger.info("Python 模型服务启动成功，端口: {}", port);

        } catch (Exception e) {
            logger.error("启动 Python 模型服务失败", e);
            throw new RuntimeException("无法启动 Python 模型服务", e);
        }
    }

    /**
     * 解析最终使用的 Python 命令。
     * 优先使用环境变量 PYTHON_COMMAND，若未设置则使用配置文件中的值。
     */
    private String resolvePythonCommand() {
        String envCommand = System.getenv("PYTHON_COMMAND");
        if (envCommand != null && !envCommand.trim().isEmpty()) {
            logger.info("使用环境变量 PYTHON_COMMAND: {}", envCommand);
            return envCommand;
        }
        return pythonCommand;
    }

    private String getPythonScriptPath() throws IOException {
        // 从 resources 目录复制到临时目录，或者直接使用绝对路径
        String resourcePath = "python/model_service.py";
        InputStream in = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (in == null) {
            throw new FileNotFoundException("资源文件不存在: " + resourcePath);
        }
        // 复制到系统临时目录
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "python_service");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        File scriptFile = new File(tempDir, "model_service.py");
        try (FileOutputStream out = new FileOutputStream(scriptFile)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        }
        // 同时复制模型文件
        String modelResourcePath = "python/tobacco_warning_model.pkl";
        InputStream modelIn = getClass().getClassLoader().getResourceAsStream(modelResourcePath);
        if (modelIn == null) {
            throw new FileNotFoundException("模型文件不存在: " + modelResourcePath);
        }
        File modelFile = new File(tempDir, "tobacco_warning_model.pkl");
        try (FileOutputStream out = new FileOutputStream(modelFile)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = modelIn.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        }
        return scriptFile.getAbsolutePath();
    }

    private String getModelPath() {
        String tempDir = System.getProperty("java.io.tmpdir");
        return Paths.get(tempDir, "python_service", "tobacco_warning_model.pkl").toString();
    }

    private void startOutputReader(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        outputReaderThread = new Thread(() -> {
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    logger.info("[Python] {}", line);
                }
            } catch (IOException e) {
                if (pythonProcess != null && pythonProcess.isAlive()) {
                    logger.warn("Python 输出流读取中断", e);
                }
            }
        });
        outputReaderThread.setDaemon(true);
        outputReaderThread.start();
    }

    private void waitForServiceReady() throws Exception {
        String url = "http://localhost:" + port + "/health";
        long start = System.currentTimeMillis();
        long timeout = startupTimeout * 1000L;
        while (System.currentTimeMillis() - start < timeout) {
            try {
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection)
                        new java.net.URL(url).openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(1000);
                conn.setReadTimeout(1000);
                int code = conn.getResponseCode();
                if (code == 200) {
                    return;
                }
            } catch (Exception e) {
                // 忽略，继续等待
            }
            Thread.sleep(1000);
        }
        throw new RuntimeException("Python 服务在指定时间内未就绪");
    }

    @PreDestroy
    public void stopPythonService() {
        if (pythonProcess != null && pythonProcess.isAlive()) {
            logger.info("正在关闭 Python 模型服务...");
            pythonProcess.destroy();
            try {
                boolean terminated = pythonProcess.waitFor(5, TimeUnit.SECONDS);
                if (!terminated) {
                    pythonProcess.destroyForcibly();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            logger.info("Python 模型服务已关闭");
        }
        if (outputReaderThread != null && outputReaderThread.isAlive()) {
            outputReaderThread.interrupt();
        }
        // 可选：清理临时目录
        cleanTempDirectory();
    }

    /**
     * 清理复制到临时目录的 Python 脚本和模型文件
     */
    private void cleanTempDirectory() {
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "python_service");
        if (tempDir.exists()) {
            File[] files = tempDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.delete()) {
                        logger.warn("无法删除临时文件: {}", file.getAbsolutePath());
                    }
                }
            }
            if (!tempDir.delete()) {
                logger.warn("无法删除临时目录: {}", tempDir.getAbsolutePath());
            }
        }
    }
}