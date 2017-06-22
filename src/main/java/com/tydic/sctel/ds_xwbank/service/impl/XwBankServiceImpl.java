package com.tydic.sctel.ds_xwbank.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tydic.sctel.ds_xwbank.dao.TagDao;
import com.tydic.sctel.ds_xwbank.service.XwBankService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebService;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kyle on 2016/11/25.
 */
@WebService(targetNamespace = "http://service.sctel.tydic.com/")
@Service("xwBankService")
public class XwBankServiceImpl implements XwBankService {
    private static final Logger LOGGER = LoggerFactory.getLogger(XwBankServiceImpl.class);

    private TagDao tagDao;

    @Autowired
    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public String userTagQuery(String jsonStr) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            LOGGER.debug("request json: \n" + jsonStr);
            Map requestMap = null;
            try {
                ObjectMapper mapper = new ObjectMapper();
                requestMap = mapper.readValue(jsonStr, Map.class);
            } catch (Exception e) {
                LOGGER.error("request json parse error\n{}", jsonStr);
                throw new Exception("request json parse error", e);
            }
            Map handBackMap = (Map) requestMap.get("handback");
            if (handBackMap != null) {//这个需要返回
                map.put("handback", handBackMap);
            }

            Map operationParamMap = (Map) requestMap.get("operationparam");
            if (operationParamMap == null) {
                LOGGER.error("operationparam is null");
                throw new Exception("operationparam is null");
            }

            String serviceid = StringUtils.trimToNull((String) operationParamMap.get("serviceid"));
            String username = StringUtils.trimToNull((String) operationParamMap.get("username"));
            String identitycard = StringUtils.trimToNull((String) operationParamMap.get("identitycard"));
            String phonenumber = StringUtils.trimToNull((String) operationParamMap.get("phonenumber"));

            if (!StringUtils.equals(serviceid, "1001")) {
                LOGGER.debug("service id is not equal 1001");
                throw new Exception("service id is not equal 1001");
            }
            Map<String, Object> tmpMap = tagDao.search(serviceid, identitycard, phonenumber);
            if (tmpMap != null) {
                map.putAll(tmpMap);
            }
            map.put("success", true);
        } catch (Exception e) {
            LOGGER.error("query error", e);
            map.put("success", false);
            map.put("error", e.getMessage());
        }
        ObjectMapper mapper = new ObjectMapper();
        String respJson = null;
        try {
            respJson = mapper.writeValueAsString(map);
            LOGGER.debug("response json: \n" + respJson);
        } catch (JsonProcessingException e) {
            LOGGER.error("marshal map to json error", e);
        }
        return respJson;
    }
}
