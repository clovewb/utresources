package com.leaves.service.impl;

import com.leaves.entity.WbeParameter;
import com.leaves.mapper.WbeParameterMapper;
import com.leaves.service.WbeParameterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 网站参数设置接口
 */
@Service
public class WbeParameterServiceImpl implements WbeParameterService {
    @Resource
    private WbeParameterMapper wbeParameterMapper;

    @Override
    public boolean editById(WbeParameter wbeParameter) {
        Integer integer = wbeParameterMapper.updateById(wbeParameter);
        if (integer > 0) {
            return true;
        }
        return false;
    }

    @Override
    public WbeParameter getWbeParameter() {
        List<WbeParameter> wbeParameterList = wbeParameterMapper.selectList(null);
        if (wbeParameterList.isEmpty()) {
            return new WbeParameter();
        }
        return wbeParameterList.get(0);
    }
}
