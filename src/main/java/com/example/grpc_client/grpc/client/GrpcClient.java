package com.example.grpc_client.grpc.client;

import org.springframework.stereotype.Service;

@Service
public class GrpcClient {

    @net.devh.boot.grpc.client.inject.GrpcClient("hello-service")
    private com.example.grpc_mtls.HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub;

    public com.example.grpc_mtls.HelloResponse testingGRPC() {
        com.example.grpc_mtls.HelloRequest helloRequest = com.example.grpc_mtls.HelloRequest
                .newBuilder()
                .setFirstName("Sandeep")
                .setLastName("Vijayakumar")
                .build();
        return helloServiceBlockingStub.hello(helloRequest);

    }


}
