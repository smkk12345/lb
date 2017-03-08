package com.longbei.appservice.controller.api;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.DictAreaMapper;
import com.longbei.appservice.entity.DictArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * test
 *
 * @author luye
 * @create 2017-03-07 下午6:18
 **/
@RestController
@RequestMapping(value = "test")
public class TestApiController {

    @Autowired
    private DictAreaMapper dictAreaMapper;

    @RequestMapping(value = "area",method = RequestMethod.GET)
    public String dictArea(){

        List<DictArea> dictAreas = dictAreaMapper.selectCityList(null,null,null);

        return JSON.toJSONString(getDictAreaJsonList(dictAreas));

    }


    private List<DictArea> getDictAreaJsonList(List<DictArea> dictAreas){

        List<DictArea> rootArea = new ArrayList<>();

        for (DictArea dictArea : dictAreas){
            if (StringUtils.isBlank(dictArea.getPid())){
                rootArea.add(dictArea);
            }
        }

        for (DictArea dictArea : rootArea){
            dictArea.setChildArea(getChildArea(dictArea.getId(),dictAreas));
        }
        return rootArea;

    }


    private List<DictArea> getChildArea(int id,List<DictArea> dictAreas){

        List<DictArea> chillAreas = new ArrayList<>();

        for (DictArea dictArea : dictAreas){
            if (!StringUtils.isBlank(dictArea.getPid())){
                if (Integer.parseInt(dictArea.getPid()) == id){
                    chillAreas.add(dictArea);
                }
            }
        }

        for (DictArea dictArea : chillAreas){
            dictArea.setChildArea(getChildArea(dictArea.getId(),dictAreas));
        }
        return chillAreas;

    }

}
