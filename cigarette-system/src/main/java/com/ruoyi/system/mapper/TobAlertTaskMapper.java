package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.TobAlertTask;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 预警任务Mapper接口
 */
public interface TobAlertTaskMapper {

    /**
     * 查询预警任务
     */
    TobAlertTask selectTobAlertTaskById(Long id);

    /**
     * 根据原始数据ID查询
     */
    TobAlertTask selectTobAlertTaskByOriginalId(Long originalId);

    /**
     * 查询预警任务列表
     */
    List<TobAlertTask> selectTobAlertTaskList(TobAlertTask tobAlertTask);

    /**
     * 新增预警任务
     */
    int insertTobAlertTask(TobAlertTask tobAlertTask);

    /**
     * 修改预警任务
     */
    int updateTobAlertTask(TobAlertTask tobAlertTask);

    /**
     * 删除预警任务
     */
    int deleteTobAlertTaskById(Long id);

    /**
     * 批量删除预警任务
     */
    int deleteTobAlertTaskByIds(Long[] ids);

    /**
     * 原子更新任务状态（防并发）
     */
    int updateTobAlertTaskStatus(@Param("taskId") Long taskId, @Param("oldStatus") Integer oldStatus,
                                 @Param("newStatus") Integer newStatus, @Param("acceptUser") String acceptUser);

    /**
     * 查询超时未处理任务
     */
    List<TobAlertTask> selectTimeoutTasks();

    /**
     * 查询某人当前处理中的任务数（status=1 已接受未完成）
     */
    int countActiveTasksByUserId(@Param("userId") String userId);
}
