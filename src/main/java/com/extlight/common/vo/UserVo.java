package com.extlight.common.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserVo implements Serializable{

    @NotEmpty(message = "旧密码不能为空")
    private String oldpwd;

    @NotEmpty(message = "新密码不能为空")
    @Length(min=6, message = "新密码最小长度为6")
    private String newpwd;
}
