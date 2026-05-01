package com.lmy.withning.utils;

import com.lmy.withning.pojo.entity.User;

public class UserContextHolder {
    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static void setUser(User user) {
        userThreadLocal.set(user);
    }

    public static User getUser() {
        return userThreadLocal.get();
    }

    public static void remove() {
        userThreadLocal.remove();
    }
}
