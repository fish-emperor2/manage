package com.zime.test.service.impl;

import com.zime.test.dao.entity.Post;
import com.zime.test.dao.mapper.PostMapper;
import com.zime.test.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    PostMapper postMapper;

    @Override
    public List<Post> selectAll() {
        return postMapper.selectAll();
    }

    @Override
    public boolean add(Post post) {
        return postMapper.insert(post) > 0;
    }

    @Override
    public boolean deleteByPrimaryKey(Integer id) {
        return postMapper.deleteByPrimaryKey(id) > 0;
    }
} 