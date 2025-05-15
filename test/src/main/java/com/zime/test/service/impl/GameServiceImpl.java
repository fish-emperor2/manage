package com.zime.test.service.impl;

import com.zime.test.dao.entity.Game;
import com.zime.test.dao.mapper.GameMapper;
import com.zime.test.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GameServiceImpl implements GameService {
    @Autowired
    GameMapper gameMapper;
    @Override
    public List<Game> All() {
        return gameMapper.All();
    }

    @Override
    public Game selectbyid(int id) {
        return gameMapper.selectbyid(id);
    }

    @Override
    public boolean updateByPrimaryKeySelective(Game record) {
        if(gameMapper.updateByPrimaryKeySelective(record)>0)return true;
        return false;
    }

    @Override
    public boolean insertSelective(Game record) {
        if(gameMapper.insertGame(record)>0)return true;
        return false;
    }
}
