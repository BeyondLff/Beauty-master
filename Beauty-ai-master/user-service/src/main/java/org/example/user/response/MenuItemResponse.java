package org.example.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuItemResponse {
    private String path;
    private String name;
    private String label;
    private String icon;
    private String url;
}
