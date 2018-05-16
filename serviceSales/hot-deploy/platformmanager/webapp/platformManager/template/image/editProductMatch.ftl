<form name="addToMatch" method="post">
<div class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">商品列表</li>
            <#if parameters.productCategoryId?has_content>
            <li><a href="<@ofbizUrl>removeMatchProductCategory?productCategoryId=${(parameters.productCategoryId)!}</@ofbizUrl>" onclick="return confirm('您确定要删除这个搭配吗？')">删除这个搭配</a></li>
            <li>搭配编号：${(parameters.productCategoryId)!}</li>
            </#if>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body">
    	<div>
    	<#if parameters.productCategoryId?has_content>
    		添加商品到这个搭配：
    	<#else>
    		添加一个商品从而创建一个搭配：
    	</#if>
    	<@htmlTemplate.lookupField formName="addToMatch" name="productId" id="productId" fieldFormName="LookupProductMatch" value="" readonly="false" className="required"/>
    	<a href="javascript:;" onclick="addProductToMatch()" class="buttontext">添加</a>
    	<span class="tooltip">您添加的第一个单品将作为搭配的主款并且不能修改</span>
    	<br/><br/>
    	
    	<#if memberList?has_content>
    		<input type="checkbox" name="iPadUse" id="iPadUse" <#if ipadUseAttribute?has_content>checked</#if>/> <label for="iPadUse">iPad平台适用</label>
    	</#if>
    	<#if contents?has_content>
    		　　 
    		<input type="checkbox" name="websiteUse" id="websiteUse" <#if webSiteUseAttribute?has_content>checked</#if>/> <label for="websiteUse">需要展示在网站上</label>
    	</#if>
    	
    	</div>
    	<#if memberList?has_content>
    		<#list memberList as member>
				<#assign product = delegator.findOne("Product",false,Static["org.ofbiz.base.util.UtilMisc"].toMap("productId",member.productId))?if_exists/>
    			<#--获取颜色编号-->
    			<#assign colorFeatrue = delegator.findByAndCache("ProductDesignFeatureWithType",Static["org.ofbiz.base.util.UtilMisc"].toMap("productId",member.productId,"productFeatureTypeId","COLOR"))?if_exists  />
    			<#-- 获取虚拟商品编号 -->
    			<#assign virtualProducts = delegator.findByAndCache("ProductAssoc",Static["org.ofbiz.base.util.UtilMisc"].toMap("productIdTo",member.productId,"productAssocTypeId","PRODUCT_VARIANT"))?if_exists  />
    			
    			<div class="matchProductBlock" variantProductId="${(product.productId)!}" virtualProductId="${(virtualProducts[0].productId)!}" colorId="${(colorFeatrue[0].productFeatureId)!}">
    				<div style="float: left;position: relative;margin:25px 25px 0px 25px;text-align: center;">
	    				<a href="<@ofbizUrl>RealProductImageAssoc?productId=${(virtualProducts[0].productId)!}&colorId=${(colorFeatrue[0].productFeatureId)!}</@ofbizUrl>">
	    				<img src="${(product.smallImageUrl)!}"/><br/><br/>
	    				<span>${(member.productId?substring(0,member.productId?last_index_of('-')))!}</span>
	    				</a>
	    				<br/>
	    				<#if member_index==0><input type="button" class="" value="主款"/></span><#else><input type="button" style="cursor:pointer" class="smallSubmit" onclick="removeProductFromMatch('${(member.productId)!}')" value="删除单品"/><br/></#if>
    				</div>
    				<#--
    				<div style="float: left;position: relative;text-align: center;margin:25px 25px 0px 25px;">
	    				<input type="button" style="cursor:pointer" class="smallSubmit" onclick="removeProductFromMatch('${(member.productId)!}')" value="删除单品"/><br/>
	    				<input type="button" style="cursor:pointer" class="smallSubmit masterBtn" onclick="setMasterProduct('${(member.productId)!}',this)" value="<#if member_index==0>主　　款<#else>设为主款</#if>"/>
	    			</div>
	    			-->
    			</div>
    		</#list>
    	</#if>
    </div>
