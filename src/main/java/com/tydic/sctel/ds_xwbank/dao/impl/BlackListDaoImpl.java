package com.tydic.sctel.ds_xwbank.dao.impl;

import com.tydic.sctel.ds_xwbank.dao.IBlackListDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shandaxw on 2016/12/8.
 */
@Repository(value = "blackListDao")
public class BlackListDaoImpl implements IBlackListDao{

    private static final Logger LOGGER = LoggerFactory.getLogger(BlackListDaoImpl.class);

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public long findIsExistInBlackList(String phone, String idcard) {
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("BlackListDaoImpl.findIsExistInBlackList method parameter: phone:"+phone+",idcard:"+idcard);
        }
        String sql="select count(1) as total from black_list where val=:phone or val=:idcard";
        Map<String,Object> map=new HashMap<>();
        map.put("phone",phone);
        map.put("idcard",idcard);
        long count=0;
        Map<String, Object> resultMap = null;
        try {
            resultMap = namedParameterJdbcTemplate.queryForMap(sql, map);
        }catch (Exception e){
            LOGGER.debug(e.getMessage());
            e.printStackTrace();
        }
        count=(long)resultMap.get("total");
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("BlackListDaoImpl.findIsExistInBlackList method return: "+count);
        }
        return count;
    }

    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    @Autowired
    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
}
