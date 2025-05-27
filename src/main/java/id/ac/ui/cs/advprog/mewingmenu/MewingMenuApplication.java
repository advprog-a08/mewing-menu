package id.ac.ui.cs.advprog.mewingmenu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class MewingMenuApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        System.setProperty("spring.datasource.url", dotenv.get("DATABASE_URL"));
        System.setProperty("spring.datasource.username", dotenv.get("DATABASE_USERNAME"));
        System.setProperty("spring.datasource.password", dotenv.get("DATABASE_PASSWORD"));

        System.setProperty("grpc.host", dotenv.get("SIGMA_AUTHENTICATION_GRPC_HOST"));
        System.setProperty("grpc.port", dotenv.get("SIGMA_AUTHENTICATION_GRPC_PORT"));

        System.out.println("Connecting to database at: " + System.getProperty("spring.datasource.url"));
        System.out.println("Connecting to gRPC at: " + System.getProperty("grpc.host") + ":" + System.getProperty("grpc.port"));
        System.out.println("Starting Mewing Menu Application...");
   
        SpringApplication.run(MewingMenuApplication.class, args);
    }

}
