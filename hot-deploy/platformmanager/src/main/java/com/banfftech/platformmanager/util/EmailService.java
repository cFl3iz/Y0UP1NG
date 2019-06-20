package com.banfftech.platformmanager.util;

import org.apache.ofbiz.base.crypto.HashCrypt;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class EmailService {
    public static final String module = EmailService.class.getName();
    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    public static final String resource = "SecurityextUiLabels";






    /**
     * Send Email
     * @author  S
     * @param map
     * @param mailTo Address
     * @throws java.net.UnknownHostException
     */
    public static void sendMail(Map map, String[] mailTo, String title) throws UnknownHostException {
        try {
            String fromMail = "information@ivcinc.cn";//UtilProperties.getPropertyValue("sendMailConfig", "SEND_MAIL");
            String port = "465";//UtilProperties.getPropertyValue("sendMailConfig", "SMTP_PORT");
            final String username ="information@ivcinc.cn";// UtilProperties.getPropertyValue("sendMailConfig", "SMTP_USER_NAME");
            final String password = "Aland2016";//UtilProperties.getPropertyValue("sendMailConfig", "SMTP_USER_PASSWORD");
//            String smtpService = UtilProperties.getPropertyValue("sendMailConfig", "SMTP_SERVER");
            String smtpService ="smtp.exmail.qq.com";//QQ
            Properties _props = System.getProperties();
            _props.put("mail.smtp.host", smtpService);// 放置邮件服务器地址
            _props.put("mail.smtp.auth", "true");
            _props.put("mail.transport.protocol", "smtp");
            _props.put("mail.smtp.socketFactory.port", port);
            _props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
            _props.put("mail.smtp.socketFactory.fallback", "false");
            _props.put("mail.smtp.sendpartial", "true");//部分发送，有错误地址可跳过
            Session session = Session.getDefaultInstance(_props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            session.setDebug(true);

            InetAddress addr = InetAddress.getLocalHost();
            String ip = addr.getHostAddress().toString();// 获得本机IP
            String address = addr.getHostName().toString();// 机器名

            Multipart mp = new MimeMultipart("related");// related意味着可以发送html格式的邮件
            BodyPart bodyPart = new MimeBodyPart();// 正文
            bodyPart.setDataHandler(new DataHandler(map.get("html"), "text/html;charset=GBK"));// 网页格式
            mp.addBodyPart(bodyPart);

            //每次发送20封
            InternetAddress[] toArrar = null;
            if(mailTo.length>20){
                toArrar = new InternetAddress[20];
            }else{
                toArrar = new InternetAddress[mailTo.length];
            }
            for (int i = 0; i < mailTo.length; i++) {
                Debug.log("==================mailTo:"+mailTo[i]);
                toArrar[i%20] = new InternetAddress(mailTo[i]);
                if((i+1)%20 == 0){
                    MimeMessage message = new MimeMessage(session);// 多用途网际邮件扩充协议的邮件信息对象
                    message.setFrom(new InternetAddress(fromMail));// 发送者信息
                    message.setSubject(title, "UTF-8");// 主题
                    message.setContent(mp);
                    message.addRecipients(Message.RecipientType.TO, toArrar);// 接收者地址
                    Transport.send(message);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Debug.log("send mail sleep 2s error:"+e.toString());
                    }
                    if((mailTo.length-i-1)>20){
                        toArrar = new InternetAddress[20];
                    }else{
                        toArrar = new InternetAddress[(mailTo.length - i - 1)];
                    }
                }else if(i == (mailTo.length-1) && (i+1)%20 != 0){
                    MimeMessage message = new MimeMessage(session);// 多用途网际邮件扩充协议的邮件信息对象
                    message.setFrom(new InternetAddress(fromMail));// 发送者信息
                    message.setSubject(title, "UTF-8");// 主题
                    message.setContent(mp);
                    message.addRecipients(Message.RecipientType.TO, toArrar);// 接收者地址
                    Transport.send(message);
                }
            }
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Object> sendEmailNotification(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException ,UnknownHostException {

        String content = (String) context.get("content");
        String title = (String) context.get("title");
        String [] mailTo = new String[]{"yinlin.shen@banff-tech.com",
                "longxi.jin@banff-tech.com",
                "hao.feng@banff-tech.com",
                "longxiang.xia@banff-tech.com",
                "jimeng.zou@banff-tech.com",
                "shijun.shao@comonetwork.com"};
        try {
            String fromMail = "yinlin.shen@banff-tech.com";//UtilProperties.getPropertyValue("sendMailConfig", "SEND_MAIL");
            String port = "465";//UtilProperties.getPropertyValue("sendMailConfig", "SMTP_PORT");
            final String username ="yinlin.shen@banff-tech.com";// UtilProperties.getPropertyValue("sendMailConfig", "SMTP_USER_NAME");
            final String password = "woaizhu131";//UtilProperties.getPropertyValue("sendMailConfig", "SMTP_USER_PASSWORD");
//            String smtpService = UtilProperties.getPropertyValue("sendMailConfig", "SMTP_SERVER");
            String smtpService ="smtp.exmail.qq.com";//QQ
            Properties _props = System.getProperties();
            _props.put("mail.smtp.host", smtpService);// 放置邮件服务器地址
            _props.put("mail.smtp.auth", "true");
            _props.put("mail.transport.protocol", "smtp");
            _props.put("mail.smtp.socketFactory.port", port);
            _props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
            _props.put("mail.smtp.socketFactory.fallback", "false");
            _props.put("mail.smtp.sendpartial", "true");//部分发送，有错误地址可跳过
            Session session = Session.getDefaultInstance(_props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            session.setDebug(true);

            InetAddress addr = InetAddress.getLocalHost();
            String ip = addr.getHostAddress().toString();// 获得本机IP
            String address = addr.getHostName().toString();// 机器名

            Multipart mp = new MimeMultipart("related");// related意味着可以发送html格式的邮件
            BodyPart bodyPart = new MimeBodyPart();// 正文
            bodyPart.setDataHandler(new DataHandler(content, "text/html;charset=GBK"));// 网页格式
            mp.addBodyPart(bodyPart);

            //每次发送20封
            InternetAddress[] toArrar = null;
            if(mailTo.length>20){
                toArrar = new InternetAddress[20];
            }else{
                toArrar = new InternetAddress[mailTo.length];
            }
            for (int i = 0; i < mailTo.length; i++) {
                Debug.log("==================mailTo:"+mailTo[i]);
                toArrar[i%20] = new InternetAddress(mailTo[i]);
                if((i+1)%20 == 0){
                    MimeMessage message = new MimeMessage(session);// 多用途网际邮件扩充协议的邮件信息对象
                    message.setFrom(new InternetAddress(fromMail));// 发送者信息
                    message.setSubject(title, "UTF-8");// 主题
                    message.setContent(mp);
                    message.addRecipients(Message.RecipientType.TO, toArrar);// 接收者地址
                    Transport.send(message);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Debug.log("send mail sleep 2s error:"+e.toString());
                    }
                    if((mailTo.length-i-1)>20){
                        toArrar = new InternetAddress[20];
                    }else{
                        toArrar = new InternetAddress[(mailTo.length - i - 1)];
                    }
                }else if(i == (mailTo.length-1) && (i+1)%20 != 0){
                    MimeMessage message = new MimeMessage(session);// 多用途网际邮件扩充协议的邮件信息对象
                    message.setFrom(new InternetAddress(fromMail));// 发送者信息
                    message.setSubject(title, "UTF-8");// 主题
                    message.setContent(mp);
                    message.addRecipients(Message.RecipientType.TO, toArrar);// 接收者地址
                    Transport.send(message);
                }
            }
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return ServiceUtil.returnSuccess();
    }



}


