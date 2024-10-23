package swp.koi.service.socketIoService;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URISyntaxException;

@Component
public class SocketClient {

    private Socket socket;

    @PostConstruct
    public void init() {
        try {
            System.out.println("Attempting to connect to Socket.IO server...");
            socket = IO.socket("https://54.255.138.0:8081");

            // Log khi sự kiện connect xảy ra
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Connected to Socket.IO server!");
                }
            });

            // Log khi có sự kiện disconnect
            socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Disconnected from Socket.IO server.");
                }
            });

            // Log khi có lỗi kết nối
            socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Connection error: " + args[0]);
                }
            });

            // Kết nối đến server
            socket.connect();
        } catch (URISyntaxException e) {
            System.err.println("Invalid URI: " + e.getMessage());
        }
    }


    public Socket getSocket() {
        return socket;
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            System.out.println("New message: " + args[0]);
        }
    };

    @PreDestroy
    public void cleanup() {
        socket.disconnect();
        socket.off("message", onNewMessage);
    }
}
