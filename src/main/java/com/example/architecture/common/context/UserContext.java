package com.example.architecture.common.context;


public class UserContext {
    private static final ThreadLocal<Integer> CURRENT_USER = new ThreadLocal<>();

    public static void setUserId(Integer userId) {
        CURRENT_USER.set(userId);
    }

    public static Integer getUserId() {
        return CURRENT_USER.get();
    }

    public static void clear() {
        CURRENT_USER.remove();
    }

    public static ContextScope withUser(Integer userId) {
        setUserId(userId);
        return new ContextScope();
    }

    public static class ContextScope implements AutoCloseable {
        @Override
        public void close() {
            clear();
        }
    }
}
