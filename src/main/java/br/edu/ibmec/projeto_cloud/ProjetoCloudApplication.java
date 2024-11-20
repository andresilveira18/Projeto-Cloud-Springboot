package br.edu.ibmec.projeto_cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjetoCloudApplication {

    public static void main(String[] args) {
        System.setProperty("DB_HOST", System.getenv("DB_HOST"));
        System.setProperty("DB_PORT", System.getenv("DB_PORT"));
        System.setProperty("DB_NAME", System.getenv("DB_NAME"));
        System.setProperty("DB_USERNAME", System.getenv("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", System.getenv("DB_PASSWORD"));

        SpringApplication.run(ProjetoCloudApplication.class, args);
    }
}
