package com.sian.translate.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DayCountDTO {

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    Date date;

    long count;
}
