package com.extlight.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class PostVo implements Serializable{

    private Integer id;

    private String title;

    private String keyword;

    private String subContent;

    private Integer readCount;

    private String content;

    private Date publishDate;

    private Integer categoryId;

    private String categoryName;

    private String tags;

    private String imgUrl;

    private String postUrl;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


}
