package id.ac.ui.cs.advprog.mewingmenu.grpc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import table_session.TableSessionOuterClass;
import table_session.TableSessionServiceGrpc;

@Service
public class TableSessionGrpcClient {

    private final TableSessionServiceGrpc.TableSessionServiceBlockingStub stub;

    public TableSessionGrpcClient(
            @Value("${grpc.host}") String host,
            @Value("${grpc.port}") int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        stub = TableSessionServiceGrpc.newBlockingStub(channel);
    }

    public TableSessionOuterClass.TableSessionResponse verifyTableSession(String sessionId) {
        TableSessionOuterClass.SessionIdRequest request = TableSessionOuterClass.SessionIdRequest.newBuilder()
                .setSessionId(sessionId)
                .build();
        return stub.verifyTableSession(request);
    }
}
