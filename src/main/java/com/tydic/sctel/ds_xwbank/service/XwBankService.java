package com.tydic.sctel.ds_xwbank.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by kyle on 2016/11/25.
 */
@WebService(targetNamespace = "http://service.sctel.tydic.com/")
public interface XwBankService {
    @WebMethod(action = "userTagQuery")
    String userTagQuery(String json);
}
