package com.zime.test.service.impl;

import com.zime.test.dao.entity.Comment;
import com.zime.test.dao.mapper.CommentMapper;
import com.zime.test.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Override
    public List<Comment> selectByid(int id) {
        return commentMapper.selectByid(id);
    }

    @Override
    public boolean add(Comment comment) {
        return commentMapper.add(comment);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return commentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Comment> selectAll() {
        return commentMapper.selectAll();
    }
}
