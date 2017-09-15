package com.longbei.appservice.cache;

import com.longbei.appservice.common.constant.RedisCacheNames;
import com.longbei.appservice.dao.HomePictureMapper;
import com.longbei.appservice.dao.HomePosterMapper;
import com.longbei.appservice.entity.HomePicture;
import com.longbei.appservice.entity.HomePoster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 相关页面内容缓存
 *
 * @author luye
 * @create 2017-08-30 下午4:02
 **/
@Service
public class PageCache {

    private static Logger logger = LoggerFactory.getLogger(PageCache.class);

    @Autowired
    private HomePosterMapper homePosterMapper;
    @Autowired
    private HomePictureMapper homePictureMapper;

    @Cacheable(cacheNames = RedisCacheNames._HOME,key = "'homeposter'")
    public HomePoster selectHomePosterIsUp() throws Exception{
        HomePoster homePoster = null;
        try {
            homePoster = homePosterMapper.selectHomePosterIsup();
        } catch (Exception e) {
            throw e;
        }
        return homePoster;
    }

    @Cacheable(cacheNames = RedisCacheNames._HOME,key = "#type")
    public List<HomePicture> selectHomePicList(String type) throws Exception{
        List<HomePicture> homePictures = null;
        try {
            HomePicture homePicture = new HomePicture();
            homePicture.setIsup("1");
            homePicture.setType(type);
            homePictures = homePictureMapper.selectList(homePicture,null,null);
        } catch (Exception e) {
            throw e;
        }
        return homePictures;
    }



}
