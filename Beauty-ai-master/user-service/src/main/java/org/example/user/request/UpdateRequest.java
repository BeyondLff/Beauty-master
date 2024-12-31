package org.example.user.request;

import lombok.Data;

@Data
public class UpdateRequest {
    private Integer id;
    private String username;
    private String phoneNumber;
    private Integer storesId;
    private Integer status;
    private String idcard;
    private String homeAddress;
}
