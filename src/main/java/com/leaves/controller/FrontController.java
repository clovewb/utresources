package com.leaves.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.leaves.entity.*;
import com.leaves.service.*;
import com.leaves.util.IntegrateUtils;
import com.leaves.util.Result;
import com.leaves.util.ResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: leaves
 * @date: 2020年4月18日 上午10:09:18
 * @version: v1.0.0
 * @Description: 前台页面相关内容
 *
 */
@RequestMapping("/front")
@Controller
public class FrontController {
    private static final transient Logger log = LoggerFactory.getLogger(FrontController.class);
    @Autowired
    private SlideshowService slideshowService;
    @Autowired
    private WbeParameterService wbeParameterService;
    @Autowired
    private IntegrateUtils itdragonUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private NewsCategoryService newsCategoryService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private LeaveWordService leaveWordService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PostInfoService postInfoService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LogService logService;
    @Autowired
    private BrowseService browseService;

    /**
     * 推荐页面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/likeList.do")
    public ModelAndView likeList(ModelAndView mv) throws Exception {
        List<BrowseRecord> browseRecordList = browseService.getList();
        Date dt = new Date();
        SimpleDateFormat matter1 = new SimpleDateFormat("yyyyMMddhhMMss");
        String date = matter1.format(dt);
        String path = itdragonUtils.writeToD(date, browseRecordList);
        log.info("++++FrontController++++++likeList方法+++++path+"+path);
        List<String> slopeOneCF = itdragonUtils.getSlopeOneCF(itdragonUtils.getSessionUser().getId(), path, postInfoService.getList());
        log.info("++++FrontController++++++likeList方法+++++slopeOneCF+"+slopeOneCF.toString());
        log.info("++++FrontController++++++likeList方法+++++slopeOneCF+"+slopeOneCF.size());
        List<PostInfo> resultList = new ArrayList<>();
       /* if (slopeOneCF.isEmpty()) {
            //如果推荐集合为空
            mv.addObject("message", "由于您数据基数较少,暂时无法为您推荐");
            log.info("++++FrontController++++++likeList方法++++++由于您数据基数较少,暂时无法为您推荐");
        } else {
            mv.addObject("message", "yes");
            log.info("++++FrontController++++++likeList方法++++++推荐");
            for (String s : slopeOneCF) {
                log.info("++++FrontController++++++likeList方法+++s+++"+s.toString());
                resultList.add(postInfoService.getOne(s));
            }

        }
*/
        mv.addObject("message", "由于您数据基数较少,暂时无法为您推荐");
        for (PostInfo postInfo : resultList) {
            log.info("++++FrontController++++++likeList方法++++++resultList"+resultList.size());

            postInfo.setUser(userService.selectByPrimaryKey(postInfo.getUserId()));

            log.info("++++FrontController++++++likeList方法++++++resultList"+resultList.toString());
            log.info("++++FrontController++++++likeList方法++++++postInfo"+postInfo.getUserId());
            log.info("++++FrontController++++++likeList方法++++++postInfo"+postInfo.toString());


        }
        mv.addObject("resultList", resultList);
        CommonMethods("likeList", mv);
        mv.setViewName("/front/likeList");
        return mv;
    }

    /**
     * 前台首页跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView index(ModelAndView mv) {
        //首页轮播图集合
        List<Slideshow> slideshowList = slideshowService.getList();
        mv.addObject("slideshowList", slideshowList);
        CommonMethods("index", mv);
        //留言集合
        List<LeaveWord> leaveWordsList = leaveWordService.selectList();
        if (leaveWordsList.size() >= 6) {
            mv.addObject("leaveWordsList", leaveWordsList.subList(0, 6));
        } else {
            mv.addObject("leaveWordsList", leaveWordsList);
        }
        //发帖总榜
        Page<User> postNum = userService.getUserListOrderByPostNum(new User(), 1, 12);
        mv.addObject("postNumList", postNum.getRecords());
        //咨询集合
        Page<News> newsPage = newsService.selectPage(new News(), 1, 5);
        if (newsPage.getRecords().size() >= 6) {
            mv.addObject("newsList", newsPage.getRecords().subList(0, 6));
        } else {
            mv.addObject("newsList", newsPage.getRecords());
        }
        mv.setViewName("/front/index");
        return mv;
    }

    /**
     * 新闻资讯页面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/news.do")
    public ModelAndView news(ModelAndView mv, Integer page, Integer limit, String categoryId) {
        //系统默认当前页码,条数
        if (page == null) {
            page = 1;
        }
        if (limit == null) {
            limit = 5;
        }
        CommonMethods("news", mv);
        //获取新闻分类
        List<NewsCategory> categoryList = newsCategoryService.selectList();
        mv.addObject("newCategoryList", categoryList);
        News news = new News();
        news.setCategoryId(categoryId);
        //当前分页默认
        Page<News> selectPage = newsService.selectPage(news, page, limit);
        mv.addObject("newsPage", selectPage);
        //将前台传入的页数传回前台
        mv.addObject("page", page);
        //将前台传入的分类id传回前台
        mv.addObject("categoryId", categoryId);
        mv.setViewName("/front/news");
        return mv;
    }

    /**
     * 新闻详情页面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/newDetail.do")
    public ModelAndView newDetail(ModelAndView mv, String id) {
        News news = newsService.getOneById(id);
        mv.addObject("newsOne", news);
        CommonMethods("news", mv);
        mv.setViewName("/front/newDetail");
        return mv;
    }

    /**
     * 在线留言页面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/leaveWord.do")
    public ModelAndView leaveWord(ModelAndView mv) {
        CommonMethods("leaveWord", mv);
        //获取留言内容
        List<LeaveWord> leaveWordList = leaveWordService.selectList();
        for (LeaveWord leaveWord : leaveWordList) {
            //将留言下的回复存放入leaveWord对象中
            List<Answering> answerings = leaveWordService.selectListAnswering(leaveWord.getId());
            leaveWord.setAnsweringList(answerings);
            leaveWord.setCount(answerings.size());
            //拿到该留言的点赞数并且封装到likeCount属性中
            leaveWord.setLikeCount(leaveWordService.getLikeNumber(leaveWord.getId()));
        }
        mv.addObject("leaveWordList", leaveWordList);
        mv.setViewName("/front/leaveWord");
        return mv;
    }

    /**
     * 个人中心页面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/perCenter.do")
    public ModelAndView perCenter(ModelAndView mv) {
        CommonMethods("no", mv);
        //查询本人发布的资源
        List<PostInfo> myList = postInfoService.getMyList();
        for (PostInfo postInfo : myList) {
            postInfo.setCommentNoCount(commentService.getCommentNum(postInfo.getId()));
        }
        mv.addObject("myList", myList);
        mv.addObject("myListNum", myList.size());
        //查询本人收藏的资源
        List<Collect> collectList = postInfoService.getCollectList();
        mv.addObject("collectList", collectList);
        mv.addObject("collectNum", collectList.size());
        mv.setViewName("/front/userIndex");
        return mv;
    }

    /**
     * 安全设置页面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/security.do")
    public ModelAndView security(ModelAndView mv) {
        CommonMethods("no", mv);
        mv.setViewName("/front/security");
        return mv;
    }

    /**
     * 基本信息页面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/setUserInfo.do")
    public ModelAndView setUserInfo(ModelAndView mv) {
        CommonMethods("no", mv);
        mv.setViewName("/front/setUserInfo");
        return mv;
    }

    /**
     * 资源列表页面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/postInfoList.do")
    public ModelAndView postInfoList(ModelAndView mv, String categoryId) {
        List<ChdCategory> chdCategoryList = categoryService.selectChdCategoryList(categoryId);
        mv.addObject("chdCategoryList", chdCategoryList);
        //父分类信息
        Category category = categoryService.getCategory(categoryId);
        category.setPlate(categoryService.getByCategoryId(category.getId()));
        mv.addObject("category", category);
        mv.addObject("isJoin", categoryService.isJoin(
                itdragonUtils.getSessionUser().getUserName(), categoryService.getByCategoryId(categoryId).getId()));
        CommonMethods("postInfoList", mv);
        mv.setViewName("/front/postInfoList");
        return mv;
    }

    /**
     * 资源详情页面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/postInfoDetail.do")
    public ModelAndView postInfoDetail(ModelAndView mv, String id) {
        //增加浏览记录
        browseService.insert(itdragonUtils.getSessionUser().getId(), id);
        PostInfo postInfo = postInfoService.getOne(id);
        //资源浏览量加1
        postInfoService.addPageView(id);
        postInfo.setUser(userService.selectByPrimaryKey(postInfo.getUserId()));
        mv.addObject("postInfo", postInfo);
        mv.addObject("isBuyed", logService.isBuyed(id));
        CommonMethods("postInfoList", mv);
        //拿到该留言的点赞数并且封装到likeCount属性中
        postInfo.setLikeCount(leaveWordService.getLikeNumber(postInfo.getId()));
        List<Comment> commentList = commentService.getList(id);
        for (Comment comment : commentList) {
            boolean admin = userService.isAdmin(comment.getUserId());
            if (admin) {
                comment.setIsAdmin("1");
            } else {
                comment.setIsAdmin("0");
            }
            if (postInfo.getUserId().equals(comment.getUserId())) {
                comment.setIsMain("1");
            } else {
                comment.setIsMain("0");
            }
            if (postInfo.getUserId().equals(itdragonUtils.getSessionUser().getId())) {
                comment.setCanCai("1");
            } else {
                comment.setCanCai("0");
            }
            if (comment.getUserId().equals(itdragonUtils.getSessionUser().getId())) {
                comment.setIsMe("1");
            }
            comment.setUser(userService.selectByPrimaryKey(comment.getUserId()));
        }
        mv.addObject("isCollect", postInfoService.isCollect(id));
        mv.addObject("commentList", commentList);
        mv.setViewName("/front/postInfoDetail");
        return mv;
    }

    /**
     * 我的资源详情页面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/postInfoMyDetail.do")
    public ModelAndView postInfoMyDetail(ModelAndView mv, String id) {
        PostInfo postInfo = postInfoService.getOne(id);
        //资源浏览量加1
        postInfoService.addPageView(id);
        postInfo.setUser(userService.selectByPrimaryKey(postInfo.getUserId()));
        mv.addObject("postInfo", postInfo);
        mv.addObject("isBuyed", logService.isBuyed(id));
        CommonMethods("postInfoList", mv);
        //拿到该留言的点赞数并且封装到likeCount属性中
        postInfo.setLikeCount(leaveWordService.getLikeNumber(postInfo.getId()));
        List<Comment> commentList = commentService.getList(id);
        for (Comment comment : commentList) {
            boolean admin = userService.isAdmin(comment.getUserId());
            if (admin) {
                comment.setIsAdmin("1");
            } else {
                comment.setIsAdmin("0");
            }
            if (postInfo.getUserId().equals(comment.getUserId())) {
                comment.setIsMain("1");
            } else {
                comment.setIsMain("0");
            }
            if (postInfo.getUserId().equals(itdragonUtils.getSessionUser().getId())) {
                comment.setCanCai("1");
            } else {
                comment.setCanCai("0");
            }
            if (comment.getUserId().equals(itdragonUtils.getSessionUser().getId())) {
                comment.setIsMe("1");
            }
            comment.setUser(userService.selectByPrimaryKey(comment.getUserId()));
        }
        //将该资源的未读改为已读
        commentService.updateType(id);
        mv.addObject("isCollect", postInfoService.isCollect(id));
        mv.addObject("commentList", commentList);
        mv.setViewName("/front/postInfoDetail");
        return mv;
    }


    /**
     * 资源列表异步加载
     *
     * @param
     * @return
     */
    @ResponseBody
    @PostMapping("/postInfoAjax.do")
    public ResultResponse postInfoAjax(PostInfo postInfo, Integer page, Integer limit) {
        postInfo.setState("1");
        Page<PostInfo> productPage = postInfoService.selectPage(postInfo, page, limit);
        if (productPage.getRecords().isEmpty()) {
            return Result.resuleError("暂无数据");
        }
        return Result.resuleSuccess(productPage.getRecords(), "");
    }


    /**
     * 资源总数查询ajax
     *
     * @param
     * @return
     */
    @ResponseBody
    @PostMapping("/countPostAjax.do")
    public Integer countPostAjax(String categoryId, String chdCategoryId) {
        Integer count = postInfoService.getCount(categoryId, chdCategoryId);
        return count;
    }

    /**
     * 编辑资源界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/editPostInfo.do")
    public ModelAndView editPostInfo(ModelAndView mv, String id) {
        CommonMethods("no", mv);
        PostInfo postInfo = postInfoService.getOne(id);
        mv.addObject("postInfo", postInfo);
        mv.setViewName("front/editPostInfo");
        return mv;
    }

    /**
     * 新增资源界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/addPostInfo.do")
    public ModelAndView addPoatInfo(ModelAndView mv) {
        //获取资源父分类
        CommonMethods("no", mv);
        List<Category> categoryList = categoryService.selectList();
        mv.addObject("categoryList", categoryList);
        mv.setViewName("front/addPostInfo");
        return mv;
    }


    /**
     * 添加评论
     *
     * @param
     * @return
     */
    @ResponseBody
    @PostMapping("/addComment.do")
    public ResultResponse addComment(Comment comment) {
        boolean insert = commentService.insert(comment);
        if (!insert) {
            return Result.resuleError("失败");
        }
        //成共后资源评论数加一
        PostInfo postInfo = postInfoService.getOne(comment.getPostId());
        postInfo.setObserver(postInfo.getObserver() + 1);
        postInfoService.editById(postInfo);
        return Result.resuleSuccess();
    }

    /**
     * 下载资源记录
     *
     * @param
     * @return
     */
    @ResponseBody
    @PostMapping("/downloadInfo.do")
    public void downloadInfo() {
        logService.insert("下载资源");
    }

    /**
     * 删除评论
     *
     * @param
     * @return
     */
    @ResponseBody
    @PostMapping("/delComment.do")
    public ResultResponse delComment(String id) {
        boolean insert = commentService.delById(id);
        if (!insert) {
            return Result.resuleError("失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 收藏功能接口
     *
     * @param postId
     * @return
     */
    @ResponseBody
    @PostMapping("/collect.do")
    public ResultResponse collect(String postId) {
        boolean result = postInfoService.isCollect(postId);
        if (result) {
            postInfoService.delCollect(postId);
            return Result.resuleSuccess(null, "取消收藏成功");
        } else {
            postInfoService.insertCollect(postId);
            return Result.resuleSuccess(null, "收藏成功");
        }
    }


    /**
     * 个人界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/myHome.do")
    public ModelAndView myHome(ModelAndView mv) {
        Log log = new Log();
        CommonMethods("yes", mv);
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUser().getId());
        mv.addObject("user", user);
        List<Attention> attentionList = userService.getAttentionList(itdragonUtils.getSessionUser().getId());
        List<Attention> fenAttentionList = userService.getFenAttentionList(itdragonUtils.getSessionUser().getId());
        mv.addObject("attentionList", attentionList);
        mv.addObject("attentionListNum", attentionList.size());
        mv.addObject("fenAttentionList", fenAttentionList);
        mv.addObject("fenAttentionListNum", fenAttentionList.size());
        //最近回答
        List<Comment> commentList = commentService.getListByUserId(itdragonUtils.getSessionUser().getId());
        mv.addObject("commentList", commentList);
        mv.setViewName("front/myHome");

        log.setOperation("访问了" + user.getUserName() + "的主页!");
        Page<Log> logPage = logService.selectPage(log, 1, 100);
        mv.addObject("logList", logPage.getRecords());
        return mv;
    }

    /**
     * 他人界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/other.do")
    public ModelAndView other(ModelAndView mv, String userId) {
        CommonMethods("no", mv);
        User user = userService.selectByPrimaryKey(userId);
        mv.addObject("user", user);
        mv.addObject("isAttention", userService.isAttention(userId));
        List<Attention> attentionList = userService.getAttentionList(userId);
        List<Attention> fenAttentionList = userService.getFenAttentionList(userId);
        mv.addObject("attentionList", attentionList);
        mv.addObject("attentionListNum", attentionList.size());
        mv.addObject("fenAttentionList", fenAttentionList);
        mv.addObject("fenAttentionListNum", fenAttentionList.size());
        //最近回答
        List<Comment> commentList = commentService.getListByUserId(userId);
        mv.addObject("commentList", commentList);
        //查询他发布的资源
        List<PostInfo> postInfoList = postInfoService.getList(userId);
        mv.addObject("postInfoList", postInfoList);
        logService.insert("访问了" + user.getUserName() + "的主页!");
        mv.setViewName("front/other");
        return mv;
    }

    /**
     * 页面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/addressBook.do")
    public ModelAndView addressBook(ModelAndView mv, String userName) {
        CommonMethods("no", mv);
        List<User> list = userService.selectList(userName);
        mv.addObject("addressBookList", list);
        CommonMethods("addressBook", mv);
        mv.setViewName("/front/addressBook");
        return mv;
    }

    /**
     * 关注功能接口
     *
     * @param attenId
     * @return
     */
    @ResponseBody
    @PostMapping("/addFriend.do")
    public ResultResponse addFriend(String attenId) {
        boolean result = userService.isAttention(attenId);
        if (result) {
            userService.delAttention(attenId);
            return Result.resuleSuccess(null, "取消关注成功");
        } else {
            userService.insertAttention(attenId);
            return Result.resuleSuccess(null, "关注成功");
        }
    }


    /**
     * 前端页面有一些公用需求在这里抽取出来
     *
     * @param which 是选中哪一个页面顶部标签参数
     * @param mv
     */
    public void CommonMethods(String which, ModelAndView mv) {
        //判断用户是否登录
        boolean gogin = itdragonUtils.isGogin();
        boolean admin = false;
        boolean isUser = false;
        if (gogin) {
            //如果登录将用户信息放入前台
            mv.addObject("userInfo", userService.selectByPrimaryKey(itdragonUtils.getSessionUser().getId()));
            //并且判断是否是管理员
            admin = userService.isAdmin(itdragonUtils.getSessionUser().getId());
            isUser = userService.isUser(itdragonUtils.getSessionUser().getId());

        }
        mv.addObject("go_in", gogin);
        mv.addObject("admin", admin);
        mv.addObject("isUser", isUser);
        //网站参数
        WbeParameter wbeParameter = wbeParameterService.getWbeParameter();
        mv.addObject("wbeParameter", wbeParameter);

        //资源分类集合
        List<Category> categoryList = categoryService.selectList();
        mv.addObject("categoryList", categoryList);
        //查询贴子热议的前十个
        List<PostInfo> list = postInfoService.getList();
        if (list.size() <= 10) {
            mv.addObject("observerList", list);
        } else {
            mv.addObject("observerList", list.subList(0, 10));

        }
    }

    /**
     * 文化育人页面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/cultural.do")
    public ModelAndView notYetDevelopeds1(ModelAndView mv){
        CommonMethods("cultural", mv);
        mv.setViewName("/front/cultural");
        return mv;
    }

    /**
     * 社会服务页面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/society.do")
    public ModelAndView notYetDevelopeds2(ModelAndView mv){
        CommonMethods("society", mv);
        mv.setViewName("/front/society");
        return mv;
    }

}
