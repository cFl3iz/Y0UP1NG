<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sku_Image_Edit</title>
    <style>
        img {
            padding-left: 10px;
            padding-right: 15px;
        }

        .imageBlock {
            width: 90px;
            height: 120px;
            border: 2px solid rgb(223, 214, 214);
            float: left;
            overflow: hidden;
            margin-left: 10px;
            margin-top: 30px;
            margin-bottom: 2px;
            border-radius: 4px;
        }

        .sortable {
            list-style-type: none;
            margin: 0;
            padding: 0;
            width: 600px;
            position: relative;
        }

        .sortable li {
            margin-left: 20px;
            margin-bottom: 10px;
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
            width: 100px
        }

        .delete {
            display: block;
            color: #000000 !important;
            background: rgba(132, 132, 132, 0.2);
            width: 30px;
            height: 30px;
            top: 0;
            right: 0px;
            position: absolute;
            text-align: center;
            line-height: 30px;
        }

        .imgLabel {
            height: 30px;
            width: 100%;
        }

        button {
            float: right;
            margin-right: 10px;
        }
    </style>
</head>
<body>
<form id="deleteProductContent" action="deleteProductContentEvent" type="POST">
    <input name="contentIds" type="hidden" id="contentIds" value=""/>
    <input name="productId" type="hidden" value="${productId}"/>
</form>


<form id="addNewPicture" action="addNewPictureEvent" type="POST">
    <input name="newPicturePaths" type="hidden" id="newPicturePaths" value=""/>
    <input name="partyContentTypeId" type="hidden" id="partyContentTypeId" value=""/>
    <input name="productId" type="hidden" value="${productId}"/>
</form>

