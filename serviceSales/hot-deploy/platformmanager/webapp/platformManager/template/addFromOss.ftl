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
 <form id="addOssPicture">
 <select>
	 <option value="SINGLE_PRODUCT_IMAGE">${uiLabelMap.SINGLE_IMAGE}</option>
	 <option value="DETAIL_PRODUCT_IMAGE">${uiLabelMap.DETAIL_IMAGE}</option>
	 <option value="MATCH_PRODUCT_IMAGE">${uiLabelMap.MATCH_IMAGE}</option>
 </select>


 <button style="margin-top: 300px;" type="submit">ADD</button>
 </form>
</body>