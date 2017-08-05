package com.longbei.appservice.dao.mongo.dao;

import com.longbei.appservice.common.dao.BaseMongoDao;
import com.longbei.appservice.common.utils.CodeGeneratorUtil;
import com.longbei.appservice.entity.CodeEntity;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * 唯一码
 *
 * @author luye
 * @create 2017-03-17 下午8:56
 **/
@Repository
public class CodeDao extends BaseMongoDao<CodeEntity>{


    public enum CodeType{
        rank,
        joinpwdrank
    }

    /**
     * 奖品获奖码
     * @param businessname
     * @return
     */
    public String getCode(String businessname){

        Criteria criteria = Criteria.where("businessname").is("rank");
        Query query = new Query(criteria);

        query.fields().slice("code",1);

        CodeEntity codeEntity = mongoTemplate.findOne(query,CodeEntity.class);
        if (null == codeEntity){
            CodeEntity codeEntity1 = new CodeEntity();
            Set<String> codelist = CodeGeneratorUtil.generatorCode(CodeType.rank,1,6);
            String[] codes = new String[codelist.size()];
            codelist.toArray(codes);
            codeEntity1.setCode(codes);
            mongoTemplate.save(codeEntity1);
        } else if (codeEntity.getCode() == null || codeEntity.getCode().length == 0){
            CodeEntity codeEntity1 = new CodeEntity();
            codeEntity1.setDatatype(codeEntity.getDatatype()+1);
            Set<String> codelist = CodeGeneratorUtil.generatorCode(CodeType.rank,codeEntity.getDatatype()+1,6);
            String[] codes = new String[codelist.size()];
            codelist.toArray(codes);
            codeEntity1.setCode(codes);
            mongoTemplate.save(codeEntity1);
        }
        codeEntity = mongoTemplate.findOne(query,CodeEntity.class);
        Update update = new Update();
        update = update.pull("code",codeEntity.getCode()[0]);
        mongoTemplate.updateFirst(query,update,CodeEntity.class);
        return codeEntity.getCode()[0];
    }

    /**
     * 榜单邀请码
     * @param businessname
     * @return
     */
    public String getRankPwdCode(String businessname){

        Criteria criteria = Criteria.where("businessname").is(businessname);
        Query query = new Query(criteria);

        query.fields().slice("code",1);

        CodeEntity codeEntity = mongoTemplate.findOne(query,CodeEntity.class);
        if (null == codeEntity){
            CodeEntity codeEntity1 = new CodeEntity();
            Set<String> codelist = CodeGeneratorUtil.generatorCode(CodeType.joinpwdrank,1,4);
            String[] codes = new String[codelist.size()];
            codelist.toArray(codes);
            codeEntity1.setCode(codes);
            codeEntity1.setBusinessname(CodeType.joinpwdrank.toString());
            mongoTemplate.save(codeEntity1);
        } else if (codeEntity.getCode() == null || codeEntity.getCode().length == 0){
            CodeEntity codeEntity1 = new CodeEntity();
            codeEntity1.setDatatype(codeEntity.getDatatype()+1);
            Set<String> codelist = CodeGeneratorUtil.generatorCode(CodeType.joinpwdrank,
                    codeEntity.getDatatype(),codeEntity.getLength()+1);
            String[] codes = new String[codelist.size()];
            codelist.toArray(codes);
            codeEntity1.setBusinessname(CodeType.joinpwdrank.toString());
            codeEntity1.setCode(codes);
            mongoTemplate.save(codeEntity1);
        }
        codeEntity = mongoTemplate.findOne(query,CodeEntity.class);
        Update update = new Update();
        update = update.pull("code",codeEntity.getCode()[0]);
        mongoTemplate.updateFirst(query,update,CodeEntity.class);
        return codeEntity.getCode()[0];
    }



}
