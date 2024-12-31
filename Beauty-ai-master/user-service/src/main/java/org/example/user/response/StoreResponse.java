package org.example.user.response;

import lombok.Data;

@Data
public class StoreResponse extends Page{
//  private Integer id;
  private String storeName;
  private String addressProvince;
  private String addressCity;
  private String addressDetail;
  private Integer status;
  private String contact;
  private String ownerName;
}
