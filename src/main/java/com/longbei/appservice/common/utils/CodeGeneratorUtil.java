package com.longbei.appservice.common.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 唯一code生成
 *
 * @author luye
 * @create 2017-03-17 下午4:37
 **/
public class CodeGeneratorUtil {

    private static int m = 6;

    private static char []baseData1 = new char[]{
            'a','b','c','d','e','f','g','h',
            '0','1','2','3'
    };

    private static char []baseData2 = new char[]{
            'n','o','p','q','r','s','t','u',
            '5','6','8','9'
    };

    private static char []baseData3 = new char[]{
            'v','w','x','y','z','i','j','k','l','m'
            ,'4','7'
    };

    public static Set<String> generatorCode(int baseDatatype){

        char []baseData = null;
        if (1 == baseDatatype){
            baseData = baseData1;
        }
        if (2 == baseDatatype){
            baseData = baseData2;
        }
        if (3 == baseDatatype){
            baseData = baseData3;
        }
        if (baseData == null){
            return null;
        }
        List<Integer> iL = new ArrayList<Integer>();
        Set<String> list = new HashSet<>();
        plzh("", iL,  m,baseData3,list);
        return list;
    }



    private static  void plzh(String s, List<Integer> iL, int m,char []data,Set<String> list) {
        if(m == 0) {
            list.add(s);
            return;
        }
        List<Integer> iL2;
        for(int i = 0; i < data.length; i++) {
            iL2 = new ArrayList<Integer>();
            iL2.addAll(iL);
            if(!iL.contains(i)) {
                String str = s + data[i];
                iL2.add(i);
                plzh(str, iL2, m-1,data,list);
            }
        }
    }





}
