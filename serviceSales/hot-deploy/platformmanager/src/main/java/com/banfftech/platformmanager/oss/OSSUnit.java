package main.java.com.banfftech.platformmanager.oss;


import java.io.File;  
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;  
import java.util.ResourceBundle;  
  

import java.util.HashMap;
import java.util.LinkedList;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.FileUtil;
import org.apache.ofbiz.base.util.StringUtil;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilGenerics;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.model.ModelEntity;
import org.apache.ofbiz.entity.transaction.GenericTransactionException;
import org.apache.ofbiz.entity.transaction.TransactionUtil;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.minilang.MiniLangException;
import org.apache.ofbiz.minilang.SimpleMapProcessor;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ModelService;
import org.apache.ofbiz.service.ServiceAuthException;
import org.apache.ofbiz.service.ServiceUtil;
import com.aliyun.oss.OSSClient;  
import com.aliyun.oss.model.Bucket;  
import com.aliyun.oss.model.OSSObject;  
import com.aliyun.oss.model.ObjectMetadata;  
import com.aliyun.oss.model.PutObjectResult;



public class OSSUnit {

	 public final static String module = OSSUnit.class.getName();
	 
    //阿里云API的内或外网域名  
    private static String ENDPOINT = "oss-cn-hangzhou.aliyuncs.com";  
    //阿里云API的密钥Access Key ID  
    private static String ACCESS_KEY_ID ="LTAIMIU42pe743cb";  
    //阿里云API的密钥Access Key Secret  
    private static String ACCESS_KEY_SECRET = "9G5xDQFq5YPfP6l1wyN2LiH9Nw6I3f";  
      
    
    //init static datas  
    static{  
//        ResourceBundle bundle = PropertyResourceBundle.getBundle("properties.oss");  
//        ENDPOINT = bundle.containsKey("endpoint") == false ? "" : bundle.getString("endpoint");  
//        ACCESS_KEY_ID = bundle.containsKey("accessKeyId") == false? "" : bundle.getString("accessKeyId");  
//        ACCESS_KEY_SECRET = bundle.containsKey("accessKeySecret") == false ? "" : bundle.getString("accessKeySecret");  
    }  
      
    /** 
     * 获取阿里云OSS客户端对象 
     * */  
    public static final OSSClient getOSSClient(){  
        return new OSSClient(ENDPOINT,ACCESS_KEY_ID, ACCESS_KEY_SECRET);  
    }  
      
    /** 
     * 新建Bucket  --Bucket权限:私有 
     * @param bucketName bucket名称 
     * @return true 新建Bucket成功 
     * */  
    public static final boolean createBucket(OSSClient client, String bucketName){  
        Bucket bucket = client.createBucket(bucketName);   
        return bucketName.equals(bucket.getName());  
    }  
      
    /** 
     * 删除Bucket  
     * @param bucketName bucket名称 
     * */  
    public static final void deleteBucket(OSSClient client, String bucketName){  
        client.deleteBucket(bucketName);   
        
        Debug.logInfo("删除" + bucketName + "Bucket成功",module);
    }  
      
    
    
    
    
    
    
