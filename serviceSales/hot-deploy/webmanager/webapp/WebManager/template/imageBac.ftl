
<head>

    <title>Images grid</title>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>





    <style>
        body {
            font-family: sans-serif;
        }
        p {
            text-align: center;
            font-weight: bold;
        }
        .imgs-grid {
            max-width: 800px;
            margin: 0 auto;
            font-size: 0;
        }
        .imgs-grid.imgs-grid-1 .imgs-grid-image {
            width: 100%;
            text-align: center;
        }
        .imgs-grid.imgs-grid-2 .imgs-grid-image,
        .imgs-grid.imgs-grid-4 .imgs-grid-image {
            width: 50%;
        }
        .imgs-grid.imgs-grid-3 .imgs-grid-image,
        .imgs-grid.imgs-grid-6 .imgs-grid-image {
            width: 33.333333333333336%;
        }
        .imgs-grid.imgs-grid-5 .imgs-grid-image:nth-child(1),
        .imgs-grid.imgs-grid-5 .imgs-grid-image:nth-child(2),
        .imgs-grid.imgs-grid-5 .imgs-grid-image:nth-child(3) {
            width: 33.333333333333336%;
        }
        .imgs-grid.imgs-grid-5 .imgs-grid-image:nth-child(4),
        .imgs-grid.imgs-grid-5 .imgs-grid-image:nth-child(5) {
            width: 50%;
        }
        .imgs-grid .imgs-grid-image {
            position: relative;
            display: inline-block;
            padding: 1px;
            box-sizing: border-box;
        }
        .imgs-grid .imgs-grid-image:hover {
            cursor: pointer;
        }
        .imgs-grid .imgs-grid-image .image-wrap {
            position: relative;
            display: inline-block;
            overflow: hidden;
            vertical-align: middle;
        }
        .imgs-grid .imgs-grid-image .image-wrap img {
            position: relative;
            width: 100%;
            height: auto;
            margin: 0;
        }
        .imgs-grid .imgs-grid-image .view-all {
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            text-align: center;
        }
        .imgs-grid .imgs-grid-image .view-all:before {
            display: inline-block;
            content: "";
            vertical-align: middle;
            height: 100%;
        }
        .imgs-grid .imgs-grid-image .view-all:hover {
            cursor: pointer;
        }
        .imgs-grid .imgs-grid-image .view-all:hover .view-all-text {
            text-decoration: underline;
        }
        .imgs-grid .imgs-grid-image .view-all .view-all-cover {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: black;
            opacity: 0.4;
        }
        .imgs-grid .imgs-grid-image .view-all .view-all-text {
            position: relative;
            font-size: 16px;
            font-family: sans-serif;
            color: white;
        }
        @media (max-width: 350px) {
            .imgs-grid .imgs-grid-image .view-all .view-all-text {
                font-size: 10px;
            }
        }
        .imgs-grid-modal {
            position: fixed;
            left: 0;
            right: 0;
            top: 0;
            bottom: 0;
            background-color: black;
            opacity: 0;
            z-index: 100;
            -webkit-user-select: none;
            -moz-user-select: -moz-none;
            -khtml-user-select: none;
            -o-user-select: none;
            user-select: none;
        }
        .imgs-grid-modal .modal-caption {
            padding: 30px 50px;
            text-align: center;
            color: white;
        }
        .imgs-grid-modal .modal-close {
            position: absolute;
            right: 10px;
            top: 10px;
            width: 35px;
            height: 35px;
            background-image: url('http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/imgs-grid-icons.png');
            background-repeat: no-repeat;
            background-position: -100px;
        }
        .imgs-grid-modal .modal-close:hover {
            cursor: pointer;
        }
        .imgs-grid-modal .modal-inner {
            position: absolute;
            top: 60px;
            bottom: 60px;
            left: 0;
            right: 0;
        }
        .imgs-grid-modal .modal-inner .modal-control {
            position: absolute;
            top: 0;
            bottom: 0;
            width: 70px;
        }
        .imgs-grid-modal .modal-inner .modal-control:hover {
            cursor: pointer;
        }
        .imgs-grid-modal .modal-inner .modal-control.left {
            left: 0;
        }
        .imgs-grid-modal .modal-inner .modal-control.right {
            right: 0;
        }
        .imgs-grid-modal .modal-inner .modal-control .arrow {
            margin: 0 auto;
            height: 100%;
            width: 40px;
            background-repeat: no-repeat;
            background-image: url('http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/imgs-grid-icons.png');
        }
        .imgs-grid-modal .modal-inner .modal-control .arrow.left {
            background-position: 2px center;
        }
        .imgs-grid-modal .modal-inner .modal-control .arrow.right {
            background-position: -42px center;
        }
        .imgs-grid-modal .modal-inner .modal-image {
            position: absolute;
            top: 0;
            left: 70px;
            right: 70px;
            bottom: 0;
            text-align: center;
        }
        .imgs-grid-modal .modal-inner .modal-image:before {
            display: inline-block;
            content: "";
            vertical-align: middle;
            height: 100%;
        }
        .imgs-grid-modal .modal-inner .modal-image img {
            max-width: 100%;
            max-height: 100%;
            vertical-align: middle;
        }
        .imgs-grid-modal .modal-inner .modal-image img:hover {
            cursor: pointer;
        }
        @media (max-width: 800px) {
            .imgs-grid-modal .modal-inner .modal-control {
                width: 40px;
            }
            .imgs-grid-modal .modal-inner .modal-control .arrow {
                -webkit-transform: scale(0.7);
                -moz-transform: scale(0.7);
                -o-transform: scale(0.7);
                -ms-transform: scale(0.7);
                transform: scale(0.7);
            }
            .imgs-grid-modal .modal-inner .modal-image {
                left: 0;
                right: 0;
            }
        }
        .imgs-grid-modal .modal-indicator {
            position: absolute;
            bottom: 0;
            height: 60px;
            width: 100%;
            text-align: center;
        }
        .imgs-grid-modal .modal-indicator ul {
            margin: 0;
            padding: 0;
        }
        .imgs-grid-modal .modal-indicator ul li {
            display: inline-block;
            width: 12px;
            height: 12px;
            border: 1px solid white;
            box-sizing: border-box;
            border-radius: 100%;
            margin: 0 1px;
            vertical-align: middle;
        }
        .imgs-grid-modal .modal-indicator ul li:hover {
            cursor: pointer;
        }
        .imgs-grid-modal .modal-indicator ul li.selected {
            background-color: white;
            width: 14px;
            height: 14px;
            margin: 0;
        }

    </style>

</head>
<body>

<p>More than five images</p>
<div id="gallery1"></div>

<p>Diff-size images</p>
<div id="gallery2"></div>

<script>

//    $('#gallery1').imagesGrid({
//        images: [
//            'imgs/1.jpg',
//            { src: 'imgs/2.jpg', alt: 'Second image', title: 'Second image' },
//            'imgs/3.jpg',
//            { src: 'imgs/4.jpg', caption: 'Beautiful forest' },
//            'imgs/5.jpg'
//        ]
//    });

//    $('#gallery2').imagesGrid({
//        images: [
//            'imgs/diff-size/1.jpg',
//            'imgs/diff-size/2.jpg',
//            'imgs/diff-size/3.jpg',
//            'imgs/diff-size/4.jpg',
//            'imgs/diff-size/5.jpg'
//        ],
//        align: true
//    });

</script>

