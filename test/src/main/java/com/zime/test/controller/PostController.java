package com.zime.test.controller;

import com.zime.test.dao.entity.Post;
import com.zime.test.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    PostService postService;

    @GetMapping("/all")
    public List<Post> getAllPosts() {
        return postService.selectAll();
    }

    @PostMapping("/add")
    public boolean addPost(@RequestBody Post post) {
        return postService.add(post);
    }

    @DeleteMapping("/delete")
    public boolean deletePost(@RequestParam int id) {
        return postService.deleteByPrimaryKey(id);
    }
} 