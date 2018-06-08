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
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@Table(name="t_category")
public class Category {

    @Id
    private Integer id;

    @NotEmpty(message = "名称不能为空")
    private String name;

    private Integer sort;

    private String descr;

    private String color;

    private String imgUrl;

    @CreateTime
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @UpdateTime
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
