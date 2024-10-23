package swp.koi.service.socketIoService;

import com.corundumstudio.socketio.AuthorizationResult;
import com.corundumstudio.socketio.SocketIOServer;
import io.socket.client.IO;
import io.socket.client.Socket;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import swp.koi.model.enums.TokenType;
import swp.koi.service.jwtService.JwtService;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * Configuration class for setting up and managing a Socket.IO server.
 * It initializes the server with the specified host and port from the application configuration,
 * handles client connections, and manages JWT authentication for clients.
 *
 * <p>This class also contains methods for handling client connection events
 * and stopping the server gracefully on application shutdown.</p>
 */

@RequiredArgsConstructor
@Slf4j
@Configuration
public class SocketIOConfig implements CommandLineRunner {

//    private final JwtService jwtService;
//    private final UserDetailsService userDetailsService;
//
//    // I have set the configuration values in application.yaml file
//    @Value("${socket.host}")
//    private String socketHost;
//
//    @Value("${socket.port}")
//    private int socketPort;
//
//    // SocketIOServer class is used to create a socket server
//    private SocketIOServer server;
//
//    /**
//     * Creates and configures a SocketIOServer bean.
//     *
//     * <p>This method sets up the server hostname and port,
//     * configures the authorization logic for incoming connections,
//     * and starts the server. It also adds listeners for client connection
//     * and disconnection events, logging relevant information.</p>
//     *
//     * @return the configured SocketIOServer instance
//     */
//    @Bean
//    public SocketIOServer socketIOServer() {
//        // Configuration object holds the server settings
//        Configuration config = new Configuration();
//
//        config.setHostname(socketHost);
//        config.setPort(socketPort);
//        try (InputStream keyStoreInputStream = new ClassPathResource("keystore.p12").getInputStream()) {
//            config.setKeyStore(keyStoreInputStream);
//            config.setKeyStorePassword("123123");
//            config.setKeyStoreFormat("PKCS12");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
////        config.setAuthorizationListener(auth -> {
////            var token = auth.getHttpHeaders().get("socket-token");
////            if (!token.isEmpty()) {
////                var username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
////                var account = userDetailsService.loadUserByUsername(username);
////                jwtService.validateToken(token, account, TokenType.ACCESS_TOKEN);
////                return new AuthorizationResult(true);
////            }
////
////            return new AuthorizationResult(true);
////        });
//
//        server = new SocketIOServer(config);
//        server.start();
//
//        server.addConnectListener(client -> log.info("Client connected: {}", client.getSessionId()));
//        server.addDisconnectListener(client -> log.info("Client disconnected: {}", client.getSessionId()));
//
//        return server;
//    }
//
//
//    /**
//     * Stops the Socket.IO server gracefully when the application is shutting down.
//     */
//    @PreDestroy
//    public void stopSocketServer() {
//        this.server.stop();
//    }

    private static final String SOCKET_IO_URL = "https://54.255.138.0:8081";

    private Socket socket;

    // Constructor injection for the Socket instance
    public Socket SocketIoConfig() throws URISyntaxException {
        this.socket = IO.socket(SOCKET_IO_URL);
        return this.socket;
    }

    @Bean
    public Socket socket() {
        return socket;
    }

    @Override
    public void run(String... args) throws Exception {
        // Log when connected to the Socket.IO server
        socket.on(Socket.EVENT_CONNECT, data -> {
            System.out.println("Connected to Socket.IO server.");
            // Emit a message or perform other actions here if needed
            socket.emit("event_name", "data");
        }).on("your_event_name", data -> {
            System.out.println("Received data: " + data[0]);
            // Handle the received data
        });

        // Log when there's a connection error
        socket.on(Socket.EVENT_CONNECT_ERROR, errorArgs -> {
            System.err.println("Connection error: " + errorArgs[0]);
        });

        // Log when disconnected from the server
        socket.on(Socket.EVENT_DISCONNECT, disconnectArgs -> {
            System.out.println("Disconnected from Socket.IO server. Reason: " + disconnectArgs[0]);
        });

        // Start the connection
        socket.connect();
    }

    @PreDestroy
    public void cleanup() {
        socket.disconnect();
        System.out.println("Disconnected from Socket.IO server.");
    }

}
