package com.longbei.appservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.constant.RedisCacheNames;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.dao.*;
import com.longbei.appservice.entity.Classroom;
import com.longbei.appservice.entity.HomePicture;
import com.longbei.appservice.entity.HomePoster;
import com.longbei.appservice.entity.HomeRecommend;
import com.longbei.appservice.entity.Rank;
import com.longbei.appservice.entity.SysCommon;
import com.longbei.appservice.service.CommentMongoService;
import com.longbei.appservice.service.PageService;
import com.longbei.appservice.service.SysSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 页面相关操作
 *
 * @author luye
 * @create 2017-03-22 下午4:55
 **/

@Service
public class PageServiceImpl implements PageService{

    private static Logger logger = LoggerFactory.getLogger(PageServiceImpl.class);

    @Autowired
    private HomePictureMapper homePictureMapper;
    @Autowired
    private HomeRecommendMapper homeRecommendMapper;
    @Autowired
    private RankMapper rankMapper;
    @Autowired
    private SysSettingService sysSettingService;
    @Autowired
    private RankMembersMapper rankMembersMapper;
    @Autowired
    private CommentMongoService commentMongoService;
    @Autowired
    private ClassroomMapper classroomMapper;
    @Autowired
    private ClassroomMembersMapper classroomMembersMapper;
    @Autowired
    private HomePosterMapper homePosterMapper;
    

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public BaseResp<Object> insertHomePage(HomePicture homePicture) {
        BaseResp baseResp = new BaseResp();
        try {
            homePicture.setCreatetime(new Date());
            int res = homePictureMapper.insertSelective(homePicture);
            if (res > 0){
                baseResp = BaseResp.ok();
            }
        } catch (Exception e) {
            logger.error("insert into homepicture is error:",e);
        }
        return baseResp;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public BaseResp<Object> udpateHomePage(HomePicture homePicture) {
        BaseResp baseResp = new BaseResp();
        try {
            if ("1".equals(homePicture.getIsup())){
                homePicture.setUptime(new Date());
            }
            if ("0".equals(homePicture.getIsup())){
                homePicture.setDowntime(new Date());
            }
            homePicture.setUpdatetime(new Date());
            int res = homePictureMapper.updateByPrimaryKeySelective(homePicture);
            if (res > 0){
                baseResp = BaseResp.ok();
            }
        } catch (Exception e) {
            logger.error("update homepicture is error:",e);
        }
        return baseResp;
    }

    @SuppressWarnings("unchecked")
	@Override
    public BaseResp<HomePicture> selectHomePageDetail(Integer id) {
        BaseResp<HomePicture> baseResp = new BaseResp<>();
        try {
            HomePicture homePicture = homePictureMapper.selectByPrimaryKey(id);
            baseResp = BaseResp.ok();
            baseResp.setData(homePicture);
        } catch (Exception e) {
            logger.error("select homepicture is error:",e);
        }
        return baseResp;
    }

    @SuppressWarnings("unchecked")
	@Override
    public BaseResp<Page<HomePicture>> selectHomePageList(HomePicture homePicture, Integer pageno, Integer pagesize) {

        BaseResp<Page<HomePicture>> baseResp = new BaseResp<>();
        Page<HomePicture> page = new Page<>(pageno,pagesize);

        try {
            int totalcount = homePictureMapper.selectCount(homePicture);
            List<HomePicture> homePictures = homePictureMapper.selectList(homePicture,(pageno-1)*pagesize,pagesize);
            baseResp = BaseResp.ok();
            page.setTotalCount(totalcount);
            page.setList(homePictures);
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("select home pic list is error:",e);
        }
        return baseResp;
    }
    

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Page<HomePoster>> homeposterlist(HomePoster homePoster, Integer pageno, Integer pagesize) {
        BaseResp<Page<HomePoster>> baseResp = new BaseResp<>();
        Page<HomePoster> page = new Page<>(pageno,pagesize);

        try {
            int totalcount = homePosterMapper.selectCount(homePoster);
            List<HomePoster> homePictures = homePosterMapper.selectList(homePoster,(pageno-1)*pagesize,pagesize);
            baseResp = BaseResp.ok();
            page.setTotalCount(totalcount);
            page.setList(homePictures);
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("select home poster list is error:",e);
        }
        return baseResp;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> insertHomePoster(HomePoster homePoster) {
		BaseResp<Object> baseResp = new BaseResp<>();
        try {
        	homePoster.setCreatetime(new Date());
        	homePoster.setIsup("0");
        	homePoster.setIsdel("0");
            int res = homePosterMapper.insertSelective(homePoster);
            if (res > 0){
                baseResp = BaseResp.ok();
            }
        } catch (Exception e) {
            logger.error("insert into homepicture is error:",e);
        }
        return baseResp;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> editPoster(HomePoster homePoster) {
		BaseResp<Object> baseResp = new BaseResp<>();
        try {
            int res = homePosterMapper.updateByPrimaryKeySelective(homePoster);
            if (res > 0){
                baseResp = BaseResp.ok();
            }
        } catch (Exception e) {
        	logger.error("editPoster homePoster = {}", JSON.toJSON(homePoster).toString(), e);
        }
        return baseResp;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<HomePoster> selectHomePoster(String id) {
		BaseResp<HomePoster> baseResp = new BaseResp<>();
        try {
        	HomePoster homePoster = homePosterMapper.selectByPrimaryKey(Integer.parseInt(id));
            baseResp = BaseResp.ok();
            baseResp.setData(homePoster);
        } catch (Exception e) {
        	logger.error("selectHomePicture id = {}", id, e);
        }
        return baseResp;
	}


	@SuppressWarnings("unchecked")
	@Override
    @Cacheable(cacheNames = RedisCacheNames._HOME,key = "homeposter")
	public BaseResp<HomePoster> selectHomePosterIsup() {
		BaseResp<HomePoster> baseResp = new BaseResp<>();
        try {
        	HomePoster homePoster = homePosterMapper.selectHomePosterIsup();
        	if(null != homePoster){
        		//contenttype 关联 内容类型 0 - 龙榜 1 - 教室  2 - 专题   3 - 达人  4 - 商品
        		if ("2".equals(homePoster.getContenttype())){
        			homePoster.setHref(AppserviceConfig.seminarurl + "?seminarid=" + homePoster.getHref());
                }
        		homePoster.setPhotos(AppserviceConfig.oss_media + homePoster.getPhotos());
        	}
            baseResp = BaseResp.ok();
            baseResp.setData(homePoster);
        } catch (Exception e) {
        	logger.error("selectHomePicture ", e);
        }
        return baseResp;
	}


	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> updateIsup(String isup, String id) {
		BaseResp<Object> baseResp = new BaseResp<>();
        try {
        	int res = 0;
        	//启动页上线只保留一个  (2017-08-04)
        	if("1".equals(isup)){
        		homePosterMapper.updateIsdown(DateUtils.formatDateTime1(new Date()));
        		res = homePosterMapper.updateIsup(isup, id, DateUtils.formatDateTime1(new Date()), null);
        	}else{
        		res = homePosterMapper.updateIsup(isup, id, null, DateUtils.formatDateTime1(new Date()));
        	}
            if (res > 0){
                baseResp = BaseResp.ok();
            }
        } catch (Exception e) {
        	logger.error("updateIsup isup = {}, id = {}", isup, id, e);
        }
        return baseResp;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> updateIsdel(String id) {
		BaseResp<Object> baseResp = new BaseResp<>();
        try {
        	int res = homePosterMapper.deleteByPrimaryKey(Integer.parseInt(id));
            if (res > 0){
                baseResp = BaseResp.ok();
            }
        } catch (Exception e) {
        	logger.error("updateIsdel id = {}", id, e);
        }
        return baseResp;
	}


    @SuppressWarnings("unchecked")
	@Override
    @Cacheable(cacheNames = RedisCacheNames._HOME,key = "#type")
    public BaseResp<List<HomePicture>> selectHomePicList(String type) {
        BaseResp<List<HomePicture>> baseResp = new BaseResp<>();
        List<HomePicture> homePictures = new ArrayList<HomePicture>();
        try {
                HomePicture homePicture = new HomePicture();
                homePicture.setIsup("1");
                homePicture.setType(type);
                homePictures = homePictureMapper.selectList(homePicture,null,null);
                for (HomePicture homePicture1 : homePictures){
                    if ("0".equals(homePicture1.getContenttype())){
                        homePicture1.setHref(AppserviceConfig.articleurl + "?articleid=" + homePicture1.getHref());
                    }
                    if ("1".equals(homePicture1.getContenttype())){
                        homePicture1.setHref(AppserviceConfig.seminarurl + "?seminarid=" + homePicture1.getHref());
                    }
                }
//            }else if("2".equals(type)){
//                HomePicture homePicture = new HomePicture();
//                homePicture.setPicname("龙杯英语900句");
//                homePicture.setPhotos("rank/b9cba2f8-462b-44a2-85e0-97e033aafe4d");
//                homePicture.setContenttype("0");
//                homePicture.setIsup("1");
//                homePicture.setIsdel(0);
//                homePicture.setSort("0");
//                homePicture.setHref(AppserviceConfig.articleurl + "?articleid=107");
//
//                HomePicture homePicture2 = new HomePicture();
//                homePicture2.setPicname("北京公益基金会");
//                homePicture2.setPhotos("rank/0b81ae78-7cb9-4e2a-b89b-e547a46dd169");
//                homePicture2.setContenttype("0");
//                homePicture2.setIsup("1");
//                homePicture2.setIsdel(0);
//                homePicture2.setSort("0");
//                homePicture2.setHref(AppserviceConfig.articleurl + "?articleid=105");
//
//                homePictures.add(0,homePicture);
//                homePictures.add(1,homePicture2);
//            }
            baseResp.setData(homePictures);
            return baseResp.initCodeAndDesp();
        } catch (Exception e) {
            logger.error("select home pic list for app is error:",e);
        }
        return baseResp;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public BaseResp<Object> insertHomeRecommend(HomeRecommend homeRecommend) {
        BaseResp baseResp = new BaseResp();
        try {
            homeRecommend.setCreatetime(new Date());
            homeRecommendMapper.insertBantch(homeRecommend);
            List<Long> list = homeRecommend.getBusinessids();
            if (null != list && list.size()!=0){
                for (Long lo: list){
                	//recommendtype 0 - 榜单 1 - 教室 2 - 圈子
                	if(homeRecommend.getRecommendtype() == 0){
                		Rank rank = new Rank();
                        rank.setRankid(lo);
                        rank.setIshomerecommend("1");
                        rankMapper.updateSymbolByRankId(rank);
                	}else{
                		classroomMapper.updateIshomerecommend(lo, "1");
                	}
                }
            }
            baseResp = BaseResp.ok();
        } catch (Exception e) {
            logger.error("insert into homerecommend is error:",e);
        }
        return baseResp;
    }

    @SuppressWarnings("unchecked")
	@Override
    public BaseResp<Page<HomeRecommend>> selectHomeRecommendList(HomeRecommend homeRecommend,
                                                                 Integer pageno, Integer pagesize) {
        BaseResp<Page<HomeRecommend>> baseResp = new BaseResp<>();
        Page<HomeRecommend> page = new Page<>(pageno,pagesize);

        try {
            int totalcount = homeRecommendMapper.selectCount(homeRecommend);
            List<HomeRecommend> homeRecommends = homeRecommendMapper.selectList(homeRecommend,pagesize*(pageno-1),pagesize);
            for (HomeRecommend homeRecommend1 : homeRecommends){
                //recommendtype 0 - 榜单 1 - 教室 2 - 圈子
                if (0 == homeRecommend1.getRecommendtype()) {
                    Rank rank = rankMapper.selectRankByRankid(homeRecommend1.getBusinessid());
                    if (null == rank){
                        continue;
                    }
                    BaseResp<Integer> baseResp1 = commentMongoService.selectCommentCountSum(String.valueOf(homeRecommend1.getBusinessid()),"10",null);
                    if (ResultUtil.isSuccess(baseResp1)){
                        rank.setCommentCount(baseResp1.getData());
                    }
                    String icount = rankMembersMapper.getRankImproveCount
                            (String.valueOf(homeRecommend1.getBusinessid()))==null?"0":rankMembersMapper.getRankImproveCount(String.valueOf(homeRecommend1.getBusinessid()));
                    rank.setIcount(Integer.parseInt(icount));
                    homeRecommend1.setRank(rank);
                } else if (1 == homeRecommend1.getRecommendtype()) {
                    Classroom classroom = classroomMapper.selectByPrimaryKey(homeRecommend1.getBusinessid());
                    if(classroom == null){
                        continue;
                    }
                    BaseResp<Integer> baseResp1 = commentMongoService.selectCommentCountSum(String.valueOf(homeRecommend1.getBusinessid()),"12",null);
                    if (ResultUtil.isSuccess(baseResp1)){
                    	classroom.setCommentCount(baseResp1.getData());
                    }
                    //教室总进步数量
                    Integer allimp = classroomMembersMapper.selectRoomImproveCount(classroom.getClassroomid());
                    if(allimp == null){
						allimp = 0;
					}
					classroom.setAllimp(allimp);
					homeRecommend1.setClassroom(classroom);
                }

            }
            page.setTotalCount(totalcount);
            page.setList(homeRecommends);
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("select home recommend list is error:",e);
        }
        return baseResp;
    }

    @SuppressWarnings("unchecked")
	@Override
    public BaseResp<List<HomeRecommend>> selectHomeRecommendList(Integer startno, Integer pagesize) {
        BaseResp<List<HomeRecommend>> baseResp = new BaseResp<>();
        HomeRecommend homeRecommend = new HomeRecommend();
        try {
            List<HomeRecommend> homeRecommends = homeRecommendMapper.selectList(homeRecommend,startno,pagesize);
            List<HomeRecommend> resultlist = new ArrayList<>();
            for (HomeRecommend homeRecommend1 : homeRecommends){
                Rank rank = rankMapper.selectRankByRankid(homeRecommend1.getBusinessid());
                if (null != rank){
                    String icount = rankMembersMapper.getRankImproveCount
                            (String.valueOf(rank.getRankid()))==null?"0":rankMembersMapper.getRankImproveCount(String.valueOf(rank.getRankid()));
                    rank.setIcount(Integer.parseInt(icount));
                    homeRecommend1.setRank(rank);
                    resultlist.add(homeRecommend1);
                }

            }
            baseResp = BaseResp.ok();
            baseResp.setData(resultlist);
        } catch (Exception e) {
            logger.error("select homeRecmment list is error:",e);
        }
        return baseResp;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public BaseResp<Object> updateHomeRecommend(HomeRecommend homeRecommend) {
        BaseResp baseResp = new BaseResp();
        try {
            int res = homeRecommendMapper.updateByPrimaryKeySelective(homeRecommend);
            if (res > 0){
                if ("1".equals(homeRecommend.getIsdel())){
                	HomeRecommend recommend = homeRecommendMapper.selectByPrimaryKey(homeRecommend.getId());
                	//recommendtype 0 - 榜单 1 - 教室 2 - 圈子
                	if(recommend.getRecommendtype() == 0){
                		Rank rank = new Rank();
                        rank.setRankid(homeRecommend.getBusinessid());
                        rank.setIshomerecommend("0");
                        rankMapper.updateSymbolByRankId(rank);
                	}else{
                		classroomMapper.updateIshomerecommend(homeRecommend.getBusinessid(), "0");
                	}
                	
                }
                baseResp = BaseResp.ok();
            }
        } catch (Exception e) {
            logger.error("update homerecommend is error:",e);
        }
        return baseResp;
    }


    @Override
    public BaseResp<Object> saveOrUpdatePublishBg(String pickey) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            boolean sueecss = sysSettingService.insertSysCommon(Constant.SYS_COMMON_KEYS.publishbg.toString(),pickey, DateUtils.getDate());
            if (sueecss){
                baseResp.initCodeAndDesp();
            }
        } catch (Exception e) {
            logger.error("save or update publisbg is error:",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<String> selectPublishBg() {
        BaseResp<String> baseResp = new BaseResp<>();
        try {
            SysCommon sysCommon = sysSettingService.getSysCommonByKey(Constant.SYS_COMMON_KEYS.publishbg.toString());
            if (null != sysCommon){
                baseResp.initCodeAndDesp();
                baseResp.setData(sysCommon.getInfo());
            }
        } catch (Exception e) {
            logger.error("select publishbg is error:",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> saveOrUpdateRegisterProtocol(String regPro) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            boolean res = sysSettingService.insertSysCommon(Constant.SYS_COMMON_KEYS.regprotocol.toString(),regPro,DateUtils.getDate());
            if (res){
                baseResp.initCodeAndDesp();
            }
        } catch (Exception e) {
            logger.error("save or update register protocol is error:",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<String> selectRegisterProtocol() {
        BaseResp<String> baseResp = new BaseResp<>();
        try {
            SysCommon sysCommon = sysSettingService.getSysCommonByKey(Constant.SYS_COMMON_KEYS.regprotocol.toString());
            if (null != sysCommon){
                baseResp.initCodeAndDesp();
                baseResp.setData(sysCommon.getInfo());
            }
        } catch (Exception e) {
            logger.error("select register protocol is error:",e);
        }
        return baseResp;
    }

}
