package com.dbts.dreambacktosong_backend.security;

/**
 * 当前登录用户上下文
 *
 * <p>通过 ThreadLocal 保存每次请求解析出的用户信息，便于在 Service 层中直接获取。
 */
public class UserContext {

    private static final ThreadLocal<UserInfo> USER_HOLDER = new ThreadLocal<>();

    public static void setUser(UserInfo userInfo) {
        USER_HOLDER.set(userInfo);
    }

    public static UserInfo getUser() {
        return USER_HOLDER.get();
    }

    public static Long getUserId() {
        UserInfo info = USER_HOLDER.get();
        return info != null ? info.getId() : null;
    }

    public static String getPhone() {
        UserInfo info = USER_HOLDER.get();
        return info != null ? info.getPhone() : null;
    }

    public static void clear() {
        USER_HOLDER.remove();
    }

    /**
     * 简化的用户信息载体
     */
    public record UserInfo(Long id, String phone) {
        public Long getId() {
            return id;
        }

        public String getPhone() {
            return phone;
        }
    }
}

