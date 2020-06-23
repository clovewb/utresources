package com.leaves.service;

import com.leaves.entity.BrowseRecord;

import java.util.List;

public interface BrowseService {

    boolean insert(String userId, String productId);

    List<BrowseRecord> getList();
}
