package org.example.user.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class ImageRequest {
    private MultipartFile image;
    private Integer id;
}
