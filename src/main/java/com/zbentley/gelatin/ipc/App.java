package com.zbentley.gelatin.ipc;

import com.zbentley.gelatin.ipc.thrift.Server;
import com.zbentley.gelatin.ipc.thrift.transport.PipeServerTrasport;
import com.zbentley.gelatin.ipc.thrift.transport.PipeTransport;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TTransportException;

import java.io.IOException;


public class App {
    public static void main(String [] args) throws InterruptedException, TTransportException, IOException {

        PipeTransport underlying = new PipeTransport();
        underlying.open();
        System.out.println(underlying.getInPipe().sink().isBlocking() + " < Blocking");
        System.out.println(underlying.getInPipe().sink().provider().getClass() + " < Prov");

        gelatin.Persistence.AsyncClient client = new gelatin.Persistence.AsyncClient(new TBinaryProtocol.Factory(), new TAsyncClientManager(), underlying);

        PipeServerTrasport serverTransport = new PipeServerTrasport();

        Server svr = new Server(serverTransport);

        System.out.println("Main thread '" + Thread.currentThread().getName() + "' starting server...");
        new Thread(svr).start();
        serverTransport.addConnection(underlying);
        System.out.println("Started server");

        try {
            client.getConfig(new AsyncMethodCallback() {
                public void onComplete(Object response) {
                    System.out.println(response);
                }

                public void onError(Exception exception) {
                    exception.printStackTrace();
                }
            });
            System.out.println("ping()");
//            client.getConfig(new AsyncMethodCallback() {
//                public void onComplete(Object response) {
//                    System.out.println(response);
//                }
//
//                public void onError(Exception exception) {
//                    System.out.println(exception);
//                }
//            });
//            System.out.println("ping()");
        } catch (TException e) {
            e.printStackTrace();
        }
    }
}