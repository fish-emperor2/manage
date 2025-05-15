package com.zime.test.controller;

import com.zime.test.dao.entity.Comment;
import com.zime.test.dao.entity.Screenshot;
import com.zime.test.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    CommentService commentService;
    @GetMapping(path = "/selectByid")
    public List<Comment> selectbyid(@RequestParam int id){
        return commentService.selectByid(id);
    }

    @PostMapping(path = "/add")
    public boolean add(@RequestBody Comment comment){
        return commentService.add(comment);
    }
    @GetMapping(path = "/delete")
    public boolean delete(@RequestParam int id){
        if (commentService.deleteByPrimaryKey(id)>0){
            return true;
        }
        return false;
    }
    @GetMapping(path = "/selectAll")
    public List<Comment> selectAll(){
        return commentService.selectAll();
    }

}
