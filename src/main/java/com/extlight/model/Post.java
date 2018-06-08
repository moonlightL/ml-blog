package com.extlight.model;

import com.extlight.plugin.CreateTime;
import com.extlight.plugin.UpdateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@Table(name="t_post")
public class Post {

    @Id
    private Integer id;

    @NotEmpty(message = "文章标题不能为空")
    private String title;

    private String keyword;

    private String subContent;

    @NotEmpty(message = "文章内容不能为空")
    private String content;

    // 显示状态 1：是 0：否
    private Integer status;

    @NotNull(message = "分类不能为空")
    private Integer categoryId;

    private String categoryName;

    private String tags;

    private String imgUrl;

    private String year;

    private String month;

    private String day;

    // 文章 url，通过该值查询文章内容
    private String postUrl;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date publishDate;

    @CreateTime
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @UpdateTime
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
