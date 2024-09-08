package com.example.grpc_client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

@Configuration
public class Config {

    @Value("${server.ssl.key-store-password}")
    private String keyStorePass;

    @Value("${server.ssl.trust-store-password}")
    private String trustStorePass;

    @Bean
    public RestTemplate restTemplate() {
        SSLContext sslContext = configureSSLContext();
        return new RestTemplate(createRequestFactory(sslContext));
    }

    private SSLContext configureSSLContext() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");

            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            char[] keyStorePassword = keyStorePass.toCharArray();
            keyStore.load(new FileInputStream("C:\\Users\\FQ115RQ\\workspace\\java-projects\\grpc-client\\src\\main\\resources\\client.p12"), keyStorePassword);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keyStorePassword);

            KeyStore trustStore = KeyStore.getInstance("PKCS12");
            char[] trustStorePassword = trustStorePass.toCharArray();
            trustStore.load(new FileInputStream("C:\\Users\\FQ115RQ\\workspace\\java-projects\\grpc-client\\src\\main\\resources\\client-truststore.p12"), trustStorePassword);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            return sslContext;
        } catch (NoSuchAlgorithmException | KeyStoreException | CertificateException | IOException |
                 UnrecoverableKeyException | KeyManagementException e) {
            throw new RuntimeException("Error configuring SSLContext", e);
        }
    }


    private ClientHttpRequestFactory createRequestFactory(SSLContext sslContext) {
        return new CustomRequestFactory(sslContext);
    }

    private static class CustomRequestFactory extends SimpleClientHttpRequestFactory {

        private final SSLContext sslContext;

        public CustomRequestFactory(SSLContext sslContext) {
            this.sslContext = sslContext;
        }

        @Override
        protected void prepareConnection(java.net.HttpURLConnection connection, String httpMethod) throws IOException {
            if (connection instanceof javax.net.ssl.HttpsURLConnection) {
                ((javax.net.ssl.HttpsURLConnection) connection).setSSLSocketFactory(sslContext.getSocketFactory());
            }
            try {
                super.prepareConnection(connection, httpMethod);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
