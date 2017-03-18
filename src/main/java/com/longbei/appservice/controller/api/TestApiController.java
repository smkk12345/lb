package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.DictAreaMapper;
import com.longbei.appservice.entity.DictArea;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

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

        return JSONArray.fromObject(getChildArea(null,dictAreas)).toString();

    }

    private List<DictArea> getChildArea(Integer id,List<DictArea> dictAreas){
        List<DictArea> chillAreas = new ArrayList<>();
        for (Iterator it = dictAreas.iterator();it.hasNext();){
            DictArea dictArea = (DictArea)it.next();
            if (!StringUtils.isBlank(dictArea.getPid())&&null!=id){
                if (Integer.parseInt(dictArea.getPid()) == id){
                    chillAreas.add(dictArea);
                    dictArea.setChildArea(getChildArea(dictArea.getId(),dictAreas));
                }
            }else if(null == id){
                chillAreas.add(dictArea);
                dictArea.setChildArea(getChildArea(dictArea.getId(),dictAreas));
            }
        }
        return chillAreas;
    }
}
