package com.zime.test.dao.mapper;

import com.zime.test.dao.entity.Game;

import java.util.List;

public interface GameMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Game record);

    int insertSelective(Game record);
    int insertGame(Game game);
    Game selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Game record);

    int updateByPrimaryKeyWithBLOBs(Game record);

    int updateByPrimaryKey(Game record);
    public List<Game> All();
    Game selectbyid(int id);
}