package com.permovdb.permovdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class PermovdbApplication {
	public static void main(String[] args) {
		try {
			new ProcessBuilder("taskkill", "/F", "/IM", "node.exe").start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SpringApplication.run(PermovdbApplication.class, args);

		// String currPath = System.getProperty("user.dir");
		// Path basePath = Paths.get("E:\\desktop25.04.2025\\page");
		// Path frontendPath = basePath.resolve("../../../frontend").normalize();

		// System.out.println(frontendPath.toString());

		// System.out.println(basePath);

		// String pathToPage = basePath + "/../../../";
		// System.out.println(pathToPage);
		try {
			ProcessBuilder pb = new ProcessBuilder(
					"cmd.exe", "/c", "start", "cmd.exe", "/k",
					"cd /d E:\\desktop25.04.2025\\page\\FrontEnd\\PerMovDb && npm run dev");
			pb.start();
			ProcessBuilder pbRec = new ProcessBuilder(
					"cmd.exe", "/c", "start", "cmd.exe", "/k",
					"cd /d E:\\desktop25.04.2025\\page\\BackEnd\\perMovDb\\src\\main\\resources\\scripts\\recommendationService && python rec.py");
			pbRec.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// @PostConstruct
	// public void runCommand(String runPath, String... command) {
	// try {
	// // ProcessBuilder processBuilder = new ProcessBuilder(command);
	// // processBuilder.directory(new File(runPath));
	// // processBuilder.redirectErrorStream(true);

	// // Process process = processBuilder.start();
	// new ProcessBuilder(
	// "gnome-terminal", "--", "bash", "-c",
	// "cd " + "/FrontEnd/perMovDb" + " && npm run dev; exec bash").start();

	// } catch (Exception e) {
	// // TODO: handle exception
	// }

	// }

	@PostConstruct
	public void runRecommendationEngine() {
		try {
			ProcessBuilder builder = new ProcessBuilder(
					"python3", "src/main/resources/scripts/recommendationService/rec.py" // or absolute path
			);

			builder.redirectErrorStream(true);
			Process process = builder.start();

			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					System.out.println("[Python] " + line);
				}
			}

			int exitCode = process.waitFor();
			System.out.println("Python script exited with code: " + exitCode);

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	// @PostConstruct
	// public void runFrontEnd() {
	// try {
	// ProcessBuilder builder = new ProcessBuilder(
	// "npm run dev", "/page/FrontEnd/perMovDb/" // or absolute path
	// );

	// builder.redirectErrorStream(true);
	// Process process = builder.start();

	// try (BufferedReader reader = new BufferedReader(
	// new InputStreamReader(process.getInputStream()))) {
	// String line;
	// while ((line = reader.readLine()) != null) {
	// System.out.println("[Python] " + line);
	// }
	// }

	// int exitCode = process.waitFor();
	// System.out.println("Python script exited with code: " + exitCode);

	// } catch (IOException | InterruptedException e) {
	// e.printStackTrace();
	// }
	// }

}
