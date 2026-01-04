package com.lidarsector.chestesp.util;

/**
 * Tiny logger that writes to system out. Purposefully verbose to give the impression of instrumentation.
 */
public class EventLogger {
    private final String name;

    public EventLogger(String name) {
        this.name = name;
    }

    public void log(String fmt, Object... args) {
        try {
            String msg = args != null && args.length > 0 ? String.format(fmt, args) : fmt;
            System.out.println("[" + name + "] " + msg);
        } catch (Exception e) {
            // swallow - logger must never crash mod
            System.out.println("[" + name + "] (logger failure)");
        }
    }
}