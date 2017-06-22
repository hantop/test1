package com.tydic.sctel.ds_xwbank.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kyle on 2016/11/24.
 */
@Controller
@RequestMapping(value = "/xwbank")
public class UserTagQueryController {
    private static final Logger LOGGER= LoggerFactory.getLogger(UserTagQueryController.class);

    @RequestMapping(value = "/user_tag_query",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> query(@RequestBody String jsonStr){
        Map<String,Object> map=new HashMap<String,Object>();
        try {

            Map requestMap=null;
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.readValue(jsonStr, Map.class);
            }catch (Exception e){
                throw new Exception("request json format error",e);
            }
            Map handBackMap= (Map) requestMap.get("handback");
            if(handBackMap!=null){//这个需要返回
                map.put("handback",handBackMap);
            }

            Map operationParamMap= (Map) requestMap.get("operationparam");
            if(operationParamMap==null){
                throw new Exception("operationparam is null");
            }
            String serviceid= (String) operationParamMap.get("serviceid");
            String username= (String) operationParamMap.get("username");
            String identitycard= (String) operationParamMap.get("identitycard");
            String phonenumber= (String) operationParamMap.get("phonenumber");



            LOGGER.debug("request json: \n"+jsonStr);
            map.put("success",true);
        }catch (Exception e){
            LOGGER.error("query error",e);
            map.put("success",false);
            map.put("error",e.getMessage());
        }
        if(LOGGER.isDebugEnabled()){
            ObjectMapper mapper = new ObjectMapper();
            try {
                String json=mapper.writeValueAsString(map);
                LOGGER.debug("response json: \n"+json);
            } catch (JsonProcessingException e) {
                LOGGER.error("mashall map to json error",e);
            }
        }
        return map;
    }
}
