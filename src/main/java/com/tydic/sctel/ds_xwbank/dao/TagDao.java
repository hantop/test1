package com.tydic.sctel.ds_xwbank.dao;

import java.util.Map;

/**
 * Created by kyle on 2016/11/25.
 */
public interface TagDao {
    Map<String,Object> search(String serviceid, String identitycard, String phonenumber) throws Exception;
}
