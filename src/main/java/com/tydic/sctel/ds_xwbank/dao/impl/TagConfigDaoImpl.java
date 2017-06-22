package com.tydic.sctel.ds_xwbank.dao.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tydic.sctel.ds_xwbank.dao.ITagConfigDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shandaxw on 2016/12/8.
 */
@Repository(value = "targetConfigDao")
public class TagConfigDaoImpl implements ITagConfigDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagConfigDaoImpl.class);

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Override
    public List<String> findConfigByServiceId(String serviceId) {
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("TagConfigDaoImpl.findConfigByServiceId method parameter: serviceId:"+serviceId);
        }
        List<String> list=null;
        String sql="select tag_id  from tag_config where service_id=:service_id";
        Map<String,Object> map=new HashMap<>();
        map.put("service_id",serviceId);
        try {
            list= namedParameterJdbcTemplate.queryForList(sql, map, String.class);
        }catch (Exception e){
            LOGGER.debug(e.getMessage());
            e.printStackTrace();
        }
        if(LOGGER.isDebugEnabled()){
            try {
                LOGGER.debug("BlackListDaoImpl.findIsExistInBlackList method return: "+new ObjectMapper().writeValueAsString(list));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Autowired
    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
}
