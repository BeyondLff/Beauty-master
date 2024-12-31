package org.example.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("follow_ups")
public class FollowUp {
    @TableId
    private int id;
    private String followUpDate;
    private int type;
    private String details;
    private int customerStatus;
    private int customerId;
    private int userId;
    @TableLogic
    private int deleted;
}
