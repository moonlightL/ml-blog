package com.extlight.model;

import com.extlight.plugin.CreateTime;
import com.extlight.plugin.UpdateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name="t_about_me")
public class AboutMe {

    @Id
    private Integer id;

    @NotEmpty(message = "内容不能为空")
    private String content;

    private Integer status;

    @CreateTime
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @UpdateTime
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
