package com.popit.popitproject.Item.controller;

import com.popit.popitproject.Item.service.S3Service;
import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api/seller")
public class S3Controller {

  private final S3Service s3Service;

  @PostMapping("/upload")
  public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
    return s3Service.uploadFile(file);
  }

  @GetMapping("/list")
  public ResponseEntity<List<String>> listFiles() {
    List<String> fileList = s3Service.listFiles();
    return ResponseEntity.ok(fileList);
  }

}
