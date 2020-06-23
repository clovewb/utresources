package com.leaves.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.leaves.entity.BrowseRecord;
import com.leaves.mapper.BrowseMapper;
import com.leaves.service.BrowseService;
import com.leaves.util.IntegrateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BrowseServiceImpl implements BrowseService {
    @Resource
    private IntegrateUtils itdragonUtils;
    @Resource
    private BrowseMapper browseMapper;


    @Override
    public boolean insert(String userId, String productId) {
        //首先查询该用户是否对此已有浏览记录
        EntityWrapper<BrowseRecord> wrapper = new EntityWrapper<>();
        wrapper.eq("userId", userId);
        wrapper.eq("productId", productId);
        List<BrowseRecord> browseRecords = browseMapper.selectList(wrapper);
        if (browseRecords.isEmpty()) {
            //如果没有浏览记录
            BrowseRecord browseRecord = new BrowseRecord();
            browseRecord.setFrequency(1);
            browseRecord.setUserId(userId);
            browseRecord.setProductId(productId);
            Date dt = new Date();
            SimpleDateFormat matter1 = new SimpleDateFormat("yyyyMMddhhMMss");
            String date = matter1.format(dt);
            browseRecord.setTime(date);
            Integer insert = browseMapper.insert(browseRecord);
            if (insert > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            BrowseRecord browseRecord = browseRecords.get(0);
            browseRecord.setFrequency(browseRecord.getFrequency() + 1);
            Date dt = new Date();
            SimpleDateFormat matter1 = new SimpleDateFormat("yyyyMMddhhMMss");
            String date = matter1.format(dt);
            browseRecord.setTime(date);
            Integer insert = browseMapper.updateById(browseRecord);
            if (insert > 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public List<BrowseRecord> getList() {
        List<BrowseRecord> browseRecords = browseMapper.selectList(null);
        return browseRecords;
    }
}
