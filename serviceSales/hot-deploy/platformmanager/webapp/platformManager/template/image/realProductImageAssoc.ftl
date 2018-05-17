<form id="imgForm" action="<@ofbizUrl>associateProductAndImage</@ofbizUrl>" method="post">
<input type="hidden" name="productId" value="${(parameters.productId)!}"/>
<input type="hidden" name="colorId" value="${(parameters.colorId)!}"/>
<input type="hidden" name="variantProductId" value="${(variantProductId)!}"/>
<input name="_useRowSubmit" value="Y" type="hidden">

    variantProductId = ${(variantProductId)!}
    parameters.colorId = ${parameters.colorId}
<div class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">SingleProductImage</li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body">
    	<a href="javascript:;" onclick="loadOSSResource('sortableSingle','singleImages')" class="buttontext">Add_Picture</a>
		<ul id="sortableSingle" class="sortable" productContentTypeId="SINGLE_PRODUCT_IMAGE">
			<#if singleProductImages?has_content>
				<#list singleProductImages as single>
					<li>
						<#if single.drObjectInfo?index_of("http") gt -1>
							<img src="${(single.drObjectInfo)!}@110w"/>
						<#else>
							<img src="${(single.drObjectInfo)!}"/>
						</#if>
						<a class="delete" href="javascript:;" onclick="removeProductContent('${(variantProductId)!}','${(single.contentId)!}','${(single.productContentTypeId)!}',$(this).parent())">×</a>
						<input type="hidden" tag="contentId" name="contentId_o_${(single_index)!}" value="${(single.contentId)!}"/>
						<input type="hidden" tag="sequenceNum" name="sequenceNum_o_${(single_index)!}" value="${(100+detail_index)!}"/>
						<input type="hidden" tag="objectInfo" name="objectInfo_o_${(single_index)!}" value="${(single.drObjectInfo)!}"/>
						<input type="hidden" tag="productContentTypeId" name="productContentTypeId_o_${(single_index+1)!}" value="SINGLE_PRODUCT_IMAGE"/>
						<input type="hidden" tag="_rowSubmit" name="_rowSubmit_o_${(single_index)!}" value="Y"/>
					</li>
				</#list>
			</#if>
		</ul>
    </div>
</div>

<div class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">DetailImage</li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body">
    	<a href="javascript:;" onclick="loadOSSResource('sortableDetail','detailImages')" class="buttontext">Add_Picture</a>
		<ul id="sortableDetail" class="sortable" productContentTypeId="DETAIL_PRODUCT_IMAGE">
			<#if detailProductImages?has_content>
				<#list detailProductImages as detail>
					<li>
						<#if detail.drObjectInfo?index_of("http") gt -1>
							<img src="${(detail.drObjectInfo)!}@110w"/>
						<#else>
							<img src="${(detail.drObjectInfo)!}"/>
						</#if>
						<a class="delete" href="javascript:;" onclick="removeProductContent('${(variantProductId)!}','${(detail.contentId)!}','${(detail.productContentTypeId)!}',$(this).parent())">×</a>
						<input type="hidden" tag="contentId" name="contentId_o_${(detail_index)!}" value="${(detail.contentId)!}"/>
						<input type="hidden" tag="sequenceNum" name="sequenceNum_o_${(detail_index)!}" value="${(100+detail_index)!}"/>
						<input type="hidden" tag="objectInfo" name="objectInfo_o_${(detail_index)!}" value="${(detail.drObjectInfo)!}"/>
						<input type="hidden" tag="productContentTypeId" name="productContentTypeId_o_${(detail_index+1)!}" value="DETAIL_PRODUCT_IMAGE"/>
						<input type="hidden" tag="_rowSubmit" name="_rowSubmit_o_${(detail_index)!}" value="Y"/>
					</li>
				</#list>
			</#if>
		</ul>
    </div>
</div>
</form>

<div class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">ProductMatch</li>
            <li><a href="<@ofbizUrl>addProductToMatch?productId=${(variantProductId)!}</@ofbizUrl>" target="_blank">Add Match ToProduct</a></li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body">
        <a href="javascript:;" onclick="loadOSSResource('sortableDetail','matchImages')" class="buttontext">Add_Picture</a>
    </div>
</div>
<div style="text-align:right;padding-right:30px">
<a href="javascript:;" onclick="$('#imgForm').submit();" class="buttontext" >Commit</a>
</div>
<div id="ossResourceDiv">
	<!-- 弹出框，配置一个加载图片 -->
</div>