</div>
</form>
<form action="<@ofbizUrl>saveMatchImages</@ofbizUrl>" method="post" id="imageForm">
<input type="hidden" name="_useRowSubmit" value="Y" />
<div class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">图片列表</li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body">
    	<a href="javascript:;" onclick="loadOSSResource('sortableMatch','matchImages');" class="buttontext">添加搭配图</a>
    	<span class="tooltip">根据主款查找</span>
    	<ul id="sortableMatch" class="sortable" productContentTypeId="SINGLE_PRODUCT_IMAGE">
    	<#if contents?has_content>
    		<#list contents as content>
    			<li>
					<#if contents[0].drObjectInfo?index_of("http") gt -1>
						<img src="${(content.drObjectInfo)!}"/>
					<#else>
						<img src="${(content.drObjectInfo)!}"/>
					</#if>
					<a class="delete" href="javascript:;" onclick="if(confirm('您确定要删除这个搭配图吗')){window.location.href='<@ofbizUrl>removeProductCategoryContent?productCategoryId=${(content.productCategoryId)!}&contentId=${(content.contentId)!}</@ofbizUrl>'}">×</a>
					<input type="hidden" tag="contentId" name="contentId_o_${(content_index)!}" value="${(content.contentId)!}"/>
					<input type="hidden" tag="productCategoryId" name="productCategoryId_o_${(content_index)!}" value="${(content.productCategoryId)!}"/>
					<input type="hidden" tag="sequenceNum" name="sequenceNum_o_${(content_index)!}" value="${(content.sequenceNum)!}"/>
					<input type="hidden" tag="objectInfo" name="objectInfo_o_${(content_index)!}" value="${(content.drObjectInfo)!}"/>
					<input type="hidden" tag="_rowSubmit" name="_rowSubmit_o_${(content_index)!}" value="Y"/>
				</li>
    		</#list>
    	</#if>
    	</ul>
    </div>
</div>
<div style="text-align:right;padding-right:30px">
<a href="javascript:;" onclick="submitMatchImages();" class="buttontext" >提交</a>
</div>
</form>

<div id="ossResourceDiv">
	<!-- 弹出框，配置一个加载图片 -->
</div>
<style>
	.sortable { 
		list-style-type: none; 
		margin: 0; 
		padding: 0; 
		width: 700px;
		position: relative; 
	}
	.sortable li { 
		margin-left:20px; 
		margin-bottom:10px;
		padding: 0px; 
		float: left; 
		width: 120px; 
		height: 159px;  
		text-align: center;
		background: #FFF;
		border-radius: 4px;
		border: 2px solid #EDEDED;
		position: relative; 
		cursor: move;
		overflow: hidden;
	}
	.sortable li img { 
		width:120px
	}
	.delete {
		display: block;
		color: #000000 !important;
		background: rgba(132, 132, 132, 0.2);
		width: 30px;
		height: 30px;
		top: 0;
		right:0px;
		position: absolute;
		text-align: center;
		line-height: 30px;
	}
	.matchProductBlock{
		width:155px;height:190px;border:1px solid;float: left;margin:20px 20px 20px 0px;background-color: #D8D8D8;border-radius: 4px;
	}
	
</style>
<script>
$(function(){
    if($(".sortable").find("li").length>0){
    	$(".sortable").each(function(){
    		initDropPlugins($(this).attr("id"));
    	});
    }
    sortDropElement();
    
    var productDivs = $(".matchProductBlock");

	var virtualProductId = $(productDivs[0]).attr("virtualProductId");
	var variantProductId = $(productDivs[0]).attr("variantProductId");
	if("${(parameters.productCategoryId)!}".length > 0){
	   $("#iPadUse").click(function(){
			var isChecked = $("#iPadUse").attr("checked");
			if(isChecked){
				isChecked="Y";
			}else{
				isChecked="N";
			}

			window.location.href="<@ofbizUrl>setMatchIsIpadUse</@ofbizUrl>?iPadUse="+isChecked+"&productCategoryId=${(parameters.productCategoryId)!}&virtualProductId=" + virtualProductId;
		}); 
		
		$("#websiteUse").click(function(){
			var isChecked = $("#websiteUse").attr("checked");
			if(isChecked){
				isChecked="Y";
			}else{
				isChecked="N";
			}
			
			window.location.href="<@ofbizUrl>setMatchIsWebsiteUse</@ofbizUrl>?websiteUse="+isChecked+"&productCategoryId=${(parameters.productCategoryId)!}&variantProductId=" + variantProductId;
		}); 
	}
});

//让图片变成可以切换
function initDropPlugins(elementId){
	$( "#" + elementId ).sortable({
		revert:true,
		update:function(event,ui){
			sortDropElement(elementId);
		}
	});
    $( "#" + elementId ).disableSelection();
}

function disabledDropPlugins(elementId){
	$( "#" + elementId ).sortable("disable");
}

//每次拖放，都重新排一次序
function sortDropElement(elementId){
	var widgetsLi =$(".sortable").find("li"); 
	var index=0;
	var seqNum = 100;
	widgetsLi.each(function(){
		$(this).find("input[tag='contentId']").attr("name","contentId_o_" + index);
		$(this).find("input[tag='productCategoryId']").attr("name","productCategoryId_o_" + index);
		$(this).find("input[tag='objectInfo']").attr("name","objectInfo_o_" + index);
		$(this).find("input[tag='sequenceNum']").attr("name","sequenceNum_o_" + index).val(Number(seqNum+index));
		$(this).find("input[tag='_rowSubmit']").attr("name","_rowSubmit_o_" + index);
		index++;
	});
}

