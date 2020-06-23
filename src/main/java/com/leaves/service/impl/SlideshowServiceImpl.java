package com.leaves.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.leaves.entity.Slideshow;
import com.leaves.service.SlideshowService;
import com.leaves.util.DateUtil;
import com.leaves.util.IntegrateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈轮播图相关操作service实现类〉<br>
 *
 * @author
 * @create 2019/8/15 17:53
 * @since 1.0.0
 */
@Service
public class SlideshowServiceImpl implements SlideshowService {
    @Resource
    private com.leaves.mapper.SlideshowMapper SlideshowMapper;
    @Resource
    private IntegrateUtils itdragonUtils;

    @Override
    public Page<Slideshow> selectPage(Slideshow Slideshow, int page, int limit) {
        EntityWrapper<com.leaves.entity.Slideshow> searchInfo = new EntityWrapper<>();
        Page<com.leaves.entity.Slideshow> pageInfo = new Page<>(page, limit);
        searchInfo.orderBy("creationTime desc");
        List<com.leaves.entity.Slideshow> resultList = SlideshowMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(Slideshow slideshow) {
        slideshow.setCreationTime(DateUtil.getNowDateSS());
        slideshow.setCreationUser(itdragonUtils.getSessionUser().getUserName());
        Integer insert = SlideshowMapper.insert(slideshow);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delById(String id) {
        Integer delete = SlideshowMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Slideshow> getList() {
        List<Slideshow> slideshows = SlideshowMapper.selectList(null);
        if (slideshows.isEmpty()) {
            return new ArrayList<>();
        }
        return slideshows;
    }
}