<head>
    <meta content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" name="viewport"/>
    <meta content="yes" name="apple-mobile-web-app-capable"/>
    <meta content="black" name="apple-mobile-web-app-status-bar-style"/>
    <meta content="telephone=no" name="format-detection"
    <meta charset="utf-8"/>
    <title></title>
    <style>
        /**
 * Swiper 3.3.1
 * Most modern mobile touch slider and framework with hardware accelerated transitions
 *
 * http://www.idangero.us/swiper/
 *
 * Copyright 2016, Vladimir Kharlampidi
 * The iDangero.us
 * http://www.idangero.us/
 *
 * Licensed under MIT
 *
 * Released on: February 7, 2016
 */
        .swiper-container {
            margin: 0 auto;
            position: relative;
            overflow: hidden;
            z-index: 1
        }

        .swiper-container-no-flexbox .swiper-slide {
            float: left
        }

        .swiper-container-vertical > .swiper-wrapper {
            -webkit-box-orient: vertical;
            -moz-box-orient: vertical;
            -ms-flex-direction: column;
            -webkit-flex-direction: column;
            flex-direction: column
        }

        .swiper-wrapper {
            position: relative;
            width: 100%;
            height: 100%;
            z-index: 1;
            display: -webkit-box;
            display: -moz-box;
            display: -ms-flexbox;
            display: -webkit-flex;
            display: flex;
            -webkit-transition-property: -webkit-transform;
            -moz-transition-property: -moz-transform;
            -o-transition-property: -o-transform;
            -ms-transition-property: -ms-transform;
            transition-property: transform;
            -webkit-box-sizing: content-box;
            -moz-box-sizing: content-box;
            box-sizing: content-box
        }

        .swiper-container-android .swiper-slide, .swiper-wrapper {
            -webkit-transform: translate3d(0, 0, 0);
            -moz-transform: translate3d(0, 0, 0);
            -o-transform: translate(0, 0);
            -ms-transform: translate3d(0, 0, 0);
            transform: translate3d(0, 0, 0)
        }

        .swiper-container-multirow > .swiper-wrapper {
            -webkit-box-lines: multiple;
            -moz-box-lines: multiple;
            -ms-flex-wrap: wrap;
            -webkit-flex-wrap: wrap;
            flex-wrap: wrap
        }

        .swiper-container-free-mode > .swiper-wrapper {
            -webkit-transition-timing-function: ease-out;
            -moz-transition-timing-function: ease-out;
            -ms-transition-timing-function: ease-out;
            -o-transition-timing-function: ease-out;
            transition-timing-function: ease-out;
            margin: 0 auto
        }

        .swiper-slide {
            -webkit-flex-shrink: 0;
            -ms-flex: 0 0 auto;
            flex-shrink: 0;
            width: 100%;
            height: 100%;
            position: relative
        }

        .swiper-container-autoheight, .swiper-container-autoheight .swiper-slide {
            height: auto
        }

        .swiper-container-autoheight .swiper-wrapper {
            -webkit-box-align: start;
            -ms-flex-align: start;
            -webkit-align-items: flex-start;
            align-items: flex-start;
            -webkit-transition-property: -webkit-transform, height;
            -moz-transition-property: -moz-transform;
            -o-transition-property: -o-transform;
            -ms-transition-property: -ms-transform;
            transition-property: transform, height
        }

        .swiper-container .swiper-notification {
            position: absolute;
            left: 0;
            top: 0;
            pointer-events: none;
            opacity: 0;
            z-index: -1000
        }

        .swiper-wp8-horizontal {
            -ms-touch-action: pan-y;
            touch-action: pan-y
        }

        .swiper-wp8-vertical {
            -ms-touch-action: pan-x;
            touch-action: pan-x
        }

        .swiper-button-next, .swiper-button-prev {
            position: absolute;
            top: 50%;
            width: 27px;
            height: 44px;
            margin-top: -22px;
            z-index: 10;
            cursor: pointer;
            -moz-background-size: 27px 44px;
            -webkit-background-size: 27px 44px;
            background-size: 27px 44px;
            background-position: center;
            background-repeat: no-repeat
        }

        .swiper-button-next.swiper-button-disabled, .swiper-button-prev.swiper-button-disabled {
            opacity: .35;
            cursor: auto;
            pointer-events: none
        }

        .swiper-button-prev, .swiper-container-rtl .swiper-button-next {
            background-image: url("data:image/svg+xml;charset=utf-8,%3Csvg%20xmlns%3D'http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg'%20viewBox%3D'0%200%2027%2044'%3E%3Cpath%20d%3D'M0%2C22L22%2C0l2.1%2C2.1L4.2%2C22l19.9%2C19.9L22%2C44L0%2C22L0%2C22L0%2C22z'%20fill%3D'%23007aff'%2F%3E%3C%2Fsvg%3E");
            left: 10px;
            right: auto
        }

        .swiper-button-prev.swiper-button-black, .swiper-container-rtl .swiper-button-next.swiper-button-black {
            background-image: url("data:image/svg+xml;charset=utf-8,%3Csvg%20xmlns%3D'http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg'%20viewBox%3D'0%200%2027%2044'%3E%3Cpath%20d%3D'M0%2C22L22%2C0l2.1%2C2.1L4.2%2C22l19.9%2C19.9L22%2C44L0%2C22L0%2C22L0%2C22z'%20fill%3D'%23000000'%2F%3E%3C%2Fsvg%3E")
        }

        .swiper-button-prev.swiper-button-white, .swiper-container-rtl .swiper-button-next.swiper-button-white {
            background-image: url("data:image/svg+xml;charset=utf-8,%3Csvg%20xmlns%3D'http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg'%20viewBox%3D'0%200%2027%2044'%3E%3Cpath%20d%3D'M0%2C22L22%2C0l2.1%2C2.1L4.2%2C22l19.9%2C19.9L22%2C44L0%2C22L0%2C22L0%2C22z'%20fill%3D'%23ffffff'%2F%3E%3C%2Fsvg%3E")
        }

        .swiper-button-next, .swiper-container-rtl .swiper-button-prev {
            background-image: url("data:image/svg+xml;charset=utf-8,%3Csvg%20xmlns%3D'http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg'%20viewBox%3D'0%200%2027%2044'%3E%3Cpath%20d%3D'M27%2C22L27%2C22L5%2C44l-2.1-2.1L22.8%2C22L2.9%2C2.1L5%2C0L27%2C22L27%2C22z'%20fill%3D'%23007aff'%2F%3E%3C%2Fsvg%3E");
            right: 10px;
            left: auto
        }

        .swiper-button-next.swiper-button-black, .swiper-container-rtl .swiper-button-prev.swiper-button-black {
            background-image: url("data:image/svg+xml;charset=utf-8,%3Csvg%20xmlns%3D'http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg'%20viewBox%3D'0%200%2027%2044'%3E%3Cpath%20d%3D'M27%2C22L27%2C22L5%2C44l-2.1-2.1L22.8%2C22L2.9%2C2.1L5%2C0L27%2C22L27%2C22z'%20fill%3D'%23000000'%2F%3E%3C%2Fsvg%3E")
        }

        .swiper-button-next.swiper-button-white, .swiper-container-rtl .swiper-button-prev.swiper-button-white {
            background-image: url("data:image/svg+xml;charset=utf-8,%3Csvg%20xmlns%3D'http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg'%20viewBox%3D'0%200%2027%2044'%3E%3Cpath%20d%3D'M27%2C22L27%2C22L5%2C44l-2.1-2.1L22.8%2C22L2.9%2C2.1L5%2C0L27%2C22L27%2C22z'%20fill%3D'%23ffffff'%2F%3E%3C%2Fsvg%3E")
        }

        .swiper-pagination {
            position: absolute;
            text-align: center;
            -webkit-transition: .3s;
            -moz-transition: .3s;
            -o-transition: .3s;
            transition: .3s;
            -webkit-transform: translate3d(0, 0, 0);
            -ms-transform: translate3d(0, 0, 0);
            -o-transform: translate3d(0, 0, 0);
            transform: translate3d(0, 0, 0);
            z-index: 10
        }

        .swiper-pagination.swiper-pagination-hidden {
            opacity: 0
        }

        .swiper-container-horizontal > .swiper-pagination-bullets, .swiper-pagination-custom, .swiper-pagination-fraction {
            bottom: 10px;
            left: 0;
            width: 100%
        }

        .swiper-pagination-bullet {
            width: 8px;
            height: 8px;
            display: inline-block;
            border-radius: 100%;
            background: #000;
            opacity: .2
        }

        button.swiper-pagination-bullet {
            border: none;
            margin: 0;
            padding: 0;
            box-shadow: none;
            -moz-appearance: none;
            -ms-appearance: none;
            -webkit-appearance: none;
            appearance: none
        }

        .swiper-pagination-clickable .swiper-pagination-bullet {
            cursor: pointer
        }

        .swiper-pagination-white .swiper-pagination-bullet {
            background: #fff
        }

        .swiper-pagination-bullet-active {
            opacity: 1;
            background: #007aff
        }

        .swiper-pagination-white .swiper-pagination-bullet-active {
            background: #fff
        }

        .swiper-pagination-black .swiper-pagination-bullet-active {
            background: #000
        }

        .swiper-container-vertical > .swiper-pagination-bullets {
            right: 10px;
            top: 50%;
            -webkit-transform: translate3d(0, -50%, 0);
            -moz-transform: translate3d(0, -50%, 0);
            -o-transform: translate(0, -50%);
            -ms-transform: translate3d(0, -50%, 0);
            transform: translate3d(0, -50%, 0)
        }

        .swiper-container-vertical > .swiper-pagination-bullets .swiper-pagination-bullet {
            margin: 5px 0;
            display: block
        }

        .swiper-container-horizontal > .swiper-pagination-bullets .swiper-pagination-bullet {
            margin: 0 5px
        }

        .swiper-pagination-progress {
            background: rgba(0, 0, 0, .25);
            position: absolute
        }

        .swiper-pagination-progress .swiper-pagination-progressbar {
            background: #007aff;
            position: absolute;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            -webkit-transform: scale(0);
            -ms-transform: scale(0);
            -o-transform: scale(0);
            transform: scale(0);
            -webkit-transform-origin: left top;
            -moz-transform-origin: left top;
            -ms-transform-origin: left top;
            -o-transform-origin: left top;
            transform-origin: left top
        }

        .swiper-container-rtl .swiper-pagination-progress .swiper-pagination-progressbar {
            -webkit-transform-origin: right top;
            -moz-transform-origin: right top;
            -ms-transform-origin: right top;
            -o-transform-origin: right top;
            transform-origin: right top
        }

        .swiper-container-horizontal > .swiper-pagination-progress {
            width: 100%;
            height: 4px;
            left: 0;
            top: 0
        }

        .swiper-container-vertical > .swiper-pagination-progress {
            width: 4px;
            height: 100%;
            left: 0;
            top: 0
        }

        .swiper-pagination-progress.swiper-pagination-white {
            background: rgba(255, 255, 255, .5)
        }

        .swiper-pagination-progress.swiper-pagination-white .swiper-pagination-progressbar {
            background: #fff
        }

        .swiper-pagination-progress.swiper-pagination-black .swiper-pagination-progressbar {
            background: #000
        }

        .swiper-container-3d {
            -webkit-perspective: 1200px;
            -moz-perspective: 1200px;
            -o-perspective: 1200px;
            perspective: 1200px
        }

        .swiper-container-3d .swiper-cube-shadow, .swiper-container-3d .swiper-slide, .swiper-container-3d .swiper-slide-shadow-bottom, .swiper-container-3d .swiper-slide-shadow-left, .swiper-container-3d .swiper-slide-shadow-right, .swiper-container-3d .swiper-slide-shadow-top, .swiper-container-3d .swiper-wrapper {
            -webkit-transform-style: preserve-3d;
            -moz-transform-style: preserve-3d;
            -ms-transform-style: preserve-3d;
            transform-style: preserve-3d
        }

        .swiper-container-3d .swiper-slide-shadow-bottom, .swiper-container-3d .swiper-slide-shadow-left, .swiper-container-3d .swiper-slide-shadow-right, .swiper-container-3d .swiper-slide-shadow-top {
            position: absolute;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            pointer-events: none;
            z-index: 10
        }

        .swiper-container-3d .swiper-slide-shadow-left {
            background-image: -webkit-gradient(linear, left top, right top, from(rgba(0, 0, 0, .5)), to(rgba(0, 0, 0, 0)));
            background-image: -webkit-linear-gradient(right, rgba(0, 0, 0, .5), rgba(0, 0, 0, 0));
            background-image: -moz-linear-gradient(right, rgba(0, 0, 0, .5), rgba(0, 0, 0, 0));
            background-image: -o-linear-gradient(right, rgba(0, 0, 0, .5), rgba(0, 0, 0, 0));
            background-image: linear-gradient(to left, rgba(0, 0, 0, .5), rgba(0, 0, 0, 0))
        }

        .swiper-container-3d .swiper-slide-shadow-right {
            background-image: -webkit-gradient(linear, right top, left top, from(rgba(0, 0, 0, .5)), to(rgba(0, 0, 0, 0)));
            background-image: -webkit-linear-gradient(left, rgba(0, 0, 0, .5), rgba(0, 0, 0, 0));
            background-image: -moz-linear-gradient(left, rgba(0, 0, 0, .5), rgba(0, 0, 0, 0));
            background-image: -o-linear-gradient(left, rgba(0, 0, 0, .5), rgba(0, 0, 0, 0));
            background-image: linear-gradient(to right, rgba(0, 0, 0, .5), rgba(0, 0, 0, 0))
        }

        .swiper-container-3d .swiper-slide-shadow-top {
            background-image: -webkit-gradient(linear, left top, left bottom, from(rgba(0, 0, 0, .5)), to(rgba(0, 0, 0, 0)));
            background-image: -webkit-linear-gradient(bottom, rgba(0, 0, 0, .5), rgba(0, 0, 0, 0));
            background-image: -moz-linear-gradient(bottom, rgba(0, 0, 0, .5), rgba(0, 0, 0, 0));
            background-image: -o-linear-gradient(bottom, rgba(0, 0, 0, .5), rgba(0, 0, 0, 0));
            background-image: linear-gradient(to top, rgba(0, 0, 0, .5), rgba(0, 0, 0, 0))
        }

        .swiper-container-3d .swiper-slide-shadow-bottom {
            background-image: -webkit-gradient(linear, left bottom, left top, from(rgba(0, 0, 0, .5)), to(rgba(0, 0, 0, 0)));
            background-image: -webkit-linear-gradient(top, rgba(0, 0, 0, .5), rgba(0, 0, 0, 0));
            background-image: -moz-linear-gradient(top, rgba(0, 0, 0, .5), rgba(0, 0, 0, 0));
            background-image: -o-linear-gradient(top, rgba(0, 0, 0, .5), rgba(0, 0, 0, 0));
            background-image: linear-gradient(to bottom, rgba(0, 0, 0, .5), rgba(0, 0, 0, 0))
        }

        .swiper-container-coverflow .swiper-wrapper, .swiper-container-flip .swiper-wrapper {
            -ms-perspective: 1200px
        }

        .swiper-container-cube, .swiper-container-flip {
            overflow: visible
        }

        .swiper-container-cube .swiper-slide, .swiper-container-flip .swiper-slide {
            pointer-events: none;
            -webkit-backface-visibility: hidden;
            -moz-backface-visibility: hidden;
            -ms-backface-visibility: hidden;
            backface-visibility: hidden;
            z-index: 1
        }

        .swiper-container-cube .swiper-slide .swiper-slide, .swiper-container-flip .swiper-slide .swiper-slide {
            pointer-events: none
        }

        .swiper-container-cube .swiper-slide-active, .swiper-container-cube .swiper-slide-active .swiper-slide-active, .swiper-container-flip .swiper-slide-active, .swiper-container-flip .swiper-slide-active .swiper-slide-active {
            pointer-events: auto
        }

        .swiper-container-cube .swiper-slide-shadow-bottom, .swiper-container-cube .swiper-slide-shadow-left, .swiper-container-cube .swiper-slide-shadow-right, .swiper-container-cube .swiper-slide-shadow-top, .swiper-container-flip .swiper-slide-shadow-bottom, .swiper-container-flip .swiper-slide-shadow-left, .swiper-container-flip .swiper-slide-shadow-right, .swiper-container-flip .swiper-slide-shadow-top {
            z-index: 0;
            -webkit-backface-visibility: hidden;
            -moz-backface-visibility: hidden;
            -ms-backface-visibility: hidden;
            backface-visibility: hidden
        }

        .swiper-container-cube .swiper-slide {
            visibility: hidden;
            -webkit-transform-origin: 0 0;
            -moz-transform-origin: 0 0;
            -ms-transform-origin: 0 0;
            transform-origin: 0 0;
            width: 100%;
            height: 100%
        }

        .swiper-container-cube.swiper-container-rtl .swiper-slide {
            -webkit-transform-origin: 100% 0;
            -moz-transform-origin: 100% 0;
            -ms-transform-origin: 100% 0;
            transform-origin: 100% 0
        }

        .swiper-container-cube .swiper-slide-active, .swiper-container-cube .swiper-slide-next, .swiper-container-cube .swiper-slide-next + .swiper-slide, .swiper-container-cube .swiper-slide-prev {
            pointer-events: auto;
            visibility: visible
        }

        .swiper-container-cube .swiper-cube-shadow {
            position: absolute;
            left: 0;
            bottom: 0;
            width: 100%;
            height: 100%;
            background: #000;
            opacity: .6;
            -webkit-filter: blur(50px);
            filter: blur(50px);
            z-index: 0
        }

        .swiper-container-fade.swiper-container-free-mode .swiper-slide {
            -webkit-transition-timing-function: ease-out;
            -moz-transition-timing-function: ease-out;
            -ms-transition-timing-function: ease-out;
            -o-transition-timing-function: ease-out;
            transition-timing-function: ease-out
        }

        .swiper-container-fade .swiper-slide {
            pointer-events: none;
            -webkit-transition-property: opacity;
            -moz-transition-property: opacity;
            -o-transition-property: opacity;
            transition-property: opacity
        }

        .swiper-container-fade .swiper-slide .swiper-slide {
            pointer-events: none
        }

        .swiper-container-fade .swiper-slide-active, .swiper-container-fade .swiper-slide-active .swiper-slide-active {
            pointer-events: auto
        }

        .swiper-scrollbar {
            border-radius: 10px;
            position: relative;
            -ms-touch-action: none;
            background: rgba(0, 0, 0, .1)
        }

        .swiper-container-horizontal > .swiper-scrollbar {
            position: absolute;
            left: 1%;
            bottom: 3px;
            z-index: 50;
            height: 5px;
            width: 98%
        }

        .swiper-container-vertical > .swiper-scrollbar {
            position: absolute;
            right: 3px;
            top: 1%;
            z-index: 50;
            width: 5px;
            height: 98%
        }

        .swiper-scrollbar-drag {
            height: 100%;
            width: 100%;
            position: relative;
            background: rgba(0, 0, 0, .5);
            border-radius: 10px;
            left: 0;
            top: 0
        }

        .swiper-scrollbar-cursor-drag {
            cursor: move
        }

        .swiper-lazy-preloader {
            width: 42px;
            height: 42px;
            position: absolute;
            left: 50%;
            top: 50%;
            margin-left: -21px;
            margin-top: -21px;
            z-index: 10;
            -webkit-transform-origin: 50%;
            -moz-transform-origin: 50%;
            transform-origin: 50%;
            -webkit-animation: swiper-preloader-spin 1s steps(12, end) infinite;
            -moz-animation: swiper-preloader-spin 1s steps(12, end) infinite;
            animation: swiper-preloader-spin 1s steps(12, end) infinite
        }

        .swiper-lazy-preloader:after {
            display: block;
            content: "";
            width: 100%;
            height: 100%;
            background-image: url("data:image/svg+xml;charset=utf-8,%3Csvg%20viewBox%3D'0%200%20120%20120'%20xmlns%3D'http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg'%20xmlns%3Axlink%3D'http%3A%2F%2Fwww.w3.org%2F1999%2Fxlink'%3E%3Cdefs%3E%3Cline%20id%3D'l'%20x1%3D'60'%20x2%3D'60'%20y1%3D'7'%20y2%3D'27'%20stroke%3D'%236c6c6c'%20stroke-width%3D'11'%20stroke-linecap%3D'round'%2F%3E%3C%2Fdefs%3E%3Cg%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%20transform%3D'rotate(30%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%20transform%3D'rotate(60%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%20transform%3D'rotate(90%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%20transform%3D'rotate(120%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%20transform%3D'rotate(150%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.37'%20transform%3D'rotate(180%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.46'%20transform%3D'rotate(210%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.56'%20transform%3D'rotate(240%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.66'%20transform%3D'rotate(270%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.75'%20transform%3D'rotate(300%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.85'%20transform%3D'rotate(330%2060%2C60)'%2F%3E%3C%2Fg%3E%3C%2Fsvg%3E");
            background-position: 50%;
            -webkit-background-size: 100%;
            background-size: 100%;
            background-repeat: no-repeat
        }

        .swiper-lazy-preloader-white:after {
            background-image: url("data:image/svg+xml;charset=utf-8,%3Csvg%20viewBox%3D'0%200%20120%20120'%20xmlns%3D'http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg'%20xmlns%3Axlink%3D'http%3A%2F%2Fwww.w3.org%2F1999%2Fxlink'%3E%3Cdefs%3E%3Cline%20id%3D'l'%20x1%3D'60'%20x2%3D'60'%20y1%3D'7'%20y2%3D'27'%20stroke%3D'%23fff'%20stroke-width%3D'11'%20stroke-linecap%3D'round'%2F%3E%3C%2Fdefs%3E%3Cg%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%20transform%3D'rotate(30%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%20transform%3D'rotate(60%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%20transform%3D'rotate(90%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%20transform%3D'rotate(120%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.27'%20transform%3D'rotate(150%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.37'%20transform%3D'rotate(180%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.46'%20transform%3D'rotate(210%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.56'%20transform%3D'rotate(240%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.66'%20transform%3D'rotate(270%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.75'%20transform%3D'rotate(300%2060%2C60)'%2F%3E%3Cuse%20xlink%3Ahref%3D'%23l'%20opacity%3D'.85'%20transform%3D'rotate(330%2060%2C60)'%2F%3E%3C%2Fg%3E%3C%2Fsvg%3E")
        }

        @-webkit-keyframes swiper-preloader-spin {
            100% {
                -webkit-transform: rotate(360deg)
            }
        }

        @keyframes swiper-preloader-spin {
            100% {
                transform: rotate(360deg)
            }
        }

        body {
            font-size: 12px;
        }

        ul li {
            list-style: none;
        }

        .track-rcol {
            width: 100%;
            border: 1px solid #eee;
        }

        .track-list {
            margin: 20px;
            padding-left: 5px;
            position: relative;
        }

        .track-list li {
            position: relative;
            padding: 9px 0 0 25px;
            line-height: 18px;
            border-left: 1px solid #d9d9d9;
            color: #999;
        }

        .track-list li.first {
            color: red;
            padding-top: 0;
            border-left-color: #fff;
        }

        .track-list li .node-icon {
            position: absolute;
            left: -6px;
            top: 50%;
            width: 11px;
            height: 11px;
            background: url('http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/order-icons.png') -21px -72px no-repeat;
        }

        .track-list li.first .node-icon {
            background-position: 0 -72px;
        }

        .track-list li .time {
            margin-right: 20px;
            position: relative;
            top: 4px;
            display: inline-block;
            vertical-align: middle;
        }

        .track-list li .txt {
            max-width: 600px;
            position: relative;
            top: 4px;
            display: inline-block;
            vertical-align: middle;
        }

        .track-list li.first .time {
            margin-right: 20px;
        }

        .track-list li.first .txt {
            max-width: 600px;
        }

        a {
            color: #428bca;
            text-decoration: none;
        }

        a:hover, a:focus {
            color: #2a6496;
            text-decoration: underline;
        }

        a:focus {
            outline: thin dotted;
            outline: 5px auto -webkit-focus-ring-color;
            outline-offset: -2px;
        }

        .padd_40 {
            padding-top: 80px;
            background: #F5F5F5;
            overflow-x: hidden;
        }

        .a {
            text-align: center;
            line-height: 40px;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            z-index: 10;
            border-bottom: 1px #ccc solid;
            background: #f50;
            color: #fff;
        }

        .tab {
            display: flex;
            line-height: 40px;
            position: fixed;
            top: 40px;
            width: 100%;
            z-index: 10;
            border-bottom: 1px #ccc solid;
        }

        .tab a {
            width: 33.333333%;
            background: #fff;
            text-align: center;
        }

        .tab .active {
            border-bottom: 1px #f50 solid;
            color: #f50;
        }

        .panel {
            margin: 0;
        }

        .refreshtip {
            position: absolute;
            left: 0;
            width: 100%;
            margin: 10px 0;
            text-align: center;
            color: #999;
        }

        .swiper-container {
            overflow: visible;
        }

        .loadtip {
            display: block;
            width: 100%;
            line-height: 40px;
            height: 40px;
            text-align: center;
            color: #999;
            border-top: 1px solid #ddd;
        }

        .swiper-container, .w {
            height: calc(100vh - 120px);
        }

        .swiper-slide {
            height: auto;
        }

        .text-center {
            text-align: center;
        }

        .list-group {
            padding-left: 0;
            margin-bottom: 20px;
        }

        .list-group-item {
            position: relative;
            display: block;
            padding: 10px 15px;
            margin-bottom: -1px;
            background-color: #fff;
            border: 1px solid #ddd;
        }

        .list-group-item:first-child {
            border-top-left-radius: 4px;
            border-top-right-radius: 4px;
        }
    </style>
    <script>
        /**
         * Swiper 3.4.0
         * Most modern mobile touch slider and framework with hardware accelerated transitions
         *
         * http://www.idangero.us/swiper/
         *
         * Copyright 2016, Vladimir Kharlampidi
         * The iDangero.us
         * http://www.idangero.us/
         *
         * Licensed under MIT
         *
         * Released on: October 16, 2016
         */
        !function () {
            "use strict";
            function e(e) {
                e.fn.swiper = function (a) {
                    var s;
                    return e(this).each(function () {
                        var e = new t(this, a);
                        s || (s = e)
                    }), s
                }
            }

            var a, t = function (e, s) {
                function i(e) {
                    return Math.floor(e)
                }

                function n() {
                    var e = b.params.autoplay, a = b.slides.eq(b.activeIndex);
                    a.attr("data-swiper-autoplay") && (e = a.attr("data-swiper-autoplay") || b.params.autoplay), b.autoplayTimeoutId = setTimeout(function () {
                        b.params.loop ? (b.fixLoop(), b._slideNext(), b.emit("onAutoplay", b)) : b.isEnd ? s.autoplayStopOnLast ? b.stopAutoplay() : (b._slideTo(0), b.emit("onAutoplay", b)) : (b._slideNext(), b.emit("onAutoplay", b))
                    }, e)
                }

                function o(e, t) {
                    var s = a(e.target);
                    if (!s.is(t))if ("string" == typeof t)s = s.parents(t); else if (t.nodeType) {
                        var r;
                        return s.parents().each(function (e, a) {
                            a === t && (r = t)
                        }), r ? t : void 0
                    }
                    if (0 !== s.length)return s[0]
                }

                function l(e, a) {
                    a = a || {};
                    var t = window.MutationObserver || window.WebkitMutationObserver, s = new t(function (e) {
                        e.forEach(function (e) {
                            b.onResize(!0), b.emit("onObserverUpdate", b, e)
                        })
                    });
                    s.observe(e, {
                        attributes: "undefined" == typeof a.attributes || a.attributes,
                        childList: "undefined" == typeof a.childList || a.childList,
                        characterData: "undefined" == typeof a.characterData || a.characterData
                    }), b.observers.push(s)
                }

                function p(e) {
                    e.originalEvent && (e = e.originalEvent);
                    var a = e.keyCode || e.charCode;
                    if (!b.params.allowSwipeToNext && (b.isHorizontal() && 39 === a || !b.isHorizontal() && 40 === a))return !1;
                    if (!b.params.allowSwipeToPrev && (b.isHorizontal() && 37 === a || !b.isHorizontal() && 38 === a))return !1;
                    if (!(e.shiftKey || e.altKey || e.ctrlKey || e.metaKey || document.activeElement && document.activeElement.nodeName && ("input" === document.activeElement.nodeName.toLowerCase() || "textarea" === document.activeElement.nodeName.toLowerCase()))) {
                        if (37 === a || 39 === a || 38 === a || 40 === a) {
                            var t = !1;
                            if (b.container.parents("." + b.params.slideClass).length > 0 && 0 === b.container.parents("." + b.params.slideActiveClass).length)return;
                            var s = {
                                left: window.pageXOffset,
                                top: window.pageYOffset
                            }, r = window.innerWidth, i = window.innerHeight, n = b.container.offset();
                            b.rtl && (n.left = n.left - b.container[0].scrollLeft);
                            for (var o = [[n.left, n.top], [n.left + b.width, n.top], [n.left, n.top + b.height], [n.left + b.width, n.top + b.height]], l = 0; l < o.length; l++) {
                                var p = o[l];
                                p[0] >= s.left && p[0] <= s.left + r && p[1] >= s.top && p[1] <= s.top + i && (t = !0)
                            }
                            if (!t)return
                        }
                        b.isHorizontal() ? (37 !== a && 39 !== a || (e.preventDefault ? e.preventDefault() : e.returnValue = !1), (39 === a && !b.rtl || 37 === a && b.rtl) && b.slideNext(), (37 === a && !b.rtl || 39 === a && b.rtl) && b.slidePrev()) : (38 !== a && 40 !== a || (e.preventDefault ? e.preventDefault() : e.returnValue = !1), 40 === a && b.slideNext(), 38 === a && b.slidePrev())
                    }
                }

                function d() {
                    var e = "onwheel", a = e in document;
                    if (!a) {
                        var t = document.createElement("div");
                        t.setAttribute(e, "return;"), a = "function" == typeof t[e]
                    }
                    return !a && document.implementation && document.implementation.hasFeature && document.implementation.hasFeature("", "") !== !0 && (a = document.implementation.hasFeature("Events.wheel", "3.0")), a
                }

                function u(e) {
                    e.originalEvent && (e = e.originalEvent);
                    var a = 0, t = b.rtl ? -1 : 1, s = m(e);
                    if (b.params.mousewheelForceToAxis)if (b.isHorizontal()) {
                        if (!(Math.abs(s.pixelX) > Math.abs(s.pixelY)))return;
                        a = s.pixelX * t
                    } else {
                        if (!(Math.abs(s.pixelY) > Math.abs(s.pixelX)))return;
                        a = s.pixelY
                    } else a = Math.abs(s.pixelX) > Math.abs(s.pixelY) ? -s.pixelX * t : -s.pixelY;
                    if (0 !== a) {
                        if (b.params.mousewheelInvert && (a = -a), b.params.freeMode) {
                            var r = b.getWrapperTranslate() + a * b.params.mousewheelSensitivity, i = b.isBeginning, n = b.isEnd;
                            if (r >= b.minTranslate() && (r = b.minTranslate()), r <= b.maxTranslate() && (r = b.maxTranslate()), b.setWrapperTransition(0), b.setWrapperTranslate(r), b.updateProgress(), b.updateActiveIndex(), (!i && b.isBeginning || !n && b.isEnd) && b.updateClasses(), b.params.freeModeSticky ? (clearTimeout(b.mousewheel.timeout), b.mousewheel.timeout = setTimeout(function () {
                                        b.slideReset()
                                    }, 300)) : b.params.lazyLoading && b.lazy && b.lazy.load(), b.emit("onScroll", b, e), b.params.autoplay && b.params.autoplayDisableOnInteraction && b.stopAutoplay(), 0 === r || r === b.maxTranslate())return
                        } else {
                            if ((new window.Date).getTime() - b.mousewheel.lastScrollTime > 60)if (a < 0)if (b.isEnd && !b.params.loop || b.animating) {
                                if (b.params.mousewheelReleaseOnEdges)return !0
                            } else b.slideNext(), b.emit("onScroll", b, e); else if (b.isBeginning && !b.params.loop || b.animating) {
                                if (b.params.mousewheelReleaseOnEdges)return !0
                            } else b.slidePrev(), b.emit("onScroll", b, e);
                            b.mousewheel.lastScrollTime = (new window.Date).getTime()
                        }
                        return e.preventDefault ? e.preventDefault() : e.returnValue = !1, !1
                    }
                }

                function m(e) {
                    var a = 10, t = 40, s = 800, r = 0, i = 0, n = 0, o = 0;
                    return "detail"in e && (i = e.detail), "wheelDelta"in e && (i = -e.wheelDelta / 120), "wheelDeltaY"in e && (i = -e.wheelDeltaY / 120), "wheelDeltaX"in e && (r = -e.wheelDeltaX / 120), "axis"in e && e.axis === e.HORIZONTAL_AXIS && (r = i, i = 0), n = r * a, o = i * a, "deltaY"in e && (o = e.deltaY), "deltaX"in e && (n = e.deltaX), (n || o) && e.deltaMode && (1 === e.deltaMode ? (n *= t, o *= t) : (n *= s, o *= s)), n && !r && (r = n < 1 ? -1 : 1), o && !i && (i = o < 1 ? -1 : 1), {
                        spinX: r,
                        spinY: i,
                        pixelX: n,
                        pixelY: o
                    }
                }

                function c(e, t) {
                    e = a(e);
                    var s, r, i, n = b.rtl ? -1 : 1;
                    s = e.attr("data-swiper-parallax") || "0", r = e.attr("data-swiper-parallax-x"), i = e.attr("data-swiper-parallax-y"), r || i ? (r = r || "0", i = i || "0") : b.isHorizontal() ? (r = s, i = "0") : (i = s, r = "0"), r = r.indexOf("%") >= 0 ? parseInt(r, 10) * t * n + "%" : r * t * n + "px", i = i.indexOf("%") >= 0 ? parseInt(i, 10) * t + "%" : i * t + "px", e.transform("translate3d(" + r + ", " + i + ",0px)")
                }

                function g(e) {
                    return 0 !== e.indexOf("on") && (e = e[0] !== e[0].toUpperCase() ? "on" + e[0].toUpperCase() + e.substring(1) : "on" + e), e
                }

                if (!(this instanceof t))return new t(e, s);
                var h = {
                    direction: "horizontal",
                    touchEventsTarget: "container",
                    initialSlide: 0,
                    speed: 300,
                    autoplay: !1,
                    autoplayDisableOnInteraction: !0,
                    autoplayStopOnLast: !1,
                    iOSEdgeSwipeDetection: !1,
                    iOSEdgeSwipeThreshold: 20,
                    freeMode: !1,
                    freeModeMomentum: !0,
                    freeModeMomentumRatio: 1,
                    freeModeMomentumBounce: !0,
                    freeModeMomentumBounceRatio: 1,
                    freeModeMomentumVelocityRatio: 1,
                    freeModeSticky: !1,
                    freeModeMinimumVelocity: .02,
                    autoHeight: !1,
                    setWrapperSize: !1,
                    virtualTranslate: !1,
                    effect: "slide",
                    coverflow: {rotate: 50, stretch: 0, depth: 100, modifier: 1, slideShadows: !0},
                    flip: {slideShadows: !0, limitRotation: !0},
                    cube: {slideShadows: !0, shadow: !0, shadowOffset: 20, shadowScale: .94},
                    fade: {crossFade: !1},
                    parallax: !1,
                    zoom: !1,
                    zoomMax: 3,
                    zoomMin: 1,
                    zoomToggle: !0,
                    scrollbar: null,
                    scrollbarHide: !0,
                    scrollbarDraggable: !1,
                    scrollbarSnapOnRelease: !1,
                    keyboardControl: !1,
                    mousewheelControl: !1,
                    mousewheelReleaseOnEdges: !1,
                    mousewheelInvert: !1,
                    mousewheelForceToAxis: !1,
                    mousewheelSensitivity: 1,
                    mousewheelEventsTarged: "container",
                    hashnav: !1,
                    hashnavWatchState: !1,
                    history: !1,
                    replaceState: !1,
                    breakpoints: void 0,
                    spaceBetween: 0,
                    slidesPerView: 1,
                    slidesPerColumn: 1,
                    slidesPerColumnFill: "column",
                    slidesPerGroup: 1,
                    centeredSlides: !1,
                    slidesOffsetBefore: 0,
                    slidesOffsetAfter: 0,
                    roundLengths: !1,
                    touchRatio: 1,
                    touchAngle: 45,
                    simulateTouch: !0,
                    shortSwipes: !0,
                    longSwipes: !0,
                    longSwipesRatio: .5,
                    longSwipesMs: 300,
                    followFinger: !0,
                    onlyExternal: !1,
                    threshold: 0,
                    touchMoveStopPropagation: !0,
                    touchReleaseOnEdges: !1,
                    uniqueNavElements: !0,
                    pagination: null,
                    paginationElement: "span",
                    paginationClickable: !1,
                    paginationHide: !1,
                    paginationBulletRender: null,
                    paginationProgressRender: null,
                    paginationFractionRender: null,
                    paginationCustomRender: null,
                    paginationType: "bullets",
                    resistance: !0,
                    resistanceRatio: .85,
                    nextButton: null,
                    prevButton: null,
                    watchSlidesProgress: !1,
                    watchSlidesVisibility: !1,
                    grabCursor: !1,
                    preventClicks: !0,
                    preventClicksPropagation: !0,
                    slideToClickedSlide: !1,
                    lazyLoading: !1,
                    lazyLoadingInPrevNext: !1,
                    lazyLoadingInPrevNextAmount: 1,
                    lazyLoadingOnTransitionStart: !1,
                    preloadImages: !0,
                    updateOnImagesReady: !0,
                    loop: !1,
                    loopAdditionalSlides: 0,
                    loopedSlides: null,
                    control: void 0,
                    controlInverse: !1,
                    controlBy: "slide",
                    normalizeSlideIndex: !0,
                    allowSwipeToPrev: !0,
                    allowSwipeToNext: !0,
                    swipeHandler: null,
                    noSwiping: !0,
                    noSwipingClass: "swiper-no-swiping",
                    passiveListeners: !0,
                    containerModifierClass: "swiper-container-",
                    slideClass: "swiper-slide",
                    slideActiveClass: "swiper-slide-active",
                    slideDuplicateActiveClass: "swiper-slide-duplicate-active",
                    slideVisibleClass: "swiper-slide-visible",
                    slideDuplicateClass: "swiper-slide-duplicate",
                    slideNextClass: "swiper-slide-next",
                    slideDuplicateNextClass: "swiper-slide-duplicate-next",
                    slidePrevClass: "swiper-slide-prev",
                    slideDuplicatePrevClass: "swiper-slide-duplicate-prev",
                    wrapperClass: "swiper-wrapper",
                    bulletClass: "swiper-pagination-bullet",
                    bulletActiveClass: "swiper-pagination-bullet-active",
                    buttonDisabledClass: "swiper-button-disabled",
                    paginationCurrentClass: "swiper-pagination-current",
                    paginationTotalClass: "swiper-pagination-total",
                    paginationHiddenClass: "swiper-pagination-hidden",
                    paginationProgressbarClass: "swiper-pagination-progressbar",
                    paginationClickableClass: "swiper-pagination-clickable",
                    paginationModifierClass: "swiper-pagination-",
                    lazyLoadingClass: "swiper-lazy",
                    lazyStatusLoadingClass: "swiper-lazy-loading",
                    lazyStatusLoadedClass: "swiper-lazy-loaded",
                    lazyPreloaderClass: "swiper-lazy-preloader",
                    notificationClass: "swiper-notification",
                    preloaderClass: "preloader",
                    zoomContainerClass: "swiper-zoom-container",
                    observer: !1,
                    observeParents: !1,
                    a11y: !1,
                    prevSlideMessage: "Previous slide",
                    nextSlideMessage: "Next slide",
                    firstSlideMessage: "This is the first slide",
                    lastSlideMessage: "This is the last slide",
                    paginationBulletMessage: "Go to slide {{index}}",
                    runCallbacksOnInit: !0
                }, f = s && s.virtualTranslate;
                s = s || {};
                var v = {};
                for (var w in s)if ("object" != typeof s[w] || null === s[w] || (s[w].nodeType || s[w] === window || s[w] === document || "undefined" != typeof Dom7 && s[w]instanceof Dom7 || "undefined" != typeof jQuery && s[w]instanceof jQuery))v[w] = s[w]; else {
                    v[w] = {};
                    for (var y in s[w])v[w][y] = s[w][y]
                }
                for (var x in h)if ("undefined" == typeof s[x])s[x] = h[x]; else if ("object" == typeof s[x])for (var T in h[x])"undefined" == typeof s[x][T] && (s[x][T] = h[x][T]);
                var b = this;
                if (b.params = s, b.originalParams = v, b.classNames = [], "undefined" != typeof a && "undefined" != typeof Dom7 && (a = Dom7), ("undefined" != typeof a || (a = "undefined" == typeof Dom7 ? window.Dom7 || window.Zepto || window.jQuery : Dom7)) && (b.$ = a, b.currentBreakpoint = void 0, b.getActiveBreakpoint = function () {
                            if (!b.params.breakpoints)return !1;
                            var e, a = !1, t = [];
                            for (e in b.params.breakpoints)b.params.breakpoints.hasOwnProperty(e) && t.push(e);
                            t.sort(function (e, a) {
                                return parseInt(e, 10) > parseInt(a, 10)
                            });
                            for (var s = 0; s < t.length; s++)e = t[s], e >= window.innerWidth && !a && (a = e);
                            return a || "max"
                        }, b.setBreakpoint = function () {
                            var e = b.getActiveBreakpoint();
                            if (e && b.currentBreakpoint !== e) {
                                var a = e in b.params.breakpoints ? b.params.breakpoints[e] : b.originalParams, t = b.params.loop && a.slidesPerView !== b.params.slidesPerView;
                                for (var s in a)b.params[s] = a[s];
                                b.currentBreakpoint = e, t && b.destroyLoop && b.reLoop(!0)
                            }
                        }, b.params.breakpoints && b.setBreakpoint(), b.container = a(e), 0 !== b.container.length)) {
                    if (b.container.length > 1) {
                        var C = [];
                        return b.container.each(function () {
                            C.push(new t(this, s))
                        }), C
                    }
                    b.container[0].swiper = b, b.container.data("swiper", b), b.classNames.push(b.params.containerModifierClass + b.params.direction), b.params.freeMode && b.classNames.push(b.params.containerModifierClass + "free-mode"), b.support.flexbox || (b.classNames.push(b.params.containerModifierClass + "no-flexbox"), b.params.slidesPerColumn = 1), b.params.autoHeight && b.classNames.push(b.params.containerModifierClass + "autoheight"), (b.params.parallax || b.params.watchSlidesVisibility) && (b.params.watchSlidesProgress = !0), b.params.touchReleaseOnEdges && (b.params.resistanceRatio = 0), ["cube", "coverflow", "flip"].indexOf(b.params.effect) >= 0 && (b.support.transforms3d ? (b.params.watchSlidesProgress = !0, b.classNames.push(b.params.containerModifierClass + "3d")) : b.params.effect = "slide"), "slide" !== b.params.effect && b.classNames.push(b.params.containerModifierClass + b.params.effect), "cube" === b.params.effect && (b.params.resistanceRatio = 0, b.params.slidesPerView = 1, b.params.slidesPerColumn = 1, b.params.slidesPerGroup = 1, b.params.centeredSlides = !1, b.params.spaceBetween = 0, b.params.virtualTranslate = !0, b.params.setWrapperSize = !1), "fade" !== b.params.effect && "flip" !== b.params.effect || (b.params.slidesPerView = 1, b.params.slidesPerColumn = 1, b.params.slidesPerGroup = 1, b.params.watchSlidesProgress = !0, b.params.spaceBetween = 0, b.params.setWrapperSize = !1, "undefined" == typeof f && (b.params.virtualTranslate = !0)), b.params.grabCursor && b.support.touch && (b.params.grabCursor = !1), b.wrapper = b.container.children("." + b.params.wrapperClass), b.params.pagination && (b.paginationContainer = a(b.params.pagination), b.params.uniqueNavElements && "string" == typeof b.params.pagination && b.paginationContainer.length > 1 && 1 === b.container.find(b.params.pagination).length && (b.paginationContainer = b.container.find(b.params.pagination)), "bullets" === b.params.paginationType && b.params.paginationClickable ? b.paginationContainer.addClass(b.params.paginationModifierClass + "clickable") : b.params.paginationClickable = !1, b.paginationContainer.addClass(b.params.paginationModifierClass + b.params.paginationType)), (b.params.nextButton || b.params.prevButton) && (b.params.nextButton && (b.nextButton = a(b.params.nextButton), b.params.uniqueNavElements && "string" == typeof b.params.nextButton && b.nextButton.length > 1 && 1 === b.container.find(b.params.nextButton).length && (b.nextButton = b.container.find(b.params.nextButton))), b.params.prevButton && (b.prevButton = a(b.params.prevButton), b.params.uniqueNavElements && "string" == typeof b.params.prevButton && b.prevButton.length > 1 && 1 === b.container.find(b.params.prevButton).length && (b.prevButton = b.container.find(b.params.prevButton)))), b.isHorizontal = function () {
                        return "horizontal" === b.params.direction
                    }, b.rtl = b.isHorizontal() && ("rtl" === b.container[0].dir.toLowerCase() || "rtl" === b.container.css("direction")), b.rtl && b.classNames.push(b.params.containerModifierClass + "rtl"), b.rtl && (b.wrongRTL = "-webkit-box" === b.wrapper.css("display")), b.params.slidesPerColumn > 1 && b.classNames.push(b.params.containerModifierClass + "multirow"), b.device.android && b.classNames.push(b.params.containerModifierClass + "android"), b.container.addClass(b.classNames.join(" ")), b.translate = 0, b.progress = 0, b.velocity = 0, b.lockSwipeToNext = function () {
                        b.params.allowSwipeToNext = !1, b.params.allowSwipeToPrev === !1 && b.params.grabCursor && b.unsetGrabCursor()
                    }, b.lockSwipeToPrev = function () {
                        b.params.allowSwipeToPrev = !1, b.params.allowSwipeToNext === !1 && b.params.grabCursor && b.unsetGrabCursor()
                    }, b.lockSwipes = function () {
                        b.params.allowSwipeToNext = b.params.allowSwipeToPrev = !1, b.params.grabCursor && b.unsetGrabCursor()
                    }, b.unlockSwipeToNext = function () {
                        b.params.allowSwipeToNext = !0, b.params.allowSwipeToPrev === !0 && b.params.grabCursor && b.setGrabCursor()
                    }, b.unlockSwipeToPrev = function () {
                        b.params.allowSwipeToPrev = !0, b.params.allowSwipeToNext === !0 && b.params.grabCursor && b.setGrabCursor()
                    }, b.unlockSwipes = function () {
                        b.params.allowSwipeToNext = b.params.allowSwipeToPrev = !0, b.params.grabCursor && b.setGrabCursor()
                    }, b.setGrabCursor = function (e) {
                        b.container[0].style.cursor = "move", b.container[0].style.cursor = e ? "-webkit-grabbing" : "-webkit-grab", b.container[0].style.cursor = e ? "-moz-grabbin" : "-moz-grab", b.container[0].style.cursor = e ? "grabbing" : "grab"
                    }, b.unsetGrabCursor = function () {
                        b.container[0].style.cursor = ""
                    }, b.params.grabCursor && b.setGrabCursor(), b.imagesToLoad = [], b.imagesLoaded = 0, b.loadImage = function (e, a, t, s, r, i) {
                        function n() {
                            i && i()
                        }

                        var o;
                        e.complete && r ? n() : a ? (o = new window.Image, o.onload = n, o.onerror = n, s && (o.sizes = s), t && (o.srcset = t), a && (o.src = a)) : n()
                    }, b.preloadImages = function () {
                        function e() {
                            "undefined" != typeof b && null !== b && (void 0 !== b.imagesLoaded && b.imagesLoaded++, b.imagesLoaded === b.imagesToLoad.length && (b.params.updateOnImagesReady && b.update(), b.emit("onImagesReady", b)))
                        }

                        b.imagesToLoad = b.container.find("img");
                        for (var a = 0; a < b.imagesToLoad.length; a++)b.loadImage(b.imagesToLoad[a], b.imagesToLoad[a].currentSrc || b.imagesToLoad[a].getAttribute("src"), b.imagesToLoad[a].srcset || b.imagesToLoad[a].getAttribute("srcset"), b.imagesToLoad[a].sizes || b.imagesToLoad[a].getAttribute("sizes"), !0, e)
                    }, b.autoplayTimeoutId = void 0, b.autoplaying = !1, b.autoplayPaused = !1, b.startAutoplay = function () {
                        return "undefined" == typeof b.autoplayTimeoutId && (!!b.params.autoplay && (!b.autoplaying && (b.autoplaying = !0, b.emit("onAutoplayStart", b), void n())))
                    }, b.stopAutoplay = function (e) {
                        b.autoplayTimeoutId && (b.autoplayTimeoutId && clearTimeout(b.autoplayTimeoutId), b.autoplaying = !1, b.autoplayTimeoutId = void 0, b.emit("onAutoplayStop", b))
                    }, b.pauseAutoplay = function (e) {
                        b.autoplayPaused || (b.autoplayTimeoutId && clearTimeout(b.autoplayTimeoutId), b.autoplayPaused = !0, 0 === e ? (b.autoplayPaused = !1, n()) : b.wrapper.transitionEnd(function () {
                            b && (b.autoplayPaused = !1, b.autoplaying ? n() : b.stopAutoplay())
                        }))
                    }, b.minTranslate = function () {
                        return -b.snapGrid[0]
                    }, b.maxTranslate = function () {
                        return -b.snapGrid[b.snapGrid.length - 1]
                    }, b.updateAutoHeight = function () {
                        var e = [], a = 0;
                        if ("auto" !== b.params.slidesPerView && b.params.slidesPerView > 1)for (r = 0; r < Math.ceil(b.params.slidesPerView); r++) {
                            var t = b.activeIndex + r;
                            if (t > b.slides.length)break;
                            e.push(b.slides.eq(t)[0])
                        } else e.push(b.slides.eq(b.activeIndex)[0]);
                        for (r = 0; r < e.length; r++)if ("undefined" != typeof e[r]) {
                            var s = e[r].offsetHeight;
                            a = s > a ? s : a
                        }
                        a && b.wrapper.css("height", a + "px")
                    }, b.updateContainerSize = function () {
                        var e, a;
                        e = "undefined" != typeof b.params.width ? b.params.width : b.container[0].clientWidth, a = "undefined" != typeof b.params.height ? b.params.height : b.container[0].clientHeight, 0 === e && b.isHorizontal() || 0 === a && !b.isHorizontal() || (e = e - parseInt(b.container.css("padding-left"), 10) - parseInt(b.container.css("padding-right"), 10), a = a - parseInt(b.container.css("padding-top"), 10) - parseInt(b.container.css("padding-bottom"), 10), b.width = e, b.height = a, b.size = b.isHorizontal() ? b.width : b.height)
                    }, b.updateSlidesSize = function () {
                        b.slides = b.wrapper.children("." + b.params.slideClass), b.snapGrid = [], b.slidesGrid = [], b.slidesSizesGrid = [];
                        var e, a = b.params.spaceBetween, t = -b.params.slidesOffsetBefore, s = 0, r = 0;
                        if ("undefined" != typeof b.size) {
                            "string" == typeof a && a.indexOf("%") >= 0 && (a = parseFloat(a.replace("%", "")) / 100 * b.size), b.virtualSize = -a, b.rtl ? b.slides.css({
                                marginLeft: "",
                                marginTop: ""
                            }) : b.slides.css({marginRight: "", marginBottom: ""});
                            var n;
                            b.params.slidesPerColumn > 1 && (n = Math.floor(b.slides.length / b.params.slidesPerColumn) === b.slides.length / b.params.slidesPerColumn ? b.slides.length : Math.ceil(b.slides.length / b.params.slidesPerColumn) * b.params.slidesPerColumn, "auto" !== b.params.slidesPerView && "row" === b.params.slidesPerColumnFill && (n = Math.max(n, b.params.slidesPerView * b.params.slidesPerColumn)));
                            var o, l = b.params.slidesPerColumn, p = n / l, d = p - (b.params.slidesPerColumn * p - b.slides.length);
                            for (e = 0; e < b.slides.length; e++) {
                                o = 0;
                                var u = b.slides.eq(e);
                                if (b.params.slidesPerColumn > 1) {
                                    var m, c, g;
                                    "column" === b.params.slidesPerColumnFill ? (c = Math.floor(e / l), g = e - c * l, (c > d || c === d && g === l - 1) && ++g >= l && (g = 0, c++), m = c + g * n / l, u.css({
                                        "-webkit-box-ordinal-group": m,
                                        "-moz-box-ordinal-group": m,
                                        "-ms-flex-order": m,
                                        "-webkit-order": m,
                                        order: m
                                    })) : (g = Math.floor(e / p), c = e - g * p), u.css("margin-" + (b.isHorizontal() ? "top" : "left"), 0 !== g && b.params.spaceBetween && b.params.spaceBetween + "px").attr("data-swiper-column", c).attr("data-swiper-row", g)
                                }
                                "none" !== u.css("display") && ("auto" === b.params.slidesPerView ? (o = b.isHorizontal() ? u.outerWidth(!0) : u.outerHeight(!0), b.params.roundLengths && (o = i(o))) : (o = (b.size - (b.params.slidesPerView - 1) * a) / b.params.slidesPerView, b.params.roundLengths && (o = i(o)), b.isHorizontal() ? b.slides[e].style.width = o + "px" : b.slides[e].style.height = o + "px"), b.slides[e].swiperSlideSize = o, b.slidesSizesGrid.push(o), b.params.centeredSlides ? (t = t + o / 2 + s / 2 + a, 0 === e && (t = t - b.size / 2 - a), Math.abs(t) < .001 && (t = 0), r % b.params.slidesPerGroup === 0 && b.snapGrid.push(t), b.slidesGrid.push(t)) : (r % b.params.slidesPerGroup === 0 && b.snapGrid.push(t), b.slidesGrid.push(t), t = t + o + a), b.virtualSize += o + a, s = o, r++)
                            }
                            b.virtualSize = Math.max(b.virtualSize, b.size) + b.params.slidesOffsetAfter;
                            var h;
                            if (b.rtl && b.wrongRTL && ("slide" === b.params.effect || "coverflow" === b.params.effect) && b.wrapper.css({width: b.virtualSize + b.params.spaceBetween + "px"}), b.support.flexbox && !b.params.setWrapperSize || (b.isHorizontal() ? b.wrapper.css({width: b.virtualSize + b.params.spaceBetween + "px"}) : b.wrapper.css({height: b.virtualSize + b.params.spaceBetween + "px"})), b.params.slidesPerColumn > 1 && (b.virtualSize = (o + b.params.spaceBetween) * n, b.virtualSize = Math.ceil(b.virtualSize / b.params.slidesPerColumn) - b.params.spaceBetween, b.isHorizontal() ? b.wrapper.css({width: b.virtualSize + b.params.spaceBetween + "px"}) : b.wrapper.css({height: b.virtualSize + b.params.spaceBetween + "px"}), b.params.centeredSlides)) {
                                for (h = [], e = 0; e < b.snapGrid.length; e++)b.snapGrid[e] < b.virtualSize + b.snapGrid[0] && h.push(b.snapGrid[e]);
                                b.snapGrid = h
                            }
                            if (!b.params.centeredSlides) {
                                for (h = [], e = 0; e < b.snapGrid.length; e++)b.snapGrid[e] <= b.virtualSize - b.size && h.push(b.snapGrid[e]);
                                b.snapGrid = h, Math.floor(b.virtualSize - b.size) - Math.floor(b.snapGrid[b.snapGrid.length - 1]) > 1 && b.snapGrid.push(b.virtualSize - b.size)
                            }
                            0 === b.snapGrid.length && (b.snapGrid = [0]), 0 !== b.params.spaceBetween && (b.isHorizontal() ? b.rtl ? b.slides.css({marginLeft: a + "px"}) : b.slides.css({marginRight: a + "px"}) : b.slides.css({marginBottom: a + "px"})), b.params.watchSlidesProgress && b.updateSlidesOffset()
                        }
                    }, b.updateSlidesOffset = function () {
                        for (var e = 0; e < b.slides.length; e++)b.slides[e].swiperSlideOffset = b.isHorizontal() ? b.slides[e].offsetLeft : b.slides[e].offsetTop
                    }, b.updateSlidesProgress = function (e) {
                        if ("undefined" == typeof e && (e = b.translate || 0), 0 !== b.slides.length) {
                            "undefined" == typeof b.slides[0].swiperSlideOffset && b.updateSlidesOffset();
                            var a = -e;
                            b.rtl && (a = e), b.slides.removeClass(b.params.slideVisibleClass);
                            for (var t = 0; t < b.slides.length; t++) {
                                var s = b.slides[t], r = (a + (b.params.centeredSlides ? b.minTranslate() : 0) - s.swiperSlideOffset) / (s.swiperSlideSize + b.params.spaceBetween);
                                if (b.params.watchSlidesVisibility) {
                                    var i = -(a - s.swiperSlideOffset), n = i + b.slidesSizesGrid[t], o = i >= 0 && i < b.size || n > 0 && n <= b.size || i <= 0 && n >= b.size;
                                    o && b.slides.eq(t).addClass(b.params.slideVisibleClass)
                                }
                                s.progress = b.rtl ? -r : r
                            }
                        }
                    }, b.updateProgress = function (e) {
                        "undefined" == typeof e && (e = b.translate || 0);
                        var a = b.maxTranslate() - b.minTranslate(), t = b.isBeginning, s = b.isEnd;
                        0 === a ? (b.progress = 0, b.isBeginning = b.isEnd = !0) : (b.progress = (e - b.minTranslate()) / a, b.isBeginning = b.progress <= 0, b.isEnd = b.progress >= 1), b.isBeginning && !t && b.emit("onReachBeginning", b), b.isEnd && !s && b.emit("onReachEnd", b), b.params.watchSlidesProgress && b.updateSlidesProgress(e), b.emit("onProgress", b, b.progress)
                    }, b.updateActiveIndex = function () {
                        var e, a, t, s = b.rtl ? b.translate : -b.translate;
                        for (a = 0; a < b.slidesGrid.length; a++)"undefined" != typeof b.slidesGrid[a + 1] ? s >= b.slidesGrid[a] && s < b.slidesGrid[a + 1] - (b.slidesGrid[a + 1] - b.slidesGrid[a]) / 2 ? e = a : s >= b.slidesGrid[a] && s < b.slidesGrid[a + 1] && (e = a + 1) : s >= b.slidesGrid[a] && (e = a);
                        b.params.normalizeSlideIndex && (e < 0 || "undefined" == typeof e) && (e = 0), t = Math.floor(e / b.params.slidesPerGroup), t >= b.snapGrid.length && (t = b.snapGrid.length - 1), e !== b.activeIndex && (b.snapIndex = t, b.previousIndex = b.activeIndex, b.activeIndex = e, b.updateClasses(), b.updateRealIndex())
                    }, b.updateRealIndex = function () {
                        b.realIndex = b.slides.eq(b.activeIndex).attr("data-swiper-slide-index") || b.activeIndex
                    }, b.updateClasses = function () {
                        b.slides.removeClass(b.params.slideActiveClass + " " + b.params.slideNextClass + " " + b.params.slidePrevClass + " " + b.params.slideDuplicateActiveClass + " " + b.params.slideDuplicateNextClass + " " + b.params.slideDuplicatePrevClass);
                        var e = b.slides.eq(b.activeIndex);
                        e.addClass(b.params.slideActiveClass), s.loop && (e.hasClass(b.params.slideDuplicateClass) ? b.wrapper.children("." + b.params.slideClass + ":not(." + b.params.slideDuplicateClass + ')[data-swiper-slide-index="' + b.realIndex + '"]').addClass(b.params.slideDuplicateActiveClass) : b.wrapper.children("." + b.params.slideClass + "." + b.params.slideDuplicateClass + '[data-swiper-slide-index="' + b.realIndex + '"]').addClass(b.params.slideDuplicateActiveClass));
                        var t = e.next("." + b.params.slideClass).addClass(b.params.slideNextClass);
                        b.params.loop && 0 === t.length && (t = b.slides.eq(0), t.addClass(b.params.slideNextClass));
                        var r = e.prev("." + b.params.slideClass).addClass(b.params.slidePrevClass);
                        if (b.params.loop && 0 === r.length && (r = b.slides.eq(-1), r.addClass(b.params.slidePrevClass)), s.loop && (t.hasClass(b.params.slideDuplicateClass) ? b.wrapper.children("." + b.params.slideClass + ":not(." + b.params.slideDuplicateClass + ')[data-swiper-slide-index="' + t.attr("data-swiper-slide-index") + '"]').addClass(b.params.slideDuplicateNextClass) : b.wrapper.children("." + b.params.slideClass + "." + b.params.slideDuplicateClass + '[data-swiper-slide-index="' + t.attr("data-swiper-slide-index") + '"]').addClass(b.params.slideDuplicateNextClass), r.hasClass(b.params.slideDuplicateClass) ? b.wrapper.children("." + b.params.slideClass + ":not(." + b.params.slideDuplicateClass + ')[data-swiper-slide-index="' + r.attr("data-swiper-slide-index") + '"]').addClass(b.params.slideDuplicatePrevClass) : b.wrapper.children("." + b.params.slideClass + "." + b.params.slideDuplicateClass + '[data-swiper-slide-index="' + r.attr("data-swiper-slide-index") + '"]').addClass(b.params.slideDuplicatePrevClass)), b.paginationContainer && b.paginationContainer.length > 0) {
                            var i, n = b.params.loop ? Math.ceil((b.slides.length - 2 * b.loopedSlides) / b.params.slidesPerGroup) : b.snapGrid.length;
                            if (b.params.loop ? (i = Math.ceil((b.activeIndex - b.loopedSlides) / b.params.slidesPerGroup), i > b.slides.length - 1 - 2 * b.loopedSlides && (i -= b.slides.length - 2 * b.loopedSlides), i > n - 1 && (i -= n), i < 0 && "bullets" !== b.params.paginationType && (i = n + i)) : i = "undefined" != typeof b.snapIndex ? b.snapIndex : b.activeIndex || 0, "bullets" === b.params.paginationType && b.bullets && b.bullets.length > 0 && (b.bullets.removeClass(b.params.bulletActiveClass), b.paginationContainer.length > 1 ? b.bullets.each(function () {
                                        a(this).index() === i && a(this).addClass(b.params.bulletActiveClass)
                                    }) : b.bullets.eq(i).addClass(b.params.bulletActiveClass)), "fraction" === b.params.paginationType && (b.paginationContainer.find("." + b.params.paginationCurrentClass).text(i + 1), b.paginationContainer.find("." + b.params.paginationTotalClass).text(n)), "progress" === b.params.paginationType) {
                                var o = (i + 1) / n, l = o, p = 1;
                                b.isHorizontal() || (p = o, l = 1), b.paginationContainer.find("." + b.params.paginationProgressbarClass).transform("translate3d(0,0,0) scaleX(" + l + ") scaleY(" + p + ")").transition(b.params.speed)
                            }
                            "custom" === b.params.paginationType && b.params.paginationCustomRender && (b.paginationContainer.html(b.params.paginationCustomRender(b, i + 1, n)), b.emit("onPaginationRendered", b, b.paginationContainer[0]))
                        }
                        b.params.loop || (b.params.prevButton && b.prevButton && b.prevButton.length > 0 && (b.isBeginning ? (b.prevButton.addClass(b.params.buttonDisabledClass), b.params.a11y && b.a11y && b.a11y.disable(b.prevButton)) : (b.prevButton.removeClass(b.params.buttonDisabledClass), b.params.a11y && b.a11y && b.a11y.enable(b.prevButton))), b.params.nextButton && b.nextButton && b.nextButton.length > 0 && (b.isEnd ? (b.nextButton.addClass(b.params.buttonDisabledClass), b.params.a11y && b.a11y && b.a11y.disable(b.nextButton)) : (b.nextButton.removeClass(b.params.buttonDisabledClass), b.params.a11y && b.a11y && b.a11y.enable(b.nextButton))))
                    }, b.updatePagination = function () {
                        if (b.params.pagination && b.paginationContainer && b.paginationContainer.length > 0) {
                            var e = "";
                            if ("bullets" === b.params.paginationType) {
                                for (var a = b.params.loop ? Math.ceil((b.slides.length - 2 * b.loopedSlides) / b.params.slidesPerGroup) : b.snapGrid.length, t = 0; t < a; t++)e += b.params.paginationBulletRender ? b.params.paginationBulletRender(b, t, b.params.bulletClass) : "<" + b.params.paginationElement + ' class="' + b.params.bulletClass + '"></' + b.params.paginationElement + ">";
                                b.paginationContainer.html(e), b.bullets = b.paginationContainer.find("." + b.params.bulletClass), b.params.paginationClickable && b.params.a11y && b.a11y && b.a11y.initPagination()
                            }
                            "fraction" === b.params.paginationType && (e = b.params.paginationFractionRender ? b.params.paginationFractionRender(b, b.params.paginationCurrentClass, b.params.paginationTotalClass) : '<span class="' + b.params.paginationCurrentClass + '"></span> / <span class="' + b.params.paginationTotalClass + '"></span>', b.paginationContainer.html(e)), "progress" === b.params.paginationType && (e = b.params.paginationProgressRender ? b.params.paginationProgressRender(b, b.params.paginationProgressbarClass) : '<span class="' + b.params.paginationProgressbarClass + '"></span>', b.paginationContainer.html(e)), "custom" !== b.params.paginationType && b.emit("onPaginationRendered", b, b.paginationContainer[0])
                        }
                    }, b.update = function (e) {
                        function a() {
                            b.rtl ? -b.translate : b.translate;
                            s = Math.min(Math.max(b.translate, b.maxTranslate()), b.minTranslate()), b.setWrapperTranslate(s), b.updateActiveIndex(), b.updateClasses()
                        }

                        if (b.updateContainerSize(), b.updateSlidesSize(), b.updateProgress(), b.updatePagination(), b.updateClasses(), b.params.scrollbar && b.scrollbar && b.scrollbar.set(), e) {
                            var t, s;
                            b.controller && b.controller.spline && (b.controller.spline = void 0), b.params.freeMode ? (a(), b.params.autoHeight && b.updateAutoHeight()) : (t = ("auto" === b.params.slidesPerView || b.params.slidesPerView > 1) && b.isEnd && !b.params.centeredSlides ? b.slideTo(b.slides.length - 1, 0, !1, !0) : b.slideTo(b.activeIndex, 0, !1, !0), t || a())
                        } else b.params.autoHeight && b.updateAutoHeight()
                    }, b.onResize = function (e) {
                        b.params.breakpoints && b.setBreakpoint();
                        var a = b.params.allowSwipeToPrev, t = b.params.allowSwipeToNext;
                        b.params.allowSwipeToPrev = b.params.allowSwipeToNext = !0, b.updateContainerSize(), b.updateSlidesSize(), ("auto" === b.params.slidesPerView || b.params.freeMode || e) && b.updatePagination(), b.params.scrollbar && b.scrollbar && b.scrollbar.set(), b.controller && b.controller.spline && (b.controller.spline = void 0);
                        var s = !1;
                        if (b.params.freeMode) {
                            var r = Math.min(Math.max(b.translate, b.maxTranslate()), b.minTranslate());
                            b.setWrapperTranslate(r), b.updateActiveIndex(), b.updateClasses(), b.params.autoHeight && b.updateAutoHeight()
                        } else b.updateClasses(), s = ("auto" === b.params.slidesPerView || b.params.slidesPerView > 1) && b.isEnd && !b.params.centeredSlides ? b.slideTo(b.slides.length - 1, 0, !1, !0) : b.slideTo(b.activeIndex, 0, !1, !0);
                        b.params.lazyLoading && !s && b.lazy && b.lazy.load(), b.params.allowSwipeToPrev = a, b.params.allowSwipeToNext = t
                    }, b.touchEventsDesktop = {
                        start: "mousedown",
                        move: "mousemove",
                        end: "mouseup"
                    }, window.navigator.pointerEnabled ? b.touchEventsDesktop = {
                        start: "pointerdown",
                        move: "pointermove",
                        end: "pointerup"
                    } : window.navigator.msPointerEnabled && (b.touchEventsDesktop = {
                        start: "MSPointerDown",
                        move: "MSPointerMove",
                        end: "MSPointerUp"
                    }), b.touchEvents = {
                        start: b.support.touch || !b.params.simulateTouch ? "touchstart" : b.touchEventsDesktop.start,
                        move: b.support.touch || !b.params.simulateTouch ? "touchmove" : b.touchEventsDesktop.move,
                        end: b.support.touch || !b.params.simulateTouch ? "touchend" : b.touchEventsDesktop.end
                    }, (window.navigator.pointerEnabled || window.navigator.msPointerEnabled) && ("container" === b.params.touchEventsTarget ? b.container : b.wrapper).addClass("swiper-wp8-" + b.params.direction), b.initEvents = function (e) {
                        var a = e ? "off" : "on", t = e ? "removeEventListener" : "addEventListener", r = "container" === b.params.touchEventsTarget ? b.container[0] : b.wrapper[0], i = b.support.touch ? r : document, n = !!b.params.nested;
                        if (b.browser.ie)r[t](b.touchEvents.start, b.onTouchStart, !1), i[t](b.touchEvents.move, b.onTouchMove, n), i[t](b.touchEvents.end, b.onTouchEnd, !1); else {
                            if (b.support.touch) {
                                var o = !("touchstart" !== b.touchEvents.start || !b.support.passiveListener || !b.params.passiveListeners) && {
                                            passive: !0,
                                            capture: !1
                                        };
                                r[t](b.touchEvents.start, b.onTouchStart, o), r[t](b.touchEvents.move, b.onTouchMove, n), r[t](b.touchEvents.end, b.onTouchEnd, o)
                            }
                            (s.simulateTouch && !b.device.ios && !b.device.android || s.simulateTouch && !b.support.touch && b.device.ios) && (r[t]("mousedown", b.onTouchStart, !1), document[t]("mousemove", b.onTouchMove, n), document[t]("mouseup", b.onTouchEnd, !1))
                        }
                        window[t]("resize", b.onResize), b.params.nextButton && b.nextButton && b.nextButton.length > 0 && (b.nextButton[a]("click", b.onClickNext), b.params.a11y && b.a11y && b.nextButton[a]("keydown", b.a11y.onEnterKey)), b.params.prevButton && b.prevButton && b.prevButton.length > 0 && (b.prevButton[a]("click", b.onClickPrev), b.params.a11y && b.a11y && b.prevButton[a]("keydown", b.a11y.onEnterKey)), b.params.pagination && b.params.paginationClickable && (b.paginationContainer[a]("click", "." + b.params.bulletClass, b.onClickIndex), b.params.a11y && b.a11y && b.paginationContainer[a]("keydown", "." + b.params.bulletClass, b.a11y.onEnterKey)), (b.params.preventClicks || b.params.preventClicksPropagation) && r[t]("click", b.preventClicks, !0)
                    }, b.attachEvents = function () {
                        b.initEvents()
                    }, b.detachEvents = function () {
                        b.initEvents(!0)
                    }, b.allowClick = !0, b.preventClicks = function (e) {
                        b.allowClick || (b.params.preventClicks && e.preventDefault(), b.params.preventClicksPropagation && b.animating && (e.stopPropagation(), e.stopImmediatePropagation()))
                    }, b.onClickNext = function (e) {
                        e.preventDefault(), b.isEnd && !b.params.loop || b.slideNext()
                    }, b.onClickPrev = function (e) {
                        e.preventDefault(), b.isBeginning && !b.params.loop || b.slidePrev();
                    }, b.onClickIndex = function (e) {
                        e.preventDefault();
                        var t = a(this).index() * b.params.slidesPerGroup;
                        b.params.loop && (t += b.loopedSlides), b.slideTo(t)
                    }, b.updateClickedSlide = function (e) {
                        var t = o(e, "." + b.params.slideClass), s = !1;
                        if (t)for (var r = 0; r < b.slides.length; r++)b.slides[r] === t && (s = !0);
                        if (!t || !s)return b.clickedSlide = void 0, void(b.clickedIndex = void 0);
                        if (b.clickedSlide = t, b.clickedIndex = a(t).index(), b.params.slideToClickedSlide && void 0 !== b.clickedIndex && b.clickedIndex !== b.activeIndex) {
                            var i, n = b.clickedIndex;
                            if (b.params.loop) {
                                if (b.animating)return;
                                i = a(b.clickedSlide).attr("data-swiper-slide-index"), b.params.centeredSlides ? n < b.loopedSlides - b.params.slidesPerView / 2 || n > b.slides.length - b.loopedSlides + b.params.slidesPerView / 2 ? (b.fixLoop(), n = b.wrapper.children("." + b.params.slideClass + '[data-swiper-slide-index="' + i + '"]:not(.' + b.params.slideDuplicateClass + ")").eq(0).index(), setTimeout(function () {
                                    b.slideTo(n)
                                }, 0)) : b.slideTo(n) : n > b.slides.length - b.params.slidesPerView ? (b.fixLoop(), n = b.wrapper.children("." + b.params.slideClass + '[data-swiper-slide-index="' + i + '"]:not(.' + b.params.slideDuplicateClass + ")").eq(0).index(), setTimeout(function () {
                                    b.slideTo(n)
                                }, 0)) : b.slideTo(n)
                            } else b.slideTo(n)
                        }
                    };
                    var S, z, M, P, E, I, k, D, L, B, H = "input, select, textarea, button, video", G = Date.now(), X = [];
                    b.animating = !1, b.touches = {startX: 0, startY: 0, currentX: 0, currentY: 0, diff: 0};
                    var Y, A;
                    b.onTouchStart = function (e) {
                        if (e.originalEvent && (e = e.originalEvent), Y = "touchstart" === e.type, Y || !("which"in e) || 3 !== e.which) {
                            if (b.params.noSwiping && o(e, "." + b.params.noSwipingClass))return void(b.allowClick = !0);
                            if (!b.params.swipeHandler || o(e, b.params.swipeHandler)) {
                                var t = b.touches.currentX = "touchstart" === e.type ? e.targetTouches[0].pageX : e.pageX, s = b.touches.currentY = "touchstart" === e.type ? e.targetTouches[0].pageY : e.pageY;
                                if (!(b.device.ios && b.params.iOSEdgeSwipeDetection && t <= b.params.iOSEdgeSwipeThreshold)) {
                                    if (S = !0, z = !1, M = !0, E = void 0, A = void 0, b.touches.startX = t, b.touches.startY = s, P = Date.now(), b.allowClick = !0, b.updateContainerSize(), b.swipeDirection = void 0, b.params.threshold > 0 && (D = !1), "touchstart" !== e.type) {
                                        var r = !0;
                                        a(e.target).is(H) && (r = !1), document.activeElement && a(document.activeElement).is(H) && document.activeElement.blur(), r && e.preventDefault()
                                    }
                                    b.emit("onTouchStart", b, e)
                                }
                            }
                        }
                    }, b.onTouchMove = function (e) {
                        if (e.originalEvent && (e = e.originalEvent), !Y || "mousemove" !== e.type) {
                            if (e.preventedByNestedSwiper)return b.touches.startX = "touchmove" === e.type ? e.targetTouches[0].pageX : e.pageX, void(b.touches.startY = "touchmove" === e.type ? e.targetTouches[0].pageY : e.pageY);
                            if (b.params.onlyExternal)return b.allowClick = !1, void(S && (b.touches.startX = b.touches.currentX = "touchmove" === e.type ? e.targetTouches[0].pageX : e.pageX, b.touches.startY = b.touches.currentY = "touchmove" === e.type ? e.targetTouches[0].pageY : e.pageY, P = Date.now()));
                            if (Y && b.params.touchReleaseOnEdges && !b.params.loop)if (b.isHorizontal()) {
                                if (b.touches.currentX < b.touches.startX && b.translate <= b.maxTranslate() || b.touches.currentX > b.touches.startX && b.translate >= b.minTranslate())return
                            } else if (b.touches.currentY < b.touches.startY && b.translate <= b.maxTranslate() || b.touches.currentY > b.touches.startY && b.translate >= b.minTranslate())return;
                            if (Y && document.activeElement && e.target === document.activeElement && a(e.target).is(H))return z = !0, void(b.allowClick = !1);
                            if (M && b.emit("onTouchMove", b, e), !(e.targetTouches && e.targetTouches.length > 1)) {
                                if (b.touches.currentX = "touchmove" === e.type ? e.targetTouches[0].pageX : e.pageX, b.touches.currentY = "touchmove" === e.type ? e.targetTouches[0].pageY : e.pageY, "undefined" == typeof E) {
                                    var t;
                                    b.isHorizontal() && b.touches.currentY === b.touches.startY || !b.isHorizontal() && b.touches.currentX !== b.touches.startX ? E = !1 : (t = 180 * Math.atan2(Math.abs(b.touches.currentY - b.touches.startY), Math.abs(b.touches.currentX - b.touches.startX)) / Math.PI, E = b.isHorizontal() ? t > b.params.touchAngle : 90 - t > b.params.touchAngle)
                                }
                                if (E && b.emit("onTouchMoveOpposite", b, e), "undefined" == typeof A && b.browser.ieTouch && (b.touches.currentX === b.touches.startX && b.touches.currentY === b.touches.startY || (A = !0)), S) {
                                    if (E)return void(S = !1);
                                    if (A || !b.browser.ieTouch) {
                                        b.allowClick = !1, b.emit("onSliderMove", b, e), e.preventDefault(), b.params.touchMoveStopPropagation && !b.params.nested && e.stopPropagation(), z || (s.loop && b.fixLoop(), k = b.getWrapperTranslate(), b.setWrapperTransition(0), b.animating && b.wrapper.trigger("webkitTransitionEnd transitionend oTransitionEnd MSTransitionEnd msTransitionEnd"), b.params.autoplay && b.autoplaying && (b.params.autoplayDisableOnInteraction ? b.stopAutoplay() : b.pauseAutoplay()), B = !1, !b.params.grabCursor || b.params.allowSwipeToNext !== !0 && b.params.allowSwipeToPrev !== !0 || b.setGrabCursor(!0)), z = !0;
                                        var r = b.touches.diff = b.isHorizontal() ? b.touches.currentX - b.touches.startX : b.touches.currentY - b.touches.startY;
                                        r *= b.params.touchRatio, b.rtl && (r = -r), b.swipeDirection = r > 0 ? "prev" : "next", I = r + k;
                                        var i = !0;
                                        if (r > 0 && I > b.minTranslate() ? (i = !1, b.params.resistance && (I = b.minTranslate() - 1 + Math.pow(-b.minTranslate() + k + r, b.params.resistanceRatio))) : r < 0 && I < b.maxTranslate() && (i = !1, b.params.resistance && (I = b.maxTranslate() + 1 - Math.pow(b.maxTranslate() - k - r, b.params.resistanceRatio))), i && (e.preventedByNestedSwiper = !0), !b.params.allowSwipeToNext && "next" === b.swipeDirection && I < k && (I = k), !b.params.allowSwipeToPrev && "prev" === b.swipeDirection && I > k && (I = k), b.params.threshold > 0) {
                                            if (!(Math.abs(r) > b.params.threshold || D))return void(I = k);
                                            if (!D)return D = !0, b.touches.startX = b.touches.currentX, b.touches.startY = b.touches.currentY, I = k, void(b.touches.diff = b.isHorizontal() ? b.touches.currentX - b.touches.startX : b.touches.currentY - b.touches.startY)
                                        }
                                        b.params.followFinger && ((b.params.freeMode || b.params.watchSlidesProgress) && b.updateActiveIndex(), b.params.freeMode && (0 === X.length && X.push({
                                            position: b.touches[b.isHorizontal() ? "startX" : "startY"],
                                            time: P
                                        }), X.push({
                                            position: b.touches[b.isHorizontal() ? "currentX" : "currentY"],
                                            time: (new window.Date).getTime()
                                        })), b.updateProgress(I), b.setWrapperTranslate(I))
                                    }
                                }
                            }
                        }
                    }, b.onTouchEnd = function (e) {
                        if (e.originalEvent && (e = e.originalEvent), M && b.emit("onTouchEnd", b, e), M = !1, S) {
                            b.params.grabCursor && z && S && (b.params.allowSwipeToNext === !0 || b.params.allowSwipeToPrev === !0) && b.setGrabCursor(!1);
                            var t = Date.now(), s = t - P;
                            if (b.allowClick && (b.updateClickedSlide(e), b.emit("onTap", b, e), s < 300 && t - G > 300 && (L && clearTimeout(L), L = setTimeout(function () {
                                        b && (b.params.paginationHide && b.paginationContainer.length > 0 && !a(e.target).hasClass(b.params.bulletClass) && b.paginationContainer.toggleClass(b.params.paginationHiddenClass), b.emit("onClick", b, e))
                                    }, 300)), s < 300 && t - G < 300 && (L && clearTimeout(L), b.emit("onDoubleTap", b, e))), G = Date.now(), setTimeout(function () {
                                        b && (b.allowClick = !0)
                                    }, 0), !S || !z || !b.swipeDirection || 0 === b.touches.diff || I === k)return void(S = z = !1);
                            S = z = !1;
                            var r;
                            if (r = b.params.followFinger ? b.rtl ? b.translate : -b.translate : -I, b.params.freeMode) {
                                if (r < -b.minTranslate())return void b.slideTo(b.activeIndex);
                                if (r > -b.maxTranslate())return void(b.slides.length < b.snapGrid.length ? b.slideTo(b.snapGrid.length - 1) : b.slideTo(b.slides.length - 1));
                                if (b.params.freeModeMomentum) {
                                    if (X.length > 1) {
                                        var i = X.pop(), n = X.pop(), o = i.position - n.position, l = i.time - n.time;
                                        b.velocity = o / l, b.velocity = b.velocity / 2, Math.abs(b.velocity) < b.params.freeModeMinimumVelocity && (b.velocity = 0), (l > 150 || (new window.Date).getTime() - i.time > 300) && (b.velocity = 0)
                                    } else b.velocity = 0;
                                    b.velocity = b.velocity * b.params.freeModeMomentumVelocityRatio, X.length = 0;
                                    var p = 1e3 * b.params.freeModeMomentumRatio, d = b.velocity * p, u = b.translate + d;
                                    b.rtl && (u = -u);
                                    var m, c = !1, g = 20 * Math.abs(b.velocity) * b.params.freeModeMomentumBounceRatio;
                                    if (u < b.maxTranslate())b.params.freeModeMomentumBounce ? (u + b.maxTranslate() < -g && (u = b.maxTranslate() - g), m = b.maxTranslate(), c = !0, B = !0) : u = b.maxTranslate(); else if (u > b.minTranslate())b.params.freeModeMomentumBounce ? (u - b.minTranslate() > g && (u = b.minTranslate() + g), m = b.minTranslate(), c = !0, B = !0) : u = b.minTranslate(); else if (b.params.freeModeSticky) {
                                        var h, f = 0;
                                        for (f = 0; f < b.snapGrid.length; f += 1)if (b.snapGrid[f] > -u) {
                                            h = f;
                                            break
                                        }
                                        u = Math.abs(b.snapGrid[h] - u) < Math.abs(b.snapGrid[h - 1] - u) || "next" === b.swipeDirection ? b.snapGrid[h] : b.snapGrid[h - 1], b.rtl || (u = -u)
                                    }
                                    if (0 !== b.velocity)p = b.rtl ? Math.abs((-u - b.translate) / b.velocity) : Math.abs((u - b.translate) / b.velocity); else if (b.params.freeModeSticky)return void b.slideReset();
                                    b.params.freeModeMomentumBounce && c ? (b.updateProgress(m), b.setWrapperTransition(p), b.setWrapperTranslate(u), b.onTransitionStart(), b.animating = !0, b.wrapper.transitionEnd(function () {
                                        b && B && (b.emit("onMomentumBounce", b), b.setWrapperTransition(b.params.speed), b.setWrapperTranslate(m), b.wrapper.transitionEnd(function () {
                                            b && b.onTransitionEnd()
                                        }))
                                    })) : b.velocity ? (b.updateProgress(u), b.setWrapperTransition(p), b.setWrapperTranslate(u), b.onTransitionStart(), b.animating || (b.animating = !0, b.wrapper.transitionEnd(function () {
                                        b && b.onTransitionEnd()
                                    }))) : b.updateProgress(u), b.updateActiveIndex()
                                }
                                return void((!b.params.freeModeMomentum || s >= b.params.longSwipesMs) && (b.updateProgress(), b.updateActiveIndex()))
                            }
                            var v, w = 0, y = b.slidesSizesGrid[0];
                            for (v = 0; v < b.slidesGrid.length; v += b.params.slidesPerGroup)"undefined" != typeof b.slidesGrid[v + b.params.slidesPerGroup] ? r >= b.slidesGrid[v] && r < b.slidesGrid[v + b.params.slidesPerGroup] && (w = v, y = b.slidesGrid[v + b.params.slidesPerGroup] - b.slidesGrid[v]) : r >= b.slidesGrid[v] && (w = v, y = b.slidesGrid[b.slidesGrid.length - 1] - b.slidesGrid[b.slidesGrid.length - 2]);
                            var x = (r - b.slidesGrid[w]) / y;
                            if (s > b.params.longSwipesMs) {
                                if (!b.params.longSwipes)return void b.slideTo(b.activeIndex);
                                "next" === b.swipeDirection && (x >= b.params.longSwipesRatio ? b.slideTo(w + b.params.slidesPerGroup) : b.slideTo(w)), "prev" === b.swipeDirection && (x > 1 - b.params.longSwipesRatio ? b.slideTo(w + b.params.slidesPerGroup) : b.slideTo(w))
                            } else {
                                if (!b.params.shortSwipes)return void b.slideTo(b.activeIndex);
                                "next" === b.swipeDirection && b.slideTo(w + b.params.slidesPerGroup), "prev" === b.swipeDirection && b.slideTo(w)
                            }
                        }
                    }, b._slideTo = function (e, a) {
                        return b.slideTo(e, a, !0, !0)
                    }, b.slideTo = function (e, a, t, s) {
                        "undefined" == typeof t && (t = !0), "undefined" == typeof e && (e = 0), e < 0 && (e = 0), b.snapIndex = Math.floor(e / b.params.slidesPerGroup), b.snapIndex >= b.snapGrid.length && (b.snapIndex = b.snapGrid.length - 1);
                        var r = -b.snapGrid[b.snapIndex];
                        if (b.params.autoplay && b.autoplaying && (s || !b.params.autoplayDisableOnInteraction ? b.pauseAutoplay(a) : b.stopAutoplay()), b.updateProgress(r), b.params.normalizeSlideIndex)for (var i = 0; i < b.slidesGrid.length; i++)-Math.floor(100 * r) >= Math.floor(100 * b.slidesGrid[i]) && (e = i);
                        return !(!b.params.allowSwipeToNext && r < b.translate && r < b.minTranslate()) && (!(!b.params.allowSwipeToPrev && r > b.translate && r > b.maxTranslate() && (b.activeIndex || 0) !== e) && ("undefined" == typeof a && (a = b.params.speed), b.previousIndex = b.activeIndex || 0, b.activeIndex = e, b.updateRealIndex(), b.rtl && -r === b.translate || !b.rtl && r === b.translate ? (b.params.autoHeight && b.updateAutoHeight(), b.updateClasses(), "slide" !== b.params.effect && b.setWrapperTranslate(r), !1) : (b.updateClasses(), b.onTransitionStart(t), 0 === a || b.browser.lteIE9 ? (b.setWrapperTranslate(r), b.setWrapperTransition(0), b.onTransitionEnd(t)) : (b.setWrapperTranslate(r), b.setWrapperTransition(a), b.animating || (b.animating = !0, b.wrapper.transitionEnd(function () {
                                    b && b.onTransitionEnd(t)
                                }))), !0)))
                    }, b.onTransitionStart = function (e) {
                        "undefined" == typeof e && (e = !0), b.params.autoHeight && b.updateAutoHeight(), b.lazy && b.lazy.onTransitionStart(), e && (b.emit("onTransitionStart", b), b.activeIndex !== b.previousIndex && (b.emit("onSlideChangeStart", b), b.activeIndex > b.previousIndex ? b.emit("onSlideNextStart", b) : b.emit("onSlidePrevStart", b)))
                    }, b.onTransitionEnd = function (e) {
                        b.animating = !1, b.setWrapperTransition(0), "undefined" == typeof e && (e = !0), b.lazy && b.lazy.onTransitionEnd(), e && (b.emit("onTransitionEnd", b), b.activeIndex !== b.previousIndex && (b.emit("onSlideChangeEnd", b), b.activeIndex > b.previousIndex ? b.emit("onSlideNextEnd", b) : b.emit("onSlidePrevEnd", b))), b.params.history && b.history && b.history.setHistory(b.params.history, b.activeIndex), b.params.hashnav && b.hashnav && b.hashnav.setHash()
                    }, b.slideNext = function (e, a, t) {
                        if (b.params.loop) {
                            if (b.animating)return !1;
                            b.fixLoop();
                            b.container[0].clientLeft;
                            return b.slideTo(b.activeIndex + b.params.slidesPerGroup, a, e, t)
                        }
                        return b.slideTo(b.activeIndex + b.params.slidesPerGroup, a, e, t)
                    }, b._slideNext = function (e) {
                        return b.slideNext(!0, e, !0)
                    }, b.slidePrev = function (e, a, t) {
                        if (b.params.loop) {
                            if (b.animating)return !1;
                            b.fixLoop();
                            b.container[0].clientLeft;
                            return b.slideTo(b.activeIndex - 1, a, e, t)
                        }
                        return b.slideTo(b.activeIndex - 1, a, e, t)
                    }, b._slidePrev = function (e) {
                        return b.slidePrev(!0, e, !0)
                    }, b.slideReset = function (e, a, t) {
                        return b.slideTo(b.activeIndex, a, e)
                    }, b.disableTouchControl = function () {
                        return b.params.onlyExternal = !0, !0
                    }, b.enableTouchControl = function () {
                        return b.params.onlyExternal = !1, !0
                    }, b.setWrapperTransition = function (e, a) {
                        b.wrapper.transition(e), "slide" !== b.params.effect && b.effects[b.params.effect] && b.effects[b.params.effect].setTransition(e), b.params.parallax && b.parallax && b.parallax.setTransition(e), b.params.scrollbar && b.scrollbar && b.scrollbar.setTransition(e), b.params.control && b.controller && b.controller.setTransition(e, a), b.emit("onSetTransition", b, e)
                    }, b.setWrapperTranslate = function (e, a, t) {
                        var s = 0, r = 0, n = 0;
                        b.isHorizontal() ? s = b.rtl ? -e : e : r = e, b.params.roundLengths && (s = i(s), r = i(r)), b.params.virtualTranslate || (b.support.transforms3d ? b.wrapper.transform("translate3d(" + s + "px, " + r + "px, " + n + "px)") : b.wrapper.transform("translate(" + s + "px, " + r + "px)")), b.translate = b.isHorizontal() ? s : r;
                        var o, l = b.maxTranslate() - b.minTranslate();
                        o = 0 === l ? 0 : (e - b.minTranslate()) / l, o !== b.progress && b.updateProgress(e), a && b.updateActiveIndex(), "slide" !== b.params.effect && b.effects[b.params.effect] && b.effects[b.params.effect].setTranslate(b.translate), b.params.parallax && b.parallax && b.parallax.setTranslate(b.translate), b.params.scrollbar && b.scrollbar && b.scrollbar.setTranslate(b.translate), b.params.control && b.controller && b.controller.setTranslate(b.translate, t), b.emit("onSetTranslate", b, b.translate)
                    }, b.getTranslate = function (e, a) {
                        var t, s, r, i;
                        return "undefined" == typeof a && (a = "x"), b.params.virtualTranslate ? b.rtl ? -b.translate : b.translate : (r = window.getComputedStyle(e, null), window.WebKitCSSMatrix ? (s = r.transform || r.webkitTransform, s.split(",").length > 6 && (s = s.split(", ").map(function (e) {
                            return e.replace(",", ".")
                        }).join(", ")), i = new window.WebKitCSSMatrix("none" === s ? "" : s)) : (i = r.MozTransform || r.OTransform || r.MsTransform || r.msTransform || r.transform || r.getPropertyValue("transform").replace("translate(", "matrix(1, 0, 0, 1,"), t = i.toString().split(",")), "x" === a && (s = window.WebKitCSSMatrix ? i.m41 : 16 === t.length ? parseFloat(t[12]) : parseFloat(t[4])), "y" === a && (s = window.WebKitCSSMatrix ? i.m42 : 16 === t.length ? parseFloat(t[13]) : parseFloat(t[5])), b.rtl && s && (s = -s), s || 0)
                    }, b.getWrapperTranslate = function (e) {
                        return "undefined" == typeof e && (e = b.isHorizontal() ? "x" : "y"), b.getTranslate(b.wrapper[0], e)
                    }, b.observers = [], b.initObservers = function () {
                        if (b.params.observeParents)for (var e = b.container.parents(), a = 0; a < e.length; a++)l(e[a]);
                        l(b.container[0], {childList: !1}), l(b.wrapper[0], {attributes: !1})
                    }, b.disconnectObservers = function () {
                        for (var e = 0; e < b.observers.length; e++)b.observers[e].disconnect();
                        b.observers = []
                    }, b.createLoop = function () {
                        b.wrapper.children("." + b.params.slideClass + "." + b.params.slideDuplicateClass).remove();
                        var e = b.wrapper.children("." + b.params.slideClass);
                        "auto" !== b.params.slidesPerView || b.params.loopedSlides || (b.params.loopedSlides = e.length), b.loopedSlides = parseInt(b.params.loopedSlides || b.params.slidesPerView, 10), b.loopedSlides = b.loopedSlides + b.params.loopAdditionalSlides, b.loopedSlides > e.length && (b.loopedSlides = e.length);
                        var t, s = [], r = [];
                        for (e.each(function (t, i) {
                            var n = a(this);
                            t < b.loopedSlides && r.push(i), t < e.length && t >= e.length - b.loopedSlides && s.push(i), n.attr("data-swiper-slide-index", t)
                        }), t = 0; t < r.length; t++)b.wrapper.append(a(r[t].cloneNode(!0)).addClass(b.params.slideDuplicateClass));
                        for (t = s.length - 1; t >= 0; t--)b.wrapper.prepend(a(s[t].cloneNode(!0)).addClass(b.params.slideDuplicateClass))
                    }, b.destroyLoop = function () {
                        b.wrapper.children("." + b.params.slideClass + "." + b.params.slideDuplicateClass).remove(), b.slides.removeAttr("data-swiper-slide-index")
                    }, b.reLoop = function (e) {
                        var a = b.activeIndex - b.loopedSlides;
                        b.destroyLoop(), b.createLoop(), b.updateSlidesSize(), e && b.slideTo(a + b.loopedSlides, 0, !1)
                    }, b.fixLoop = function () {
                        var e;
                        b.activeIndex < b.loopedSlides ? (e = b.slides.length - 3 * b.loopedSlides + b.activeIndex, e += b.loopedSlides, b.slideTo(e, 0, !1, !0)) : ("auto" === b.params.slidesPerView && b.activeIndex >= 2 * b.loopedSlides || b.activeIndex > b.slides.length - 2 * b.params.slidesPerView) && (e = -b.slides.length + b.activeIndex + b.loopedSlides, e += b.loopedSlides, b.slideTo(e, 0, !1, !0))
                    }, b.appendSlide = function (e) {
                        if (b.params.loop && b.destroyLoop(), "object" == typeof e && e.length)for (var a = 0; a < e.length; a++)e[a] && b.wrapper.append(e[a]); else b.wrapper.append(e);
                        b.params.loop && b.createLoop(), b.params.observer && b.support.observer || b.update(!0)
                    }, b.prependSlide = function (e) {
                        b.params.loop && b.destroyLoop();
                        var a = b.activeIndex + 1;
                        if ("object" == typeof e && e.length) {
                            for (var t = 0; t < e.length; t++)e[t] && b.wrapper.prepend(e[t]);
                            a = b.activeIndex + e.length
                        } else b.wrapper.prepend(e);
                        b.params.loop && b.createLoop(), b.params.observer && b.support.observer || b.update(!0), b.slideTo(a, 0, !1)
                    }, b.removeSlide = function (e) {
                        b.params.loop && (b.destroyLoop(), b.slides = b.wrapper.children("." + b.params.slideClass));
                        var a, t = b.activeIndex;
                        if ("object" == typeof e && e.length) {
                            for (var s = 0; s < e.length; s++)a = e[s], b.slides[a] && b.slides.eq(a).remove(), a < t && t--;
                            t = Math.max(t, 0)
                        } else a = e, b.slides[a] && b.slides.eq(a).remove(), a < t && t--, t = Math.max(t, 0);
                        b.params.loop && b.createLoop(), b.params.observer && b.support.observer || b.update(!0), b.params.loop ? b.slideTo(t + b.loopedSlides, 0, !1) : b.slideTo(t, 0, !1)
                    }, b.removeAllSlides = function () {
                        for (var e = [], a = 0; a < b.slides.length; a++)e.push(a);
                        b.removeSlide(e)
                    }, b.effects = {
                        fade: {
                            setTranslate: function () {
                                for (var e = 0; e < b.slides.length; e++) {
                                    var a = b.slides.eq(e), t = a[0].swiperSlideOffset, s = -t;
                                    b.params.virtualTranslate || (s -= b.translate);
                                    var r = 0;
                                    b.isHorizontal() || (r = s, s = 0);
                                    var i = b.params.fade.crossFade ? Math.max(1 - Math.abs(a[0].progress), 0) : 1 + Math.min(Math.max(a[0].progress, -1), 0);
                                    a.css({opacity: i}).transform("translate3d(" + s + "px, " + r + "px, 0px)")
                                }
                            }, setTransition: function (e) {
                                if (b.slides.transition(e), b.params.virtualTranslate && 0 !== e) {
                                    var a = !1;
                                    b.slides.transitionEnd(function () {
                                        if (!a && b) {
                                            a = !0, b.animating = !1;
                                            for (var e = ["webkitTransitionEnd", "transitionend", "oTransitionEnd", "MSTransitionEnd", "msTransitionEnd"], t = 0; t < e.length; t++)b.wrapper.trigger(e[t])
                                        }
                                    })
                                }
                            }
                        }, flip: {
                            setTranslate: function () {
                                for (var e = 0; e < b.slides.length; e++) {
                                    var t = b.slides.eq(e), s = t[0].progress;
                                    b.params.flip.limitRotation && (s = Math.max(Math.min(t[0].progress, 1), -1));
                                    var r = t[0].swiperSlideOffset, i = -180 * s, n = i, o = 0, l = -r, p = 0;
                                    if (b.isHorizontal() ? b.rtl && (n = -n) : (p = l, l = 0, o = -n, n = 0), t[0].style.zIndex = -Math.abs(Math.round(s)) + b.slides.length, b.params.flip.slideShadows) {
                                        var d = b.isHorizontal() ? t.find(".swiper-slide-shadow-left") : t.find(".swiper-slide-shadow-top"), u = b.isHorizontal() ? t.find(".swiper-slide-shadow-right") : t.find(".swiper-slide-shadow-bottom");
                                        0 === d.length && (d = a('<div class="swiper-slide-shadow-' + (b.isHorizontal() ? "left" : "top") + '"></div>'), t.append(d)), 0 === u.length && (u = a('<div class="swiper-slide-shadow-' + (b.isHorizontal() ? "right" : "bottom") + '"></div>'), t.append(u)), d.length && (d[0].style.opacity = Math.max(-s, 0)), u.length && (u[0].style.opacity = Math.max(s, 0))
                                    }
                                    t.transform("translate3d(" + l + "px, " + p + "px, 0px) rotateX(" + o + "deg) rotateY(" + n + "deg)")
                                }
                            }, setTransition: function (e) {
                                if (b.slides.transition(e).find(".swiper-slide-shadow-top, .swiper-slide-shadow-right, .swiper-slide-shadow-bottom, .swiper-slide-shadow-left").transition(e), b.params.virtualTranslate && 0 !== e) {
                                    var t = !1;
                                    b.slides.eq(b.activeIndex).transitionEnd(function () {
                                        if (!t && b && a(this).hasClass(b.params.slideActiveClass)) {
                                            t = !0, b.animating = !1;
                                            for (var e = ["webkitTransitionEnd", "transitionend", "oTransitionEnd", "MSTransitionEnd", "msTransitionEnd"], s = 0; s < e.length; s++)b.wrapper.trigger(e[s])
                                        }
                                    })
                                }
                            }
                        }, cube: {
                            setTranslate: function () {
                                var e, t = 0;
                                b.params.cube.shadow && (b.isHorizontal() ? (e = b.wrapper.find(".swiper-cube-shadow"), 0 === e.length && (e = a('<div class="swiper-cube-shadow"></div>'), b.wrapper.append(e)), e.css({height: b.width + "px"})) : (e = b.container.find(".swiper-cube-shadow"), 0 === e.length && (e = a('<div class="swiper-cube-shadow"></div>'), b.container.append(e))));
                                for (var s = 0; s < b.slides.length; s++) {
                                    var r = b.slides.eq(s), i = 90 * s, n = Math.floor(i / 360);
                                    b.rtl && (i = -i, n = Math.floor(-i / 360));
                                    var o = Math.max(Math.min(r[0].progress, 1), -1), l = 0, p = 0, d = 0;
                                    s % 4 === 0 ? (l = 4 * -n * b.size, d = 0) : (s - 1) % 4 === 0 ? (l = 0, d = 4 * -n * b.size) : (s - 2) % 4 === 0 ? (l = b.size + 4 * n * b.size, d = b.size) : (s - 3) % 4 === 0 && (l = -b.size, d = 3 * b.size + 4 * b.size * n), b.rtl && (l = -l), b.isHorizontal() || (p = l, l = 0);
                                    var u = "rotateX(" + (b.isHorizontal() ? 0 : -i) + "deg) rotateY(" + (b.isHorizontal() ? i : 0) + "deg) translate3d(" + l + "px, " + p + "px, " + d + "px)";
                                    if (o <= 1 && o > -1 && (t = 90 * s + 90 * o, b.rtl && (t = 90 * -s - 90 * o)), r.transform(u), b.params.cube.slideShadows) {
                                        var m = b.isHorizontal() ? r.find(".swiper-slide-shadow-left") : r.find(".swiper-slide-shadow-top"), c = b.isHorizontal() ? r.find(".swiper-slide-shadow-right") : r.find(".swiper-slide-shadow-bottom");
                                        0 === m.length && (m = a('<div class="swiper-slide-shadow-' + (b.isHorizontal() ? "left" : "top") + '"></div>'), r.append(m)), 0 === c.length && (c = a('<div class="swiper-slide-shadow-' + (b.isHorizontal() ? "right" : "bottom") + '"></div>'), r.append(c)), m.length && (m[0].style.opacity = Math.max(-o, 0)), c.length && (c[0].style.opacity = Math.max(o, 0))
                                    }
                                }
                                if (b.wrapper.css({
                                            "-webkit-transform-origin": "50% 50% -" + b.size / 2 + "px",
                                            "-moz-transform-origin": "50% 50% -" + b.size / 2 + "px",
                                            "-ms-transform-origin": "50% 50% -" + b.size / 2 + "px",
                                            "transform-origin": "50% 50% -" + b.size / 2 + "px"
                                        }), b.params.cube.shadow)if (b.isHorizontal())e.transform("translate3d(0px, " + (b.width / 2 + b.params.cube.shadowOffset) + "px, " + -b.width / 2 + "px) rotateX(90deg) rotateZ(0deg) scale(" + b.params.cube.shadowScale + ")"); else {
                                    var g = Math.abs(t) - 90 * Math.floor(Math.abs(t) / 90), h = 1.5 - (Math.sin(2 * g * Math.PI / 360) / 2 + Math.cos(2 * g * Math.PI / 360) / 2), f = b.params.cube.shadowScale, v = b.params.cube.shadowScale / h, w = b.params.cube.shadowOffset;
                                    e.transform("scale3d(" + f + ", 1, " + v + ") translate3d(0px, " + (b.height / 2 + w) + "px, " + -b.height / 2 / v + "px) rotateX(-90deg)")
                                }
                                var y = b.isSafari || b.isUiWebView ? -b.size / 2 : 0;
                                b.wrapper.transform("translate3d(0px,0," + y + "px) rotateX(" + (b.isHorizontal() ? 0 : t) + "deg) rotateY(" + (b.isHorizontal() ? -t : 0) + "deg)")
                            }, setTransition: function (e) {
                                b.slides.transition(e).find(".swiper-slide-shadow-top, .swiper-slide-shadow-right, .swiper-slide-shadow-bottom, .swiper-slide-shadow-left").transition(e), b.params.cube.shadow && !b.isHorizontal() && b.container.find(".swiper-cube-shadow").transition(e)
                            }
                        }, coverflow: {
                            setTranslate: function () {
                                for (var e = b.translate, t = b.isHorizontal() ? -e + b.width / 2 : -e + b.height / 2, s = b.isHorizontal() ? b.params.coverflow.rotate : -b.params.coverflow.rotate, r = b.params.coverflow.depth, i = 0, n = b.slides.length; i < n; i++) {
                                    var o = b.slides.eq(i), l = b.slidesSizesGrid[i], p = o[0].swiperSlideOffset, d = (t - p - l / 2) / l * b.params.coverflow.modifier, u = b.isHorizontal() ? s * d : 0, m = b.isHorizontal() ? 0 : s * d, c = -r * Math.abs(d), g = b.isHorizontal() ? 0 : b.params.coverflow.stretch * d, h = b.isHorizontal() ? b.params.coverflow.stretch * d : 0;
                                    Math.abs(h) < .001 && (h = 0), Math.abs(g) < .001 && (g = 0), Math.abs(c) < .001 && (c = 0), Math.abs(u) < .001 && (u = 0), Math.abs(m) < .001 && (m = 0);
                                    var f = "translate3d(" + h + "px," + g + "px," + c + "px)  rotateX(" + m + "deg) rotateY(" + u + "deg)";
                                    if (o.transform(f), o[0].style.zIndex = -Math.abs(Math.round(d)) + 1, b.params.coverflow.slideShadows) {
                                        var v = b.isHorizontal() ? o.find(".swiper-slide-shadow-left") : o.find(".swiper-slide-shadow-top"), w = b.isHorizontal() ? o.find(".swiper-slide-shadow-right") : o.find(".swiper-slide-shadow-bottom");
                                        0 === v.length && (v = a('<div class="swiper-slide-shadow-' + (b.isHorizontal() ? "left" : "top") + '"></div>'), o.append(v)), 0 === w.length && (w = a('<div class="swiper-slide-shadow-' + (b.isHorizontal() ? "right" : "bottom") + '"></div>'), o.append(w)), v.length && (v[0].style.opacity = d > 0 ? d : 0), w.length && (w[0].style.opacity = -d > 0 ? -d : 0)
                                    }
                                }
                                if (b.browser.ie) {
                                    var y = b.wrapper[0].style;
                                    y.perspectiveOrigin = t + "px 50%"
                                }
                            }, setTransition: function (e) {
                                b.slides.transition(e).find(".swiper-slide-shadow-top, .swiper-slide-shadow-right, .swiper-slide-shadow-bottom, .swiper-slide-shadow-left").transition(e)
                            }
                        }
                    }, b.lazy = {
                        initialImageLoaded: !1, loadImageInSlide: function (e, t) {
                            if ("undefined" != typeof e && ("undefined" == typeof t && (t = !0), 0 !== b.slides.length)) {
                                var s = b.slides.eq(e), r = s.find("." + b.params.lazyLoadingClass + ":not(." + b.params.lazyStatusLoadedClass + "):not(." + b.params.lazyStatusLoadingClass + ")");
                                !s.hasClass(b.params.lazyLoadingClass) || s.hasClass(b.params.lazyStatusLoadedClass) || s.hasClass(b.params.lazyStatusLoadingClass) || (r = r.add(s[0])), 0 !== r.length && r.each(function () {
                                    var e = a(this);
                                    e.addClass(b.params.lazyStatusLoadingClass);
                                    var r = e.attr("data-background"), i = e.attr("data-src"), n = e.attr("data-srcset"), o = e.attr("data-sizes");
                                    b.loadImage(e[0], i || r, n, o, !1, function () {
                                        if (r ? (e.css("background-image", 'url("' + r + '")'), e.removeAttr("data-background")) : (n && (e.attr("srcset", n), e.removeAttr("data-srcset")), o && (e.attr("sizes", o), e.removeAttr("data-sizes")), i && (e.attr("src", i), e.removeAttr("data-src"))), e.addClass(b.params.lazyStatusLoadedClass).removeClass(b.params.lazyStatusLoadingClass), s.find("." + b.params.lazyPreloaderClass + ", ." + b.params.preloaderClass).remove(), b.params.loop && t) {
                                            var a = s.attr("data-swiper-slide-index");
                                            if (s.hasClass(b.params.slideDuplicateClass)) {
                                                var l = b.wrapper.children('[data-swiper-slide-index="' + a + '"]:not(.' + b.params.slideDuplicateClass + ")");
                                                b.lazy.loadImageInSlide(l.index(), !1)
                                            } else {
                                                var p = b.wrapper.children("." + b.params.slideDuplicateClass + '[data-swiper-slide-index="' + a + '"]');
                                                b.lazy.loadImageInSlide(p.index(), !1)
                                            }
                                        }
                                        b.emit("onLazyImageReady", b, s[0], e[0])
                                    }), b.emit("onLazyImageLoad", b, s[0], e[0])
                                })
                            }
                        }, load: function () {
                            var e, t = b.params.slidesPerView;
                            if ("auto" === t && (t = 0), b.lazy.initialImageLoaded || (b.lazy.initialImageLoaded = !0), b.params.watchSlidesVisibility)b.wrapper.children("." + b.params.slideVisibleClass).each(function () {
                                b.lazy.loadImageInSlide(a(this).index())
                            }); else if (t > 1)for (e = b.activeIndex; e < b.activeIndex + t; e++)b.slides[e] && b.lazy.loadImageInSlide(e); else b.lazy.loadImageInSlide(b.activeIndex);
                            if (b.params.lazyLoadingInPrevNext)if (t > 1 || b.params.lazyLoadingInPrevNextAmount && b.params.lazyLoadingInPrevNextAmount > 1) {
                                var s = b.params.lazyLoadingInPrevNextAmount, r = t, i = Math.min(b.activeIndex + r + Math.max(s, r), b.slides.length), n = Math.max(b.activeIndex - Math.max(r, s), 0);
                                for (e = b.activeIndex + t; e < i; e++)b.slides[e] && b.lazy.loadImageInSlide(e);
                                for (e = n; e < b.activeIndex; e++)b.slides[e] && b.lazy.loadImageInSlide(e)
                            } else {
                                var o = b.wrapper.children("." + b.params.slideNextClass);
                                o.length > 0 && b.lazy.loadImageInSlide(o.index());
                                var l = b.wrapper.children("." + b.params.slidePrevClass);
                                l.length > 0 && b.lazy.loadImageInSlide(l.index())
                            }
                        }, onTransitionStart: function () {
                            b.params.lazyLoading && (b.params.lazyLoadingOnTransitionStart || !b.params.lazyLoadingOnTransitionStart && !b.lazy.initialImageLoaded) && b.lazy.load()
                        }, onTransitionEnd: function () {
                            b.params.lazyLoading && !b.params.lazyLoadingOnTransitionStart && b.lazy.load()
                        }
                    }, b.scrollbar = {
                        isTouched: !1, setDragPosition: function (e) {
                            var a = b.scrollbar, t = b.isHorizontal() ? "touchstart" === e.type || "touchmove" === e.type ? e.targetTouches[0].pageX : e.pageX || e.clientX : "touchstart" === e.type || "touchmove" === e.type ? e.targetTouches[0].pageY : e.pageY || e.clientY, s = t - a.track.offset()[b.isHorizontal() ? "left" : "top"] - a.dragSize / 2, r = -b.minTranslate() * a.moveDivider, i = -b.maxTranslate() * a.moveDivider;
                            s < r ? s = r : s > i && (s = i), s = -s / a.moveDivider, b.updateProgress(s), b.setWrapperTranslate(s, !0)
                        }, dragStart: function (e) {
                            var a = b.scrollbar;
                            a.isTouched = !0, e.preventDefault(), e.stopPropagation(), a.setDragPosition(e), clearTimeout(a.dragTimeout), a.track.transition(0), b.params.scrollbarHide && a.track.css("opacity", 1), b.wrapper.transition(100), a.drag.transition(100), b.emit("onScrollbarDragStart", b)
                        }, dragMove: function (e) {
                            var a = b.scrollbar;
                            a.isTouched && (e.preventDefault ? e.preventDefault() : e.returnValue = !1, a.setDragPosition(e), b.wrapper.transition(0), a.track.transition(0), a.drag.transition(0), b.emit("onScrollbarDragMove", b))
                        }, dragEnd: function (e) {
                            var a = b.scrollbar;
                            a.isTouched && (a.isTouched = !1, b.params.scrollbarHide && (clearTimeout(a.dragTimeout), a.dragTimeout = setTimeout(function () {
                                a.track.css("opacity", 0), a.track.transition(400)
                            }, 1e3)), b.emit("onScrollbarDragEnd", b), b.params.scrollbarSnapOnRelease && b.slideReset())
                        }, draggableEvents: function () {
                            return b.params.simulateTouch !== !1 || b.support.touch ? b.touchEvents : b.touchEventsDesktop
                        }(), enableDraggable: function () {
                            var e = b.scrollbar, t = b.support.touch ? e.track : document;
                            a(e.track).on(e.draggableEvents.start, e.dragStart), a(t).on(e.draggableEvents.move, e.dragMove), a(t).on(e.draggableEvents.end, e.dragEnd)
                        }, disableDraggable: function () {
                            var e = b.scrollbar, t = b.support.touch ? e.track : document;
                            a(e.track).off(b.draggableEvents.start, e.dragStart), a(t).off(b.draggableEvents.move, e.dragMove), a(t).off(b.draggableEvents.end, e.dragEnd)
                        }, set: function () {
                            if (b.params.scrollbar) {
                                var e = b.scrollbar;
                                e.track = a(b.params.scrollbar), b.params.uniqueNavElements && "string" == typeof b.params.scrollbar && e.track.length > 1 && 1 === b.container.find(b.params.scrollbar).length && (e.track = b.container.find(b.params.scrollbar)), e.drag = e.track.find(".swiper-scrollbar-drag"), 0 === e.drag.length && (e.drag = a('<div class="swiper-scrollbar-drag"></div>'), e.track.append(e.drag)), e.drag[0].style.width = "", e.drag[0].style.height = "", e.trackSize = b.isHorizontal() ? e.track[0].offsetWidth : e.track[0].offsetHeight, e.divider = b.size / b.virtualSize, e.moveDivider = e.divider * (e.trackSize / b.size), e.dragSize = e.trackSize * e.divider, b.isHorizontal() ? e.drag[0].style.width = e.dragSize + "px" : e.drag[0].style.height = e.dragSize + "px", e.divider >= 1 ? e.track[0].style.display = "none" : e.track[0].style.display = "", b.params.scrollbarHide && (e.track[0].style.opacity = 0)
                            }
                        }, setTranslate: function () {
                            if (b.params.scrollbar) {
                                var e, a = b.scrollbar, t = (b.translate || 0, a.dragSize);
                                e = (a.trackSize - a.dragSize) * b.progress, b.rtl && b.isHorizontal() ? (e = -e, e > 0 ? (t = a.dragSize - e, e = 0) : -e + a.dragSize > a.trackSize && (t = a.trackSize + e)) : e < 0 ? (t = a.dragSize + e, e = 0) : e + a.dragSize > a.trackSize && (t = a.trackSize - e), b.isHorizontal() ? (b.support.transforms3d ? a.drag.transform("translate3d(" + e + "px, 0, 0)") : a.drag.transform("translateX(" + e + "px)"), a.drag[0].style.width = t + "px") : (b.support.transforms3d ? a.drag.transform("translate3d(0px, " + e + "px, 0)") : a.drag.transform("translateY(" + e + "px)"), a.drag[0].style.height = t + "px"), b.params.scrollbarHide && (clearTimeout(a.timeout), a.track[0].style.opacity = 1, a.timeout = setTimeout(function () {
                                    a.track[0].style.opacity = 0, a.track.transition(400)
                                }, 1e3))
                            }
                        }, setTransition: function (e) {
                            b.params.scrollbar && b.scrollbar.drag.transition(e)
                        }
                    }, b.controller = {
                        LinearSpline: function (e, a) {
                            this.x = e, this.y = a, this.lastIndex = e.length - 1;
                            var t, s;
                            this.x.length;
                            this.interpolate = function (e) {
                                return e ? (s = r(this.x, e), t = s - 1, (e - this.x[t]) * (this.y[s] - this.y[t]) / (this.x[s] - this.x[t]) + this.y[t]) : 0
                            };
                            var r = function () {
                                var e, a, t;
                                return function (s, r) {
                                    for (a = -1, e = s.length; e - a > 1;)s[t = e + a >> 1] <= r ? a = t : e = t;
                                    return e
                                }
                            }()
                        }, getInterpolateFunction: function (e) {
                            b.controller.spline || (b.controller.spline = b.params.loop ? new b.controller.LinearSpline(b.slidesGrid, e.slidesGrid) : new b.controller.LinearSpline(b.snapGrid, e.snapGrid))
                        }, setTranslate: function (e, a) {
                            function s(a) {
                                e = a.rtl && "horizontal" === a.params.direction ? -b.translate : b.translate, "slide" === b.params.controlBy && (b.controller.getInterpolateFunction(a), i = -b.controller.spline.interpolate(-e)), i && "container" !== b.params.controlBy || (r = (a.maxTranslate() - a.minTranslate()) / (b.maxTranslate() - b.minTranslate()), i = (e - b.minTranslate()) * r + a.minTranslate()), b.params.controlInverse && (i = a.maxTranslate() - i), a.updateProgress(i), a.setWrapperTranslate(i, !1, b), a.updateActiveIndex()
                            }

                            var r, i, n = b.params.control;
                            if (b.isArray(n))for (var o = 0; o < n.length; o++)n[o] !== a && n[o]instanceof t && s(n[o]); else n instanceof t && a !== n && s(n)
                        }, setTransition: function (e, a) {
                            function s(a) {
                                a.setWrapperTransition(e, b), 0 !== e && (a.onTransitionStart(), a.wrapper.transitionEnd(function () {
                                    i && (a.params.loop && "slide" === b.params.controlBy && a.fixLoop(), a.onTransitionEnd())
                                }))
                            }

                            var r, i = b.params.control;
                            if (b.isArray(i))for (r = 0; r < i.length; r++)i[r] !== a && i[r]instanceof t && s(i[r]); else i instanceof t && a !== i && s(i)
                        }
                    }, b.hashnav = {
                        onHashCange: function (e, a) {
                            var t = document.location.hash.replace("#", ""), s = b.slides.eq(b.activeIndex).attr("data-hash");
                            t !== s && b.slideTo(b.wrapper.children("." + b.params.slideClass + '[data-hash="' + t + '"]').index());
                        }, attachEvents: function (e) {
                            var t = e ? "off" : "on";
                            a(window)[t]("hashchange", b.hashnav.onHashCange)
                        }, setHash: function () {
                            if (b.hashnav.initialized && b.params.hashnav)if (b.params.replaceState && window.history && window.history.replaceState)window.history.replaceState(null, null, "#" + b.slides.eq(b.activeIndex).attr("data-hash") || ""); else {
                                var e = b.slides.eq(b.activeIndex), a = e.attr("data-hash") || e.attr("data-history");
                                document.location.hash = a || ""
                            }
                        }, init: function () {
                            if (b.params.hashnav && !b.params.history) {
                                b.hashnav.initialized = !0;
                                var e = document.location.hash.replace("#", "");
                                if (e) {
                                    for (var a = 0, t = 0, s = b.slides.length; t < s; t++) {
                                        var r = b.slides.eq(t), i = r.attr("data-hash") || r.attr("data-history");
                                        if (i === e && !r.hasClass(b.params.slideDuplicateClass)) {
                                            var n = r.index();
                                            b.slideTo(n, a, b.params.runCallbacksOnInit, !0)
                                        }
                                    }
                                    b.params.hashnavWatchState && b.hashnav.attachEvents()
                                }
                            }
                        }, destroy: function () {
                            b.params.hashnavWatchState && b.hashnav.attachEvents(!0)
                        }
                    }, b.history = {
                        init: function () {
                            if (b.params.history) {
                                if (!window.history || !window.history.pushState)return b.params.history = !1, void(b.params.hashnav = !0);
                                b.history.initialized = !0, this.paths = this.getPathValues(), (this.paths.key || this.paths.value) && (this.scrollToSlide(0, this.paths.value, b.params.runCallbacksOnInit), b.params.replaceState || window.addEventListener("popstate", this.setHistoryPopState))
                            }
                        }, setHistoryPopState: function () {
                            b.history.paths = b.history.getPathValues(), b.history.scrollToSlide(b.params.speed, b.history.paths.value, !1)
                        }, getPathValues: function () {
                            var e = window.location.pathname.slice(1).split("/"), a = e.length, t = e[a - 2], s = e[a - 1];
                            return {key: t, value: s}
                        }, setHistory: function (e, a) {
                            if (b.history.initialized && b.params.history) {
                                var t = b.slides.eq(a), s = this.slugify(t.attr("data-history"));
                                window.location.pathname.includes(e) || (s = e + "/" + s), b.params.replaceState ? window.history.replaceState(null, null, s) : window.history.pushState(null, null, s)
                            }
                        }, slugify: function (e) {
                            return e.toString().toLowerCase().replace(/\s+/g, "-").replace(/[^\w\-]+/g, "").replace(/\-\-+/g, "-").replace(/^-+/, "").replace(/-+$/, "")
                        }, scrollToSlide: function (e, a, t) {
                            if (a)for (var s = 0, r = b.slides.length; s < r; s++) {
                                var i = b.slides.eq(s), n = this.slugify(i.attr("data-history"));
                                if (n === a && !i.hasClass(b.params.slideDuplicateClass)) {
                                    var o = i.index();
                                    b.slideTo(o, e, t)
                                }
                            } else b.slideTo(0, e, t)
                        }
                    }, b.disableKeyboardControl = function () {
                        b.params.keyboardControl = !1, a(document).off("keydown", p)
                    }, b.enableKeyboardControl = function () {
                        b.params.keyboardControl = !0, a(document).on("keydown", p)
                    }, b.mousewheel = {
                        event: !1,
                        lastScrollTime: (new window.Date).getTime()
                    }, b.params.mousewheelControl && (b.mousewheel.event = navigator.userAgent.indexOf("firefox") > -1 ? "DOMMouseScroll" : d() ? "wheel" : "mousewheel"), b.disableMousewheelControl = function () {
                        if (!b.mousewheel.event)return !1;
                        var e = b.container;
                        return "container" !== b.params.mousewheelEventsTarged && (e = a(b.params.mousewheelEventsTarged)), e.off(b.mousewheel.event, u), !0
                    }, b.enableMousewheelControl = function () {
                        if (!b.mousewheel.event)return !1;
                        var e = b.container;
                        return "container" !== b.params.mousewheelEventsTarged && (e = a(b.params.mousewheelEventsTarged)), e.on(b.mousewheel.event, u), !0
                    }, b.parallax = {
                        setTranslate: function () {
                            b.container.children("[data-swiper-parallax], [data-swiper-parallax-x], [data-swiper-parallax-y]").each(function () {
                                c(this, b.progress)
                            }), b.slides.each(function () {
                                var e = a(this);
                                e.find("[data-swiper-parallax], [data-swiper-parallax-x], [data-swiper-parallax-y]").each(function () {
                                    var a = Math.min(Math.max(e[0].progress, -1), 1);
                                    c(this, a)
                                })
                            })
                        }, setTransition: function (e) {
                            "undefined" == typeof e && (e = b.params.speed), b.container.find("[data-swiper-parallax], [data-swiper-parallax-x], [data-swiper-parallax-y]").each(function () {
                                var t = a(this), s = parseInt(t.attr("data-swiper-parallax-duration"), 10) || e;
                                0 === e && (s = 0), t.transition(s)
                            })
                        }
                    }, b.zoom = {
                        scale: 1,
                        currentScale: 1,
                        isScaling: !1,
                        gesture: {
                            slide: void 0,
                            slideWidth: void 0,
                            slideHeight: void 0,
                            image: void 0,
                            imageWrap: void 0,
                            zoomMax: b.params.zoomMax
                        },
                        image: {
                            isTouched: void 0,
                            isMoved: void 0,
                            currentX: void 0,
                            currentY: void 0,
                            minX: void 0,
                            minY: void 0,
                            maxX: void 0,
                            maxY: void 0,
                            width: void 0,
                            height: void 0,
                            startX: void 0,
                            startY: void 0,
                            touchesStart: {},
                            touchesCurrent: {}
                        },
                        velocity: {
                            x: void 0,
                            y: void 0,
                            prevPositionX: void 0,
                            prevPositionY: void 0,
                            prevTime: void 0
                        },
                        getDistanceBetweenTouches: function (e) {
                            if (e.targetTouches.length < 2)return 1;
                            var a = e.targetTouches[0].pageX, t = e.targetTouches[0].pageY, s = e.targetTouches[1].pageX, r = e.targetTouches[1].pageY, i = Math.sqrt(Math.pow(s - a, 2) + Math.pow(r - t, 2));
                            return i
                        },
                        onGestureStart: function (e) {
                            var t = b.zoom;
                            if (!b.support.gestures) {
                                if ("touchstart" !== e.type || "touchstart" === e.type && e.targetTouches.length < 2)return;
                                t.gesture.scaleStart = t.getDistanceBetweenTouches(e)
                            }
                            return t.gesture.slide && t.gesture.slide.length || (t.gesture.slide = a(this), 0 === t.gesture.slide.length && (t.gesture.slide = b.slides.eq(b.activeIndex)), t.gesture.image = t.gesture.slide.find("img, svg, canvas"), t.gesture.imageWrap = t.gesture.image.parent("." + b.params.zoomContainerClass), t.gesture.zoomMax = t.gesture.imageWrap.attr("data-swiper-zoom") || b.params.zoomMax, 0 !== t.gesture.imageWrap.length) ? (t.gesture.image.transition(0), void(t.isScaling = !0)) : void(t.gesture.image = void 0)
                        },
                        onGestureChange: function (e) {
                            var a = b.zoom;
                            if (!b.support.gestures) {
                                if ("touchmove" !== e.type || "touchmove" === e.type && e.targetTouches.length < 2)return;
                                a.gesture.scaleMove = a.getDistanceBetweenTouches(e)
                            }
                            a.gesture.image && 0 !== a.gesture.image.length && (b.support.gestures ? a.scale = e.scale * a.currentScale : a.scale = a.gesture.scaleMove / a.gesture.scaleStart * a.currentScale, a.scale > a.gesture.zoomMax && (a.scale = a.gesture.zoomMax - 1 + Math.pow(a.scale - a.gesture.zoomMax + 1, .5)), a.scale < b.params.zoomMin && (a.scale = b.params.zoomMin + 1 - Math.pow(b.params.zoomMin - a.scale + 1, .5)), a.gesture.image.transform("translate3d(0,0,0) scale(" + a.scale + ")"))
                        },
                        onGestureEnd: function (e) {
                            var a = b.zoom;
                            !b.support.gestures && ("touchend" !== e.type || "touchend" === e.type && e.changedTouches.length < 2) || a.gesture.image && 0 !== a.gesture.image.length && (a.scale = Math.max(Math.min(a.scale, a.gesture.zoomMax), b.params.zoomMin), a.gesture.image.transition(b.params.speed).transform("translate3d(0,0,0) scale(" + a.scale + ")"), a.currentScale = a.scale, a.isScaling = !1, 1 === a.scale && (a.gesture.slide = void 0))
                        },
                        onTouchStart: function (e, a) {
                            var t = e.zoom;
                            t.gesture.image && 0 !== t.gesture.image.length && (t.image.isTouched || ("android" === e.device.os && a.preventDefault(), t.image.isTouched = !0, t.image.touchesStart.x = "touchstart" === a.type ? a.targetTouches[0].pageX : a.pageX, t.image.touchesStart.y = "touchstart" === a.type ? a.targetTouches[0].pageY : a.pageY))
                        },
                        onTouchMove: function (e) {
                            var a = b.zoom;
                            if (a.gesture.image && 0 !== a.gesture.image.length && (b.allowClick = !1, a.image.isTouched && a.gesture.slide)) {
                                a.image.isMoved || (a.image.width = a.gesture.image[0].offsetWidth, a.image.height = a.gesture.image[0].offsetHeight, a.image.startX = b.getTranslate(a.gesture.imageWrap[0], "x") || 0, a.image.startY = b.getTranslate(a.gesture.imageWrap[0], "y") || 0, a.gesture.slideWidth = a.gesture.slide[0].offsetWidth, a.gesture.slideHeight = a.gesture.slide[0].offsetHeight, a.gesture.imageWrap.transition(0));
                                var t = a.image.width * a.scale, s = a.image.height * a.scale;
                                if (!(t < a.gesture.slideWidth && s < a.gesture.slideHeight)) {
                                    if (a.image.minX = Math.min(a.gesture.slideWidth / 2 - t / 2, 0), a.image.maxX = -a.image.minX, a.image.minY = Math.min(a.gesture.slideHeight / 2 - s / 2, 0), a.image.maxY = -a.image.minY, a.image.touchesCurrent.x = "touchmove" === e.type ? e.targetTouches[0].pageX : e.pageX, a.image.touchesCurrent.y = "touchmove" === e.type ? e.targetTouches[0].pageY : e.pageY, !a.image.isMoved && !a.isScaling) {
                                        if (b.isHorizontal() && Math.floor(a.image.minX) === Math.floor(a.image.startX) && a.image.touchesCurrent.x < a.image.touchesStart.x || Math.floor(a.image.maxX) === Math.floor(a.image.startX) && a.image.touchesCurrent.x > a.image.touchesStart.x)return void(a.image.isTouched = !1);
                                        if (!b.isHorizontal() && Math.floor(a.image.minY) === Math.floor(a.image.startY) && a.image.touchesCurrent.y < a.image.touchesStart.y || Math.floor(a.image.maxY) === Math.floor(a.image.startY) && a.image.touchesCurrent.y > a.image.touchesStart.y)return void(a.image.isTouched = !1)
                                    }
                                    e.preventDefault(), e.stopPropagation(), a.image.isMoved = !0, a.image.currentX = a.image.touchesCurrent.x - a.image.touchesStart.x + a.image.startX, a.image.currentY = a.image.touchesCurrent.y - a.image.touchesStart.y + a.image.startY, a.image.currentX < a.image.minX && (a.image.currentX = a.image.minX + 1 - Math.pow(a.image.minX - a.image.currentX + 1, .8)), a.image.currentX > a.image.maxX && (a.image.currentX = a.image.maxX - 1 + Math.pow(a.image.currentX - a.image.maxX + 1, .8)), a.image.currentY < a.image.minY && (a.image.currentY = a.image.minY + 1 - Math.pow(a.image.minY - a.image.currentY + 1, .8)), a.image.currentY > a.image.maxY && (a.image.currentY = a.image.maxY - 1 + Math.pow(a.image.currentY - a.image.maxY + 1, .8)), a.velocity.prevPositionX || (a.velocity.prevPositionX = a.image.touchesCurrent.x), a.velocity.prevPositionY || (a.velocity.prevPositionY = a.image.touchesCurrent.y), a.velocity.prevTime || (a.velocity.prevTime = Date.now()), a.velocity.x = (a.image.touchesCurrent.x - a.velocity.prevPositionX) / (Date.now() - a.velocity.prevTime) / 2, a.velocity.y = (a.image.touchesCurrent.y - a.velocity.prevPositionY) / (Date.now() - a.velocity.prevTime) / 2, Math.abs(a.image.touchesCurrent.x - a.velocity.prevPositionX) < 2 && (a.velocity.x = 0), Math.abs(a.image.touchesCurrent.y - a.velocity.prevPositionY) < 2 && (a.velocity.y = 0), a.velocity.prevPositionX = a.image.touchesCurrent.x, a.velocity.prevPositionY = a.image.touchesCurrent.y, a.velocity.prevTime = Date.now(), a.gesture.imageWrap.transform("translate3d(" + a.image.currentX + "px, " + a.image.currentY + "px,0)")
                                }
                            }
                        },
                        onTouchEnd: function (e, a) {
                            var t = e.zoom;
                            if (t.gesture.image && 0 !== t.gesture.image.length) {
                                if (!t.image.isTouched || !t.image.isMoved)return t.image.isTouched = !1, void(t.image.isMoved = !1);
                                t.image.isTouched = !1, t.image.isMoved = !1;
                                var s = 300, r = 300, i = t.velocity.x * s, n = t.image.currentX + i, o = t.velocity.y * r, l = t.image.currentY + o;
                                0 !== t.velocity.x && (s = Math.abs((n - t.image.currentX) / t.velocity.x)), 0 !== t.velocity.y && (r = Math.abs((l - t.image.currentY) / t.velocity.y));
                                var p = Math.max(s, r);
                                t.image.currentX = n, t.image.currentY = l;
                                var d = t.image.width * t.scale, u = t.image.height * t.scale;
                                t.image.minX = Math.min(t.gesture.slideWidth / 2 - d / 2, 0), t.image.maxX = -t.image.minX, t.image.minY = Math.min(t.gesture.slideHeight / 2 - u / 2, 0), t.image.maxY = -t.image.minY, t.image.currentX = Math.max(Math.min(t.image.currentX, t.image.maxX), t.image.minX), t.image.currentY = Math.max(Math.min(t.image.currentY, t.image.maxY), t.image.minY), t.gesture.imageWrap.transition(p).transform("translate3d(" + t.image.currentX + "px, " + t.image.currentY + "px,0)")
                            }
                        },
                        onTransitionEnd: function (e) {
                            var a = e.zoom;
                            a.gesture.slide && e.previousIndex !== e.activeIndex && (a.gesture.image.transform("translate3d(0,0,0) scale(1)"), a.gesture.imageWrap.transform("translate3d(0,0,0)"), a.gesture.slide = a.gesture.image = a.gesture.imageWrap = void 0, a.scale = a.currentScale = 1)
                        },
                        toggleZoom: function (e, t) {
                            var s = e.zoom;
                            if (s.gesture.slide || (s.gesture.slide = e.clickedSlide ? a(e.clickedSlide) : e.slides.eq(e.activeIndex), s.gesture.image = s.gesture.slide.find("img, svg, canvas"), s.gesture.imageWrap = s.gesture.image.parent("." + e.params.zoomContainerClass)), s.gesture.image && 0 !== s.gesture.image.length) {
                                var r, i, n, o, l, p, d, u, m, c, g, h, f, v, w, y, x, T;
                                "undefined" == typeof s.image.touchesStart.x && t ? (r = "touchend" === t.type ? t.changedTouches[0].pageX : t.pageX, i = "touchend" === t.type ? t.changedTouches[0].pageY : t.pageY) : (r = s.image.touchesStart.x, i = s.image.touchesStart.y), s.scale && 1 !== s.scale ? (s.scale = s.currentScale = 1, s.gesture.imageWrap.transition(300).transform("translate3d(0,0,0)"), s.gesture.image.transition(300).transform("translate3d(0,0,0) scale(1)"), s.gesture.slide = void 0) : (s.scale = s.currentScale = s.gesture.imageWrap.attr("data-swiper-zoom") || e.params.zoomMax, t ? (x = s.gesture.slide[0].offsetWidth, T = s.gesture.slide[0].offsetHeight, n = s.gesture.slide.offset().left, o = s.gesture.slide.offset().top, l = n + x / 2 - r, p = o + T / 2 - i, m = s.gesture.image[0].offsetWidth, c = s.gesture.image[0].offsetHeight, g = m * s.scale, h = c * s.scale, f = Math.min(x / 2 - g / 2, 0), v = Math.min(T / 2 - h / 2, 0), w = -f, y = -v, d = l * s.scale, u = p * s.scale, d < f && (d = f), d > w && (d = w), u < v && (u = v), u > y && (u = y)) : (d = 0, u = 0), s.gesture.imageWrap.transition(300).transform("translate3d(" + d + "px, " + u + "px,0)"), s.gesture.image.transition(300).transform("translate3d(0,0,0) scale(" + s.scale + ")"))
                            }
                        },
                        attachEvents: function (e) {
                            var t = e ? "off" : "on";
                            if (b.params.zoom) {
                                var s = (b.slides, !("touchstart" !== b.touchEvents.start || !b.support.passiveListener || !b.params.passiveListeners) && {
                                    passive: !0,
                                    capture: !1
                                });
                                b.support.gestures ? (b.slides[t]("gesturestart", b.zoom.onGestureStart, s), b.slides[t]("gesturechange", b.zoom.onGestureChange, s), b.slides[t]("gestureend", b.zoom.onGestureEnd, s)) : "touchstart" === b.touchEvents.start && (b.slides[t](b.touchEvents.start, b.zoom.onGestureStart, s), b.slides[t](b.touchEvents.move, b.zoom.onGestureChange, s), b.slides[t](b.touchEvents.end, b.zoom.onGestureEnd, s)), b[t]("touchStart", b.zoom.onTouchStart), b.slides.each(function (e, s) {
                                    a(s).find("." + b.params.zoomContainerClass).length > 0 && a(s)[t](b.touchEvents.move, b.zoom.onTouchMove)
                                }), b[t]("touchEnd", b.zoom.onTouchEnd), b[t]("transitionEnd", b.zoom.onTransitionEnd), b.params.zoomToggle && b.on("doubleTap", b.zoom.toggleZoom)
                            }
                        },
                        init: function () {
                            b.zoom.attachEvents()
                        },
                        destroy: function () {
                            b.zoom.attachEvents(!0)
                        }
                    }, b._plugins = [];
                    for (var O in b.plugins) {
                        var W = b.plugins[O](b, b.params[O]);
                        W && b._plugins.push(W)
                    }
                    return b.callPlugins = function (e) {
                        for (var a = 0; a < b._plugins.length; a++)e in b._plugins[a] && b._plugins[a][e](arguments[1], arguments[2], arguments[3], arguments[4], arguments[5])
                    }, b.emitterEventListeners = {}, b.emit = function (e) {
                        b.params[e] && b.params[e](arguments[1], arguments[2], arguments[3], arguments[4], arguments[5]);
                        var a;
                        if (b.emitterEventListeners[e])for (a = 0; a < b.emitterEventListeners[e].length; a++)b.emitterEventListeners[e][a](arguments[1], arguments[2], arguments[3], arguments[4], arguments[5]);
                        b.callPlugins && b.callPlugins(e, arguments[1], arguments[2], arguments[3], arguments[4], arguments[5])
                    }, b.on = function (e, a) {
                        return e = g(e), b.emitterEventListeners[e] || (b.emitterEventListeners[e] = []), b.emitterEventListeners[e].push(a), b
                    }, b.off = function (e, a) {
                        var t;
                        if (e = g(e), "undefined" == typeof a)return b.emitterEventListeners[e] = [], b;
                        if (b.emitterEventListeners[e] && 0 !== b.emitterEventListeners[e].length) {
                            for (t = 0; t < b.emitterEventListeners[e].length; t++)b.emitterEventListeners[e][t] === a && b.emitterEventListeners[e].splice(t, 1);
                            return b
                        }
                    }, b.once = function (e, a) {
                        e = g(e);
                        var t = function () {
                            a(arguments[0], arguments[1], arguments[2], arguments[3], arguments[4]), b.off(e, t)
                        };
                        return b.on(e, t), b
                    }, b.a11y = {
                        makeFocusable: function (e) {
                            return e.attr("tabIndex", "0"), e
                        },
                        addRole: function (e, a) {
                            return e.attr("role", a), e
                        },
                        addLabel: function (e, a) {
                            return e.attr("aria-label", a), e
                        },
                        disable: function (e) {
                            return e.attr("aria-disabled", !0), e
                        },
                        enable: function (e) {
                            return e.attr("aria-disabled", !1), e
                        },
                        onEnterKey: function (e) {
                            13 === e.keyCode && (a(e.target).is(b.params.nextButton) ? (b.onClickNext(e), b.isEnd ? b.a11y.notify(b.params.lastSlideMessage) : b.a11y.notify(b.params.nextSlideMessage)) : a(e.target).is(b.params.prevButton) && (b.onClickPrev(e), b.isBeginning ? b.a11y.notify(b.params.firstSlideMessage) : b.a11y.notify(b.params.prevSlideMessage)), a(e.target).is("." + b.params.bulletClass) && a(e.target)[0].click())
                        },
                        liveRegion: a('<span class="' + b.params.notificationClass + '" aria-live="assertive" aria-atomic="true"></span>'),
                        notify: function (e) {
                            var a = b.a11y.liveRegion;
                            0 !== a.length && (a.html(""), a.html(e))
                        },
                        init: function () {
                            b.params.nextButton && b.nextButton && b.nextButton.length > 0 && (b.a11y.makeFocusable(b.nextButton), b.a11y.addRole(b.nextButton, "button"), b.a11y.addLabel(b.nextButton, b.params.nextSlideMessage)), b.params.prevButton && b.prevButton && b.prevButton.length > 0 && (b.a11y.makeFocusable(b.prevButton), b.a11y.addRole(b.prevButton, "button"), b.a11y.addLabel(b.prevButton, b.params.prevSlideMessage)), a(b.container).append(b.a11y.liveRegion)
                        },
                        initPagination: function () {
                            b.params.pagination && b.params.paginationClickable && b.bullets && b.bullets.length && b.bullets.each(function () {
                                var e = a(this);
                                b.a11y.makeFocusable(e), b.a11y.addRole(e, "button"), b.a11y.addLabel(e, b.params.paginationBulletMessage.replace(/{{index}}/, e.index() + 1))
                            })
                        },
                        destroy: function () {
                            b.a11y.liveRegion && b.a11y.liveRegion.length > 0 && b.a11y.liveRegion.remove()
                        }
                    }, b.init = function () {
                        b.params.loop && b.createLoop(), b.updateContainerSize(), b.updateSlidesSize(), b.updatePagination(), b.params.scrollbar && b.scrollbar && (b.scrollbar.set(), b.params.scrollbarDraggable && b.scrollbar.enableDraggable()), "slide" !== b.params.effect && b.effects[b.params.effect] && (b.params.loop || b.updateProgress(), b.effects[b.params.effect].setTranslate()), b.params.loop ? b.slideTo(b.params.initialSlide + b.loopedSlides, 0, b.params.runCallbacksOnInit) : (b.slideTo(b.params.initialSlide, 0, b.params.runCallbacksOnInit), 0 === b.params.initialSlide && (b.parallax && b.params.parallax && b.parallax.setTranslate(), b.lazy && b.params.lazyLoading && (b.lazy.load(), b.lazy.initialImageLoaded = !0))), b.attachEvents(), b.params.observer && b.support.observer && b.initObservers(), b.params.preloadImages && !b.params.lazyLoading && b.preloadImages(), b.params.zoom && b.zoom && b.zoom.init(), b.params.autoplay && b.startAutoplay(), b.params.keyboardControl && b.enableKeyboardControl && b.enableKeyboardControl(), b.params.mousewheelControl && b.enableMousewheelControl && b.enableMousewheelControl(), b.params.hashnavReplaceState && (b.params.replaceState = b.params.hashnavReplaceState), b.params.history && b.history && b.history.init(), b.params.hashnav && b.hashnav && b.hashnav.init(), b.params.a11y && b.a11y && b.a11y.init(), b.emit("onInit", b)
                    }, b.cleanupStyles = function () {
                        b.container.removeClass(b.classNames.join(" ")).removeAttr("style"), b.wrapper.removeAttr("style"), b.slides && b.slides.length && b.slides.removeClass([b.params.slideVisibleClass, b.params.slideActiveClass, b.params.slideNextClass, b.params.slidePrevClass].join(" ")).removeAttr("style").removeAttr("data-swiper-column").removeAttr("data-swiper-row"), b.paginationContainer && b.paginationContainer.length && b.paginationContainer.removeClass(b.params.paginationHiddenClass), b.bullets && b.bullets.length && b.bullets.removeClass(b.params.bulletActiveClass), b.params.prevButton && a(b.params.prevButton).removeClass(b.params.buttonDisabledClass), b.params.nextButton && a(b.params.nextButton).removeClass(b.params.buttonDisabledClass), b.params.scrollbar && b.scrollbar && (b.scrollbar.track && b.scrollbar.track.length && b.scrollbar.track.removeAttr("style"), b.scrollbar.drag && b.scrollbar.drag.length && b.scrollbar.drag.removeAttr("style"))
                    }, b.destroy = function (e, a) {
                        b.detachEvents(), b.stopAutoplay(), b.params.scrollbar && b.scrollbar && b.params.scrollbarDraggable && b.scrollbar.disableDraggable(), b.params.loop && b.destroyLoop(), a && b.cleanupStyles(), b.disconnectObservers(), b.params.zoom && b.zoom && b.zoom.destroy(), b.params.keyboardControl && b.disableKeyboardControl && b.disableKeyboardControl(), b.params.mousewheelControl && b.disableMousewheelControl && b.disableMousewheelControl(), b.params.a11y && b.a11y && b.a11y.destroy(), b.params.history && !b.params.replaceState && window.removeEventListener("popstate", b.history.setHistoryPopState), b.params.hashnav && b.hashnav && b.hashnav.destroy(), b.emit("onDestroy"), e !== !1 && (b = null)
                    }, b.init(), b
                }
            };
            t.prototype = {
                isSafari: function () {
                    var e = navigator.userAgent.toLowerCase();
                    return e.indexOf("safari") >= 0 && e.indexOf("chrome") < 0 && e.indexOf("android") < 0
                }(),
                isUiWebView: /(iPhone|iPod|iPad).*AppleWebKit(?!.*Safari)/i.test(navigator.userAgent),
                isArray: function (e) {
                    return "[object Array]" === Object.prototype.toString.apply(e)
                },
                browser: {
                    ie: window.navigator.pointerEnabled || window.navigator.msPointerEnabled,
                    ieTouch: window.navigator.msPointerEnabled && window.navigator.msMaxTouchPoints > 1 || window.navigator.pointerEnabled && window.navigator.maxTouchPoints > 1,
                    lteIE9: function () {
                        var e = document.createElement("div");
                        return e.innerHTML = "<!--[if lte IE 9]><i></i><![endif]-->", 1 === e.getElementsByTagName("i").length
                    }()
                },
                device: function () {
                    var e = navigator.userAgent, a = e.match(/(Android);?[\s\/]+([\d.]+)?/), t = e.match(/(iPad).*OS\s([\d_]+)/), s = e.match(/(iPod)(.*OS\s([\d_]+))?/), r = !t && e.match(/(iPhone\sOS)\s([\d_]+)/);
                    return {ios: t || r || s, android: a}
                }(),
                support: {
                    touch: window.Modernizr && Modernizr.touch === !0 || function () {
                        return !!("ontouchstart"in window || window.DocumentTouch && document instanceof DocumentTouch)
                    }(), transforms3d: window.Modernizr && Modernizr.csstransforms3d === !0 || function () {
                        var e = document.createElement("div").style;
                        return "webkitPerspective"in e || "MozPerspective"in e || "OPerspective"in e || "MsPerspective"in e || "perspective"in e
                    }(), flexbox: function () {
                        for (var e = document.createElement("div").style, a = "alignItems webkitAlignItems webkitBoxAlign msFlexAlign mozBoxAlign webkitFlexDirection msFlexDirection mozBoxDirection mozBoxOrient webkitBoxDirection webkitBoxOrient".split(" "), t = 0; t < a.length; t++)if (a[t]in e)return !0
                    }(), observer: function () {
                        return "MutationObserver"in window || "WebkitMutationObserver"in window
                    }(), passiveListener: function () {
                        var e = !1;
                        try {
                            var a = Object.defineProperty({}, "passive", {
                                get: function () {
                                    e = !0
                                }
                            });
                            window.addEventListener("testPassiveListener", null, a)
                        } catch (e) {
                        }
                        return e
                    }(), gestures: function () {
                        return "ongesturestart"in window
                    }()
                },
                plugins: {}
            };
            for (var s = ["jQuery", "Zepto", "Dom7"], r = 0; r < s.length; r++)window[s[r]] && e(window[s[r]]);
            var i;
            i = "undefined" == typeof Dom7 ? window.Dom7 || window.Zepto || window.jQuery : Dom7, i && ("transitionEnd"in i.fn || (i.fn.transitionEnd = function (e) {
                function a(i) {
                    if (i.target === this)for (e.call(this, i), t = 0; t < s.length; t++)r.off(s[t], a)
                }

                var t, s = ["webkitTransitionEnd", "transitionend", "oTransitionEnd", "MSTransitionEnd", "msTransitionEnd"], r = this;
                if (e)for (t = 0; t < s.length; t++)r.on(s[t], a);
                return this
            }), "transform"in i.fn || (i.fn.transform = function (e) {
                for (var a = 0; a < this.length; a++) {
                    var t = this[a].style;
                    t.webkitTransform = t.MsTransform = t.msTransform = t.MozTransform = t.OTransform = t.transform = e
                }
                return this
            }), "transition"in i.fn || (i.fn.transition = function (e) {
                "string" != typeof e && (e += "ms");
                for (var a = 0; a < this.length; a++) {
                    var t = this[a].style;
                    t.webkitTransitionDuration = t.MsTransitionDuration = t.msTransitionDuration = t.MozTransitionDuration = t.OTransitionDuration = t.transitionDuration = e
                }
                return this
            }), "outerWidth"in i.fn || (i.fn.outerWidth = function (e) {
                return this.length > 0 ? e ? this[0].offsetWidth + parseFloat(this.css("margin-right")) + parseFloat(this.css("margin-left")) : this[0].offsetWidth : null
            })), window.Swiper = t
        }(), "undefined" != typeof module ? module.exports = window.Swiper : "function" == typeof define && define.amd && define([], function () {
            "use strict";
            return window.Swiper
        });
        //# sourceMappingURL=maps/swiper.jquery.min.js.map

    </script>
