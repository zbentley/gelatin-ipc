package com.zbentley.gelatin.ipc;

import org.freedesktop.dbus.DBusInterface;

public interface IPCReceiver extends DBusInterface {

    public String testMethod(String s1, String s2);
}
