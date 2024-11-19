package com.example.healthcare_api.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/uploads")
public class UploadsController {

    // Đường dẫn đến thư mục uploads
    private final Path uploadsPath = Paths.get("uploads");

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            // Tạo đường dẫn tới tệp ảnh
            Path file = uploadsPath.resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());

            // Kiểm tra xem tệp ảnh có tồn tại và có thể đọc được không
            if (resource.exists() && resource.isReadable()) {
                // Nếu tệp ảnh tồn tại và có thể đọc được, trả về phản hồi với ảnh
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // hoặc MediaType.IMAGE_PNG tùy thuộc vào loại ảnh
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                // Nếu không tìm thấy hoặc không thể đọc tệp ảnh, trả về phản hồi lỗi 404
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            // Xử lý ngoại lệ nếu có lỗi URL
            return ResponseEntity.badRequest().build();
        }
    }
}