</head>

<body>
<section class="g-flexview">
<!-- Order Base Info -->
<#if orderInfo?has_content>
<section class="g-scrollview">
<article class="m-list list-theme4">
        <div class="list-img">
            <img style="width:80px;height:80px;" src="${orderInfo.detailImageUrl}?x-oss-process=image/resize,w_250,h_250/quality,q_60" data-url="${orderInfo.detailImageUrl}?x-oss-process=image/resize,w_250,h_250/quality,q_60">
        </div>
        <div class="list-mes">

            <h1 class="list-title">${orderInfo.productName}</h1>

            <div class="list-mes-item">
                <div>
                                <span class="list" style="font-size:14px;">${uiLabel.orderTime}<span
                                        style="font-size:14px;">&nbsp;${orderInfo.orderDate?string("yyyy-MM-dd")}</span></span><br/>
                    <span class="list" style="font-size:14px;">${uiLabel.shipmentAddress}<span
                            style="font-size:15px;">&nbsp;${(orderInfo.personAddressInfoMap.contactAddress)!}</span></span>

                </div>
                <div>
                    <#assign payStat = "${orderInfo.orderPayStatus}" />
                                ${payStat}<br/>
                <#--<a href="#"><img src="${orderInfo.personInfoMap.headPortrait}" /></a>-->
                </div>
            </div>
        </div>

