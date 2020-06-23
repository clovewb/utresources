package com.leaves.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.leaves.entity.*;
import com.leaves.service.*;
import com.leaves.util.IntegrateUtils;
import com.leaves.util.Result;
import com.leaves.util.ResultResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author: leaves
 * @date: 2020年4月11日 下午15:13:14
 * @version: v1.0.0
 * @Description: 内容
 */
@Controller
public class IndexController {
    private static final transient Logger log = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private IntegrateUtils itdragonUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private LogService logService;
    @Autowired
    private WbeParameterService wbeParameterService;
    @Autowired
    private PostInfoService postInfoService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/indexWbe")
    public ModelAndView index(ModelAndView mv, Model model) {
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUser().getId());
        List<Menu> menuList = permissionService.getMenuInfoByUserId(user.getId());
        JSONArray jsonArray = new JSONArray();
        for (Menu menu : menuList) {
            JSONObject menuMap = new JSONObject();
            menuMap.put("F_ModuleId", menu.getMenuId());
            menuMap.put("F_ParentId", menu.getPerentMenuId());
            menuMap.put("F_FullName", menu.getMenuName());
            menuMap.put("F_UrlAddress", menu.getMenuURL());
            jsonArray.add(menuMap);
        }
        model.addAttribute("menuInfo", jsonArray);
        User userByUserName = userService.findUserByUserName(user.getUserName());
        List<Role> roleList = userByUserName.getRoleList();
        if (roleList.isEmpty()) {
            model.addAttribute("role", "暂无身份");
        } else {
            model.addAttribute("role", roleList.get(0).getRole());
        }
        model.addAttribute("user", user);
        mv.setViewName("content/index");
        return mv;
    }

    /**
     * 后台管理系统登录页面
     *
     * @param mv
     * @return
     */
    @RequestMapping("/loginWbe")
    public ModelAndView login(ModelAndView mv) {
        WbeParameter wbeParameter = wbeParameterService.getWbeParameter();
        mv.addObject("wbeParameter", wbeParameter);
        mv.setViewName("/login");
        return mv;
    }

    /**
     * 前台登录页面
     *
     * @param mv
     * @return
     */
    @RequestMapping("/loginNormalWbe")
    public ModelAndView loginNormal(ModelAndView mv) {
        WbeParameter wbeParameter = wbeParameterService.getWbeParameter();
        mv.addObject("wbeParameter", wbeParameter);
        mv.setViewName("/loginNormal");
        return mv;
    }

    /**
     * Shiro登录跳转地址,重定向到登录页面
     *
     * @param mv
     * @return
     */
    @RequestMapping("/loginShiro")
    public String loginShiro(ModelAndView mv) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()) {
            if (userService.isAdmin(itdragonUtils.getSessionUser().getId())) {
                return "forward:/indexWbe";
            }
            return "forward:/front/index.do";
        } else {
            return "forward:/loginNormalWbe";
        }
    }

    /**
     * Shiro登录成功后index跳转地址,重定向到indexWbe页面
     *
     * @param mv
     * @return
     */
    @RequestMapping("/indexShiro")
    public String indexShiro(ModelAndView mv) {
       /* if (userService.isAdmin(itdragonUtils.getSessionUser().getId())) {
            return "forward:/indexWbe";
        }*/
        return "forward:/front/index.do";
    }

    /**
     * 修改密码页面跳转
     *
     * @param mv
     * @return
     */
    @GetMapping("/changePwd")
    public ModelAndView changePwd(ModelAndView mv) {
        User user = (User) itdragonUtils.getShiroSession().getAttribute("loginUserInfo");
        mv.addObject("user", user);
        mv.setViewName("content/changePwd");
        return mv;
    }

    /**
     * 用户修改密码
     *
     * @param newPas
     * @param oldPas
     * @param userName
     * @return
     */
    @ResponseBody
    @PostMapping("/updatePas")
    public ResultResponse updatePassword(String newPas, String oldPas, String userName) {
        User user = (User) itdragonUtils.getShiroSession().getAttribute("loginUserInfo");
        if (oldPas.equals(user.getPlainPassword())) {
            boolean result = userService.changePass(newPas, userName);
            if (result) {
                user.setPlainPassword(newPas);
                return Result.resuleSuccess();
            } else {
                return Result.resuleError("修改失败");
            }

        }
        return Result.resuleError("原密码错误,请重新输入");


    }


    /**
     * 用户中心
     *
     * @param mv
     * @return
     */
    @GetMapping("/userInfo")
    public ModelAndView userInfo(ModelAndView mv) {
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUser().getId());
        if (!IntegrateUtils.stringIsNotBlack(user.getImgUrl())) {
            user.setImgUrl("/resource/image/default.png");
        }
        mv.addObject("user", user);
        mv.setViewName("content/userInfo");
        return mv;
    }

    /**
     * 首页
     *
     * @param mv
     * @return
     */
    @GetMapping("/main")
    public ModelAndView main(ModelAndView mv) {
        WbeParameter wbeParameter = wbeParameterService.getWbeParameter();
        mv.addObject("wbeParameter", wbeParameter);
        Log log = new Log();
        log.setOperation("下载");
        Page<Log> logPage = logService.selectPage(log, 1, 1000);
        mv.addObject("logNum", logPage.getRecords().size());
        List<Comment> commentList = commentService.getList();
        mv.addObject("commentNum", commentList.size());
        List<PostInfo> postInfoList = postInfoService.getList();
        mv.addObject("postInfoNum", postInfoList.size());
        Page<User> userList = userService.getUserList(new User(), 1, 1000);
        mv.addObject("userNum", userList.getRecords().size());
        mv.setViewName("/wbeSet/wbeParameter/index");
        return mv;
    }

    /**
     * 404页面
     *
     * @param mv
     * @return
     */
    @GetMapping("/404")
    public ModelAndView silingsi(ModelAndView mv) {
        mv.setViewName("content/404");
        return mv;
    }


    /**
     * 注册页面跳转
     */
    @RequestMapping("/reg")
    public ModelAndView reg(ModelAndView mv) {
        WbeParameter wbeParameter = wbeParameterService.getWbeParameter();
        mv.addObject("wbeParameter", wbeParameter);
        mv.setViewName("regs");
        return mv;
    }

    /**
     * 登录页面跳转
     */
    @RequestMapping("/login")
    public ModelAndView login1(ModelAndView mv) {
        WbeParameter wbeParameter = wbeParameterService.getWbeParameter();
        mv.addObject("wbeParameter", wbeParameter);
        mv.setViewName("login");
        return mv;
    }

    /**
     * 忘记密码页面
     */
    @RequestMapping("/retrieve")
    public ModelAndView retrieve(ModelAndView mv) {
        mv.setViewName("/front/retrieve");
        return mv;
    }

//    /**
//     * 找回密码
//     *
//     * @param toEmail
//     * @return
//     */
//    @ResponseBody
//    @PostMapping("/retrievePass")
//    public ResultResponse retrievePass(String toEmail, String userName) throws Exception {
//        User userByUserName = userService.getUserByUserName(userName);
//        if (!toEmail.equals(userByUserName.getEmail())) {
//            return Result.resuleError("用户名和邮箱不符!");
//        }
//        try {
//            final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
//            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
//            message.setFrom("1156326165@qq.com");
//            message.setTo(toEmail);
//            message.setSubject("找回密码");
//            message.setText("您的密码是:" + userByUserName.getPassword());
//            this.mailSender.send(mimeMessage);
//            return Result.resuleSuccess();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return Result.resuleError("发送失败");
//        }
//    }


}
