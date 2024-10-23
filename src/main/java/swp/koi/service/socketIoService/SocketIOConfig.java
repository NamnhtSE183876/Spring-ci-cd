package swp.koi.service.socketIoService;

import io.socket.client.IO;
import io.socket.client.Socket;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

/**
 * Configuration class for setting up and managing a Socket.IO server.
 * It initializes the server with the specified host and port from the application configuration,
 * handles client connections, and manages JWT authentication for clients.
 *
 * <p>This class also contains methods for handling client connection events
 * and stopping the server gracefully on application shutdown.</p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SocketIOConfig {

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

    private Socket socket;

    @Bean
    public Socket socketIOServer() {
        try {
            // Kết nối đến server Socket.IO với HTTPS
            socket = IO.socket("https://socketio:8081"); // Sử dụng HTTPS
            socket.on(Socket.EVENT_CONNECT, obj -> {
                log.info("Connected to Socket.IO server");
            });
            socket.connect();
        } catch (URISyntaxException e) {
            log.error("Error connecting to Socket.IO server: {}", e.getMessage());
        }
        return socket; // Trả về đối tượng Socket
    }

    public Socket getSocket() {
        return socket; // Getter để truy cập socket từ nơi khác trong ứng dụng
    }

    @PreDestroy
    public void disconnect() {
        if (socket != null) {
            socket.disconnect();
            log.info("Disconnected from Socket.IO server");
        }
    }
}
