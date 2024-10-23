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
            socket = IO.socket("http://54.255.138.0:8081");
            socket.on("message", onNewMessage);
            socket.connect();

            // Kiểm tra kết nối
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Connected to Socket.IO server!");
                }
            });

            // Xử lý ngắt kết nối
            socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Disconnected from Socket.IO server.");
                }
            });

        } catch (URISyntaxException e) {
            e.printStackTrace();
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
