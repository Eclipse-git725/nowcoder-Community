package com.nowcoder.community.service;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nowcoder.community.dao.DiscussPostMapper;
import org.springframework.web.util.HtmlUtils;
import org.unbescape.html.HtmlEscape;

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

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit){
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    public int findDiscussPostsRows(int userId) {
        return discussPostMapper.selectDiscussPostsRows(userId);
    }

    // 增加帖子
    public int addDiscussPost(DiscussPost post) {
        if(post == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }

        // 将帖子中的 HTML 标记内容转义
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));

        // 过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        return discussPostMapper.insertDiscussPost(post);
    }

    // 根据帖子Id查询帖子
    public DiscussPost findDiscussPost(int id) {
        return discussPostMapper.selectDiscussPostById(id);
    }

    // 更新评论数
    public int updateCommentCount(int id, int commentCount) {
        return discussPostMapper.updateCommentCount(id, commentCount);
    }
}
