package com.zime.test.service;

import com.zime.test.dao.entity.User;
import com.zime.test.dao.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    public User find(User user);
    boolean insert(User user);
    List<User> all();
    boolean updateByPrimaryKeySelective(User user);
    boolean deleteByPrimaryKey(Integer id);


}
