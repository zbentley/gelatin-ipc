package com.zbentley.gelatin.ipc;

import gelatin.PersistenceException;
import gelatin.PersistenceResult;
import gelatin.ServiceException;
import gelatin.ServiceRunningException;
import message.Message;
import org.apache.thrift.TException;
import rpc.RPCException;
import rpc.RPCResult;

import com.google.common.collect.ImmutableMap;

public class PersistenceImpl implements gelatin.Persistence.Iface {
    public ImmutableMap<String, String> getConfig() throws TException {
        System.out.println("FOOO " + Thread.currentThread().getName());

        return new ImmutableMap.Builder<String, String>()
                .put("one", "1")
                .put("two", "2")
                .put("three", "3")
                .build();
    }

    public boolean start() throws ServiceException, TException {
        return false;
    }

    public boolean stop() throws ServiceException, TException {
        return false;
    }

    public PersistenceResult persist(Message message) throws PersistenceException, ServiceException, TException {
        return null;
    }

    public RPCResult persistRPC(Message message, boolean durableReply) throws PersistenceException, ServiceException, RPCException, TException {
        return null;
    }

    public RPCResult flushPendingRPC(boolean continueOnError) throws ServiceException, ServiceRunningException, RPCException, TException {
        return null;
    }
}
