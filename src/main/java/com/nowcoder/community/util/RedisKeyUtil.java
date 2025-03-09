package com.nowcoder.community.util;

/**
 * 文件名称: RedisKeyUtil.java
 * 作者: gxy
 * 创建日期: 2025/3/9
 * 描述: Redis的Key生成器
 */
public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";

    // 某个实体的赞
    // like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }
}
