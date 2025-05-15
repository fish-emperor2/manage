package com.zime.test.dao.mapper;

import com.zime.test.dao.entity.Screenshot;

import java.util.List;

public interface ScreenshotMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Screenshot record);

    int insertSelective(Screenshot record);

    Screenshot selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Screenshot record);

    int updateByPrimaryKey(Screenshot record);
    List<Screenshot> selectByid(int id);
    int deleteBygame(Screenshot screenshot);
}