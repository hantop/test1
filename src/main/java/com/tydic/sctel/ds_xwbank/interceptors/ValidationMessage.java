package com.tydic.sctel.ds_xwbank.interceptors;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.saaj.SAAJInInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import java.util.List;

/**
 * Created by shandaxw on 2016/12/5.
 */
public class ValidationMessage extends AbstractPhaseInterceptor<SoapMessage> {
    private SAAJInInterceptor saa = new SAAJInInterceptor();

    public ValidationMessage() {
        super(Phase.PRE_PROTOCOL);
        getAfter().add(SAAJInInterceptor.class.getName());
    }
    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        SOAPMessage soapMsg = message.getContent(SOAPMessage.class);
        if (soapMsg == null){
            saa.handleMessage(message);
            soapMsg = message.getContent(SOAPMessage.class);
        }
        SOAPHeader header = null;
        try {
            header = soapMsg.getSOAPHeader();
            NodeList userNodes = header.getElementsByTagName("username");
            NodeList passNodes = header.getElementsByTagName("password");
            if(userNodes!=null && passNodes!=null){
                Node userNode = userNodes.item(0);
                Node passNode = passNodes.item(0);
                //验证节点是否存在
                if(userNode==null || passNode== null){
                    throw new Fault(new Exception("auth error!   Auth header is not complete"));
                }
                String username = userNode.getTextContent();
                String password = passNode.getTextContent();
                //认证信息是否完善
                if(!StringUtils.isNotBlank(username)||!StringUtils.isNotBlank(password)){
                    throw new Fault(new Exception("auth error!   username and password canot be empty"));
                }
                if(!StringUtils.equals(username,"xwbank")){
                    throw new Fault(new Exception("auth error!   username is wrong"));
                }
                if(!StringUtils.equals(password,DigestUtils.md5Hex("123456") )){
                    throw new Fault(new Exception("auth error!   password is wrong"));
                }
                return ;
            }else{
                throw new Fault(new Exception("auth error."));
            }
        } catch (SOAPException e) {
            e.printStackTrace();
        }
        throw new Fault(new Exception("auth error."));
      }
    }