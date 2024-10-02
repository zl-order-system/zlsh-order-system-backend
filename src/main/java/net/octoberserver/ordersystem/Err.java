package net.octoberserver.ordersystem;

public record Err(String message, boolean error) {
    public static Err err(String msg) {
        return new Err(msg, true);
    }

    public static Err ok(String msg) {
        return new Err(msg, false);
    }
}