</article>
</section>
</#if>


<!-- 快递信息 -->
<#if orderExpressInfo?has_content>
<div class="swiper-container">
<#--下拉可以刷新-->
    <div class="refreshtip"></div>
    <div class="swiper-wrapper w">
        <div class="swiper-slide d">
        <#--下拉可以刷新-->
            <div class="init-loading list-group-item text-center" style="display: none;background-color:#F5F5F5;border:1px solid #F5F5F5;">
            </div>
            <div class="swiper-container2">
                <div class="swiper-wrapper">
                    <div class="swiper-slide list-group">
                    <#--<div class="list-group-item">列表</div>-->
                    <#--<div class="list-group-item">列表</div>-->
                    <#--<div class="list-group-item">列表</div>-->
                    <#--<div class="list-group-item">列表</div>-->
                    <#--<div class="list-group-item">列表</div>-->
                    <#--<div class="list-group-item">列表</div>-->
                    <#--<div class="track-rcol">-->
                        <div class="track-list">
                            <ul>
                                <li class="first">
                                    <i class="node-icon"></i>
                                    <span class="time">2016-03-10 18:07:15</span>
                                    <span class="txt">感谢您在京东购物，欢迎您再次光临！</span>
                                </li>
                                <li>
                                    <i class="node-icon"></i>
                                    <span class="time">2016-03-10 18:07:15</span>
                                    <span class="txt">【京东到家】京东配送员【申国龙】已出发，联系电话【18410106883，感谢您的耐心等待，参加评价还能赢取京豆呦】</span>
                                </li>
                                <li>
                                    <i class="node-icon"></i>
                                    <span class="time">2016-03-10 18:07:15</span>
                                    <span class="txt">感谢您在京东购物，欢迎您再次光临！</span>
                                </li>
                                <li>
                                    <i class="node-icon"></i>
                                    <span class="time">2016-03-10 18:07:15</span>
                                    <span class="txt">感谢您在京东购物，欢迎您再次光临！</span>
                                </li>
                                <li>
                                    <i class="node-icon"></i>
                                    <span class="time">2016-03-10 18:07:15</span>
                                    <span class="txt">感谢您在京东购物，欢迎您再次光临！</span>
                                </li>
                                <li>
                                    <i class="node-icon"></i>
                                    <span class="time">2016-03-10 18:07:15</span>
                                    <span class="txt">感谢您在京东购物，欢迎您再次光临！</span>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>

