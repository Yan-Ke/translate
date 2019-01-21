package com.sian.translate.management.templates.dictionary;


import com.sian.translate.DTO.PageInfoDTO;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.dictionary.enity.Dictionary;
import com.sian.translate.dictionary.enity.Thesaurus;
import com.sian.translate.management.dictionary.service.DictionaryManageService;
import com.sian.translate.management.excel.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/manage")
public class DictionaryTemplatesController {

    @Autowired
    DictionaryManageService dictionaryManageService;

    @Autowired
    ExcelService excelService;

    @RequestMapping("/dictionary/list")
    public ModelAndView index(HttpSession session, Map<String, Object> map) {

        ResultVO resultVO = dictionaryManageService.getAllDictionary(session);

        List<Dictionary> dictionaryList = (List<Dictionary>) resultVO.getData();

        map.put("dictionaryList", dictionaryList);

        return new ModelAndView("html/dictionary/index.html", map);
    }

    @RequestMapping("/dictionary/thesaurus")
    public ModelAndView thesaurus(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                  @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                                  @RequestParam(value = "name", required = false) String name,
                                  HttpSession session,Integer id, Integer type,String dictionaryName,Map<String, Object> map) {


        ResultVO resultVO = dictionaryManageService.getThesaurusList(id, name, page, size, session);

        PageInfoDTO pageInfoDTO = (PageInfoDTO) resultVO.getData();
        List<Thesaurus> thesaurusList = (List<Thesaurus>) pageInfoDTO.getList();
        map.put("thesaurusList", thesaurusList);
        map.put("indexPage", pageInfoDTO.getPage());
        map.put("totalPage", pageInfoDTO.getTotalPages());
        if (!StringUtils.isEmpty(name)){
            map.put("name", name);
        }else{
            map.put("name", "");
        }
        map.put("id", id);
        map.put("type", type);
        map.put("dictionaryName", dictionaryName);
        setType(type, map);


        return new ModelAndView("html/dictionary/thesaurus.html", map);
    }



    @RequestMapping("/dictionary/importExcel")
    public ModelAndView importExcel(Integer id, Integer type,String dictionaryName, Map<String, Object> map) {

        map.put("id", id);
        setType(type, map);
        map.put("type", type);
        map.put("dictionaryName", dictionaryName);

        return new ModelAndView("html/dictionary/import.html", map);
    }
    @RequestMapping("/dictionary/detail")
    public ModelAndView deatil(Integer id,Integer thesaurusId, Integer type,String dictionaryName, String contentOne,String contentTwo,Map<String, Object> map) {
        map.put("thesaurusId", thesaurusId);
        map.put("id", id);
        map.put("type", type);
        setType(type, map);
        map.put("dictionaryName", dictionaryName);
        map.put("contentOne", contentOne);
        map.put("contentTwo", contentTwo);



        return new ModelAndView("html/dictionary/details.html", map);
    }

    @RequestMapping("/dictionary/add")
    public ModelAndView add(Map<String, Object> map) {

        return new ModelAndView("html/dictionary/add.html", map);
    }

    @RequestMapping("/dictionary/addEntry")
    public ModelAndView add(Integer id, Integer type,String dictionaryName, Map<String, Object> map) {
        map.put("id", id);
        setType(type, map);
        map.put("type", type);

        map.put("dictionaryName", dictionaryName);

        return new ModelAndView("html/dictionary/add_Entry.html", map);
    }

    private void setType(Integer type, Map<String, Object> map) {
        // 词典类型 1藏汉 2 汉藏 3 臧英 4英臧 5臧臧 6臧梵7 汉汉
        switch (type){
            case 1:
                map.put("contentOneName", "藏语");
                map.put("contentTwoName", "汉语");
                break;
            case 2:
                map.put("contentOneName", "汉语");
                map.put("contentTwoName", "藏语");
                break;
            case 3:
                map.put("contentOneName", "藏语");
                map.put("contentTwoName", "英语");
                break;
            case 4:
                map.put("contentOneName", "英语");
                map.put("contentTwoName", "藏语");
                break;
            case 5:
                map.put("contentOneName", "藏语");
                map.put("contentTwoName", "藏语");
                break;
            case 6:
                map.put("contentOneName", "藏语");
                map.put("contentTwoName", "梵语");
                break;
            case 7:
                map.put("contentOneName", "汉语");
                map.put("contentTwoName", "汉语");
                break;
        }
    }
}
