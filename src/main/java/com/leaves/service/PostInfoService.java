package com.leaves.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.leaves.entity.Collect;
import com.leaves.entity.PostInfo;

import java.util.List;

public interface PostInfoService {
    /**
     * 分页查询
     *
     * @param postInfo
     * @param page
     * @param limit
     * @return
     */
    Page<PostInfo> selectPage(PostInfo postInfo, int page, int limit);

    /**
     * 新增
     *
     * @param postInfo
     * @return
     */
    boolean insert(PostInfo postInfo);

    /**
     * 删除
     */
    boolean delById(String id);

    /**
     * 编辑
     */
    boolean editById(PostInfo postInfo);

    /**
     * 根据id得到单个视频
     */
    PostInfo getOne(String id);

    /**
     * 根据父子分类id查询视频数目
     *
     * @param categoryId
     * @param chdCategoryId
     * @return
     */
    Integer getCount(String categoryId, String chdCategoryId);

    boolean addPageView(String id);

    List<PostInfo> getMyList();
    List<PostInfo> getList(String userId);
    List<PostInfo> getList();
    List<PostInfo> getListAll();

    /**
     * 收藏
     * @param videoId
     * @return
     */
    boolean insertCollect(String videoId);

    /**
     * 取消搜藏
     * @param videoId
     * @return
     */
    boolean delCollect(String videoId);
    /**
     * 判断当前用户是否收藏
     */
    boolean isCollect(String postId);
    /**
     * 得到收藏文章集合
     */
    List<Collect> getCollectList();
}
