package com.zbentley.gelatin.ipc;

/**
 * Created by zbentley on 2/9/16.
 */
public interface Persistence {
    Message Poll(int timeout) throws TimeoutExecption, ServiceNotStartedException;
    ArrayList<Message> Poll(int count, int timeout) throws TimeoutException, ServiceNotStartedException;
    // Returns up to 'count' message
    ArrayList<Message> FlexiblePoll(int count, int timeout) throws ServiceNotStartedException;

    Message Pop(int timeout) throws TimeoutException, ServiceNotStartedException;
    ArrayList<Message> Pop(int count, int timeout) throws TimeoutException, ServiceNotStartedException;
    // Returns and removes up to 'count' messages
    ArrayList<Message> FlexiblePop(int count, int timeout) throws ServiceNotStartedException;

    int getCount() throws ServiceNotStartedException;
    int setCount(int count) throws ServiceNotStartedException;
    void Initialize() throws ServiceStartupException;

    default ArrayList<Message> FlexiblePollNonblock(int count) throws ServiceNotStartedException {
        return this.FlexiblePoll(count, com.zbentley.gelatin.ipc.timeout.IMMEDIATE);
    }

    default ArrayList<Message> FlexiblePoll(int count) throws ServiceNotStartedException {
        return this.FlexiblePoll(count, com.zbentley.gelatin.ipc.timeout.INFINITE);
    }

    default ArrayList<Message> NonblockingFlexiblePop(int count) throws ServiceNotStartedException {
        return this.FlexiblePop(count, com.zbentley.gelatin.ipc.timeout.IMMEDIATE);
    }

    default ArrayList<Message> BlockingFlexiblePop(int count) throws ServiceNotStartedException {
        return this.FlexiblePop(count, com.zbentley.gelatin.ipc.timeout.INFINITE);
    }

    default void Start() throws ServiceStartupException {
        this.Initialize();
        this.setCount(this.getCount());
    }
}