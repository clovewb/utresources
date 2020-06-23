package com.leaves.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.leaves.entity.Collect;
import com.leaves.entity.Comment;
import com.leaves.entity.PostInfo;
import com.leaves.entity.User;
import com.leaves.mapper.*;
import com.leaves.service.PostInfoService;
import com.leaves.util.DateUtil;
import com.leaves.util.IntegrateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PostInfoServiceImpl implements PostInfoService {
    @Resource
    private PostInfoMapper postInfoMapper;
    @Resource
    private IntegrateUtils itdragonUtils;
    @Resource
    private ChdCategoryMapper chdCategoryMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private CollectMapper collectMapper;

    @Override
    public Page<PostInfo> selectPage(PostInfo postInfo, int page, int limit) {
        EntityWrapper<PostInfo> searchInfo = new EntityWrapper<>();
        if (IntegrateUtils.stringIsNotBlack(postInfo.getName())) {
            searchInfo.like("name", postInfo.getName());
        }
        if (IntegrateUtils.stringIsNotBlack(postInfo.getCategoryId())) {
            searchInfo.eq("categoryId", postInfo.getCategoryId());
        }
        if (IntegrateUtils.stringIsNotBlack(postInfo.getChdCategoryId())) {
            searchInfo.eq("chdCategoryId", postInfo.getChdCategoryId());
        }
        if (IntegrateUtils.stringIsNotBlack(postInfo.getOrderParam())) {
            searchInfo.orderBy(postInfo.getOrderParam());
        }
        if (IntegrateUtils.stringIsNotBlack(postInfo.getState())) {
            searchInfo.eq("state", postInfo.getState());
        }
        if (IntegrateUtils.stringIsNotBlack(postInfo.getCreateTime())) {
            searchInfo.between("createTime", postInfo.getCreateTime().split(" ~ ")[0], postInfo.getCreateTime().split(" ~ ")[1]);
        }
        if (IntegrateUtils.stringIsNotBlack(postInfo.getSearchParam())) {
            searchInfo.like("content", postInfo.getSearchParam());
            searchInfo.or().like("userName", postInfo.getSearchParam());
            searchInfo.or().like("cag", postInfo.getSearchParam());
            searchInfo.or().like("faculty", postInfo.getSearchParam());
            searchInfo.or().like("introduce", postInfo.getSearchParam()).clone();
        }
        searchInfo.orderBy("isTop desc ,createTime desc");
        Page<PostInfo> pageInfo = new Page<>(page, limit);
        List<PostInfo> resultList = postInfoMapper.selectPage(pageInfo, searchInfo);
        for (PostInfo info : resultList) {
            info.setUser(userMapper.selectById(info.getUserId()));
        }
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(PostInfo postInfo) {
        postInfo.setCreateTime(DateUtil.getNowDateSS());
        postInfo.setPageView(0);
        postInfo.setObserver(0);
        postInfo.setIsTop(0);
        postInfo.setState("0");
        User user = userMapper.selectById(itdragonUtils.getSessionUser().getId());
        postInfo.setUserId(user.getId());
        postInfo.setUserName(user.getUserName());
        postInfo.setFaculty(user.getBlance());
        postInfo.setCag(user.getUserRank());
        postInfo.setChdCategoryName(chdCategoryMapper.selectById(postInfo.getChdCategoryId()).getName());
        Integer insert = postInfoMapper.insert(postInfo);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delById(String id) {
        Integer delete = postInfoMapper.deleteById(id);
        if (delete > 0) {
            //删除视频后删除视频下的评论
            EntityWrapper<Comment> wrapper = new EntityWrapper<>();
            wrapper.eq("postId", id);
            commentMapper.delete(wrapper);
            //删除视频后删除视频下的收藏
            EntityWrapper<Collect> wrapper1 = new EntityWrapper<>();
            wrapper.eq("postId", id);
            collectMapper.delete(wrapper1);
            return true;
        }
        return false;
    }

    @Override
    public boolean editById(PostInfo postInfo) {
        Integer update = postInfoMapper.updateById(postInfo);
        if (update > 0) {
            return true;
        }
        return false;
    }

    @Override
    public PostInfo getOne(String id) {
        return postInfoMapper.selectById(id);
    }

    @Override
    public Integer getCount(String categoryId, String chdCategoryId) {
        EntityWrapper<PostInfo> wrapper = new EntityWrapper<>();
        if (IntegrateUtils.stringIsNotBlack(categoryId)) {
            wrapper.eq("categoryId", categoryId);
        }
        if (IntegrateUtils.stringIsNotBlack(chdCategoryId)) {
            wrapper.eq("chdCategoryId", chdCategoryId);
        }
        List<PostInfo> resultList = postInfoMapper.selectList(wrapper);
        return resultList.size();
    }

    @Override
    public boolean addPageView(String id) {
        PostInfo postInfo = postInfoMapper.selectById(id);
        postInfo.setPageView(postInfo.getPageView() + 1);
        Integer integer = postInfoMapper.updateById(postInfo);
        if (integer > 0) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public List<PostInfo> getMyList() {
        EntityWrapper<PostInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("userId", itdragonUtils.getSessionUser().getId());
        wrapper.eq("state", "1");
        return postInfoMapper.selectList(wrapper);

    }

    @Override
    public List<PostInfo> getList(String userId) {
        EntityWrapper<PostInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("userId", userId);
        wrapper.eq("state", "1");
        return postInfoMapper.selectList(wrapper);
    }

    @Override
    public List<PostInfo> getList() {
        EntityWrapper<PostInfo> wrapper = new EntityWrapper<>();
        wrapper.orderBy("observer desc");
        wrapper.eq("state", "1");
        List<PostInfo> postInfos = postInfoMapper.selectList(wrapper);
        for (PostInfo postInfo : postInfos) {
            postInfo.setUser(userMapper.selectById(postInfo.getUserId()));

        }
        return postInfos;
    }

    @Override
    public List<PostInfo> getListAll() {
        EntityWrapper<PostInfo> wrapper = new EntityWrapper<>();
        List<PostInfo> postInfos = postInfoMapper.selectList(wrapper);
        return postInfos;
    }

    @Override
    public boolean insertCollect(String postId) {
        Collect collect = new Collect();
        collect.setUserId(itdragonUtils.getSessionUser().getId());
        collect.setPostId(postId);
        Integer insert = collectMapper.insert(collect);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delCollect(String postId) {
        EntityWrapper<Collect> wrapper = new EntityWrapper<>();
        wrapper.eq("postId", postId);
        wrapper.eq("userId", itdragonUtils.getSessionUser().getId());
        Integer delete = collectMapper.delete(wrapper);
        if (delete > 0) {
            return false;
        }
        return false;
    }

    @Override
    public boolean isCollect(String postId) {
        Collect collect = new Collect();
        collect.setPostId(postId);
        collect.setUserId(itdragonUtils.getSessionUser().getId());
        Collect one = collectMapper.selectOne(collect);
        if (one != null) {
            return true;
        }
        return false;
    }

    @Override
    public List<Collect> getCollectList() {
        EntityWrapper<Collect> wrapper = new EntityWrapper<>();
        wrapper.eq("userId", itdragonUtils.getSessionUser().getId());
        List<Collect> collectList = collectMapper.selectList(wrapper);
        for (Collect collect : collectList) {
            collect.setPostInfo(postInfoMapper.selectById(collect.getPostId()));
        }
        return collectList;
    }
}
