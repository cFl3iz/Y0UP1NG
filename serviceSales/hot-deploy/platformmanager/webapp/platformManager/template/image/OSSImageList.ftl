<#assign imageList=request.getAttribute('imgList')/>
<div style="width:100%;height:100%;" xmlns="http://www.w3.org/1999/html">
	<div style="float;left;font-size:17px;">
	changePrefix:  personerp/<input id="prefix" value="datas/ankorau_jiaonang/singleImages/"
										style="border: 1px black solid;width: 370px;color:red;"/>
		&nbsp;&nbsp;&nbsp;<button onclick="javascript:reloadOss($('#prefix').val());" id="change">CHANGE!</button></div>
    <br class="clear"/>
	<div style="float:left;">
	${uiLabelMap.ChoseContentType}
        <select id="selector">
            <option value="SINGLE_PRODUCT_IMAGE">${uiLabelMap.SINGLE_IMAGE}</option>
            <option value="DETAIL_PRODUCT_IMAGE">${uiLabelMap.DETAIL_IMAGE}</option>
            <option value="MATCH_PRODUCT_IMAGE">${uiLabelMap.MATCH_IMAGE}</option>
        </select>
	</div>
    <br class="clear"/>
	<#if imageList?has_content>
	<#list imageList as img>
		<div class="imageBlock" filename="cb_${(img.fileName)!}">
			<img src="${(img.fileUrl)!}?x-oss-process=image/resize,m_mfit,h_84,w_65" for="cb_${(img.fileName)!}"/>
			<div class="imgLabel">
				<input type="checkbox" onclick="selectNewPicture(this);" class="imgCheckbox" id="cb_${(img.fileName)!}" url="${(img.fileUrl)!}"
				<label for="cb_${(img.fileName)!}" style="word-break: break-all;">${(img.fileName)!}</label></input>
			</div>
		</div>
	</#list>
</#if>
</div>
<input id="newPictures"/>
<style>
	.imageBlock{
		width:120px;
		height:125px;
		border: 2px solid rgb(223, 214, 214);
		float:left;
		overflow:hidden;
		margin-left:10px;
		margin-bottom: 15px;
		border-radius: 4px;
	}
	
	.imgLabel{
		height:30px;
		width:100%;
	}
</style>
<script>
	var newPictArray = new Array();
    //Chose Image
    function selectNewPicture(obj) {
        let isCheck = $(obj).attr("checked");
//        let value = $(obj).val();
        var value=$(obj).attr("url");
//		alert(value);
        if (isCheck === "checked") {
            if (!isInArray2(newPictArray, value)) {
                newPictArray.push(value);
            }
        } else {
            if (isInArray2(newPictArray, value)) {
                newPictArray.removeByValue(value);
            }
        }
//		alert(isCheck);
//		alert(newPictArray.length);
    }
	$(function(){
//		$(".imageBlock").click(function(){
//			var cbId=$(this).attr("filename");
//			var isChecked = $("[id='" + cbId + "']").attr("checked");
//			$("[id='" + cbId + "']").attr("checked",!isChecked);
//            if (isChecked === "checked") {
//				alert(cbId);
//			}
//		});
	});
</script>
