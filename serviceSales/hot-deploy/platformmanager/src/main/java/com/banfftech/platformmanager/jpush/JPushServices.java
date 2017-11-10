package main.java.com.banfftech.platformmanager.jpush;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.PushPayload.Builder;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilGenerics;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.base.util.string.FlexibleStringExpander;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import java.util.*;


/**
 * Copy By Cloud Card
 */
public class JPushServices {
	public static final String module = JPushServices.class.getName();
//	public static final String resourceError = "cloudcardErrorUiLabels";

	
	//注册jPushClient
	private static JPushClient getJPushClient(Delegator delegator,String appType) {
		JPushClient jPushClient = null;
		String appkey = "cb43c0495515a6827a191fbf";
		String secret = "565d6cf05270a7ba009bfff0";
		jPushClient = new JPushClient(secret, appkey);
		return jPushClient;
	}



	/**
	 * 注册设备ID
	 * @param dctx
	 * @param context
	 * @return
	 */
	public static Map<String, Object> regJpushRegId(DispatchContext dctx, Map<String, Object> context){
		LocalDispatcher dispatcher = dctx.getDispatcher();
		Delegator delegator = dispatcher.getDelegator();
		System.out.println("REG JPUSH REGID ++++++++++++++++++++++++++++++++++++++++++++");
		Map<String, Object> retMap = ServiceUtil.returnSuccess();
		Locale locale = (Locale) context.get("locale");
		String regId = (String) context.get("regId");
		String deviceType = (String) context.get("deviceType");
		String sendType  = "";
		GenericValue userLogin = (GenericValue) context.get("userLogin");
		String partyId = userLogin.getString("partyId");

		String partyIdentificationTypeId = null;
		if(deviceType.toLowerCase().indexOf("android")>0){
			sendType= "ANDROID";
			partyIdentificationTypeId = "JPUSH_ANDROID";
		}else if(deviceType.toLowerCase().indexOf("os")>0){
			sendType= "IOS";
			partyIdentificationTypeId = "JPUSH_IOS";
		}

		if(null == partyIdentificationTypeId){
			Debug.logError("appType invalid", module);
			return retMap;
		}

		//原app regId
		String oldRegId = null;
		String time = "";
		// 查询registrationID
		EntityCondition pConditions = EntityCondition.makeCondition("partyId", partyId);
		List<EntityCondition> devTypeExprs = new ArrayList<EntityCondition>();
		devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_ANDROID"));
		devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_IOS"));
		EntityCondition devCondition = EntityCondition.makeCondition(devTypeExprs, EntityOperator.OR);
		pConditions = EntityCondition.makeCondition(pConditions, devCondition);

		try {
			//查找regId
			List<GenericValue> partyIdentifications = new ArrayList<GenericValue>();
			try {
				partyIdentifications = delegator.findList("PartyIdentification", pConditions, null, null, null, false);
			} catch (GenericEntityException e) {
				return retMap;
			}

			//判断该用户是否存在regId,如果不存在，插入一条新数据，否则修改该partyId的regId
			if(UtilValidate.isEmpty(partyIdentifications)){
				delegator.makeValue("PartyIdentification", UtilMisc.toMap("partyId",partyId,"partyIdentificationTypeId",partyIdentificationTypeId,"idValue", regId)).create();
			}else{
				for(GenericValue partyIdentification : partyIdentifications){
					//只要partyIdentificationTypeId和regId不一样，对PartyIdentification进行实体操作
					if(!partyIdentification.getString("idValue").equals(regId)){
						oldRegId = partyIdentification.getString("idValue");
						Calendar cal = Calendar.getInstance();
						String year = String.valueOf(cal.get(Calendar.YEAR));
						String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
						String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
						String hours = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
						String minutes = String.valueOf(cal.get(Calendar.MINUTE));
						time = year + "年" + month + "月" + day + "日 " + hours + ":" + minutes;
						//删除partyIdentification
						partyIdentification.remove();
						//新增partyIdentification
						delegator.createOrStore(delegator.makeValue("PartyIdentification",
								UtilMisc.toMap("partyId",partyId,"partyIdentificationTypeId",partyIdentificationTypeId,"idValue", regId)));

					}
				}
			}
		} catch (GenericEntityException e) {
			Debug.logError(e.getMessage(), module);

			return retMap;
		}


		retMap.put("deviceType", deviceType);
		retMap.put("regId", oldRegId);
		retMap.put("time", time);
		return retMap;
	}






