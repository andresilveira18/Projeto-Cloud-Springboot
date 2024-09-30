package br.edu.ibmec.projeto_cloud;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjetoCloudApplication {

	public static void main(String[] args) {
		// Load .env only if not running under the 'test' profile
		String activeProfile = System.getProperty("spring.profiles.active");
		if (!"test".equals(activeProfile)) {
			Dotenv dotenv = Dotenv.configure()
					.directory("./Projeto-Cloud-Springboot")
					.load();
			System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
			System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
			System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
			System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
			System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		}
		SpringApplication.run(ProjetoCloudApplication.class, args);
	}
}
