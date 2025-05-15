package com.zime.test.service;

import com.zime.test.dao.entity.Screenshot;

import java.util.List;

public interface ScreenshotService {
    List<Screenshot> selectByid(int id);
    int insert(Screenshot record);
    int deleteBygame(Screenshot screenshot);
}
