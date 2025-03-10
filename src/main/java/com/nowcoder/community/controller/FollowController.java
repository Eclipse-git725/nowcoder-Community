package com.nowcoder.community.controller;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 文件名称: FollowController.java
 * 作者: gxy
 * 创建日期: 2025/3/10
 * 描述: 关注和取消关注的Controller
 */
@Controller
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(value = "/follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType, int entityId) {
        System.out.println("entityType: " + entityType + " entityId: " + entityId);
        User user = hostHolder.getUser();
        followService.follow(user.getId(), entityType, entityId);
        System.out.println("已关注！");
        return CommunityUtil.getJSONString(0, "已关注！");
    }

    @RequestMapping(value = "/unfollow", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(int entityType, int entityId) {
        System.out.println("entityType: " + entityType + " entityId: " + entityId);
        User user = hostHolder.getUser();
        followService.unfollow(user.getId(), entityType, entityId);
        System.out.println("已取消关注！");
        return CommunityUtil.getJSONString(0, "已取消关注！");
    }

}
