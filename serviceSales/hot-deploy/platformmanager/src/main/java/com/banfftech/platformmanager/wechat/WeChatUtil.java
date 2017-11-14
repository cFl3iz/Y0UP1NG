package main.java.com.banfftech.platformmanager.wechat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.sf.json.JSONException;


import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.http.HttpResponse;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import main.java.com.banfftech.platformmanager.constant.PeConstant;
import main.java.com.banfftech.platformmanager.util.EmojiFilter;
import main.java.com.banfftech.platformmanager.util.EmojiHandler;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.DispatchContext;
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

import static main.java.com.banfftech.platformmanager.util.HttpHelper.sendPost;


/**
 * Created by S.Y.L on 2017/5/2.
 */
public class WeChatUtil {

    public final static String module = WeChatUtil.class.getName();

    //此处应当从数据库取值,苟且先将硬代码使用。

    //We Chat App Key ID
    private static String APP_KEY = PeConstant.WECHAT_GZ_APP_ID;
    //We Caht Secret
    private static String ACCESS_KEY_SECRET = PeConstant.ACCESS_KEY_SECRET;



    public static String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接������
            URLConnection connection = realUrl.openConnection();
            // 设置通用请求属性��������
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立连接���
            connection.connect();
            // 获取响应头字段�
            // Map<String, List<String>> map = connection.getHeaderFields();
            //  遍历所有的响应头字段ֶ�
            // for (String key : map.keySet()) {
            // System.out.println(key + "--->" + map.get(key));
            // }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 get 异常" + e);
            e.printStackTrace();
        }
        // ʹ��finally�����ر�������
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        System.out.println();
        return result;
    }



    public static AccessToken getAccessToken(String appid, String appsecret) {
        AccessToken accessToken = null;
        // 获取access_token的接口地址（GET） 限200（次/天）
        String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
        JSONObject jsonObject = JSONObject.fromObject(sendGet(requestUrl));
        // 如果请求成功�
        if (null != jsonObject) {
            try {
                accessToken = new AccessToken();
                accessToken.setToken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
            } catch (JSONException e) {
                accessToken = null;
                // 获取token失败
                System.out.println("获取 token 失败errcode:" + jsonObject.getInt("errcode") + " errmsg:"
                        + jsonObject.getString("errmsg"));
            }
        }
        return accessToken;
    }


    /**
     * Get Access Token / By Code
     *
     * @author S.Y.L
     * @param  code
     * @return Map
     */
    public static Map<String,Object> getWeChatAccess(String code)  {


        Map<String,Object> returnMap = new HashMap<String, Object>();
        StringBuffer requestParamSB = new StringBuffer();

        requestParamSB.append("appid="+APP_KEY);//AK
        requestParamSB.append("&secret="+ACCESS_KEY_SECRET);//SC
        requestParamSB.append("&code="+code);//用户微信授权代码
        requestParamSB.append("&grant_type=authorization_code");//授予类型

        String responseStr = main.java.com.banfftech.platformmanager.util.HttpHelper.sendGet(PeConstant.WX_ACCESS_PATH,requestParamSB.toString());

        JSONObject jsonMap = JSONObject.fromObject(responseStr);

        if(null == jsonMap.get("access_token") || null== jsonMap.get("openid")){
            //Debug.logInfo("*Not Foud AccessToken " +module);
        }


        String accessToken = (String) jsonMap.get("access_token");
        String openId      = (String) jsonMap.get("openid");


        returnMap.put("accessToken",accessToken);
        returnMap.put("openId", openId);
        returnMap.put("code", code);

        System.out.println("*getWeChatAccess========================================================" + returnMap);

        return returnMap;
    }


