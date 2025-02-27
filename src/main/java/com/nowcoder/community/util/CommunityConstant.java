package com.nowcoder.community.util;

/**
 * 文件名称: CommunityConstant.java
 * 作者: gxy
 * 创建日期: 2025/2/26
 * 描述: 常量接口
 */
public interface CommunityConstant{

    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;

    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * 默认状态的登录凭证的超时时间
     */
    int DEFAULT_EXPIRED_SECOND = 3600 * 12;

    /**
     * 记住状态的登录凭证超时时间
     */
    int REMEMBER_EXPIRED_SECOND = 3600 * 24 * 100;
}
