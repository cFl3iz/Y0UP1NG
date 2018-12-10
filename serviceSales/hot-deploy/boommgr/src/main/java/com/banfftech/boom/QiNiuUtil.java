package main.java.com.banfftech.boom;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

import java.io.IOException;

public class QiNiuUtil {
	 //设置好账号的ACCESS_KEY和SECRET_KEY
    static String ACCESS_KEY = "tju2NO7JjmPqX9fO1IuC9w44aNmiBIzhhFkIr18e";
    static String SECRET_KEY = "tZirm_YOrP94PD67Rz5lswQZ5xUEnrJPOoP32AOn";
    //要上传的空间
    static String bucketname = "dc-server";
    //上传到七牛后保存的文件名
    String key = "my-java.png";
    //上传文件的路径
    String FilePath = "d:/test.jpg";

    //密钥配置
    static Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    ///////////////////////指定上传的Zone的信息//////////////////
    //第一种方式: 指定具体的要上传的zone
    //注：该具体指定的方式和以下自动识别的方式选择其一即可
    //要上传的空间(bucket)的存储区域为华东时
    // Zone z = Zone.zone0();
    //要上传的空间(bucket)的存储区域为华北时
    // Zone z = Zone.zone1();
    //要上传的空间(bucket)的存储区域为华南时
    // Zone z = Zone.zone2();

    //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
    static Zone z = Zone.autoZone();
    static Configuration c = new Configuration(z);

    //创建上传对象
    static UploadManager uploadManager = new UploadManager(c);

   

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public static String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    public static String upload(String key,byte[] data) throws IOException {
        try {
            //调用put方法上传
            Response res = uploadManager.put(data, key, getUpToken());
            //打印返回的信息
            System.out.println(res.bodyString());
            return res.bodyString();
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString()); 
            try {
                //响应的文本信息
                System.out.println(r.bodyString());
                return r.bodyString();
            } catch (QiniuException e1) {
                //ignore
            }
        }
        return null;
    }
}
