package com.ruoyi.web.controller.cigarette.caution;


import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/cigarette/vehicle/factorConfig")
public class ToRiskFactorController {

    private static final String FACTOR_FILE = "config/risk_factor.json";
    private static final String POSITION_FILE = "risk_position.txt";

    @GetMapping
    public Map<String, Integer> getFactors() throws IOException {
        return readFactorConfig();
    }

    @PostMapping
    public String updateFactors(@RequestBody Map<String, Integer> updated) throws IOException {
        writeFactorConfig(updated);
        resetPosition();
        return "Risk factors updated and position reset.";
    }

    private Map<String, Integer> readFactorConfig() throws IOException {
        File file = new File(FACTOR_FILE);
        if (!file.exists()) return new HashMap<>();

        try (Reader reader = new FileReader(file)) {
            return new com.fasterxml.jackson.databind.ObjectMapper().readValue(reader, Map.class);
        }
    }

    private void writeFactorConfig(Map<String, Integer> data) throws IOException {
        try (Writer writer = new FileWriter(FACTOR_FILE)) {
            new com.fasterxml.jackson.databind.ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(writer, data);
        }
    }

    private void resetPosition() throws IOException {
        File posFile = new File(POSITION_FILE);
        if (posFile.exists()) {
            try (Writer writer = new FileWriter(posFile)) {
                writer.write("0");
            }
        }
    }
}


