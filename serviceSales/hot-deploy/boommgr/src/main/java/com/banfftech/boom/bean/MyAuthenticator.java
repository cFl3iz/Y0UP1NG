package main.java.com.banfftech.boom.bean;

/**
 * Created by S on 2018/8/8.
 */
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;import java.lang.String;

/**
 * 邮箱授权类
 */
public class MyAuthenticator extends Authenticator{
    String userName=null;
    String password=null;

    public MyAuthenticator(){
    }
    public MyAuthenticator(String username, String password) {
        this.userName = username;
        this.password = password;
    }
    protected PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(userName, password);
    }


}
