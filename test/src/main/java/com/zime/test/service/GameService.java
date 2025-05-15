package com.zime.test.service;

import com.zime.test.dao.entity.Game;
import com.zime.test.dao.entity.User;

import java.util.List;

public interface GameService {
//    public User find(User user);
    public List<Game> All();
    Game selectbyid(int id);
    boolean updateByPrimaryKeySelective(Game record);
    boolean insertSelective(Game record);
}