	/**
	 * 退出登录删除设备ID
	 * @param dctx
	 * @param context
	 * @return
	 */
	public static Map<String, Object> removeJpushRegId(DispatchContext dctx, Map<String, Object> context){
		LocalDispatcher dispatcher = dctx.getDispatcher();
		Delegator delegator = dispatcher.getDelegator();
		Locale locale = (Locale) context.get("locale");
		String regId = (String) context.get("regId");

		GenericValue userLogin = (GenericValue) context.get("userLogin");
		String partyId = userLogin.getString("partyId");
		Map<String, Object> partyIdentificationMap = new HashMap<String, Object>();
		partyIdentificationMap.put("partyId", partyId);
		partyIdentificationMap.put("idValue", regId);
		Map<String, Object> retMap = ServiceUtil.returnSuccess();
		try {
			delegator.removeByAnd("PartyIdentification", partyIdentificationMap);
		} catch (GenericEntityException e) {
			Debug.logError(e.getMessage(), module);
//			return ServiceUtil.returnError("error", locale);
			return retMap;
		}


		return retMap;
	}




	// 推送消息
	public static Map<String,Object> pushNotifOrMessage(DispatchContext dctx, Map<String, Object> context) {
		LocalDispatcher dispatcher = dctx.getDispatcher();
		Delegator delegator = dispatcher.getDelegator();

		GenericValue userLogin = (GenericValue) context.get("userLogin");

		String partyId = (String) userLogin.get("partyId");

		// all 所有人
		String sendType = (String) context.get("sendType");

		String partyIdentificationTypeId = "JPUSH_IOS";

		if(sendType!=null & sendType.equals("android")){
			partyIdentificationTypeId = "JPUSH_ANDROID";
		}

		String regId = (String) context.get("regId");
		String objectId = (String) context.get("objectId");
		String title = "友评";

		// 通知消息
		String content = (String) context.get("content");
		if(UtilValidate.isNotEmpty(content)){
			content = FlexibleStringExpander.expandString(content, context);
		}

		// 透传消息
		String message = (String) context.get("message");
		if(UtilValidate.isNotEmpty(message)){
			message = FlexibleStringExpander.expandString(message, context);
		}


		
		//Map<String, String> extras = UtilGenerics.checkMap(context.get("extras"));
		Map<String, String> extras = new HashMap<String, String>();
		extras.put("objectId",objectId);
		if(null==extras){
			extras =  new HashMap<String, String>();
		}

		PushResult pushResult = null;


		// 发送特定人群
		JPushClient jPushClient = getJPushClient(delegator, "");
//		boolean setApnsProduction = "1".equals(EntityUtilProperties.getPropertyValue("cloudcard", "jpush.setApnsProduction", "0", delegator));
//
		Builder payloadBuilder = PushPayload.newBuilder()
				.setPlatform(Platform.all());

		// 按照registrationID发送
		if(null != regId){
			System.out.println("=============== to one regId = " +regId);

			payloadBuilder.setAudience(Audience.registrationId(regId)); // FIXME 多个regId的情况下，一个id出错全错？
		}
//
		// 按标签发送
		if("tag".equals(sendType)){
			String tag = (String) context.get("tag");
			if(UtilValidate.isEmpty(tag)){
				Debug.logWarning("没有推送目标", module);
//				return ServiceUtil.returnSuccess();
			}
			payloadBuilder.setAudience(Audience.tag(tag));
		}

		// 发送透传消息
		if(UtilValidate.isNotEmpty(message)){
			payloadBuilder.setMessage(
					Message.newBuilder().setMsgContent(message).addExtras(extras).build());
		}

		// 发送通知
		if(UtilValidate.isNotEmpty(content)){
			payloadBuilder.setNotification(
					Notification.newBuilder().addPlatformNotification(
							IosNotification.newBuilder().setAlert(content).setBadge(1).setSound("default").addExtras(extras).build())
							.addPlatformNotification(
									AndroidNotification.newBuilder().setAlert(content).setTitle(title).addExtras(extras).build())
							.build());
		}

		// 发送消息
		try {
			pushResult = jPushClient.sendPush(payloadBuilder.build());
			if (200 != pushResult.getResponseCode()) {
				Debug.logError("推送通知[" + content + "], 消息[" + message + "]失败: code:" + pushResult.getResponseCode() + " response: " + pushResult.getOriginalContent(), module);
			}
		} catch (APIConnectionException e) {
			Debug.logError("推送通知[" + content + "], 消息[" + message + "]失败:" + e.getMessage(), module);
		} catch (APIRequestException e) {
			Debug.logError("推送通知[" + content + "], 消息[" + message + "]失败:" + e.getMessage(), module);
		}

		return ServiceUtil.returnSuccess();
	}
}
