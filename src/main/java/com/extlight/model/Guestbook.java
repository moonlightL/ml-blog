package com.extlight.model;

import com.extlight.plugin.CreateTime;
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
@Table(name="t_guestbook")
public class Guestbook {

    @Id
    private Integer id;

    @NotEmpty(message = "昵称不能为空")
    private String nickname;

    private String email;

    private String homeUrl;

    private String imgUrl;

    @NotEmpty(message = "留言不能为空")
    private String content;

    private String ip;

    // ip 归属地
    private String ipAddr;

    // 1:已读 0：未读
    private Integer status;

    // 1：删除 0：未删除
    private Integer delStatus;

    @CreateTime
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
