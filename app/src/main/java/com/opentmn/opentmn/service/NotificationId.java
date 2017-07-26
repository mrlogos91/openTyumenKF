package com.opentmn.opentmn.service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kost on 25.01.17.
 */

public class NotificationId {
    private final static AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        return c.incrementAndGet();
    }
}