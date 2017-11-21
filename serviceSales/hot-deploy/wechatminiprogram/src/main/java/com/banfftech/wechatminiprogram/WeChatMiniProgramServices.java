package main.java.com.banfftech.wechatminiprogram;

import main.java.com.banfftech.personmanager.PersonManagerQueryServices;
import main.java.com.banfftech.platformmanager.constant.PeConstant;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

import static main.java.com.banfftech.platformmanager.util.HttpHelper.sendGet;

/**
 * Created by S on 2017/11/20.
 */
public class WeChatMiniProgramServices {



    public final static String module = WeChatMiniProgramServices.class.getName();


    /**
     * jscode2session
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     * @throws GenericEntityException
     */
    public static String jscode2session(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException, GenericEntityException {

        // Servlet Head

        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();

        String code = (String) request.getParameter("code");

        String responseStr2 = sendGet(PeConstant.WECHAT_MINI_PROGRAM_SNS_PATH,
                "appid=" + PeConstant.WECHAT_MINI_PROGRAM_APP_ID +
                        "&secret=" + PeConstant.WECHAT_MINI_PROGRAM_APP_SECRET_ID +
                        "&js_code="+code+"&grant_type=authorization_code");

        JSONObject jsonMap2 = JSONObject.fromObject(responseStr2);

        String unionid = (String) jsonMap2.get("unionid");

        request.setAttribute("unionid",unionid);

        return "success";
    }

}
