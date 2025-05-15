package com.zime.test.controller;

import com.zime.test.dao.entity.Screenshot;
import com.zime.test.service.ScreenshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/screenshot")
public class ScreenshotController {
    @Autowired
    ScreenshotService screenshotService;
    @GetMapping(path = "/selectByid")
    public List<Screenshot> selectbyid(@RequestParam int id){
//        System.out.println(gameService.All().get(0).getTitle());
        return screenshotService.selectByid(id);
    }
    @GetMapping(path = "/delete")
    public int delete(@RequestParam String url,@RequestParam int id){
//        System.out.println(gameService.All().get(0).getTitle());
        Screenshot screenshot=new Screenshot();
        screenshot.setImageUrl(url);
        screenshot.setGameId(id);
        return screenshotService.deleteBygame(screenshot);
    }
}
