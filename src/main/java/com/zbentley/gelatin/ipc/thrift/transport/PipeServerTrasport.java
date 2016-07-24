package com.zbentley.gelatin.ipc.thrift.transport;

import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.nio.channels.Selector;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

public class PipeServerTrasport extends TNonblockingServerTransport {

    private LinkedTransferQueue<PipeTransport> conns = null;

    public PipeServerTrasport() {
        conns = new LinkedTransferQueue<PipeTransport>();
    }
    @Override
    public void registerSelector(Selector selector) {
        System.out.println("Registering server selector");
    }

    @Override
    public void listen() throws TTransportException {}

    public void addConnection(PipeTransport conn) {
        conns.add(conn);
    }

    @Override
    public void close() {
        conns.clear();
    }

    @Override
    protected TTransport acceptImpl() throws TTransportException {
        try {
            PipeTransport conn = conns.poll(0, TimeUnit.MICROSECONDS);
            if ( conn == null ) {
                return null;
            } else {
                System.out.println("Accepting new connection");
                return new PipeTransport(conn.getOutPipe(), conn.getInPipe());
            }
        } catch (InterruptedException e) {
            throw new TTransportException(e);
        }
    }
}