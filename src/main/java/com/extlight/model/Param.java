package com.extlight.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name="t_param")
public class Param {

    @Id
    private Integer id;

    private String paramName;

    private String paramValue;

    private String descr;
}