//    /**
//     * Get Access Token
//     *
//     * @return
//     * @throws GenericServiceException
//     */
//    public static String getWeChatAccess()
//            throws GenericServiceException {
//
//        // Servlet Head
//
//
//        StringBuffer requestParamSB = new StringBuffer();
//        requestParamSB.append("?grant_type=client_credential");//授予类型
//        requestParamSB.append("&appid="+APP_KEY);//AK
//        requestParamSB.append("&secret="+ACCESS_KEY_SECRET);//SC
//
//
//        // Do HTTP Get
//        String responseStr = sendGet(PeConstant.WX_GET_ACCESS_TOKEN,requestParamSB.toString());
//
//        JSONObject jsonMap = JSONObject.fromObject(responseStr);
//
//        if(null == jsonMap.get("access_token") || null== jsonMap.get("openid")){
//            //THROW ERROR
//        }
//        String accessToken = (String) jsonMap.get("access_token");
//
//
//
//
//        return accessToken;
//    }






    public static String PostSendMsg(JSONObject json, String url) {
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Basic YWRtaW46");
        String result = "";
        try {
            StringEntity s = new StringEntity(json.toString(), "utf-8");
            s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(s);
            // 发送请求����
            HttpResponse httpResponse = HttpClients.createDefault().execute(post);
            // 响应输出流���
            InputStream inStream = httpResponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
            StringBuilder strber = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
                strber.append(line + "\n");
            inStream.close();

            result = strber.toString();
            System.out.println(result);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                System.out.println("请求服务成功 request success");
            } else {
                System.out.println("请求服务端失败 request error");
            }
        } catch (Exception e) {
            System.out.println("请求异常 request Exception");
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Get We Chat User Info
     *
     * @author S.Y.L
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> getWeChatUserInfo(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Map<String,String> weChatUserInfo = new HashMap<String, String>();

       StringBuffer requestParamSB = new StringBuffer();

        String contentAK=(String)context.get("accessToken");



        //此处微信做了更改，一律从新获取ak
        StringBuffer requestParamSBa = new StringBuffer();

        requestParamSBa.append("appid="+APP_KEY);//AK
        requestParamSBa.append("&secret="+ACCESS_KEY_SECRET);//SC
        requestParamSBa.append("&grant_type=client_credential");

        // Do HTTP Get
        String responseStrAk = sendPost(PeConstant.WX_USER_INFO_CGI_BIN_TOKEN_PATH, requestParamSBa.toString());

        JSONObject jsonMapAk = JSONObject.fromObject(responseStrAk);

        String realAk   =(String) jsonMapAk.get("access_token");

//        System.out.println("==================================================openId= " +context.get("openId"));
        if(context.get("openId")!=null){
            requestParamSB.append("access_token="+realAk);//AK
            requestParamSB.append("&openid="+(String)context.get("openId"));//AK
        }else{
            return result;
        }



        // Do HTTP Get
        String responseStr = sendGet(PeConstant.WX_USER_INFO_CGI_BIN_PATH,requestParamSB.toString());

        JSONObject jsonMap = JSONObject.fromObject(responseStr);
        System.out.println("==================================================jsonMap= " + jsonMap);
        if(null != jsonMap.get("nickname")){
            //关键字段:是否订阅,这将影响我们是否要他重复关注公众号
            weChatUserInfo.put("subscribe",jsonMap.get("subscribe")+"");
        }



        //用户信息又得单独去获取,否则不关注还拿不到了
        StringBuffer requestParamUserInfoSB = new StringBuffer();
        requestParamUserInfoSB.append("access_token="+contentAK);//AK
        requestParamUserInfoSB.append("&openid="+context.get("openId"));//SC


        String jsonUserInfoMapStr = sendGet(PeConstant.WX_USER_INFO_PATH,requestParamUserInfoSB.toString());

        JSONObject jsonUserInfoMap = JSONObject.fromObject(jsonUserInfoMapStr);

        String wxNickName = (String) jsonUserInfoMap.get("nickname");
        System.out.println("==================================================jsonUserInfoMap= " + jsonUserInfoMap);
        System.out.println("==================================================wxNickName= " + wxNickName);
        if(EmojiFilter.containsEmoji(wxNickName)){
            //包含emoji表情
            wxNickName = EmojiHandler.encodeJava(wxNickName);
        }
        if(null != jsonUserInfoMap.get("nickname")){
            weChatUserInfo.put("weChatId",(String) jsonUserInfoMap.get("openid"));
            weChatUserInfo.put("nickname",wxNickName);
            weChatUserInfo.put("headimgurl",(String) jsonUserInfoMap.get("headimgurl"));
            weChatUserInfo.put("sex",jsonUserInfoMap.get("sex")+"");
            weChatUserInfo.put("language",(String) jsonUserInfoMap.get("language"));
            weChatUserInfo.put("city",(String) jsonUserInfoMap.get("city"));
            weChatUserInfo.put("province",(String) jsonUserInfoMap.get("province"));
            weChatUserInfo.put("country",(String) jsonUserInfoMap.get("country"));
            String uuid = (String) jsonUserInfoMap.get("unionid");
            weChatUserInfo.put("unionid",(String) jsonUserInfoMap.get("unionid"));
        }

//        List<GenericValue> partyIdentificationList = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", (String) jsonUserInfoMap.get("unionid")).queryList();
//
//        String   partyId ="";
//
//        if(partyIdentificationList.size()>1){
//
//            partyId =  (String) partyIdentificationList.get(partyIdentificationList.size()-1).get("partyId");
//
//        }else{
//            if(null != partyIdentificationList && partyIdentificationList.size()>0 && partyIdentificationList.get(0) != null){
//
//                partyId =(String) partyIdentificationList.get(0).get("partyId");
//
//            }
//        }


        // Service Foot
        result.put("weChatUserInfo",weChatUserInfo);
       // result.put("nowPartyId",partyId);
        return result;
    }
}
