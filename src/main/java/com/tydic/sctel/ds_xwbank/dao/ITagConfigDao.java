package com.tydic.sctel.ds_xwbank.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by shandaxw on 2016/12/8.
 */
public interface ITagConfigDao {
    /**
     * 查询serviceId对应的配置项集合
     * @param serviceId serviceId
     * @return 配置项集合
     */
    List<String> findConfigByServiceId(String serviceId);
}
