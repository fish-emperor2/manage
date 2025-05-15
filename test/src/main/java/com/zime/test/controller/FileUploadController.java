package com.zime.test.controller;

import com.zime.test.dao.entity.Screenshot;
import com.zime.test.service.ScreenshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
@CrossOrigin
@RestController
@RequestMapping("/upload")
public class FileUploadController {
    @Autowired
    ScreenshotService screenshotService;
    private static final String UPLOAD_DIR = "src/main/resources/static/images/";
    @PostMapping("/image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        // 检查文件是否为空
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "请选择文件"));
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "文件名不能为空"));
        }
        // 创建文件保存路径
        Path path = Paths.get(UPLOAD_DIR + fileName);
        // 创建目录（如果不存在）
        Files.createDirectories(path.getParent());
        // 保存文件到指定路径
        file.transferTo(path);
        // 返回文件的 URL 路径（以便前端访问）
        Map<String, String> response = new HashMap<>();
        String fileUrl = "http://localhost:8080/images/" + fileName;  // 文件可以通过 /images/xxx 访问
        response.put("url", fileUrl);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/screenshot")
    public ResponseEntity<Map<String, String>> uploadImage2(@RequestParam("file") MultipartFile file,@RequestParam("gameId") String id) throws IOException {
        // 检查文件是否为空
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "请选择文件"));
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "文件名不能为空"));
        }
        // 创建文件保存路径
        Path path = Paths.get(UPLOAD_DIR + fileName);
        // 创建目录（如果不存在）
        Files.createDirectories(path.getParent());
        // 保存文件到指定路径
        file.transferTo(path);
        // 返回文件的 URL 路径（以便前端访问）
        Map<String, String> response = new HashMap<>();
        String fileUrl = "http://localhost:8080/images/" + fileName;  // 文件可以通过 /images/xxx 访问
        response.put("url", fileUrl);
        Screenshot screenshot = new Screenshot();

        screenshot.setGameId(Integer.parseInt(id));
        screenshot.setImageUrl(fileUrl);
        screenshotService.insert(screenshot);
        return ResponseEntity.ok(response);
    }
}