<div id="content">

    <script>

        function loadOSSResource(elementId, imageType) {
            //配置弹出框
            var dialog = $("#ossResourceDiv").dialog({
                modal: true,
                bgiframe: true,
                autoOpen: false,
                height: 510,
                width: 1000,
                draggable: true,
                resizeable: true,
                title: "ADD PICTURE FROM OSS",
                close: function () {
                    $("#ossResourceDiv").html("");	//关闭的时候清空
                },
                buttons: {
                    "add": function () {
                        addNew();
                        dialog.dialog("close");
                    },
                }
            });
            dialog.dialog("open");
            $("#ossResourceDiv").html("<img src='/images/spinner.gif'/>");//放一个加载中的图片
            $("#ossResourceDiv").load("<@ofbizUrl>LoadOSSResourceHtml</@ofbizUrl>", {
                productId: '${(parameters.productId)!}',
                colorId: "${(parameters.colorId)!}",
                imgType: imageType,
                prefix:"datas/ankorau_jiaonang/singleImages/"
            }, function () {
            });

        }

        function reloadOss(preifx){
//            alert("preifx="+preifx);
            $("#ossResourceDiv").html("");
            var dialog = $("#ossResourceDiv").dialog({
                modal: true,
                bgiframe: true,
                autoOpen: false,
                height: 500,
                width: 985,
                draggable: true,
                resizeable: true,
                title: "ADD PICTURE FROM OSS",
                close: function () {
                    $("#ossResourceDiv").html("");	//关闭的时候清空
                },
                buttons: {
                    "add": function () {
                        addNew();
                        dialog.dialog("close");
                    },
                }
            });
            dialog.dialog("open");
            $("#ossResourceDiv").html("<img src='/images/spinner.gif'/>");//放一个加载中的图片
            $("#ossResourceDiv").load("<@ofbizUrl>LoadOSSResourceHtml</@ofbizUrl>", {
                productId: '${(productId)!}',
                colorId: null,
                imgType: null,
                prefix:preifx
            }, function () {
            });
        }

        //每次拖放，都重新排一次序
        function sortDropElement(elementId) {
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

            var widgetsLi = $(".sortable").find("li");
            var index = 0;
            var seqNum = 100;
            widgetsLi.each(function () {
                $(this).find("input[tag='contentId']").attr("name", "contentId_o_" + index);
                $(this).find("input[tag='sequenceNum']").attr("name", "sequenceNum_o_" + index).val(Number(seqNum + index));
                $(this).find("input[tag='objectInfo']").attr("name", "objectInfo_o_" + index);
                $(this).find("input[tag='productContentTypeId']").attr("name", "productContentTypeId_o_" + index);
                $(this).find("input[tag='_rowSubmit']").attr("name", "_rowSubmit_o_" + index);
                index++;
            });
        }
        //把每一个图片元素放到控件中
        function addOneImage(elementId, newEl) {
            newEl.appendTo($("#" + elementId));
        }

        //让图片变成可以切换
        function initDropPlugins(elementId) {
            $("#" + elementId).sortable({
                revert: true,
                update: function (event, ui) {
                    sortDropElement(elementId);
                }
            });
            $("#" + elementId).disableSelection();
        }

        function disabledDropPlugins(elementId) {
            $("#" + elementId).sortable("disable");
        }

        Array.prototype.removeByValue = function (val) {
            for (var i = 0; i < this.length; i++) {
                if (this[i] == val) {
                    this.splice(i, 1);
                    break;
                }
            }
        }


        var choseArray = new Array();

        $(
                function () {

                    $("#delete_btn").click(
                            function () {
                                if (choseArray.length < 1) {
                                    alert("${uiLabelMap.NO_CHOSE}");
                                    return false;
                                }
                                if (confirm("Delete?")) {
                                    //TODO DELETE PRODUCT CONTENT
                                    $("#contentIds").val(choseArray.toString());
                                    $("#deleteProductContent").submit();
                                }

                            }
                    );

                    $("#addContentType").click(
                            function () {
                                location.href = '/catalog/control/EditProductContent?productId=${productId}';
                            }
                    );

                    $("#addFromOss").click(function () {
                        loadOSSResource('sortableDetail', 'detailImages');
                    });
                }
        );

        //add new
        function addNew(){
            var selector = $("#selector").val();
            var msg = "Are You Sure Add " + newPictArray.length + " To " + selector ;
            if(confirm(msg)){
                $("#newPicturePaths").val(newPictArray.toString());
                $("#partyContentTypeId").val(selector);
//                alert(newPictArray.toString());
                $("#addNewPicture").submit();
//                alert("added successful");
            }else{
                alert("cancel");
            }
        }




        //Chose Image
        function selectPicture(obj) {
            let isCheck = $(obj).attr("checked");
            let value = $(obj).val();
            console.log('isCheck=' + isCheck);
            console.log('value=' + value);
            if (isCheck === "checked") {
                if (!isInArray2(choseArray, value)) {
                    choseArray.push($(obj).val());
                }
            } else {
                if (isInArray2(choseArray, value)) {
                    choseArray.removeByValue(value);
                }
            }

        }

        console.log('choseArray size=' + choseArray.length);


        //判断字符串是否在数组中存在
        function isInArray2(arr, value) {
            var index = $.inArray(value, arr);
            if (index >= 0) {
                return true;
            }
            return false;
        }

    </script>
    <div id="ossResourceDiv">

        <!-- 弹出框，配置一个加载图片 -->
    </div>
    <button id="delete_btn">${uiLabelMap.DELETE}</button>
    <button id="addFromOss">${uiLabelMap.ADDED_FROM_OSS}</button>
    <button id="addContentType">${uiLabelMap.ADDED_CONTENT_TYPE}</button>
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">${uiLabelMap.SINGLE_IMAGE}</li>

        <#if singlePictures?has_content>
            <#list singlePictures as img>
                <div class="imageBlock" filename="cb_${(img.contentId)!}">
                    <img src="${(img.drObjectInfo)!}?x-oss-process=image/resize,m_mfit,h_84,w_70"/>

                    <div class="imgLabel">
                        <input type="checkbox" onclick="selectPicture(this);" class="imgCheckbox"
                               id="cb_${(img.contentId)!}"
                               url="${(img.drObjectInfo)!}" value="${(img.contentId)!}">
                        <label for="cb_${(img.contentId)!}" style="word-break: break-all;">${(img.contentId)!}</label>
                        </input>
                    </div>
                </div>
            </#list>
        </#if>


        </ul>

        <br class="clear"/>
    <#if !singlePictures?has_content>
        NOT FOUND ${uiLabelMap.SINGLE_IMAGE}
    </#if>
    </div>

    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">${uiLabelMap.DETAIL_IMAGE}</li>
        <#if detailPictures?has_content>
            <#list detailPictures as img>
                <div class="imageBlock" filename="cb_${(img.contentId)!}">
                    <img src="${(img.drObjectInfo)!}?x-oss-process=image/resize,m_mfit,h_84,w_65"/>

                    <div class="imgLabel">
                        <input type="checkbox" onclick="selectPicture(this);" class="imgCheckbox"
                               id="cb_${(img.contentId)!}"
                               url="${(img.drObjectInfo)!}" value="${(img.contentId)!}"/>
                        <label for="cb_${(img.contentId)!}" style="word-break: break-all;">${(img.contentId)!}</label>
                    </div>
                </div>
            </#list>
        </#if>

        </ul>

        <br class="clear"/>
    <#if !detailPictures?has_content>
        NOT FOUND ${uiLabelMap.DETAIL_IMAGE}
    </#if>
    </div>


    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">${uiLabelMap.MATCH_IMAGE}</li>
        <#if matchPictures?has_content>
            <#list matchPictures as img>
                <div class="imageBlock" filename="cb_${(img.contentId)!}">
                    <img src="${(img.drObjectInfo)!}?x-oss-process=image/resize,m_mfit,h_84,w_65"
                            />

                    <div class="imgLabel">
                        <input type="checkbox" onclick="selectPicture(this);" class="imgCheckbox"
                               id="cb_${(img.contentId)!}"
                               url="${(img.drObjectInfo)!}" value="${(img.contentId)!}"/>
                        <label for="cb_${(img.contentId)!}" style="word-break: break-all;">${(img.contentId)!}</label>
                    </div>
                </div>
            </#list>
        </#if>
        </ul>

        <br class="clear"/>
    <#if !matchPictures?has_content>
        NOT FOUND ${uiLabelMap.matchPictures}
    </#if>
    </div>
</div>
</body>