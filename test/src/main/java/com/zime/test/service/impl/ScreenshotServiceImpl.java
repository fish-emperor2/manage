package com.zime.test.service.impl;

import com.zime.test.dao.entity.Screenshot;
import com.zime.test.dao.mapper.ScreenshotMapper;
import com.zime.test.service.ScreenshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ScreenshotServiceImpl implements ScreenshotService {
    @Autowired
    ScreenshotMapper screenshotMapper;
    @Override
    public List<Screenshot> selectByid(int id) {
        return screenshotMapper.selectByid(id);
    }

    @Override
    public int insert(Screenshot record) {
        return screenshotMapper.insert(record);
    }

    @Override
    public int deleteBygame(Screenshot screenshot) {
        return screenshotMapper.deleteBygame(screenshot);
    }
}
