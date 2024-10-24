package swp.koi.service.socketIoService;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class SSLTrust {

    private String keystorePath;
    private String keystorePassword;
    private String url;

    public SSLTrust(String keystorePath, String keystorePassword, String url) {
        this.keystorePath = keystorePath;
        this.keystorePassword = keystorePassword;
        this.url = url;
        setupSSL();
    }

    private void setupSSL() {
        try {
            // Tải keystore
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (InputStream keyStoreStream = getClass().getResourceAsStream(keystorePath)) {
                keyStore.load(keyStoreStream, keystorePassword.toCharArray());
            }

            // Tạo TrustManager
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            // Tạo SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            // Thiết lập SSLContext cho HttpsURLConnection
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public int sendGetRequest() {
        int responseCode = -1;
        try {
            // Thực hiện yêu cầu HTTPS
            URL urlObj = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) urlObj.openConnection();
            connection.setRequestMethod("GET");

            responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Xử lý phản hồi ở đây...
            // Ví dụ: đọc phản hồi từ InputStream

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseCode;
    }
}
