package com.tydic.sctel.ds_xwbank.dao.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tydic.sctel.ds_xwbank.dao.TagDao;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * Created by kyle on 2016/11/25.
 */
@Repository
public class TagDaoImpl implements TagDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagDaoImpl.class);
    private ElasticsearchTemplate template;

    @Autowired
    public void setTemplate(ElasticsearchTemplate template) {
        this.template = template;
    }

    @Override
    public Map<String, Object> search(String serviceid, String identitycard, String phonenumber) throws Exception {
        Map<String, Object> resultMap = null;
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                //.withIndices("tag_aliases")
                .withIndices("tag_test")
                .withTypes("USER_TAG")
                .withQuery(boolQuery()
                        .must(termQuery("T10000136", identitycard))
                        .must(termQuery("T10000001", phonenumber))
                )
                .build();

        List<Map> result = template.queryForList(searchQuery, Map.class);
        if (result != null && result.size() > 0) {
            Map map = result.get(0);
            if (LOGGER.isDebugEnabled()) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    String json = objectMapper.writeValueAsString(map);
                    LOGGER.debug(">>request params: T10000136={},T10000001={}\n{}", identitycard, phonenumber, json);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            String T10000195 = StringUtils.trimToEmpty((String) map.get("T10000195"));//手机号码状态，是指停机、在用、欠费等状态
            String T10000206 = StringUtils.trimToNull((String) map.get("T10000206"));//手机号码-月累计上网时长区间，需要最近1个月的上网时长区间值
            String T10000207 = StringUtils.trimToNull((String) map.get("T10000207"));//手机号码-月累计上网流量区间，需要最近1个月的上网流量区间值
            String T10000208 = StringUtils.trimToNull((String) map.get("T10000208"));//手机号码-指定月通话时长阶梯，需要最近1月的通话时长区间值
            String T10000209 = StringUtils.trimToNull((String) map.get("T10000209"));//短信沟通次数，需要“月均手机短信接收量区间均值”
            String T10000210 = StringUtils.trimToNull((String) map.get("T10000210"));//手机号码-在网时长区间
            String T10000211 = StringUtils.trimToNull((String) map.get("T10000211"));//终端价格
            String T10000212 = StringUtils.trimToNull((String) map.get("T10000212"));//宽带在网时长
            String T10000213 = StringUtils.trimToNull((String) map.get("T10000213"));//历史信用得分
            resultMap = new HashMap<String, Object>();

            String status;
            if (T10000195.equalsIgnoreCase("-1")) {
                status = "未知状态";
            } else if (T10000195.equalsIgnoreCase("1")) {
                status = "在用";
            } else if (T10000195.equalsIgnoreCase("10")) {
                status = "违章拆机";
            } else if (T10000195.equalsIgnoreCase("11")) {
                status = "施工未完";
            } else if (T10000195.equalsIgnoreCase("12")) {
                status = "障碍";
            } else if (T10000195.equalsIgnoreCase("13")) {
                status = "预拆机";
            } else if (T10000195.equalsIgnoreCase("14")) {
                status = "资源封锁";
            } else if (T10000195.equalsIgnoreCase("15")) {
                status = "ADSL加锁停机";
            } else if (T10000195.equalsIgnoreCase("16")) {
                status = "预开户待激活";
            } else if (T10000195.equalsIgnoreCase("17")) {
                status = "停机3月以上拆机";
            } else if (T10000195.equalsIgnoreCase("18")) {
                status = "无";
            } else if (T10000195.equalsIgnoreCase("2")) {
                status = "用户报停";
            } else if (T10000195.equalsIgnoreCase("25")) {
                status = "非实名违章单停";
            } else if (T10000195.equalsIgnoreCase("26")) {
                status = "非实名违章双停";
            } else if (T10000195.equalsIgnoreCase("3")) {
                status = "用户拆机";
            } else if (T10000195.equalsIgnoreCase("4")) {
                status = "预先";
            } else if (T10000195.equalsIgnoreCase("5")) {
                status = "欠停（双向）";
            } else if (T10000195.equalsIgnoreCase("6")) {
                status = "欠停（单向）";
            } else if (T10000195.equalsIgnoreCase("7")) {
                status = "违章停机";
            } else if (T10000195.equalsIgnoreCase("8")) {
                status = "挂失";
            } else if (T10000195.equalsIgnoreCase("9")) {
                status = "欠费拆机";
            } else if (T10000195.equalsIgnoreCase("98")) {
                status = "无";
            } else {
                status = "未知状态(" + T10000195 + ")";
            }

            long online_time = 0L;
            if (T10000206 == null || !NumberUtils.isNumber(T10000206)) {
                LOGGER.warn("T10000206({}) is not a number", T10000206);
                throw new Exception("T10000206(" + T10000206 + ") is not a number");
            }
            online_time = NumberUtils.createLong(T10000206);

            long phone_traffic = 0L;
            if (T10000207 == null || !NumberUtils.isNumber(T10000207)) {
                LOGGER.warn("T10000207({}) is not a number", T10000207);
                throw new Exception("T10000207(" + T10000207 + ") is not a number");
            }
            phone_traffic = NumberUtils.createLong(T10000207);

            long call_time = 0L;
            if (T10000208 == null || !NumberUtils.isNumber(T10000208)) {
                LOGGER.warn("T10000208({}) is not a number", T10000208);
                throw new Exception("T10000208(" + T10000208 + ") is not a number");
            }
            call_time = NumberUtils.createLong(T10000208);

            long sms = 0L;
            if (T10000209 == null || !NumberUtils.isNumber(T10000209)) {
                LOGGER.warn("T10000209({}) is not a number", T10000209);
                throw new Exception("T10000209(" + T10000209 + ") is not a number");
            }
            sms = NumberUtils.createLong(T10000209);

            long now = System.currentTimeMillis();

            long phone_zwsc = 0L;//手机在网时间, 单位:毫秒
            try {
                phone_zwsc = DateUtils.parseDate(T10000210, new String[]{"yyyy-MM-dd HH:mm:ss.S"}).getTime();
            } catch (Exception e) {
                LOGGER.error("parse T10000210({}) with format yyyy-MM-dd HH:mm:ss.S error", T10000210);
                throw new Exception("parse T10000210(" + T10000210 + ") with format yyyy-MM-dd HH:mm:ss.S error");
            }


            long kd_zwsc = 0L;//宽带在网时间, 单位:毫秒
            try {
                kd_zwsc = DateUtils.parseDate(T10000212, new String[]{"yyyy-MM-dd HH:mm:ss.S"}).getTime();
            } catch (Exception e) {
                LOGGER.error("parse T10000212({}) with format yyyy-MM-dd HH:mm:ss.S error", T10000212);
                throw new Exception("parse T10000212(" + T10000212 + ") with format yyyy-MM-dd HH:mm:ss.S error");
            }

            long phone_zwsc_cha = now - phone_zwsc;
            long kd_zwsc_cha = now - kd_zwsc;
            if(T10000211==null|| !NumberUtils.isNumber(T10000211)){
                LOGGER.warn("T10000211({}) is not a number", T10000211);
                throw new Exception("T10000211(" + T10000211 + ") is not a number");
            }
            long price=NumberUtils.createLong(T10000211);
            String credit=T10000213;

            resultMap.put("T10000195", status);
            resultMap.put("T10000206", (long)Math.ceil((double)online_time / 60/ 30));                   //手机号码-月累计上网时长区间, 30分钟一个区间, 原始数据单位:秒, 需要转换成分钟,
            resultMap.put("T10000207", (long)Math.ceil((double)phone_traffic / 1024 / 100));             //手机号码-月累计上网流量区间, 100M一个区间, 原始数据单位:kb, 需转成M
            resultMap.put("T10000208", (long)Math.ceil((double)call_time / 60 / 30));                     //手机号码-月通话时长区间, 原始数据单位:单位分钟
            resultMap.put("T10000209", (long)Math.ceil((double)sms / 10));                               //短信次数, 10条一个区间
            resultMap.put("T10000210", (long)Math.ceil((double)phone_zwsc_cha / 1000 / 60 / 60 / 24));   //手机在网时长, 30天一个区间, 原始数据格式:yyyy-MM-dd HH:mm:ss.Z
            resultMap.put("T10000211", (long)Math.ceil((double)price/500));                              //终端价格, 500元一个区间, 原始数据单位:元
            resultMap.put("T10000212", (long)Math.ceil((double)kd_zwsc_cha / 1000 / 60 / 60 / 24 / 30 / 3));   //宽带在网时长, 3个月一个区间,原始数据单位:yyyy-MM-dd HH:mm:ss.Z
            resultMap.put("T10000213", credit);                            //历史信用得分
        }else{
            throw new Exception("未找到记录");
        }
        return resultMap;
    }
}
