package com.nowcoder.community.service;

import com.nowcoder.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nowcoder.community.dao.DiscussPostMapper;

import java.util.List;

/**
 * 文件名称: DiscussPostService.java
 * 作者: gxy
 * 创建日期: 2025/2/21
 * 描述: DicussPost的service层开发
 */
@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit){
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }
    public int findDiscussPostsRows(int userId) {
        return discussPostMapper.selectDiscussPostsRows(userId);
    }
}
