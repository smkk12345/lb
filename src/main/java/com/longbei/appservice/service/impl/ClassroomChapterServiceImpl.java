package com.longbei.appservice.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.ClassroomChapterMapper;
import com.longbei.appservice.dao.ClassroomCoursesMapper;
import com.longbei.appservice.entity.ClassroomChapter;
import com.longbei.appservice.service.ClassroomChapterService;

@Service
public class ClassroomChapterServiceImpl implements ClassroomChapterService {
	
	@Autowired
	private ClassroomChapterMapper classroomChapterMapper;
	@Autowired
	private ClassroomCoursesMapper classroomCoursesMapper;
	
	
	private static Logger logger = LoggerFactory.getLogger(ClassroomChapterServiceImpl.class);

	

	@Override
	public BaseResp<Page<ClassroomChapter>> selectPcSearchChapterList(ClassroomChapter classroomChapter, int startNo, int pageSize) {
		BaseResp<Page<ClassroomChapter>> reseResp = new BaseResp<>();
		Page<ClassroomChapter> page = new Page<>(startNo/pageSize+1, pageSize);
		try {
			int totalcount = classroomChapterMapper.selectSearchCount(classroomChapter);
			List<ClassroomChapter> list = classroomChapterMapper.selectPcSearchChapterList(classroomChapter, startNo, pageSize);
			page.setTotalCount(totalcount);
            page.setList(list);
			reseResp.setData(page);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectPcSearchChapterList classroomid = {}, startNo = {}, pageSize = {}", 
					classroomChapter.getClassroomid(), startNo, pageSize, e);
		}
		return reseResp;
	}
	
	@Override
	public BaseResp<ClassroomChapter> selectByPrimaryKey(long chapterid) {
		BaseResp<ClassroomChapter> baseResp = new BaseResp<>();
		try{
			ClassroomChapter classroomChapter = classroomChapterMapper.selectByPrimaryKey(chapterid);
			baseResp.setData(classroomChapter);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		}catch(Exception e){
			logger.error("selectByPrimaryKey chapterid = {}", chapterid, e);
        }
    	return baseResp;
	}

	@Override
	public BaseResp<Object> updateByPrimaryKeySelective(ClassroomChapter record) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			int temp = classroomChapterMapper.updateByPrimaryKeySelective(record);
			if(temp>0){
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		}catch(Exception e){
			logger.error("updateByPrimaryKeySelective record = {}", JSON.toJSON(record).toString(), e);
        }
    	return baseResp;
	}
	

	@Override
	public BaseResp<Object> insertSelective(ClassroomChapter record) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			int temp = classroomChapterMapper.insertSelective(record);
			if(temp>0){
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		}catch(Exception e){
			logger.error("insertSelective record = {}", JSON.toJSON(record).toString(), e);
        }
    	return baseResp;
	}


	@Override
	public BaseResp<List<ClassroomChapter>> selectChapterByCid(long classroomid, Integer startNo, Integer pageSize) {
		BaseResp<List<ClassroomChapter>> baseResp = new BaseResp<>();
		try{
			List<ClassroomChapter> chapterList = classroomChapterMapper.selectChapterByCid(classroomid, startNo, pageSize);
			baseResp.setData(chapterList);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		}catch(Exception e){
			logger.error("selectChapterByCid classroomid = {}, startNo = {}, pageSize = {}", 
					classroomid, startNo, pageSize, e);
        }
    	return baseResp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> updateIsdel(long classroomid, long chapterid) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			Integer count = classroomCoursesMapper.selectCountByCidAndChapterid(classroomid, chapterid);
			if(count > 0){
				return baseResp.initCodeAndDesp(Constant.STATUS_SYS_1116, Constant.RTNINFO_SYS_1116);
			}
			int temp = classroomChapterMapper.updateIsdel(classroomid, chapterid);
			if(temp>0){
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		}catch(Exception e){
			logger.error("updateIsdel classroomid = {}, chapterid = {}", 
					classroomid, chapterid, e);
        }
    	return baseResp;
	}

	@Override
	public BaseResp<Object> updateSortByid(long classroomid, long chapterid, Integer sort) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			int temp = classroomChapterMapper.updateSortByid(classroomid, chapterid, sort);
			if(temp>0){
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		}catch(Exception e){
			logger.error("updateSortByid classroomid = {}, chapterid = {}, sort = {}", 
					classroomid, chapterid, sort, e);
        }
    	return baseResp;
	}

	@Override
	public BaseResp<Object> updateDisplayByid(long classroomid, long chapterid, String display) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			int temp = classroomChapterMapper.updateDisplayByid(classroomid, chapterid, display);
			if(temp>0){
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		}catch(Exception e){
			logger.error("updateDisplayByid classroomid = {}, chapterid = {}, display = {}", 
					classroomid, chapterid, display, e);
        }
    	return baseResp;
	}

}
