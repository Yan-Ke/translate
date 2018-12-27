package com.sian.translate.DTO;


import lombok.Data;


@Data
public class PageInfoDTO<T> {


    /**当前页码*/
    private Integer page;

    /**当前条数*/
    private Integer size;

    /**总条数**/
    private Integer totalElements;

    /**总页数**/
    private Integer totalPages;

    private T list;
}
