package com.hjc.sharding.demo.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 * Created with Intellij IDEA.
 * @author hjc
 * @version 2018/5/31
 */
@Data
@TableName("member")
public class Member {

    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    private String name;

    private String mobile;
}
