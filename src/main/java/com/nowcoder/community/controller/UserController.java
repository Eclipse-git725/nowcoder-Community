package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 文件名称: UserController.java
 * 作者: gxy
 * 创建日期: 2025/2/28
 * 描述: 处理用户相关的请求
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    // 访问设置页面
    @LoginRequired
    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }
    // 上传图片
    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if(headerImage == null) {
            model.addAttribute("error", "您还没有选择图片！");
            return "/site/setting";
        }

        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件的格式不正确！");
            return "/site/setting";
        }

        // 生成随机文件名
        fileName = CommunityUtil.generateUUID() + suffix;
        // 确定文件的存放路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            headerImage.transferTo(dest);
        } catch (Exception e) {
            logger.error("上传文件失败：" + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常！", e);
        }
        // 更新当前用户头像的路径(web访问路径)
        // http://localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    // 获取头像
    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 服务器响应图片
        response.setContentType("image/" + suffix);

        try (
                OutputStream os = response.getOutputStream();   // 输出流
                FileInputStream fis = new FileInputStream(fileName);  // 输入流

        ) {
            byte[] buffer = new byte[1024];  // 缓冲区
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败：" + e.getMessage());
        }
    }

    // 修改密码
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public String updatePassword(String oldPassword, String newPassword, String confirmPassword, Model model) {
        User user = hostHolder.getUser();

        // 校验旧密码是否正确
        if(!userService.checkPassword(user.getId(), oldPassword)) {
            model.addAttribute("oldPasswordMsg", "旧密码不正确！");
            return "/site/setting";
        }

        // 校验新密码和确认密码是否一致
        if(StringUtils.isBlank(newPassword) || StringUtils.isBlank(confirmPassword)) {
            model.addAttribute("newPasswordMsg", "新密码和确认密码不能为空！");
            return "/site/setting";
        }

        if(newPassword.length() < 2) {
            model.addAttribute("newPasswordMsg", "密码长度不能少于8位！");
            return "/site/setting";
        }

        if(!newPassword.equals(confirmPassword)) {
            model.addAttribute("confirmPassword", "两次输入的密码不一致！");
            return "/site/setting";
        }

        // 更新密码
        userService.updatePassword(user.getId(), newPassword);
        model.addAttribute("msg", "密码修改成功！");
        return "redirect:/login";
    }

}
