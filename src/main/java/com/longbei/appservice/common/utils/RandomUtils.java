package com.longbei.appservice.common.utils;

import java.util.Random;

/**
 * Created by smkk on 2017/2/25.
 */
public class RandomUtils {

    public static int getRandomCode(int min,int max)
    {
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }

}
