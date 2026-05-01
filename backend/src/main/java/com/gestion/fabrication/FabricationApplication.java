package com.gestion.fabrication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée principal de l'application.
 *
 * @SpringBootApplication : annotation "magique" qui active tout :
 *   - Détection automatique des @Controller, @Service, @Repository
 *   - Configuration automatique de Spring (datasource, JPA...)
 *   - Activation du scan des composants dans ce package
 */
@SpringBootApplication
public class FabricationApplication {

    public static void main(String[] args) {
        SpringApplication.run(FabricationApplication.class, args);

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  Application démarrée avec succès !      ║");
        System.out.println("║  API     → http://localhost:8080/api      ║");
        System.out.println("║  Swagger → http://localhost:8080/swagger-ui.html ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }
}
