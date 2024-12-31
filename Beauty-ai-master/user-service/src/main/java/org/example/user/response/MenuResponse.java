package org.example.user.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class MenuResponse {
    private String path;
    private String name;
    private String label;
    private String icon;
    private String url;
    private Map<String, String> meta;
    private List<MenuResponse> children = new ArrayList<>();
}
