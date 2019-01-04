package com.sian.translate.DTO;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class ManageResourceDTO {

    private String parentName;

    private List<HashMap<String,String>> resourceList;


}
