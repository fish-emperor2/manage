package com.zime.test.service;

import com.zime.test.dao.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> selectByid(int id);
    boolean add(Comment comment);
    int deleteByPrimaryKey(Integer id);
    List<Comment> selectAll();
}
