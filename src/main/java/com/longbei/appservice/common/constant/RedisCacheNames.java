package com.longbei.appservice.common.constant;

import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lixb on 2017/8/28.
 */
@Component
public class RedisCacheNames {

    public static final String _USER = "1";// 用户
    public static final String _IMPROVE_LINE = "2";// 进步
    public static final String _IMPROVE_DETAIL = "3";// 进步详情
    public static final String _RANK_DETAIL = "4";// 榜详情
    public static final String _RANK_LIST = "5";
    public static final String _SYS_RECOMMAEND = "6";//推荐的
    public static final String _HOME = "7";//首页相关
    public static final String _RANK_AWARD = "8";//奖品相关
    public static final String _ROOM_LIST = "9";//教室列表
    public static final String _RANK_HOME_AWARD = "10";//获奖公示
    public static final String _USER_GRADE = "11";//用户等级
    public static final String _LIVE = "12";//直播
    public static final String _ShortUrl = "13";//短链接

    public static final String _COMMENT = "15"; //评论

    /**
     * 缓存时长 单位 秒
     */
    public static final Long _PERIOD_SECOND_05 = 5L;//
    public static final Long _PERIOD_SECOND_08 = 8L;//
    public static final Long _PERIOD_SECOND_10 = 10L;//
    public static final Long _PERIOD_SECOND_15 = 15L;//
    public static final Long _PERIOD_SECOND_20 = 20L;//
    public static final Long _PERIOD_SECOND_30 = 30L;//
    public static final Long _PERIOD_SECOND_60 = 60L;//
    public static final Long _PERIOD_SECOND_60X5 = 300L;//
    public static final Long _PERIOD_SECOND_60X30 = 1800L;//
    public static final Long _PERIOD_SECOND_60X60 = 3600L;//
    public static final Long _COMMENT_SECOND_60X60X24X30X3 = 7776000L;
    public static final Long _PERIOD_SECOND_60x60x24x7 = 604800L;

    // 根据key设定具体的缓存时间
    private Map<String, Long> expiresMap = null;

    @PostConstruct
    public void init(){
        expiresMap = new HashMap<>();
        expiresMap.put(_USER, _PERIOD_SECOND_05);
        expiresMap.put(_IMPROVE_LINE,_PERIOD_SECOND_10);
        expiresMap.put(_IMPROVE_DETAIL,_PERIOD_SECOND_10);
        expiresMap.put(_SYS_RECOMMAEND,_PERIOD_SECOND_10);
        expiresMap.put(_HOME,_PERIOD_SECOND_10);
        expiresMap.put(_RANK_AWARD,_PERIOD_SECOND_10);
        expiresMap.put(_RANK_HOME_AWARD,_PERIOD_SECOND_60X30);
        expiresMap.put(_ROOM_LIST,_PERIOD_SECOND_08);
        expiresMap.put(_RANK_LIST,_PERIOD_SECOND_08);
        expiresMap.put(_USER_GRADE,_PERIOD_SECOND_60X60);
        expiresMap.put(_LIVE,_PERIOD_SECOND_30);
        expiresMap.put(_COMMENT,_COMMENT_SECOND_60X60X24X30X3);
        expiresMap.put(_ShortUrl,_PERIOD_SECOND_60x60x24x7);
    }

    public Map<String, Long> getExpiresMap(){
        return this.expiresMap;
    }

}
