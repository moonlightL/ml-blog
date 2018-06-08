package com.extlight.common.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FileResult implements Serializable{

    public int success = 1;

    private String message = "上传成功";

    private String url;

    public FileResult(int success, String message) {
        this.success = success;
        this.message = message;
    }

    private FileResult(String url) {
        this.url = url;
    }

    public static FileResult success(String url) {
        return new FileResult(url);
    }

    public static FileResult fail(String message) {
        return new FileResult(0,message);
    }
}
