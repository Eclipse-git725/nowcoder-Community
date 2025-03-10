package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * 文件名称: FollowService.java
 * 作者: gxy
 * 创建日期: 2025/3/9
 * 描述: 关注业务
 */
@Service
public class FollowService {

    @Autowired
    private RedisTemplate redisTemplate;

    // 关注(当前用户id，关注的实体类型，关注的实体id)
    public void follow(int userId, int entityType, int entityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getPrefixFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getPrefixFollowerKey(entityType, entityId);

                operations.multi();

                // 某个用户关注的实体
                operations.opsForZSet().add(followeeKey, entityId, System.currentTimeMillis());
                // 某个实体拥有的粉丝
                operations.opsForZSet().add(followerKey, userId, System.currentTimeMillis());

                return operations.exec();
            }
        });
    }

    // 取消关注(当前用户id，关注的实体类型，关注的实体id)
    public void unfollow(int userId, int entityType, int entityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getPrefixFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getPrefixFollowerKey(entityType, entityId);

                operations.multi();

                // 某个用户关注的实体
                operations.opsForZSet().remove(followeeKey, entityId);
                // 某个实体拥有的粉丝
                operations.opsForZSet().remove(followerKey, userId);

                return operations.exec();
            }
        });
    }

    // 查询某个用户关注的实体的数量(关注了xxx人)
    public long findFolloweeCount(int userId, int entityType) {
        String followeeKey = RedisKeyUtil.getPrefixFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().zCard(followeeKey);
    }

    // 查询某个实体拥有的粉丝的数量(关注者xxx人)
    public long findFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getPrefixFollowerKey(entityType, entityId);
        return redisTemplate.opsForZSet().zCard(followerKey);
    }

    // 查询当前用户是否已关注该实体
    public boolean hasFollowed(int userId, int entityType, int entityId) {
        String followeeKey = RedisKeyUtil.getPrefixFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().score(followeeKey, entityId) != null;
    }

}
