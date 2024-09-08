package com.example.grpc_client.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClientService {

    private RestTemplate restTemplate;

    @Value("${server.app.url}")
    private String serverUrl;

    public ClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String callServer() {
        String response = restTemplate.getForObject(serverUrl, String.class);
        return response;
    }
}
