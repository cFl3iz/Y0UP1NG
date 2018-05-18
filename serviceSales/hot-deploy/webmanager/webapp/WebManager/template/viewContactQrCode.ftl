<head>
    <meta charset="UTF-8">
    <title>${(title)!}</title>
    <style type="text/css">
        .floatResult {
            /*display: none;*/
        }

        .floatResult .bg {
            position: fixed;
            top: 0;
            bottom: 0;
            left: 0;
            right: 0;
            background: rgba(0, 0, 0, 0.6);
        }

        .floatResult .result {
            text-align: center;
            position: fixed;
            left: 0;
            top: 0;
            bottom: 0;
            right: 0;
        }

        .floatResult .result .span {
            display: inline-block;
            width: 1%;
            height: 100%;
            vertical-align: middle;
        }

        .floatResult .result .image {
            width: 80%;
            height: auto;
            display: inline-block;
            vertical-align: middle;
            position: relative;
        }

        .floatResult .result img {
            width: 100%;
            vertical-align: middle;
        }

        .floatResult .result .imgBtn {
            display: inline-block;
            width: 70%;
            position: absolute;
            left: 50%;
            bottom: 8%;
            margin-left: -35%;
            height: 44px;
            line-height: 42px;
            font-size: 18px;
            color: #fff;
            text-align: center;
            border-radius: 22px;
            background: #ff4345;
            text-decoration: none;
        }

    </style>
</head>
<body>


<div class="floatResult">
    <div class="bg"></div>
    <div class="result">
        <span class="span"></span>
            <span class="image">
                <img src="${(qrCodePath)!}" >
            </span>
        <span class="span"></span>
    </div>
</div>

</body>