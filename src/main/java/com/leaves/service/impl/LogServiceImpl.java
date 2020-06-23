package com.leaves.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.leaves.entity.Log;
import com.leaves.mapper.LogMapper;
import com.leaves.service.LogService;
import com.leaves.util.DateUtil;
import com.leaves.util.IntegrateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 〈日志〉
 *
 * @author
 * @create 2019/2/15 17:53
 * @since 1.0.0
 */
@Service
public class LogServiceImpl implements LogService {
    @Resource
    private LogMapper LogMapper;
    @Resource
    private IntegrateUtils itdragonUtils;

    @Override
    public Page<Log> selectPage(Log Log, int page, int limit) {
        EntityWrapper<Log> searchInfo = new EntityWrapper<>();
        Page<Log> pageInfo = new Page<>(page, limit);
        if (IntegrateUtils.stringIsNotBlack(Log.getUserName())) {
            searchInfo.eq("userName", Log.getUserName());
        }
        if (IntegrateUtils.stringIsNotBlack(Log.getOperation())) {
            searchInfo.like("operation", Log.getOperation());
        }
        searchInfo.orderBy("time desc");
        List<Log> resultList = LogMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(String operation) {
        Log log = new Log();
        log.setOperation(operation);
        log.setTime(DateUtil.getNowDateSS());
        log.setUserName(itdragonUtils.getSessionUser().getUserName());
        Integer insert = LogMapper.insert(log);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delById(String id) {
        Integer delete = LogMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isBuyed(String id) {
        EntityWrapper<Log> wrapper = new EntityWrapper<>();
        wrapper.like("operation", id);
        wrapper.eq("userName", itdragonUtils.getSessionUser().getUserName());
        List<Log> logs = LogMapper.selectList(wrapper);
        if (logs.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isSign() {
        EntityWrapper<Log> wrapper = new EntityWrapper<>();
        wrapper.like("operation", "签到");
        wrapper.like("time", DateUtil.getNowDate());
        wrapper.eq("userName", itdragonUtils.getSessionUser().getUserName());
        List<Log> logs = LogMapper.selectList(wrapper);
        if (logs.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public Integer signNum() {
        EntityWrapper<Log> wrapper = new EntityWrapper<>();
        wrapper.like("operation", "签到");
        wrapper.eq("userName", itdragonUtils.getSessionUser().getUserName());
        List<Log> logs = LogMapper.selectList(wrapper);
        return logs.size();
    }
}