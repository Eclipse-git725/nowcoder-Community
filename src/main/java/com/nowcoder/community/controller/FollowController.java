package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.event.EventProducer;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.WebParam;

import java.util.List;
import java.util.Map;

import static com.nowcoder.community.util.CommunityConstant.ENTITY_TYPE_USER;

/**
 * 文件名称: FollowController.java
 * 作者: gxy
 * 创建日期: 2025/3/10
 * 描述: 关注和取消关注的Controller
 */
@Controller
public class FollowController implements CommunityConstant {

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = "/follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType, int entityId) {
        // System.out.println("entityType: " + entityType + " entityId: " + entityId);
        User user = hostHolder.getUser();
        followService.follow(user.getId(), entityType, entityId);
        // System.out.println("已关注！");

        // 触发关注事件
        Event event = new Event().setTopic(TOPIC_FOLLOW)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(entityType)
                .setEntityId(entityId)
                .setEntityUserId(entityId);
        eventProducer.fireEvent(event);

        return CommunityUtil.getJSONString(0, "已关注！");
    }

    @RequestMapping(path = "/unfollow", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(int entityType, int entityId) {
        // System.out.println("entityType: " + entityType + " entityId: " + entityId);
        User user = hostHolder.getUser();
        followService.unfollow(user.getId(), entityType, entityId);
        // System.out.println("已取消关注！");
        return CommunityUtil.getJSONString(0, "已取消关注！");
    }

    @RequestMapping(path = "/followees/{userId}", method = RequestMethod.GET)
    public String getFollowees(@PathVariable("userId") int userId, Model model, Page page) {
        User user = userService.findUserById(userId);
        if(user == null) {
            throw new RuntimeException("该用户不存在！");
        }
        model.addAttribute("user", user);
        //分页信息
        page.setLimit(5);
        page.setPath("/followees/" + userId);
        page.setRows((int) followService.findFolloweeCount(userId, ENTITY_TYPE_USER));

        // 关注列表
        List<Map<String,Object>> userList = followService.findFollowees(userId, page.getOffset(), page.getLimit());
        if(userList != null) {
            for(Map<String, Object> map : userList) {
                // 判断是否已关注
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users", userList);
        return "/site/followee";
    }

    @RequestMapping(path = "/followers/{userId}", method = RequestMethod.GET)
    public String getFollowers(@PathVariable("userId") int userId, Model model, Page page) {
        User user = userService.findUserById(userId);
        if(user == null) {
            throw new RuntimeException("该用户不存在！");
        }
        model.addAttribute("user", user);
        //分页信息
        page.setLimit(5);
        page.setPath("/followers/" + userId);
        page.setRows((int) followService.findFollowerCount(ENTITY_TYPE_USER, userId));

        // 关注列表
        List<Map<String,Object>> userList = followService.findFollowers(userId, page.getOffset(), page.getLimit());
        if(userList != null) {
            for(Map<String, Object> map : userList) {
                // 判断是否已关注
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users", userList);
        return "/site/follower";
    }

    public boolean hasFollowed(int userId) {
        if(hostHolder.getUser() == null) {
            return false;
        }
        return followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
    }

}
