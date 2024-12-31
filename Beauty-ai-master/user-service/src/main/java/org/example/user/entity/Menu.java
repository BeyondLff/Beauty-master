package org.example.user.entity;

import lombok.Data;

@Data
public class Menu {
    private Integer id;
    private String path;
    private String name;
    private String label;
    private String icon;
    private String url;
}
