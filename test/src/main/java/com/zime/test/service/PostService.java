package com.zime.test.service;

import com.zime.test.dao.entity.Post;
import java.util.List;

public interface PostService {
    List<Post> selectAll();
    boolean add(Post post);
    boolean deleteByPrimaryKey(Integer id);
} 