    /**
	  * testUpload
	  * 
	  * @param request
	  * @param response
	  * @return
	  */
	 public static String testUpload(HttpServletRequest request, HttpServletResponse response) {
			try {
				Locale locale = UtilHttp.getLocale(request);
				LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
				Delegator delegator = (Delegator) request.getAttribute("delegator");
				HttpSession session = request.getSession();
				GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

//				ServletFileUpload dfu = new ServletFileUpload(
//						new DiskFileItemFactory(10240, FileUtil.getFile("runtime/tmp")));
				ServletFileUpload dfu = new ServletFileUpload(new DiskFileItemFactory(10240,null));
				List<FileItem> items = dfu.parseRequest(request);

				for (FileItem item : items) {
					InputStream in = item.getInputStream();
					
//					String motherFuckID =  uploadObject2OSS(in,item.getName(),getOSSClient(),null,"personerp","datas/images/");
//					System.out.println(motherFuckID);
				}
		
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "success";

		}
    
    
   
	 
	 
	 
	 
    /** 
     * 向阿里云的OSS存储中存储文件  --file也可以用InputStream替代 
     * @param client OSS客户端 
     * @param file 上传文件 
     * @param bucketName bucket名称 
     * @param diskName 上传文件的目录  --bucket下文件的路径 
     * @return String 唯一MD5数字签名 
     * */  
    public static final String uploadObject2OSS(InputStream is,String fileName,OSSClient client, File file, String bucketName, String diskName,long tm) {  
        String resultStr = null;  
        try {  
            //InputStream is = new FileInputStream(file);  
            //String fileName = file.getName();  
            // Long fileSize = file.length();  
            //创建上传Object的Metadata  
            ObjectMetadata metadata = new ObjectMetadata();  
            metadata.setContentLength(is.available());  
            metadata.setCacheControl("no-cache");  
            metadata.setHeader("Pragma", "no-cache");  
            metadata.setContentEncoding("utf-8");  
            metadata.setContentType(getContentType(fileName));  
           // metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");  
            String fName = getContentType(fileName);
            //上传文件   
            PutObjectResult putResult = client.putObject(bucketName, diskName + tm+fileName.substring(fileName.indexOf(".")), is, metadata);  
            //解析结果  
            resultStr = putResult.getETag();  
        } catch (Exception e) {  
           
        	Debug.logInfo("上传阿里云OSS服务器异常.",module); 
        }  
        return resultStr;  
    }  
      
    /**  
     * 根据key获取OSS服务器上的文件输入流 
     * @param client OSS客户端 
     * @param bucketName bucket名称 
     * @param diskName 文件路径 
     * @param key Bucket下的文件的路径名+文件名 
     */    
     public static final InputStream getOSS2InputStream(OSSClient client, String bucketName, String diskName, String key){   
        OSSObject ossObj = client.getObject(bucketName, diskName + key);  
        return ossObj.getObjectContent();     
     }    
      
   /**  
    * 根据key删除OSS服务器上的文件  
    * @param client OSS客户端 
    * @param bucketName bucket名称 
    * @param diskName 文件路径 
    * @param key Bucket下的文件的路径名+文件名 
    */    
      public static void deleteFile(OSSClient client, String bucketName, String diskName, String key){    
        client.deleteObject(bucketName, diskName + key);   
        String eMessage ="删除" + bucketName + "下的文件" + diskName + key + "成功";  
        Debug.logInfo(eMessage,module);
      }    
       
    /**  
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType  
     * @param fileName 文件名 
     * @return 文件的contentType    
     */    
     public static final String getContentType(String fileName){    
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));  
        if("bmp".equalsIgnoreCase(fileExtension)) return "image/bmp";  
        if("gif".equalsIgnoreCase(fileExtension)) return "image/gif";  
        if("jpeg".equalsIgnoreCase(fileExtension) || "jpg".equalsIgnoreCase(fileExtension)  || "png".equalsIgnoreCase(fileExtension) ) return "image/jpeg";  
        if("html".equalsIgnoreCase(fileExtension)) return "text/html";  
        if("txt".equalsIgnoreCase(fileExtension)) return "text/plain";  
        if("vsd".equalsIgnoreCase(fileExtension)) return "application/vnd.visio";  
        if("ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) return "application/vnd.ms-powerpoint";  
        if("doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) return "application/msword";  
        if("xml".equalsIgnoreCase(fileExtension)) return "text/xml";  
        return "text/html";    
     }    
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     private static void uploadSmallFile(final OSSClient client, final String bucketName, 
     		final String key, final File uploadFile)
             throws  FileNotFoundException {
         ObjectMetadata objectMeta = new ObjectMetadata();
         objectMeta.setContentLength(uploadFile.length());
         // 可以在metadata中标记文件类型
         objectMeta.setContentType("application/pdf");
         //对object进行服务器端加密，目前服务器端只支持x-oss-server-side-encryption加密
         objectMeta.setHeader("x-oss-server-side-encryption", "AES256");
        final InputStream input = new FileInputStream(uploadFile);
         Thread t = new Thread(new Runnable() {
 			
 			@Override
 			public void run() {
 				InputStream tmpInput = null;
 				while(true){
 					//将input缓存在tmpInput中，防止在调用available()方法是异常导致上传失败
 					tmpInput = input;
 					try {
 						Thread.sleep(1000);
 						
 					} catch (InterruptedException e1) {
 						// TODO Auto-generated catch block
 						e1.printStackTrace();
 					}
 					try {
 						if(input!=null){
 							System.out.println(uploadFile.getName()+"的上传进度为"+(float)(uploadFile.length()-tmpInput.available())/uploadFile.length());
 							if(tmpInput.available() == 0){
 								break;
 							}
 						}else{
 							break;
 						}
 					} catch (IOException e) {
 						break;
 					}
 				}
 				//通过获取oss上文件的大小来判断是否上传成功，如果不能从oss获得文件的大小说明上传失败
 				try{
 					ObjectMetadata tmpObjectMeta = client.getObjectMetadata(bucketName, key);
 					System.out.println(uploadFile.getName()+"的上传进度为："+tmpObjectMeta.getContentLength()/uploadFile.length());
 				}catch(Exception e){
 					e.printStackTrace();
 					System.out.println(uploadFile.getName()+"上传失败");
 				}
 			}
 		});
        t.start();
         PutObjectResult result =client.putObject(bucketName, key, input, objectMeta);
         
        
         System.out.println("上传的object返回的E_tag："+result.getETag());
   //      System.out.println("上传是否成功："+ md5.equalsIgnoreCase(result.getETag()));
     }
}
