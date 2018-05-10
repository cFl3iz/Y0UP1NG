<#assign imageList=request.getAttribute('imgList')/>
<div style="width:100%;height:100%;">
<#if imageList?has_content>
	<#list imageList as img>
		<div class="imageBlock" filename="cb_${(img.fileName)!}">
			<img src="${(img.fileUrl)!}@120w" for="cb_${(img.fileName)!}"/>
			<div class="imgLabel">
				<input type="checkbox" class="imgCheckbox" id="cb_${(img.fileName)!}" url="${(img.fileUrl)!}"/>
				<label for="cb_${(img.fileName)!}" style="word-break: break-all;">${(img.fileName)!}</label>
			</div>
		</div>
	</#list>
</#if>
</div>

<style>
	.imageBlock{
		width:120px;
		height:195px;
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
	$(function(){
		$(".imageBlock").click(function(){
			var cbId=$(this).attr("filename");
			var isChecked = $("[id='" + cbId + "']").attr("checked");
			$("[id='" + cbId + "']").attr("checked",!isChecked);
		});
	});
</script>