function submitMatchImages(){
	//var data =
	if($("#sortableMatch").find("li").length > 0){
		$("#imageForm").submit();
	}else{
		showErrorAlert("提示","您还没有选择搭配图片");
	}
}

function loadOSSResource(elementId,imageType){
	alert("test"+elementId+"|"+imageType);
	//配置弹出框
	var dialog = $("#ossResourceDiv").dialog({
        modal: true,
        bgiframe: true,
        autoOpen: false,
        height: 500,
        width: 985,
        draggable: true,
        resizeable: true,
        title:"请选择图片",
        close:function(){
        	$("#ossResourceDiv").html("");	//关闭的时候清空
        },
        buttons:{
			"添加": function() {
				//点击添加，把选择的图片加到当前的DIV中
				imgs = $(".imgCheckbox");
				var selectImages = [];
				$(imgs).each(function(){
					if($(this).attr("checked")){
						var url = $(this).attr("url");
						var cbId = $(this).attr("id");
						var newEl = $('<li>'+
										'<img src="' + url +
										'<a class="delete" href="javascript:;" onclick="$(this).parent().remove()">×</a>'+
										'<input type="hidden" tag="contentId" name="" value=""/>'+
										'<input type="hidden" tag="productCategoryId" name="" value="${(parameters.productCategoryId)!}"/>'+
										'<input type="hidden" tag="sequenceNum" name="" value=""/>'+
										'<input type="hidden" tag="objectInfo" name="" value="' + url + '"/>'+
										'<input type="hidden" tag="_rowSubmit" name="" value="Y"/>'+
									'</li>');
									
									
						addOneImage(elementId,newEl);
					}
				});
				initDropPlugins(elementId);
				sortDropElement(elementId);
				dialog.dialog("close");
			}
		}
    });
    
    var productDivs = $(".matchProductBlock");
    if($(".matchProductBlock").length <= 0){
    	showErrorAlert("提示","您还没有添加单品或者没有设置主款，不能查找图片");
    	return false;
    }
	//获取第一个商品的款号
	var virtualProductId = $(productDivs[0]).attr("virtualProductId");
	//获取第一个商品的颜色
	var colorId = $(productDivs[0]).attr("colorId");
    
	dialog.dialog("open");
	$("#ossResourceDiv").html("<img src='/images/spinner.gif'/>");//放一个加载中的图片
	$("#ossResourceDiv").load("<@ofbizUrl>LoadOSSResourceHtml</@ofbizUrl>",{productId:virtualProductId,colorId:colorId,imgType:imageType},function(){
	});
}
//把每一个图片元素放到控件中
function addOneImage(elementId,newEl){
	newEl.appendTo($("#" + elementId));
}

//设置主款
function setMasterProduct(productId,alabel){
	var productDivs = $(".matchProductBlock");
	if($(".matchProductBlock").length>1){
		var theFirstId = $(productDivs).attr("variantProductId");
		if(theFirstId!=productId){
			//把点选的这个插到第一个全面去
			$("div [variantProductId='" + productId + "']").insertBefore(productDivs[0]);
		}
	}
	
	var data={};
	var index=0;
	var seq=100;
	var productDivs = $(".matchProductBlock");	//重新获取
	//提交到后台
	$(productDivs).each(function(k,v){
		var name_productId = "productId_o_" + index;
		data[name_productId] = $(v).attr("variantproductid");
		
		var name_seq = "sequenceNum_o_" + index;
		data[name_seq] = Number(seq+index);
		
		var name_productCategoryId = "productCategoryId_o_" + index;
		data[name_productCategoryId] = "${(parameters.productCategoryId)!}";
		
		data["_rowSubmit_o_" + index]="Y";
		
		index++;
	});
	data._useRowSubmit = "Y";
	$.post("<@ofbizUrl>setMatchMasterProduct</@ofbizUrl>",data,function(result){
		if(("_ERROR_MESSAGE_" in result) || ("_ERROR_MESSAGE_LIST_" in result)){
			showErrorAlert("提示","操作失败，请重试");
		}else{
			$(".masterBtn").val("设为主款");
			$(alabel).val("主　　款");
		}
	});
}

function addProductToMatch(){
	var productId = $("#0_lookupId_productId").val();
	if(productId==""){
		showErrorAlert("提示","您必须先选择商品");
		return false;
	}
	window.location.href="<@ofbizUrl>addProductToMatch</@ofbizUrl>?productId=" + productId + "&productCategoryId=${(parameters.productCategoryId)!}";
}

function removeProductFromMatch(productId){
	if(window.confirm("您确定要从搭配中删除这个商品吗？")){
		window.location.href="<@ofbizUrl>removeProductFromMatch</@ofbizUrl>?productId=" + productId + "&productCategoryId=${(parameters.productCategoryId)!}";
	}
}
</script>