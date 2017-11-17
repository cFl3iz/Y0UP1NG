<head>
    <meta charset="utf-8"/>
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache">
    <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
    <meta HTTP-EQUIV="expires" CONTENT="0">
    <meta name="apple-touch-fullscreen" content="yes"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0 user-scalable=no" media="screen"/>
    <title>${uiLabel.to}${(payToPartyFirstName)!}${uiLabel.chat}</title>
    <script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=NYGTLd8V2Lz3cGsolRnZzXaHmuXW2XZ3"></script>
    <style type="text/css">
        #allmap{height:80px;width:100px;}
        #r-result{width:100%; font-size:14px;}
        body {
            background: url(http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/chatBG.jpeg) no-repeat;
            background-size: 100%;
            background-attachment: fixed;
        }

        @media all and (min-width: 640px) {
            body, html, .wenwen-footer, .speak_window {
                width: 640px !important;
                margin: 0 auto
            }

            .speak_window, .wenwen-footer {
                left: 50% !important;
                margin-left: -320px
            }
        }

        input, button {
            outline: none;
        }

        .wenwen-footer {
            width: 100%;
            position: fixed;
            bottom: -5px;
            left: 0;
            background: #fff;
            padding: 3%;
            border-top: solid 1px #ddd;
            box-sizing: border-box;
        }

        .wenwen_btn, .wenwen_help {
            width: 15%;
            text-align: center;
        }

        .wenwen_btn img, .wenwen_help img {
            height: 40px;
        }

        .wenwen_text {
            height: 40px;
            border-radius: 5px;
            border: solid 1px #636162;
            box-sizing: border-box;
            width: 66%;
            text-align: center;
            overflow: hidden;
            margin-left: 2%;
        }

        .circle-button {
            padding: 0 5px;
        }

        .wenwen_text .circle-button {
            font-size: 14px;
            color: #666;
            line-height: 38px;
        }

        .write_box {
            background: #fff;
            width: 100%;
            height: 40px;
            line-height: 40px;
            display: none;
        }

        .write_box input {
            height: 40px;
            padding: 0 5px;
            line-height: 40px;
            width: 100%;
            box-sizing: border-box;
            border: 0;
        }

        .wenwen_help button {
            width: 95%;
            background: #42929d;
            color: #fff;
            border-radius: 5px;
            border: 0;
            height: 40px;
            display: none;
        }

        #wenwen {
            height: 100%;
        }

        .speak_window {
            overflow-y: scroll;
            height: 100%;
            width: 100%;
            position: fixed;
            top: 0;
            left: 0;
        }

        .speak_box {
            margin-bottom: 70px;
            padding: 10px;
        }

        .question, .answer {
            margin-bottom: 1rem;
        }

        .question {
            text-align: right;
        }

        .question > div {
            display: inline-block;
        }

        .left {
            float: left;
        }

        .right {
            float: right;
        }

        .clear {
            clear: both;
        }

        .heard_img {
            height: 60px;
            width: 60px;
            border-radius: 5px;
            overflow: hidden;
            background: #ddd;
        }

        .heard_img img {
            width: 100%;
            height: 100%
        }

        .question_text, .answer_text {
            box-sizing: border-box;
            position: relative;
            display: table-cell;
            min-height: 60px;
        }

        .question_text {
            padding-right: 20px;
        }

        .answer_text {
            padding-left: 20px;
        }

        .question_text p, .answer_text p {
            border-radius: 10px;
            padding: .5rem;
            margin: 0;
            font-size: 16px;
            line-height: 28px;
            box-sizing: border-box;
            vertical-align: middle;
            display: table-cell;
            height: 60px;
            word-wrap: break-word;
        }

        .answer_text p {
            background: #fff;
        }

        .question_text p {
            background: #42929d;
            color: #fff;
            text-align: left;
        }

        .question_text i, .answer_text i {
            width: 0;
            height: 0;
            border-top: 5px solid transparent;
            border-bottom: 5px solid transparent;
            position: absolute;
            top: 25px;
        }

        .answer_text i {
            border-right: 10px solid #fff;
            left: 10px;
        }

        .question_text i {
            border-left: 10px solid #42929d;
            right: 10px;
        }

        .answer_text p a {
            color: #42929d;
            display: inline-block;
        }

        audio {
            display: none;
        }

        .saying {
            position: fixed;
            bottom: 30%;
            left: 50%;
            width: 120px;
            margin-left: -60px;
            display: none;
        }

        .saying img {
            width: 100%;
        }

        .write_list {
            font-size: 18px;
            position: absolute;
            left: 0;
            width: 100%;
            background: #fff;
            border-top: solid 1px #ddd;
            padding: 5px;
            line-height: 30px;
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
            background-image: url(img/imgs-grid-icons.png);
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
            background-image: url(img/imgs-grid-icons.png);
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
    <script>

        var messagesArray = new Array();

        var images  = new Array();

        function flushMessage() {
            var url = "queryMessage";
            var param = {
                payToPartyId:${(payToPartyId)!}
            };
            $.ajax({
                type: 'POST',
                url: url,
                data: param,
                success: function (data) {
                    alert(data);
                },
                error: function (data) {
                    alert("ERROR :" + data.status);
                }
            });
        }


        $(
                function () {


                    $("#wenwen_btn_left").click();

                    //初始化历史聊天记录
                    iniChatPage();








                }
        );
    </script>
    <script>

        (function($) {
            alert("s");
            // Plugin
            $.fn.imagesGrid = function(options) {

                var args = arguments;

                return this.each(function() {

                    if ($.isPlainObject(options)) {
                        // Create ImagesGrid
                        var cfg = $.extend({}, $.fn.imagesGrid.defaults, options);
                        cfg.element = $(this);
                        this._imgGrid = new ImagesGrid(cfg);
                        this._imgGrid.render();
                        return;
                    }

                    if (this._imgGrid) {
                        switch (options) {
                            case 'modal.open':
                                this._imgGrid.modal.open(args[1]);
                                break;
                            case 'modal.close':
                                this._imgGrid.modal.close();
                                break;
                        }
                    }

                });

            };

            // Plugin default options
            $.fn.imagesGrid.defaults = {
                images: [],
                cells: 5,
                align: false,
                nextOnClick: true,
                getViewAllText: function(imagesCount) {
                    return 'View all ' + imagesCount + ' images';
                },
                onGridRendered: $.noop,
                onGridItemRendered: $.noop,
                onGridLoaded: $.noop,
                onGridImageLoaded: $.noop,
                onModalOpen: $.noop,
                onModalClose: $.noop,
                onModalImageClick: $.noop
            };

            /*
              ImagesGrid constructor
               *cfg         - Configuration object
               *cfg.element - jQuery element
               *cfg.images  - Array of images urls of images option objects
                cfg.align   - Aling diff-size images
                cfg.cells   - Max grid cells (1-6)
                cfg.getViewAllText     - Returns text for "view all images" link,
                cfg.onGridRendered     - Called when grid items added to the DOM
                cfg.onGridItemRendered - Called when grid item added to the DOM
                cfg.onGridLoaded       - Called when grid images loaded
                cfg.onGridImageLoaded  - Called when grid image loaded
            */
            function ImagesGrid(cfg) {

                cfg = cfg || {};

                this.images = cfg.images;
                this.isAlign = cfg.align;
                this.maxGridCells = (cfg.cells < 1)? 1: (cfg.cells > 6)? 6: cfg.cells;
                this.imageLoadCount = 0;
                this.modal = null;

                this.$window = $(window);
                this.$el = cfg.element;
                this.$gridItems = [];

                this.render = function() {

                    this.setGridClass();
                    this.renderGridItems();

                    this.modal = new ImagesGridModal({
                        images: cfg.images,
                        nextOnClick: cfg.nextOnClick,
                        onModalOpen: cfg.onModalOpen,
                        onModalClose: cfg.onModalClose,
                        onModalImageClick: cfg.onModalImageClick
                    });

                    this.$window.on('resize', this.resize.bind(this));

                };

                this.setGridClass = function() {

                    this.$el.removeClass(function(index, classNames) {
                        if (/(imgs-grid-\d)/.test(classNames)) {
                            return RegExp.$1;
                        }
                    });

                    var cellsCount = (this.images.length > this.maxGridCells)?
                            this.maxGridCells: this.images.length;

                    this.$el.addClass('imgs-grid imgs-grid-' + cellsCount);

                };

                this.renderGridItems = function() {

                    if (!this.images) {
                        return;
                    }

                    this.$el.empty();
                    this.$gridItems = [];

                    for (var i = 0; i < this.images.length; ++i) {
                        if (i == this.maxGridCells) {
                            break;
                        }
                        this.renderGridItem(this.images[i], i);
                    }

                    if (this.images.length > this.maxGridCells) {
                        this.renderViewAll();
                    }

                    cfg.onGridRendered(this.$el);

                };

                this.renderGridItem = function(image, index) {

                    var src = image,
                            alt = '',
                            title = '';

                    if ($.isPlainObject(image)) {
                        src = image.src;
                        alt = image.alt || '';
                        title = image.title || '';
                    }

                    var item = $('<div>', {
                        class: 'imgs-grid-image',
                        click: this.imageClick.bind(this),
                        data: { index: index }
                    });

                    var self = this;

                    item.append(
                            $('<div>', {
                                class: 'image-wrap'
                            }).append(
                                    $('<img>', {
                                        src: src,
                                        alt: alt,
                                        title: title,
                                        load: function(event) {
                                            self.imageLoaded(event, $(this), image);
                                        }
                                    })
                            )
                    );

                    this.$gridItems.push(item);

                    this.$el.append(item);

                    cfg.onGridItemRendered(item, image);

                };

                this.renderViewAll = function() {

                    this.$el.find('.imgs-grid-image:last .image-wrap').append(
                            $('<div>', {
                                class: 'view-all'
                            }).append(
                                    $('<span>', {
                                        class: 'view-all-cover',
                                    }),
                                    $('<span>', {
                                        class: 'view-all-text',
                                        text: cfg.getViewAllText(this.images.length)
                                    })
                            )
                    );

                };

                this.resize = function(event) {
                    if (this.isAlign) {
                        this.align();
                    }
                };

                this.imageClick = function(event) {
                    var imageIndex = $(event.currentTarget).data('index');
                    this.modal.open(imageIndex);
                };

                this.imageLoaded = function(event, imageEl, image) {

                    ++this.imageLoadCount;

                    if (this.imageLoadCount == this.$gridItems.length) {
                        this.imageLoadCount = 0;
                        this.allImagesLoaded()
                    }

                    cfg.onGridImageLoaded(event, imageEl, image)

                };

                this.allImagesLoaded = function() {

                    if (this.isAlign) {
                        this.align();
                    }

                    cfg.onGridLoaded(this.$el);

                };

                this.align = function() {

                    var len = this.$gridItems.length;

                    switch (len) {
                        case 2:
                        case 3:
                            this.alignItems(this.$gridItems);
                            break;
                        case 4:
                            this.alignItems(this.$gridItems.slice(0, 2));
                            this.alignItems(this.$gridItems.slice(2));
                            break;
                        case 5:
                        case 6:
                            this.alignItems(this.$gridItems.slice(0, 3));
                            this.alignItems(this.$gridItems.slice(3));
                            break;
                    }

                };

                this.alignItems = function(items) {

                    var height = items.map(function(item) {
                        return item.find('img').height();
                    });

                    var itemHeight = Math.min.apply(null, height);

                    $(items).each(function() {

                        var item = $(this),
                                imgWrap = item.find('.image-wrap'),
                                img = item.find('img'),
                                imgHeight = img.height();

                        imgWrap.height(itemHeight);

                        if (imgHeight > itemHeight) {
                            var top = Math.floor((imgHeight - itemHeight) / 2);
                            img.css({ top: -top });
                        }

                    });

                };

            }

            /*
              ImagesGridModal constructor
               *cfg             - Configuration object
               *cfg.images      - Array of string or objects
                cfg.nextOnClick - Show next image when click on modal image
                cfg.onModalOpen       - Called when modal opened
                cfg.onModalClose      - Called when modal closed
                cfg.onModalImageClick - Called on modal image click
            */
            function ImagesGridModal(cfg) {

                this.images = cfg.images;
                this.imageIndex = null;

                this.$modal = null;
                this.$indicator = null;
                this.$document = $(document);

                this.open = function(imageIndex) {

                    if (this.$modal && this.$modal.is(':visible')) {
                        return;
                    }

                    this.imageIndex = parseInt(imageIndex) || 0;

                    this.render();

                };

                this.close = function(event) {

                    if (!this.$modal) {
                        return;
                    }

                    this.$modal.animate({
                        opacity: 0
                    }, {
                        duration: 100,
                        complete: function() {

                            this.$modal.remove();
                            this.$modal = null;
                            this.$indicator = null;
                            this.imageIndex = null;

                            cfg.onModalClose();

                        }.bind(this)
                    });

                    this.$document.off('keyup', this.keyUp);

                };

                this.render = function() {

                    this.renderModal();
                    this.renderCaption();
                    this.renderCloseButton();
                    this.renderInnerContainer();
                    this.renderIndicatorContainer();

                    this.keyUp = this.keyUp.bind(this);
                    this.$document.on('keyup', this.keyUp);

                    var self = this;

                    this.$modal.animate({
                        opacity: 1
                    }, {
                        duration: 100,
                        complete: function() {
                            cfg.onModalOpen(self.$modal);
                        }
                    });

                };

                this.renderModal = function() {
                    this.$modal = $('<div>', {
                        class: 'imgs-grid-modal'
                    }).appendTo('body');
                };

                this.renderCaption = function() {

                    this.$caption = $('<div>', {
                        class: 'modal-caption',
                        text: this.getImageCaption(this.imageIndex)
                    }).appendTo(this.$modal);

                };

                this.renderCloseButton = function() {
                    this.$modal.append($('<div>', {
                        class: 'modal-close',
                        click: this.close.bind(this)
                    }));
                };

                this.renderInnerContainer = function() {

                    var image = this.getImage(this.imageIndex),
                            self = this;

                    this.$modal.append(
                            $('<div>', {
                                class: 'modal-inner'
                            }).append(
                                    $('<div>', {
                                        class: 'modal-image'
                                    }).append(
                                            $('<img>', {
                                                src: image.src,
                                                alt: image.alt,
                                                title: image.title,
                                                click: function(event) {
                                                    self.imageClick(event, $(this), image);
                                                }
                                            })
                                    ),
                                    $('<div>', {
                                        class: 'modal-control left',
                                        click: this.prev.bind(this)
                                    }).append(
                                            $('<div>', {
                                                class: 'arrow left'
                                            })
                                    ),
                                    $('<div>', {
                                        class: 'modal-control right',
                                        click: this.next.bind(this)
                                    }).append(
                                            $('<div>', {
                                                class: 'arrow right'
                                            })
                                    )
                            )
                    );

                    if (this.images.length <= 1) {
                        this.$modal.find('.modal-control').hide();
                    }

                };

                this.renderIndicatorContainer = function() {

                    if (this.images.length == 1) {
                        return;
                    }

                    this.$indicator = $('<div>', {
                        class: 'modal-indicator'
                    });

                    var list = $('<ul>');

                    for (var i = 0; i < this.images.length; ++i) {
                        list.append($('<li>', {
                            class: this.imageIndex == i? 'selected': '',
                            click: this.indicatorClick.bind(this),
                            data: { index: i }
                        }));
                    }

                    this.$indicator.append(list);

                    this.$modal.append(this.$indicator);

                };

                this.prev = function() {
                    if (this.imageIndex > 0) {
                        --this.imageIndex;
                    } else {
                        this.imageIndex = this.images.length - 1;
                    }
                    this.updateImage();
                };

                this.next = function() {
                    if (this.imageIndex < this.images.length - 1) {
                        ++this.imageIndex;
                    } else {
                        this.imageIndex = 0;
                    }
                    this.updateImage();
                };

                this.updateImage = function() {

                    var image = this.getImage(this.imageIndex);

                    this.$modal.find('.modal-image img').attr({
                        src: image.src,
                        alt: image.alt,
                        title: image.title
                    });

                    this.$modal.find('.modal-caption').text(
                            this.getImageCaption(this.imageIndex) );

                    if (this.$indicator) {
                        var indicatorList = this.$indicator.find('ul');
                        indicatorList.children().removeClass('selected');
                        indicatorList.children().eq(this.imageIndex).addClass('selected');
                    }

                };

                this.imageClick = function(event, imageEl, image) {

                    if (cfg.nextOnClick) {
                        this.next();
                    }

                    cfg.onModalImageClick(event, imageEl, image);

                };

                this.indicatorClick = function(event) {
                    var index = $(event.target).data('index');
                    this.imageIndex = index;
                    this.updateImage();
                };

                this.keyUp = function(event) {
                    if (this.$modal) {
                        switch (event.keyCode) {
                            case 27: // Esc
                                this.close();
                                break;
                            case 37: // Left arrow
                                this.prev();
                                break;
                            case 39: // Right arrow
                                this.next();
                                break;
                        }
                    }
                };

                this.getImage = function(index) {
                    var image = this.images[index];
                    if ($.isPlainObject(image)) {
                        return image;
                    } else {
                        return { src: image, alt: '', title: '' }
                    }
                };

                this.getImageCaption = function(imgIndex) {
                    var img = this.getImage(imgIndex);
                    return img.caption || '';
                };

            }

        })(jQuery);

    </script>
</head>
<body>
<input type="hidden" value="${uiLabel.About}" id="about"/>

<div id="gallery2"></div>

<div class="speak_window">
    <div class="speak_box">
        <div class="answer" id="firstAboutMessage" style="display:none;">
            <div class="heard_img left">
                <img src="${(payToPartyHead)!}">
            </div>
            <div class="answer_text" >
                <p >${uiLabel.About}<br/>
                <#--<a href="#">${uiLabel.BuyNow}</a>-->
                </p>
                <i></i>
            </div>
        </div>
    </div>
</div>
<div class="saying">
    <img src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/saying.gif">
</div>
<div class="wenwen-footer">
    <div class="wenwen_btn left" id="wenwen_btn_left" onClick="to_write()">
        <img src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/jp_btn.png">
    </div>
    <div class="wenwen_text left">
        <div class="write_box">
            <input type="text" class="left" onKeyUp="keyup()" placeholder=""/>
        </div>
        <div class="circle-button" id="wenwen">
        ${uiLabel.clickOnSay}
        </div>
    </div>
    <div class="wenwen_help right">
        <a href="javascript:window.history.back();">
            <img src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/help_btn.png">
        </a>
        <button onClick="up_say()" class="right">${uiLabel.send}</button>
    </div>
    <div style="opacity:0;" class="clear"></div>
</div>
<#--<script type="text/javascript" src="js/jquery-1.11.1.min.js"></script>-->
<input type="hidden" id="payToPartyId" value="${(payToPartyId)!}"/>
<input type="hidden" id="payFromPartyId" value="${(personInfo.partyId)!}"/>
<input type="hidden" id="tarjeta" value="${(tarjeta)!}"/>
<input type="hidden" id="productId" value="${(productId)!}"/>

<input type="hidden" id="payToPartyHead" value="${(payToPartyHead)!}"/>
<input type="hidden" id="payFromPartyHead" value="${(personInfo.headPortrait)!}"/>

<script type="text/javascript">





    // 用经纬度设置地图中心点
    function theLocation(name,longitude,latitude){
        // 百度地图API功能
        var map = new BMap.Map("allmap");
        map.centerAndZoom(new BMap.Point(116.331398,39.897445),11);
        map.enableScrollWheelZoom(true);

        if(longitude != "" && latitude != ""){
            map.clearOverlays();
            var new_point = new BMap.Point(longitude,latitude);
            var marker = new BMap.Marker(new_point);  // 创建标注
            map.addOverlay(marker);              // 将标注添加到地图中
            map.panTo(new_point);
        }
    }
























    var payToPartyHead = $("#payToPartyHead").val();
    var payFromPartyHead = $("#payFromPartyHead").val();
    payToPartyHead   = payToPartyHead   + "?x-oss-process=image/resize,w_50,h_50/quality,q_60";
    payFromPartyHead = payFromPartyHead + "?x-oss-process=image/resize,w_50,h_50/quality,q_60";
    function to_write() {
        $('.wenwen_btn img').attr('src', 'http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/yy_btn.png');
        $('.wenwen_btn').attr('onclick', 'to_say()');
        $('.write_box,.wenwen_help button').show();
        $('.circle-button,.wenwen_help a').hide();
        $('.write_box input').focus();
        for_bottom();
    }

    function to_say() {
        $('.write_list').remove();
        $('.wenwen_btn img').attr('src', 'http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/jp_btn.png');
        $('.wenwen_btn').attr('onclick', 'to_write()');
        $('.write_box,.wenwen_help button').hide();
        $('.circle-button,.wenwen_help a').show();
    }

    /*
    初始化聊天界面,如果已经有历史聊天记录就显示出来
    */
    function iniChatPage(){
        var payToPartyId = $("#payToPartyId").val();
        var payFromPartyId = $("#payFromPartyId").val();
        var tarjeta = $("#tarjeta").val();

        var url = "loadMessage";

        var param = {
            partyIdTo: payFromPartyId,
            tarjeta: tarjeta,
            partyIdFrom: payToPartyId,
            bizType: "webChat"
        };

        $.ajax({
            type: 'POST',
            url: url,
            data: param,
            success: function (data) {

                var messages = eval(data.messages);

                for (var i = 0; i < messages.length; i++) {
//                    alert(messages[i].messageId+" ");
                    var partyIdTo = messages[i].user.toPartyId;

                    var text =  messages[i].text;
                 //   alert("toPartyId="+partyIdTo);
                //    alert("text= "+ text);
                    var bool = (payToPartyId === partyIdTo);
               //     alert("bool = " + bool+"");

                    var messageId = messages[i].messageId;

                    var messageLogTypeId =   messages[i].messageLogTypeId;

                    // True 等于 左边
                    if (bool) {
                        if(null != messageLogTypeId && messageLogTypeId === "IMAGE"){
                            images.push(text);
                            text = text + "?x-oss-process=image/resize,w_100,h_80/quality,q_75";
                            var ans = '<div class="answer"><div class="heard_img left"><img src="' + payToPartyHead + '"></div>';
                            ans += '<div class="answer_text"><a href="#"><img style="width:100px;height:80px;no-repeat;"  src=' + text + '></img></a><i></i>';
                            ans += '</div></div>';
                            $('.speak_box').append(ans);
                            messagesArray.push(messageId);
//                            alert(images);
                            $('#gallery2').imagesGrid({
                                images: images,
                                align: true
                            });
                        }
                        if(null != messageLogTypeId && messageLogTypeId === "TEXT"){
                            var ans = '<div class="answer"><div class="heard_img left"><img src="' + payToPartyHead + '"></div>';
                            ans += '<div class="answer_text"><p>' + text + '</p><i></i>';
                            ans += '</div></div>';
                            $('.speak_box').append(ans);
                            messagesArray.push(messageId);
                        }
                        if(null != messageLogTypeId && messageLogTypeId === "LOCATION"){
                            var latitude = messages[i].latitude;
                            var longitude = messages[i].longitude;
                            var tip = "${uiLabel.MyLocation}";
                            var baiduUrl = "http://api.map.baidu.com/marker?location="+latitude+","+ longitude +"&title="+tip+"&content="+tip+"&output=html";
                            var ans =  '<a href="'+baiduUrl+'">'+ '<div class="answer"><div class="heard_img left"><img src="' + payToPartyHead + '"></div>';
                            ans += '<div class="answer_text"><div style="width:180px;height:100px;" id="' + "allmap" + '"></div><i></i>';
                            ans += '</div></div></a>';
                            $('.speak_box').append(ans);
                            messagesArray.push(messageId);


                                    theLocation(longitude,longitude,latitude);
                        }
                       // autoWidth();
                     //   for_bottom();
                      //  for_bottom();
                    }else{

                        if(null != messageLogTypeId && messageLogTypeId === "IMAGE"){
                            images.push(text);
                            var str = '<div class="question">';
                            text = text + "?x-oss-process=image/resize,w_100,h_80/quality,q_75";
                            str += '<div class="heard_img right"><img src="' + payFromPartyHead + '"></div>';
                            str += '<div class="question_text clear"><a href="#"><img style="width:100px;height:80px;no-repeat;"  src=' + text + '></img></a><i></i>';
                            str += '</div></div>';
                            $('.speak_box').append(str);
                            $('.write_box input').val('');

//                            alert(images);
                            $('#gallery2').imagesGrid({
                                images: images,
                                align: true
                            });
                        }
                        if(null != messageLogTypeId && messageLogTypeId === "TEXT"){
                            var str = '<div class="question">';
                            str += '<div class="heard_img right"><img src="' + payFromPartyHead + '"></div>';
                            str += '<div class="question_text clear"><p>' + text + '</p><i></i>';
                            str += '</div></div>';
                            $('.speak_box').append(str);
                            $('.write_box input').val('');
                        }
                        if(null != messageLogTypeId && messageLogTypeId === "LOCATION"){
                            var latitude = messages[i].latitude;
                            var longitude = messages[i].longitude;
                            var tip = "${uiLabel.MyLocation}";
                            var baiduUrl = "http://api.map.baidu.com/marker?location="+latitude+","+ longitude +"&title="+tip+"&content="+tip+"&output=html";
                            var str =  '<a href="'+baiduUrl+'">'+ '<div class="question">';
                            str += '<div class="heard_img right"><img src="' + payFromPartyHead + '"></div>';
                            str +=  '<div class="question_text clear"><div style="width:180px;height:100px;" id="' + "allmap" + '"></div><i></i>';
                            str += '</div></div></a>';
                            $('.speak_box').append(str);
                            $('.write_box input').val('');

                            theLocation(longitude,longitude,latitude);
                        }


//                        $('.write_box input').focus();

                    //    for_bottom();
                    }
//                    messagesArray.push(messages[i].messageId);

                }
                autoWidth();
                for_bottom();

//                alert(messagesArray);
            },
            error: function (data) {
                alert("ERROR :" + data.status);
            }
        });

        if(messagesArray.length == null){
            $("#firstAboutMessage").show();
        }




    }


    /**
     * 发送说的话
     */
    function up_say() {
        $('.write_list').remove();

        var text = $('.write_box input').val(),
                str = '<div class="question">';
        str += '<div class="heard_img right"><img src="' + payFromPartyHead + '"></div>';
        str += '<div class="question_text clear"><p>' + text + '</p><i></i>';
        str += '</div></div>';

        if (text == '') {
            alert('NULL!');
            $('.write_box input').focus();
        } else {
            $('.speak_box').append(str);
            $('.write_box input').val('');
            $('.write_box input').focus();
            autoWidth();
            for_bottom();

        }

        var payToPartyId = $("#payToPartyId").val();
        var payFromPartyId = $("#payFromPartyId").val();
        var tarjeta = $("#tarjeta").val();
        var productId = $("#productId").val();
        var url = "pushMessage";
//        alert("productId = " + productId);
        var param = {
            partyIdTo: payToPartyId,
            tarjeta: tarjeta,
            partyIdFrom: payFromPartyId,
            text: text,
            objectId: productId
        };
        $.ajax({
            type: 'POST',
            url: url,
            data: param,
            success: function (data) {
//                alert(data.messages);
            },
            error: function (data) {
                alert("ERROR :" + data.status);
            }
        });
    }


    setInterval(function () {
//                var ans  = '<div class="answer"><div class="heard_img left"><img src="' + payToPartyHead + '"></div>';
//                ans += '<div class="answer_text"><p>You Say :'+text+'</p><i></i>';
//                ans += '</div></div>';
//                $('.speak_box').append(ans);
//                for_bottom();

        var payToPartyId = $("#payToPartyId").val();

        var payFromPartyId = $("#payFromPartyId").val();

        var tarjeta = $("#tarjeta").val();

        var url = "loadMaiJiaMessage";

        var param = {
            partyIdTo: payFromPartyId,
            tarjeta: tarjeta,
            partyIdFrom: payToPartyId,
            bizType: "webChat"
        };
        //alert("messagesArray="+messagesArray);
        $.ajax({
            type: 'POST',
            url: url,
            data: param,
            success: function (data) {
                var messages = eval(data.messages);
            //    alert("messages.length = "+messages.length);
                for (var i = 0; i < messages.length; i++) {
//                    alert(messages[i].messageId+" ");
                    var messageId = messages[i].messageId;
                    //alert("now messageId = " + messageId);
                  //  alert("messagesArray.length = " + messagesArray.length);
                    if ( 0 == messagesArray.length) {
                        messagesArray.push(messageId);
                        var text =  messages[i].text;
                        var ans = '<div class="answer"><div class="heard_img left"><img src="' + payToPartyHead + '"></div>';
                        ans += '<div class="answer_text"><p>' + text + '</p><i></i>';
                        ans += '</div></div>';
                        $('.speak_box').append(ans);
                        autoWidth();
                        for_bottom();
                    }else{
                      //  alert(" messagesArray = " + messagesArray);
                        if($.inArray(messageId, messagesArray)>=0){
                           // alert(" messageId in messagesArray contains != " + messageId);
                        }else{
                          //  alert(" else messagesArray no contains = " + messageId);
                            messagesArray.push(messageId);
                            var text =  messages[i].text;
                            var ans = '<div class="answer"><div class="heard_img left"><img src="' + payToPartyHead + '"></div>';
                            ans += '<div class="answer_text"><p>' + text + '</p><i></i>';
                            ans += '</div></div>';
                            $('.speak_box').append(ans);
                            autoWidth();
                            for_bottom();
                        }
                    }
//                    messagesArray.push(messages[i].messageId);

                }
//                alert(messagesArray);
            },
            error: function (data) {
                alert("ERROR :" + data.status);
            }
        });


    }, 8000);


    //输入文字的放大镜效果
    function keyup() {
//        var footer_height = $('.wenwen-footer').outerHeight(),
//                text = $('.write_box input').val(),
//                str = '<div class="write_list">'+text+'</div>';
//        if (text == '' || text == undefined){
//            $('.write_list').remove();
//        }else{
//            $('.wenwen-footer').append(str);
//            $('.write_list').css('bottom',footer_height);
//        }
    }

    var wen = document.getElementById('wenwen');
    function _touch_start(event) {
        event.preventDefault();
        $('.wenwen_text').css('background', '#c1c1c1');
        $('.wenwen_text span').css('color', '#fff');
        $('.saying').show();
    }

    function _touch_end(event) {
        event.preventDefault();
        $('.wenwen_text').css('background', '#fff');
        $('.wenwen_text .circle-button').css('color', '#666');
        $('.saying').hide();
        var str = '<div class="question">';
        str += '<div class="heard_img right"><img src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png"></div>';
        str += '<div class="question_text clear"><p>Send Soud</p><i></i>';
        str += '</div></div>';
        $('.speak_box').append(str);
        for_bottom();
        setTimeout(function () {
            var ans = '<div class="answer"><div class="heard_img left"><img src="http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png"></div>';
            ans += '<div class="answer_text"><p>I dont know</p><i></i>';
            ans += '</div></div>';
            $('.speak_box').append(ans);
            for_bottom();
        }, 1000);
    }

    wen.addEventListener("touchstart", _touch_start, false);
    wen.addEventListener("touchend", _touch_end, false);

    function for_bottom() {
        var speak_height = $('.speak_box').height();
        $('.speak_box,.speak_window').animate({scrollTop: speak_height}, 500);
    }

    function autoWidth() {
        $('.question_text').css('max-width', $('.question').width() - 60);
    }
    autoWidth();
</script>
</body>
