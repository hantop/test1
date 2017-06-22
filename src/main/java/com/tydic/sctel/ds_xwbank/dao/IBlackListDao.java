package com.tydic.sctel.ds_xwbank.dao;

/**
 * Created by shandaxw on 2016/12/8.
 */
public interface IBlackListDao {

    /**
     * 判断电话号码或身份证是否出现在黑名单内
     * @param phone 电话号码
     * @param idcard 身份证
     * @return  存在的条数
     */
    long findIsExistInBlackList(String phone,String idcard);
}
