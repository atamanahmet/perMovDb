package com.permovdb.permovdb.startup;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class Startup {

    @PostConstruct
    public void startupProcesses() {
        killAllNode();
        killRecEngine();
        startFrontend();
        startPythonRecommendationEngine();
    }

    private void killRecEngine() {
        try {
            new ProcessBuilder("powershell.exe", "/c",
                    "Get-CimInstance Win32_Process | " +
                            "Where-Object { $_.CommandLine -like '*rec.py*' } | " +
                            "ForEach-Object { Stop-Process -Id $_.ProcessId -Force }")
                    .start();
            System.out.println("Rec killed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void killAllNode() {
        try {
            new ProcessBuilder("taskkill", "/F", "/IM", "node.exe").start();
            System.out.println("Node killed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startFrontend() {
        try {
            new ProcessBuilder(
                    "cmd.exe", "/c", "start", "cmd.exe", "/k",
                    "cd /d E:\\desktop25.04.2025\\page\\FrontEnd\\PerMovDb && npm run dev").start();
            System.out.println("Frontend started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startPythonRecommendationEngine() {
        try {
            new ProcessBuilder(
                    "cmd.exe", "/c", "start", "cmd.exe", "/k",
                    "cd /d E:\\desktop25.04.2025\\page\\RecommendationEngine\\recommendationService && python rec.py")
                    .start();
            System.out.println("RecEngine started");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
