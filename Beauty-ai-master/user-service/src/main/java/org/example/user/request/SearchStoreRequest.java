package org.example.user.request;

import lombok.Data;

@Data
public class SearchStoreRequest extends PageArgs{

    private String storeName;
    private String addressProvince;
    private String addressCity;
    private String addressDetail;
    private Integer status;
    private String contact;
    private String ownerName;
}
