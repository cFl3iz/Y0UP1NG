<head>
    <meta charset="UTF-8">
    <title>Document</title>
    <style type="text/css">

        .ui-flex {
            display: -webkit-box !important;
            display: -webkit-flex !important;
            display: -ms-flexbox !important;
            display: flex !important;
            -webkit-flex-wrap: wrap;
            -ms-flex-wrap: wrap;
            flex-wrap: wrap
        }

        .ui-flex, .ui-flex *, .ui-flex :after, .ui-flex :before {
            box-sizing: border-box
        }

        .ui-flex.justify-center {
            -webkit-box-pack: center;
            -webkit-justify-content: center;
            -ms-flex-pack: center;
            justify-content: center
        }
        .ui-flex.center {
            -webkit-box-pack: center;
            -webkit-justify-content: center;
            -ms-flex-pack: center;
            justify-content: center;
            -webkit-box-align: center;
            -webkit-align-items: center;
            -ms-flex-align: center;
            align-items: center
        }
    </style>
</head>
<body>

<div class="ui-flex justify-center center" style="border: green solid 1px; width: 500px; height: 500px;">
    <div class="cell">
        <img alt="" src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/1489557679.png" style="" />
    </div>
</div>

</body>