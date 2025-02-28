package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * 文件名称: HostHolder.java
 * 作者: gxy
 * 创建日期: 2025/2/28
 * 描述: 持有用户的信息，用于代替session
 */
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUsers(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    // 清理数据
    // ThreadLocal 每次请求完要清除，否则会内存泄漏。
    public void clear() {
        users.remove();
    }
}
