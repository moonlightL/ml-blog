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
public class GeetestVO implements Serializable{

    public int success;

    private String challenge;

    private String gt;
}
