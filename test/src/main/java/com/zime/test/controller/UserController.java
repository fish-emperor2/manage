package com.zime.test.controller;

import com.zime.test.dao.entity.User;
import com.zime.test.model.AjaxResult;
import com.zime.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userservice;
    @GetMapping(path = "/login")
    public User login(@RequestParam String username, @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        User user1 = userservice.find(user);

        if (user1 != null) {
            return user1;
        } else {
            return null;
        }
    }
    @GetMapping(path = "/register")
    public boolean register(@RequestParam String username, @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        User user1 = userservice.find(user);
        if (user1 != null) {
            return false;
        } else {
            return userservice.insert(user);
        }



    }
    @GetMapping(path = "/all")
    public List<User> all() {
        return userservice.all();
    }
    @GetMapping(path = "/alter")
    public boolean alter(@RequestParam String username, @RequestParam String password,@RequestParam String id) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setId(Integer.parseInt(id));
        return userservice.updateByPrimaryKeySelective(user);
    }
    @GetMapping(path = "/delete")
    public boolean delete(@RequestParam int id) {

        return userservice.deleteByPrimaryKey(id);
    }



}
