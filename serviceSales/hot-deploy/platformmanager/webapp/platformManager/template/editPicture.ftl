<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sku_Image_Edit</title>
    <style>
        img{
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

        .imgLabel {
            height: 30px;
            width: 100%;
        }
        button{
            float: right;
            margin-right: 10px;
        }
    </style>
</head>
<body>
<div id="content">

    <script>

            $(
                    function(){
                        let selectCount = 0;



                    }
            );

            //Chose Image
            function selectPicture(obj){
                $(obj).attr("checked","checked");
                $(obj).attr("checked",true);
            }

    </script>

    <button>${uiLabelMap.DELETE}</button>
    <button>${uiLabelMap.ADDED_FROM_OSS}</button>
    <button>${uiLabelMap.ADDED_CONTENT_TYPE}</button>
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">${uiLabelMap.SINGLE_IMAGE}</li>

        <#if singlePictures?has_content>
            <#list singlePictures as img>
                <div class="imageBlock" filename="cb_${(img.contentId)!}">
                    <img src="${(img.drObjectInfo)!}?x-oss-process=image/resize,m_mfit,h_84,w_70" />
                    <div class="imgLabel">
                        <input type="checkbox" onclick="selectPicture(this);" class="imgCheckbox" id="cb_${(img.contentId)!}"
                               url="${(img.drObjectInfo)!}">
                        <label for="cb_${(img.contentId)!}" style="word-break: break-all;">${(img.contentId)!}</label>
                        </input>
                    </div>
                </div>
            </#list>
        </#if>

        </ul>
        <br class="clear"/>
    </div>

    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">${uiLabelMap.DETAIL_IMAGE}</li>
        <#if detailPictures?has_content>
            <#list detailPictures as img>
                <div class="imageBlock" filename="cb_${(img.contentId)!}">
                    <img src="${(img.drObjectInfo)!}?x-oss-process=image/resize,m_mfit,h_84,w_65"
                            />

                    <div class="imgLabel">
                        <input type="checkbox" onclick="selectPicture(this);" class="imgCheckbox" id="cb_${(img.contentId)!}"
                               url="${(img.drObjectInfo)!}"/>
                        <label for="cb_${(img.contentId)!}" style="word-break: break-all;">${(img.contentId)!}</label>
                    </div>
                </div>
            </#list>
        </#if>
        </ul>
        <br class="clear"/>
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
                        <input type="checkbox" onclick="selectPicture(this);" class="imgCheckbox" id="cb_${(img.contentId)!}"
                               url="${(img.drObjectInfo)!}"/>
                        <label for="cb_${(img.contentId)!}" style="word-break: break-all;">${(img.contentId)!}</label>
                    </div>
                </div>
            </#list>
        </#if>
        </ul>
        <br class="clear"/>
    </div>
</div>
</body>