package com.zime.test.controller;

import com.zime.test.dao.entity.Game;
import com.zime.test.dao.entity.Screenshot;
import com.zime.test.dao.entity.User;
import com.zime.test.service.GameService;
import com.zime.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/game")
public class GameController {
    @Autowired
    UserService userservice;
    @Autowired
    GameService gameService;
    @GetMapping(path = "/all")
    public List<Game> All(){
        return gameService.All();
    }
    @GetMapping(path = "/selectbyid")
    public Game selectbyid(@RequestParam int id){
        return gameService.selectbyid(id);
    }
    @PostMapping(path = "/update")
    public boolean update(@RequestBody Game game){
        return gameService.updateByPrimaryKeySelective(game);
    }
    @PostMapping(path = "/insert")
    public boolean insert(@RequestBody Game game){

//        System.out.println(game.getTags().get(0));
        return gameService.insertSelective(game);
    }
//    @PostMapping(path = "/image")
//    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
//        // 处理文件上传逻辑，保存文件并返回 URL
//        Map<String, String> response = new HashMap<>();
//        response.put("url", "上传后的文件路径");
//        return ResponseEntity.ok(response);
//    }
}
