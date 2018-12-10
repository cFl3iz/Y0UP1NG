package main.java.com.banfftech.boom;

import main.java.com.banfftech.boom.bean.MailInfo;
import main.java.com.banfftech.boom.bean.MyAuthenticator;
import org.apache.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 邮件（可带多个附件）发送器
 */
public class MailSender {
    private static final Logger logger = Logger.getLogger(MailSender.class);

    /**
     * 发送邮件
     * @param mailInfo 待发送的邮件信息
     */
    public static boolean sendMail(MailInfo mailInfo) {
        // 判断是否需要身份认证
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        // 如果需要身份认证，则创建一个密码验证器
        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        //Session sendMailSession = Session.getDefaultInstance(pro, authenticator);//废弃旧Session
        Session sendMailSession = Session.getInstance(pro, authenticator);//为保证邮箱通道配置变更后立马生效,需每次创建新的Session
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailInfo.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(mailInfo.getToAddress());
            // Message.RecipientType.TO属性表示接收者的类型为TO
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart multipart = new MimeMultipart();
            // 创建一个MimeBodyPart
            BodyPart bodyPart = new MimeBodyPart();
            // 设置内容及格式
            bodyPart.setContent(mailInfo.getContent(), mailInfo.getContentType()+"; charset=utf-8");
            multipart.addBodyPart(bodyPart);
            // 添加附件的内容
            List<File> attachments=mailInfo.getAttachments();
            if(attachments!=null){
                for (int i = 0; i < attachments.size(); i++) {
                    BodyPart attachmentBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(attachments.get(i));
                    attachmentBodyPart.setDataHandler(new DataHandler(source));
                    // MimeUtility.encodeWord可以避免文件名乱码
                    attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachments.get(i).getName()));
                    multipart.addBodyPart(attachmentBodyPart);
                }
            }
            // 将MiniMultipart对象设置为邮件内容
            mailMessage.setContent(multipart);
            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (Exception e) {
            logger.error("发送邮件异常:", e);
        }
        return false;
    }


}
