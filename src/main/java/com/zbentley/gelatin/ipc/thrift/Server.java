package com.zbentley.gelatin.ipc.thrift;

import com.zbentley.gelatin.ipc.PersistenceImpl;
import gelatin.Persistence;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

// Generated code

public class Server implements Runnable {

    private Persistence.Processor processor;
    public TServer server;

    public Server(TServerTransport transport) throws TTransportException {
        PersistenceImpl handler = new PersistenceImpl();
        this.processor = new Persistence.Processor(handler);

        this.server = new TSimpleServer(new Args(transport).processor(this.processor));
    }

    public void run() {
        try {
            System.out.println("Server thread " + Thread.currentThread().getName() + " starting server...");
            // Use this for a multithreaded server
            // TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
            this.server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}