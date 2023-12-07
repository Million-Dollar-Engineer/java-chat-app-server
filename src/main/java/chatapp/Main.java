package chatapp;

import chatapp.internal.logger.AppLogger;
import chatapp.socket.ServerSocketComponent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class Main implements CommandLineRunner {
    private static final Logger logger = AppLogger.getLogger();

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ServerSocketComponent server = new ServerSocketComponent();
        server.run();
    }

}
