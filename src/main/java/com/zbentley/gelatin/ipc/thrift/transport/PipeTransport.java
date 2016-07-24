package com.zbentley.gelatin.ipc.thrift.transport;

import org.apache.thrift.transport.TNonblockingTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.Pipe;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;


public class PipeTransport extends TNonblockingTransport {

    public Pipe getInPipe() {
        return inPipe_;
    }

    private Pipe inPipe_ = null;

    public Pipe getOutPipe() {
        return outPipe_;
    }

    private Pipe outPipe_ = null;

    public PipeTransport() {}

    public PipeTransport(Pipe in, Pipe out) throws TTransportException {
        inPipe_ = in;
        outPipe_ = out;
    }

    @Override
    public SelectionKey registerSelector(Selector selector, int interests) {
        try {
            if ( interests == SelectionKey.OP_WRITE ) {
                SelectionKey k =  outPipe_.sink().register(selector, SelectionKey.OP_WRITE);
                k.interestOps(SelectionKey.OP_WRITE);
                return outPipe_.sink().register(selector, SelectionKey.OP_WRITE);
            } else if ( interests == SelectionKey.OP_READ ) {
                SelectionKey k = inPipe_.source().register(selector, SelectionKey.OP_READ);
                k.interestOps(SelectionKey.OP_READ);
                return inPipe_.source().register(selector, SelectionKey.OP_READ);
            } else {
                System.out.println("WHAAAAAAT?" + interests);
            }

        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks whether the socket is connected.
     */
    public boolean isOpen() {
        // isConnected() does not return false after close(), but isOpen() does
        return inPipe_.sink().isOpen() && inPipe_.source().isOpen() && outPipe_.sink().isOpen() && outPipe_.source().isOpen();
    }

    /**
     * Do not call, the implementation provides its own lazy non-blocking connect.
     */
    public void open() throws TTransportException {
        try {
            if (inPipe_ == null) {
                inPipe_ = Pipe.open();
            }
            if (outPipe_ == null) {
                outPipe_ = Pipe.open();
            }

            inPipe_.sink().configureBlocking(false);
            inPipe_.source().configureBlocking(false);
            outPipe_.sink().configureBlocking(false);
            outPipe_.source().configureBlocking(false);

        } catch (IOException e) {
            throw new TTransportException(e);
        }
    }

    /**
     * Perform a nonblocking read into buffer.
     */
    public int read(ByteBuffer buffer) throws IOException {
        return inPipe_.source().read(buffer);
    }

    /**
     * Reads from the underlying input stream if not null.
     */
    public int read(byte[] buf, int off, int len) throws TTransportException {
        try {
            return inPipe_.source().read(ByteBuffer.wrap(buf, off, len));
        } catch (IOException e) {
            throw new TTransportException(e);
        }
    }

    /**
     * Perform a nonblocking write of the data in buffer;
     */
    public int write(ByteBuffer buffer) throws IOException {
        return outPipe_.sink().write(buffer);
    }

    /**
     * Writes to the underlying output stream if not null.
     */
    public void write(byte[] buf, int off, int len) throws TTransportException {
        try {
            this.write(ByteBuffer.wrap(buf, off, len));
        } catch (IOException e) {
            throw new TTransportException(e);
        }
    }

    /**
     * Closes the socket.
     */
    public void close() {
        System.out.println("CLOSING TRANSPORT");
//        try {
////            inPipe_.sink().close();
////            inPipe_.source().close();
////            outPipe_.sink().close();
////            outPipe_.source().close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public boolean startConnect() throws IOException {
        return true;
    }

    public boolean finishConnect() throws IOException {
        return true;
    }
}
