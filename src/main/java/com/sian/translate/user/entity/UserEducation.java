package com.sian.translate.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class UserEducation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    /**学历名称 中文**/
    @JsonIgnore
    private String educationName;

    /**学历名称 藏语**/
    @JsonIgnore
    private String zangEducationName;

    @Transient
    private String name;
}
