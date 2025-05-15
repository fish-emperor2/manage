package com.zime.test.service.impl;

import com.zime.test.dao.entity.User;
import com.zime.test.dao.mapper.UserMapper;
import com.zime.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public User find(User user) {
        return userMapper.find(user);
    }

    @Override
    public boolean insert(User user) {
        if (userMapper.insert(user)>0){
            return true;
        }
        return false;
    }

    @Override
    public List<User> all() {
        return userMapper.all();
    }

    @Override
    public boolean updateByPrimaryKeySelective(User user) {
        if (userMapper.updateByPrimaryKeySelective(user)>0){
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteByPrimaryKey(Integer id) {
       if (userMapper.deleteByPrimaryKey(id)>0){
           return true;
       }
       return false;
    }
}