<#--<div class="loadtip">上拉加载更多</div>-->
<div class="swiper-scrollbar"></div>
</div>
<input id="DownReFlash" type="hidden" value="${uiLabel.DownReFlash}" />

<input id="StopReFlash" type="hidden" value="${uiLabel.StopReFlash}" />

<input id="NowReFlash" type="hidden" value="${uiLabel.NowReFlash}" />

<input id="ReFlashSuccess" type="hidden" value="${uiLabel.ReFlashSuccess}" />

<script type="text/javascript">

    var DownReFlash = $("#DownReFlash").val()+"...";
    var StopReFlash = $("#StopReFlash").val()+"...";
    var NowReFlash = $("#NowReFlash").val()+"...";
    var ReFlashSuccess = $("#ReFlashSuccess").val();

    var loadFlag = true;
    var oi = 0;
    var mySwiper = new Swiper('.swiper-container', {
        direction: 'vertical',
        scrollbar: '.swiper-scrollbar',
        slidesPerView: 'auto',
        mousewheelControl: true,
        freeMode: true,
        onTouchMove: function (swiper) {		//手动滑动中触发
            var _viewHeight = document.getElementsByClassName('swiper-wrapper')[0].offsetHeight;
            var _contentHeight = document.getElementsByClassName('swiper-slide')[0].offsetHeight;


            if (mySwiper.translate < 50 && mySwiper.translate > 0) {
                $(".init-loading").html(DownReFlash).show();
            } else if (mySwiper.translate > 50) {
                $(".init-loading").html(StopReFlash).show();
            }
        },
        onTouchEnd: function (swiper) {
            var _viewHeight = document.getElementsByClassName('swiper-wrapper')[0].offsetHeight;
            var _contentHeight = document.getElementsByClassName('swiper-slide')[0].offsetHeight;
            // 上拉加载
//            if(mySwiper.translate <= _viewHeight - _contentHeight - 50 && mySwiper.translate < 0) {
//                // console.log("已经到达底部！");
//
//                if(loadFlag){
//                    $(".loadtip").html('正在加载...');
//                }else{
//                    $(".loadtip").html('没有更多啦！');
//                }
//
//                setTimeout(function() {
//                    for(var i = 0; i <5; i++) {
//                        oi++;
//                        $(".list-group").eq(mySwiper2.activeIndex).append('<li class="list-group-item">我是加载出来的'+oi+'...</li>');
//                    }
//                    $(".loadtip").html('上拉加载更多...');
//                    mySwiper.update(); // 重新计算高度;
//                }, 800);
//            }

            // 下拉刷新
            if (mySwiper.translate >= 50) {
                $(".init-loading").html(NowReFlash).show();
                $(".loadtip").html('上拉加载更多');
                loadFlag = true;

                setTimeout(function () {
                    $(".refreshtip").show(0);
                    $(".init-loading").html(ReFlashSuccess);
                    setTimeout(function () {
                        $(".init-loading").html('').hide();
                    }, 800);
                    $(".loadtip").show(0);

                    //刷新操作
                    mySwiper.update(); // 重新计算高度;
                }, 1000);
            } else if (mySwiper.translate >= 0 && mySwiper.translate < 50) {
                $(".init-loading").html('').hide();
            }
            return false;
        }
    });
    var mySwiper2 = new Swiper('.swiper-container2', {
        onTransitionEnd: function (swiper) {

            $('.w').css('transform', 'translate3d(0px, 0px, 0px)')
            $('.swiper-container2 .swiper-slide-active').css('height', 'auto').siblings('.swiper-slide').css('height', '0px');
            mySwiper.update();

            $('.tab a').eq(mySwiper2.activeIndex).addClass('active').siblings('a').removeClass('active');
        }

    });
    $('.tab a').click(function () {

        $(this).addClass('active').siblings('a').removeClass('active');
        mySwiper2.slideTo($(this).index(), 500, false)

        $('.w').css('transform', 'translate3d(0px, 0px, 0px)')
        $('.swiper-container2 .swiper-slide-active').css('height', 'auto').siblings('.swiper-slide').css('height', '0px');
        mySwiper.update();
    });
</script>
</#if>

<!-- 没找到快递信息-->
<#if !orderExpressInfo?has_content>
${uiLabel.ExpressInfoNotFound}
</#if>
</section>
</body>