package com.example.grpc_client.api;

import com.example.grpc_client.grpc.client.GrpcClient;
import com.example.grpc_client.service.ClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientAPI {

    private ClientService clientService;

    private GrpcClient grpcClient;

    public ClientAPI(ClientService clientService, GrpcClient grpcClient) {
        this.clientService = clientService;
        this.grpcClient = grpcClient;
    }

    @PostMapping("/client/hello")
    public String hello() {
        return clientService.callServer();
    }

    @PostMapping("client/grpc/hello")
    public String helloGrpc() {
        com.example.grpc_mtls.HelloResponse helloResponse = grpcClient.testingGRPC();
        return helloResponse.getGreeting();
    }

}
