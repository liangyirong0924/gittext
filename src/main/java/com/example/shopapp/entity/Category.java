package com.example.shopapp.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;


/**
 * <p>
 * 
 * </p>
 *
 * @author 梁艺荣
 * @since 2021-11-21
 */
@Data
@Component
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer parentid;

    private String name;

    private Boolean status;

    private String sortorder;

    private Date createTime;

    private Date updateTime;


}