<style>
	.sortable { 
		list-style-type: none; 
		margin: 0; 
		padding: 0; 
		width: 600px;
		position: relative; 
	}
	.sortable li { 
		margin-left:20px; 
		margin-bottom:10px;
		padding: 0px; 
		float: left; 
		width: 100px; 
		height: 132px;  
		text-align: center;
		background: #FFF;
		border-radius: 4px;
		border: 2px solid #EDEDED;
		position: relative; 
		cursor: move;
		overflow: hidden;
	}
	.sortable li img { 
		width:100px
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
	.mixMatch{
		width:450px;
		height:300px;
		border-radius: 4px;
		background-color:#D8D8D8;
		margin-left:20px;
		margin-bottom:20px;
		float:left
	}
	.mixMatchLeft{
		width:150px;
		float:left;
		text-align: center;
		border: 2px solid #F5F5F5;
		border-radius: 4px;
		margin-left: 10px;
		margin-top: 10px;
	}
	.mixMatchRight{
		width:270px;
		float:left;
		margin-left: 10px;
		margin-top: 10px;
		height:250px
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
});

function loadOSSResource(elementId,imageType){
	//配置弹出框
	var dialog = $("#ossResourceDiv").dialog({
        modal: true,
        bgiframe: true,
        autoOpen: false,
        height: 500,
        width: 985,
        draggable: true,
        resizeable: true,
        title:"SelectPicture",
        close:function(){
        	$("#ossResourceDiv").html("");	//关闭的时候清空
        },
        buttons:{
			"add": function() {
				//点击添加，把选择的图片加到当前的DIV中
				imgs = $(".imgCheckbox");
				var selectImages = [];
				$(imgs).each(function(){
					if($(this).attr("checked")){
						var url = $(this).attr("url");
						var cbId = $(this).attr("id");
						var newEl = $('<li>'+
									'<img src="' + url + '@110w"/>'+
									'<a class="delete" href="javascript:;" onclick="$(this).parent().remove()">×</a>'+
									'<input type="hidden" tag="contentId" name="" value=""/>'+
									'<input type="hidden" tag="sequenceNum" name="" value=""/>'+
									'<input type="hidden" tag="objectInfo" name="" value="' + url + '"/>'+
									'<input type="hidden" tag="productContentTypeId" name="" value="' + $('#'+elementId).attr('productContentTypeId') + '"/>'+
									'<input type="hidden" tag="_rowSubmit" name="" value="Y"/>'+
									'</li>');
						addOneImage(elementId,newEl);
					}
				});
				initDropPlugins(elementId);
				sortDropElement(elementId);
				dialog.dialog("close");
			} ,
		}
    });
	dialog.dialog("open");
	$("#ossResourceDiv").html("<img src='/images/spinner.gif'/>");//放一个加载中的图片
	$("#ossResourceDiv").load("<@ofbizUrl>LoadOSSResourceHtml</@ofbizUrl>",{productId:'${(parameters.productId)!}',colorId:"${(parameters.colorId)!}",imgType:imageType},function(){
	});
    
}

//把每一个图片元素放到控件中
function addOneImage(elementId,newEl){
	newEl.appendTo($("#" + elementId));
}

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
	/**
	var widgetsLi = $( "#" + elementId ).sortable( "widget" ).find("li");
	var index=1;
	widgetsLi.each(function(){
		//$(this).attr("sort",index);
		$(this).find("input[tag='contentId']").attr("name","contentId_o_" + index);
		$(this).find("input[tag='sequenceNum']").attr("name","sequenceNum_o_" + index).val(index);
		$(this).find("input[tag='objectInfo']").attr("name","objectInfo_o_" + index);
		$(this).find("input[tag='productContentTypeId']").attr("name","productContentTypeId_o_" + index);
		index++;
	});
	**/
	
	var widgetsLi =$(".sortable").find("li"); 
	var index=0;
	var seqNum = 100;
	widgetsLi.each(function(){
		$(this).find("input[tag='contentId']").attr("name","contentId_o_" + index);
		$(this).find("input[tag='sequenceNum']").attr("name","sequenceNum_o_" + index).val(Number(seqNum + index));
		$(this).find("input[tag='objectInfo']").attr("name","objectInfo_o_" + index);
		$(this).find("input[tag='productContentTypeId']").attr("name","productContentTypeId_o_" + index);
		$(this).find("input[tag='_rowSubmit']").attr("name","_rowSubmit_o_" + index);
		index++;
	});
}
function removeProductContent(productId,contentId,productContentTypeId,li){
	if(confirm("您确定要删除这个图片吗？删除动作立即生效")){
		$.post("<@ofbizUrl>removeProductContent</@ofbizUrl>",{productId:productId,contentId:contentId,productContentTypeId:productContentTypeId},function(result){
			if(("_ERROR_MESSAGE_" in result) || ("_ERROR_MESSAGE_LIST_" in result)){
				showErrorAlert("提示","删除失败，请重试");
			}else{
				$(li).remove();
			}
		});
	}
}
</script>