<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>${(resourceDetail.firstName)!}${uiLabel.De}${(resourceDetail.productName)!}</title>
    <meta http-equiv="content-type" content="text/html;charset=utf-8" />
    <meta name="viewport" content="width=device-width,user-scalable=no,initial-scale=1,minimum-scale=1,maximum-scale=1" />
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <style>
      html {
            font-family: sans-serif;
            -webkit-text-size-adjust: 100%;
            -ms-text-size-adjust: 100%
        }

        body {
            margin: 0
        }

        article,aside,details,figcaption,figure,footer,header,hgroup,main,nav,section,summary {
            display: block
        }

        audio,canvas,progress,video {
            display: inline-block;
            vertical-align: baseline
        }

        audio:not([controls]) {
            display: none;
            height: 0
        }[hidden],template {
             display: none
         }

        a {
            background: 0 0
        }

        a:active,a:hover {
            outline: 0
        }

        abbr[title] {
            border-bottom: 1px dotted
        }

        b,strong {
            font-weight: 700
        }

        dfn {
            font-style: italic
        }

        h1 {
            margin: .67em 0
        }

        mark {
            color: #000;
            background: #ff0
        }

        small {
            font-size: 80%
        }

        sub,sup {
            position: relative;
            font-size: 75%;
            line-height: 0;
            vertical-align: baseline
        }

        sup {
            top: -.5em
        }

        sub {
            bottom: -.25em
        }

        img {
            border: 0
        }

        svg:not(:root) {
            overflow: hidden
        }

        figure {
            margin: 1em 40px
        }

        hr {
            height: 0;
            -moz-box-sizing: content-box;
            box-sizing: content-box
        }

        pre {
            overflow: auto
        }

        code,kbd,pre,samp {
            font-family: monospace,monospace;
            font-size: 1em
        }

        button,input,optgroup,select,textarea {
            margin: 0;
            font: inherit;
            color: inherit
        }

        button {
            overflow: visible
        }

        button,select {
            text-transform: none
        }

        button,html input[type=button],input[type=reset],input[type=submit] {
            -webkit-appearance: button;
            cursor: pointer
        }

        button[disabled],html input[disabled] {
            cursor: default
        }

        button::-moz-focus-inner,input::-moz-focus-inner {
            padding: 0;
            border: 0
        }

        input {
            line-height: normal
        }

        input[type=checkbox],input[type=radio] {
            box-sizing: border-box;
            padding: 0
        }

        input[type=number]::-webkit-inner-spin-button,input[type=number]::-webkit-outer-spin-button {
            height: auto
        }

        input[type=search]::-webkit-search-cancel-button,input[type=search]::-webkit-search-decoration {
            -webkit-appearance: none
        }

        fieldset {
            padding: .35em .625em .75em;
            margin: 0 2px;
            border: 1px solid silver
        }

        legend {
            padding: 0;
            border: 0
        }

        textarea {
            overflow: auto
        }

        optgroup {
            font-weight: 700
        }

        table {
            border-spacing: 0;
            border-collapse: collapse
        }

        td,th {
            padding: 0
        }

        * {
            -webkit-box-sizing: border-box;
            -moz-box-sizing: border-box;
            box-sizing: border-box
        }

        body {
            position: fixed;
            top: 0;
            right: 0;
            bottom: 0;
            left: 0;
            font-family: "Helvetica Neue",Helvetica,sans-serif;
            font-size: 17px;
            line-height: 21px;
            color: #000;
            background-color: #fff
        }

        a {
            color: #fff;
            text-decoration: none;
            -webkit-tap-highlight-color: transparent
        }

        a:active {
            color: #ccc
        }

        .content {
            position: absolute;
            top: 0;
            right: 0;
            bottom: 0;
            left: 0;
            overflow: auto;
            -webkit-overflow-scrolling: touch;
            background-color: #fff
        }

        .content>* {
            -webkit-transform: translateZ(0);
            -ms-transform: translateZ(0);
            transform: translateZ(0)
        }

        .bar-nav~.content {
            padding-top: 44px
        }

        .bar-header-secondary~.content {
            padding-top: 88px
        }

        .bar-footer~.content {
            padding-bottom: 44px
        }

        .bar-footer-secondary~.content {
            padding-bottom: 88px
        }

        .bar-tab~.content {
            padding-bottom: 50px
        }

        .bar-footer-secondary-tab~.content {
            padding-bottom: 94px
        }

        .content-padded {
            margin: 10px
        }

        .pull-left {
            float: left
        }

        .pull-right {
            float: right
        }

        .clearfix:after,.clearfix:before {
            display: table;
            content: " "
        }

        .clearfix:after {
            clear: both
        }

        h1,h2,h3,h4,h5,h6 {
            margin-top: 0;
            margin-bottom: 10px;
            line-height: 1
        }

        .h1,h1 {
            font-size: 36px
        }

        .h2,h2 {
            font-size: 30px
        }

        .h3,h3 {
            font-size: 24px
        }

        .h4,h4 {
            font-size: 18px
        }

        .h5,h5 {
            margin-top: 20px;
            font-size: 14px
        }

        .h6,h6 {
            margin-top: 20px;
            font-size: 12px
        }

        p {
            margin-top: 0;
            margin-bottom: 10px;
            font-size: 14px;
            color: #777
        }

        .btn {
            position: relative;
            display: inline-block;
            padding: 6px 8px 7px;
            margin-bottom: 0;
            font-size: 12px;
            font-weight: 400;
            line-height: 1;
            color: #333;
            text-align: center;
            white-space: nowrap;
            vertical-align: top;
            cursor: pointer;
            background-color: #fff;
            border: 1px solid #ccc;
            border-radius: 3px
        }

        .btn.active,.btn:active {
            color: inherit;
            background-color: #ccc
        }

        .btn.disabled,.btn:disabled {
            opacity: .6
        }

        .btn-primary {
            color: #fff;
            background-color: #428bca;
            border: 1px solid #428bca
        }

        .btn-primary.active,.btn-primary:active {
            color: #fff;
            background-color: #3071a9;
            border: 1px solid #3071a9
        }

        .btn-positive {
            color: #fff;
            background-color: #5cb85c;
            border: 1px solid #5cb85c
        }

        .btn-positive.active,.btn-positive:active {
            color: #fff;
            background-color: #449d44;
            border: 1px solid #449d44
        }

        .btn-negative {
            color: #fff;
            background-color: #d9534f;
            border: 1px solid #d9534f
        }

        .btn-negative.active,.btn-negative:active {
            color: #fff;
            background-color: #c9302c;
            border: 1px solid #c9302c
        }

        .btn-outlined {
            background-color: transparent
        }

        .btn-outlined.btn-primary {
            color: #428bca
        }

        .btn-outlined.btn-positive {
            color: #5cb85c
        }

        .btn-outlined.btn-negative {
            color: #d9534f
        }

        .btn-outlined.btn-negative:active,.btn-outlined.btn-positive:active,.btn-outlined.btn-primary:active {
            color: #fff
        }

        .btn-link {
            padding-top: 6px;
            padding-bottom: 6px;
            color: #428bca;
            background-color: transparent;
            border: 0
        }

        .btn-link.active,.btn-link:active {
            color: #3071a9;
            background-color: transparent
        }

        .btn-block {
            display: block;
            width: 100%;
            padding: 15px 0;
            margin-bottom: 10px;
            font-size: 18px
        }

        input[type=button],input[type=reset],input[type=submit] {
            width: 100%
        }

        .btn .badge {
            margin: -2px -4px -2px 4px;
            font-size: 12px;
            background-color: rgba(0,0,0,.15)
        }

        .btn .badge-inverted,.btn:active .badge-inverted {
            background-color: transparent
        }

        .btn-negative:active .badge-inverted,.btn-positive:active .badge-inverted,.btn-primary:active .badge-inverted {
            color: #fff
        }

        .btn-block .badge {
            position: absolute;
            right: 0;
            margin-right: 10px
        }

        .btn .icon {
            font-size: inherit
        }

        .bar {
            position: fixed;
            right: 0;
            left: 0;
            z-index: 10;
            height: 47px;
            padding-right: 10px;
            padding-left: 10px;
            background-color: #fff;
            border-bottom: 1px solid #ddd;
            -webkit-backface-visibility: hidden;
            backface-visibility: hidden
        }

        .bar-header-secondary {
            top: 44px
        }

        .bar-footer {
            bottom: 0
        }

        .bar-footer-secondary {
            bottom: 44px
        }

        .bar-footer-secondary-tab {
            bottom: 50px
        }

        .bar-footer,.bar-footer-secondary,.bar-footer-secondary-tab {
            border-top: 1px solid #ddd;
            border-bottom: 0
        }

        .bar-nav {
            top: 0
        }

        .title {
            position: absolute;
            display: block;
            width: 100%;
            padding: 0;
            margin: 0 -10px;
            font-size: 18px;
            font-weight: 500;
            line-height: 47px;
            color: #fff;
            text-align: center;
            white-space: nowrap
        }

        .title a {
            color: inherit
        }

        .bar-tab {
            bottom: 0;
            display: table;
            width: 100%;
            height: 50px;
            padding: 0;
            table-layout: fixed;
            border-top: 1px solid #ddd;
            border-bottom: 0
        }

        .bar-tab .tab-item {
            display: table-cell;
            width: 1%;
            height: 50px;
            color: #929292;
            text-align: center;
            vertical-align: middle
        }

        .bar-tab .tab-item.active,.bar-tab .tab-item:active {
            color: #428bca
        }

        .bar-tab .tab-item .icon {
            top: 3px;
            width: 24px;
            height: 24px;
            padding-top: 0;
            padding-bottom: 0
        }

        .bar-tab .tab-item .icon~.tab-label {
            display: block;
            font-size: 11px
        }

        .bar .btn {
            position: relative;
            top: 7px;
            z-index: 20;
            padding: 6px 12px 7px;
            margin-top: 0;
            font-weight: 400
        }

        .bar .btn.pull-right {
            margin-left: 10px
        }

        .bar .btn.pull-left {
            margin-right: 10px
        }

        .bar .btn-link {
            top: 0;
            padding: 0;
            font-size: 16px;
            line-height: 44px;
            color: #428bca;
            border: 0
        }

        .bar .btn-link.active,.bar .btn-link:active {
            color: #3071a9
        }

        .bar .btn-block {
            top: 6px;
            padding: 7px 0;
            margin-bottom: 0;
            font-size: 16px
        }

        .bar .btn-nav.pull-left {
            margin-left: -5px
        }

        .bar .btn-nav.pull-left .icon-left-nav {
            margin-right: -3px
        }

        .bar .btn-nav.pull-right {
            margin-right: -5px
        }

        .bar .btn-nav.pull-right .icon-right-nav {
            margin-left: -3px
        }

        .bar .icon {
            position: relative;
            z-index: 20;
            padding-top: 10px;
            padding-bottom: 10px;
            font-size: 24px
        }

        .bar .btn .icon {
            top: 3px;
            padding: 0
        }

        .bar .title .icon {
            padding: 0
        }

        .bar .title .icon.icon-caret {
            top: 4px;
            margin-left: -5px
        }

        .bar input[type=search] {
            height: 29px;
            margin: 6px 0
        }

        .bar .segmented-control {
            top: 7px;
            margin: 0 auto
        }

        .badge {
            display: inline-block;
            padding: 2px 9px 3px;
            font-size: 12px;
            line-height: 1;
            color: #333;
            background-color: rgba(0,0,0,.15);
            border-radius: 100px
        }

        .badge.badge-inverted {
            padding: 0 5px 0 0;
            background-color: transparent
        }

        .badge-primary {
            color: #fff;
            background-color: #428bca
        }

        .badge-primary.badge-inverted {
            color: #428bca
        }

        .badge-positive {
            color: #fff;
            background-color: #5cb85c
        }

        .badge-positive.badge-inverted {
            color: #5cb85c
        }

        .badge-negative {
            color: #fff;
            background-color: #d9534f
        }

        .badge-negative.badge-inverted {
            color: #d9534f
        }

        .card {
            margin: 10px;
            overflow: hidden;
            background-color: #fff;
            border: 1px solid #ddd;
            border-radius: 6px
        }

        .card .table-view {
            margin-bottom: 0;
            border-top: 0;
            border-bottom: 0
        }

        .card .table-view .table-view-divider:first-child {
            top: 0;
            border-top-left-radius: 6px;
            border-top-right-radius: 6px
        }

        .card .table-view .table-view-divider:last-child {
            border-bottom-right-radius: 6px;
            border-bottom-left-radius: 6px
        }

        .card .table-view-cell:last-child {
            border-bottom: 0
        }

        .table-view {
            padding-left: 0;
            margin-top: 0;
            margin-bottom: 15px;
            list-style: none;
            background-color: #fff;
            border-top: 1px solid #ddd;
            border-bottom: 1px solid #ddd
        }

        .table-view-cell {
            position: relative;
            padding: 11px 65px 11px 15px;
            overflow: hidden;
            border-bottom: 1px solid #ddd
        }

        .table-view-cell:last-child {
            border-bottom: 0
        }

        .table-view-cell>a:not(.btn) {
            position: relative;
            display: block;
            padding: inherit;
            margin: -15px -65px -11px -15px;
            overflow: hidden;
            color: inherit
        }

        .table-view-cell>a:not(.btn):active {
            background-color: #eee
        }

        .table-view-cell p {
            margin-bottom: 0
        }

        .table-view-divider {
            padding-top: 6px;
            padding-bottom: 6px;
            padding-left: 15px;
            margin-top: -1px;
            margin-left: 0;
            font-weight: 500;
            color: #999;
            background-color: #fafafa;
            border-top: 1px solid #ddd;
            border-bottom: 1px solid #ddd
        }

        .table-view .media,.table-view .media-body {
            overflow: hidden
        }

        .table-view .media-object.pull-left {
            margin-right: 10px
        }

        .table-view .media-object.pull-right {
            margin-left: 10px
        }

        .table-view-cell>.badge,.table-view-cell>.btn,.table-view-cell>.toggle,.table-view-cell>a>.badge,.table-view-cell>a>.btn,.table-view-cell>a>.toggle {
            position: absolute;
            top: 50%;
            right: 15px;
            -webkit-transform: translateY(-50%);
            -ms-transform: translateY(-50%);
            transform: translateY(-50%)
        }

        .table-view-cell .navigate-left>.badge,.table-view-cell .navigate-left>.btn,.table-view-cell .navigate-left>.toggle,.table-view-cell .navigate-right>.badge,.table-view-cell .navigate-right>.btn,.table-view-cell .navigate-right>.toggle,.table-view-cell .push-left>.badge,.table-view-cell .push-left>.btn,.table-view-cell .push-left>.toggle,.table-view-cell .push-right>.badge,.table-view-cell .push-right>.btn,.table-view-cell .push-right>.toggle,.table-view-cell>a .navigate-left>.badge,.table-view-cell>a .navigate-left>.btn,.table-view-cell>a .navigate-left>.toggle,.table-view-cell>a .navigate-right>.badge,.table-view-cell>a .navigate-right>.btn,.table-view-cell>a .navigate-right>.toggle,.table-view-cell>a .push-left>.badge,.table-view-cell>a .push-left>.btn,.table-view-cell>a .push-left>.toggle,.table-view-cell>a .push-right>.badge,.table-view-cell>a .push-right>.btn,.table-view-cell>a .push-right>.toggle {
            right: 35px
        }

        .content>.table-view:first-child {
            margin-top: 15px
        }

        button,input,select,textarea {
            font-family: "Helvetica Neue",Helvetica,sans-serif;
            font-size: 17px
        }

        input[type=color],input[type=date],input[type=datetime-local],input[type=datetime],input[type=email],input[type=month],input[type=number],input[type=password],input[type=search],input[type=tel],input[type=text],input[type=time],input[type=url],input[type=week],select,textarea {
            width: 100%;
            height: 35px;
            -webkit-appearance: none;
            padding: 0 15px;
            margin-bottom: 15px;
            line-height: 21px;
            background-color: #fff;
            border: 1px solid #ddd;
            border-radius: 3px;
            outline: 0
        }

        input[type=search] {
            -webkit-box-sizing: border-box;
            -moz-box-sizing: border-box;
            box-sizing: border-box;
            padding: 0 10px;
            font-size: 16px;
            border-radius: 20px
        }

        input[type=search]:focus {
            text-align: left
        }

        textarea {
            height: auto
        }

        select {
            height: auto;
            font-size: 14px;
            background-color: #f8f8f8;
            -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.1);
            box-shadow: inset 0 1px 1px rgba(0,0,0,.1)
        }

        .input-group {
            background-color: #fff
        }

        .input-group input,.input-group textarea {
            margin-bottom: 0;
            background-color: transparent;
            border-top: 0;
            border-right: 0;
            border-left: 0;
            border-radius: 0;
            -webkit-box-shadow: none;
            box-shadow: none
        }

        .input-row {
            height: 35px;
            overflow: hidden;
            border-bottom: 1px solid #ddd
        }

        .input-row label {
            float: left;
            width: 35%;
            padding: 8px 15px;
            font-family: "Helvetica Neue",Helvetica,sans-serif;
            line-height: 1.1
        }

        .input-row input {
            float: right;
            width: 65%;
            padding-left: 0;
            margin-bottom: 0;
            border: 0
        }

        .segmented-control {
            position: relative;
            display: table;
            overflow: hidden;
            font-size: 12px;
            font-weight: 400;
            background-color: #fff;
            border: 1px solid #ccc;
            border-radius: 3px
        }

        .segmented-control .control-item {
            display: table-cell;
            width: 1%;
            padding-top: 6px;
            padding-bottom: 7px;
            overflow: hidden;
            line-height: 1;
            color: #333;
            text-align: center;
            text-overflow: ellipsis;
            white-space: nowrap;
            border-left: 1px solid #ccc
        }

        .segmented-control .control-item:first-child {
            border-left-width: 0
        }

        .segmented-control .control-item:active {
            background-color: #eee
        }

        .segmented-control .control-item.active {
            background-color: #ccc
        }

        .segmented-control-primary {
            border-color: #428bca
        }

        .segmented-control-primary .control-item {
            color: #428bca;
            border-color: inherit
        }

        .segmented-control-primary .control-item:active {
            background-color: #cde1f1
        }

        .segmented-control-primary .control-item.active {
            color: #fff;
            background-color: #428bca
        }

        .segmented-control-positive {
            border-color: #5cb85c
        }

        .segmented-control-positive .control-item {
            color: #5cb85c;
            border-color: inherit
        }

        .segmented-control-positive .control-item:active {
            background-color: #d8eed8
        }

        .segmented-control-positive .control-item.active {
            color: #fff;
            background-color: #5cb85c
        }

        .segmented-control-negative {
            border-color: #d9534f
        }

        .segmented-control-negative .control-item {
            color: #d9534f;
            border-color: inherit
        }

        .segmented-control-negative .control-item:active {
            background-color: #f9e2e2
        }

        .segmented-control-negative .control-item.active {
            color: #fff;
            background-color: #d9534f
        }

        .control-content {
            display: none
        }

        .control-content.active {
            display: block
        }

        .popover {
            position: fixed;
            top: 55px;
            left: 50%;
            z-index: 20;
            display: none;
            width: 280px;
            margin-left: -140px;
            background-color: #fff;
            border-radius: 6px;
            -webkit-box-shadow: 0 0 15px rgba(0,0,0,.1);
            box-shadow: 0 0 15px rgba(0,0,0,.1);
            opacity: 0;
            -webkit-transition: all .25s linear;
            -moz-transition: all .25s linear;
            transition: all .25s linear;
            -webkit-transform: translate3d(0,-15px,0);
            -ms-transform: translate3d(0,-15px,0);
            transform: translate3d(0,-15px,0)
        }

        .popover:before {
            position: absolute;
            top: -15px;
            left: 50%;
            width: 0;
            height: 0;
            margin-left: -15px;
            content: '';
            border-right: 15px solid transparent;
            border-bottom: 15px solid #fff;
            border-left: 15px solid transparent
        }

        .popover.visible {
            opacity: 1;
            -webkit-transform: translate3d(0,0,0);
            -ms-transform: translate3d(0,0,0);
            transform: translate3d(0,0,0)
        }

        .popover .bar~.table-view {
            padding-top: 44px
        }

        .backdrop {
            position: fixed;
            top: 0;
            right: 0;
            bottom: 0;
            left: 0;
            z-index: 15;
            background-color: rgba(0,0,0,.3)
        }

        .popover .btn-block {
            margin-bottom: 5px
        }

        .popover .btn-block:last-child {
            margin-bottom: 0
        }

        .popover .bar-nav {
            border-bottom: 1px solid #ddd;
            border-top-left-radius: 12px;
            border-top-right-radius: 12px;
            -webkit-box-shadow: none;
            box-shadow: none
        }

        .popover .table-view {
            max-height: 300px;
            margin-bottom: 0;
            overflow: auto;
            -webkit-overflow-scrolling: touch;
            background-color: #fff;
            border-top: 0;
            border-bottom: 0;
            border-radius: 6px
        }

        .modal {
            position: fixed;
            top: 0;
            z-index: 11;
            width: 100%;
            min-height: 100%;
            overflow: hidden;
            background-color: #fff;
            opacity: 0;
            -webkit-transition: -webkit-transform .25s,opacity 1ms .25s;
            -moz-transition: -moz-transform .25s,opacity 1ms .25s;
            transition: transform .25s,opacity 1ms .25s;
            -webkit-transform: translate3d(0,100%,0);
            -ms-transform: translate3d(0,100%,0);
            transform: translate3d(0,100%,0)
        }

        .modal.active {
            height: 100%;
            opacity: 1;
            -webkit-transition: -webkit-transform .25s;
            -moz-transition: -moz-transform .25s;
            transition: transform .25s;
            -webkit-transform: translate3d(0,0,0);
            -ms-transform: translate3d(0,0,0);
            transform: translate3d(0,0,0)
        }

        .slider {
            width: 100%;
            overflow: hidden;
            background-color: #000
        }

        .slider .slide-group {
            position: relative;
            font-size: 0;
            white-space: nowrap;
            -webkit-transition: all 0s linear;
            -moz-transition: all 0s linear;
            transition: all 0s linear
        }

        .slider .slide-group .slide {
            display: inline-block;
            width: 100%;
            height: 100%;
            font-size: 14px;
            vertical-align: top
        }

        .toggle {
            position: relative;
            display: block;
            width: 74px;
            height: 30px;
            background-color: #fff;
            border: 2px solid #ddd;
            border-radius: 20px;
            -webkit-transition-duration: .2s;
            -moz-transition-duration: .2s;
            transition-duration: .2s;
            -webkit-transition-property: background-color,border;
            -moz-transition-property: background-color,border;
            transition-property: background-color,border
        }

        .toggle .toggle-handle {
            position: absolute;
            top: -1px;
            left: -1px;
            z-index: 2;
            width: 28px;
            height: 28px;
            background-color: #fff;
            border: 1px solid #ddd;
            border-radius: 100px;
            -webkit-transition-duration: .2s;
            -moz-transition-duration: .2s;
            transition-duration: .2s;
            -webkit-transition-property: -webkit-transform,border,width;
            -moz-transition-property: -moz-transform,border,width;
            transition-property: transform,border,width
        }

        .toggle:before {
            position: absolute;
            top: 3px;
            right: 11px;
            font-size: 13px;
            color: #999;
            text-transform: uppercase;
            content: "Off"
        }

        .toggle.active {
            background-color: #5cb85c;
            border: 2px solid #5cb85c
        }

        .toggle.active .toggle-handle {
            border-color: #5cb85c;
            -webkit-transform: translate3d(44px,0,0);
            -ms-transform: translate3d(44px,0,0);
            transform: translate3d(44px,0,0)
        }

        .toggle.active:before {
            right: auto;
            left: 15px;
            color: #fff;
            content: "On"
        }

        .toggle input[type=checkbox] {
            display: none
        }

        .content.fade {
            left: 0;
            opacity: 0
        }

        .content.fade.in {
            opacity: 1
        }

        .content.sliding {
            z-index: 2;
            -webkit-transition: -webkit-transform .4s;
            -moz-transition: -moz-transform .4s;
            transition: transform .4s;
            -webkit-transform: translate3d(0,0,0);
            -ms-transform: translate3d(0,0,0);
            transform: translate3d(0,0,0)
        }

        .content.sliding.left {
            z-index: 1;
            -webkit-transform: translate3d(-100%,0,0);
            -ms-transform: translate3d(-100%,0,0);
            transform: translate3d(-100%,0,0)
        }

        .content.sliding.right {
            z-index: 3;
            -webkit-transform: translate3d(100%,0,0);
            -ms-transform: translate3d(100%,0,0);
            transform: translate3d(100%,0,0)
        }

        .navigate-left:after,.navigate-right:after,.push-left:after,.push-right:after {
            position: absolute;
            top: 50%;
            display: inline-block;
            font-family: Ratchicons;
            font-size: inherit;
            line-height: 1;
            color: #bbb;
            text-decoration: none;
            -webkit-transform: translateY(-50%);
            -ms-transform: translateY(-50%);
            transform: translateY(-50%);
            -webkit-font-smoothing: antialiased
        }

        .navigate-left:after,.push-left:after {
            left: 15px;
            content: '\e822'
        }

        .navigate-right:after,.push-right:after {
            right: 15px;
            content: '\e826'
        }

        @font-face {
            font-family:Ratchicons;font-style:normal;font-weight:400;src:url(../js/fonts/ratchicons.eot);src:url(../js/fonts/ratchicons.eot?#iefix) format("embedded-opentype"),url(../js/fonts/ratchicons.woff) format("woff"),url(../fonts/ratchicons.ttf) format("truetype"),url(../fonts/ratchicons.svg#svgFontName) format("svg")
        }

        .icon {
            display: inline-block;
            font-family: Ratchicons;
            font-size: 24px;
            line-height: 1;
            text-decoration: none;
            -webkit-font-smoothing: antialiased
        }

        .icon-back:before {
            content: '\e80a'
        }

        .icon-bars:before {
            content: '\e80e'
        }

        .icon-caret:before {
            content: '\e80f'
        }

        .icon-check:before {
            content: '\e810'
        }

        .icon-close:before {
            content: '\e811'
        }

        .icon-code:before {
            content: '\e812'
        }

        .icon-compose:before {
            content: '\e813'
        }

        .icon-download:before {
            content: '\e815'
        }

        .icon-edit:before {
            content: '\e829'
        }

        .icon-forward:before {
            content: '\e82a'
        }

        .icon-gear:before {
            content: '\e821'
        }

        .icon-home:before {
            content: '\e82b'
        }

        .icon-info:before {
            content: '\e82c'
        }

        .icon-list:before {
            content: '\e823'
        }

        .icon-more-vertical:before {
            content: '\e82e'
        }

        .icon-more:before {
            content: '\e82f'
        }

        .icon-pages:before {
            content: '\e824'
        }

        .icon-pause:before {
            content: '\e830'
        }

        .icon-person:before {
            content: '\e832'
        }

        .icon-play:before {
            content: '\e816'
        }

        .icon-plus:before {
            content: '\e817'
        }

        .icon-refresh:before {
            content: '\e825'
        }

        .icon-search:before {
            content: '\e819'
        }

        .icon-share:before {
            content: '\e81a'
        }

        .icon-sound:before {
            content: '\e827'
        }

        .icon-sound2:before {
            content: '\e828'
        }

        .icon-sound3:before {
            content: '\e80b'
        }

        .icon-sound4:before {
            content: '\e80c'
        }

        .icon-star-filled:before {
            content: '\e81b'
        }

        .icon-star:before {
            content: '\e81c'
        }

        .icon-stop:before {
            content: '\e81d'
        }

        .icon-trash:before {
            content: '\e81e'
        }

        .icon-up-nav:before {
            content: '\e81f'
        }

        .icon-up:before {
            content: '\e80d'
        }

        .icon-right-nav:before {
            content: '\e818'
        }

        .icon-right:before {
            content: '\e826'
        }

        .icon-down-nav:before {
            content: '\e814'
        }

        .icon-down:before {
            content: '\e820'
        }

        .icon-left-nav:before {
            content: '\e82d'
        }

        .icon-left:before {
            content: '\e822'
        }
        html, body {
            position: relative;
            height: 100%;
        }
        body {
            background: #eee;
            font-family: "Microsoft YaHei",Arial,Helvetica,sans-serif,"宋体";
            font-size: 14px;
            color:#000;
            margin: 0;
            padding: 0;

        }
        .swiper-container {
            width: 100%;
            height: 100%;
        }
        .swiper-slide {
            text-align: center;
            font-size: 18px;
            background: #fff;

            /* Center slide text vertically */
            display: -webkit-box;
            display: -ms-flexbox;
            display: -webkit-flex;
            display: flex;
            -webkit-box-pack: center;
            -ms-flex-pack: center;
            -webkit-justify-content: center;
            justify-content: center;
            -webkit-box-align: center;
            -ms-flex-align: center;
            -webkit-align-items: center;
            align-items: center;
        }
        .poster{ width: 100%; height: 56px; background-size: 100%; background-color: hsla(0,0%,19%,1.00); }
        .item{
            width:100%;
            background: #fff;
            margin-top:5px;

        }
        .item a{
            display: block;
            height: 80%;
            width: 100%;

            position: absolute;
            text-align: center;
            font-size: 14px;
        }
        .item .img{
            background-repeat: no-repeat;
            background-position: center;
            background-size: auto 38px;
            height: 38px;
            width: 54px;
            margin: 31px auto auto;
        }
        .item span{
            display: inline-block;
            margin-top: 15px;
            box-sizing: border-box;
            color:#333;
        }
        .charset{
            float: left;
            width: 25%;
            height: 80%;
            min-width: 77px;
            min-height: 115px;

            display: table-cell;

            position: relative;


        }
        .t_img{
            float: left;
            padding-top: 5px;
        }
        .dd_w{
            float: left;
            margin-left:10px;
        }
        .one_text{
            font-size: 15px;
            color: #333;

            height:38px;
            line-height:38px;
            overflow:hidden;
        }
        .two_text{
            font-size: 12px;
        }
        .t_post{
            float: right;
            margin-top:10px;

        }
        .b_wdith{
            position: relative;

        }
        #yours{
            width: 100%;
            height: 47px;}
        .img2{
            width: 26px;
            height: 26px;
            background:url(../images/balance.png);
            background-size: 100%;
            float: left;
            margin-top:7px;

        }
        .tbim2{
            background:url(../images/card.png) 0 0 no-repeat;
            width: 26px;
            height: 26px;

            background-size: 100%;
            float: left;
            margin-top:7px;
        }
        .tbim3{
            background:url(../images/cut.png)0 0 no-repeat;
            width: 26px;
            height: 26px;

            background-size: 100%;
            float: left;
            margin-top:9px;
        }
        .tbim4{
            background:url(../images/transaction.png)0 0 no-repeat;
            width: 26px;
            height: 26px;

            background-size: 100%;
            float: left;
            margin-top:7px;
        }

        .justt{



            height: 16px;
            position: absolute;
            top: 20px;
            right: 15px;
            background-image: url(../images/icon.png);
            background-repeat: no-repeat;
            background-position: -419px 0;
            display: block;
            width: 14px;
            background-size: 450px auto;
            transition: 250ms;


        }
        #entrance{
            font-size: 14px;
            height: 30px;
            color:#666;

        }
        .tisco{
            color: #3da1f6;
        }
        .banq{
            color:#ccc;
            margin-top: 5px;;
            position: absolute;
            left: 50%;
            bottom:0%;
            margin-left: -50px;
            margin-top:-50px;



        }
        .imgban{
            width: 640px;
            height: 335px;
        }
        .imgban img{
            width: 100%;
            height: 375px;
        }
        body{
            font-family: "Microsoft YaHei" ! important;
        }.bar-nav{
             background:#fc9f4a;
             padding-bottom: 2px;
         }
        .title{
            background:#fc9f4a;

        }
        .tioimg{
            float: left;
            display: block;
            cursor: pointer;
            text-align: center;
            background-image: url(http://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/back.png);
            position: absolute;
            top:15px;
            left:5px;
            width: 30px;
            height: 30px;
            background-repeat: no-repeat;

        }
        .seek{


            width: 100%;
            background: #fff;

            height: 60px;

        }
        .dw{
            float: left;
            margin-top: 8px;

            width: 35%;
            height: 42px;
            border-radius: 5px;
            background:#eeeeee;
        }
        .p_i{
            width: 35%;
            padding-left: 7px;
            padding-top: 6px;
            float: left;
        }
        .cs{
            width: 50%;
            float: left;
            font-size: 16px;
            line-height:42px;
        }
        .btn{
            width: 19%;
            float: left;
            margin-top: 8px;
            margin-left: 6px;
            height: 42px;

        }
        .rongq{
            width: 91%;

            margin:0 auto;

            height: 60px;
        }
        .ch{
            width: 97%;
            margin:0 auto;

        }
        .ipo{
            min-height: 7.186667rem;
            height: auto;

        }
        .ipo2{
            min-height: 1039px;
            height: auto;
            background: #fff;
        }
        /* .lef{
         float: left;
         width: 5.0rem;
         height: 6.253333rem;



        }
         .lef img,.lef2 img{
             width: 100%;
             height: 100%;
         }
           .lef2{
             float: left;
             width: 5.0rem;
             height: 3.12rem;
         }
         */
        .leftc{
            width: 50%;
            float: left;
            height: 468px;
            overflow: hidden;
        }
        .leftc img{
            width: 100%;
            min-height: 468px;


        }
        .rightc{
            width: 50%;
            float: left;


        }
        .rightc p{
            float: left;
            margin-bottom: -5px;
        }
        .rightc img{
            width: 100%;
            min-height: 234px;
        }

        .table-view{
            margin-bottom: 0px;
        }
        .rig{
            float: right;
        }
        .maqio{
            width: 100%;
            float: left;
            /*height: 500px;*/
        }
        .maqio .rqo{
            float: left;
            width: 4.533333rem;
            /*height: 6.533333rem;*/
            margin-right: 0.266667rem;

        }
        .zhengc div:last-child{
            margin-right: 0;
        }
        .dhh{
            padding: 0.133333rem;
            width:4.533333rem;
            /*height: 4.906667rem;*/
            overflow: hidden;
            border:1px solid #ededed;
        }


        .dhh img{
            width: 4.266667rem;
            height: 4.266667rem;
        }
        .dhh  span{
            display: block;
            padding-left:5px;
            line-height: 0.533333rem;
        }
        .dhh .font1{font-size: 0.32rem}
        .dhh .font2{font-size: 0.266667rem;margin-right: 0.2rem;}
        .dhh .price{
            font-size: 0.373333rem;
        }
        .zhengc{
            margin:0 auto;text-align:center;width: 9.333333rem;overflow: hidden;
        }
        .xunf img{
            width: 1.866667rem;
            height: 0.8rem;
            position: fixed;
            top: 85%;
            right: 0;
            /*-moz-transform: translate(-50%, -50%);*/
            /*-ms-transform: translate(-50%, -50%);*/
            /*-webkit-transform: translate(-50%, -50%);*/
            /*transform: translate(-50%, -50%);*/
            z-index: 10000;
            cursor: pointer;


        }
        /*   @media screen and (max-width:480px){
           .zhengc {
          width:92%;
        } */


        @media screen and (max-width:320px){

            .leftc{
                width: 100%;
            }
            .rightc{
                width: 100%;
            }
        }
        .oi_ds{

        }
        .oi_ds img{
            border-radius: 24px;
        }
        a,body {
            color: #fff
        }

        .ui-error,.ui-loading {
            text-align: center
        }

        blockquote,body,dd,dir,dl,fieldset,figure,h1,h2,h3,h4,h5,h6,hr,input[type=radio],input[type=checkbox],input[type=range],listing,menu,ol,p,plaintext,pre,ul,xmp {
            margin: 0
        }

        button,dir,fieldset,input,input[type=radio],input[type=reset],input[type=checkbox],input[type=range],input[type=password],input[type=search],input[type=hidden],input[type=image],input[type=file],input[type=button],input[type=submit],isindex,legend,menu,ol,textarea,ul {
            padding: 0
        }

        h1,h2,h3,h4,h5,h6 {
            font-size: 100%;
            font-weight: 400
        }

        address,cite,dfn,em,i,var {
            font-style: normal
        }

        a:-webkit-any-link {
            text-decoration: none
        }

        table {
            border-collapse: collapse;
            border-spacing: 0
        }

        a img,fieldset,iframe {
            border: none
        }

        ol,ul {
            list-style: none
        }

        button,input,select,textarea {
            font-family: inherit;
            font-weight: inherit;
            font-size: inherit;
            margin: 0
        }

        button,input,select {
            color: inherit
        }

        html {
            -webkit-text-size-adjust: none;
            font-size: 100px
        }

        body {
            font: .12rem/1.5 helvetica
        }

        a {
            text-decoration: none
        }

        .ui-error {
            padding: 1em 0
        }

        @-webkit-keyframes rotate {
            from {
                -webkit-transform: rotate(0)
            }

            to {
                -webkit-transform: rotate(360deg)
            }
        }

        .ui-img-loading,.ui-loading i {
            -webkit-animation: rotate 1.2s linear infinite;
            background: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAeCAYAAAA7MK6iAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2ZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDoxNzk1ODUwQzlEMDhFMzExOTFBMkJBQUFCMkFDODIzRSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo0RjIzMTM3QTMzM0MxMUUzODU3MDg3MTczRUU5MUI2OCIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo0RjIzMTM3OTMzM0MxMUUzODU3MDg3MTczRUU5MUI2OCIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M2IChXaW5kb3dzKSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjFBOTU4NTBDOUQwOEUzMTE5MUEyQkFBQUIyQUM4MjNFIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjE3OTU4NTBDOUQwOEUzMTE5MUEyQkFBQUIyQUM4MjNFIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+SXvjNwAAAvpJREFUeNqsl0toU0EUhm80baJWS7Wttj6qqIuCohhdKaQouBARVCgiCOJSV1KXblUEN+rCB/haCC4UdeFG1LrwLUERFB+oC0vF+G5o1TRp/A/8A9PjzM29sQc+bu4kM//MmXPOTBKVSiWIarlcznxcBPaCZeArOA+OgbJ8mclkqo6VdAxazeaBC6CB721gD2gE+6IOkjAr9gjXg6Jq2w+2On5bAsvBj2qi4pGko3082AE2clVvwRHwxFqxz3sdFE6ALrAQvAG3wag9HecYYCfYZrlyPjjEQcQ+eIRHQB/73QC3wAk+e7kVXuFJXKm2OrCFn0873C92kYF2AKxW32XBwTDh6XbAKWvj8yXYDl7z/Rc4wygX6/b073ZGNe0TGOYKtX20Pt8Da+mhPwwsYxM9whPCVjwILjk6/WauBo7fl1TbTY/wqPakI42OgyGwiQHxChwF7+x0CCkuPWAFaFee3O1zdYJuEtGzpBaT9FvCOOhkTJwDeXuSRngNo1lWWABXwPWgdvvCFAwtmSs5O2OTmccl5mCk2htiU0CzVQVlUgMivMHTYZ0RjmJ6ctxzWcRMqznF94RE9TTPWK3B/1uLp71ZhPs9X/aPgXCdp71ehK/qAk67PAbCw572ouyxbMZhsBnMAJ8p+tB1VscMNBlrlivqTTrlSE0Wcoko8MRq+SeqZQWq41SwiinwDdzlzONamq4ukCDskJAI38WwN2exVKFTnHkUa2XFSjN25HB5oe9jWni9JWpffySnT0a4m0nlW8rya8pwO5/Pwk6nDs+As9V7A7dCT3yOJarP8nSYq4ccKzbtZvWy/wtMWoD7PMECPbiyFI9X54ofeTo95rPLEjUTyVoeKXj6l3l2e119h7eLEb5XmM+9PDLnegbu5PM9bySuo7IU5moRvMbraBP4bs00VSV1Aoo+YDY08b1PXZucwvaVZlC1DXAg1wTy6pr0PNJfmGplkClU5jZk1RbJvj6NW12SMX8v/wp+gsW8NeYpWoxbx/8KMAC97sC/2v4BrgAAAABJRU5ErkJggg==) 50% 50% no-repeat;min-width: 30px;
            min-height: 30px
        }

        .ui-loading i {
            height: 50px;
            width: 50px;
            display: inline-block
        }

        #content,html .mui-cover>header {
            overflow: hidden
        }

        .mui-flex {
            display: -webkit-box;
            display: -webkit-flex;
            display: -ms-flexbox;
            display: flex;
            -webkit-flex-wrap: wrap;
            -ms-flex-wrap: wrap;
            flex-wrap: wrap
        }

        .mui-flex,.mui-flex *,.mui-flex:after,.mui-flex:before {
            box-sizing: border-box
        }

        .mui-flex.vertical {
            -webkit-box-orient: vertical;
            -webkit-box-direction: normal;
            -webkit-flex-direction: column;
            -ms-flex-direction: column;
            flex-direction: column
        }

        .mui-flex.vertical.reverse {
            -webkit-box-orient: vertical;
            -webkit-box-direction: reverse;
            -webkit-flex-direction: column-reverse;
            -ms-flex-direction: column-reverse;
            flex-direction: column-reverse
        }

        .mui-flex.vertical .cell {
            width: auto
        }

        .mui-flex.vertical>.cell>.inner {
            position: absolute;
            width: 100%;
            height: 100%
        }

        .mui-flex.horizental {
            -webkit-box-orient: horizontal;
            -webkit-box-direction: normal;
            -webkit-flex-direction: row;
            -ms-flex-direction: row;
            flex-direction: row
        }

        .mui-flex.reverse {
            -webkit-box-orient: horizontal;
            -webkit-box-direction: reverse;
            -webkit-flex-direction: row-reverse;
            -ms-flex-direction: row-reverse;
            flex-direction: row-reverse
        }

        .mui-flex.justify-start {
            -webkit-box-pack: start;
            -webkit-justify-content: flex-start;
            -ms-flex-pack: start;
            justify-content: flex-start
        }

        .mui-flex.justify-end {
            -webkit-box-pack: end;
            -webkit-justify-content: flex-end;
            -ms-flex-pack: end;
            justify-content: flex-end
        }

        .mui-flex.justify-center {
            -webkit-box-pack: center;
            -webkit-justify-content: center;
            -ms-flex-pack: center;
            justify-content: center
        }

        .mui-flex.justify-between {
            -webkit-box-pack: justify;
            -webkit-justify-content: space-between;
            -ms-flex-pack: justify;
            justify-content: space-between
        }

        .mui-flex.justify-around {
            -webkit-justify-content: space-around;
            -ms-flex-pack: distribute;
            justify-content: space-around
        }

        .mui-flex.align-start {
            -webkit-box-align: start;
            -webkit-align-items: flex-start;
            -ms-flex-align: start;
            align-items: flex-start
        }

        .mui-flex.align-end {
            -webkit-box-align: end;
            -webkit-align-items: flex-end;
            -ms-flex-align: end;
            align-items: flex-end
        }

        .mui-flex.align-center {
            -webkit-box-align: center;
            -webkit-align-items: center;
            -ms-flex-align: center;
            align-items: center
        }

        .mui-flex.align-stretch {
            -webkit-box-align: stretch;
            -webkit-align-items: stretch;
            -ms-flex-align: stretch;
            align-items: stretch
        }

        .mui-flex.align-stretch .cell {
            height: auto!important
        }

        .mui-flex.center {
            -webkit-box-pack: center;
            -webkit-justify-content: center;
            -ms-flex-pack: center;
            justify-content: center;
            -webkit-box-align: center;
            -webkit-align-items: center;
            -ms-flex-align: center;
            align-items: center
        }

        .mui-flex>.cell {
            -webkit-box-flex: 1;
            -webkit-flex: 1;
            -ms-flex: 1;
            flex: 1;
            width: 0;
            -webkit-flex-basis: 0;
            -ms-flex-preferred-size: 0;
            flex-basis: 0;
            max-width: 100%;
            display: block;
            padding: 0!important;
            position: relative
        }

        .mui-flex>.cell.fixed {
            -webkit-box-flex: 0!important;
            -webkit-flex: none!important;
            -ms-flex: none!important;
            flex: none!important;
            width: auto
        }

        .mui-flex>.cell.align-start {
            -webkit-align-self: flex-start;
            -ms-flex-item-align: start;
            align-self: flex-start
        }

        .mui-flex>.cell.align-end {
            -webkit-align-self: flex-end;
            -ms-flex-item-align: end;
            align-self: flex-end
        }

        .mui-flex>.cell.align-center {
            -webkit-align-self: center;
            -ms-flex-item-align: center;
            align-self: center
        }

        .mui-flex>.cell.align-stretch {
            -webkit-box-align: stretch;
            -webkit-align-items: stretch;
            -ms-flex-align: stretch;
            align-items: stretch;
            height: auto!important
        }

        .mui-flex .image-box {
            height: 0;
            padding-bottom: 100%;
            position: relative
        }

        .mui-flex .image-box>img {
            width: 100%;
            height: 100%;
            display: block;
            position: absolute
        }

        .mui-flex>.cell-1,.mui-flex>.cell-10,.mui-flex>.cell-11,.mui-flex>.cell-12,.mui-flex>.cell-2,.mui-flex>.cell-3,.mui-flex>.cell-4,.mui-flex>.cell-5,.mui-flex>.cell-6,.mui-flex>.cell-8,.mui-flex>.cell-9 {
            width: auto!important
        }

        .mui-flex>.cell-12 {
            -webkit-flex-basis: 100%;
            -ms-flex-preferred-size: 100%;
            flex-basis: 100%;
            max-width: 100%
        }

        .mui-flex>.order-12 {
            -webkit-box-ordinal-group: 13;
            -webkit-order: 12;
            -ms-flex-order: 12;
            order: 12
        }

        .mui-flex>.cell-11 {
            -webkit-flex-basis: 91.66666667%;
            -ms-flex-preferred-size: 91.66666667%;
            flex-basis: 91.66666667%;
            max-width: 91.66666667%
        }

        .mui-flex>.order-11 {
            -webkit-box-ordinal-group: 12;
            -webkit-order: 11;
            -ms-flex-order: 11;
            order: 11
        }

        .mui-flex>.cell-10 {
            -webkit-flex-basis: 83.33333333%;
            -ms-flex-preferred-size: 83.33333333%;
            flex-basis: 83.33333333%;
            max-width: 83.33333333%
        }

        .mui-flex>.order-10 {
            -webkit-box-ordinal-group: 11;
            -webkit-order: 10;
            -ms-flex-order: 10;
            order: 10
        }

        .mui-flex>.cell-9 {
            -webkit-flex-basis: 75%;
            -ms-flex-preferred-size: 75%;
            flex-basis: 75%;
            max-width: 75%
        }

        .mui-flex>.order-9 {
            -webkit-box-ordinal-group: 10;
            -webkit-order: 9;
            -ms-flex-order: 9;
            order: 9
        }

        .mui-flex>.cell-8 {
            -webkit-flex-basis: 66.66666667%;
            -ms-flex-preferred-size: 66.66666667%;
            flex-basis: 66.66666667%;
            max-width: 66.66666667%
        }

        .mui-flex>.order-8 {
            -webkit-box-ordinal-group: 9;
            -webkit-order: 8;
            -ms-flex-order: 8;
            order: 8
        }

        .mui-flex>.cell-7 {
            -webkit-flex-basis: 58.33333333%;
            -ms-flex-preferred-size: 58.33333333%;
            flex-basis: 58.33333333%;
            max-width: 58.33333333%;
            width: auto!important
        }

        .mui-flex>.order-7 {
            -webkit-box-ordinal-group: 8;
            -webkit-order: 7;
            -ms-flex-order: 7;
            order: 7
        }

        .mui-flex>.cell-6 {
            -webkit-flex-basis: 50%;
            -ms-flex-preferred-size: 50%;
            flex-basis: 50%;
            max-width: 50%
        }

        .mui-flex>.order-6 {
            -webkit-box-ordinal-group: 7;
            -webkit-order: 6;
            -ms-flex-order: 6;
            order: 6
        }

        .mui-flex>.cell-5 {
            -webkit-flex-basis: 41.66666667%;
            -ms-flex-preferred-size: 41.66666667%;
            flex-basis: 41.66666667%;
            max-width: 41.66666667%
        }

        .mui-flex>.order-5 {
            -webkit-box-ordinal-group: 6;
            -webkit-order: 5;
            -ms-flex-order: 5;
            order: 5
        }

        .mui-flex>.cell-4 {
            -webkit-flex-basis: 33.33333333%;
            -ms-flex-preferred-size: 33.33333333%;
            flex-basis: 33.33333333%;
            max-width: 33.33333333%
        }

        .mui-flex>.order-4 {
            -webkit-box-ordinal-group: 5;
            -webkit-order: 4;
            -ms-flex-order: 4;
            order: 4
        }

        .mui-flex>.cell-3 {
            -webkit-flex-basis: 25%;
            -ms-flex-preferred-size: 25%;
            flex-basis: 25%;
            max-width: 25%
        }

        .mui-flex>.order-3 {
            -webkit-box-ordinal-group: 4;
            -webkit-order: 3;
            -ms-flex-order: 3;
            order: 3
        }

        .mui-flex>.cell-2 {
            -webkit-flex-basis: 16.66666667%;
            -ms-flex-preferred-size: 16.66666667%;
            flex-basis: 16.66666667%;
            max-width: 16.66666667%
        }

        .mui-flex>.order-2 {
            -webkit-box-ordinal-group: 3;
            -webkit-order: 2;
            -ms-flex-order: 2;
            order: 2
        }

        .mui-flex>.cell-1 {
            -webkit-flex-basis: 8.33333333%;
            -ms-flex-preferred-size: 8.33333333%;
            flex-basis: 8.33333333%;
            max-width: 8.33333333%
        }

        .mui-flex>.order-1 {
            -webkit-box-ordinal-group: 2;
            -webkit-order: 1;
            -ms-flex-order: 1;
            order: 1
        }

        body {
            background-color: #f0f0f0
        }


        header,section {
            background-color: #fff
        }

        .tm-detail-font {
            font-family: tm-detail-font;
            font-style: normal
        }

        #content {

            background: #f4f4f4;
            height:auto;

        }

        #content.pt85 {
            padding-top: 85px
        }

        #content>.scroller {
            display: -webkit-box;
            display: -ms-flexbox;
            display: flex
        }

        #content>.scroller .page {
            min-width: 100%;
            max-width: 100%;
            display: block
        }

        .hideHeaderInApp.sku-demote-active .mui-cover header,.mui-global-m-smart-banner,html .p-decision-origin .mui-cover>header .back::before {
            display: none
        }

        #content>.scroller #p-summary {
            padding-bottom: 50px
        }

        #content>.scroller #p-summary .pull-loading-active {
            -webkit-transform: translateZ(0);
            -moz-transform: translateZ(0);
            -ms-transform: translateZ(0);
            -o-transform: translateZ(0);
            transform: translateZ(0);
            -webkit-backface-visibility: hidden;
            -moz-backface-visibility: hidden;
            -ms-backface-visibility: hidden;
            backface-visibility: hidden;
            -webkit-perspective: 1000;
            -moz-perspective: 1000;
            -ms-perspective: 1000;
            perspective: 1000
        }

        .tab-other #content {
            padding-bottom: .6rem
        }

        .pageType-chaoshi #content {
            padding-top: 0
        }

        html .mui-dialog {
            z-index: 9999999999
        }

        html .p-decision-origin .mui-cover {
            background-color: #fff;
            position: fixed;
            z-index: 999999;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            -webkit-transform: translate3d(100%,0,0);
            transform: translate3d(100%,0,0);
            -webkit-transition: -webkit-transform .2s cubic-bezier(0,0,.25,1);
            transition: transform .2s cubic-bezier(0,0,.25,1);
            display: none
        }

        html .p-decision-origin .mui-cover>header {
            text-align: center;
            font-size: 14px;
            position: relative;
            -webkitbox-shadow: 0 1px 0 rgba(0,0,0,.05),0 .5px 0 rgba(0,0,0,.09);
            -webkit-box-shadow: 0 1px 0 rgba(0,0,0,.05),0 .5px 0 rgba(0,0,0,.09);
            box-shadow: 0 1px 0 rgba(0,0,0,.05),0 .5px 0 rgba(0,0,0,.09);
            background-color: #fff;
            padding: 0 1em;
            color: #666;
            z-index: 1;
            height: 44px;
            line-height: 44px
        }

        html .p-decision-origin .mui-cover>header h1 {
            font-size: inherit;
            margin: 0
        }

        html .p-decision-origin .mui-cover>header .back {
            padding-left: 16px;
            background: url(//g.alicdn.com/mui/cover/3.0.0/sliding-menu/img/back@2x.png) 0 50% no-repeat;
            -webkit-background-size: 12px 16px;
            background-size: 12px 16px;
            position: absolute;
            left: 6px;
            top: 0;
            width: 50px;
            color: #666;
            text-indent: 0;
            font-size: 14px
        }

        html .p-decision-origin .mui-cover.show {
            -webkit-transform: translate3d(0,0,0);
            transform: translate3d(0,0,0);
            top: 0
        }

        html .mui-cover {
            -webkit-transform: translate3d(0,100%,0);
            transform: translate3d(0,100%,0)
        }

        html .mui-cover>header .back {
            left: auto;
            right: 0;
            background: 0 0;
            width: 30px;
            height: 100%;
            font-size: 0;
            color: #fff
        }

        html .mui-cover>header .back::before {
            content: '\e614';
            font-family: tm-detail-font;
            font-size: 14px;
            position: absolute;
            right: 10px;
            color: #555
        }

        html .mui-cover.show {
            -webkit-transform: translate3d(0,0,0);
            transform: translate3d(0,0,0);
            top: 20%
        }

        html .cover-normal {
            width: 100%;
            opacity: .5;
            height: 100%;
            position: fixed;
            background-color: #666;
            top: 0;
            left: 0;
            z-index: 100
        }

        #detail-base-smart-banner {
            position: fixed;
            height: 44px;
            width: 100%;
            z-index: 1;
            top: 41px;
            left: 0
        }

        .none-fixed {
            position: inherit!important
        }

        .app-download-popup {
            z-index: 1
        }

        .mui-indicator.mui-indicator-fixed,.mui-loading.mui-loading-fixed {
            position: fixed;
            top: 0;
            left: 0;
            bottom: 0;
            right: 0;
            pointer-events: none
        }

        @-webkit-keyframes rotate {
            from {
                -webkit-transform: rotate(0)
            }

            to {
                -webkit-transform: rotate(360deg)
            }
        }

        .mui-loading {
            display: -webkit-box;
            -webkit-box-align: center;
            -webkit-box-pack: center;
            display: -ms-flexbox;
            -ms-flex-pack: center;
            -ms-flex-align: center;
            z-index: 1000000
        }

        .mui-loading i {
            display: inline-block;
            padding: 5px 10px;
            font-size: 0
        }

        .mui-loading i::after {
            content: '';
            display: inline-block;
            -webkit-animation: rotate 1s linear infinite;
            background: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAeCAYAAAA7MK6iAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2ZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDoxNzk1ODUwQzlEMDhFMzExOTFBMkJBQUFCMkFDODIzRSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo0RjIzMTM3QTMzM0MxMUUzODU3MDg3MTczRUU5MUI2OCIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo0RjIzMTM3OTMzM0MxMUUzODU3MDg3MTczRUU5MUI2OCIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M2IChXaW5kb3dzKSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjFBOTU4NTBDOUQwOEUzMTE5MUEyQkFBQUIyQUM4MjNFIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjE3OTU4NTBDOUQwOEUzMTE5MUEyQkFBQUIyQUM4MjNFIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+SXvjNwAAAvpJREFUeNqsl0toU0EUhm80baJWS7Wttj6qqIuCohhdKaQouBARVCgiCOJSV1KXblUEN+rCB/haCC4UdeFG1LrwLUERFB+oC0vF+G5o1TRp/A/8A9PjzM29sQc+bu4kM//MmXPOTBKVSiWIarlcznxcBPaCZeArOA+OgbJ8mclkqo6VdAxazeaBC6CB721gD2gE+6IOkjAr9gjXg6Jq2w+2On5bAsvBj2qi4pGko3082AE2clVvwRHwxFqxz3sdFE6ALrAQvAG3wag9HecYYCfYZrlyPjjEQcQ+eIRHQB/73QC3wAk+e7kVXuFJXKm2OrCFn0873C92kYF2AKxW32XBwTDh6XbAKWvj8yXYDl7z/Rc4wygX6/b073ZGNe0TGOYKtX20Pt8Da+mhPwwsYxM9whPCVjwILjk6/WauBo7fl1TbTY/wqPakI42OgyGwiQHxChwF7+x0CCkuPWAFaFee3O1zdYJuEtGzpBaT9FvCOOhkTJwDeXuSRngNo1lWWABXwPWgdvvCFAwtmSs5O2OTmccl5mCk2htiU0CzVQVlUgMivMHTYZ0RjmJ6ctxzWcRMqznF94RE9TTPWK3B/1uLp71ZhPs9X/aPgXCdp71ehK/qAk67PAbCw572ouyxbMZhsBnMAJ8p+tB1VscMNBlrlivqTTrlSE0Wcoko8MRq+SeqZQWq41SwiinwDdzlzONamq4ukCDskJAI38WwN2exVKFTnHkUa2XFSjN25HB5oe9jWni9JWpffySnT0a4m0nlW8rya8pwO5/Pwk6nDs+As9V7A7dCT3yOJarP8nSYq4ccKzbtZvWy/wtMWoD7PMECPbiyFI9X54ofeTo95rPLEjUTyVoeKXj6l3l2e119h7eLEb5XmM+9PDLnegbu5PM9bySuo7IU5moRvMbraBP4bs00VSV1Aoo+YDY08b1PXZucwvaVZlC1DXAg1wTy6pr0PNJfmGplkClU5jZk1RbJvj6NW12SMX8v/wp+gsW8NeYpWoxbx/8KMAC97sC/2v4BrgAAAABJRU5ErkJggg==) 50% 50% no-repeat;background-size: 30px;
            width: 30px;
            height: 30px
        }

        .mui-loading.mui-loading-fixed i {
            background-color: rgba(0,0,0,.4);
            -webkit-border-radius: 3px;
            border-radius: 3px
        }

        .mui-prompt-loading {
            width: 30px;
            height: 21px;
            overflow: hidden;
            z-index: 99999
        }

        .mui-prompt-loading .mui-loading-img {
            vertical-align: top;
            width: 100%;
            height: 100%;
            background: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAADwAAAHICAMAAAAlVnn7AAAATlBMVEUAAADdJyfdJyfdJyfdJyfdJyfdJyfdJyfdJyfdJyfdJyfdJyfdJyfdJyfdJyfdJyfdJyfdJyfdJyfdJyfdJyfdJyfdJyfdJyfdJyfdJydorXnTAAAAGXRSTlMA7luml0ESIdAHa2OA98EuA8aNNdy9TeDe9sbavAAAAppJREFUeNrtm9tS6kAQRTs3ksmdCOj+/x89sq1yiCVCGk8JYa+3URYZeCCLrsGOpHm1b0u7krLdV3lqH4QMJL/OzUGywFWGpB6K7H19jXt8XDHUCTLuGQl3XHB92U0K7j3Bced7NEZeOuSX99y9GGmwNyuRBPvggEt2Chzsg5CgtPRksw3/d54CqE9eQGr5ydpaoPjRbeOqRm797OETkP6w52n2VL3tUNoJ7dnXnfO6kRI7S7CxU5oO2+aby27Rzf+8QWLAaDOGCujbojx9zrYHqsFmjIAB9oXQ7HDEIsCuCfaFKM8Yi6l/TSwyFaPZWfkykiVLlnyPMj+3PQR0VmHwyQMq3ug88EaXoho97lghNdti8sgTtseaALZpGZaIoUy3wAtbpIKD6t0ldfbWYQHdW1bbM7Oseje1t3rJwVu9xFe9xFm9xFm9xFu9vIq3eg9m/uqd7AGq95OyWFi9c5ZX7yevvapXsmTJa5RVvareNaNZr2a9vzzrjXirl3irl3irl/irN8qXkfwL8qNmhWTJdyyrelW9a0bVq+p9uBMOmvVKliz5/8tjQ9lJeMivg3Fa4YLV68VdvYTV6yO4q3dj5q/efhxvqN5B1avqVfWqelW9kiVLlqxZ76pmvRZZPuu1iKpX1avqVfWqeiVLlixZ1Xsv1WuR5SccLKJZr6pX1avqVfVKlixZ8iqr126o3p3dUL2tEV/1BvukX36uN5LrhIOqV9Wr6lX1Sj4vP96RS8lPI6t6n3bWq1+zfYOq9y+ql7irl3irl3irl3irl3irl3irl3irl3irl1xRvbpLSpYseXWyqlfVu2ZSvnPe6k2N+Kq3NOKuXuKuXuKe9RL3rJd4Z70k+Ge9NmSa9WrWK1myZMmq3uet3n9e9UhllKazwQAAAABJRU5ErkJggg==) no-repeat;-webkit-background-size: 100% auto;
            background-size: 100% auto
        }

        .mui-prompt-loading .mui-loading-play {
            -webkit-animation: mui-loading-ani 1s infinite
        }

        @-webkit-keyframes mui-loading-ani {
            0%,10% {
                background-position-y: 0
            }

            10.01%,20% {
                background-position-y: -23px
            }

            20.01%,30% {
                background-position-y: -46px
            }

            30.01%,40% {
                background-position-y: -69px
            }

            40.01%,50% {
                background-position-y: -92px
            }

            50.01%,60% {
                background-position-y: -115px
            }

            60.01%,70% {
                background-position-y: -138px
            }

            70.01%,80% {
                background-position-y: -161px
            }

            80.01%,90% {
                background-position-y: -184px
            }

            100%,90.01% {
                background-position-y: -207px
            }
        }

        .mui-indicator {
            display: -webkit-box;
            -webkit-box-align: center;
            -webkit-box-pack: center;
            z-index: 1000000
        }

        .mui-indicator.mui-indicator-fixed i {
            background-color: rgba(0,0,0,.4);
            border-radius: 3px
        }

        .mui-indicator.mui-indicator-fixed .mui-prompt-loading {
            background: rgba(255,255,255,.8);
            border-radius: 4px
        }

        .mui-indicator .mui-prompt-loading {
            padding: 5px
        }

        #s-header {
            display: -webkit-box;
            display: -ms-flexbox;
            display: flex;
            text-align: center;
            font-size: .13rem
        }

        #s-header .go-desc {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            background-color: #fff;
            border-bottom: 1px solid #a6a6a6;
            height: 3em;
            line-height: 3em;
            color: #000;
            text-align: center
        }

        #s-header .go-desc .dback {
            position: absolute;
            top: 0;
            left: 0;
            display: block;
            width: 40px;
            height: 100%
        }

        #s-header .go-desc .dback::before {
            content: "\e610";
            font-family: tm-detail-font;
            font-size: 17px
        }

        #s-header a {
            -webkit-box-flex: 1;
            -ms-flex: 1;
            flex: 1;
            flex-basis: auto;
            display: block;
            flex-basis: 1px;
            display: -webkit-box;
            -webkit-box-align: center;
            -webkit-box-pack: center;
            display: -ms-flexbox;
            -ms-flex-pack: center;
            -ms-flex-align: center;
            -webkit-box-orient: vertical;
            -ms-flex-orient: vertical;
            height: 3em;
            color: #051b28;
            border-top: 2px solid transparent;
            position: relative
        }

        #s-header a::after {
            content: '\20';
            position: absolute;
            right: 0;
            top: .4em;
            bottom: .4em;
            border-right: 1px solid #e5e5e5
        }

        #s-header a:last-child::after {
            display: none
        }

        #s-header a.current {
            border-top-color: #c40000;
            color: #c40000
        }

        #s-header a.review {
            line-height: 14px
        }

        #s-header a b {
            color: #000
        }

        #s-header {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            z-index: 1
        }

        .pageType-chaoshi #s-header {
            position: static
        }

        #s-showcase .scroller .itbox .item:before,#s-showcase .scroller .itbox:before {
            content: '\20';
            padding-top: 100%;
            display: block
        }

        #s-showcase {
            position: relative;
            overflow: hidden;
            border: none
        }

        #s-showcase .scroller {
            display: -webkit-box;
            -webkit-box-lines: multiple
        }

        #s-showcase .scroller .itbox {
            box-sizing: border-box;
            display: none;
            width: 100%;
            height: 100%;
            overflow: hidden;
            display: -webkit-box;
            -webkit-box-align: center;
            -webkit-box-pack: center;
            position: relative
        }

        #s-showcase .scroller .itbox .item {
            position: relative;
            left: 50%;
            width: 100%;
            margin-left: -50%;
            -webkit-transition: -webkit-transform .25s ease;
            transition: -webkit-transform .25s ease;
            position: absolute;
            top: 0;
            max-height: 100%
        }

        #s-showcase .scroller .itbox .item img {
            max-width: 100%;
            max-height: 100%;
            position: absolute;
            top: 50%;
            left: 50%;
            -webkit-transform: translate3d(-50%,-50%,0)
        }

        #s-showcase .scroller .main {
            display: block
        }

        #s-showcase .nav-container {
            position: absolute;
            text-align: center;
            top: 95%;
            left: 0;
            right: 0
        }

        #s-showcase .nav-container i {
            height: 9px;
            width: 9px;
            -webkit-border-radius: 4.5px;
            border-radius: 4.5px;
            margin: 0 4px;
            display: inline-block;
            background-color: #c4c4c4;
            opacity: .4
        }

        #s-showcase .nav-container .current {
            background-color: #DD2727;
            opacity: 1
        }

        .pull-notice-hori {
            text-align: center;
            height: 100%;
            width: 50px;
            position: absolute;
            right: 0;
            top: 0;
            background: red
        }

        .pull-notice-hori div hr {
            background: #000;
            opacity: .1
        }

        .pull-notice-hori .txt {
            width: 140px;
            color: #999
        }

        #s-from .from-content {
            text-align: left;
            padding: 10px 0 0 10px
        }

        #s-from .from-content img {
            height: 20px;
            width: 20px;
            -webkit-border-radius: 10px;
            border-radius: 10px;
            float: left
        }

        #s-from .from-content span {
            color: #999;
            padding-left: 5px;
            line-height: 20px
        }


        #s-title {
            padding: 10px 0 0
        }

        #s-title .main {
            position: relative;
            display: -webkit-box;
            display: -ms-flexbox;
            display: flex
        }

        #s-title .main .vacantPosition {
            width: 60px;
            border-left: 1px solid rgba(0,0,0,.1)
        }

        #s-title .main h1 {
            padding: 0 10px;
            -webkit-box-flex: 1;
            -ms-flex: 1;
            flex: 1;
            flex-basis: auto;
            display: block;
            flex-basis: 1px;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            word-wrap: break-word;
            overflow: hidden;
            -webkit-box-pack: center;
            line-height: 1.35em;
            height: 3em;
            font-size: 14px;
            color: #051B28
        }

        #s-title .main .coupon {
            width: 40px;
            height: 42px;
            display: block;
            position: absolute;
            top: -12px;
            right: 8px;
            font-family: iconfont;
            font-size: 32px;
            text-align: center;
            cursor: pointer;
            color: #dd2727
        }

        #s-title .main em {
            position: absolute;
            bottom: -2px;
            right: 5px;
            color: #dd2727
        }

        #s-title .main .fav {
            display: -webkit-box;
            -webkit-box-align: center;
            -webkit-box-pack: center;
            display: -ms-flexbox;
            -ms-flex-pack: center;
            -ms-flex-align: center;
            -webkit-box-orient: vertical;
            -ms-flex-orient: vertical;
            color: #999;
            width: 5em;
            border-left: 1px solid #e5e5e5
        }

        #s-title .main .fav::before {
            content: "\20";
            background: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAAC8AAAAsCAYAAAD1s+ECAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyRpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoTWFjaW50b3NoKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo1QTkwODBFQUNFMEUxMUUzODYwM0NDQTFGRDY5QzlFRSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo1QTkwODBFQkNFMEUxMUUzODYwM0NDQTFGRDY5QzlFRSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjVBOTA4MEU4Q0UwRTExRTM4NjAzQ0NBMUZENjlDOUVFIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjVBOTA4MEU5Q0UwRTExRTM4NjAzQ0NBMUZENjlDOUVFIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+6x4XswAABVFJREFUeNrUWF9MW1UYb6vQAsrkP4WwLQSIUEbpKMbgjGAY7RgN8GDUxDklFTKW4V4MRAzIgzyQGGNFA5uQaWAQH3yk6AwNICbbnIwViKIik8IwZZQhECSQ+fuac5MrltI/9zJ6kt8959xz7jm/+53vfOf7jrSyslIiYjoLfOGuQ3t7u8+Dy0QkHg20AjFiTSAm+VeBJ4BXApH8WZa/GWjkVUBOSEjILHINcCyQyDulHhERMcHqrwcK+ceA16iQm5s7wN6dAR4PBPJFgDI8PHy8uLj4Z+RW1OMAXSCQd6pIfHz8KOVKpXJULNURmvwhoIwKOp3OQrler+dUpxSIPMjkXwYU2Kg/ZmZmLtKLjIyMpcjIyBsoyln7gSXvtDIpKSnD/JepqanDYqiOkORTgTy5XH7PaDRa+A0VFRWDCoXChuKzQPpBJL/Ttv8niWHzZQKOQ7ZcotFoBlx1yMnJsfBsvuwgkc8HDsOmT5SVlY276mAwGCapHcVE4KQQk9KpdwG4CKwD//g4jpIeMTExVned4uLibq+srJDfcxVYpndVVVWbyNZ8mLNDyoIROs4vASG+SiEqKup6c3PzB3v1q6+vf2dxcfF5PwROP/s2gpg2KS+SOg58DRyBZZgtKSn5yNPREhISHCqV6r6n/WdmZp602+1hTiabm0Hr6+tyd/37+vpq0OcoivPASyD+A72X7ggDKer5iumwRK1Wf1pdXf2N5BGltra2k6OjoxdYdYQRv7fbhrWzzfQxVcbGxs43NjbWPAriNC+P+GdAAZ+4K8nz0xm2D5zHPQ4aU1pa2rLYpKempp7q7OyscTgcWlQ3gHMgfcWl7w37u9s4dwAzcGpjYyMdUtAGBQX9kpyc7BCLuNlsTuvp6XlvdXX1aVT/JP8OxM27Bg5uyFOiZeqmuGJra0s9OTmpn5ubW9Jqtb8LTby1tfX00NDQu5gnHFU66IpA/Dd/DyluH5ioghUQfB80NDRctFqtVaz6IZO4fa/vpF5eOpFf0s72wU1M2hwaGrrtD/Ha2tr3l5eXj7ODygjSvWK5B18CJ4BZbKjclpaWan+Im0wmAyM+DTznDXFffZtbnG+CH8jwh/zCwoKKWwAQH9svx+wZesDR+tUf8ohzOTf5xf30Kg0suJ7wh3xWVtYdVjy9X+Tl7HpDUlBQcMMvPzo//y42/F1yp+FdHtsP8i/QLQHSWHp6ut8HFqzWJCue2g/yJfSIjY0dd+tsd3TkNzU1VVPurh+C9VusqPclGPE20f2LJDs7+6arRovFcrS/v/8NZgIl8/Pzevgr+Xq9/grUbOZ/g5WW/jQ4OEjFE1CdQ7A6D8SSfBbpJ+lpYWHh9M5GSPpcb2+viRH/g5wqyqlO76l95zdhYWFb5PihGMTtJbHIl7q6IRgeHj6Mk7IRUia9fchcWNqAbSyn+kNqpxMVPswR/veJiYnclaCo5J0mDRv1Ol/aXV1drZAueXikFoXAeV5cusbqhdwqdHd3f8JfBTiHHPliqI5UDPIJ3OFUXl5+e2RkJKmurq6BJ+12plYDu3xP79VsNZyrQN9j1ZLy8vJsUMVpNofGU0LeOGZG4HJ0dPT3wcHBfzPSlMhOvwVc80IQtAqfU7zMYmCzTCbbstlsdPjVY9M2Cy155+3v9va2gifty0za17xUv+/Yd5e4VVhbW1N6a+89lTxdidznXY3MMmkLEZwXsVVIYnVysWMh/SWhJK/jEe9gFkSQWwWQ/BZZJhvXGd15emDJvLAyc2xJSfcfSARM+IEVwMjGn/PUZMo87EMqQ753v5g3B/iBfjbPXzCZe3L7V4ABAOyY0P76t75nAAAAAElFTkSuQmCC) no-repeat;background-size: 24px;
            width: 24px;
            height: 22px;
            display: block
        }

        .relGoods,.relGoods .content {
            display: -webkit-box;
            display: -ms-flexbox
        }

        #s-title .main .fav.faved::before {
            background-image: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAAC8AAAAsCAYAAAD1s+ECAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyRpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoTWFjaW50b3NoKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDoxRTg3NzBFRkVFMUExMUUzQTJFMzhCRENDOENGRTJCNyIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDoxRTg3NzBGMEVFMUExMUUzQTJFMzhCRENDOENGRTJCNyI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjFFODc3MEVERUUxQTExRTNBMkUzOEJEQ0M4Q0ZFMkI3IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjFFODc3MEVFRUUxQTExRTNBMkUzOEJEQ0M4Q0ZFMkI3Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+/QzNBwAABPJJREFUeNrUWX1MW1UUPy2dpdDRljBdx2BmAgIbI1Myp3wMl+kYE1mMi5rsI0sQNMbM//zbGBP/UBPBmHU6dajTLNG/5zbNdHGJyViiokbHx9bSdogCg9IBgdXfue8+KFDq68fr6El+vHfeu7339+4959xzLobW1lbSUQ4DJ6M1cLlccXdu1JF4HvAesEavAfQk/xxgBZ5NR/KH5fVIupHfBDzoNI/w/VagIp3Ii1kvzrqh6ofShXwGcIBvHrb/pT47CJjSgfzjgLMky08bLYNUku3jZ/cAu9OBvDCRcuuAYvzZA7qZTrLJ24B9fLPdppjM9nnTaQZyVzL5Z4DMCqubHKsmxAO7KUiVq6/zrVm+X7HkRZSpsvUueFiV06uL6SSTfDEHGL55yNaz4MU26GvNo8KKgLKVSJ5n1fCI/U8yUGjpl+kQ841J7OegiO22qxEbqA4s2xlXEvkdwIZixHYZ15dIEWae30PygceSMSjvei8DrwBjwGyc/Tj5T0m2P2qjcquXrgZF01OAcIK2trZpXCbiGPMEk+8AOIM6DljinYXK1dfoyTWXo7ZpzLtC3kkHXR67LzeBmM8fexRFzDFDWCX1APA1L78TkeFI/nfadybTLcRz7ZM3MZtJwdm7xP1MyEjToehpT6evngYmxbeyTe4H8UusGBaVgVz1nAbqWTngvEi1jj/oTsmPo6UgXjenSuL+5Rx2SDrTu6x85q+lk74dd4Q4jxtG/H3g0XDitEyaOiMduIv94NLo/ZnjMxY6tO57yoF56C1jGKsTxH8NFLI6CbwI0p/EGio/BaoBN3f0zvUmck/m6Uq8/9bd9Na1JpW4G6hZjriWOH+FUxPggn/KTm/0PUUXR0p1IX5heBO92b+PBqftrHK0qALxrkQ3KdUP2hU/qEu6H3zsracvblSr6ttcuID4kJZNSouwHxyVfuBiPwjMWuiF9Wcpw3A7IeLt7kb6LbCe5EbVAtJf6pUedLIdAp5fxgvpc39NQsS/Hd6sEu9j/4qFeLy5TZeam/QE1yZEvkdJFVheBfGfU5WYbeM/91qGEiJflDUXtnemMqtsWpSjxyWl8xno3lSRN8vjDdqi1KZxS755WABSiOyyIhXkOU7aSrO9SMiCCYfJovnV25MK8k+I3D0reu7+080iRKNacY0mm60e9bYhFeSbFZNxR3zJqSvH7o+8O+mHkTJxZV2mtBEKlDnyNTAdm57kt7B9sp0WZP6z5CXH/df7nlZjdz8nVXxlnZ9H2hdM2OT4nAeySvUlvcg3R4oyvikHdbj3YKbLWQ3JFJYd8Ji8sh7i9+1o55vKXTT7c0eCupLfq5Z84bP9Wu9+6g4UsMovdgEvhdWlE1LfpaxCAdovXIXy+fPMRpiOQQ/y69TNqQyFNGeZHe6G8Nl2SbNarn7k55VyNcQq8O/9WDU+kJJmyGNsjeX0QKs0ctlYldNHp/zVKmkWDvbPA+c09DEu/eAr4MPuQOEGgOocvwtT9Cj1QoNMxZM68+L0d+q2KXy2P5CzfS5G8zsvf3dcXYW/p3NijvdayVvU/ENWOR45SKs874mr4uNjGznTnm6lX3HoBrvPTSb53WFnOidkBPkmGYUIssmzvFfJflkytG5YxhiijFfOdgtwM5mVFD5gDGiR/Xu1hkyjxjb/kvLvyTN6FuD4gDNynEGYzv9y+0+AAQCqYI2Kix5tZwAAAABJRU5ErkJggg==)
        }

        #s-title .product-sellpoint {
            padding: 0 10px;
            color: #f55b5b;
            font: .12rem/1.3 helvetica
        }

        #s-title .product-sellpoint.color666 {
            color: #666
        }

        #s-title .sub .attraction50,#s-title .sub .slogon {
            padding: 0 10px;
            font-size: .13rem;
            color: #c40000
        }

        .relGoods {
            -webkit-box-align: center;
            -ms-flex-align: center;
            border-top: 1px solid #e5e5e5;
            border-bottom: 1px solid #e5e5e5;
            margin-top: 10px;
            background-color: #fff
        }

        .relGoods .content {
            -webkit-box-flex: 1;
            -ms-flex: 1;
            flex: 1;
            flex-basis: auto;
            -webkit-box-align: center;
            -ms-flex-align: center;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            padding: .5em 14px .5em 10px;
            display: block;
            line-height: 2em
        }

        .relGoods .content:before {
            content: "\20";
            float: right;
            background: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAYCAYAAADKx8xXAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyRpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoTWFjaW50b3NoKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo1QTkwODBFRUNFMEUxMUUzODYwM0NDQTFGRDY5QzlFRSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo1QTkwODBFRkNFMEUxMUUzODYwM0NDQTFGRDY5QzlFRSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjVBOTA4MEVDQ0UwRTExRTM4NjAzQ0NBMUZENjlDOUVFIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjVBOTA4MEVEQ0UwRTExRTM4NjAzQ0NBMUZENjlDOUVFIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+ZRD4UgAAAH1JREFUeNqc1GEKwCAIBeB897+Ap3Ub7IeI5lNh1EYfaQ1FVc8f9j5yyIBDfqRgXGwszNIzNtUxhpuPMMI7jZF8ozCKTFqMS/1XjObUSwzirlOMswwGZtch2KBuR7vViw2qoDEniw2KkEZZ66BQ1TpaVLUOmfw5MkFfPAIMACrRGPu1OoDUAAAAAElFTkSuQmCC) 100% 50% no-repeat;background-size: 7px;
            width: 7px;
            height: 2em;
            margin-left: .9em
        }

        .alihealthApp #s-title .main h1 {
            margin-right: 0;
            border-right: 0
        }

        .alihealthApp #s-title .main .coupon,.alihealthApp #s-title .main em {
            display: none
        }

        .mui-price {
            color: #dd2727;
            font-size: 12px
        }

        .mui-price-integer {
            font-size: 16px
        }

        .mui-price-rmb {
            font-family: arial;
            font-style: normal
        }

        .mui-price-decimal {
            margin-left: -3px
        }

        .mui-price.big {
            font-size: 18px
        }

        .mui-price.big .mui-price-integer {
            font-size: 24px;
            margin-left: -3px
        }

        .mui-price.gray {
            color: #999;
            font-size: 10px;
            text-decoration: line-through
        }

        .mui-price.gray .mui-price-integer {
            font-size: 10px
        }

        #s-price {
            position: relative;
            padding: 0 10px
        }

        #s-price .maokeapp-price {
            right: 15px;
            color: #dd2727;
            font-size: 12px;
            top: 12px;
            display: block
        }

        #s-price .smart-banner {
            position: absolute;
            width: 32%;
            line-height: 36px;
            max-height: 40px;
            display: -webkit-box;
            -webkit-box-pack: center;
            display: -ms-flexbox;
            -ms-flex-align: center;
            -webkit-box-orient: vertical;
            -ms-flex-orient: vertical;
            z-index: 100;
            -moz-border-radius-topleft: 0;
            -moz-border-radius-topright: 2px;
            -moz-border-radius-bottomright: 2px;
            -moz-border-radius-bottomleft: 0;
            visibility: hidden;
            background: #DD2727;
            right: 10px;
            top: 0;
            height: 100%;
            -webkit-border-radius: 0 2px 2px 0;
            border-radius: 0 2px 2px 0
        }

        #s-price .smart-banner .text-box {
            position: absolute;
            top: 0;
            right: 0;
            line-height: 21px;
            height: 100%;
            width: 70%
        }

        #s-price .jhs-tag,#s-price .wrt-pre-price .arrowPoint {
            position: relative
        }

        #s-price .smart-banner .text-box .t {
            color: #fff
        }

        #s-price .smart-banner .text-box .more {
            color: #fff;
            font-size: 8px
        }

        #s-price .smart-banner::before {
            content: '\e612';
            width: 10px;
            font-size: 17px;
            font-family: tm-detail-font;
            margin-left: 5px;
            color: #fff
        }

        #s-price .pnormal {
            width: 100%
        }

        #s-price .pnormal .mui-price {
            word-break: break-all;
            white-space: normal
        }

        #s-price .pnormal .mui-price img {
            -left: 5px;
            vertical-align: baseline
        }

        #s-price hr {
            background: #000;
            opacity: .1
        }

        #s-price .dollor-noline {
            text-decoration: none;
            color: #666;
            font-weight: lighter;
            margin-right: 9px;
            white-space: nowrap
        }

        #s-price .dollor-noline .t {
            font-size: 10px
        }

        #s-price .dollor-noline .b {
            font-size: 18px
        }

        #s-price .ui-label,#s-price .ui-mark-qm {
            display: inline-block;
            vertical-align: middle;
            font-size: .12rem
        }

        #s-price .big {
            margin-right: 14px;
            white-space: nowrap
        }

        #s-price .jhs-tag {
            background: #FE13A1;
            color: #fff;
            padding: 1px 2px;
            top: -3px
        }

        #s-price .restrict {
            color: #c40000
        }

        #s-price .ui-yen {
            vertical-align: middle;
            font-weight: lighter
        }

        #s-price .ui-preSellPrice b,#s-price .ui-prepayfee b {
            font-weight: 400
        }

        #s-price .ui-price-wrap {
            margin: 0 5px 0 0
        }

        #s-price .rule .ui-label {
            color: #999
        }

        #s-price .ui-label {
            line-height: .12rem;
            height: .12rem;
            color: #888
        }

        #s-price .ui-mark-qm {
            color: #999;
            border: 1px solid #999;
            width: 18px;
            height: 18px;
            line-height: 18px;
            border-radius: 18px;
            text-align: center
        }

        #s-price .ui-mark-clock {
            width: 12px;
            height: 12px;
            display: inline-block;
            vertical-align: middle;
            background: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyRpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoTWFjaW50b3NoKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDozMzQyNjc2M0RCQkIxMUUzQTJGQkIwQ0M5RTBBNTJENSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDozMzQyNjc2NERCQkIxMUUzQTJGQkIwQ0M5RTBBNTJENSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjMzNDI2NzYxREJCQjExRTNBMkZCQjBDQzlFMEE1MkQ1IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjMzNDI2NzYyREJCQjExRTNBMkZCQjBDQzlFMEE1MkQ1Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+4cat7QAAAsVJREFUeNqsls+LUlEUx4+voBl1ECRH3TSCP4iarQqzm4W20FrJNLXPltEi6h+IalFDS11PPxk3geAqA93ouhxQLB0EpXIxkqKE2vne7hteju9l5IGv77377vmcd++59x5NqVSKDOwC6yZrm3WJ5WD9ZLVYTdYH1hvWFz3AWfwkk8k/GtPpNMCPWTusMzM+51gXpa6wHsogD5hzNMP5HWCm8Rpf9llreDabzeTxeMjhcJDFYqHxeEyDwYC63S41Gg3cK9ztBusq+3KM5KtTI9DA7/DlGUsBOBwOk9frJUVR5g5/a2uL6vU6lUolBLJy0wtmuDjIntpH0cCv8wUvFJfLRYlEgvx+vy5cOPM79EFf+LCZWE8lS5hJJhlz/ollRcd4PG4InmeTyYSy2Sy12208/mBdZh2plEeAY46j0aguHPOOHOiNJhKJiDyBxXqiTtEGaxcPwWCQVlZW5gKq1SplMhkqFAq6o4BvKBRSH7ECNxQJF0n1+Xy6zlg5sF6vZzhVWBRgyY/fVeQmEkvxX+ddb6rAkrYN4ibu1tfXaVmmYW0iwHl1Qy3LNCzHyZzorQ7t0GGdTocODw8N+2pZ8PquTaKeBQIBstvt4h4rySiIhvVNkRtMrHEjwxLEBlwkiIb1EQHe4w4HF3bj/wYBo9lsqo95RR61EwwLB9ffbDZIsVik4+Pjk/dg9Pt9EQtsBGiw3qKlXC7TcDhcOIjT6SSbzUarq6uiHb5gSAOzceqwc7vdFIvFlnvYyUp0izVFh1wut9BIVBuNRsJHwqdgqdXt5DO54TVf7uJjWq2WONhqtZph4qfTqehzcHBA8JHwe5J1uqLxi+dcLFDA9zlRa/l8XlQrtWRarVaxiZBETclU3TEtt5nxUrdkyiDvOMimPM93UHMrlYrh9MuE3p8t+tqKtsjfFiRNPcW+ykWRl/DPeoBfAgwAsOk7YKEmFkMAAAAASUVORK5CYII=);background-size: 12px 12px
        }

        #s-price .ui-preSellPrice .ui-label,#s-price .ui-prepayfee .ui-label {
            line-height: .16rem;
            height: .16rem;
            display: inline-block;
            vertical-align: middle
        }

        #s-price .ui-prepayfee {
            color: #DD2727;
            font-size: .16rem
        }

        #s-price .ui-prepayfee span {
            position: relative;
            top: -2px;
            font-size: .24rem
        }

        #s-price .ui-prepayfee span b {
            font-size: .18rem
        }

        #s-price .ui-prepayfee .ui-label {
            font-size: .16rem;
            color: #051B28
        }

        #s-price .ui-preSellPrice {
            color: #051B28;
            font-size: .16rem
        }

        #s-price .ui-preSellPrice .ui-label {
            font-size: .16rem;
            color: #051B28
        }

        #s-price .depositexpandtext,#s-price .stagingtext {
            color: #DD2727;
            font-weight: lighter
        }

        #s-price .depositexpandtext {
            font-size: 16px
        }

        #s-price .delayposttime,#s-price .finalpaymenttimetext {
            font-weight: lighter;
            color: #999
        }

        #s-price .wrt-pre-price .ui-yen {
            color: #000;
            display: inline-block;
            vertical-align: middle
        }

        #s-price .wrt-pre-price .ui-yen.preSellStage1 {
            color: #c40000
        }

        #s-price .wrt-pre-price .extra-tip .ui-yen {
            color: #051b28;
            font-size: .12rem
        }

        #s-price .wrt-pre-price .arrowPoint:after,#s-price .wrt-pre-price .arrowPoint:before {
            display: block;
            width: 0;
            position: absolute;
            border-style: solid;
            bottom: -8px;
            content: "";
            height: 0;
            left: 50%
        }

        #s-price .wrt-pre-price .arrowPoint:before {
            border-width: 9px;
            border-color: transparent transparent #e5e5e5;
            margin-left: -10px
        }

        #s-price .wrt-pre-price .arrowPoint:after {
            border-width: 8px;
            border-color: transparent transparent #fff;
            margin-left: -9px
        }

        #s-price .item,#s-price .item:last-child {
            margin: 0
        }

        #s-price .item.gift a,#s-price .item.rule a {
            font-weight: 700
        }

        #s-price .item.rule a {
            margin-left: 10px
        }

        #s-price .wrt-stage {
            display: -webkit-box;
            display: -ms-flexbox;
            display: flex;
            margin: 7px 0 0;
            border: 1px solid #e5e5e5;
            color: #999;
            font-size: .12rem
        }

        #s-price .wrt-stage .wrt-stage-item {
            margin: 10px 0;
            padding: 2px 0;
            text-align: center;
            -webkit-box-flex: 1;
            -ms-flex: 1;
            flex: 1;
            flex-basis: auto;
            display: block;
            flex-basis: 1px;
            border-right: 1px dotted #e5e5e5
        }

        #s-price .wrt-stage .wrt-stage-item .text {
            line-height: 1.5em
        }

        #s-price .wrt-stage .wrt-stage-item .price {
            line-height: 1.2em
        }

        #s-price .wrt-stage .wrt-stage-item .price.del {
            text-decoration: line-through
        }

        #s-price .wrt-stage .wrt-stage-item:last-child {
            border-right: none
        }

        #s-price .wrt-stage .wrt-stage-item.current {
            color: #333;
            font-weight: 700;
            position: relative
        }

        #s-price .wrt-stage .wrt-stage-item.current::after {
            content: "";
            display: inline-block;
            height: 0;
            position: absolute;
            width: 90%;
            top: 100%;
            left: 50%;
            margin-top: .08rem;
            margin-left: -45%;
            border-bottom: .03rem solid #000
        }

        #s-price .price-bar,#s-price .tag {
            position: relative
        }

        #s-price .ui-timer {
            color: #051b28
        }

        #s-price .ui-timer .num {
            display: inline-block;
            height: .12rem;
            line-height: .12rem;
            vertical-align: 0;
            font-family: avenir;
            font-weight: 400
        }

        #s-price .price-origin {
            vertical-align: middle;
            font-weight: lighter;
            font-size: .12rem;
            color: #888
        }

        #s-price .reserve-count,#s-price .reserve-count .ui-label {
            color: #f7132c
        }

        #s-price .reserve-count .count {
            font-size: .12rem;
            line-height: .12rem;
            height: .12rem;
            display: inline-block;
            vertical-align: middle;
            margin-left: 3px
        }

        #s-price .tag {
            background: #fb6878;
            color: #fff;
            height: .14rem;
            line-height: .14rem;
            display: inline-block;
            padding: 0 .04em;
            margin-right: .07rem;
            vertical-align: middle;
            font-size: .1rem;
            right: -5px
        }

        #s-price .tag.tag-ju {
            background-color: #dd2727
        }

        #s-price .tag.tag-ju::before {
            display: none
        }

        #s-price .price-bar .nowrap {
            white-space: nowrap;
            display: inline-block
        }

        @-webkit-keyframes zoomIn {
            0% {
                transform: scale(.01);
                opacity: 0
            }

            100% {
                opacity: 1;
                transform: scale(1)
            }
        }

        @keyframes zoomIn {
            0% {
                transform: scale(.01);
                opacity: 0
            }

            100% {
                opacity: 1;
                transform: scale(1)
            }
        }

        #s-price .price-bar .tm-multi-wrap {
            position: absolute;
            right: 5px;
            top: -20px;
            width: 154px;
            border: 1px solid #c40000;
            overflow: hidden;
            -webkit-animation: .3s zoomIn;
            animation: .3s zoomIn
        }

        #s-price .price-bar .tm-multi-text-wrap {
            float: left;
            border-right: 1px dashed #bbb;
            width: 85px;
            -webkit-transform: scale(.75);
            margin: 1px 0 0 -1px
        }

        #s-price .price-bar .tm-multi-vm {
            display: table-cell;
            height: 40px;
            vertical-align: middle
        }

        #s-price .price-bar .tm-multi-text {
            font-size: 12px;
            line-height: 1.2;
            vertical-align: middle
        }

        #s-price .price-bar .tm-multi-text strong {
            color: #c40000;
            padding: 0 3px
        }

        #s-price .price-bar .tm-multi-icon-wrap {
            float: left;
            width: 65px;
            padding: 7px 0 0;
            overflow: hidden;
            cursor: pointer;
            height: 37px
        }

        #s-price .price-bar .tm-multi-icon {
            float: left;
            width: 27px;
            height: 27px;
            background: url(//img-tmdetail.alicdn.com/tps/i3/TB1y2cpHXXXXXcHXVXX.vHKHpXX-54-54.png) no-repeat;
            background-size: 27px 27px
        }

        #s-price .price-bar .tm-multi-icon-text {
            float: left;
            margin: 0 0 0 5px;
            color: #c40000;
            line-height: 1.2;
            -webkit-transform: scale(.75)
        }

        #s-price .price-bar .tm-multi-icon-arrow {
            float: left;
            margin: 7px 0 0;
            width: 7px;
            overflow: hidden;
            height: 10px;
            position: relative
        }

        #s-price .price-bar .tm-multi-icon-arrow:after,#s-price .price-bar .tm-multi-icon-arrow:before {
            border: 5px solid transparent;
            border-left: 5px solid #fff;
            width: 0;
            height: 0;
            position: absolute;
            top: 0;
            right: -3px;
            content: ' '
        }

        #s-price .price-bar .tm-multi-icon-arrow:before {
            border-left-color: #c40000;
            right: -5px
        }

        #s-price .ui-point-wrap {
            font-size: .12rem;
            line-height: .12rem;
            display: inline-block;
            vertical-align: middle;
            margin: 0 0 0 5px
        }

        #s-price .ui-point-plus {
            vertical-align: 0
        }

        #s-price .ui-point-number {
            color: #051b28;
            font-weight: 700
        }

        #s-price .restrict {
            padding-bottom: .07rem
        }

        .happy11Price .ui-label,.happy11Price b {
            color: #dd2727;
            font-weight: 400
        }

        #s-adds #J_indPanel .delivery,#s-adds #J_indPanel div {
            text-overflow: ellipsis;
            white-space: nowrap;
            overflow: hidden
        }

        #s-adds {
            margin-bottom: 10px
        }

        #s-adds #J_interTdsPanel {
            padding: 5px 10px;
            text-align: left;
            color: #666
        }

        #s-adds #J_interTdsPanel hr {
            background: #000;
            opacity: .1
        }

        #s-adds #J_interTdsPanel>div {
            line-height: 20px
        }

        #s-adds #J_interTdsPanel .ct,#s-adds #J_interTdsPanel .t {
            color: #666
        }

        #s-adds #J_interTdsPanel .inter-blue {
            color: #5C2694;
            margin-left: 11px
        }

        #s-adds #J_indPanel {
            padding: 0 10px;
            text-align: left;
            line-height: 30px;
            color: #999;
            font-weight: lighter
        }

        #s-adds #J_indPanel .delivery {
            text-align: right
        }

        #s-adds #J_indPanel .sales,#s-adds #J_indPanel div {
            text-align: center
        }

        #s-adds #J_indPanel div:last-child {
            text-align: right
        }

        #s-adds #J_indPanel div:first-child {
            text-align: left
        }

        #s-advantage .module-scene-highlight,#s-advantage .module-scene-highlight hr {
            border-top: 1px solid rgba(240,231,215,.5)
        }

        #s-advantage .module-scene-highlight {
            background: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAATCAIAAAB3HTjaAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAkZJREFUeNq8lduO0zAQhj0e20na3aqUFQsS4v3fhhuuuEACcVq0pVV6jg/Db2cpFWJLYaGR68h1Mt8c/nHIbxfqXJdWZ7zM6Y8Gv+u2q+C7GCKWbNhY5+qhsdW/gRHp3WbRzm9Xy1mKQRPlZIj0e0qlJMJsBhePRuOrqrkUSces3V8z6raL+e2nzXpBhUEagykvsFQCJq4UJQEAqDSDy/HVM1df5s3TYTA2n35uZzcpJTZOa6MNF1Lh7WEgScKcQkz4hU5rPZpcjydP5WSYTG/erdo5aWOtw4TB7L7DdEmgyoHlLGZYjF0KHsP7TlIYjsaPr18Un4/WDEn6+uXDerlgWxtXGVNr69gYBAd22T9wSsqUAqcqhpB8R2Ybuh1e1/rj5MlzETkGW7Wz9bJl66xrjG3YVmyt1qyIC+eQ1VcGBg1LZI6RrfZwi323gZGqnqGI98FIJC7bKWVJD2w1MK7WXGLKmqBflvYuVoFOETrjecxQk++AmzYXIyLe68W8ff0SuVacpYZHkT1g8rANStXL7/cdlDWKeto8qSIgEr9bv3/zCg2DqqqYYA0yc7mbwCI0DASBgJoSU589Or3rC5CRLEOQasLklYrktQCVYNBU9TClSKWN8hqiAEn/MenHKaBZBHbqskSyfBatSjlvrhlBT33bIjRdBoL8G9Kex+hFRw53F2PoWx5/w4NhlnBRWp+IfFLQgw7o/DqchimIIKtfegRqVh2cLr0a6OEHfHaaIW8+NG767vk/n5SfXT/r9+yssG8CDAAo/QgrgjX2OgAAAABJRU5ErkJggg==) bottom repeat-x #FBF8F3;background-size: 10px;
            padding-bottom: 5px
        }

        #s-advantage .module-scene-highlight .cainiao-icon,#s-advantage .module-scene-highlight .cainiao-wrap .cainiao-cell,#s-advantage .module-scene-highlight .server-list li,#s-advantage .module-scene-highlight .wuyougou {
            color: #666
        }

        #s-advantage .module-scene-nologo {
            margin-top: 10px;
            border-top: 1px solid #F0E7D7
        }

        #s-advantage .module-scene {
            padding-top: 10px;
            font-size: 16px;
            font-weight: 700;
            margin-bottom: -5px;
            color: #000
        }

        #s-advantage .module-scene img {
            margin: 0 5px 0 10px;
            height: .18rem;
            vertical-align: text-bottom
        }

        #s-advantage .logo {
            margin-top: 10px;
            background: #fff;
            line-height: 1
        }

        #s-advantage .logo img {
            height: .2rem;
            padding: 10px 0 5px 10px
        }

        #s-advantage .cainiao-wrap {
            padding: 10px
        }

        #s-advantage .cainiao-wrap .cainiao-cell {
            width: 100%;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis
        }

        #s-advantage .cainiao-more::after,#s-advantage .server-list::after {
            right: 10px;
            content: '\e606';
            font-family: tm-detail-font;
            width: 17px;
            font-size: 18px;
            color: #B5B5B5
        }

        #s-advantage .cainiao-more {
            position: relative
        }

        #s-advantage .cainiao-more::after {
            position: absolute;
            top: 10px;
            line-height: 14px
        }

        #s-advantage .cainiao-icon img,#s-advantage .cainiao-icon span {
            height: .14rem;
            line-height: 1;
            vertical-align: text-bottom;
            margin-right: 5px
        }

        #s-advantage #J_service {
            padding: 10px 0
        }

        #s-advantage .server-list {
            position: relative;
            padding-left: 4px
        }

        #s-advantage .server-list li {
            display: none;
            margin-left: 6px
        }

        #s-advantage .server-list li:nth-child(1),#s-advantage .server-list li:nth-child(2),#s-advantage .server-list li:nth-child(3),#s-advantage .server-list li:nth-child(4),#s-advantage .server-list li:nth-child(5),#s-advantage .server-list li:nth-child(6),#s-advantage .server-list li:nth-child(7),#s-advantage .server-list li:nth-child(8) {
            display: inline
        }

        @media screen and (max-width:330px) {
            #s-advantage .server-list li:nth-child(7),#s-advantage .server-list li:nth-child(8) {
                display: none
            }
        }

        #s-advantage .server-list::after {
            position: absolute;
            top: 0;
            padding: 1px;
            border-radius: 2px;
            -webkit-border-radius: 2px;
            -moz-border-radius: 2px;
            line-height: 14px
        }

        #s-advantage .wuyougou {
            color: #000;
            padding: 0 10px
        }

        #s-advantage .wuyougou img {
            height: .14rem;
            line-height: 1;
            vertical-align: text-bottom;
            margin-right: 5px
        }

        #s-advantage hr {
            border: 0;
            height: 0;
            margin: 0 10px;
            border-top: 1px solid #eee
        }

        #s-advantage .certification-list {
            padding: 10px 0;
            line-height: 18px
        }

        #s-advantage .certification-list img {
            height: 18px;
            margin: 0 5px 0 10px;
            vertical-align: top
        }

        .deliveryInfo {
            height: 40px;
            line-height: 40px;
            padding: 0 0 0 10px;
            vertical-align: middle
        }

        #s-action-container {
            background-color: #f5f5f5;
            margin: 10px 0
        }

        #s-action-container .extraTip {
            padding: 5px 0 5px 10px
        }

        #s-action-container .extraTip a,#s-action-container .extraTip a:hover {
            color: #1193ce
        }

        #s-action-container .sku {
            display: -webkit-box;
            -webkit-box-align: center;
            display: -ms-flexbox;
            -ms-flex-align: center;
            background-color: #fff
        }

        #s-action-container .sku .content {
            -webkit-box-flex: 1;
            -ms-flex: 1;
            flex: 1;
            flex-basis: auto;
            display: -webkit-box;
            -webkit-box-align: center;
            display: -ms-flexbox;
            -ms-flex-align: center;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            padding: .7em 10px;
            display: block;
            line-height: 2em;
            font-size: 14px
        }

        #s-action-container .sku .content:before {
            content: '\e606';
            font-family: tm-detail-font;
            float: right;
            background-size: 7px;
            width: 7px;
            margin-right: 10px;
            font-size: 18px;
            color: #999
        }

        #s-action-container .restrict {
            padding: 10px 10px 0;
            color: #939393
        }

        #s-action-container .restrict strong {
            color: #c40000
        }

        #s-action {
            background-color: #f5f5f5;
            border: none;
            display: -webkit-box;
            display: -ms-flexbox;
            display: flex;
            position: relative
        }

        #s-action.float {
            position: fixed;
            left: 0;
            right: 0;
            bottom: 0;
            background-color: #fff
        }

        #s-action .countdown,#s-action button {
            -webkit-box-flex: 1;
            -ms-flex: 1;
            flex: 1;
            flex-basis: auto;
            display: block;
            flex-basis: 1px
        }

        #s-action .buy {
            border: 1px solid #fb6878;
            -webkit-border-radius: .5em;
            border-radius: .5em;
            padding: .8em;
            font-size: .14rem;
            min-width: 7em;
            background-color: #fff0f0;
            color: #c40000;
            margin-right: 10px
        }

        #s-action .buy:disabled {
            background-color: #999;
            border-color: #999;
            color: #fff
        }

        #s-action .countdown {
            display: none;
            height: .4rem;
            line-height: .4rem
        }

        #s-action .countdown .label {
            color: #999
        }

        #s-action .countdown .time {
            padding: 0 0 0 .03rem;
            color: #c40000
        }

        #s-action .countdown .num {
            font-weight: 400
        }

        #s-action .cart,#s-action .chaoshiCart {
            border: 1px solid #c40000;
            -webkit-border-radius: .5em;
            border-radius: .5em;
            padding: .8em;
            font-size: .14rem;
            min-width: 7em;
            background-color: #c40000;
            color: #fff
        }

        #s-action .chaoshiCart.shorter {
            margin-left: 50px
        }

        #s-action .cart-link {
            position: absolute;
            width: .4rem;
            height: .4rem;
            background: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyRpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoTWFjaW50b3NoKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpBNTE3QjFGN0NFMzcxMUUzODYwM0NDQTFGRDY5QzlFRSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpBNTE3QjFGOENFMzcxMUUzODYwM0NDQTFGRDY5QzlFRSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjVBOTA4MEYwQ0UwRTExRTM4NjAzQ0NBMUZENjlDOUVFIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkE1MTdCMUY2Q0UzNzExRTM4NjAzQ0NBMUZENjlDOUVFIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+mLbMvQAAAnlJREFUeNrMWL1LHEEUnz3PTxQLERQEU2ihmMIvVFALTSOksRUF0X8gtYWkSWKhhWAjiGgh2oqNgo2eiJJDwcqPED8KDUSiIRg0GNbfgzl5DOfuzu3Orj/4cTd3+2Z/zLz35r0RQohB8A94C/5iPAcnwCJhELZtO5JwQ885cBW0ohR46CKQ2B2VQFqZenAKzGV29HsDG8+Cw6YEZooLtoLHUW7xS1hQtrk8CoFxB9sE2MfGKzKgAoVlpY2/O/A3+MXJttZD8JjmvlP6oP9+giUiOtzFnNwD3BLRYjfm8kBCGb+XKxsYEQjPxDhL+l4KO24CN5Vxh+EVqwGLuUA3g7g8p1NOu20yzcjDgAdJqZc51pjBA1hgUOAMe9cJ/RbzMAf3wxywxeAWN6nbqyuQ0GlCGRJ2IT7qdPwvhTzwni39uoktBroU/2vUKSQSzJCOoWwDAkeUd8S9+qBQEjYFSaOBXW5l35MQ9+jVB9P5YbthgTu6xpQ8H9kWLAdcsFYp/tfLK2qv2JPVN+FvwEm7hM1NKIe4H7qTTIZUYp3w5B3TELgZUgUzl6nhW8MrR7l2mioarz2JijZlwg9RN00qxpm4/2DZaxJI0X7KBG68hq6Og06ON2z8DXxnoKu7Bs8g7FZ3nrEQOzm6uKrV3eJkyO3mJ908+D3kbu6QO78XUAUzBFYYFpZPzTo4b8v9tUTEcPOzTAVa0lcCEYhItmw/93AMzeCRLL2WwMIA5vwoK2iK3gE/Jwl1/WdKtH32Ka5Hme8fWJ1pNUNHW2WaBO4Hqj31OvxmV0vgVZp0k/QpcE8Z0wp+9dtYH8jSaDGgW4ZReTF6CfarPvgkwACn4Gx5Tjsz9gAAAABJRU5ErkJggg==) 50% 50% no-repeat;background-size: 20px;
            right: .1rem;
            top: .2rem;
            margin-top: -10px
        }

        #s-action .cart-link i {
            color: #fff;
            -webkit-border-radius: 1em;
            border-radius: 1em;
            padding: 0 .4em;
            -webkit-box-sizing: border-box;
            box-sizing: border-box;
            background-color: #000;
            position: absolute;
            top: -10px;
            left: 14px;
            line-height: 1.5;
            min-width: 1.5em;
            height: 1.5em;
            -webkit-transform: scale(.7);
            -webkit-transform-origin: left bottom;
            text-align: center
        }

        #s-action .cart-link i::after {
            content: "\20";
            position: absolute;
            left: 0;
            bottom: 0;
            border: 5px solid #000;
            border-color: transparent transparent #000 #000
        }

        #s-rate {
            margin-bottom: 10px
        }

        #s-rate .mui-tagscloud .mui-tagscloud-more button {
            height: 26px;
            line-height: 26px;
            padding: 0 12px
        }

        #s-shop {
            padding: 10px;
            background-color: #fff;
            border-top: none
        }

        #s-shop .main {
            position: relative;
            font-size: .14rem
        }

        #s-shop .main .img-box {
            max-width: 54px;
            max-height: 54px;
            position: relative;
            left: 50%;
            width: 100%;
            margin-left: -50%
        }

        #s-shop .main .img-box:before {
            content: '\20';
            padding-top: 100%;
            display: block
        }

        #s-shop .main .img-box img {
            max-width: 100%;
            max-height: 100%;
            position: absolute;
            top: 50%;
            left: 50%;
            -webkit-transform: translate3d(-50%,-50%,0)
        }

        #s-shop .main .shop-logo .img-box {
            border: 1px solid rgba(151,151,151,.5)
        }

        #s-shop .main .shop-ct .shop-t {
            text-indent: 6px;
            color: #333;
            font-size: 16px
        }

        #s-shop .main .shop-ct .shop-t img {
            width: 14px;
            height: 14px;
            border-radius: 7px;
            -webkit-border-radius: 7px;
            -moz-border-radius: 7px
        }

        #s-shop .main .shop-ct .shop-t a {
            color: #333
        }

        #s-shop .main .shop-ct .down-t {
            font-size: 12px;
            padding-left: 7px
        }

        #s-shop .main .shop-ct .down-t .dark {
            background: #5C2695;
            color: #fff
        }

        #s-shop .main .shop-ct .down-t .light {
            background: #FAF7FE;
            border: 1px solid #5C2695;
            color: #5C2695
        }

        #s-shop .main .yao-qualification {
            margin-right: 10px;
            width: 54px;
            height: 27px;
            display: block;
            background-size: 54px 27px;
            background-image: url(//gtms04.alicdn.com/tps/i4/TB1577NHpXXXXaLXVXXn33Y_pXX-108-56.png);
            background-repeat: no-repeat;
            position: absolute;
            right: 0;
            top: 0
        }

        #s-shop .main .ww {
            background: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAAC8AAAAwCAYAAACBpyPiAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyRpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoTWFjaW50b3NoKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpBNTE3QjFGQkNFMzcxMUUzODYwM0NDQTFGRDY5QzlFRSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpBNTE3QjFGQ0NFMzcxMUUzODYwM0NDQTFGRDY5QzlFRSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkE1MTdCMUY5Q0UzNzExRTM4NjAzQ0NBMUZENjlDOUVFIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkE1MTdCMUZBQ0UzNzExRTM4NjAzQ0NBMUZENjlDOUVFIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+tvSlnQAABA1JREFUeNrUmnlsDGEYxqdr6z6Duv9CiLsh7hAhGoI4i6SOiJQ6gggRdUbrDBUJQSTOv6qVCqVEolTrqlJXFK0jKo0r7lTret7sszImszuz25k9nuSX7Uzn2z4z8x3v+36NiM/9qphUO3AanAF7wWMlyHL4cO0zUAKWgAKwB3QIF/O/QCKoAHVAAsgHm0DDUDcvug1yVMd1wQpwA0wD1UPZvOiCzrn24Ai4BpaDHiDSbvNOP9p4G6jRZCN4BHLBZY6RIvA72OZrm7imGuhM4kEleMCbuQLywMtgmB/qR5tIdiVhPvgMboFzIINvxfY+3w1MsOCN1wdDwGZwB5wCE0ENu8xLF0gD9SwedzXBKHAc3GQ3q2ml+dHgImcVO9UV7OOYGGaF+XngBGgawClcZqwskOTNo5H5VWC3nwO7qqrGFT2VK7pP5teCDUrwJRNEut4U7cn8YrBOCR3FgIMgwsi8DM7tIWI6i4NXFMtu7NF8a7Dfz5jHau0CI8Ag9nvRGtDHk3mJ0ZtbbOI5WM0E5qfJNrICb1GF4gvAG04cOzmY/zMfyy5jpb7wO5MY/09hnGOke+C16vgtOMaf+4LxavOyoq234dWfBPdVxzJrpJhoV+Sh/7u1UG1e4oqONpi/qnNuK3hn0K5CZ225yzcp6idxltv8HJsGXYnOufcMxLxpBihkvLMSxPGGnqii4Rgx30U9gi3WGDAARGnOnzVoVwt0Yo9IBtM52F+pruktdzDcxpQtgcjTfsqBmMZYvtLk35XFaS6ffLm6FCNPvlcA5uzGfLuxvJFSfhppB5hF49rZsbEctA3QolMGBrNc8gd8NLheZqWlmnPqNai2I0Chrhgdy6xJYSJulIx/0xw34/h0K8JhNmupgmRanASua7KnBgbtZjJddCtOU9wqd5hc8aqiozq1nlY6M5CiE2el0PAIhhhqfXIwjrBTMl3W1ZwzO8PJYH0IMnXeVJlDE0PYobaakKAlWOZD+xbaON4dQjhZARtu8w3MpulCBlVtLPjOfCcXjEBoJLFCslhlS7fJBt+V8JLMXMUOJgt5YWb+kHq53R9Gxp8z2vxnPoPxcjgo2b36us1XMm4OdcnmxWG9BDxTlSeGor4xvK70VD1YpLh2NEJRi1WBna75D2CqyVg70P38gPakXnFJ7m5cCN2A7G+t0vuFp8pYDkOGYO5y/1BcW0CJni7wVtYrYOaTHgTjEgNJcXWPt4uMapJlzOBneChj2JG4SNw+EFwyuthsQVU2iHsqrg3iYhtMS0lDSoLR/DT13xy+7HhIHrpNcRVMJbOZzCcU5afhUsZUsmV0njOdT/Jnu0ZKbqmkCegO+rNIJHF6I8W1DeNkkl3ONlIsfaG4apf5zJA+VeV1/RVgAH141HdD0wkSAAAAAElFTkSuQmCC) 50% 50% no-repeat;background-size: 24px;
            height: 2em;
            width: 55px;
            display: block;
            text-indent: -999em;
            overflow: hidden;
            padding: .5em 0
        }

        #s-shop .main .ww::before {
            content: "\20";
            float: left;
            height: 2em;
            width: 0;
            border-right: 1px solid #e5e5e5
        }

        #s-shop .score {
            display: -webkit-box;
            display: -ms-flexbox;
            display: flex;
            text-align: center;
            color: #999;
            padding: 1em 0 0
        }

        #s-shop .score b {
            padding-right: 14px;
            background-position: 100% 50%;
            background-repeat: no-repeat;
            background-size: 12px;
            font-weight: 400
        }

        #s-shop .score .Up,#s-shop .score .up {
            color: #e13e4c;
            background-image: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAABcAAAAXCAYAAADgKtSgAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyRpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoTWFjaW50b3NoKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpBNTE3QjFGRkNFMzcxMUUzODYwM0NDQTFGRDY5QzlFRSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpBNTE3QjIwMENFMzcxMUUzODYwM0NDQTFGRDY5QzlFRSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkE1MTdCMUZEQ0UzNzExRTM4NjAzQ0NBMUZENjlDOUVFIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkE1MTdCMUZFQ0UzNzExRTM4NjAzQ0NBMUZENjlDOUVFIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+GyNHKwAAAUtJREFUeNpifGjnw4ADMAGxExAHAbElECsAsQAQfwDiB0B8HIjXAfE+IP6HzQBGHIaDBDuBWIuBMLgGxOVAvAWb65ABGxDPAOLNRBrMAFUHUj8Tqh+r4SCJjUCcjssUuYOb8VmSBtXPhs3wKUDsQchgAhaA9E9FN9wbiFOJdTEBC1KgcQY2nAkaeSQFBQELukDmwpKbNhkG4JPXBGJnFmg6xgoe2ftiNQhZHA8IBLncioE2wBJkuDyNDFdkgmZpWgB+JmhZQQvwEWT4QxoZfp8JWrrRAhwHGb6WRoZvYIKWx9epbDDIvD1M0IK+jMqGg8z7Byu4QAX9HCoZPBdWcSDXRLDy3IMCg3cCsR8Q/0Ivz0EC/hT4YC6ywdiquV/Qct2XhEi+DlWfgmwwCLDg0AAKs22gYhOp9lcEYj4g/gTKIEi1/15ctT9AgAEAEd1OJQ/YU3gAAAAASUVORK5CYII=)
        }

        #s-shop .score .Down,#s-shop .score .down {
            color: #00b78b;
            background-image: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAABcAAAAXCAYAAADgKtSgAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyRpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoTWFjaW50b3NoKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpFRTM4OENDMENFNDgxMUUzODYwM0NDQTFGRDY5QzlFRSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpFRTM4OENDMUNFNDgxMUUzODYwM0NDQTFGRDY5QzlFRSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkVFMzg4Q0JFQ0U0ODExRTM4NjAzQ0NBMUZENjlDOUVFIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkVFMzg4Q0JGQ0U0ODExRTM4NjAzQ0NBMUZENjlDOUVFIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+QGIr3QAAAUxJREFUeNq0lcFKQkEUhschXJpPULqsnkCXKnQhJfIZrIVtQ5/BallQ0H2FQAoySre+QS2rN1CXd6H9J87kIB7nKnd++ECuM99c7syck1IvV0qIBiVQBwWQA1kwAt9gCB7BAEyXCVKCvAo6YF+58wHa4HnZ29lJgzvwFFOseByNv+f5/9laEHdBoDbLGdgBxyBalN+sEs+Ci/m37F1Lw2j+LTi1P8uReZBAGrxnf3LNm5dkLslrjttBwvI9UNZ8jn3khORFT/ICyXc9yfOar7SPbGuuFT4yJvmPJ/mX5urmI0OqihX8eHNdeSkrSsGh5nr8ueZE1//ke9dc6FvrChwLk29qChcV+oe4CzjEoWkcdrM4Bz3XAg7xK2hKbS7N9bixwekIWRxJbS7iul6TNlnYvBq/UBSnQZuFy1b3z4MMmNAFsbp/X+r+vwIMAOR+S7e76t7qAAAAAElFTkSuQmCC)
        }

        #s-shop .score .Equal,#s-shop .score .equal {
            color: #1193ce;
            background-image: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAABcAAAAXCAYAAADgKtSgAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyRpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoTWFjaW50b3NoKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpFRTM4OENDNENFNDgxMUUzODYwM0NDQTFGRDY5QzlFRSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpFRTM4OENDNUNFNDgxMUUzODYwM0NDQTFGRDY5QzlFRSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkVFMzg4Q0MyQ0U0ODExRTM4NjAzQ0NBMUZENjlDOUVFIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkVFMzg4Q0MzQ0U0ODExRTM4NjAzQ0NBMUZENjlDOUVFIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+5fruKAAAAUZJREFUeNq0lb9OQjEUh0tDHBF8AIVRmBxx1MVExeAzoAMwyzPACiaa4CNoIBojCbKy6eYdgRcg6ngH4u+YU9M0vRG47S/5lpv2S2//nJPItN9ERCQ4AOegCLIgDT7BFIzBAxiBhU2QiJCfgCbIi//zARrgybY6PRvgBjwuKRY8jsbf8vy/JA1xHxyJ9XIJtsEZCM2Vd2KIVWj+tbktx+BCuEmFz+xXLvnwXKZFXnXdCo7lu+BQ8j32kTLJ9z3Ji3QVd8yv8/reyqatzrv5KSf5SfvIpuRa4SNftC0zc/WWX1wnE8nVzUfGJL/3JO9JrseBYzH5hpIL/ZVjOfkWqnBRoe86Et+pxqGX3Bp4iSkegKqtE4Vc6LsxVlxSjcLW5kKu66crHHLA4yu62GxzemjPnqlsat0/B1Lgmx6I1v1fo7r/jwADAIPiPpmwv1IuAAAAAElFTkSuQmCC)
        }

        #s-shop .score li {
            -webkit-box-flex: 1;
            -ms-flex: 1;
            flex: 1;
            flex-basis: auto;
            display: block;
            flex-basis: 1px
        }

        #s-shop .score li:last-child {
            border: none
        }

        #s-shop .letter {
            height: 54px;
            margin: 10px -10px -10px;
            padding: 5px 16px 0;
            background: #faf8fe
        }

        #s-shop .letter .hide {
            display: none
        }

        #s-shop .letter .letter-bd {
            position: relative
        }

        #s-shop .letter .letter-bd::before {
            position: absolute;
            right: 0;
            top: 50%;
            content: '\e604';
            font-family: tm-detail-font;
            float: right;
            background-size: 7px;
            width: 7px;
            margin-right: 5px;
            font-size: 19px;
            color: #999;
            margin-top: -13px
        }

        #s-shop .letter .desc {
            color: #666;
            font-size: 16px;
            line-height: 50px;
            float: left;
            text-indent: 6px
        }

        #s-shop .letter .img-shop-box {
            float: left;
            max-width: 50px;
            max-height: 50px;
            position: relative;
            left: 50%;
            width: 100%;
            margin-left: -50%
        }

        #s-shop .letter .img-shop-box::before {
            content: '\20';
            padding-top: 100%;
            display: block
        }

        #s-shop .letter .img-shop-box img {
            max-width: 100%;
            max-height: 100%;
            position: absolute;
            top: 50%;
            left: 50%;
            -webkit-transform: translate3d(-50%,-50%,0)
        }

        #s-shop .action {
            margin-top: 10px;
            text-align: center
        }

        #s-shop .action .all-product {
            border-radius: 4px;
            -webkit-border-radius: 4px;
            -moz-border-radius: 4px;
            margin-right: 10px
        }

        #s-shop .action .all-product:before,#s-shop .action .go-shop:before,#s-shop .action .ww-link:before {
            width: 10px;
            font-family: tm-detail-font;
            margin-right: 6px;
            font-size: 17px
        }

        #s-shop .action .all-product:before {
            content: '\e60f'
        }

        #s-shop .action .go-shop {
            border-radius: 4px;
            -webkit-border-radius: 4px;
            -moz-border-radius: 4px
        }

        #s-shop .action .go-shop:before {
            content: '\e60d'
        }

        #s-shop .action .ww-link:before {
            content: '\e60a'
        }

        #s-shop .action>a {
            border: 1px solid #ccc;
            height: 38px;
            line-height: 38px;
            font-size: 14px;
            color: #666
        }

        .alihealthApp #s-shop .main .shop-ct .down-t {
            margin-top: 16px
        }

        #s-globalsell .globalsell-content {
            z-index: 1;
            background: #fff;
            height: 43px;
            width: 100%;
            position: fixed;
            bottom: 50px;
            transition: height .2s cubic-bezier(0,0,.25,1);
            font-size: 12px;
            max-height: 80%;
            min-height: 43px;
            display: flex;
            flex-direction: column;
            -webkit-transform: translate3d(0,0,0);
            transform: translate3d(0,0,0)
        }

        #s-globalsell .globalsell-content::after {
            content: '\20';
            height: 0;
            display: block;
            clear: both
        }

        #s-globalsell .globalsell-content .hd {
            color: #fff;
            line-height: 43px;
            height: 43px;
            padding-left: 8px;
            flex: 0;
            -webkit-box-flex: 0!important;
            -webkit-flex: none!important;
            background: #D50a17
        }

        #s-globalsell .globalsell-content .hd .bg {
            background-image: url(data:image/png;
            base64,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);background-repeat: no-repeat;
            width: 77px;
            height: 32px;
            background-size: 77px 32px;
            position: absolute;
            top: -25px;
            left: 20px
        }

        #s-globalsell .globalsell-content .hd::before {
            display: none;
            content: '\e614';
            font-family: tm-detail-font;
            float: right;
            width: 20px;
            margin-right: 10px;
            font-size: 16px;
            color: #fff
        }

        #s-globalsell .globalsell-content .globalsell-link {
            float: right;
            margin: 8px 10px 0 0;
            border: 1px solid #fff;
            line-height: 14px;
            padding: 5px;
            color: #fff;
            -webkit-border-radius: 2px;
            border-radius: 2px
        }

        #s-globalsell .globalsell-cover {
            content: "";
            position: fixed;
            top: 0;
            bottom: 50px;
            left: 0;
            right: 0;
            opacity: .7;
            display: none;
            background: #000;
            z-index: 1
        }

        #s-action-moda {
            background-color: #f5f5f5
        }

        #s-action-moda .extraTip {
            padding: 0 0 5px 10px
        }

        #s-action-moda .extraTip a,#s-action-moda .extraTip a:hover {
            color: #1193ce
        }

        #s-action-moda .sku {
            display: -webkit-box;
            -webkit-box-align: center;
            display: -ms-flexbox;
            -ms-flex-align: center;
            border-bottom: 1px solid #e5e5e5;
            margin-top: 10px;
            background-color: #fff
        }

        #s-action-moda .sku .content {
            -webkit-box-flex: 1;
            -ms-flex: 1;
            flex: 1;
            flex-basis: auto;
            display: -webkit-box;
            -webkit-box-align: center;
            display: -ms-flexbox;
            -ms-flex-align: center;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            padding: .5em 14px .5em 10px;
            display: block;
            line-height: 2em
        }

        #s-action-moda .sku .content:before {
            content: '\e606';
            font-family: tm-detail-font;
            float: right;
            background-size: 7px;
            width: 7px;
            margin-right: 10px;
            font-size: 19px
        }

        #s-action-moda .restrict {
            padding: 10px 10px 0;
            color: #939393
        }

        #s-action-moda .restrict strong {
            color: #c40000
        }

        .product-slider {
            overflow: hidden;
            display: block;
            background: #fff;
            margin-top: 9px
        }

        .product-slider h3 {
            padding: 10px
        }

        .product-slider .viewport {
            box-sizing: border-box;
            width: 100%;
            overflow: hidden;
            -webkit-transform: translateZ(0);
            position: relative
        }

        .product-slider .viewport .panel {
            width: 100%
        }

        .product-slider .viewport .panel .activity1111 .info::before {
            background-image: url(//gtms04.alicdn.com/tps/i4/TB1dzynKVXXXXaZXXXXZomoHFXX-56-24.png);
            background-size: 28px 12px;
            width: 28px;
            height: 12px;
            display: block;
            content: '';
            position: absolute
        }

        .product-slider .viewport .panel .activity1111 .info .title {
            text-indent: 2.6em
        }

        .product-slider .viewport .panel .intro {
            width: 33.333%;
            text-align: center;
            margin-bottom: .1rem;
            display: inline-block
        }

        .product-slider .viewport .panel .intro a div.img {
            position: relative;
            left: 50%;
            width: 85%;
            margin-left: -42.5%
        }

        .product-slider .viewport .panel .intro a div.img:before {
            content: '\20';
            padding-top: 100%;
            display: block
        }

        .product-slider .viewport .panel .intro a div.img img {
            max-width: 100%;
            max-height: 100%;
            position: absolute;
            top: 0;
            left: 0
        }

        .product-slider .viewport .panel .info {
            padding: 2px 5px
        }

        .product-slider .viewport .panel .info .title {
            text-align: left;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            text-overflow: ellipsis;
            display: -webkit-box;
            height: 2.4em;
            line-height: 1.25;
            overflow: hidden;
            color: #333
        }

        .product-slider .viewport .panel .info .title img.act-tag {
            width: auto;
            height: 12px;
            position: relative;
            top: 2px;
            margin-right: 2px
        }

        .product-slider .viewport .panel .info .cart {
            float: right;
            width: 2.3em;
            height: 2.3em;
            border-radius: 50%;
            background: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAAC4AAAAsCAMAAAAtr3oOAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyRpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoTWFjaW50b3NoKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpBM0VGOTFGMjI1RUIxMUU0Qjk4RkFENEU2OTIyOEJENyIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpBM0VGOTFGMzI1RUIxMUU0Qjk4RkFENEU2OTIyOEJENyI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjRDMkEyQzc1MjVCODExRTRCOThGQUQ0RTY5MjI4QkQ3IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjRDMkEyQzc2MjVCODExRTRCOThGQUQ0RTY5MjI4QkQ3Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+GPzq3wAAAu5QTFRF6jxQ6jtQ7D5T6jpP6z5T6jxR6zxR5zRJ6DhN5jJH5TBG5TFH5jNJ5jNI7k1g5jJI6UFU6ThN6TlO6ThO7UJW7UFV7UFW+MDH/ezu//7+//r7/OXo//3+/vn68Gd4+cPJ/vn571Bi6EZa7EJX9Kew/Ojq+tba7naE7neG/evt/ers/ODk/ejq/vDy736M/vT18ouX+9zg+9vf+s3S/vT273KB9q22/OTn86Cq//z8729//vf497zD5TJI85qk/vP05zhN8ZOf9pyn6kNY8pCc+tLX7V9w85ei/N/j6kpd5jVJ8pij6D1R9ay1/vLz711v//v7/vr750RX9Jei73qJ60db+tfc/fHz/vr696aw5zlP6ERZ9rG5+9jc60pd8YiU7FNl60da/Ofq9ri/+s/U96+4+cvR6EBV60xf7kte++Dj84uY70xf8W59+tfb+tXZ9aKs9Zyn72598oOQ5jVL6DtP8XiH7WBx7Vpt7ERY6D5S7Vps9Jej9Jyn9Ky162Jy8HiG8XSD72V29qWv9qSu856p+MHH+cjO+MTL8YSR9ZSg8pWg50BU+tXa9rC4+MnP8XuJ+9Xa6EVY9IeU9re+85um9Zum5TNH+LrB84WS8nOC83WF9JSg85mj7ENY96228oKP/OLl6EJW50BT7ltt7EFV/vX2+crP8oiV/Ofp60VZ9q+46kNX7E5h9aiy8Y6a7ENX74OQ8F5w8YSS+9fb7mR17GR08ISR85Wh+MDG+MnO9rS8+cXL+cTK7Fdq6T1S+LjA8YCO9KWv/ejr85ah9KWu97W98Y2Z7niH601g85ij8WZ29KCq7ktf7GN072x89I+c7W9+832L+sPJ61Zn+srP609i9bO77WBw+LzD846a+tPX7W5+97i/7lxu5jZM97S897C49qu0/e3v9KGr72x986Cp62Nz6DlO71Vo50NX5zZL7kRZ7kNY6DZL7UJX7kNX5TFG5zVK6TpP7EBV7D9U7kRY6z1S6DdM////vAgZlwAAAnZJREFUeNqM1FVUG0EAheEhhBZKaUuLBPdipbhD3d3d3d3d3d3d3d3d3d0NAhSSwn3rrO9pT3b3e5jJXv6TnDwEAqDloUytCM3RYPEfjYif3+1YePXRmhuNxsxQrDZqQ3Kox6iUow3JppbhbLY2xIlaBQNzlYGKCCfiQt11w096qeY+LsSZEY5H9OxZTFEXzHImJsYFTDapWZcKbxOXP0Csau6N1AATMTO8EbfVrGINnprNXH7AHfXVch8MpLmOtRArdcrMEeir0xE9az266pW9g/tIvZ7kswbjSL6y3qhKTz7fAvcA5XwFpjB5IVZ+JCYVUnQZE+lJbDjd8MpGSQd4NqUX4Q3AW6JkA2LY24ozDOFWSj7iE3MRa85euO2yVlADC5iL5PFCMSjPsnbATeYWc1dU/786/+zohKgvH6ZH18NGdiAleVfgKrzM6zE8fsfYeWfafnaTfhpt2D+RUryvMIxIOTwmYdrcsGQpauHx+uW+Exfvn6vMVcSRt0f2Tslh/U8mNJ6ZMrqj4z+IraAXyvebsXtp+/hqNX/YWkIKBNdwp0CVlEdhu4Y8S/ACkQVZakiGYGhDPMlQQ3JFzVCuQq4KWV5F4f+Xq5AXFuXeam455xtZTo0qYUEdIS8q4396G6KbyJchszvBkNRKfCYOkk0e7Afvl02d2WV+LeGZFJckIeiY/yX4dheXe/A83jrECyHCQOwkpVHWzs4hEQ/FZS3S6BmIusJA7CU0t7dPT8QbcamNNHoGYpwwkN+Sbwj6vugqfDeLy3h4HmwU7IVgYZDnc7ivel02LWGXU8vFvIjMr+dxiLkhX9Ir7oRh6nvx+a8AAwDxppSXjbnVCgAAAABJRU5ErkJggg==) center/1.5em 1.5em no-repeat #DD2727
        }

        .product-slider .viewport .panel .info .numbers {
            overflow: hidden;
            padding-left: 2px;
            line-height: 13px
        }

        .product-slider .viewport .panel .info .numbers .d-price {
            text-align: left;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            color: #ccc;
            text-decoration: line-through
        }

        .product-slider .viewport .panel .info .numbers .price {
            text-align: left;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            color: #333;
            font-weight: 700
        }

        .product-slider .viewport .scroller {
            display: -webkit-box;
            -webkit-box-lines: multiple
        }

        .product-slider .viewport .scroller a {
            -webkit-user-drag: none;
            -webkit-user-select: none;
            max-width: 100%;
            max-height: 100%
        }

        .product-slider .tm-recbanner {
            padding: 0 10px
        }

        .product-slider .tm-recbanner a {
            margin-top: 10px;
            display: block;
            width: 100%;
            height: 48.75%
        }

        .product-slider .tm-recbanner a img {
            vertical-align: top;
            max-width: 100%;
            max-height: 100%
        }

        .alihealthApp .product-slider .tm-recbanner {
            display: none
        }

        body,img {
            vertical-align: top
        }

        blockquote,body,dd,dir,dl,fieldset,figure,h1,h2,h3,h4,h5,h6,hr,input[type=radio],input[type=checkbox],input[type=range],menu,ol,p,pre,ul {
            margin: 0
        }

        button,dir,fieldset,input,input[type=radio],input[type=reset],input[type=checkbox],input[type=range],input[type=password],input[type=search],input[type=hidden],input[type=image],input[type=file],input[type=button],input[type=submit],isindex,legend,menu,ol,textarea,ul {
            padding: 0
        }

        h1,h2,h3,h4,h5,h6 {
            font-size: 100%;
            font-weight: 400
        }

        address,cite,dfn,em,i,var {
            font-style: normal
        }

        table {
            border-collapse: collapse;
            border-spacing: 0
        }

        fieldset,iframe {
            border: none
        }

        #description .module-content:after,#description .subtitle:after {
            border-top: 1px solid rgba(0,0,0,.1);
            content: '\20'
        }

        ol,ul {
            list-style: none
        }

        button,input,select,textarea {
            font-family: inherit;
            font-weight: inherit;
            font-size: inherit;
            margin: 0
        }

        button,input,select {
            color: inherit
        }

        html {
            -webkit-text-size-adjust: none
        }

        body {
            background-color: #fff;
            word-wrap: break-word;
            color: #051b28
        }

        body,div,ol,p,span,ul {
            word-break: break-all
        }



        #description .subtitle .newAttraction {
            height: 48px;
            line-height: 48px;
            color: #051B28;
            text-align: center;
            overflow: hidden;
            font-size: 16px
        }

        #description .subtitle:after {
            background: #eee;
            height: 10px;
            display: block
        }

        #description .module-container:last-child .module-content:after {
            display: none
        }

        #description .module-title {
            background: #FFF;
            height: 34px;
            line-height: 34px;
            position: relative;
            padding: 6px 0 0 12px;
            font-size: 14px;
            color: #666
        }

        #description .module-content:after {
            background: #eee;
            height: 10px;
            display: block
        }

        #recommend {
            max-width: 100%;
            overflow: hidden;
            background-color: #fff
        }

        #recommend h1 {
            background: #fff;
            height: 50px;
            line-height: 50px;
            text-align: center
        }

        #recommend h1:before {
            position: relative;
            content: '\20';
            display: block;
            border-top: 1px solid #dedede;
            top: 50%;
            width: 95%;
            margin: 0 auto
        }

        #recommend h1 span {
            padding: 0 5px;
            color: #bbb;
            background: #fff;
            position: relative;
            z-index: 1
        }

        #recommend ul {
            width: 95%;
            margin: 0 auto;
            overflow: hidden;
            zoom: 1
        }

        #recommend li {
            float: left;
            width: 50%;
            text-align: center
        }

        #recommend .goodsBox {
            text-align: left;
            border: 1px solid #dedede;
            width: 97%;
            margin: 2px auto
        }

        #recommend .goodsBox .imgBox {
            display: block;
            position: relative;
            left: 50%;
            width: 100%;
            margin-left: -50%
        }

        #recommend .goodsBox .imgBox:before {
            content: '\20';
            padding-top: 100%;
            display: block
        }

        #recommend .goodsBox .imgBox img {
            max-width: 100%;
            max-height: 100%;
            position: absolute;
            top: 50%;
            left: 50%;
            -webkit-transform: translate3d(-50%,-50%,0)
        }

        #recommend .goodsBox .priceBox {
            color: #d30000;
            padding: 0 5px;
            margin: 5px 0 0;
            font-size: 16px;
            position: relative
        }

        #recommend .goodsBox img.act-tag {
            width: auto;
            height: 12px;
            margin: 0 5px 0 0;
            position: absolute;
            right: 0;
            top: 5px
        }

        #recommend .goodsBox .double11-icon {
            display: inline-block;
            background: url(//gtms04.alicdn.com/tps/i4/TB1cbaoKVXXXXbQXXXXLKsfFVXX-28-12.png) no-repeat;
            background-size: 28px 12px;
            width: 28px;
            height: 12px;
            margin: 0 5px 0 0;
            position: absolute;
            right: 0;
            top: 5px
        }

        #recommend .goodsBox .titleBox {
            font-size: 16px;
            height: 35px;
            line-height: 1.2;
            overflow: hidden;
            padding: 0 5px;
            margin: 5px 0;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            text-overflow: ellipsis
        }

        #recommend .goodsBox .titleBox .title {
            color: #000;
            text-decoration: none
        }

        .lookForGoodsBar {
            background-color: #fff;
            border-radius: 3px;
            text-align: center;
            display: block;
            width: 95%;
            height: 50px;
            line-height: 50px;
            font-size: 20px;
            border: 1px solid #dedede;
            margin: 10px auto
        }

        .lookForGoodsBar i {
            font-style: normal;
            font-family: tm-detail-m-font;
            color: #5d5e5e
        }

        .lookForGoodsBar .keywords {
            margin: 0 0 0 15px
        }

        .lookForGoodsBar .keywords,.lookForGoodsBar .link {
            font-family: "\5fae\8f6f\96c5\9ed1";
            text-decoration: none;
            color: #999
        }

        .lookForGoodsBar .link {
            display: inline-block;
            margin: 0 20px 0 0
        }

        .miao2o-store-list:after {
            border-top: 1px solid rgba(0,0,0,.1);
            background: #eee;
            height: 10px;
            content: '\20';
            display: block
        }

        .miao2o-store-list .hd {
            display: block;
            padding: 6px 12px 0;
            line-height: 34px;
            background-color: #fff;
            overflow: hidden
        }

        .miao2o-store-list .hd h3 {
            color: #666;
            font-weight: 400;
            font-size: 14px;
            float: left
        }

        .miao2o-store-list .hd .more {
            color: #B5B5B5;
            font-size: 12px;
            text-decoration: none;
            float: right
        }

        .miao2o-store-list .hd .more:after {
            content: '\e604';
            font-family: tm-detail-font;
            float: right;
            background-size: 7px;
            width: 7px;
            font-size: 14px
        }

        .miao2o-store-list .bd {
            background-color: #fff
        }

        .miao2o-store-list .bd.multi {
            overflow-x: auto;
            overflow-y: hidden;
            font-size: 0
        }

        .miao2o-store-list .bd.multi:after {
            content: ' ';
            clear: both;
            height: 0;
            overflow: hidden
        }

        .miao2o-store-list .bd.multi ul {
            white-space: nowrap
        }

        .miao2o-store-list .bd.multi ul li {
            display: inline-block;
            width: 90%;
            margin-right: 3px
        }

        .miao2o-store-list .bd.multi .item {
            width: 100%;
            font-size: 12px
        }

        .miao2o-store-list .bd .item {
            position: relative;
            padding-top: 40%
        }

        .miao2o-store-list .bd .item a,.miao2o-store-list .bd .item img {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%
        }

        .miao2o-store-list .bd .item img {
            z-index: 2
        }

        .miao2o-store-list .bd .item .mark {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            overflow: hidden;
            z-index: 4
        }

        .miao2o-store-list .bd .item .mark img {
            display: block;
            width: 100%;
            height: auto
        }

        .miao2o-store-list .bd .item .btn360,.miao2o-store-list .bd .item .text-name,.miao2o-store-list .bd .item .text-store,.miao2o-store-list .bd .item .text-video {
            position: absolute;
            z-index: 5
        }

        .miao2o-store-list .bd .item .text-store {
            width: 100%;
            text-align: center;
            font-size: 18px;
            color: #FFF;
            top: 18%;
            left: 0
        }

        .miao2o-store-list .bd .item .text-name {
            width: 100%;
            text-align: center;
            font-size: 12px;
            color: #FFF;
            top: 36%;
            left: 0
        }

        .miao2o-store-list .bd .item .btn360 {
            width: 115px;
            height: 30px;
            line-height: 30px;
            top: 60%;
            left: 0;
            right: 0;
            margin: auto;
            text-align: center;
            font-size: 14px;
            color: #FFF;
            background-image: linear-gradient(-180deg,#FC4328 0,#E01313 99%);
            border-radius: 100px
        }

        .miao2o-store-list .bd .item .btn360 i {
            vertical-align: middle;
            background: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAkCAQAAABLCVATAAACtUlEQVR4AaXSA9QjZxyH0V9t27Zt27Zt27Zt27bbtW3b9m6+W83pbLXKPcr/xZNkklTH2moZ4VNLpTraqeVUbXyaalgCOyRONSLVMJO+3rWDWmqlOnbUF+2snZJ93OddN1i3XDOf87zsZeeZr1y1vpu87wH7JWayRErm8R6ACS4vVjfXDUA3mxerV5oA4APzZHIeBRP1BuyXWEAPMMQQ0MMCif0BPYvcYylZRg0aWiKxpRFombgC3GxmM7sFXJFoiWE2SyyhIWosW4YOBLsW0z2oFF+2t5mK36c33jOvCu4sTu4GDixD22OQOYvpFtRYQAPUS8GTeMrq4KxibXVwXko2VPzJrawf2iV/hixlP7tY0Gy2NdsUQyW7+9YYcHIRGmcSqLjfXMm0hpoDGidFqMRH0x46XmfAPkWIxg50oMbgkKmGLGaO4tXWRqBZERptsWJ/NJ425ZANTdDZrMV0LyaZRwM0SkFj1DblkCPANsV0M1jQJ+iagq74xELgxmJtO3BEGVoFfGGuxIr6omPiBnBakjgN3JDohD5WTMztK7BKSt4G/dQyGpySWMpg0FwLMNiSiVPBaLX0B29nchbVQOmJYnUfwwAMs3f5HwfQwKJl5CnHJ2Z1rvc19qI98xfLutdPfnKvZctVe3pRY+87xyyJEz2WxKnG2SBVsKHxToj67kqV3KN2DHdUquRoQ+IXz6RKnlI79lHjoFTB/ir2TeIWFTeY7d8fWCOLZ4rM7mqT3FCMjjFCN6ebJ5OxiOZaWSz/w1xO095QJ/z90l1GG+U1R1iq/JtqruW/UxZ3mFcNM9J9lsw/mc/xvjAGnX3tOTc61TEGKlJmcrQ7vaczxvnCSRac8rfe2sUe8p4G+qiAu5PE/Nr6xXMutbU5Mn0srZtWFk91LKal6jMWLTLVcqxG0575FW/GipJtTHRJAAAAAElFTkSuQmCC) no-repeat;background-size: cover;
            width: 18px;
            height: 18px;
            margin-right: 3px;
            display: inline-block
        }

        .mui-shoprecommend {
            position: relative
        }

        .mui-shoprecommend-item {
            overflow: hidden;
            width: 33%;
            margin: 0 3px 0 0
        }

        .mui-shoprecommend-item:last-child {
            margin-right: 0
        }

        .mui-shoprecommend-item .wrap {
            width: 100%;
            overflow: hidden
        }

        .mui-shoprecommend-item .mui-shoprecommend-img {
            position: relative;
            display: block
        }

        .mui-shoprecommend-item .mui-shoprecommend-img:before {
            content: '\20';
            padding-top: 100%;
            display: block
        }

        .mui-shoprecommend-item .mui-shoprecommend-img img {
            max-width: 100%;
            max-height: 100%;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate3d(-50%,-50%,0);
            -webkit-transform: translate3d(-50%,-50%,0)
        }

        .mui-shoprecommend-item .mui-shoprecommend-title {
            font-size: 14px;
            padding: 6px 6px 0;
            height: 42px;
            line-height: 18px;
            overflow: hidden
        }

        .mui-shoprecommend-item .mui-shoprecommend-price {
            padding: 0 0 2px 4px;
            color: #dd2727;
            line-height: 30px;
            font-size: 16px
        }

        .mui-shoprecommend-item .mui-shoprecommend-price .unit {
            position: relative;
            top: 1px
        }

        .muicell {
            display: -webkit-box;
            display: -webkit-flex;
            display: -ms-flexbox;
            display: flex;
            position: relative;
            max-width: 100%;
            height: auto;
            overflow: hidden;
            overflow-y: auto;
            overflow-scrolling: touch;
            -webkit-box-flex: 1;
            -webkit-flex: 1 1 0;
            -ms-flex: 1 1 0;
            flex: 1 1 0;
            -webkit-box-orient: horizontal;
            -webkit-box-direction: normal;
            -webkit-flex-direction: row;
            -ms-flex-direction: row;
            flex-direction: row;
            -webkit-flex-wrap: wrap;
            -ms-flex-wrap: wrap;
            flex-wrap: wrap;
            -webkit-box-ordinal-group: 1;
            -webkit-order: 0;
            -ms-flex-order: 0;
            order: 0;
            -webkit-backface-visibility: hidden;
            backface-visibility: hidden
        }

        .muicell.vertical {
            -webkit-box-orient: vertical;
            -webkit-box-direction: normal;
            -webkit-flex-direction: column;
            -ms-flex-direction: column;
            flex-direction: column
        }

        .muicell.vertical.reverse {
            -webkit-box-orient: vertical;
            -webkit-box-direction: reverse;
            -webkit-flex-direction: column-reverse;
            -ms-flex-direction: column-reverse;
            flex-direction: column-reverse
        }

        .muicell.horizental {
            -webkit-box-orient: horizontal;
            -webkit-box-direction: normal;
            -webkit-flex-direction: row;
            -ms-flex-direction: row;
            flex-direction: row
        }

        .muicell.horizental.reverse {
            -webkit-box-orient: horizontal;
            -webkit-box-direction: reverse;
            -webkit-flex-direction: row-reverse;
            -ms-flex-direction: row-reverse;
            flex-direction: row-reverse
        }

        .muicell.justify-content-start {
            -webkit-box-pack: start;
            -webkit-justify-content: flex-start;
            -ms-flex-pack: start;
            justify-content: flex-start
        }

        .muicell.justify-content-end {
            -webkit-box-pack: end;
            -webkit-justify-content: flex-end;
            -ms-flex-pack: end;
            justify-content: flex-end
        }

        .muicell.justify-content-center {
            -webkit-box-pack: center;
            -webkit-justify-content: center;
            -ms-flex-pack: center;
            justify-content: center
        }

        .muicell.justify-content-between {
            -webkit-box-pack: justify;
            -webkit-justify-content: space-between;
            -ms-flex-pack: justify;
            justify-content: space-between
        }

        .muicell.justify-content-around {
            -webkit-justify-content: space-around;
            -ms-flex-pack: distribute;
            justify-content: space-around
        }

        .muicell.align-items-start {
            -webkit-box-align: start;
            -webkit-align-items: flex-start;
            -ms-flex-align: start;
            align-items: flex-start
        }

        .muicell.align-items-end {
            -webkit-box-align: end;
            -webkit-align-items: flex-end;
            -ms-flex-align: end;
            align-items: flex-end
        }

        .muicell.align-items-center {
            -webkit-box-align: center;
            -webkit-align-items: center;
            -ms-flex-align: center;
            align-items: center
        }

        .muicell.align-items-stretch {
            -webkit-box-align: stretch;
            -webkit-align-items: stretch;
            -ms-flex-align: stretch;
            align-items: stretch
        }

        .muicell.align-self-start {
            -webkit-align-self: flex-start;
            -ms-flex-item-align: start;
            align-self: flex-start
        }

        .muicell.align-self-end {
            -webkit-align-self: flex-end;
            -ms-flex-item-align: end;
            align-self: flex-end
        }

        .muicell.align-self-center {
            -webkit-align-self: center;
            -ms-flex-item-align: center;
            align-self: center
        }

        .muicell.align-self-stretch {
            -webkit-box-align: stretch;
            -webkit-align-items: stretch;
            -ms-flex-align: stretch;
            align-items: stretch
        }

        .muicell.center {
            -webkit-box-pack: center;
            -webkit-justify-content: center;
            -ms-flex-pack: center;
            justify-content: center;
            -webkit-box-align: center;
            -webkit-align-items: center;
            -ms-flex-align: center;
            align-items: center
        }

        .muicell.shrink {
            -webkit-box-flex: 0;
            -webkit-flex: 0 0 auto;
            -ms-flex: 0 0 auto;
            flex: 0 0 auto
        }

        .muicell.inline {
            width: 0
        }

        .muicell.content {
            display: block
        }

        .muicell,.muicell *,.muicell :after,.muicell :before {
            box-sizing: border-box
        }

        .mui-shopcoupon-list {
            color: #fff;
            position: relative;
            overflow: hidden;
            height: 72px;
            padding-bottom: 9px
        }

        .mui-shopcoupon-list .wrap {
            position: absolute;
            white-space: nowrap
        }

        .mui-shopcoupon-list .wrap.center {
            left: 50%;
            transform: translate3d(-50%,0,0);
            -webkit-transform: translate3d(-50%,0,0)
        }

        .mui-shopcoupon-list .mui-shopcoupon-item {
            display: inline-block;
            width: 178px;
            height: 70px;
            position: relative;
            margin: 0 2px 0 0
        }

        .mui-shopcoupon-list .mui-shopcoupon-item:nth-child(3n+1) {
            background-color: #FF7675
        }

        .mui-shopcoupon-list .mui-shopcoupon-item:nth-child(3n+2) {
            background-color: #FF7675
        }

        .mui-shopcoupon-list .mui-shopcoupon-item:nth-child(3n+3) {
            background-color: #FF7675
        }

        .mui-shopcoupon-list .mui-shopcoupon-top {
            overflow: hidden;
            margin: 0 0 0 10px
        }

        .mui-shopcoupon-list .mui-shopcoupon-tl {
            float: left
        }

        .mui-shopcoupon-list .mui-shopcoupon-tl .unit {
            font-size: 18px
        }

        .mui-shopcoupon-list .mui-shopcoupon-tl .number {
            font-size: 30px
        }

        .mui-shopcoupon-list .mui-shopcoupon-tr {
            float: right;
            margin: 8px 30px 0 0;
            width: 68px
        }

        .mui-shopcoupon-list .mui-shopcoupon-tr p {
            padding-top: 12px;
            text-align: right
        }

        .mui-shopcoupon-list .mui-shopcoupon-handler {
            position: absolute;
            font-size: 14px;
            width: 24px;
            background: rgba(0,0,0,.1);
            right: 0;
            top: 0;
            height: 100%;
            overflow: hidden;
            line-height: 100%;
            text-align: center
        }

        .mui-shopcoupon-list .mui-shopcoupon-handler .group {
            margin: 11px 0 0
        }

        .mui-shopcoupon-list .mui-shopcoupon-handler .qu {
            margin: 18px 0 0
        }

        .mui-shopcoupon-list .mui-shopcoupon-bottom {
            transform: scale(0.8);
            -webkit-transform: scale(0.8)
        }

        .mui-shopactivity-unload {
            min-height: 100px
        }

        .mui-shopactivity {
            overflow: hidden;
            background-color: #FFF;
            padding-bottom: 9px
        }

        .mui-shopactivity .mui-shopactivity-item a {
            position: relative;
            display: block;
            overflow: hidden
        }

        .mui-shopactivity .mui-shopactivity-item a:before {
            content: '\20';
            padding-top: 47.5%;
            display: block
        }

        .mui-shopactivity .mui-shopactivity-item img {
            position: absolute;
            top: 50%;
            left: 50%;
            -webkit-transform: translate3d(-50%,-50%,0);
            width: 100%;
            max-height: 100%
        }

        .mui-custommodule-item.unloaded {
            min-height: 320px
        }

        .mui-custommodule-item img {
            width: 100%
        }

        #s-actionBar-container .extraTip,.sku-other h3 {
            text-overflow: ellipsis;
            white-space: nowrap;
            overflow: hidden
        }

        #s-actionBar-container {
            position: fixed;
            bottom: 0;
            width: 100%;
            z-index: 1
        }

        #s-actionBar-container .extraTip {
            padding: 10px 0 10px 10px;
            background-color: #000;
            opacity: .8;
            color: #fff;
            position: absolute;
            top: -38px;
            left: 0;
            height: 18px;
            line-height: 18px;
            right: 0
        }

        #s-actionBar-container .extraTip a,#s-actionBar-container .extraTip a:hover {
            color: #1193ce
        }

        #s-actionBar-container .extraTip .tipBtn {
            border: 1px solid #fff;
            padding: 0 4px;
            float: right;
            color: #fff;
            margin: 0 10px;
            border-radius: 2px
        }

        #s-actionBar-container .action-bar .addfav,#s-actionBar-container .action-bar .support,#s-actionBar-container .action-bar .toshop {
            max-width: 50px;
            min-width: 26px;
            border-right: 1px solid rgba(0,0,0,.05)
        }

        #s-actionBar-container .extraTip .tipBtn:hover {
            color: #fff
        }

        #s-actionBar-container .activity-box {
            display: none
        }

        #s-actionBar-container .countdown {
            width: 100%;
            height: 30px;
            position: absolute;
            top: -30px;
            background: #EE9393;
            color: #fff;
            text-align: center;
            line-height: 30px
        }

        #s-actionBar-container .cart-link {
            position: absolute;
            display: block;
            width: 36px;
            height: 36px;
            background: #000;
            top: -58px;
            left: 10px;
            opacity: .5;
            text-align: center;
            -moz-border-radius: 18px;
            -webkit-border-radius: 18px;
            border-radius: 18px;
            display: -webkit-box;
            -webkit-box-align: center;
            -webkit-box-pack: center;
            display: -ms-flexbox;
            -ms-flex-pack: center;
            -ms-flex-align: center;
            -webkit-box-orient: vertical;
            -ms-flex-orient: vertical;
            z-index: 10
        }

        #s-actionBar-container .cart-link::before {
            content: '\e60e';
            font-family: tm-detail-font;
            font-size: 24px;
            color: #fff
        }

        #s-actionBar-container .preSale {
            background: #ff9500!important;
            text-align: center
        }

        #s-actionBar-container .action-bar {
            color: #999;
            border-top: 1px solid #E5E5E5
        }

        #s-actionBar-container .action-bar .cell {
            height: 49px;
            text-align: center
        }

        #s-actionBar-container .action-bar .cell a {
            position: absolute;
            width: 100%;
            height: 100%;
            display: block;
            top: 0
        }

        #s-actionBar-container .action-bar .cell.pageType2 {
            background: #DD2727!important;
            font-size: 12px;
            padding: 0 0 0 10px!important
        }

        #s-actionBar-container .action-bar .cell.pageType2.pintuan {
            padding: 0!important;
            font-size: 15px
        }

        #s-actionBar-container .action-bar .cell.pageType3 {
            background: #DD2727!important;
            font-size: 12px;
            padding: 0 0 0 10px!important;
            display: block!important
        }

        #s-actionBar-container .action-bar .support::before {
            content: '\e60a';
            color: #4A90E2
        }

        #s-actionBar-container .action-bar .addfav::before {
            content: '\e609';
            color: #5F646E
        }

        #s-actionBar-container .action-bar .faved {
            max-width: 50px
        }

        #s-actionBar-container .action-bar .faved::before {
            content: '\e608';
            color: #FFE96E
        }

        #s-actionBar-container .action-bar .toshop::before {
            content: '\e60d';
            color: #5F646E
        }

        #s-actionBar-container .action-bar .toshop.tochaoshi {
            max-width: 96px;
            min-width: 76px
        }

        #s-actionBar-container .action-bar>div::before {
            font-family: tm-detail-font;
            width: 25px;
            height: 25px;
            font-size: 20px;
            display: block;
            margin: 1px auto 0
        }

        #s-actionBar-container .action-bar>button {
            border: none;
            color: #fff;
            font-size: 15px
        }

        #s-actionBar-container .action-bar .buy {
            background: #DD2727
        }

        #s-actionBar-container .action-bar .buy.cell.pageType2 {
            background: #DD2727!important;
            font-size: 15px;
            padding: 0
        }

        #s-actionBar-container .action-bar .buy:disabled {
            color: #ddd;
            color: rgba(255,255,255,.4)
        }

        #s-actionBar-container .action-bar .buy .disabled-box {
            display: block;
            width: 100%;
            height: 100%;
            padding-top: 5px;
            background-color: #DD2727;
            color: #fff;
            white-space: nowrap;
            overflow: hidden
        }

        #s-actionBar-container .action-bar .buy .disabled-box.linecenter {
            padding-top: 0;
            display: box;
            display: -webkit-box;
            display: -moz-box;
            -webkit-box-pack: center;
            -moz-box-pack: center;
            -webkit-box-align: center;
            -moz-box-align: center
        }

        #s-actionBar-container .action-bar .buy span {
            font-size: 12px
        }

        #s-actionBar-container .action-bar .buy p {
            font-size: 16px;
            line-height: 24px
        }

        #s-actionBar-container .action-bar .buy.pageType2 {
            background: #DD2727!important
        }

        #s-actionBar-container .action-bar .cart {
            background: #FF9500
        }

        #s-actionBar-container .action-bar .cart:disabled {
            color: rgba(255,255,255,.4)
        }

        #s-actionBar-container .action-bar .restrict {
            font-size: 8px
        }

        #s-actionBar-container .action-bar .buytimetip {
            min-width: 130px;
            border-right: 0
        }

        #s-actionBar-container .action-bar.buytimewillstart .buytimetip {
            line-height: 49px;
            text-align: left;
            color: #FFF;
            font-size: 12px;
            background-color: #00C477;
            font-size: 16px;
            text-align: center;
            padding-left: 0
        }

        #s-actionBar-container .action-bar.buytimeending .buytimetip,#s-actionBar-container .action-bar.buytimestarting .buytimetip {
            line-height: 49px;
            text-align: left;
            color: #FFF;
            font-size: 12px;
            background-color: #7555f4
        }

        #s-actionBar-container .action-bar.buytimestarting .buytimetip p,#s-actionBar-container .action-bar.buytimewillstart .buytimetip p {
            padding-left: 9px
        }

        #s-actionBar-container .action-bar.buytimestarting .buy {
            min-width: 80px;
            background-color: #7555f4
        }

        #s-actionBar-container .action-bar.buytimeending .buytimetip {
            overflow: hidden
        }

        #s-actionBar-container .action-bar.buytimeending .buytimetip p {
            padding-left: 9px;
            float: left
        }

        #s-actionBar-container .action-bar.buytimeending .buytimetip p:last-child {
            float: right;
            height: 49px;
            text-align: right;
            padding-right: 12px;
            color: rgba(255,255,255,.4);
            font-size: 15px
        }

        #s-actionBar-container .action-bar.buytimeending .buy {
            background-color: #7555f4
        }

        #s-actionBar-container .action-bar .hasshiptime p {
            line-height: 16px
        }

        #s-actionBar-container .action-bar .hasshiptime p:first-child {
            padding-top: 10px
        }

        #s-actionBar-container .action-bar .hasshiptime p:nth-child {
            padding-top: 3px
        }

        #s-actionBar-container.actionBar-activitiy .buy,#s-actionBar-container.actionBar-activitiy .cart {
            display: none
        }

        #s-actionBar-container.actionBar-activitiy .activity-box {
            display: block;
            background: #00c477
        }

        .alihealthApp #s-actionBar-container .action-bar .addfav,.alihealthApp #s-actionBar-container .action-bar .support,.alihealthApp #s-actionBar-container .action-bar .toshop,.hideHeaderInApp #s-actionBar-container .cart-link {
            display: none
        }

        #s-actionBar-container.actionBar-activitiy.actionBar-activitiy-nobuy .activity-box {
            background-color: #999;
            border-color: #999;
            color: #fff;
            line-height: 24px;
            font-size: 15px
        }

        #s-actionBar-container.actionBar-activitiy.actionBar-activitiy-nobuy .activity-box>div {
            height: 24px
        }

        #s-actionbar .cell {
            border-right: 1px solid rgba(0,0,0,.05)
        }

        #s-actionBar-container #s-actionbar .cell:last-child {
            border-right: none
        }

        .alihealthApp #s-actionBar-container .action-bar .cart {
            background: #23c2aa
        }

        .alihealthApp #s-actionBar-container .action-bar .buy {
            background: #f4a929
        }

        .alihealthApp #s-actionBar-container .action-bar .buy:disabled {
            background-color: #999;
            border-color: #999;
            color: #fff
        }

        .sold-out-remind {
            color: #fff;
            background: #00C478;
            font-size: 14px;
            line-height: 49px
        }

        .sold-out-remind span {
            font-size: 12px;
            display: block;
            color: #6cdcb1
        }

        .sold-out-remind-url {
            line-height: 26px
        }

        .sold-out-remind-url span {
            line-height: 12px
        }

        .sold-out-success {
            background: #00C478;
            overflow: hidden
        }

        .sold-out-success::after {
            content: '\20';
            height: 0;
            display: block;
            clear: both
        }

        .sold-out-success span {
            float: left;
            text-align: left;
            color: #6cdcb1;
            width: 70%;
            padding: 5px 5% 0;
            line-height: 20px
        }

        .sold-out-success strong {
            float: left;
            color: #fff;
            height: 49px;
            line-height: 49px;
            width: 30%;
            font-size: 14px;
            text-align: left
        }

        .sold-out-success-nolink span {
            width: 100%;
            text-align: center
        }

        .dialog-download-tmall {
            color: #f11c42
        }

        .sku-other {
            background-color: #fff
        }

        .sku-other h3 {
            font-weight: 400;
            line-height: 2em;
            padding: .7em 10px
        }

        .sku-other .sku-other-list {
            overflow: hidden;
            padding: 0 10px 12px
        }

        .sku-other .sku-other-list a {
            float: left;
            width: 47.5%;
            margin-left: 3%;
            margin-top: 6px;
            height: 38px;
            line-height: 38px;
            text-align: center;
            border: 1px solid #e5e5e5;
            border-radius: 4px
        }

        .sku-other .sku-other-list a span {
            display: inline-block;
            vertical-align: middle;
            padding: 0 6px;
            line-height: 16px;
            max-height: 32px;
            overflow: hidden;
            word-break: break-all
        }

        .sku-other .sku-other-list a:nth-of-type(2n - 1) {
            margin-left: 0
        }

        .sku-other .sku-other-list a:nth-of-type(1),.sku-other .sku-other-list a:nth-of-type(2) {
            margin-top: 0
        }

        .pintuanEntry {
            min-height: 50px;
            color: #666;
            padding: 5px 10px 0
        }

        .pintuanEntry .v {
            word-break: break-all
        }

        .pintuanEntry.dot::after {
            content: '\e604';
            font-family: tm-detail-font;
            width: 17px;
            padding: 1px;
            border-radius: 2px;
            -webkit-border-radius: 2px;
            -moz-border-radius: 2px;
            font-size: 18px;
            color: #DD2727
        }

        .pintuanEntry .pingtuan-wrap {
            text-align: left;
            line-height: 30px;
            border-top: 1px dashed #D6D6D6;
            padding-top: 5px
        }

        .pintuanEntry .pingtuan-wrap:after {
            content: ' ';
            display: block;
            clear: both;
            height: 0;
            font-size: 0;
            overflow: hidden
        }

        .pintuanEntry .pingtuan-wrap .pintuan-name {
            color: #E61414;
            font-size: 14px;
            font-weight: 700;
            width: 70px;
            height: 30px;
            float: left
        }

        .pintuanEntry .pingtuan-wrap .pintuan-name .pintuan-icon {
            display: inline-block;
            vertical-align: text-bottom;
            width: 16px;
            height: 22px;
            margin-right: 5px;
            background-image: url(data:image/png;
            base64,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);background-size: 16px;
            background-repeat: no-repeat
        }

        .pintuanEntry .pingtuan-wrap .pingtuan-process {
            float: left;
            width: 210px;
            height: 30px
        }

        .pintuanEntry .pingtuan-wrap .pingtuan-process .pingtuan-bg {
            position: relative;
            width: 200px;
            height: 17px;
            margin-top: 6px;
            line-height: 17px;
            color: #fff;
            text-align: center;
            background: #FFC3C3;
            border-radius: 36px
        }

        .pintuanEntry .pingtuan-wrap .pingtuan-process .pingtuan-bg .pingtuan-ing {
            position: absolute;
            top: 0;
            left: 0;
            border-radius: 36px;
            height: 17px;
            text-indent: -12px;
            background: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAYAAAAmlE46AAAABGdBTUEAALGPC/xhBQAAALdJREFUKBWVklEPgjAQg5kEovjE//+JPoMIurMluozZW2KThey6bz04go2jNRXdYmw24Z9ELZVWMwm1OFEFJ4BK1xB8MAK6CyqgNmC5iZOAWLoQ8hINaXOlTV4gExcYL7qFeuw7pFESrH2UnVLghhbXr5s9OYJztv9J9NIGtBg+bZI/gN4IeJAjyHUAZzhq5BxBm6XxggRyBF6b/FNKJfABR42gQ72vgf+kMX1PfKJNJpaiyfdTegOFKzkWN9NkhAAAAABJRU5ErkJggg==) repeat-x
        }

        .pintuanEntry .pingtuan-wrap .pingtuan-process .pingtuan-bg .pingtuan-ing .pingtuan-icon {
            position: absolute;
            right: 0;
            top: 1px;
            display: block;
            background-image: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABGdBTUEAALGPC/xhBQAABGFJREFUWAnFV01oVEkQ/l4IWTQo5kdNIuIPIpiYwYMgQcXsQVxRF7woYYlIULwP6EFhkcAKXvYgkj0o3lZBD8KKsCdHCTIJHgTRGFQ8GMwYI4kmo/gT/baqKy/tezOTeQZWG/p1d3V11Vfd1dX1AiQsbGmpQi73M8hfZUkzgqBJWq1ahoU+LO2A0P9BY2MmePDgo5sp8wnKzIOLFzdgaup3EfybKFlYjt/NB8GE8P6NysruYHT0xWxrSgLgmjU/YWzshCxOi7BqJySVAnbtAtraIFYCDQ0m+4XoyOWAbBa4fh24d8/oQfBWOn+itvaP4MmTD0ZM8FWrWVubZU0NXT14kHz8mImL8uqacL3K0p1MUrhkSUqUD7nFGzaQd+4k1lvAqGtVhgJRmSJ7VgzTlpvy3bvJsbECmWUJnz5FWVSGygpBlNoJPfOZbdcFcUFRsYWjL1/IM2fIzZvJAwfkTgx7HpXlQWSdf8W3QpR3z2z7XCzv6/Nnrtam0x6A9lSmP47uUH+FdqYdJO2I584BNTXhfOn2rTi41rBMTIQ9a0dHo2OVqbKtpCNOybq6v5z16rnlijpWZye5fr3VvXvJCxfIkRGyq4usrzd6Nltckr8dPQ6LRjjZ/jcOQLmrduMGuXy5bXVzM6k1vGp69pkMOThIvnxZXLlSVYc55BsXXUX5DkfYtq30Ip2ZmCBTKVt8+DA5Pk6+fk1u3epBrFhB3r8/uxydVV0GYkeFRDmN7RbhXKfEJ5CguWmTxEVxlbNngUWLgNu3gYcP/QL1g+4Z//L0eE+jqRbVLUgyDk1vb3nkX3NMTpJtbd76VavMBxobyWfPvuYs7KsuO7pMpXvVSIvthivZ9/x5YHDQ83Z1ATt3AlevAh0dQEsLsG4dsH279T2n16UvqvjApEOTzxciLUVRC1evDq0gW1vtnj9/bnddfWHZMrKpiezoID9/jkpSXeYDk5UzwHQXkpZTp4Dxcc997JjFDr3rt24BHyUVUJ9RmfPmARUu3Hh+r4s6o4kEMDLimrKfa9eAy5ejbH19wPv3RlsoKUN9PVBXZ211dZRXR15XTm+BAdD3PF7Uq58+9fXuXeCEpAhqwf79FtnmzwcuXgSOHAHy+biE4uNQl+jWIxiQ2u6SiS1b/IKpKaCzE+jt9bSwt3EjcPo0oNYuWGDKdWcUQI8EuKVLQ87irSYuVgYCDURi0b/QbOfmzXDCrFRP7+/3ND3Xqirg6FFg5UpPV5D79tnZq4zWVj9XrNfebllTEPyCbwrFUV/2oytXzKuPH/e0Ur14KFaAciV63LVI8hjFBQ8NkWvX2gP06lV8tnAcf4wcAMsB8w7Et6Rger8PHTLrL10qVBanqGy7//nIc+xAzCUhefTIBCbJoEokJKrblTmlZO/ekSdPkv39cVuj4yQpmaKIJKV79swtKY2qNhk+Hxwq2Hqz339/aFoewpjeie/yYyKRpXj5Xr9mJQGEsNyZ/Yif0xBA2P5fv+f/AXyra/Iz5ke6AAAAAElFTkSuQmCC);background-size: 16px;
            background-repeat: no-repeat;
            height: 16px;
            width: 16px
        }

        .pintuanEntry .pingtuan-wrap .pingtuan-process .pingtuan-bg .pingtuan-ing .pingtuan_fast {
            background-image: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAcCAYAAAAnbDzKAAAABGdBTUEAALGPC/xhBQAABNdJREFUWAnNWF1oVEcU/lbF/8bEYJpsNsaoiQ+KBokVaSNU8cFEC5W8+LM0UFR8qRIb4kMsxbdaC2kb7UuiJRgpfSn4l7ZBCQoqiBWUgApK6poEbFISMDEJSU6/k9m7d3fdvXub3sQe+JiZMzNnzpk758yZ68M0k4gs4ZI7iXJiOeEn0omXRBfRTlwkfvf5fIMs/x9ExVcSPxNjhBsa4KCvCDXu7REVmBlWZITlZKiXk4LJLPAl67D4nDyf9YX8nPqJI0T+bDYChJOMd9j/HVFKuKfWVqC5GXj1Cti9G9i2DUhLO0UBNdRj3LUgKvkxMUgo/WhNZH0T8bcyPacHD0RKS0UyMgwKC0VaW0VGJj5gg6WDVc6wKknKWvLnhfs+obKF4frnLDPCdW+Lp0/NzltSe3qAzk5geFg5n1KHw1aXlrOiGwnqf5K3PswfYPlXuN4RLidXjI0B9+4BN24w7jDwpKUB69YBGzcCJSVAfj4QCgE6btUqg3nWPuIbGnGdR+mhLu50fsGB73LMl4SGvnpOamOp/IUsviDWEvEyZpL3ATGHeJP6+oBTPM4XLgBaj6alS4E9e4whaqD6wI4dwJo1wGx1uQi1UJeySMvLCo2rIRLT8LBIba1IQYE538GgyJkzIkePihQV2ee+okLk8ePEMmzu+17qHZFF+fftNeJq7e0imzeLZGWJHDsm0tlpBly5IlJSYhugDrx/v0hHR5yAmOb3kUW9qlB8fswS8Y0B3k2XLolcuybS3296u7tFdu3i/bzENiAzUyQQEFHD9Kslpueq9yz26Zn9iMhWxn+k9xznz+eVomc6ms6fBx7SH0dHDVf9oJxZxtWrwK1b9DK6WSAQPcOq51H3ZRqFmgh6zlsgddSLTHs0VFoUDAJbtgB37gANDUBLC6BGrV5tDCsuZmCPRKRcNUB3f/rp9Wvg3Dng2TN77Q0bgO3bzY6vWAF0dwO9vUzzmATcvQv095u+vDxrjl8N+JWosDjTVurO37wJDIYTzgULgAMHgIICs8N1dcDQkMZsWyU9gnPn2m3W1AA9Pr8QXvjAh5QTd8jJiadHj4CzZ4EXL2J79PJSg/SIqLIKZ+py7v6XvXSqfQkDxvi4iEJJo8+hQyK5uSJ+vwmnZWUi2dkmvB4/LqKRyR1NOHFKNSlLt+IzoijFYJ6DKGprAxobTS5jsXWHnzMCjowAR44ABw+aPOfECRN56uvNDV1VZVIKX/xFbwlCiLdxR6TlVKEBP7jbkLhRTU0ixcUiixfb0BifkyNSVSUSCtkTenpEqqsZGJeZ++DkSRHlJaeJi0x9wA0xGE+CNK/ZutXscLQz6q7m5MQ6ZGYm4yEDojq2JncakRYtclr0J6fOmD5uwl5iNPlmeNDT1ydSWWl8o66Orw3H5wZvOUOuvgDPWjNVbOOU5eF5qYqdHFCdalBMv0YlvXk1pda7ICPpc4M5Nmpi5nrdoLE+Qh/w7uj2bb7xNpks9fJlkaEhp3kxD5oZXiuv8vjF9PapJP4gUpM+bDSdUJ9Zz/fTnMRPCQpqpOxvUwv0aAS3MZ34zWk7J/o0Gp0+LfLkiX1fvDnpa7KmZMMdzeWiU/pbxXFxLztpyJT82Ep6zXmpfLQsGpLFtuZL5YRGNT+RTrwkuoh2gpmeu1+L/wCCnJTK968sTwAAAABJRU5ErkJggg==);background-size: auto 16px;
            height: 16px;
            width: 28px
        }

        .pintuanEntry .pingtuan-wrap .pingtuan-process .pingtuan-bg .pingtuan-ing .pingtuan_iconfull {
            background-image: url(data:image/png;
            base64,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);background-size: 25px 21px;
            width: 25px;
            height: 21px;
            top: -2px;
            right: -5px
        }

        .pintuanEntry .pingtuan-wrap .pingtuan-process .pingtuan-bg .pingtuan_full {
            background: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAYAAAAmlE46AAAABGdBTUEAALGPC/xhBQAAAMBJREFUKBV9klESgjAMRGkRBS/C/Q/kEfwFldq6q1BiSbozDCXlZQsbdxvH1FR0D6EJyr5Xarn0SkmFWrxRBecYcxO5GLy3wQi3B65SDoXBORucFIhNekIWmOhWOSYbqN/4BPjmbqEOzx3cKBWcDLcrfsqmfbVWFrgt2664M4LL6sbyAbQi6OHmLNCKgA6MQOrPccYxj8n9ImgtkBFYx+SklMoVzqUWwQnEuXBjkwxakyIjkK5fMMCNjqW4yRHT9AHgnUcgCLZ2JQAAAABJRU5ErkJggg==) repeat-x
        }

        .pintuanEntry .pingtuan-wrap .pingtuan-num {
            width: 70px;
            color: #EB2200;
            float: left;
            height: 30px
        }

        @media screen and (max-width:320px) {
            #s-actionBar-container .action-bar .buy p {
                font-size: 14px
            }
        }

        .store-link {
            margin-top: 10px;
            display: block;
            background-color: #fff;
            border-bottom: 1px solid #e5e5e5;
            font-size: 12px;
            padding: 10px;
            line-height: 22px;
            color: #051b28
        }

        .store-link p {
            height: 20px;
            line-height: 20px;
            color: #999
        }

        .store-link p i {
            vertical-align: middle;
            background-repeat: no-repeat;
            background-size: cover;
            width: 18px;
            height: 18px;
            margin-right: 3px;
            display: inline-block
        }

        .store-link.onlymiao2 p {
            color: #051b28
        }

        .store-link.more {
            margin-bottom: 10px
        }

        .store-link.more:after {
            content: '\e604';
            font-family: tm-detail-font;
            float: right;
            background-size: 7px;
            width: 7px;
            margin-right: 10px;
            font-size: 19px;
            color: #999
        }
        .web{
            padding-top: 6px;
            max-width: 50px;
            min-width: 26px;
            height: 49px;
            width: 50px;
            text-align: center;
            border-right: 1px solid rgba(0,0,0,.05);
            cursor:pointer;


        }
        .web p{
            font-size: 12px;
        }
        .ipo{
            margin-top: 10px;
        }
        @charset "UTF-8";

        body,button,dd,dl,dt,fieldset,form,h1,h2,h3,h4,h5,h6,input,legend,li,ol,p,select,table,td,textarea,th,ul {
            margin: 0;
            padding: 0
        }

        li,ul {
            list-style: none
        }

        article,aside,footer,header,hgroup,main,nav,section {
            display: block
        }

        a {

            text-decoration: none
        }

        body {
            min-width: 320px;
            font-size: 1em;
            font-family: PingFangSC-Regular,Helvetica,"Droid Sans",Arial,sans-serif;
            color: #000;
            -webkit-text-size-adjust: none;
            line-height: normal
        }

        body,button,input,select,table,textarea {
            font-size: 16px
        }

        .hot-search-bar span i:before,.jd-auto-complete-list li:before,.jd-footer-platforms li a.badge:after,.jd-footer-platforms li a:before,.jd-header-icon-back span,.jd-header-icon-cancel span,.jd-header-icon-category span,.jd-header-icon-close:after,.jd-header-icon-logo span,.jd-header-icon-search span,.jd-header-icon-shortcut span,.jd-header-shortcut span,.jd-search-tab li.sort-by-integrative a:after,.jd-search-tab li.sort-by-integrative.active a:after,.jd-search-tab li.sort-by-price a:after,.jd-search-tab li.sort-by-price.active a.arrow-down:after,.jd-search-tab li.sort-by-price.active a.arrow-up:after,.landing-keywords a:before,.region-title-back i,.sidebar-btn-location:after,.sidebar-categories .arrow,.sidebar-categories li.checked .tick,.supplier-arrow-right {
            display: block;

            background-size: 200px 200px
        }

        .bdr-tb-bold {
            border-top: 1px solid #e1e1e1;
            border-bottom: 1px solid #e1e1e1;
            border-width: 1px 0 1px 0;

        }

        .bdr-t-bold {
            border-top: 1px solid #e1e1e1;
            border-width: 1px 0 0 0;
            -webkit-border-image: url(data:image/gif;
            base64,R0lGODlhAgAEAJEDAMfGy8jHy8jGy////yH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4zLWMwMTEgNjYuMTQ1NjYxLCAyMDEyLzAyLzA2LTE0OjU2OjI3ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdFJlZj0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlUmVmIyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M2IChNYWNpbnRvc2gpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkY1NEY4MjZDNkQ3MTExRTRCOTRCQkI3NjVBQzgwRjQ3IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkY1NEY4MjZENkQ3MTExRTRCOTRCQkI3NjVBQzgwRjQ3Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6RjU0RjgyNkE2RDcxMTFFNEI5NEJCQjc2NUFDODBGNDciIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6RjU0RjgyNkI2RDcxMTFFNEI5NEJCQjc2NUFDODBGNDciLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4B//79/Pv6+fj39vX08/Lx8O/u7ezr6uno5+bl5OPi4eDf3t3c29rZ2NfW1dTT0tHQz87NzMvKycjHxsXEw8LBwL++vby7urm4t7a1tLOysbCvrq2sq6qpqKempaSjoqGgn56dnJuamZiXlpWUk5KRkI+OjYyLiomIh4aFhIOCgYB/fn18e3p5eHd2dXRzcnFwb25tbGtqaWhnZmVkY2JhYF9eXVxbWllYV1ZVVFNSUVBPTk1MS0pJSEdGRURDQkFAPz49PDs6OTg3NjU0MzIxMC8uLSwrKikoJyYlJCMiISAfHh0cGxoZGBcWFRQTEhEQDw4NDAsKCQgHBgUEAwIBAAAh+QQBAAADACwAAAAAAgAEAAACBAyGI1IAOw==) 2 0 stretch
        }

        .bdr-b-bold {
            border-bottom: 1px solid #e1e1e1;
            border-width: 0 0 1px 0;
            -webkit-border-image: url(data:image/gif;
            base64,R0lGODlhAgAEAJEDAMfGy8jHy8jGy////yH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4zLWMwMTEgNjYuMTQ1NjYxLCAyMDEyLzAyLzA2LTE0OjU2OjI3ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdFJlZj0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlUmVmIyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M2IChNYWNpbnRvc2gpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkY1NEY4MjZDNkQ3MTExRTRCOTRCQkI3NjVBQzgwRjQ3IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkY1NEY4MjZENkQ3MTExRTRCOTRCQkI3NjVBQzgwRjQ3Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6RjU0RjgyNkE2RDcxMTFFNEI5NEJCQjc2NUFDODBGNDciIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6RjU0RjgyNkI2RDcxMTFFNEI5NEJCQjc2NUFDODBGNDciLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4B//79/Pv6+fj39vX08/Lx8O/u7ezr6uno5+bl5OPi4eDf3t3c29rZ2NfW1dTT0tHQz87NzMvKycjHxsXEw8LBwL++vby7urm4t7a1tLOysbCvrq2sq6qpqKempaSjoqGgn56dnJuamZiXlpWUk5KRkI+OjYyLiomIh4aFhIOCgYB/fn18e3p5eHd2dXRzcnFwb25tbGtqaWhnZmVkY2JhYF9eXVxbWllYV1ZVVFNSUVBPTk1MS0pJSEdGRURDQkFAPz49PDs6OTg3NjU0MzIxMC8uLSwrKikoJyYlJCMiISAfHh0cGxoZGBcWFRQTEhEQDw4NDAsKCQgHBgUEAwIBAAAh+QQBAAADACwAAAAAAgAEAAACBAyGI1IAOw==) 2 0 stretch
        }

        .borb-bold {
            border-bottom: 0
        }

        .bdr-tb {
            position: relative
        }

        .bdr-tb::before {
            position: absolute;
            content: '';
            -webkit-transform: scaleY(.5);
            transform: scaleY(.5);
            top: -1px;
            left: 0;
            width: 100%;
            height: 1px;
            border-top: 1px solid #e3e5e9;
            z-index: 10
        }

        .bdr-tb::after {
            position: absolute;
            content: '';
            -webkit-transform: scaleY(.5);
            transform: scaleY(.5);
            bottom: -1px;
            left: 0;
            width: 100%;
            height: 1px;
            border-bottom: 1px solid #e3e5e9;
            z-index: 10
        }

        .bdr-t {
            position: relative
        }

        .bdr-t::before {
            position: absolute;
            content: '';
            -webkit-transform: scaleY(.5);
            transform: scaleY(.5);
            top: -1px;
            left: 0;
            width: 100%;
            height: 1px;
            border-top: 1px solid #e3e5e9;
            z-index: 10
        }

        .bdr-b {
            position: relative
        }

        .bdr-b::after {
            position: absolute;
            content: '';
            -webkit-transform: scaleY(.5);
            transform: scaleY(.5);
            bottom: -1px;
            left: 0;
            width: 100%;
            height: 1px;
            border-bottom: 1px solid #e3e5e9;
            z-index: 10
        }

        a.bdr-tb::before {
            border-color: #e3e5e9
        }

        a.bdr-tb::after {
            border-color: #e3e5e9
        }

        a.bdr-t::before {
            border-color: #e3e5e9
        }

        a.bdr-b::after {
            border-color: #e3e5e9
        }

        .borb::after {
            border-bottom: 0 solid #e3e5e9
        }

        .new-wt {
            background-color: #f0f2f5
        }

        .page-content {
            background-color: #f0f2f5
        }

        .goods-part {
            padding-left: 10px;
            background-color: #fff
        }

        .part-note-msg {
            display: inline-block;
            line-height: 15px;
            font-size: 13px;
            color: #81838e;
            width: 26px;
            white-space: normal;
            height: auto
        }

        .loc-serv-note,.prod-coupon,.prod-spec {
            height: auto;
            padding-top: 15px;
            font-size: 0;
            padding-bottom: 15px
        }

        .icon-arrow {
            position: absolute;
            right: 7px;
            width: 15px;
            height: 15px;

            background-repeat: no-repeat;
            background-size: 100px 100px
        }

        .icon-arrow-up {
            background-position: 3px -5px
        }

        .icon-arrow-down {
            background-position: 3px 6px
        }

        .icon-arrow-right {
            background-position: 5px -16px
        }

        .jd-slider-container {
            -webkit-transition: .5s
        }

        address,em {
            font-style: normal
        }

        .pro-color,.pro-count,.pro-size,.pro-spec,.spec-desc {
            font-size: 0;
            overflow: hidden;
            position: relative
        }

        .nowrap-txt {
            white-space: nowrap;
            text-overflow: ellipsis;
            overflow: hidden
        }

        .base-txt {
            margin-left: 34px;
            font-size: 13px;
            color: #252525;
            line-height: 15px
        }

        .msg-notice .part-note-msg,.prod-spec .part-note-msg,.provide-srv .part-note-msg,.send-to .part-note-msg {
            float: left
        }

        .icon {
            vertical-align: top;
            font-style: normal
        }

        .ext-warr .icon-arrow-down,.ext-warr .icon-arrow-up,.more-detail .icon-arrow-right {
            top: 15px
        }



        .cf:after,.cf:before {
            content: "";
            display: table
        }

        .cf:after {
            clear: both
        }

        .cf {
            zoom: 1
        }

        .mar-t {
            margin-top: 9px
        }

        .bottom-to-top {
            position: fixed;
            bottom: 62px;
            right: 9px;
            width: 38px;
            height: 38px;
            z-index: 20;
            display: none
        }

        .hold-div-top {
            height: 48px;
            width: 100%;
            display: block
        }

        .hold-div-bottom {
            height: 35px;
            width: 100%;
            display: block
        }

        .col02 {
            color: #686868;
            -moz-box-flex: 1;
            box-flex: 1;
            margin-left: 38px
        }

        #download-floor {
            display: block
        }

        #download-floor img {
            width: 100%;
            height: auto
        }

        .posi-rela {
            position: relative
        }

        .icon-popups {
            position: absolute;
            right: 10px;
            width: 19px;
            height: 4px;
            background-image: url(../images/5.4/product-detail-sprites-mjs.png?v=1);
            background-repeat: no-repeat;
            background-size: 100px 100px;
            background-position: -42px -17px
        }

        input::-webkit-inner-spin-button,input::-webkit-outer-spin-button {
            -webkit-appearance: none!important;
            margin: 0
        }

        .is-loading {
            display: none;
            height: 27px;
            text-align: center;
            font-size: 0
        }

        .is-loading em {
            background: url(../images/5.4/loading-animation.gif?v=1) center 2px no-repeat;
            display: inline-block;
            width: 23px;
            height: 25px;
            background-size: 23px 23px;
            vertical-align: top
        }

        .is-loading span {
            color: #b3b3b3;
            font-size: 11px;
            display: inline-block;
            line-height: 27px;
            margin-left: 10px
        }

        .header-slider {
            position: relative;
            width: 100%;
            overflow: hidden
        }

        .header-slider-con {
            overflow: hidden
        }

        .header-slider-con:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .header-slider-item {
            float: left;
            overflow: hidden;
            display: inline;
            width: 100%
        }

        .loading-position {
            position: absolute;
            top: 132px;
            left: 50%;
            margin-left: -44px;
            margin-top: -14px
        }

        .cart-loading-icon {
            position: fixed;
            display: none;
            top: 50%;
            left: 50%;
            margin-top: -18px;
            margin-left: -18px;
            z-index: 300;
            background: url(data:image/png;
            base64,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) no-repeat;background-size: 37px 37px;
            height: 37px;
            width: 37px;
            background-position: 0 0;
            -webkit-animation: rotateChange .8s cubic-bezier(.5,.5,.5,.5) infinite;
            -moz-animation: rotateChange .8s cubic-bezier(.5,.5,.5,.5) infinite;
            -ms-animation: rotateChange .8s cubic-bezier(.5,.5,.5,.5) infinite;
            -o-animation: rotateChange .8s cubic-bezier(.5,.5,.5,.5) infinite;
            animation: rotateChange .8s cubic-bezier(.5,.5,.5,.5) infinite
        }

        @-webkit-keyframes rotateChange {
            0% {
                -webkit-transform: rotate(0)
            }

            100% {
                -webkit-transform: rotate(360deg)
            }
        }

        @-moz-keyframes rotateChange {
            0% {
                -webkit-transform: rotate(0)
            }

            100% {
                -webkit-transform: rotate(360deg)
            }
        }

        @-ms-keyframes rotateChange {
            0% {
                -webkit-transform: rotate(0)
            }

            100% {
                -webkit-transform: rotate(360deg)
            }
        }

        @-o-keyframes rotateChange {
            0% {
                -webkit-transform: rotate(0)
            }

            100% {
                -webkit-transform: rotate(360deg)
            }
        }

        @keyframes rotateChange {
            0% {
                -webkit-transform: rotate(0)
            }

            100% {
                -webkit-transform: rotate(360deg)
            }
        }

        .bottom-tip-floor {
            width: 100%;
            height: 67px;
            text-align: center;
            padding-top: 21px
        }

        .bottom-tip {
            display: inline-block;
            line-height: 13px;
            font-size: 13px;
            color: #9b9c9f;
            vertical-align: middle
        }

        .btm-detail-icon {
            display: inline-block;
            width: 14px;
            height: 14px;
            background-image: url(../images/5.4/product-detail-sprites-mjs.png?v=1);
            background-repeat: no-repeat;
            background-size: 100px 100px;
            vertical-align: middle;
            margin-right: 2px
        }

        .btm-detail-icon.btm-up {
            background-position: -60px -67px
        }

        .btm-detail-icon.btm-down {
            background-position: -76px -67px
        }

        .btm-loading {
            line-height: 73px;
            height: 0;
            text-align: center;
            transition: height .25s,border-bottom-width .25s;
            background-color: #f3f4f6;
            font-size: 13px;
            color: #9b9c9f;
            overflow: hidden;
            vertical-align: middle
        }

        .btm-detail {
            background-color: #fff
        }

        .btm-detail-to-top {
            position: fixed;
            bottom: 62px;
            right: 9px;
            width: 38px;
            height: 38px;
            z-index: 20;
            display: none
        }

        .share-gift-wrap {
            position: fixed;
            width: 100%;
            height: 100%;
            top: 0;
            left: 0;
            z-index: 100;
            opacity: 0
        }

        .share-gift-show {
            opacity: 1;
            -webkit-transition: opacity .5s
        }

        .share-gift-btn {
            position: absolute;
            width: 70px;
            height: 70px;
            right: 10px;
            top: 41px;
            background: url(../images/5.4/icon_shareClose.png?v=1) no-repeat center/30px;
            border: none
        }

        .share-gift-content {
            height: 100%;
            border: none
        }

        .overflowHidden {
            overflow: hidden
        }

        .label-icon-div {
            font-style: normal;
            display: inline-block;
            height: 14px;
            line-height: 20px;
            font-size: 0;
            width: auto;
            border-radius: 2px;
            position: relative;
            box-sizing: border-box;
            overflow: hidden
        }

        .have-icon-div {
            border: 1px solid #f23030
        }

        .have-icon-div::after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .have-icon-div .label-text {
            float: left
        }

        .have-not-icon-div::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            border: 1px solid #f23030;
            border-radius: 3px;
            transform: scale(.5,.5);
            -webkit-transform: scale(.5,.5);
            transform-origin: left top;
            -webkit-transform-origin: left top;
            box-sizing: border-box;
            height: 28px;
            width: 200%;
            z-index: 1
        }

        .label-text {
            display: inline-block;
            padding: 2px 2px;
            font-size: 10px;
            color: #f23030;
            line-height: 10px;
            height: 14px
        }

        .label-icon {
            display: inline-block;
            background: url(../images/5.4/icon-sprites-extend.png?v=1) #f23030;
            background-size: 98px 14px;
            width: 14px;
            height: 14px;
            float: left
        }

        .phone-exclusive-icon {
            background-position: 0 -1px
        }

        .shop-vip-icon {
            background-position: -14px -1px
        }

        .seckill-icon {
            background-position: -28px -1px
        }

        .flash-icon {
            background-position: -42px -1px
        }

        .line-service {
            background-position: -56px -1px
        }

        .otc-icon {
            background-position: -70px -1px
        }

        .seckill-floor-icon {
            background-position: -84px -1px
        }

        .blue-border.have-icon-div {
            border: 1px solid #1a68bc
        }

        .blue-border.have-not-icon-div::before {
            border: 1px solid #1a68bc
        }

        .white-border.have-icon-div {
            border: 1px solid #fff
        }

        .white-border.have-not-icon-div::before {
            border: 1px solid #fff
        }

        .red-div {
            background-color: #f23030
        }

        .blue-div {
            background-color: #1a68bc
        }

        .jx-div {
            background-color: #c09947
        }

        .jx-border.have-not-icon-div::before {
            border: 1px solid #c09947
        }

        .jx-text {
            color: #c09947
        }

        .white-text {
            color: #fff
        }

        .red-text {
            color: #f23030
        }

        .blue-text {
            color: #1a68bc
        }

        .diamond-icon-div {
            background-color: #b400d3
        }

        .diamond-icon-div::before {
            border: 1px solid #b400d3
        }

        .diamond-icon-div .label-text {
            color: #fff
        }

        .global-icon {
            width: auto;
            height: 14px;
            vertical-align: top;
            margin-top: 3px
        }

        .double11-icon {
            width: auto;
            height: 14px;
            vertical-align: top;
            margin-top: 3px
        }

        .rxd-icon-text {
            padding: 2px 2px 2px 1px
        }

        .tryme {
            background: rgba(0,0,0,.8);
            width: 100%;
            height: 50px
        }

        .tryme div {
            width: 320px;
            height: 50px;
            margin: 0 auto;
            position: relative
        }

        .tryme .later {
            border: 0;
            display: block;
            left: 4px;
            top: 9px;
            width: 32px;
            height: 32px;
            position: absolute
        }

        .tryme .trynow {
            border: 0;
            display: block;
            left: 40px;
            width: 280px;
            height: 50px;
            position: absolute
        }

        .tryme span {
            display: block;
            width: 140px;
            margin-left: 90px;
            padding-top: 10px;
            color: #fff;
            font-size: 12px;
            line-height: 15px
        }

        .tryme-img {
            width: 320px;
            height: 50px;
            margin: 0 auto;
            position: absolute
        }

        .download-pannel {
            height: 50px;
            width: 100%;
            line-height: 50px;
            position: relative;
            font-size: 0;
            overflow: hidden
        }

        .download-pannel .pannel-bg {
            position: absolute;
            width: 100%;
            height: 100%;
            top: 0;
            left: 0;
            z-index: 4
        }

        .download-pannel .pannel-bg img {
            width: 100%;
            height: 100%
        }

        .download-pannel div[class^=download-] {
            position: relative;
            z-index: 5;
            float: left;
            overflow: hidden
        }

        .download-pannel .download-close {
            width: 16px;
            height: 50px;
            line-height: 50px;
            display: inline-block;
            margin-left: 2.5%
        }

        .download-pannel .download-close img {
            width: 100%;
            height: auto
        }

        .download-pannel .download-logo {
            width: 35px;
            height: 50px;
            line-height: 50px;
            margin-left: 5%;
            margin-right: 2.5%
        }

        .download-pannel .download-logo img {
            width: 100%;
            height: auto
        }

        .download-pannel .download-txt {
            display: inline-block;
            width: 40%;
            height: 100%
        }

        .download-pannel .font-large {
            font-size: 15px
        }

        .download-pannel .download-action {
            display: inline-block;
            height: 100%;
            width: 31.88%;
            float: right!important;
            text-align: center;
            font-size: 13px;
            color: #fff
        }

        .download-pannel .download-content {
            font-size: 12px;
            display: block;
            position: relative
        }

        .download-pannel .download-content .content-up {
            color: #fff;
            font-weight: 500;
            position: absolute;
            top: -9px
        }

        .download-pannel .download-content .content-down {
            color: #999;
            position: absolute;
            top: 9px
        }

        .download-pannel em {
            font-style: normal
        }

        .download-pannel div,.download-pannel img {
            vertical-align: middle
        }

        .download-noBg {
            background-color: #232326
        }

        .download-noBg .download-action {
            background-color: #cd2525
        }

        .download-noBg .pannel-bg {
            display: none
        }

        .jd-header,.jd-header a {
            font-family: PingFangSC-Regular,Helvetica,"Droid Sans",Arial,sans-serif;
            color: #252525;
            text-decoration: none;
            font-size: 16px
        }

        .jd-header {
            min-height: 44px;
            padding: 0;
            margin: 0;
            text-decoration: none;
            color: #252525;
            width: 100%
        }

        .jd-header-bar {
            position: relative;
            background: #efefef url(../images/5.4/header-bg.png?v=1) repeat-x 0 0;
            background-size: 100% 44px;
            min-height: 44px;
            border-bottom: 1px solid #bfbfbf;
            border-width: 0 0 1px 0;
            -webkit-border-image: url(data:image/gif;
            base64,R0lGODlhBAAEAIABAL+/v////yH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4zLWMwMTEgNjYuMTQ1NjYxLCAyMDEyLzAyLzA2LTE0OjU2OjI3ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjAxODAxMTc0MDcyMDY4MTE4MDgzOEM2RDA0Mzc2M0VFIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkRDNUJDQkY0OTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkRDNUJDQkYzOTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzYgKE1hY2ludG9zaCkiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpGNEEwMDBGQjIxMjA2ODExODA4M0YzNThFREFFRUMzMyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDowMTgwMTE3NDA3MjA2ODExODA4MzhDNkQwNDM3NjNFRSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgH//v38+/r5+Pf29fTz8vHw7+7t7Ovq6ejn5uXk4+Lh4N/e3dzb2tnY19bV1NPS0dDPzs3My8rJyMfGxcTDwsHAv769vLu6ubi3trW0s7KxsK+urayrqqmop6alpKOioaCfnp2cm5qZmJeWlZSTkpGQj46NjIuKiYiHhoWEg4KBgH9+fXx7enl4d3Z1dHNycXBvbm1sa2ppaGdmZWRjYmFgX15dXFtaWVhXVlVUU1JRUE9OTUxLSklIR0ZFRENCQUA/Pj08Ozo5ODc2NTQzMjEwLy4tLCsqKSgnJiUkIyIhIB8eHRwbGhkYFxYVFBMSERAPDg0MCwoJCAcGBQQDAgEAACH5BAEAAAEALAAAAAAEAAQAAAIFhBGpeVAAOw==) 2 0 stretch;-o-border-image: url(data:image/gif;
        base64,R0lGODlhBAAEAIABAL+/v////yH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4zLWMwMTEgNjYuMTQ1NjYxLCAyMDEyLzAyLzA2LTE0OjU2OjI3ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjAxODAxMTc0MDcyMDY4MTE4MDgzOEM2RDA0Mzc2M0VFIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkRDNUJDQkY0OTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkRDNUJDQkYzOTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzYgKE1hY2ludG9zaCkiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpGNEEwMDBGQjIxMjA2ODExODA4M0YzNThFREFFRUMzMyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDowMTgwMTE3NDA3MjA2ODExODA4MzhDNkQwNDM3NjNFRSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgH//v38+/r5+Pf29fTz8vHw7+7t7Ovq6ejn5uXk4+Lh4N/e3dzb2tnY19bV1NPS0dDPzs3My8rJyMfGxcTDwsHAv769vLu6ubi3trW0s7KxsK+urayrqqmop6alpKOioaCfnp2cm5qZmJeWlZSTkpGQj46NjIuKiYiHhoWEg4KBgH9+fXx7enl4d3Z1dHNycXBvbm1sa2ppaGdmZWRjYmFgX15dXFtaWVhXVlVUU1JRUE9OTUxLSklIR0ZFRENCQUA/Pj08Ozo5ODc2NTQzMjEwLy4tLCsqKSgnJiUkIyIhIB8eHRwbGhkYFxYVFBMSERAPDg0MCwoJCAcGBQQDAgEAACH5BAEAAAEALAAAAAAEAAQAAAIFhBGpeVAAOw==) 2 0 round;border-image: url(data:image/gif;
        base64,R0lGODlhBAAEAIABAL+/v////yH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4zLWMwMTEgNjYuMTQ1NjYxLCAyMDEyLzAyLzA2LTE0OjU2OjI3ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjAxODAxMTc0MDcyMDY4MTE4MDgzOEM2RDA0Mzc2M0VFIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkRDNUJDQkY0OTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkRDNUJDQkYzOTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzYgKE1hY2ludG9zaCkiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpGNEEwMDBGQjIxMjA2ODExODA4M0YzNThFREFFRUMzMyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDowMTgwMTE3NDA3MjA2ODExODA4MzhDNkQwNDM3NjNFRSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgH//v38+/r5+Pf29fTz8vHw7+7t7Ovq6ejn5uXk4+Lh4N/e3dzb2tnY19bV1NPS0dDPzs3My8rJyMfGxcTDwsHAv769vLu6ubi3trW0s7KxsK+urayrqqmop6alpKOioaCfnp2cm5qZmJeWlZSTkpGQj46NjIuKiYiHhoWEg4KBgH9+fXx7enl4d3Z1dHNycXBvbm1sa2ppaGdmZWRjYmFgX15dXFtaWVhXVlVUU1JRUE9OTUxLSklIR0ZFRENCQUA/Pj08Ozo5ODc2NTQzMjEwLy4tLCsqKSgnJiUkIyIhIB8eHRwbGhkYFxYVFBMSERAPDg0MCwoJCAcGBQQDAgEAACH5BAEAAAEALAAAAAAEAAQAAAIFhBGpeVAAOw==) 2 0 round
        }

        .jd-header-icon-back,.jd-header-icon-logo {
            position: absolute;
            width: 40px;
            height: 44px
        }

        .jd-header-icon-back span {
            width: 20px;
            height: 20px;
            background-position: -20px 0;
            margin: 12px 0 0 10px
        }

        .jd-header-icon-back span,.jd-header-icon-cancel span,.jd-header-icon-search span,.jd-header-icon-shortcut span {
            text-indent: -100px;
            overflow: hidden
        }

        .jd-header-title {
            margin: 0 50px;
            text-align: center;
            height: 44px;
            line-height: 44px;
            font-size: 16px
        }

        .jd-header-icon-category,.jd-header-icon-shortcut {
            position: absolute;
            right: 0;
            top: 0;
            width: 40px;
            height: 44px;
            overflow: hidden
        }

        .jd-header-icon-shortcut span {
            width: 20px;
            height: 20px;
            background-position: -60px 0;
            margin: 12px 10px 0 10px
        }

        .jd-header-shortcut span,.jd-header-shortcut strong {
            display: block;
            margin: 0 auto;
            color: #fff;
            font-weight: 400;
            font-size: 12px;
            line-height: 1
        }

        .jd-header-shortcut {
            display: table;
            width: 100%;
            background: #404042;
            height: 57px
        }

        .jd-header-shortcut li {
            display: table-cell;
            width: 20%;
            text-align: center
        }

        .jd-header-shortcut a {
            border: 0;
            color: #fff
        }

        .jd-header-shortcut span {
            width: 30px;
            height: 30px;
            margin-top: 6px
        }

        .jd-header-shortcut span.shortcut-categories {
            background-position: -60px -27px
        }

        .jd-header-shortcut span.shortcut-cart {
            background-position: -90px -27px
        }

        .jd-header-shortcut span.shortcut-my-account {
            background-position: -120px -27px
        }

        .jd-header-shortcut span.shortcut-home {
            background-position: -30px -27px
        }

        .jd-header-shortcut span.shortcut-my-history {
            background-position: -10px -105px
        }

        .jd-header-new-bar {
            width: 100%;
            position: relative;
            background: #efefef url(../images/5.4/header-bg.png?v=1) repeat-x 0 0;
            background-size: 100% 44px;
            min-height: 44px;
            z-index: 20
        }

        #header-tabs {
            overflow: hidden;
            margin: 0 80px;
            -webkit-tap-highlight-color: rgba(255,0,0,0);
            display: -moz-box;
            display: -webkit-box;
            display: box
        }

        .header-tab-item {
            text-align: center;
            box-flex: 1;
            -moz-box-flex: 1;
            -webkit-box-flex: 1;
            box-flex: 1;
            width: 1%;
            overflow: hidden;
            white-space: nowrap
        }

        .header-tab-title {
            font-size: 14px;
            line-height: 14px;
            padding-top: 15px;
            padding-bottom: 15px;
            color: #222;
            width: 28px;
            margin: 0 auto
        }

        .header-tab-selected {
            font-size: 15px;
            line-height: 15px;
            padding-top: 15px;
            padding-bottom: 12px;
            border-bottom: 2px solid #222;
            font-weight: 600;
            width: 34px;
            text-align: center
        }

        .header-bar-border {
            border-bottom: 1px solid #bbb;
            border-width: 0 0 1px 0;
            -webkit-border-image: url(data:image/gif;
            base64,R0lGODlhBAAEAIABAL+/v////yH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4zLWMwMTEgNjYuMTQ1NjYxLCAyMDEyLzAyLzA2LTE0OjU2OjI3ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjAxODAxMTc0MDcyMDY4MTE4MDgzOEM2RDA0Mzc2M0VFIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkRDNUJDQkY0OTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkRDNUJDQkYzOTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzYgKE1hY2ludG9zaCkiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpGNEEwMDBGQjIxMjA2ODExODA4M0YzNThFREFFRUMzMyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDowMTgwMTE3NDA3MjA2ODExODA4MzhDNkQwNDM3NjNFRSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgH//v38+/r5+Pf29fTz8vHw7+7t7Ovq6ejn5uXk4+Lh4N/e3dzb2tnY19bV1NPS0dDPzs3My8rJyMfGxcTDwsHAv769vLu6ubi3trW0s7KxsK+urayrqqmop6alpKOioaCfnp2cm5qZmJeWlZSTkpGQj46NjIuKiYiHhoWEg4KBgH9+fXx7enl4d3Z1dHNycXBvbm1sa2ppaGdmZWRjYmFgX15dXFtaWVhXVlVUU1JRUE9OTUxLSklIR0ZFRENCQUA/Pj08Ozo5ODc2NTQzMjEwLy4tLCsqKSgnJiUkIyIhIB8eHRwbGhkYFxYVFBMSERAPDg0MCwoJCAcGBQQDAgEAACH5BAEAAAEALAAAAAAEAAQAAAIFhBGpeVAAOw==) 2 0 stretch;-o-border-image: url(data:image/gif;
        base64,R0lGODlhBAAEAIABAL+/v////yH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4zLWMwMTEgNjYuMTQ1NjYxLCAyMDEyLzAyLzA2LTE0OjU2OjI3ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjAxODAxMTc0MDcyMDY4MTE4MDgzOEM2RDA0Mzc2M0VFIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkRDNUJDQkY0OTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkRDNUJDQkYzOTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzYgKE1hY2ludG9zaCkiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpGNEEwMDBGQjIxMjA2ODExODA4M0YzNThFREFFRUMzMyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDowMTgwMTE3NDA3MjA2ODExODA4MzhDNkQwNDM3NjNFRSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgH//v38+/r5+Pf29fTz8vHw7+7t7Ovq6ejn5uXk4+Lh4N/e3dzb2tnY19bV1NPS0dDPzs3My8rJyMfGxcTDwsHAv769vLu6ubi3trW0s7KxsK+urayrqqmop6alpKOioaCfnp2cm5qZmJeWlZSTkpGQj46NjIuKiYiHhoWEg4KBgH9+fXx7enl4d3Z1dHNycXBvbm1sa2ppaGdmZWRjYmFgX15dXFtaWVhXVlVUU1JRUE9OTUxLSklIR0ZFRENCQUA/Pj08Ozo5ODc2NTQzMjEwLy4tLCsqKSgnJiUkIyIhIB8eHRwbGhkYFxYVFBMSERAPDg0MCwoJCAcGBQQDAgEAACH5BAEAAAEALAAAAAAEAAQAAAIFhBGpeVAAOw==) 2 0 round;border-image: url(data:image/gif;
        base64,R0lGODlhBAAEAIABAL+/v////yH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4zLWMwMTEgNjYuMTQ1NjYxLCAyMDEyLzAyLzA2LTE0OjU2OjI3ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjAxODAxMTc0MDcyMDY4MTE4MDgzOEM2RDA0Mzc2M0VFIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkRDNUJDQkY0OTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkRDNUJDQkYzOTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzYgKE1hY2ludG9zaCkiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpGNEEwMDBGQjIxMjA2ODExODA4M0YzNThFREFFRUMzMyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDowMTgwMTE3NDA3MjA2ODExODA4MzhDNkQwNDM3NjNFRSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgH//v38+/r5+Pf29fTz8vHw7+7t7Ovq6ejn5uXk4+Lh4N/e3dzb2tnY19bV1NPS0dDPzs3My8rJyMfGxcTDwsHAv769vLu6ubi3trW0s7KxsK+urayrqqmop6alpKOioaCfnp2cm5qZmJeWlZSTkpGQj46NjIuKiYiHhoWEg4KBgH9+fXx7enl4d3Z1dHNycXBvbm1sa2ppaGdmZWRjYmFgX15dXFtaWVhXVlVUU1JRUE9OTUxLSklIR0ZFRENCQUA/Pj08Ozo5ODc2NTQzMjEwLy4tLCsqKSgnJiUkIyIhIB8eHRwbGhkYFxYVFBMSERAPDg0MCwoJCAcGBQQDAgEAACH5BAEAAAEALAAAAAAEAAQAAAIFhBGpeVAAOw==) 2 0 round;margin-top: -1px
        }

        .m_common_new_top {
            position: fixed;
            top: 0;
            left: 0;
            z-index: 100;
            width: 100%
        }

        .hide {
            display: none
        }

        .sift-tab {
            width: 100%;
            height: 42px;
            position: relative;
            background-color: #fff;
            display: none
        }

        .tab-lst {
            display: -moz-box;
            display: -webkit-box;
            display: box;
            width: 100%;
            position: relative
        }

        .tab-lst::after {
            position: absolute;
            content: '';
            -webkit-transform: scaleY(.5);
            transform: scaleY(.5);
            bottom: -1px;
            left: 0;
            width: 100%;
            height: 1px;
            border-bottom: 1px solid #e1e1e1;
            z-index: 10
        }

        .tab-lst li {
            box-flex: 1;
            -moz-box-flex: 1;
            -webkit-box-flex: 1;
            box-flex: 1;
            width: 1%;
            text-align: center
        }

        .tab-lst li a {
            display: block;
            font-size: 13px;
            color: #9a9a9a;
            padding: 13px 0 8px 0;
            line-height: 20px;
            text-shadow: 1px 1px 1px rgba(255,255,255,.2)
        }

        .tab-lst li a.on {
            color: #f15353!important
        }

        #tabs {
            overflow: hidden;
            width: 100%;
            background-color: #fff;
            font-family: SimHei,"Helvetica Neue",Arial,"Droid Sans",sans-serif;
            display: none;
            position: relative
        }

        #tabs:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        #tabs::before {
            position: absolute;
            content: '';
            -webkit-transform: scaleY(.5);
            transform: scaleY(.5);
            bottom: 0;
            left: 0;
            width: 100%;
            height: 1px;
            border-bottom: 1px solid #e2e2e2;
            z-index: 10
        }

        .tab-item {
            float: left;
            overflow: hidden;
            width: 20%;
            text-align: center;
            white-space: nowrap;
            color: #9a9a9a;
            -webkit-tap-highlight-color: transparent
        }

        .tab-assess-title {
            font-size: 12px;
            line-height: 12px;
            padding-top: 10px;
            padding-bottom: 5px
        }

        .tab-assess-num {
            font-size: 11px;
            line-height: 11px;
            padding-bottom: 9px
        }

        .tab-assess-rednum {
            font-size: 11px;
            line-height: 11px
        }

        .tab-hover {
            color: #f15353
        }

        .cart-concern-btm-fixed {
            display: table;
            position: fixed;
            left: 0;
            bottom: 0;
            z-index: 10;
            text-align: center;
            width: 100%;
            height: 50px;
            overflow: hidden
        }

        .cart-concern-btm-fixed:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .concern-cart {
            background-color: rgba(255,255,255,.9);
            position: relative
        }

        .concern-cart:before {
            position: absolute;
            content: '';
            -webkit-transform: scaleY(.5);
            transform: scaleY(.5);
            top: 0;
            left: 0;
            width: 100%;
            height: 1px;
            border-top: 1px solid #d2d2d2
        }

        .action-list a,.concern-cart a {
            display: inline-block;
            height: 100%;
            line-height: 50px;
            float: left;
            overflow: hidden;
            position: relative
        }

        .action-list,.concern-cart {
            float: left;
            height: 100%;
            overflow: hidden
        }

        .action-list a,.concern-cart a {
            width: 50%
        }

        .concern-cart a {
            color: #d4d4d4;
            font-size: 10px
        }

        em.btm-act-icn {
            width: 23px;
            height: 21px;
            display: block;
            margin: 0 auto;
            background-color: #fff;
            margin-top: 10px;
            margin-bottom: 4px;
            background: url(../images/5.4/cart_sprits_all.png?v=1) 0 0 no-repeat;
            background-size: 100px 100px;
            position: relative
        }

        .order-numbers {
            display: inline-block;
            position: absolute;
            top: -5px;
            background-color: #f23030;
            line-height: 10px;
            font-style: normal;
            border-radius: 8px;
            padding: 0 4px;
            font-size: 8px;
            color: #fff;
            right: -9px;
            border: 1px solid #fff
        }

        .dong-dong-icn .btm-act-icn {
            background-position: -26px 0;
            background-size: 100px 100px;
            width: 19px
        }

        .jimi-icn .btm-act-icn {
            background-position: -26px -21px;
            background-size: 100px 100px;
            width: 20px
        }

        .jx-icn .btm-act-icn {
            background-position: 0 0;
            background-size: 100px 100px;
            width: 20px;
            height: 20px;
            margin-bottom: 5px
        }

        .cart-car-icn .btm-act-icn {
            background-position: 0 -21px;
            background-size: 100px 100px;
            width: 22px
        }

        .drug-list-icn .btm-act-icn {
            background-position: -78px -2px;
            background-size: 100px 100px;
            width: 16px
        }

        .love-heart-icn .btm-act-icn {
            background-position: -50px -3px;
            background-size: 100px 100px;
            width: 22px
        }

        .love-heart-icn .btm-act-icn.focus-out {
            background-position: -50px -3px
        }

        .love-heart-icn .btm-act-icn.focus-on {
            background-position: -50px -25px
        }

        .concern-cart a span {
            display: block;
            line-height: 9px;
            height: 15px;
            text-align: center
        }

        .action-list a {
            color: #fff;
            font-size: 15px
        }

        .action-list a.cart-black-font {
            color: #333
        }

        .red-color {
            background-color: #f23030
        }

        .yellow-color {
            background-color: #ffb03f
        }

        .disabled {
            background-color: #bfbfbf!important
        }

        .jx-cart-yellow {
            background-color: #efc532
        }

        .five-column .concern-cart {
            width: 47.5%
        }

        .five-column .concern-cart a {
            width: 33.33%
        }

        .five-column .action-list {
            width: 52.5%
        }

        .five-column .action-list a {
            width: 50%
        }

        .four-column .concern-cart {
            width: 42%
        }

        .four-column .concern-cart a {
            width: 50%
        }

        .four-column .action-list {
            width: 58%
        }

        .four-column .action-list a {
            width: 50%
        }

        .four-column-l3r1 .concern-cart {
            width: 53.125%
        }

        .four-column-l3r1 .concern-cart a {
            width: 33.33%
        }

        .four-column-l3r1 .action-list {
            width: 46.875%
        }

        .four-column-l3r1 .action-list a {
            width: 100%
        }

        .three-column .concern-cart {
            width: 50%
        }

        .three-column .concern-cart a {
            width: 50%
        }

        .three-column .action-list {
            width: 50%
        }

        .three-column .action-list a {
            width: 100%
        }

        .three-column .action-list a:last-child {
            display: none
        }

        .message-floor {
            width: 100%;
            height: 100%;
            position: fixed;
            top: 0;
            left: 0;
            text-align: center;
            z-index: 500
        }

        .message-floor .messge-box {
            width: 145px;
            background: rgba(0,0,0,.8);
            border-radius: 8px;
            text-align: center;
            position: absolute;
            top: 50%;
            left: 50%
        }

        .messge-box .messge-box-icon {
            width: 26px;
            height: 26px;
            display: inline-block;
            margin: 18px 0 9px 0;
            position: relative;
            overflow: hidden;
            vertical-align: middle
        }

        .messge-box .messge-box-content {
            font-size: 15px;
            line-height: 15px;
            color: #fff;
            padding: 0 10px 21px 10px
        }

        .messge-box-icon h1 {
            color: #fff;
            display: inline;
            margin-top: 5px;
            font-size: 22px
        }

        .message-toast-icon {
            background: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAADgAAAB4CAYAAACn3jFyAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkQ4MzkzQzEzRjY0RjExRTU5MDI5RkQyNzk0QTQ5MjVBIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkQ4MzkzQzE0RjY0RjExRTU5MDI5RkQyNzk0QTQ5MjVBIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6RDgzOTNDMTFGNjRGMTFFNTkwMjlGRDI3OTRBNDkyNUEiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6RDgzOTNDMTJGNjRGMTFFNTkwMjlGRDI3OTRBNDkyNUEiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4a44QkAAAGEUlEQVR42uxc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) no-repeat;background-size: 28px 60px
        }

        .succee-icon {
            display: inline-block;
            height: 26px;
            width: 26px;
            background-position: -1px -29px
        }

        .error-icon {
            display: inline-block;
            height: 26px;
            width: 26px;
            background-position: -1px -1px
        }

        .focus-info {
            color: #232326;
            font-size: 9px
        }

        .focus-scale {
            background: url(../images/5.4/cart_sprits_all.png?v=1) no-repeat;
            background-size: 100px 100px;
            display: none;
            width: 22px;
            height: 17px;
            position: absolute;
            top: 10px;
            left: 50%;
            margin-left: -11px;
            background-position: -50px -25px
        }

        .click-focus-show {
            -webkit-animation: focused .2s 1 ease 0s;
            -moz-animation: focused .2s 1 ease 0s;
            -ms-animation: focused .2s 1 ease 0s;
            -o-animation: focused .2s 1 ease 0s;
            animation: focused .2s 1 ease 0s
        }

        .focus-scale-show {
            -webkit-animation: focusedScale .2s 1 ease 0s;
            -moz-animation: focusedScale .2s 1 ease 0s;
            -ms-animation: focusedScale .2s 1 ease 0s;
            -o-animation: focusedScale .2s 1 ease 0s;
            animation: focusedScale .2s 1 ease 0s
        }

        @-webkit-keyframes focused {
            0% {
                transform: scale(0,0);
                -webkit-transform: scale(0,0);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: 1
            }

            20% {
                transform: scale(.8,.8);
                -webkit-transform: scale(.8,.8);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .6
            }

            40% {
                transform: scale(1,1);
                -webkit-transform: scale(1,1);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .6
            }

            60% {
                transform: scale(1.1,1.1);
                -webkit-transform: scale(1.1,1.1);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .6
            }

            80% {
                transform: scale(1.2,1.2);
                -webkit-transform: scale(1.2,1.2);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .8
            }

            100% {
                transform: scale(1,1);
                -webkit-transform: scale(1,1);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: 1
            }
        }

        @-moz-keyframes focused {
            0% {
                transform: scale(0,0);
                -webkit-transform: scale(0,0);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: 1
            }

            20% {
                transform: scale(.8,.8);
                -webkit-transform: scale(.8,.8);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .6
            }

            40% {
                transform: scale(1,1);
                -webkit-transform: scale(1,1);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .6
            }

            60% {
                transform: scale(1.1,1.1);
                -webkit-transform: scale(1.1,1.1);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .6
            }

            80% {
                transform: scale(1.2,1.2);
                -webkit-transform: scale(1.2,1.2);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .8
            }

            100% {
                transform: scale(1,1);
                -webkit-transform: scale(1,1);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: 1
            }
        }

        @-ms-keyframes focused {
            0% {
                transform: scale(0,0);
                -webkit-transform: scale(0,0);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: 1
            }

            20% {
                transform: scale(.8,.8);
                -webkit-transform: scale(.8,.8);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .6
            }

            40% {
                transform: scale(1,1);
                -webkit-transform: scale(1,1);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .6
            }

            60% {
                transform: scale(1.1,1.1);
                -webkit-transform: scale(1.1,1.1);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .6
            }

            80% {
                transform: scale(1.2,1.2);
                -webkit-transform: scale(1.2,1.2);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .8
            }

            100% {
                transform: scale(1,1);
                -webkit-transform: scale(1,1);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: 1
            }
        }

        @-o-keyframes focused {
            0% {
                transform: scale(0,0);
                -webkit-transform: scale(0,0);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: 1
            }

            20% {
                transform: scale(.8,.8);
                -webkit-transform: scale(.8,.8);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .6
            }

            40% {
                transform: scale(1,1);
                -webkit-transform: scale(1,1);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .6
            }

            60% {
                transform: scale(1.1,1.1);
                -webkit-transform: scale(1.1,1.1);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .6
            }

            80% {
                transform: scale(1.2,1.2);
                -webkit-transform: scale(1.2,1.2);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .8
            }

            100% {
                transform: scale(1,1);
                -webkit-transform: scale(1,1);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: 1
            }
        }

        @keyframes focused {
            0% {
                transform: scale(0,0);
                -webkit-transform: scale(0,0);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: 1
            }

            20% {
                transform: scale(.8,.8);
                -webkit-transform: scale(.8,.8);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .6
            }

            40% {
                transform: scale(1,1);
                -webkit-transform: scale(1,1);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .6
            }

            60% {
                transform: scale(1.1,1.1);
                -webkit-transform: scale(1.1,1.1);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .6
            }

            80% {
                transform: scale(1.2,1.2);
                -webkit-transform: scale(1.2,1.2);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .8
            }

            100% {
                transform: scale(1,1);
                -webkit-transform: scale(1,1);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: 1
            }
        }

        @-webkit-keyframes focusedScale {
            0% {
                transform: scale(0,0);
                -webkit-transform: scale(0,0);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .2
            }

            20% {
                transform: scale(.6,.6);
                -webkit-transform: scale(.6,.6);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .2
            }

            40% {
                transform: scale(1.2,1.2);
                -webkit-transform: scale(1.2,1.2);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .1
            }

            60% {
                transform: scale(1.5,1.5);
                -webkit-transform: scale(1.5,1.5);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .1
            }

            80% {
                transform: scale(1.8,1.8);
                -webkit-transform: scale(1.8,1.8);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .1
            }

            100% {
                transform: scale(2,2);
                -webkit-transform: scale(2,2);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .1
            }
        }

        @-moz-keyframes focusedScale {
            0% {
                transform: scale(0,0);
                -webkit-transform: scale(0,0);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .2
            }

            20% {
                transform: scale(.6,.6);
                -webkit-transform: scale(.6,.6);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .2
            }

            40% {
                transform: scale(1.2,1.2);
                -webkit-transform: scale(1.2,1.2);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .1
            }

            60% {
                transform: scale(1.5,1.5);
                -webkit-transform: scale(1.5,1.5);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .1
            }

            80% {
                transform: scale(1.8,1.8);
                -webkit-transform: scale(1.8,1.8);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .1
            }

            100% {
                transform: scale(2,2);
                -webkit-transform: scale(2,2);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .1
            }
        }

        @-ms-keyframes focusedScale {
            0% {
                transform: scale(0,0);
                -webkit-transform: scale(0,0);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .2
            }

            20% {
                transform: scale(.6,.6);
                -webkit-transform: scale(.6,.6);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .2
            }

            40% {
                transform: scale(1.2,1.2);
                -webkit-transform: scale(1.2,1.2);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .1
            }

            60% {
                transform: scale(1.5,1.5);
                -webkit-transform: scale(1.5,1.5);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .1
            }

            80% {
                transform: scale(1.8,1.8);
                -webkit-transform: scale(1.8,1.8);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .1
            }

            100% {
                transform: scale(2,2);
                -webkit-transform: scale(2,2);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .1
            }
        }

        @-o-keyframes focusedScale {
            0% {
                transform: scale(0,0);
                -webkit-transform: scale(0,0);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .2
            }

            20% {
                transform: scale(.6,.6);
                -webkit-transform: scale(.6,.6);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .2
            }

            40% {
                transform: scale(1.2,1.2);
                -webkit-transform: scale(1.2,1.2);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .1
            }

            60% {
                transform: scale(1.5,1.5);
                -webkit-transform: scale(1.5,1.5);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .1
            }

            80% {
                transform: scale(1.8,1.8);
                -webkit-transform: scale(1.8,1.8);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .1
            }

            100% {
                transform: scale(2,2);
                -webkit-transform: scale(2,2);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .1
            }
        }

        @keyframes focusedScale {
            0% {
                transform: scale(0,0);
                -webkit-transform: scale(0,0);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .2
            }

            20% {
                transform: scale(.6,.6);
                -webkit-transform: scale(.6,.6);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .2
            }

            40% {
                transform: scale(1.2,1.2);
                -webkit-transform: scale(1.2,1.2);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .1
            }

            60% {
                transform: scale(1.5,1.5);
                -webkit-transform: scale(1.5,1.5);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .1
            }

            80% {
                transform: scale(1.8,1.8);
                -webkit-transform: scale(1.8,1.8);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .1
            }

            100% {
                transform: scale(2,2);
                -webkit-transform: scale(2,2);
                transform-origin: center center;
                -webkit-transform-origin: center center;
                opacity: .1
            }
        }

        .bottom-prompt {
            position: fixed;
            left: 0;
            bottom: 50px;
            z-index: 10;
            text-align: center;
            width: 100%;
            height: 35px;
            background-color: rgba(253,250,229,.9)
        }

        .bottom-prompt:before {
            position: absolute;
            content: '';
            -webkit-transform: scaleY(.5);
            transform: scaleY(.5);
            top: 0;
            left: 0;
            width: 100%;
            height: 1px;
            border-top: 1px solid #efe6d4
        }

        .prompt-title {
            line-height: 35px;
            color: #ec7307;
            font-size: 12px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            width: 72%;
            display: inline-block
        }

        .scroll-imgs {
            text-align: center;
            background-color: #fff;
            height: auto;
            font-size: 0
        }

        .slide {
            width: 320px;
            height: 320px;
            overflow: hidden;
            display: inline-block;
            position: relative
        }

        .slide ul {
            position: absolute;
            top: 0;
            left: 0;
            visibility: visible;
            z-index: 1
        }

        .slide ul li {
            width: 320px;
            height: 320px;
            visibility: visible;
            position: relative;
            float: left;
            display: -webkit-box;
            -webkit-box-pack: center;
            -webkit-box-align: center;
            background-color: #fff;
            background-size: 100% 100%
        }

        .slide ul li img {
            max-width: 100%;
            max-height: 100%;
            display: block;
            overflow: hidden
        }

        .slide ol {
            position: absolute;
            right: 10px;
            bottom: 10px;
            visibility: visible;
            z-index: 11
        }

        .slide ol li {
            float: left;
            width: 10px;
            height: 5px;
            visibility: visible;
            display: inline;
            background-color: red;
            margin-right: 5px
        }

        .slide ol li.active {
            background-color: #ff0
        }

        .slide .page-nub {
            font-size: 16px;
            z-index: 11;
            position: absolute;
            width: 40px;
            height: 40px;
            border-radius: 50%;
            -webkit-border-radius: 50%;
            background: rgba(0,0,0,.15);
            right: 15px;
            bottom: 11px;
            text-align: center;
            line-height: 40px;
            overflow: hidden
        }

        .slide .fz18 {
            font-size: 14px;
            display: inline-block;
            color: #fff;
            margin-right: -3px
        }

        .slide .nub-bg {
            display: inline-block;
            color: #fff;
            font-size: 12px
        }

        .slide .fz12 {
            font-size: 12px;
            color: #fff;
            margin-left: -3px
        }

        .slide .fz10 {
            font-size: 8px
        }

        .slide .tittup {
            width: 38px;
            height: 100%;
            display: block;
            position: absolute;
            right: -45px;
            top: 0;
            display: -webkit-box;
            -webkit-box-align: center
        }

        .slide .tittup .inner {
            width: 18px;
            display: block;
            font-size: 12px;
            color: #252525;
            margin-left: 7px;
            line-height: 17px
        }

        .slide .arrow-box {
            width: 15px;
            height: 15px;
            position: relative;
            -webkit-transition: 1s;
            transition: 1s
        }

        .slide .rotate {
            -webkit-transform: rotate3d(0,7.5,0,180deg);
            transform: rotate3d(0,7.5,0,180deg)
        }

        .slide .arrow {
            width: 15px;
            height: 15px;
            display: block;
            background-color: #fff;
            position: absolute;
            top: 0;
            left: 0;
            no-repeat #fff;
            background-size: 100px 100px;
            background-position: 0 -67px
        }

        .slide .arrow2 {
            width: 15px;
            height: 15px;
            display: block;
            position: absolute;
            top: 0;
            left: 0;
            no-repeat #fff;
            background-size: 100px 100px;
            background-position: -15px -67px
        }

        .scroll-imgs-label {
            width: 145px;
            height: auto;
            display: block;
            position: absolute;
            top: 0;
            right: 0
        }

        .scroll-imgs-jx-label {
            width: 78px;
            height: auto;
            display: block;
            position: absolute;
            left: 10px;
            bottom: 10px
        }

        .seckill-floor {
            width: 100%;
            height: 50px
        }

        .seckill-floor::after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .seckil-price-wrap {
            float: left;
            width: 70%;
            height: 100%;
            font-size: 0;
            background: -webkit-linear-gradient(left,#ef3390,#ef3647);
            background: -o-linear-gradient(right,#ef3390,#ef3647);
            background: -moz-linear-gradient(right,#ef3390,#ef3647);
            background: linear-gradient(to right,#ef3390,#ef3647)
        }

        .seckil-price-wrap .seckill-price {
            display: inline-block;
            font-size: 14px;
            color: #fff;
            margin: 3px 0 0 8px
        }

        .seckil-price-wrap .seckill-price .seckill-big-price {
            font-size: 26px;
            line-height: 26px
        }

        .seckil-price-wrap .seckill-btm-div {
            margin: 1px 0 0 8px;
            font-size: 0
        }

        .seckil-price-wrap .seckill-btm-div .skf-icon-pos {
            display: inline-block;
            margin-right: 5px;
            vertical-align: middle
        }

        .seckil-price-wrap .seckill-btm-div .skf-jdPrice {
            display: inline-block;
            color: #fff;
            font-size: 12px;
            line-height: 14px;
            text-decoration: line-through;
            vertical-align: middle
        }

        .seckil-time-wrap {
            float: left;
            width: 30%;
            height: 100%;
            background-color: #feeae9;
            text-align: center;
            font-size: 0
        }

        .seckil-time-wrap .seckil-time-title {
            margin: 6px 0 3px;
            display: inline-block;
            font-size: 11px;
            line-height: 11px;
            color: #e8063c
        }

        .seckil-time-wrap .seckill-time-div .seckill-time-num {
            display: inline-block;
            width: 20px;
            height: 24px;
            border-radius: 5px;
            background-color: #ef3561;
            color: #fff;
            font-size: 12px;
            line-height: 24px;
            text-align: center
        }

        .seckil-time-wrap .seckill-time-div .seckill-time-colon {
            display: inline-block;
            width: 5px;
            height: 21px;
            line-height: 21px;
            text-align: center;
            font-size: 16px;
            color: #ef3561
        }

        .basic-info .prod-title {
            position: relative
        }

        .basic-info .prod-act {
            font-size: 13px;
            color: #81838e;
            margin: 11px 10px 0 0;
            line-height: 17px;
            text-align: justify
        }

        .basic-info .prod-act a {
            color: #f23030;
            border-bottom: 1px solid #f23030;
            margin-left: 3px
        }

        .basic-info .act-link {
            color: #f15353;
            margin-top: 8px;
            font-size: 13px;
            line-height: 18px;
            margin-right: 8px
        }

        .basic-info .prod-price-wrap .prod-price {
            font-size: 23px;
            font-family: Helvetica;
            color: #f23030;
            font-weight: 700;
            line-height: 23px;
            float: left;
            margin-top: 7px;
            margin-right: 6px
        }

        .basic-info .prod-price-wrap .prod-price:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .basic-info .prod-price-wrap .prod-sam-price-a {
            float: left
        }

        .basic-info .prod-price-wrap .prod-sam-price-a:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .basic-info .prod-price-wrap .prod-sam-price {
            font-size: 16px;
            font-family: Helvetica;
            color: #1a68bc;
            font-weight: 700;
            line-height: 16px;
            float: left;
            margin-top: 12px
        }

        .basic-info .prod-price-wrap .prod-sam-price:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .basic-info .prod-price-wrap:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .basic-info .sam-member-wrap {
            padding: 13px 10px 12px 0
        }

        .basic-info .sam-member-wrap .buy-sam-card-a {
            float: left;
            font-size: 0;
            line-height: 18px;
            height: 18px
        }

        .basic-info .sam-member-wrap .bound-sam-card-a {
            float: right;
            font-size: 0;
            line-height: 18px;
            height: 18px
        }

        .basic-info .sam-member-wrap:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .basic-info .prod-seckill {
            font-size: 0;
            margin-top: 7px;
            position: relative
        }

        .prod-title a {
            text-underline: none;
            display: block
        }

        .prod-title .title-text-wrap {
            display: inline-block;
            font-size: 0;
            color: #232326;
            margin-right: 10px;
            line-height: 20px;
            text-align: left
        }

        .prod-title .title-text-wrap .goods-sign {
            margin-right: 4px
        }

        .prod-title .title-text-wrap .icon-pos {
            vertical-align: top;
            margin-top: 3px;
            margin-right: 6px
        }

        .prod-title .title-text-wrap .title-text {
            font-size: 16px;
            color: #232326;
            line-height: 20px
        }

        .prod-title .title-text-wrap .jx-title-text {
            font-size: 14px;
            font-weight: 700;
            line-height: 18px
        }

        .prod-title .jx-title-text-wrap .icon-pos {
            margin-top: 3px
        }

        .act-link a {
            border-bottom: solid 1px red
        }

        .prod-seckill .phone-exclusive-pos {
            font-size: 0;
            margin-right: 5px;
            display: inline-block
        }

        .yang-pic {
            float: left;
            display: block;
            no-repeat 0 0;
            background-size: 100px 100px;
            background-position: -14px -17px;
            width: 10px;
            height: 12px;
            margin-top: 7px
        }

        .sam-icon {
            background: url(../images/5.4/sam-icon.png?v=1) #1a68bc 0 -1px;
            background-size: 30px 14px;
            width: 30px;
            height: 14px;
            float: left
        }

        .sam-yang-pic {
            background-position: -14px -30px;
            width: 10px;
            height: 12px;
            margin-top: 3px
        }

        .yang-pic-price {
            display: block;
            float: left;
            margin-left: 3px;
            line-height: 23px
        }

        .sam-yang-pic-price {
            line-height: 16px
        }

        .sam-triangle-icon {
            float: left;
            width: 0;
            height: 0;
            border-left: 2px solid transparent;
            border-top: 2px solid #1a68bc;
            margin-top: 4px;
            margin-left: 2px
        }

        .sam-pic-icon-box {
            float: left;
            margin-top: 4px;
            width: 24px;
            height: 9px;
            overflow: hidden
        }

        .sam-pic-icon {
            background-color: #1a68bc;
            color: #fff;
            font-size: 14px;
            width: 48px;
            height: 18px;
            line-height: 18px;
            text-align: center;
            transform: scale(.5,.5);
            -webkit-transform: scale(.5,.5);
            transform-origin: left top;
            -webkit-transform-origin: left top
        }

        .sam-introduce-icon {
            float: left;
            no-repeat 0 0;
            background-size: 100px 100px;
            background-position: -87px -17px;
            width: 12px;
            height: 12px;
            margin-left: 4px;
            margin-top: 2px
        }

        .buy-sam-card {
            display: inline-block;
            color: #1a68bc;
            font-size: 12px;
            line-height: 18px
        }

        .sam-jump-entry-icon {
            display: inline-block;
            width: 5px;
            height: 5px;
            border-width: 1px;
            border-style: solid;
            border-color: #1a68bc #1a68bc transparent transparent;
            transform: rotate(45deg);
            -ms-transform: rotate(45deg);
            -moz-transform: rotate(45deg);
            -webkit-transform: rotate(45deg);
            -o-transform: rotate(45deg);
            margin-bottom: 1px
        }

        .bound-sam-card {
            display: inline-block;
            color: #686868;
            font-size: 12px;
            line-height: 18px
        }

        .bound-entry {
            color: #1a68bc;
            display: inline-block;
            margin-left: 5px
        }

        .sam-experience {
            display: inline-block;
            color: #1a68bc;
            font-size: 12px;
            line-height: 18px;
            word-wrap: break-word
        }

        .reservation-num {
            font-size: 13px;
            line-height: 13px;
            margin-top: 10px;
            color: #252525
        }

        .reservation-red {
            color: #f23030
        }

        .big-price {
            display: inline-block;
            font-size: 23px;
            line-height: 23px
        }

        .small-price {
            display: inline-block;
            font-size: 17px;
            line-height: 17px
        }

        .price-floor {
            padding-bottom: 10px;
            padding-top: 7px
        }

        .oversea-tip {
            margin-top: 10px;
            line-height: 14px;
            word-wrap: break-word;
            margin-right: 10px;
            font-size: 0
        }

        .jx-oversea-tip {
            margin-top: 13px
        }

        .oversea-tax {
            font-size: 13px;
            color: #252525;
            line-height: 14px;
            display: inline-block;
            text-align: justify;
            vertical-align: top
        }

        .jx-oversea-tax-title {
            font-size: 13px;
            color: #999;
            line-height: 14px;
            display: inline-block;
            margin-right: 9px;
            vertical-align: top
        }

        .oversea-tax-icon {
            width: 12px;
            height: 12px;
            vertical-align: top;
            margin-left: 5px
        }

        .customize-icon {
            background: url(../images/5.4/customize-icon.png?v=1) no-repeat 0 0;
            background-size: 20px 13px;
            width: 20px;
            height: 13px;
            float: left;
            margin-left: -2px;
            margin-top: 13px
        }

        .plus-price-wrap .plus-price-row {
            padding-top: 3px
        }

        .plus-price-wrap .plus-price-row::after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .plus-price-wrap .price-icon {
            display: inline-block;
            no-repeat 0 0;
            background-size: 100px 100px
        }

        .plus-price-wrap .price-icon.black {
            background-position: 0 -32px;
            width: 9px;
            height: 11px;
            position: relative;
            top: 1px
        }

        .plus-price-wrap .price-icon.grey {
            background-position: -90px -30px;
            width: 8px;
            height: 10px;
            position: relative;
            top: 1px
        }

        .plus-price-mt {
            margin-top: 6px
        }

        .plus-jd-price {
            display: inline-block;
            font-size: 0;
            float: left
        }

        .plus-jd-price-title {
            display: inline-block;
            color: #999;
            font-size: 12px;
            line-height: 12px;
            position: relative;
            margin-top: 1px
        }

        .plus-jd-price-text {
            display: inline-block;
            color: #999;
            font-size: 13px;
            line-height: 14px
        }

        .plus-member-price {
            display: block;
            font-size: 0;
            float: left
        }

        .plus-member-price-title {
            display: inline-block;
            color: #333;
            font-size: 13px;
            line-height: 13px;
            font-weight: 700;
            position: relative;
            margin-top: 1px
        }

        .plus-member-price-text {
            display: inline-block;
            color: #333;
            font-size: 15px;
            line-height: 15px;
            margin-right: 3px;
            font-weight: 700
        }

        .plus-pri-icon {
            width: 30px;
            height: 10px;
            position: relative;
            vertical-align: baseline
        }

        .plus-favor {
            color: #333;
            font-size: 13px;
            line-height: 13px;
            margin-right: 10px;
            float: right
        }

        .jx-bdrt {
            margin-top: 13px
        }

        .js-rec-wrap {
            padding: 14px 10px 4px 0;
            text-align: justify;
            line-height: 14px;
            font-size: 0
        }

        .jx-rec-pct {
            display: inline-block;
            width: 8px;
            height: 7px;
            no-repeat 0 0;
            background-size: 100px 100px
        }

        .jx-rec-pct-before {
            margin-left: 10px;
            margin-right: 5px;
            background-position: -64px -53px;
            vertical-align: top
        }

        .jx-rec-pct-after {
            background-position: -74px -53px;
            vertical-align: bottom
        }

        .jx-rec-er {
            color: #333;
            font-size: 12px;
            font-weight: 700;
            margin-right: 8px
        }

        .jx-rec-er-desc {
            color: #999;
            font-size: 12px
        }

        .open-plus {
            display: block;
            height: 31px;
            line-height: 31px;
            position: relative;
            font-size: 0
        }

        .open-plus .icon-arrow-right {
            top: 8px
        }

        .open-plue-no-adword {
            margin-bottom: -10px
        }

        .open-plus-icon {
            display: inline-block;
            width: 45px;
            height: 15px;
            border-radius: 3px;
            vertical-align: top;
            top: 8px;
            position: relative;
            margin-right: 10px
        }

        .open-plus-text {
            display: inline-block;
            font-size: 12px;
            color: #333;
            line-height: 31px
        }

        .plusPreBuy-yuyue-floor {
            width: 100%;
            height: 33px;
            background-color: #fffcd7;
            padding-left: 10px;
            box-sizing: border-box
        }

        .plusPreBuy-yuyue-floor::after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .plusPreBuy-yuyue-plusIcon {
            display: inline-block;
            width: 45px;
            height: 14px;
            border-radius: 3px;
            vertical-align: top;
            top: 9px;
            position: relative;
            margin-right: 5px
        }

        .plusPreBuy-yuyue-text {
            color: #c98506;
            font-size: 12px;
            line-height: 33px;
            height: 33px;
            display: inline-block;
            max-width: 50%;
            white-space: nowrap;
            text-overflow: ellipsis;
            overflow: hidden
        }

        .plusPreBuy-href {
            float: right;
            display: block;
            font-size: 0
        }

        .plusPreBuy-yuyue-triangle {
            display: inline-block;
            width: 0;
            height: 0;
            border-right: 25px solid #feee9f;
            border-top: 33px solid transparent
        }

        .plusPreBuy-yuyue-login {
            display: inline-block;
            background-color: #feee9f;
            width: 72px;
            height: 33px;
            font-size: 0;
            vertical-align: top
        }

        .plusPreBuy-yuyue-login-text {
            margin-left: 14px;
            font-size: 12px;
            color: #c98506;
            line-height: 33px;
            display: inline-block
        }

        .x-plusPreBuy-Seckill-wrap {
            height: 33px;
            width: 100%;
            background-color: #fffeee;
            display: -webkit-box;
            -webkit-box-align: center;
            padding: 0 10px;
            -moz-box-sizing: border-box;
            -webkit-box-sizing: border-box;
            -o-box-sizing: border-box;
            -ms-box-sizing: border-box;
            box-sizing: border-box
        }

        .x-plusPreBuy-Seckill-text {
            font-size: 12px;
            color: #c98506;
            line-height: 33px;
            height: 33px;
            white-space: nowrap;
            text-overflow: ellipsis;
            overflow: hidden
        }

        .x-plusPreBuy-Seckill-text-left {
            -webkit-box-flex: 1
        }

        .x-plusPreBuy-Seckill-icon {
            width: 5px;
            height: 10px;
            display: inline-block;
            background: url(../images/5.4/icon-plusPreBuy-arrowRight.png) no-repeat center/contain;
            margin-left: 7px;
            position: relative;
            top: 1px
        }

        .depreciate-arrival-inform-box {
            float: right;
            margin-right: 10px;
            margin-top: 5px;
            z-index: 4000;
            overflow: hidden;
            height: 26px;
            width: 63px
        }

        .product-inform-floor {
            position: absolute;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,.7);
            z-index: 100;
            top: 0;
            left: 0;
            display: none
        }

        .depreciate-inform-block {
            height: 280px;
            width: 272px;
            background-color: #f8f8f8;
            position: absolute;
            top: 258px;
            left: 50%;
            z-index: 1000;
            display: none;
            border-radius: 8px;
            margin-left: -136px
        }

        .depreciate-inform-title {
            display: block;
            text-align: center;
            font-size: 16px;
            color: #686868;
            padding-top: 14px;
            padding-bottom: 12px;
            line-height: 16px
        }

        .depreciate-inform-title2 {
            display: block;
            text-align: center;
            font-size: 13px;
            color: #848689;
            padding-bottom: 16px;
            line-height: 13px
        }

        .input-box {
            width: 233px;
            height: 42px;
            margin-left: 13px;
            padding-left: 11px;
            border-radius: 3px;
            font-size: 14px;
            color: #686868;
            border: 1px solid #dbdbdb;
            outline: 0;
            -webkit-appearance: none
        }

        .input-prompt {
            display: block;
            height: 24px;
            margin-left: 13px;
            line-height: 24px;
            font-size: 12px;
            color: #f15353;
            visibility: hidden
        }

        .phone-num-prompt {
            position: relative;
            display: block;
            height: 27px;
            margin-left: 13px;
            line-height: 27px;
            font-size: 12px;
            color: #f15353;
            visibility: hidden
        }

        .cancel-but-box {
            float: left;
            height: 42px;
            width: 118px;
            margin-left: 12px;
            margin-top: 15px;
            overflow: hidden
        }

        .yes-but {
            height: 40px;
            width: 116px;
            margin-left: 12px;
            margin-top: 15px;
            color: #fff;
            font-size: 14px;
            border: 1px solid #f15353;
            border-radius: 3px;
            text-align: center;
            line-height: 40px;
            float: left;
            background-color: #f15353
        }

        .discount-block {
            position: absolute;
            top: 85px;
            right: 20px;
            border: 1px solid #f15353;
            text-align: center;
            height: 12px;
            line-height: 12px;
            color: #f15353;
            padding-left: 6px;
            padding-right: 6px;
            font-size: 10px;
            display: none
        }

        .arrval-block {
            height: 212px
        }

        .ppp {
            position: absolute;
            top: 450px;
            right: 0
        }

        .cancel-yes-box {
            position: absolute
        }

        .cancel-yes-box:before {
            position: absolute;
            content: '';
            -webkit-transform: scaleY(.5);
            transform: scaleY(.5);
            height: 0;
            top: 1px;
            left: 13px;
            width: 246px;
            border-top: 1px solid #bdbdbd
        }

        .depreciate-arrival-inform {
            border: 1px solid #bfbfbf;
            color: #686868;
            font-size: 24px;
            height: 50px;
            width: 124px;
            line-height: 50px;
            text-align: center;
            border-radius: 6px;
            -webkit-border-radius: 6px;
            transform: scale(.5,.5);
            -webkit-transform: scale(.5,.5);
            transform-origin: left top;
            -webkit-transform-origin: left top
        }

        .tbl-type {
            display: table;
            width: 100%
        }

        .tbl-cell {
            display: table-cell;
            margin-right: 13px
        }

        .btn-cart-1 {
            background: #6cb248
        }

        .detail-tbn2 {
            padding: 5px 0;
            background: #f1f1f5
        }

        .detail-tbn2 .tbl-cell {
            width: 50%;
            text-align: center
        }

        .detail-tbn2 .tbl-cell a {
            display: block;
            height: 35px;
            margin: 0 10px;
            border-radius: 3px;
            line-height: 35px;
            color: #fff
        }

        .detail-tbn2 .tbl-cell a.btn-yuyue2 {
            text-align: center;
            padding: 8px 10px 7px 30px
        }

        .detail-tbn2 .tbl-cell a.btn-yuyue2 .txt-yuyue {
            float: none
        }

        .detail-tbn2 .tbl-cell a.btn-yuyue2 .icon-clock {
            margin-right: 10px
        }

        .detail-tbn2 .tbl-cell .txt-yuyue {
            float: right;
            color: #e4393c
        }

        .detail-tbn2 .tbl-cell a.btn-yuyue,.detail-tbn2 .tbl-cell a.btn-yuyue2 {
            padding: 7px 30px 7px 10px;
            text-align: left;
            color: #333;
            line-height: normal;
            height: auto;
            background: #f3f3f3;
            background: -webkit-gradient(linear,left top,left bottom,from(#fff),to(#ededed));
            background: -moz-linear-gradient(top,#fff,#ededed);
            font-size: 14px
        }

        .icon-clock {
            display: inline-block;
            width: 20px;
            height: 20px;
            margin-right: 5px;
            background: url(/images/touch2013/icon_clock.png) 0 0 no-repeat;
            background-size: 100% 100%;
            vertical-align: -4px
        }

        .detail-tbn2 .tbl-cell .txt-yuyue span,.detail-tbn2 .tbl-cell .txt-yuyue2 span {
            display: inline-block;
            padding-left: 10px;
            height: 15px;
            border-left: 1px solid #e4393c;
            vertical-align: -2px
        }

        .absolute-box {
            position: relative;
            margin-bottom: 30px;
            padding-left: 50px
        }

        .absolute-box .col02 {
            position: absolute;
            top: -35px
        }

        .prod-whitebar {
            padding: 15px 0;
            position: relative
        }

        .prod-whitebar .part-note-msg {
            float: left
        }

        .prod-whitebar-txt {
            margin-left: 34px;
            font-size: 13px;
            color: #252525;
            line-height: 15px;
            margin-right: 28px;
            white-space: nowrap;
            text-overflow: ellipsis;
            overflow: hidden
        }

        .flick-icon {
            display: block;
            no-repeat;
            background-size: 100px 100px;
            width: 18px;
            height: 4px;
            background-position: -42px -17px;
            position: absolute;
            right: 10px;
            top: 21px
        }

        .coupons-container {
            height: 62px;
            overflow: hidden
        }

        .coupon_unit {
            position: relative;
            width: 122px;
            height: 62px;
            border-radius: 5px;
            box-sizing: border-box;
            margin-right: 10px;
            float: left
        }

        .coupon_unit:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .expe_disc {
            width: 96px;
            height: 100%;
            float: left
        }

        .expeNum {
            padding-top: 2px;
            line-height: 36px;
            padding-left: 8px;
            overflow: hidden
        }

        .expeNum span {
            float: left
        }

        .condi_msg {
            height: 22px;
            font-size: 12px;
            padding-left: 8px;
            line-height: 22px
        }

        em.left_m {
            position: absolute;
            width: 4px;
            height: 9px;
            left: -1px;
            top: 26px
        }

        .coupon-total-nums {
            height: 15px;
            display: inline-block;
            line-height: 15px;
            font-size: 13px;
            color: #848689;
            letter-spacing: 1px;
            float: right;
            margin-right: 10px
        }

        .coupon_unit .coupon_icon {
            width: 5px;
            height: 62px;
            margin-top: -1px;
            float: left
        }

        .coupon_unit .up {
            height: 38px;
            padding-top: 9px;
            line-height: 12px;
            padding-right: 3px;
            box-sizing: border-box
        }

        .coupon_unit .down {
            height: 22px;
            line-height: 12px;
            padding-right: 3px;
            padding-top: 1px;
            box-sizing: border-box
        }

        .oper_msg {
            float: left;
            width: 19px;
            height: 100%;
            font-size: 13px;
            text-align: center;
            box-sizing: border-box
        }

        .rmb {
            font-size: 18px;
            height: 29px;
            line-height: 32px;
            text-indent: -3px;
            margin: 4px 3px 0 3px;
            font-weight: 600
        }

        .actual-number {
            font-size: 25px;
            font-weight: 600
        }

        .coupon_unit.blue {
            border: 1px solid #60bbf5;
            border-width: 1px 1px 1px 1px;
            background-color: #fff;
            -webkit-border-image: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAJCAMAAADXT/YiAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpGQjI4QkE0QzM2ODAxMUU1ODE0RkVDNjE2MzQxRUJGMSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpGQjI4QkE0QjM2ODAxMUU1ODE0RkVDNjE2MzQxRUJGMSIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpEMDEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgqlCaYAAAAPUExURWe+9Wa99WC79f///////8YJFhsAAAAFdFJOU/////8A+7YOUwAAAChJREFUeNpiYGFkAgFGZgZGRmYQYGRgYGIBs1iYiGYh9DJDzWMBCDAAJR8A0n97w3QAAAAASUVORK5CYII=) 2 round;-moz-border-image: url(data:image/png;
        base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAJCAMAAADXT/YiAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpGQjI4QkE0QzM2ODAxMUU1ODE0RkVDNjE2MzQxRUJGMSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpGQjI4QkE0QjM2ODAxMUU1ODE0RkVDNjE2MzQxRUJGMSIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpEMDEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgqlCaYAAAAPUExURWe+9Wa99WC79f///////8YJFhsAAAAFdFJOU/////8A+7YOUwAAAChJREFUeNpiYGFkAgFGZgZGRmYQYGRgYGIBs1iYiGYh9DJDzWMBCDAAJR8A0n97w3QAAAAASUVORK5CYII=) 2 round;-ms-border-image: url(data:image/png;
        base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAJCAMAAADXT/YiAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpGQjI4QkE0QzM2ODAxMUU1ODE0RkVDNjE2MzQxRUJGMSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpGQjI4QkE0QjM2ODAxMUU1ODE0RkVDNjE2MzQxRUJGMSIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpEMDEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgqlCaYAAAAPUExURWe+9Wa99WC79f///////8YJFhsAAAAFdFJOU/////8A+7YOUwAAAChJREFUeNpiYGFkAgFGZgZGRmYQYGRgYGIBs1iYiGYh9DJDzWMBCDAAJR8A0n97w3QAAAAASUVORK5CYII=) 2 round;-o-border-image: url(data:image/png;
        base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAJCAMAAADXT/YiAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpGQjI4QkE0QzM2ODAxMUU1ODE0RkVDNjE2MzQxRUJGMSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpGQjI4QkE0QjM2ODAxMUU1ODE0RkVDNjE2MzQxRUJGMSIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpEMDEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgqlCaYAAAAPUExURWe+9Wa99WC79f///////8YJFhsAAAAFdFJOU/////8A+7YOUwAAAChJREFUeNpiYGFkAgFGZgZGRmYQYGRgYGIBs1iYiGYh9DJDzWMBCDAAJR8A0n97w3QAAAAASUVORK5CYII=) 2 round;border-image: url(data:image/png;
        base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAJCAMAAADXT/YiAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpGQjI4QkE0QzM2ODAxMUU1ODE0RkVDNjE2MzQxRUJGMSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpGQjI4QkE0QjM2ODAxMUU1ODE0RkVDNjE2MzQxRUJGMSIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpEMDEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgqlCaYAAAAPUExURWe+9Wa99WC79f///////8YJFhsAAAAFdFJOU/////8A+7YOUwAAAChJREFUeNpiYGFkAgFGZgZGRmYQYGRgYGIBs1iYiGYh9DJDzWMBCDAAJR8A0n97w3QAAAAASUVORK5CYII=) 2 round
        }

        .coupon_unit.blue .coupon_icon {
            background: url(../images/5.4/vertical-icon-all.png?v=1) no-repeat;
            background-position: 0 0;
            background-size: 24px 62px
        }

        .coupon_unit.blue .condi_msg,.coupon_unit.blue .down,.coupon_unit.blue .expeNum,.coupon_unit.blue .up {
            color: #5092d4
        }

        .coupon_unit.blue .condi_msg,.coupon_unit.blue .down {
            background-color: #dcf4ff
        }

        .coupon_unit.blue em.left_m {
            background: url(../images/5.4/right-icon-all.png?v=1) no-repeat;
            background-size: 20px 9px;
            background-position: 0 0
        }

        .coupon_unit.red {
            border: 1px solid #f57c7c;
            border-width: 1px 1px 1px 1px;
            background-color: #fff;
            -webkit-border-image: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAJCAMAAADXT/YiAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpCNDNFOTMxRDM2OEYxMUU1QTYxQ0YwQzk0RUY1QTFDQyIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpCNDNFOTMxQzM2OEYxMUU1QTYxQ0YwQzk0RUY1QTFDQyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpEMDEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pnv3VGAAAAAJUExURfV8fP///////38uHTYAAAADdFJOU///ANfKDUEAAAAgSURBVHjaYmBigABGEAYBEIcJzGIigQXXywg1jwkgwAALEwA+xZ9YSQAAAABJRU5ErkJggg==) 2 round;-moz-border-image: url(data:image/png;
        base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAJCAMAAADXT/YiAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpCNDNFOTMxRDM2OEYxMUU1QTYxQ0YwQzk0RUY1QTFDQyIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpCNDNFOTMxQzM2OEYxMUU1QTYxQ0YwQzk0RUY1QTFDQyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpEMDEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pnv3VGAAAAAJUExURfV8fP///////38uHTYAAAADdFJOU///ANfKDUEAAAAgSURBVHjaYmBigABGEAYBEIcJzGIigQXXywg1jwkgwAALEwA+xZ9YSQAAAABJRU5ErkJggg==) 2 round;-ms-border-image: url(data:image/png;
        base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAJCAMAAADXT/YiAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpCNDNFOTMxRDM2OEYxMUU1QTYxQ0YwQzk0RUY1QTFDQyIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpCNDNFOTMxQzM2OEYxMUU1QTYxQ0YwQzk0RUY1QTFDQyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpEMDEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pnv3VGAAAAAJUExURfV8fP///////38uHTYAAAADdFJOU///ANfKDUEAAAAgSURBVHjaYmBigABGEAYBEIcJzGIigQXXywg1jwkgwAALEwA+xZ9YSQAAAABJRU5ErkJggg==) 2 round;-o-border-image: url(data:image/png;
        base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAJCAMAAADXT/YiAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpCNDNFOTMxRDM2OEYxMUU1QTYxQ0YwQzk0RUY1QTFDQyIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpCNDNFOTMxQzM2OEYxMUU1QTYxQ0YwQzk0RUY1QTFDQyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpEMDEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pnv3VGAAAAAJUExURfV8fP///////38uHTYAAAADdFJOU///ANfKDUEAAAAgSURBVHjaYmBigABGEAYBEIcJzGIigQXXywg1jwkgwAALEwA+xZ9YSQAAAABJRU5ErkJggg==) 2 round;border-image: url(data:image/png;
        base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAJCAMAAADXT/YiAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpCNDNFOTMxRDM2OEYxMUU1QTYxQ0YwQzk0RUY1QTFDQyIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpCNDNFOTMxQzM2OEYxMUU1QTYxQ0YwQzk0RUY1QTFDQyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpEMDEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pnv3VGAAAAAJUExURfV8fP///////38uHTYAAAADdFJOU///ANfKDUEAAAAgSURBVHjaYmBigABGEAYBEIcJzGIigQXXywg1jwkgwAALEwA+xZ9YSQAAAABJRU5ErkJggg==) 2 round
        }

        .coupon_unit.red .coupon_icon {
            background: url(../images/5.4/vertical-icon-all.png?v=1) no-repeat;
            background-position: -18px 0;
            background-size: 24px 62px
        }

        .coupon_unit.red .condi_msg,.coupon_unit.red .down,.coupon_unit.red .expeNum,.coupon_unit.red .up {
            color: #fe7c7d
        }

        .coupon_unit.red .condi_msg,.coupon_unit.red .down {
            background-color: #ffebeb
        }

        .coupon_unit.red em.left_m {
            background: url(../images/5.4/right-icon-all.png?v=1) no-repeat;
            background-size: 20px 9px;
            background-position: -15px 0
        }

        .coupon-slider .coupons-container em.left_m,.coupons-container .coupon_unit .coupon_icon {
            background-color: #fff
        }

        .coupon_unit.green {
            border: 1px solid #60c4a2;
            border-width: 1px 1px 1px 1px;
            background-color: #fff;
            -webkit-border-image: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAJCAYAAADgkQYQAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA3ZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo5Njk4ZTYwOC02OTFiLTljNDktODA4My0zMjRjMjM3MjY2ZjYiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6OUZDMjRGM0I2MTk4MTFFNUE1NEVBNEU5RjAyRUFENDUiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6OUZDMjRGM0E2MTk4MTFFNUE1NEVBNEU5RjAyRUFENDUiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6OTY5OGU2MDgtNjkxYi05YzQ5LTgwODMtMzI0YzIzNzI2NmY2IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjk2OThlNjA4LTY5MWItOWM0OS04MDgzLTMyNGMyMzcyNjZmNiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PvuytDAAAABDSURBVHjaYvz//z8DIcAIU5R4dDGG6vnWsYxgBkhRwpFF/4EYzEbGIHEQzcRABKCzIhaYL0F+APoQw/coQYAPAAQYAHwvKQ9CNtGiAAAAAElFTkSuQmCC) 2 round;-moz-border-image: url(data:image/png;
        base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAJCAYAAADgkQYQAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA3ZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo5Njk4ZTYwOC02OTFiLTljNDktODA4My0zMjRjMjM3MjY2ZjYiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6OUZDMjRGM0I2MTk4MTFFNUE1NEVBNEU5RjAyRUFENDUiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6OUZDMjRGM0E2MTk4MTFFNUE1NEVBNEU5RjAyRUFENDUiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6OTY5OGU2MDgtNjkxYi05YzQ5LTgwODMtMzI0YzIzNzI2NmY2IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjk2OThlNjA4LTY5MWItOWM0OS04MDgzLTMyNGMyMzcyNjZmNiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PvuytDAAAABDSURBVHjaYvz//z8DIcAIU5R4dDGG6vnWsYxgBkhRwpFF/4EYzEbGIHEQzcRABKCzIhaYL0F+APoQw/coQYAPAAQYAHwvKQ9CNtGiAAAAAElFTkSuQmCC) 2 round;-ms-border-image: url(data:image/png;
        base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAJCAYAAADgkQYQAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA3ZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo5Njk4ZTYwOC02OTFiLTljNDktODA4My0zMjRjMjM3MjY2ZjYiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6OUZDMjRGM0I2MTk4MTFFNUE1NEVBNEU5RjAyRUFENDUiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6OUZDMjRGM0E2MTk4MTFFNUE1NEVBNEU5RjAyRUFENDUiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6OTY5OGU2MDgtNjkxYi05YzQ5LTgwODMtMzI0YzIzNzI2NmY2IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjk2OThlNjA4LTY5MWItOWM0OS04MDgzLTMyNGMyMzcyNjZmNiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PvuytDAAAABDSURBVHjaYvz//z8DIcAIU5R4dDGG6vnWsYxgBkhRwpFF/4EYzEbGIHEQzcRABKCzIhaYL0F+APoQw/coQYAPAAQYAHwvKQ9CNtGiAAAAAElFTkSuQmCC) 2 round;-o-border-image: url(data:image/png;
        base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAJCAYAAADgkQYQAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA3ZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo5Njk4ZTYwOC02OTFiLTljNDktODA4My0zMjRjMjM3MjY2ZjYiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6OUZDMjRGM0I2MTk4MTFFNUE1NEVBNEU5RjAyRUFENDUiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6OUZDMjRGM0E2MTk4MTFFNUE1NEVBNEU5RjAyRUFENDUiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6OTY5OGU2MDgtNjkxYi05YzQ5LTgwODMtMzI0YzIzNzI2NmY2IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjk2OThlNjA4LTY5MWItOWM0OS04MDgzLTMyNGMyMzcyNjZmNiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PvuytDAAAABDSURBVHjaYvz//z8DIcAIU5R4dDGG6vnWsYxgBkhRwpFF/4EYzEbGIHEQzcRABKCzIhaYL0F+APoQw/coQYAPAAQYAHwvKQ9CNtGiAAAAAElFTkSuQmCC) 2 round;border-image: url(data:image/png;
        base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAJCAYAAADgkQYQAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA3ZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo5Njk4ZTYwOC02OTFiLTljNDktODA4My0zMjRjMjM3MjY2ZjYiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6OUZDMjRGM0I2MTk4MTFFNUE1NEVBNEU5RjAyRUFENDUiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6OUZDMjRGM0E2MTk4MTFFNUE1NEVBNEU5RjAyRUFENDUiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6OTY5OGU2MDgtNjkxYi05YzQ5LTgwODMtMzI0YzIzNzI2NmY2IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjk2OThlNjA4LTY5MWItOWM0OS04MDgzLTMyNGMyMzcyNjZmNiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PvuytDAAAABDSURBVHjaYvz//z8DIcAIU5R4dDGG6vnWsYxgBkhRwpFF/4EYzEbGIHEQzcRABKCzIhaYL0F+APoQw/coQYAPAAQYAHwvKQ9CNtGiAAAAAElFTkSuQmCC) 2 round
        }

        .coupon_unit.green .coupon_icon {
            background: url(../images/5.4/vertical-icon-all.png?v=1) no-repeat;
            background-position: -6px 0;
            background-size: 24px 62px
        }

        .coupon_unit.green .condi_msg,.coupon_unit.green .down,.coupon_unit.green .expeNum,.coupon_unit.green .up {
            color: #60c4a2
        }

        .coupon_unit.green .condi_msg,.coupon_unit.green .down {
            background-color: #e6f8f2
        }

        .coupon_unit.green em.left_m {
            background: url(../images/5.4/right-icon-all.png?v=1) no-repeat;
            background-size: 20px 9px;
            background-position: -10px 0
        }

        .coupon_unit.grep {
            border: 1px solid #bfbfbf;
            border-width: 1px 1px 1px 1px;
            background-color: #fff;
            -webkit-border-image: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAJCAMAAADXT/YiAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpFODJCQTBGRTM2OEYxMUU1QUU2NkRGQzNCNEY2OEVBOCIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpFODJCQTBGRDM2OEYxMUU1QUU2NkRGQzNCNEY2OEVBOCIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpEMDEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pved8AQAAAAJUExURb+/v////////z1OUW0AAAADdFJOU///ANfKDUEAAAAgSURBVHjaYmBigABGEAYBEIcJzGIigQXXywg1jwkgwAALEwA+xZ9YSQAAAABJRU5ErkJggg==) 2 round;-moz-border-image: url(data:image/png;
        base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAJCAMAAADXT/YiAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpFODJCQTBGRTM2OEYxMUU1QUU2NkRGQzNCNEY2OEVBOCIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpFODJCQTBGRDM2OEYxMUU1QUU2NkRGQzNCNEY2OEVBOCIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpEMDEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pved8AQAAAAJUExURb+/v////////z1OUW0AAAADdFJOU///ANfKDUEAAAAgSURBVHjaYmBigABGEAYBEIcJzGIigQXXywg1jwkgwAALEwA+xZ9YSQAAAABJRU5ErkJggg==) 2 round;-ms-border-image: url(data:image/png;
        base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAJCAMAAADXT/YiAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpFODJCQTBGRTM2OEYxMUU1QUU2NkRGQzNCNEY2OEVBOCIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpFODJCQTBGRDM2OEYxMUU1QUU2NkRGQzNCNEY2OEVBOCIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpEMDEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pved8AQAAAAJUExURb+/v////////z1OUW0AAAADdFJOU///ANfKDUEAAAAgSURBVHjaYmBigABGEAYBEIcJzGIigQXXywg1jwkgwAALEwA+xZ9YSQAAAABJRU5ErkJggg==) 2 round;-o-border-image: url(data:image/png;
        base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAJCAMAAADXT/YiAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpFODJCQTBGRTM2OEYxMUU1QUU2NkRGQzNCNEY2OEVBOCIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpFODJCQTBGRDM2OEYxMUU1QUU2NkRGQzNCNEY2OEVBOCIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpEMDEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pved8AQAAAAJUExURb+/v////////z1OUW0AAAADdFJOU///ANfKDUEAAAAgSURBVHjaYmBigABGEAYBEIcJzGIigQXXywg1jwkgwAALEwA+xZ9YSQAAAABJRU5ErkJggg==) 2 round;border-image: url(data:image/png;
        base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAJCAMAAADXT/YiAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2RpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpFODJCQTBGRTM2OEYxMUU1QUU2NkRGQzNCNEY2OEVBOCIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpFODJCQTBGRDM2OEYxMUU1QUU2NkRGQzNCNEY2OEVBOCIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpEMDEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpDRjEyQTQ4NjZFMzZFNTExOTg2N0JDMjA0NUNCQzdBMSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pved8AQAAAAJUExURb+/v////////z1OUW0AAAADdFJOU///ANfKDUEAAAAgSURBVHjaYmBigABGEAYBEIcJzGIigQXXywg1jwkgwAALEwA+xZ9YSQAAAABJRU5ErkJggg==) 2 round
        }

        .coupon_unit.grep .coupon_icon {
            background: url(../images/5.4/vertical-icon-all.png?v=1) no-repeat;
            background-position: -12px 0;
            background-size: 24px 62px
        }

        .coupon_unit.grep .expeNum,.coupon_unit.grep .up {
            color: #d9d9d9
        }

        .coupon_unit.grep .down {
            color: #fff
        }

        .coupon_unit.grep .condi_msg {
            color: #fff
        }

        .coupon_unit.grep .condi_msg,.coupon_unit.grep .down {
            background-color: #d9d9d9
        }

        .coupon_unit.grep em.left_m {
            background: url(../images/5.4/right-icon-all.png?v=1) no-repeat;
            background-size: 20px 9px;
            background-position: -5px 0
        }

        .coupon-slider {
            margin-left: 27px;
            white-space: nowrap;
            overflow: hidden;
            margin-top: 10px
        }

        .pop-floor {
            width: 100%;
            height: 100%;
            position: absolute;
            top: 0;
            left: 0;
            vertical-align: middle;
            background: rgba(0,0,0,.7);
            visibility: hidden;
            z-index: 8889
        }

        .captchas-tip {
            border-radius: 10px;
            border: 1px solid #313131;
            background-color: #f8f8f8;
            padding: 19px 12px 16px;
            width: 272px;
            position: absolute;
            overflow: hidden;
            z-index: 8890;
            left: 50%;
            margin-left: -136px;
            box-sizing: border-box
        }

        .tip-title {
            font-size: 17px;
            color: #4d4d4d;
            text-align: center;
            line-height: 20px
        }

        .tip-coupon {
            display: block;
            text-align: center;
            height: 21px
        }

        .tip-coupon-info {
            padding: 11px 0;
            font-size: 13px;
            color: #4d4d4d;
            line-height: 15px;
            height: auto
        }

        .captchas-info {
            border-bottom: 1px solid #dcdcdc;
            box-shadow: 0 1px 1px #fff;
            -webkit-box-shadow: 0 1px 1px #fff;
            opacity: .8;
            overflow: hidden
        }

        .captchas-input {
            font-size: 15px;
            padding: 13px 12px;
            border-radius: 5px;
            border: 1px solid #d7d7d7;
            width: 158px;
            height: 43px;
            outline: 0;
            -webkit-appearance: none;
            box-sizing: border-box
        }

        .captchas-img {
            width: 30%;
            float: right;
            height: 43px
        }

        .tip-warn {
            color: #f15353;
            line-height: 20px;
            font-size: 13px;
            padding: 1px 0;
            min-height: 20px
        }

        .captchas-btns {
            padding-top: 16px;
            overflow: hidden
        }

        .tip-btn {
            text-decoration: none;
            text-align: center;
            font-size: 15px;
            border-radius: 5px;
            width: 117px;
            height: 40px;
            box-sizing: border-box;
            line-height: 38px
        }

        .btn-cancel {
            background-color: #fff;
            color: #4d4d4d;
            border: 1px solid #b7b7b7;
            float: left
        }

        .btn-ensure {
            background-color: #f25454;
            color: #fff;
            border: 1px solid #fc5858;
            float: right
        }

        .btn-ensure:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .get-captchas {
            text-decoration: none;
            border: 1px solid #000;
            color: #000
        }

        .fix-one-page {
            position: relative;
            height: 100%
        }

        .x-already {
            position: absolute;
            top: 0;
            right: 7px;
            width: 45px;
            height: 33px;
            background: url(../images/5.4/icon-already.png?v=1) no-repeat center/contain
        }

        .prod-promotion {
            padding-top: 15px;
            padding-bottom: 11px
        }

        .prod-promotion-container {
            position: relative
        }

        .prod-promotion-container .part-note-msg {
            position: absolute
        }

        .promotion-content {
            margin: 0 35px 0 34px
        }

        .promotion-item-suit {
            font-size: 13px;
            color: #848689;
            margin-top: 3px
        }

        .promotion-item .promotion-icon {
            font-style: normal;
            background-color: #f15353;
            font-size: 12px;
            line-height: 14px;
            color: #fff;
            padding: 1px 4px;
            display: inline-block
        }

        .promotion-item .promotion-item-text {
            font-size: 13px;
            color: #252525;
            line-height: 15px;
            vertical-align: top
        }

        .promotion-item.promotion-info {
            font-size: 13px;
            line-height: 15px;
            color: #252525;
            vertical-align: top
        }

        .promotion-item a {
            margin-left: 5px
        }

        .promotion-up .promotion-icon {
            vertical-align: top;
            margin-top: 2px
        }

        .promotion-up .label-icon-div {
            vertical-align: top;
            margin-top: 3px
        }

        .promotion-up .promotion-item-text {
            display: inline-block;
            margin-right: -28px;
            line-height: 20px;
            width: 79%;
            vertical-align: top;
            text-align: justify
        }

        .promotion-up .promotion-suit-container {
            display: block
        }

        .promotion-up .detail-import {
            display: block
        }

        .promotion-up .icon-arrow-right-group {
            background-position: 5px -16px;
            top: 2px;
            right: -28px
        }

        .promotion-up .icon-arrow-position {
            background-position: 3px -5px;
            top: 0
        }

        .promotion-up .promotion-item-suit {
            display: inline-block;
            position: absolute;
            right: -12px;
            top: 0
        }

        .promotion-up .promotion-item {
            margin-bottom: 10px
        }

        .promotion-item-text i {
            font-size: 13px;
            font-style: normal;
            color: #f23030
        }

        .promotion-down .label-icon-div {
            vertical-align: top
        }

        .promotion-down .promotion-info {
            display: none
        }

        .promotion-down .promotion-content {
            max-height: 62px;
            overflow: hidden
        }

        .promotion-down .promotion-item {
            margin-bottom: 8px;
            line-height: 15px;
            text-overflow: ellipsis;
            white-space: nowrap;
            overflow: hidden;
            vertical-align: top
        }

        .promotion-down .item-display-inline {
            display: inline-block;
            font-size: 0;
            margin-bottom: 0;
            line-height: 15px
        }

        .promotion-down .promotion-item-link {
            display: none
        }

        .promotion-down .icon-arrow-right {
            display: none
        }

        .promotion-down .icon-arrow-position {
            background-position: 3px 6px;
            top: 0
        }

        .promotion-down .promotion-item-suit {
            display: none
        }

        .promotion-down .icon-arrow-right-group {
            display: none
        }

        .promotion-item-link {
            display: none
        }

        .promotion-suit-container {
            overflow: hidden;
            position: relative;
            margin-bottom: -8px;
            margin-top: 10px;
            margin-right: -30px;
            font-size: 0
        }

        .suit-list {
            margin: 0;
            padding: 0;
            list-style-type: none;
            margin-left: -40px;
            margin-right: -40px
        }

        .suit-list li {
            margin: 0;
            padding: 0;
            list-style-type: none
        }

        .suit-list .suit-item {
            float: left;
            margin-left: 40px
        }

        .suit-item .suit-item-title {
            font-size: 11px;
            color: #848689
        }

        .suit-item .suit-item-content {
            margin-top: 10px;
            overflow: hidden;
            display: table
        }

        .suit-item-content .suit-item-pic {
            width: 63px;
            height: 63px;
            border-radius: 5px;
            overflow: hidden;
            border: 1px solid #d8d8d8;
            display: table-cell
        }

        .suit-item-content .suit-item-add {
            position: relative;
            top: 24px;
            color: #848689;
            font-size: 10px;
            padding: 0 4px;
            vertical-align: top;
            display: inline-block;
            width: 7px
        }

        .suit-item-pic img {
            width: 63px;
            height: 63px
        }

        .spec-desc .icon-arrow {
            top: 0
        }

        .spec-desc .icon-popups {
            top: 7px
        }

        .pro-color {
            margin-top: 13px
        }

        .pro-buy-style,.pro-count,.pro-size,.pro-spec {
            margin-top: 3px
        }

        .pro-count {
            height: 26px
        }

        .quantity-wrapper {
            display: inline-block;
            height: 26px;
            border-radius: 5px;
            margin-left: 8px;
            float: left;
            line-height: 26px
        }

        .quantity-decrease {
            -webkit-border-radius: 2px 0 0 2px
        }

        .quantity-decrease em {
            no-repeat;
            background-size: 100px 100px;
            background-position: -32px -85px;
            height: 12px;
            width: 12px;
            display: block;
            margin: 6px
        }

        .quantity-decrease.limited em {
            background-position: -45px -85px
        }

        .quantity {
            color: #232326;
            border: solid #cbcbcb;
            border-width: 1px 0 1px 0;
            height: 24px;
            line-height: 24px;
            width: 34px;
            border-radius: 0;
            -webkit-appearance: none;
            box-sizing: border-box
        }

        .quantity-increase {
            -webkit-border-radius: 0 2px 2px 0
        }

        .quantity-increase em {
            no-repeat;
            background-position: -32px -67px;
            background-size: 100px 100px;
            height: 12px;
            width: 12px;
            display: block;
            margin: 6px
        }

        .quantity-increase.limited em {
            background-position: -45px -67px
        }

        .quantity-decrease,.quantity-increase {
            background: #fff;
            border: 1px solid #cbcbcb;
            color: #232326;
            display: block;
            height: 24px;
            line-height: 24px;
            width: 26px;
            overflow: hidden;
            text-indent: -200px;
            box-sizing: border-box
        }

        .quantity,.quantity-decrease,.quantity-increase {
            float: left;
            font-size: 15px;
            text-align: center
        }

        .spec-desc .base-txt,.spec-desc .part-note-msg {
            line-height: 18px
        }

        .pro-color .part-note-msg,.pro-size .part-note-msg {
            line-height: 23px
        }

        .spec-desc .base-txt {
            margin-right: 30px
        }

        .pro-count .part-note-msg {
            line-height: 26px
        }

        .pro-count:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .srv-item {
            display: inline-block
        }

        .show-msg-detail .srv-item {
            display: block
        }

        .pro-color p,.pro-size p,.pro-spec p {
            margin-left: 34px;
            overflow: hidden
        }

        .pro-buy-style {
            font-size: 0;
            position: relative
        }

        .pro-buy-style p {
            margin-left: 34px
        }

        .pro-buy-style p:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .pro-buy-style p a,.pro-color p a,.pro-size p a,.pro-spec p a {
            height: 27px;
            border: 1px solid #bfbfbf;
            border-radius: 4px;
            line-height: 25px;
            font-size: 13px;
            color: #232326;
            float: left;
            margin-right: 10px;
            padding-left: 20px;
            padding-right: 20px;
            margin-bottom: 10px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            min-width: 69px;
            max-width: 98%;
            box-sizing: border-box;
            text-align: center
        }

        .pro-buy-style p a:active,.pro-color p a:active,.pro-size p a:active,.pro-spec p a:active {
            background-color: #e7e7e7
        }

        .pro-buy-style p a.selected,.pro-color p a.selected,.pro-size p a.selected,.pro-spec p a.selected {
            border: 1px solid #f23030;
            color: #f23030
        }

        .pro-buy-style p a.no-goods,.pro-color p a.no-goods,.pro-size p a.no-goods,.pro-spec p a.no-goods {
            border: 1px dashed #bfbfbf;
            color: #232326
        }

        .list-yb:after,.pro-buy-style:after,.pro-color:after,.pro-size:after,.pro-spec:after {
            visibility: hidden;
            display: block;
            font-size: 0;
            content: " ";
            clear: both;
            height: 0
        }

        .no-change-wrapper {
            display: block;
            position: relative;
            float: left;
            overflow: visible;
            max-width: 98%
        }

        .no-change-wrapper .no-change {
            background-color: #232326;
            font-size: 8px;
            line-height: 10px;
            color: #fff;
            width: 24px;
            height: 10px;
            display: inline-block;
            border-radius: 10px;
            padding: 0 3px;
            position: absolute;
            right: 7px;
            top: -5px
        }

        .contract-reply {
            margin: 0 10px 12px 34px
        }

        .contract-reply a span {
            color: #f15353;
            font-size: 12px;
            vertical-align: middle;
            line-height: 12px
        }

        .question-icon {
            display: inline-block;
            width: 12px;
            height: 12px;
            no-repeat 0 0;
            background-size: 100px 100px;
            background-position: -28px 0;
            margin-right: 5px;
            margin-left: 3px;
            vertical-align: top
        }

        .prod-spec .multy-spec {
            width: 26px;
            white-space: normal;
            line-height: 15px
        }

        .lowestbuy-tip {
            display: inline-block;
            margin-left: 12px;
            font-size: 13px;
            color: #f23030;
            line-height: 26px
        }

        #eventArea {
            position: relative;
            height: 44px;
            line-height: 44px
        }

        .box-i-tit {
            color: #686868;
            font-size: 12px;
            font-weight: 400
        }

        .extend-items dd {
            margin-left: 34px
        }

        .extend-items dd .fore01 {
            text-align: left;
            color: #686868;
            line-height: 20px;
            font-size: 12px;
            float: left
        }

        .icon-check {
            width: 14px;
            height: 14px;
            border: 1px solid #9a9ca5;
            border-radius: 10px;
            display: inline-block;
            font-style: normal;
            margin-right: 8px;
            float: left;
            line-height: 20px;
            margin-top: 2px
        }

        .icon-check.seled {
            width: 14px;
            height: 14px;
            no-repeat;
            background-size: 88px 88px;
            background-position: -59px -15px;
            border: 1px solid #f03338
        }

        section.extend-items {
            padding-right: 15px;
            display: none;
            transition: 2s all ease-out
        }

        .extend-items dd .fore02 {
            display: block;
            text-align: left;
            color: #686868;
            font-size: 12px;
            margin-left: 126px;
            line-height: 20px
        }

        .extend-items dd .fore01 span {
            line-height: 20px;
            display: inline-block
        }

        .extend-items dl dd {
            margin-bottom: 5px
        }

        .extend-items dl:last-child {
            margin-bottom: 15px
        }

        .extend-items dt {
            margin-bottom: 5px;
            margin-top: 10px
        }

        .extend-items dd:after,.send_loc:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .list-yb {
            color: #000;
            line-height: 22px;
            padding-right: 23px
        }

        .list-yb.kleft {
            padding-left: 34px;
            font-size: 13px;
            margin-bottom: 10px;
            margin-top: -10px
        }

        .list-yb .list-item {
            overflow: hidden
        }

        .list-yb .fore02 {
            margin-left: 70%
        }

        .list-yb .fore01 {
            float: left;
            width: 65%;
            margin-right: 5%
        }

        .list-yb .fore01,.list-yb .fore02 {
            display: block
        }

        .loc-floor {
            padding-bottom: 9px
        }

        .send-to {
            padding-top: 14px
        }

        .send_loc {
            position: relative;
            font-size: 0;
            margin-left: 34px;
            overflow: hidden;
            word-break: break-all;
            max-width: 88%;
            margin-right: 10px
        }

        .send_loc span {
            font-size: 13px;
            line-height: 16px;
            margin-right: 8px;
            color: #252525
        }

        .icon-location {
            display: inline-block;
            width: 12px;
            height: 14px;
            no-repeat 0 0;
            background-size: 100px 100px;
            background-position: -14px 0;
            margin-left: 4px
        }

        .stockStatus {
            color: #e9321f;
            font-size: 13px;
            margin: 7px 10px 0 34px;
            overflow: hidden;
            word-break: break-all
        }

        .jx-stockStatus {
            color: #c09947
        }

        .isExist {
            float: left;
            line-height: 19px;
            padding-right: 5px;
            text-align: justify
        }

        .send-time {
            display: none;
            color: #81838e;
            line-height: 20px
        }

        .list_content_mask {
            width: 100%;
            height: 100%;
            min-height: 100%;
            position: absolute;
            left: 0;
            top: 0;
            background: rgba(0,0,0,.5);
            z-index: 11110
        }

        .sidebar-content {
            position: absolute;
            top: 0;
            left: 100%;
            width: 87.5%;
            height: 100%;
            overflow: scroll;
            background: rgba(0,0,0,.7);
            z-index: 105
        }

        .sidebar-header {
            position: absolute;
            top: 0;
            right: 0;
            z-index: 111;
            background: #fff;
            height: 44px;
            border-width: 0 0 1px 0;
            border-bottom: 1px solid #d7d7d7;
            -webkit-border-image: url(data:image/gif;
            base64,R0lGODlhBAAEAIABAL+/v////yH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4zLWMwMTEgNjYuMTQ1NjYxLCAyMDEyLzAyLzA2LTE0OjU2OjI3ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjAxODAxMTc0MDcyMDY4MTE4MDgzOEM2RDA0Mzc2M0VFIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkRDNUJDQkY0OTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkRDNUJDQkYzOTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzYgKE1hY2ludG9zaCkiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpGNEEwMDBGQjIxMjA2ODExODA4M0YzNThFREFFRUMzMyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDowMTgwMTE3NDA3MjA2ODExODA4MzhDNkQwNDM3NjNFRSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgH//v38+/r5+Pf29fTz8vHw7+7t7Ovq6ejn5uXk4+Lh4N/e3dzb2tnY19bV1NPS0dDPzs3My8rJyMfGxcTDwsHAv769vLu6ubi3trW0s7KxsK+urayrqqmop6alpKOioaCfnp2cm5qZmJeWlZSTkpGQj46NjIuKiYiHhoWEg4KBgH9+fXx7enl4d3Z1dHNycXBvbm1sa2ppaGdmZWRjYmFgX15dXFtaWVhXVlVUU1JRUE9OTUxLSklIR0ZFRENCQUA/Pj08Ozo5ODc2NTQzMjEwLy4tLCsqKSgnJiUkIyIhIB8eHRwbGhkYFxYVFBMSERAPDg0MCwoJCAcGBQQDAgEAACH5BAEAAAEALAAAAAAEAAQAAAIFhBGpeVAAOw==) 2 0 stretch;-o-border-image: url(data:image/gif;
        base64,R0lGODlhBAAEAIABAL+/v////yH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4zLWMwMTEgNjYuMTQ1NjYxLCAyMDEyLzAyLzA2LTE0OjU2OjI3ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjAxODAxMTc0MDcyMDY4MTE4MDgzOEM2RDA0Mzc2M0VFIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkRDNUJDQkY0OTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkRDNUJDQkYzOTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzYgKE1hY2ludG9zaCkiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpGNEEwMDBGQjIxMjA2ODExODA4M0YzNThFREFFRUMzMyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDowMTgwMTE3NDA3MjA2ODExODA4MzhDNkQwNDM3NjNFRSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgH//v38+/r5+Pf29fTz8vHw7+7t7Ovq6ejn5uXk4+Lh4N/e3dzb2tnY19bV1NPS0dDPzs3My8rJyMfGxcTDwsHAv769vLu6ubi3trW0s7KxsK+urayrqqmop6alpKOioaCfnp2cm5qZmJeWlZSTkpGQj46NjIuKiYiHhoWEg4KBgH9+fXx7enl4d3Z1dHNycXBvbm1sa2ppaGdmZWRjYmFgX15dXFtaWVhXVlVUU1JRUE9OTUxLSklIR0ZFRENCQUA/Pj08Ozo5ODc2NTQzMjEwLy4tLCsqKSgnJiUkIyIhIB8eHRwbGhkYFxYVFBMSERAPDg0MCwoJCAcGBQQDAgEAACH5BAEAAAEALAAAAAAEAAQAAAIFhBGpeVAAOw==) 2 0 round;border-image: url(data:image/gif;
        base64,R0lGODlhBAAEAIABAL+/v////yH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4zLWMwMTEgNjYuMTQ1NjYxLCAyMDEyLzAyLzA2LTE0OjU2OjI3ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjAxODAxMTc0MDcyMDY4MTE4MDgzOEM2RDA0Mzc2M0VFIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkRDNUJDQkY0OTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkRDNUJDQkYzOTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzYgKE1hY2ludG9zaCkiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpGNEEwMDBGQjIxMjA2ODExODA4M0YzNThFREFFRUMzMyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDowMTgwMTE3NDA3MjA2ODExODA4MzhDNkQwNDM3NjNFRSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgH//v38+/r5+Pf29fTz8vHw7+7t7Ovq6ejn5uXk4+Lh4N/e3dzb2tnY19bV1NPS0dDPzs3My8rJyMfGxcTDwsHAv769vLu6ubi3trW0s7KxsK+urayrqqmop6alpKOioaCfnp2cm5qZmJeWlZSTkpGQj46NjIuKiYiHhoWEg4KBgH9+fXx7enl4d3Z1dHNycXBvbm1sa2ppaGdmZWRjYmFgX15dXFtaWVhXVlVUU1JRUE9OTUxLSklIR0ZFRENCQUA/Pj08Ozo5ODc2NTQzMjEwLy4tLCsqKSgnJiUkIyIhIB8eHRwbGhkYFxYVFBMSERAPDg0MCwoJCAcGBQQDAgEAACH5BAEAAAEALAAAAAAEAAQAAAIFhBGpeVAAOw==) 2 0 round;width: 100%
        }

        .sidebar-header.region-title {
            text-align: center
        }

        .sidebar-header span.region-title-back {
            margin-left: 15px;
            padding: 12px 0;
            float: left;
            display: inline-block;
            width: 12px;
            height: 20px
        }

        .region-title-name {
            display: inline-block;
            font-size: 16px;
            line-height: 16px;
            padding: 14px 7px;
            margin-right: 27px
        }

        .sidebar-items-container {
            height: 100%;
            overflow: auto;
            width: 100%;
            background: #fff
        }

        .sidebar-items-container.region-list-group {
            overflow: hidden
        }

        .spacer44 {
            height: 44px
        }

        .region-wrapper {
            width: 100%;
            height: 100%;
            min-height: 100%;
            overflow-y: auto
        }

        .region-list-group ul {
            display: none;
            width: 100%;
            overflow-x: hidden;
            float: left
        }

        .region-list-group ul.cur {
            display: block
        }

        .region-list {
            padding-bottom: 44px
        }

        .sidebar-list,.sidebar-list ul {
            border-width: 1px 0 0 0;
            border-top: 1px solid #bfbfbf;
            -webkit-border-image: url(data:image/gif;
            base64,R0lGODlhBAAEAIABAL+/v////yH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4zLWMwMTEgNjYuMTQ1NjYxLCAyMDEyLzAyLzA2LTE0OjU2OjI3ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjAxODAxMTc0MDcyMDY4MTE4MDgzOEM2RDA0Mzc2M0VFIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkRDNUJDQkY0OTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkRDNUJDQkYzOTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzYgKE1hY2ludG9zaCkiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpGNEEwMDBGQjIxMjA2ODExODA4M0YzNThFREFFRUMzMyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDowMTgwMTE3NDA3MjA2ODExODA4MzhDNkQwNDM3NjNFRSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgH//v38+/r5+Pf29fTz8vHw7+7t7Ovq6ejn5uXk4+Lh4N/e3dzb2tnY19bV1NPS0dDPzs3My8rJyMfGxcTDwsHAv769vLu6ubi3trW0s7KxsK+urayrqqmop6alpKOioaCfnp2cm5qZmJeWlZSTkpGQj46NjIuKiYiHhoWEg4KBgH9+fXx7enl4d3Z1dHNycXBvbm1sa2ppaGdmZWRjYmFgX15dXFtaWVhXVlVUU1JRUE9OTUxLSklIR0ZFRENCQUA/Pj08Ozo5ODc2NTQzMjEwLy4tLCsqKSgnJiUkIyIhIB8eHRwbGhkYFxYVFBMSERAPDg0MCwoJCAcGBQQDAgEAACH5BAEAAAEALAAAAAAEAAQAAAIFhBGpeVAAOw==) 2 0 stretch;-o-border-image: url(data:image/gif;
        base64,R0lGODlhBAAEAIABAL+/v////yH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4zLWMwMTEgNjYuMTQ1NjYxLCAyMDEyLzAyLzA2LTE0OjU2OjI3ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjAxODAxMTc0MDcyMDY4MTE4MDgzOEM2RDA0Mzc2M0VFIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkRDNUJDQkY0OTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkRDNUJDQkYzOTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzYgKE1hY2ludG9zaCkiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpGNEEwMDBGQjIxMjA2ODExODA4M0YzNThFREFFRUMzMyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDowMTgwMTE3NDA3MjA2ODExODA4MzhDNkQwNDM3NjNFRSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgH//v38+/r5+Pf29fTz8vHw7+7t7Ovq6ejn5uXk4+Lh4N/e3dzb2tnY19bV1NPS0dDPzs3My8rJyMfGxcTDwsHAv769vLu6ubi3trW0s7KxsK+urayrqqmop6alpKOioaCfnp2cm5qZmJeWlZSTkpGQj46NjIuKiYiHhoWEg4KBgH9+fXx7enl4d3Z1dHNycXBvbm1sa2ppaGdmZWRjYmFgX15dXFtaWVhXVlVUU1JRUE9OTUxLSklIR0ZFRENCQUA/Pj08Ozo5ODc2NTQzMjEwLy4tLCsqKSgnJiUkIyIhIB8eHRwbGhkYFxYVFBMSERAPDg0MCwoJCAcGBQQDAgEAACH5BAEAAAEALAAAAAAEAAQAAAIFhBGpeVAAOw==) 2 0 round;border-image: url(data:image/gif;
        base64,R0lGODlhBAAEAIABAL+/v////yH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4zLWMwMTEgNjYuMTQ1NjYxLCAyMDEyLzAyLzA2LTE0OjU2OjI3ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjAxODAxMTc0MDcyMDY4MTE4MDgzOEM2RDA0Mzc2M0VFIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkRDNUJDQkY0OTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkRDNUJDQkYzOTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzYgKE1hY2ludG9zaCkiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpGNEEwMDBGQjIxMjA2ODExODA4M0YzNThFREFFRUMzMyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDowMTgwMTE3NDA3MjA2ODExODA4MzhDNkQwNDM3NjNFRSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgH//v38+/r5+Pf29fTz8vHw7+7t7Ovq6ejn5uXk4+Lh4N/e3dzb2tnY19bV1NPS0dDPzs3My8rJyMfGxcTDwsHAv769vLu6ubi3trW0s7KxsK+urayrqqmop6alpKOioaCfnp2cm5qZmJeWlZSTkpGQj46NjIuKiYiHhoWEg4KBgH9+fXx7enl4d3Z1dHNycXBvbm1sa2ppaGdmZWRjYmFgX15dXFtaWVhXVlVUU1JRUE9OTUxLSklIR0ZFRENCQUA/Pj08Ozo5ODc2NTQzMjEwLy4tLCsqKSgnJiUkIyIhIB8eHRwbGhkYFxYVFBMSERAPDg0MCwoJCAcGBQQDAgEAACH5BAEAAAEALAAAAAAEAAQAAAIFhBGpeVAAOw==) 2 0 round
        }

        .sidebar-list li {
            border-width: 0 0 1px 0;
            border-bottom: 1px solid #e0e0e0;
            -webkit-border-image: url(data:image/gif;
            base64,R0lGODlhBAAEAIABAL+/v////yH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4zLWMwMTEgNjYuMTQ1NjYxLCAyMDEyLzAyLzA2LTE0OjU2OjI3ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjAxODAxMTc0MDcyMDY4MTE4MDgzOEM2RDA0Mzc2M0VFIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkRDNUJDQkY0OTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkRDNUJDQkYzOTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzYgKE1hY2ludG9zaCkiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpGNEEwMDBGQjIxMjA2ODExODA4M0YzNThFREFFRUMzMyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDowMTgwMTE3NDA3MjA2ODExODA4MzhDNkQwNDM3NjNFRSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgH//v38+/r5+Pf29fTz8vHw7+7t7Ovq6ejn5uXk4+Lh4N/e3dzb2tnY19bV1NPS0dDPzs3My8rJyMfGxcTDwsHAv769vLu6ubi3trW0s7KxsK+urayrqqmop6alpKOioaCfnp2cm5qZmJeWlZSTkpGQj46NjIuKiYiHhoWEg4KBgH9+fXx7enl4d3Z1dHNycXBvbm1sa2ppaGdmZWRjYmFgX15dXFtaWVhXVlVUU1JRUE9OTUxLSklIR0ZFRENCQUA/Pj08Ozo5ODc2NTQzMjEwLy4tLCsqKSgnJiUkIyIhIB8eHRwbGhkYFxYVFBMSERAPDg0MCwoJCAcGBQQDAgEAACH5BAEAAAEALAAAAAAEAAQAAAIFhBGpeVAAOw==) 2 0 stretch;-o-border-image: url(data:image/gif;
        base64,R0lGODlhBAAEAIABAL+/v////yH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4zLWMwMTEgNjYuMTQ1NjYxLCAyMDEyLzAyLzA2LTE0OjU2OjI3ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjAxODAxMTc0MDcyMDY4MTE4MDgzOEM2RDA0Mzc2M0VFIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkRDNUJDQkY0OTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkRDNUJDQkYzOTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzYgKE1hY2ludG9zaCkiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpGNEEwMDBGQjIxMjA2ODExODA4M0YzNThFREFFRUMzMyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDowMTgwMTE3NDA3MjA2ODExODA4MzhDNkQwNDM3NjNFRSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgH//v38+/r5+Pf29fTz8vHw7+7t7Ovq6ejn5uXk4+Lh4N/e3dzb2tnY19bV1NPS0dDPzs3My8rJyMfGxcTDwsHAv769vLu6ubi3trW0s7KxsK+urayrqqmop6alpKOioaCfnp2cm5qZmJeWlZSTkpGQj46NjIuKiYiHhoWEg4KBgH9+fXx7enl4d3Z1dHNycXBvbm1sa2ppaGdmZWRjYmFgX15dXFtaWVhXVlVUU1JRUE9OTUxLSklIR0ZFRENCQUA/Pj08Ozo5ODc2NTQzMjEwLy4tLCsqKSgnJiUkIyIhIB8eHRwbGhkYFxYVFBMSERAPDg0MCwoJCAcGBQQDAgEAACH5BAEAAAEALAAAAAAEAAQAAAIFhBGpeVAAOw==) 2 0 round;border-image: url(data:image/gif;
        base64,R0lGODlhBAAEAIABAL+/v////yH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4zLWMwMTEgNjYuMTQ1NjYxLCAyMDEyLzAyLzA2LTE0OjU2OjI3ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjAxODAxMTc0MDcyMDY4MTE4MDgzOEM2RDA0Mzc2M0VFIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkRDNUJDQkY0OTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkRDNUJDQkYzOTI1MDExRTQ4NEVFRDAyNUY5NUEwMEY3IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzYgKE1hY2ludG9zaCkiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpGNEEwMDBGQjIxMjA2ODExODA4M0YzNThFREFFRUMzMyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDowMTgwMTE3NDA3MjA2ODExODA4MzhDNkQwNDM3NjNFRSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgH//v38+/r5+Pf29fTz8vHw7+7t7Ovq6ejn5uXk4+Lh4N/e3dzb2tnY19bV1NPS0dDPzs3My8rJyMfGxcTDwsHAv769vLu6ubi3trW0s7KxsK+urayrqqmop6alpKOioaCfnp2cm5qZmJeWlZSTkpGQj46NjIuKiYiHhoWEg4KBgH9+fXx7enl4d3Z1dHNycXBvbm1sa2ppaGdmZWRjYmFgX15dXFtaWVhXVlVUU1JRUE9OTUxLSklIR0ZFRENCQUA/Pj08Ozo5ODc2NTQzMjEwLy4tLCsqKSgnJiUkIyIhIB8eHRwbGhkYFxYVFBMSERAPDg0MCwoJCAcGBQQDAgEAACH5BAEAAAEALAAAAAAEAAQAAAIFhBGpeVAAOw==) 2 0 round;font-size: 1px;
            height: 43px
        }

        .sidebar-list a {
            display: block
        }

        .sidebar-list .address-kind {
            height: 26px;
            background-color: #f3f5f7
        }

        .sidebar-list .address-kind span {
            line-height: 26px;
            height: 26px;
            color: #252525;
            font-size: 12px;
            padding-left: 16px
        }

        .sidebar-list li li span,.sidebar-list li span {
            height: 43px;
            line-height: 43px;
            font-size: 13px;
            color: #252525
        }

        .sidebar-categories span {
            display: inline-block;
            width: 83%;
            white-space: nowrap;
            text-overflow: ellipsis;
            overflow: hidden
        }

        .sidebar-categories li.checked .tick {
            background-position: -162px -56px;
            width: 13px;
            height: 9px;
            float: right;
            margin-right: 10px;
            margin-top: 17px
        }

        .sidebar-categories li.checked span {
            color: #f15353
        }

        .region-list span {
            padding-left: 16px
        }

        .region-list-group .loading-mask {
            background: rgba(255,255,255,.1) url(../images/5.4/loading-animation.gif?v=1) no-repeat center center;
            background-size: 19px 19px;
            top: 0;
            width: 100%;
            height: 100%;
            min-height: 100%;
            position: absolute
        }

        .sidebar-move {
            height: 100%;
            overflow: hidden;
            animation: sidebar-body-move .5s 1 ease 0s;
            -webkit-animation: sidebar-body-move .5s 1 ease 0s;
            position: relative;
            left: -87.5%
        }

        .sidebar-move body {
            height: 100%;
            overflow: hidden
        }

        .region-title-back i {
            display: block;
            width: 12px;
            height: 20px;
            background-position: -24px 0
        }

        .sidebar-back {
            animation: sidebar-body-back .2s 1 ease 0s;
            -webkit-animation: sidebar-body-back .2s 1 ease 0s;
            left: 0
        }

        @keyframes sidebar-body-move {
            0% {
                left: 0
            }

            100% {
                left: -87.5%
            }
        }

        @keyframes sidebar-body-back {
            0% {
                left: -87.5%
            }

            100% {
                left: 0
            }
        }

        @-webkit-keyframes sidebar-body-move {
            0% {
                left: 0
            }

            100% {
                left: -87.5%
            }
        }

        @-webkit-keyframes sidebar-body-back {
            0% {
                left: -87.5%
            }

            100% {
                left: 0
            }
        }

        .move-marinr8 span {
            margin-right: 0
        }

        .send_loc_i {
            display: inline-block;
            width: 8px;
            height: 1px
        }

        .msg-notice,.provide-srv {
            margin-top: 13px
        }

        .service-floor {
            font-size: 0;
            padding: 0 10px 9px 10px;
            background-color: #f7f8fa;
            word-wrap: break-word;
            word-break: break-all
        }

        .service-tip-module {
            display: inline-block;
            margin-top: 8px;
            margin-right: 14px;
            font-size: 0
        }

        .service-icon {
        ;
            background-size: 100px 100px;
            width: 12px;
            height: 12px;
            display: inline-block;
            margin-right: 5px
        }

        .support-service-icon {
            background-position: -60px -82px
        }

        .nonsupport-service-icon {
            background-position: -75px -82px
        }

        .service-icon-text {
            font-size: 12px;
            color: #81838e;
            line-height: 12px;
            vertical-align: top
        }

        .service-menu {
            position: fixed;
            bottom: 0;
            left: 0;
            right: 0;
            width: 100%;
            height: 62%;
            background-color: #fff;
            border-top: 1px solid #dadada;
            z-index: 250;
            display: none
        }

        .service-menu-wrapper {
            position: absolute;
            bottom: 44px;
            top: 45px;
            left: 0;
            right: 0;
            padding-left: 10px;
            overflow: scroll
        }

        .service-item-row {
            font-size: 0;
            padding-top: 15px;
            border-bottom: 1px solid #ebebeb
        }

        .service-item-icon {
            display: inline-block;
            width: 15px;
            height: 15px;
            margin-right: 6px;
            vertical-align: top
        }

        .service-item-title {
            font-size: 13px;
            line-height: 15px;
            color: #252525;
            word-wrap: break-word;
            word-break: break-all
        }

        .service-item-text {
            display: block;
            font-size: 13px;
            line-height: 18px;
            color: #848689;
            margin: 3px 10px 12px 21px
        }

        .service-ok-btn {
            width: 100%;
            height: 44px;
            color: #fff;
            line-height: 44px;
            font-size: 15px;
            text-align: center;
            background-color: #f23030
        }

        @media screen and (orientation:portrait) {
            .service-menu {
                height: 62%
            }@	-webkit-keyframes service-menu-move {
        0%{height: 0
        }

        100% {
            height: 62%
        }
        }

        @-moz-keyframes service-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 62%
            }
        }

        @-ms-keyframes service-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 62%
            }
        }

        @-o-keyframes service-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 62%
            }
        }

        @keyframes service-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 62%
            }
        }

        @-webkit-keyframes service-menu-back {
            0% {
                height: 62%
            }

            100% {
                height: 0
            }
        }

        @-moz-keyframes service-menu-back {
            0% {
                height: 62%
            }

            100% {
                height: 0
            }
        }

        @-ms-keyframes service-menu-back {
            0% {
                height: 62%
            }

            100% {
                height: 0
            }
        }

        @-o-keyframes service-menu-back {
            0% {
                height: 62%
            }

            100% {
                height: 0
            }
        }

        @keyframes service-menu-back {
            0% {
                height: 62%
            }

            100% {
                height: 0
            }
        }}

        @media screen and (orientation:landscape) {
            .service-menu {
                height: 62%
            }@	-webkit-keyframes service-menu-move {
        0%{height: 0
        }

        100% {
            height: 62%
        }
        }

        @-moz-keyframes service-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 62%
            }
        }

        @-ms-keyframes service-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 62%
            }
        }

        @-o-keyframes service-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 62%
            }
        }

        @keyframes service-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 62%
            }
        }

        @-webkit-keyframes service-menu-back {
            0% {
                height: 62%
            }

            100% {
                height: 0
            }
        }

        @-moz-keyframes service-menu-back {
            0% {
                height: 62%
            }

            100% {
                height: 0
            }
        }

        @-ms-keyframes service-menu-back {
            0% {
                height: 62%
            }

            100% {
                height: 0
            }
        }

        @-o-keyframes service-menu-back {
            0% {
                height: 62%
            }

            100% {
                height: 0
            }
        }

        @keyframes service-menu-back {
            0% {
                height: 62%
            }

            100% {
                height: 0
            }
        }}

        .service-menu-show {
            -webkit-animation: service-menu-move .5s 1 ease 0s;
            -moz-animation: service-menu-move .5s 1 ease 0s;
            -ms-animation: service-menu-move .5s 1 ease 0s;
            -o-animation: service-menu-move .5s 1 ease 0s;
            animation: service-menu-move .5s 1 ease 0s
        }

        .service-menu-hide {
            -webkit-animation: service-menu-back .5s 1 ease 0s;
            -moz-animation: service-menu-back .5s 1 ease 0s;
            -ms-animation: service-menu-back .5s 1 ease 0s;
            -o-animation: service-menu-back .5s 1 ease 0s;
            animation: service-menu-back .5s 1 ease 0s
        }

        .shop-part {
            width: 100%;
            position: relative;
            overflow: hidden;
            padding: 13px 0 12px 0
        }

        .shop-row1 {
            padding-right: 12px;
            width: 100%;
            overflow: hidden
        }

        .shop-row {
            width: 100%;
            display: inline-block;
            vertical-align: middle;
            float: left
        }

        .shop-row .goods-sign {
            margin-top: 2px;
            vertical-align: top
        }

        .shop-row .score-text {
            font-size: 12px;
            margin-right: 10px;
            vertical-align: top;
            float: right;
            margin-top: 2px
        }

        .shop-row:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .shop-icon {
            width: 80px;
            height: 26px;
            display: inline-block;
            border: 1px solid #d9d9d9
        }

        .shop-icon img {
            width: 80px;
            height: 26px
        }

        .shop-name {
            font-size: 14px;
            color: #222;
            margin: 1px 2px 0 7px;
            display: inline-block;
            vertical-align: top;
            line-height: 16px;
            max-width: 41%;
            text-overflow: ellipsis;
            white-space: nowrap;
            overflow: hidden
        }

        .shop-attr {
            float: left;
            display: inline-block;
            vertical-align: middle
        }

        .shop-jd-self {
            padding-left: 7px;
            padding-right: 7px;
            font-size: 12px;
            border-radius: 3px;
            color: #fff;
            background-color: #f15353;
            text-align: center;
            line-height: 16px
        }

        .shop-diamond {
            width: 62px;
            height: 16px;
            background: url(../images/5.4/diamond.png?v=1) 0 0 no-repeat;
            background-size: 62px 16px
        }

        .icon-right {
            position: absolute;
            top: 14px;
            right: 7px;
            width: 15px;
            height: 15px;
        ;
            background-position: 5px -16px;
            background-repeat: no-repeat;
            background-size: 100px 100px
        }

        .shop-slogan {
            color: #848689;
            font-size: 10px;
            line-height: 10px;
            margin-left: 89px;
            margin-top: -5px
        }

        .shop-describe {
            position: relative;
            width: 100%;
            overflow: hidden;
            float: left;
            padding-top: 9px
        }

        .shop-describe:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .describe-info {
            width: 33.33%;
            font-size: 12px;
            line-height: 12px;
            overflow: hidden;
            text-align: center;
            float: left;
            color: #81838e
        }

        .describe-info .green-font {
            color: #049b36
        }

        .describe-info .red-font {
            color: #f23030
        }

        .shop-tab {
            width: 100%;
            position: relative;
            overflow: hidden;
            float: left;
            padding-top: 8px
        }

        .shop-tab .tabs {
            width: 33.3%;
            float: left;
            text-align: center;
            overflow: hidden;
            position: relative
        }

        .shop-tab .tabs .num {
            font-size: 16px;
            line-height: 16px;
            color: #252525
        }

        .shop-tab .tabs .text {
            margin-top: 3px;
            font-size: 10px;
            line-height: 18px;
            color: #848689
        }

        .shop-tab .tabs .vertical-line {
            position: absolute;
            border-right: 1px solid #d7d7d7;
            height: 26px;
            right: 0;
            top: 50%;
            margin-top: -13px
        }

        .shop-tab:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .shop-footer {
            padding: 7px 10px 0 0;
            overflow: hidden
        }

        .shop-footer:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .shop-dong,.shop-go {
            border-radius: 5px;
            border: 1px solid #d7d7d7;
            width: 48%;
            font-size: 0
        }

        .shop-dong {
            float: left
        }

        .shop-go {
            float: right
        }

        .no-chat-shop-go {
            border-radius: 5px;
            border: 1px solid #d7d7d7;
            width: 100%;
            font-size: 0
        }

        .shop-btn {
            text-align: center;
            width: 100%;
            display: block;
            font-size: 0;
            height: 32px
        }

        .dong-text,.go-text {
            display: inline-block;
            font-size: 13px;
            color: #686868;
            line-height: 14px;
            vertical-align: top;
            margin-top: 9px
        }

        .icon-dong {
            display: inline-block;

            width: 14px;
            height: 14px;
            background-size: 100px 100px;
            margin-right: 4px;
            vertical-align: top;
            margin-top: 8px
        }

        .icon-dong-jimi {
            background-position: 0 -86px
        }

        .icon-dong-red {
            background-position: 0 -50px
        }

        .icon-dong-not {
            background-position: -16px -50px
        }

        .icon-dong-blue {
            background-position: -32px -50px
        }

        .icon-dong-yellow {
            background-position: -48px -50px
        }

        .icon-go {
            display: inline-block;

            width: 15px;
            height: 13px;
            background-size: 100px 100px;
            margin-right: 4px;
            vertical-align: top;
            margin-top: 9px
        }

        .noshop-dong {
            width: 100%;
            text-align: center;
            font-size: 0
        }

        .noshop-dong-btn {
            text-align: center;
            width: 100%;
            display: block;
            font-size: 0;
            height: 14px
        }

        .noshop-dong-btn .icon-right {
            top: 15px
        }

        .noshop-dong-text {
            display: inline-block;
            font-size: 13px;
            color: #686868;
            line-height: 14px;
            vertical-align: middle;
            height: 14px
        }

        .noshop-icon-dong {
            display: inline-block;

            width: 14px;
            height: 14px;
            background-size: 100px 100px;
            margin-right: 4px;
            vertical-align: top;
            margin-top: -1px
        }

        .noshop-icon-dong-red {
            background-position: 0 -50px
        }

        .noshop-icon-dong-not {
            background-position: -16px -50px
        }

        .noshop-icon-dong-blue {
            background-position: -32px -50px
        }

        .noshop-icon-dong-yellow {
            background-position: -48px -50px
        }

        .shop-href {
            display: block;
            width: 100%;
            height: 100%;
            overflow: hidden
        }

        .noshop-jimi {
            background-position: 0 -85px;
            height: 15px;
            margin-top: -1px
        }

        .score-text.red-font {
            color: #f23030
        }

        .score-text.green-font {
            color: #049b36
        }

        .jx-floor {
            display: block;
            position: relative;
            padding: 15px 10px
        }

        .jx-floor-icon {
            display: block;
            position: absolute;
            width: 34px;
            height: 34px;
            left: 10px;
            top: 15px
        }

        .jx-floor-title-wrap {
            margin-left: 39px;
            height: 34px;
            padding-top: 4px
        }

        .jx-floor-title {
            font-size: 13px;
            line-height: 13px;
            color: #333;
            font-weight: 700
        }

        .jx-floor-subtitle {
            font-size: 11px;
            line-height: 11px;
            color: #666;
            margin-top: 4px
        }

        .jx-floor-service {
            display: -webkit-box;
            display: -moz-box;
            display: -ms-flexbox;
            display: -webkit-flex;
            display: flex;
            margin-top: 5px
        }

        .jx-floor-service-item {
            -webkit-box-flex: 1;
            -moz-box-flex: 1;
            -webkit-flex: 1;
            -ms-flex: 1;
            flex: 1;
            text-align: center;
            border-right: 1px solid #e9e9e9;
            box-sizing: border-box
        }

        .jx-floor-service-item:last-child {
            border-right: 0 solid #e9e9e9
        }

        .jx-floor-service-icon {
            display: block;
            width: 16px;
            height: 16px;
            margin: 0 auto
        }

        .jx-floor-service-text {
            font-size: 11px;
            line-height: 14px;
            color: #666;
            margin-top: 4px;
            overflow: hidden
        }

        .jx-floor-jump-btn {
            display: block;
            margin-top: 10px;
            width: 100%;
            height: 26px;
            text-align: center;
            font-size: 0;
            color: #333;
            position: relative
        }

        .jx-floor-jump-btn::before {
            content: "";
            position: absolute;
            border: 1px solid #333;
            border-radius: 3px;
            top: 0;
            left: 0;
            transform: scale(.5,.5);
            -webkit-transform: scale(.5,.5);
            transform-origin: left top;
            -webkit-transform-origin: left top;
            box-sizing: border-box;
            height: 52px;
            width: 200%;
            z-index: 10
        }

        em.jx-floor-jump-icon {
            width: 14px;
            height: 13px;
            display: inline-block;
            margin: 6px 4px 0 0;
            background-size: 63px 63px
        }

        .jx-floor-jump-text {
            font-size: 12px;
            line-height: 26px;
            vertical-align: top
        }

        .share-quest {
            padding-top: 15px;
            padding-bottom: 15px;
            position: relative
        }

        .goodseval .info {
            position: relative;
            padding-top: 15px;
            padding-bottom: 14px;
            line-height: 13px
        }

        .goodseval .info .text {
            font-size: 15px
        }

        .goodseval .info .good-comment-text {
            float: right;
            margin-right: 22px;
            color: #252525;
            font-size: 13px;
            margin-top: 2px
        }

        .goodseval .info .part-note-msg {
            white-space: nowrap;
            width: auto
        }

        .goodseval .icon-arrow-right {
            top: 15px
        }

        .good-comment-text em {
            color: #f15353
        }

        .jx-good-comment-text em {
            color: #c09947
        }

        .eval-box-i {
            display: table;
            width: 100%;
            text-align: center
        }

        .eval-box-i a {
            color: #252525;
            font-size: 14px;
            display: table-cell;
            vertical-align: middle
        }

        .eval-box-i .icon {
        ;
            background-size: 100px 100px;
            background-position: -28px -28px;
            width: 17px;
            height: 15px;
            margin-bottom: 2px;
            display: inline-block;
            vertical-align: middle;
            font-style: normal
        }

        .icon.icon-cons {
            background-position: -46px -30px
        }

        .btn-good {
            border-right: 1px solid #dedede
        }

        .comment-list {
            padding: 0;
            margin: 0
        }

        .comment-list .comment-list-item {
            min-height: 64px;
            overflow: hidden
        }

        .comment-list-item {
            position: relative
        }

        .comment-list-item::before {
            position: absolute;
            content: '';
            -webkit-transform: scaleY(.5);
            transform: scaleY(.5);
            top: 0;
            left: 0;
            width: 100%;
            height: 1px;
            border-top: 1px solid #e2e2e2;
            z-index: 10
        }

        .comment-list-item .comment-item-content {
            max-height: 35px;
            display: table;
            overflow: hidden;
            vertical-align: middle;
            margin-top: -6px;
            width: 100%
        }

        .comment-list-item .comment-item-content .content {
            font-size: 13px;
            display: table-cell;
            vertical-align: middle;
            line-height: 17px;
            color: #252525;
            margin-right: 10px
        }

        .comment-item-info .comment-item-author {
            font-size: 9px;
            color: #848689;
            float: right;
            margin-top: 10px;
            margin-right: 10px;
            margin-bottom: 10px;
            line-height: 11px
        }

        .comment-item-info .comment-item-date {
            padding-right: 15px;
            float: right
        }

        .comment-item-date {
            font-size: 9px;
            color: #848689;
            text-align: right;
            margin: 11px 10px 10px 0
        }

        .comment-item-star {
            display: inline-block;
            overflow: hidden;
            width: 75px;
            height: 12px;
            background: url(../images/5.4/comment-star.png?v=1) repeat-x 0 -12px;
            background-size: 15px 24px;
            margin-top: 4px
        }

        .comment-item-star .real-star {
            display: inline-block;
            height: 20px;
            background: url(../images/5.4/comment-star.png?v=1) repeat-x 0 0;
            background-size: 15px 24px
        }

        .comment-stars-width1 {
            width: 20%
        }

        .comment-stars-width2 {
            width: 40%
        }

        .comment-stars-width3 {
            width: 60%
        }

        .comment-stars-width4 {
            width: 80%
        }

        .comment-stars-width5 {
            width: 100%
        }

        .comment-item-content .icon {
            width: 40px;
            display: table-cell;
            vertical-align: middle;
            text-align: right
        }

        .up-icon .icon i {
            background: url(../images/5.4/icon_sprits.png?v=1) no-repeat 0 0;
            background-size: 50px 50px;
            width: 15px;
            height: 15px;
            display: inline-block;
            background-position: 3px -5px
        }

        .up-icon .content {
            padding-bottom: 5px
        }

        .down-icon .icon i {
            background: url(../i/icon_sprits.png) no-repeat 0 0;
            background-size: 50px 50px;
            width: 15px;
            height: 15px;
            display: inline-block;
            background-position: 3px 6px
        }

        .down-icon .content {
            display: -webkit-box!important;
            overflow: hidden;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical
        }

        .comment-img-container {
            margin: 9px 0 1px 0;
            overflow: hidden;
            font-size: 0
        }

        .comment-img-container .img-container {
            overflow: hidden
        }

        .img-container li {
            display: inline-block;
            list-style-type: none;
            float: left
        }

        .img-container .comment-img-item {
            width: 65px;
            height: 65px;
            overflow: hidden;
            border-radius: 4px
        }

        .comment-img-item img {
            border-radius: 4px
        }

        .comment-img-space {
            width: 13px;
            height: 13px
        }

        .drug-consultation-wrap {
            background-color: #fff;
            width: 100%;
            padding: 10px 0;
            display: table
        }

        .drug-consultation-wrap a {
            display: table-cell;
            width: 50%;
            text-align: center;
            color: #686868;
            font-size: 0;
            height: 26px;
            vertical-align: middle
        }

        .drug-consultation-a.single-consult {
            width: 100%
        }

        .drug-certificate-a {
            border-right: 1px solid #d7d7d7
        }

        .drug-certificate-icon {
        ;
            background-size: 100px 100px;
            background-position: -66px -38px;
            width: 16px;
            height: 14px;
            display: inline-block;
            vertical-align: middle;
            margin-right: 3px
        }

        .drug-consultation-icon {
            background: url(../images/5.4/assess-btns-icon.png?v=1);
            display: inline-block;
            background-size: 75px 14px;
            width: 15px;
            height: 14px;
            background-position: -32px 0;
            vertical-align: middle;
            margin-right: 3px
        }

        .drug-certificate-text,.drug-consultation-text {
            display: inline-block;
            font-size: 13px;
            line-height: 13px;
            vertical-align: top
        }

        .af-open-app-wrap {
            background-color: #fff;
            text-align: center;
            padding: 8px 10px
        }

        .af-open-app-btn {
            font-size: 13px;
            line-height: 33px;
            height: 33px;
            color: #252525;
            border: 1px solid #d7d7d7;
            border-radius: 3px
        }

        .more-detail:first-child {
            border-top: 0 solid #e1e1e1
        }

        .more-detail a {
            display: block
        }

        .more-detail a div {
            position: relative;
            height: 44px;
            line-height: 44px
        }

        .more-detail .part-note-msg {
            color: #232326;
            vertical-align: middle;
            width: auto;
            max-width: 90%;
            text-overflow: ellipsis;
            white-space: nowrap;
            overflow: hidden
        }

        .save-energy {
            display: inline-block;
            no-repeat;
            background-size: 100px 100px;
            margin-left: 2px;
            margin-right: 6px;
            vertical-align: top;
            width: 12px;
            height: 15px;
            background-position: -42px 0
        }

        .fourG-icon {
            display: inline-block;
            no-repeat;
            background-size: 100px 100px;
            margin-left: 2px;
            margin-right: 6px;
            vertical-align: top;
            width: 11px;
            height: 15px;
            background-position: -55px 0
        }

        .phone-fitting {
            display: inline-block;
            no-repeat;
            background-size: 100px 100px;
            margin-left: 2px;
            margin-right: 6px;
            vertical-align: top;
            width: 14px;
            height: 12px;
            background-position: -68px 0
        }

        .shopping-guess-container {
            background: #f0f2f5;
            display: block;
            left: 0;
            width: 100%;
            bottom: 51px
        }

        .shopping-guess {
            background: #fff
        }

        .shopping-guess-title {
            line-height: 13px;
            font-size: 13px;
            color: #848689;
            margin: 15px 0 15px 10px;
            display: inline-block
        }

        .shopping-guess-title a {
            float: right;
            margin-right: 10px;
            height: 40px;
            display: block;
            text-decoration: none;
            width: 100px
        }

        .shopping-switch-cover {
            position: relative;
            width: 100%;
            overflow: hidden;
            display: block;
            min-height: 370px;
            max-height: 387px
        }

        .shopping-guess-list,.shopping-scan-list {
            margin-left: -3px;
            list-style: none;
            width: 640px
        }

        .shopping-guess-list:after,.shopping-scan-list:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .shopping-guess-item .guess-item-pic,.shopping-scan-item .scan-item-pic {
            background: url(../images/5.4/occupy-img320.gif?v=1) 0 0 no-repeat;
            background-size: 123px 123px;
            height: 121px;
            width: 121px;
            overflow: hidden;
            border: 1px solid #f0f2f5
        }

        .shopping-guess-item,.shopping-scan-item {
            list-style: none;
            margin: 0 0 8px 3px;
            width: 123px;
            float: left
        }

        .guess-item-pic img,.scan-item-pic img {
            width: 100%;
            height: auto;
            border: none
        }

        .shopping-guess-item .guess-item-content,.shopping-scan-item .scan-item-content {
            margin: 4px 2px 0 9px
        }

        .guess-item-content span,.scan-item-content span {
            font-size: 12px;
            color: #686868;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            display: -webkit-box;
            height: 28px;
            line-height: 14px
        }

        .guess-item-price,.scan-item-price {
            margin-top: 7px;
            margin-left: 11px;
            height: 15px;
            line-height: 15px
        }

        .guess-item-price span,.scan-item-price span {
            font-size: 13px;
            color: #222
        }

        .guess-item-price span i,.scan-item-price span i {
            font-style: normal
        }

        .guess-item-price span i:before,.scan-item-price span i:before {
            content: ' ';
            no-repeat;
            background-position: 0 -32px;
            background-size: 100px 100px;
            display: inline-block;
            width: 8px;
            height: 9px
        }

        .shopping-guess {
            position: relative
        }

        .shopping-guess-more {
            position: absolute;
            top: 15px;
            right: 19px;
            color: #bfbfbf;
            font-size: 12px;
            line-height: 12px
        }

        .guess-more-right {
            right: 6px;
            top: 16px
        }

        .manage-scan {
            color: #252525;
            font-size: 13px;
            text-align: center;
            height: 44px;
            position: relative;
            line-height: 44px;
            display: none
        }

        .manage-scan a {
            display: inline-block;
            width: 100%
        }

        .guess-tab {
            width: 100%;
            padding: 13px 0;
            display: flex;
            display: -webkit-flex;
            display: -webkit-box;
            display: -moz-box;
            display: -ms-flexbox
        }

        .guess-tab .switch-title {
            display: block;
            -moz-box-flex: 1;
            -webkit-box-flex: 1;
            -ms-flex: 1;
            flex: 1;
            font-size: 13px;
            color: #848689;
            text-align: center;
            border-right: 1px solid #dedede
        }

        .guess-tab .switch-title.selected {
            color: #f23030
        }

        .guess-tab .switch-title:last-child {
            border-right: 0 solid #dedede
        }

        #scan-record {
            display: none
        }

        #rank {
            display: none
        }

        .no-scan-tip {
            position: absolute;
            top: 175px;
            font-size: 17px;
            text-align: center;
            color: #bfbfbf;
            width: 100%
        }

        .shopping-rank-list {
            list-style: none;
            font-size: 0;
            padding: 0 10px;
            display: flex;
            display: -webkit-flex
        }

        .rank-column {
            flex-basis: 100%;
            -webkit-flex-basis: 100%;
            display: flex;
            display: -webkit-flex;
            justify-content: space-between;
            -webkit-justify-content: space-between;
            flex-direction: column;
            -webkit-flex-direction: column
        }

        .rank-column:nth-child(2) {
            align-items: center;
            -webkit-align-items: center
        }

        .rank-column:nth-child(3) {
            align-items: flex-end;
            -webkit-align-items: flex-end
        }

        .shopping-rank-item {
            list-style: none;
            margin-bottom: 10px;
            display: inline-block;
            flex: 1;
            -webkit-flex: 1
        }

        .shopping-rank-item a {
            display: inline-block;
            width: 80px
        }

        .shopping-rank-item .rank-item-pic {
            background: url(../images/5.4/occupy-img320.gif?v=1) 0 0 no-repeat;
            background-size: 80px 80px;
            height: 80px;
            width: 80px;
            border-radius: 5px;
            position: relative
        }

        .shopping-rank-item .rank-item-content {
            margin-top: 3px
        }

        .rank-item-pic img {
            width: 100%;
            height: auto;
            border-radius: 5px;
            border: none
        }

        .rank-item-content span {
            font-size: 10px;
            color: #686868;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            display: -webkit-box;
            height: 28px;
            line-height: 14px
        }

        .rank-item-price {
            margin-top: 7px;
            height: 15px;
            line-height: 15px
        }

        .rank-item-price span {
            font-size: 11px;
            color: #222
        }

        .rank-item-price span i {
            font-style: normal
        }

        .rank-item-price span i:before {
            content: ' ';
            no-repeat;
            background-position: 0 -32px;
            background-size: 100px 100px;
            display: inline-block;
            width: 8px;
            height: 9px
        }

        .top-icon {
            display: inline-block;
            background: url(../images/5.4/top-icon.png?v=1) no-repeat;
            background-size: 60px 20px;
            width: 20px;
            height: 20px;
            position: absolute;
            right: 0;
            top: -3px
        }

        .top-one {
            background-position: 0 0
        }

        .top-two {
            background-position: -20px 0
        }

        .top-three {
            background-position: -40px 0
        }

        .guess-focus-btn {
            overflow: hidden;
            text-align: center;
            width: 100%;
            height: 17px
        }

        .guess-focus-btn span {
            border-radius: 20px;
            -webkit-border-radius: 20px;
            -ms-border-radius: 20px;
            height: 5px;
            width: 5px;
            border: 1px solid #fff;
            margin: 0 2px;
            display: inline-block;
            background-color: #e3e5e9;
            vertical-align: top
        }

        .guess-focus-btn .active {
            border-radius: 20px;
            -webkit-border-radius: 20px;
            -ms-border-radius: 20px;
            height: 5px;
            width: 5px;
            border: 1px solid #fff;
            margin: 0 2px;
            display: inline-block;
            background: #ed2323;
            vertical-align: top
        }

        .guess-down,.scan-down {
            display: none
        }

        .guess-up,.scan-up {
            display: block
        }

        .flick-menu-mask {
            width: 100%;
            height: 120%;
            min-height: 100%;
            position: fixed;
            left: 0;
            top: 0;
            background: rgba(0,0,0,.5);
            z-index: 200;
            display: none;

        }

        .flick-menu-title {
            text-align: center;
            font-size: 15px;
            color: #848689;
            height: 45px;
            line-height: 45px;
            position: absolute;
            top: 0;
            left: 0;
            width: 100%
        }

        .flick-menu-btn {
            width: 100%;
            font-size: 0;
            position: absolute;
            bottom: 0;
            left: 0;

        }

        .x-margintop0 {
            margin-top: 0!important
        }

        .x-top2 {
            position: relative;
            top: 2px!important
        }

        .x-top3 {
            position: relative;
            top: 3px!important
        }

        .x-top4 {
            position: relative;
            top: 4px!important
        }

        .x-bigsale-hidden {
            display: none
        }

        .x-skin618-banner {
            width: 100%;
            height: 50px;
            border: none
        }

        .x-bottom-glass-wrap {
            width: 10%;
            height: 200%;
            position: absolute;
            left: -100%;
            top: -50%;
            transform: rotate(40deg)
        }

        .x-bottom-glass {
            display: inline-block;
            border-left: 8px solid #fffada;
            height: 100%;
            opacity: .5;
            box-shadow: 0 0 12px 4px #fffada
        }

        .x-bottom-glass:last-child {
            margin-left: 3px
        }

        @-webkit-keyframes blingbling-s {
            0% {
                left: -100%
            }

            5% {
                left: 130%
            }

            100% {
                left: 130%
            }
        }

        @-moz-keyframes blingbling-s {
            0% {
                left: -100%
            }

            5% {
                left: 130%
            }

            100% {
                left: 130%
            }
        }

        @-ms-keyframes blingbling-s {
            0% {
                left: -100%
            }

            5% {
                left: 130%
            }

            100% {
                left: 130%
            }
        }

        @-o-keyframes blingbling-s {
            0% {
                left: -100%
            }

            5% {
                left: 130%
            }

            100% {
                left: 130%
            }
        }

        @keyframes blingbling-s {
            0% {
                left: -100%
            }

            5% {
                left: 130%
            }

            100% {
                left: 130%
            }
        }

        @-webkit-keyframes blingbling-m {
            0% {
                left: -100%
            }

            7.5% {
                left: 130%
            }

            100% {
                left: 130%
            }
        }

        @-moz-keyframes blingbling-m {
            0% {
                left: -100%
            }

            7.5% {
                left: 130%
            }

            100% {
                left: 130%
            }
        }

        @-ms-keyframes blingbling-m {
            0% {
                left: -100%
            }

            7.5% {
                left: 130%
            }

            100% {
                left: 130%
            }
        }

        @-o-keyframes blingbling-m {
            0% {
                left: -100%
            }

            7.5% {
                left: 130%
            }

            100% {
                left: 130%
            }
        }

        @keyframes blingbling-m {
            0% {
                left: -100%
            }

            7.5% {
                left: 130%
            }

            100% {
                left: 130%
            }
        }

        @-webkit-keyframes blingbling-l {
            0% {
                left: -100%
            }

            15% {
                left: 130%
            }

            100% {
                left: 130%
            }
        }

        @-moz-keyframes blingbling-l {
            0% {
                left: -100%
            }

            15% {
                left: 130%
            }

            100% {
                left: 130%
            }
        }

        @-ms-keyframes blingbling-l {
            0% {
                left: -100%
            }

            15% {
                left: 130%
            }

            100% {
                left: 130%
            }
        }

        @-o-keyframes blingbling-l {
            0% {
                left: -100%
            }

            15% {
                left: 130%
            }

            100% {
                left: 130%
            }
        }

        @keyframes blingbling-l {
            0% {
                left: -100%
            }

            15% {
                left: 130%
            }

            100% {
                left: 130%
            }
        }

        .blingbling-2000ms {
            -webkit-animation: blingbling 10s 2s infinite;
            -moz-animation: blingbling 10s 2s infinite;
            -ms-animation: blingbling 10s 2s infinite;
            -o-animation: blingbling 10s 2s infinite;
            animation: blingbling 10s 2s infinite
        }

        .blingbling-2500ms {
            -webkit-animation: blingbling 10s 2.5s infinite;
            -moz-animation: blingbling 10s 2.5s infinite;
            -ms-animation: blingbling 10s 2.5s infinite;
            -o-animation: blingbling 10s 2.5s infinite;
            animation: blingbling 10s 2.5s infinite
        }

        .x-triangle-icon-wrap {
            display: inline-block;
            font-size: 0;
            position: relative;
            top: 11px;
            left: -6px;
            float: left
        }

        .x-triangle-icon {
            display: inline-block;
            width: 0;
            height: 0;
            border-left: 4px solid transparent;
            border-top: 4px solid #e83274;
            margin-top: 2px;
            margin-left: 2px;
            vertical-align: top
        }

        .x-pic-icon-box {
            display: inline-block;
            margin-top: 2px;
            width: 56px;
            height: 13px;
            overflow: hidden;
            vertical-align: top
        }

        .x-pic-icon {
            display: inline-block;
            background: -webkit-linear-gradient(left,#e83274,#fe614d);
            background: -o-linear-gradient(right,#e83274,#fe614d);
            background: -moz-linear-gradient(right,#e83274,#fe614d);
            background: linear-gradient(to right,#e83274,#fe614d);
            color: #fff;
            font-size: 20px;
            width: 112px;
            height: 26px;
            line-height: 26px;
            text-align: center;
            transform: scale(.5,.5);
            -webkit-transform: scale(.5,.5);
            transform-origin: left top;
            -webkit-transform-origin: left top
        }

        .x-bottom-prompt {
            position: fixed;
            left: 0;
            bottom: 50px;
            z-index: 10;
            width: 100%;
            height: 35px;
            background-color: rgba(253,250,229,.9);
            z-index: 9
        }

        .x-bottom-prompt:before {
            position: absolute;
            content: '';
            -webkit-transform: scaleY(.5);
            transform: scaleY(.5);
            top: 0;
            left: 0;
            width: 100%;
            height: 1px;
            border-top: 1px solid #efe6d4
        }

        .x-prompt-clock {
            display: inline-block;
            width: 13px;
            height: 35px;
            background: url(../images/5.4/icon_clock.png) no-repeat center/13px 14px;
            margin: 0 5px 0 15px;
            float: left
        }

        .x-prompt-title {
            line-height: 35px;
            color: #ec7307;
            font-size: 12px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            width: 72%;
            display: inline-block
        }

        .x-618price-wrap {
            width: 100%
        }

        .x-618price-wait {
            height: 47px;
            margin: 0 0 1px 0;
            padding: 2px 0 2px 0
        }

        .x-618price-begin {
            height: 56px;
            margin: -3px 0 -6px 0;
            padding: 0 0 1px 0
        }

        .x-618price-wait>div {
            line-height: 23.5px
        }

        .x-618price-begin>div:first-child {
            position: relative;
            top: -4px
        }

        .x-618price-begin>div:last-child {
            position: relative;
            top: 2px
        }

        .x-618price-icon {
            font-size: 10px;
            color: #fff;
            background: -webkit-linear-gradient(left,#e83274,#fe614d);
            background: -o-linear-gradient(right,#e83274,#fe614d);
            background: -moz-linear-gradient(right,#e83274,#fe614d);
            background: linear-gradient(to right,#e83274,#fe614d);
            padding: 1px 5px;
            margin-right: 1px;
            border-radius: 2px;
            display: inline-block;
            height: 14px;
            line-height: 16px
        }

        .x-618price-text {
            font-size: 13px
        }

        .x-618price-red {
            color: #e73076;
            font-weight: 600
        }

        .x-618price-grey {
            color: #81838e
        }

        .x-618price-timeborder {
            border: 1px solid #81838e;
            width: 18px;
            height: 12px;
            line-height: 12px;
            display: inline-block;
            text-align: center;
            font-size: 11px;
            position: relative;
            top: 0
        }

        .x-618price-black {
            color: #000
        }

        .x-618price-plus-member-price {
            position: relative;
            top: 12px
        }

        .spec-menu-content {
            position: fixed;
            bottom: 0;
            left: 0;
            right: 0;
            width: 100%;
            height: 70%;
            background-color: #fff;
            border-top: 1px solid #dadada;
            z-index: 250;
            display: none;
        }

        .spec-first-pic {
            position: absolute;
            left: 8px;
            top: -26px;
            width: 100px;
            height: 100px;
            border-radius: 3px;
            border: 1px solid #f0f1f3;
            background-color: #fff;
            overflow: hidden;
            display: -webkit-box;
            -webkit-box-pack: center;
            -webkit-box-align: center
        }

        .spec-first-pic img {
            max-width: 100%;
            max-height: 100%;
            overflow: hidden
        }

        .spec-menu-top {
            width: 100%;
            height: 84px
        }

        .spec-yang-pic {
            width: 16px;
            height: 16px;
            margin-right: 3px;
            margin-top: -3px;
            display:block;
        }

        .spec-price {
            font-size: 15px;
            line-height: 15px;
            color: #f23030;
            padding-top: 42px;
            padding-left: 121px;
            font-weight: 700
        }

        .spec-weight {
            font-size: 11px;
            line-height: 11px;
            color: #81838e;
            padding-top: 8px;
            padding-left: 121px
        }

        .spec-menu-middle {
            position: absolute;
            bottom: 43px;
            top: 85px;
            left: 0;
            right: 0;
            padding-left: 10px;
            overflow: auto
        }

        .spec-menu-btn {
            display: -webkit-box;
            display: -moz-box;
            display: -ms-flexbox;
            display: -webkit-flex;
            display: flex
        }

        .spec-menu-btn a {

            display: block;
            height: 43px;
            width: 1%;
            -webkit-box-flex: 1;
            -moz-box-flex: 1;
            -webkit-flex: 1;
            -ms-flex: 1;
            flex: 1;
            font-size: 15px;
            line-height: 43px;
            color: #fff;
            text-align: center;

        }

        .spec-menu-btn a.cart-black-font {
            color: #333
        }

        @media screen and (orientation:portrait) {
            .spec-menu-content {
                height: 70%
            }@	-webkit-keyframes spec-menu-move {
        0%{height: 0
        }

        100% {
            height: 70%
        }
        }

        @-moz-keyframes spec-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 70%
            }
        }

        @-ms-keyframes spec-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 70%
            }
        }

        @-o-keyframes spec-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 70%
            }
        }

        @keyframes spec-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 70%
            }
        }

        @-webkit-keyframes spec-menu-back {
            0% {
                height: 70%
            }

            100% {
                height: 0
            }
        }

        @-moz-keyframes spec-menu-back {
            0% {
                height: 70%
            }

            100% {
                height: 0
            }
        }

        @-ms-keyframes spec-menu-back {
            0% {
                height: 70%
            }

            100% {
                height: 0
            }
        }

        @-o-keyframes spec-menu-back {
            0% {
                height: 70%
            }

            100% {
                height: 0
            }
        }

        @keyframes spec-menu-back {
            0% {
                height: 70%
            }

            100% {
                height: 0
            }
        }}

        @media screen and (orientation:landscape) {
            .spec-menu-content {
                height: 70%
            }@	-webkit-keyframes spec-menu-move {
        0%{height: 0
        }

        100% {
            height: 70%
        }
        }

        @-moz-keyframes spec-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 70%
            }
        }

        @-ms-keyframes spec-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 70%
            }
        }

        @-o-keyframes spec-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 70%
            }
        }

        @keyframes spec-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 70%
            }
        }

        @-webkit-keyframes spec-menu-back {
            0% {
                height: 70%
            }

            100% {
                height: 0
            }
        }

        @-moz-keyframes spec-menu-back {
            0% {
                height: 70%
            }

            100% {
                height: 0
            }
        }

        @-ms-keyframes spec-menu-back {
            0% {
                height: 70%
            }

            100% {
                height: 0
            }
        }

        @-o-keyframes spec-menu-back {
            0% {
                height: 70%
            }

            100% {
                height: 0
            }
        }

        @keyframes spec-menu-back {
            0% {
                height: 70%
            }

            100% {
                height: 0
            }
        }}

        .spec-menu-show {
            -webkit-animation: spec-menu-move .5s 1 ease 0s;
            -moz-animation: spec-menu-move .5s 1 ease 0s;
            -ms-animation: spec-menu-move .5s 1 ease 0s;
            -o-animation: spec-menu-move .5s 1 ease 0s;
            animation: spec-menu-move .5s 1 ease 0s
        }

        .spec-menu-hide {
            -webkit-animation: spec-menu-back .5s 1 ease 0s;
            -moz-animation: spec-menu-back .5s 1 ease 0s;
            -ms-animation: spec-menu-back .5s 1 ease 0s;
            -o-animation: spec-menu-back .5s 1 ease 0s;
            animation: spec-menu-back .5s 1 ease 0s
        }

        .spec-jd-price {
            float: left;
            font-size: 0
        }

        .spec-plus-price {
            float: left;
            font-size: 0
        }

        .spec-plus-price-mtl {
            padding-left: 121px;
            padding-top: 21px
        }

        .warranty-icon {
            display: inline-block;
            no-repeat;
            background-size: 100px 100px;
            vertical-align: top;
            width: 14px;
            height: 14px;
            background-position: -84px 0;
            margin-right: 6px
        }

        .warranty-text {
            margin-top: 7px;
            font-size: 12px;
            color: #81838e;
            line-height: 14px
        }

        .warranty-text.selected-warranty {
            color: #6f81b7
        }

        .warranty-wrap {
            padding: 10px 10px 25px 0
        }

        .warranty-title {
            font-size: 13px;
            color: #848689;
            line-height: 13px
        }

        .warranty-items {
            padding-top: 21px
        }

        .warranty-items-wrap:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .warranty-items-img {
            width: 14px;
            height: 14px;
            margin-right: 7px;
            float: left
        }

        .warranty-items-title {
            display: inline-block;
            font-size: 13px;
            color: #232326;
            line-height: 14px;
            vertical-align: bottom;
            float: left
        }

        .warranty-des-href {
            display: inline-block;
            float: right;
            height: 14px;
            padding-left: 30px;
            font-size: 0
        }

        .warranty-des {
            display: inline-block;
            font-size: 12px;
            color: #f23030;
            line-height: 14px;
            vertical-align: top;
            margin-right: 4px
        }

        .warranty-des-icon {
            display: inline-block;
            no-repeat;
            background-size: 100px 100px;
            background-position: -28px 0;
            width: 12px;
            height: 12px;
            position: relative;
            top: 1px
        }

        .warranty-btn-row {
            display: -webkit-box;
            display: -webkit-flex;
            display: -ms-flexbox;
            display: flex;
            -webkit-box-pack: justify;
            -webkit-justify-content: space-between;
            -ms-flex-pack: justify;
            justify-content: space-between
        }

        .warranty-btn {
            display: block;
            width: 48%;
            height: 26px;
            border: 1px solid #e3e5e9;
            border-radius: 2px;
            line-height: 16px;
            font-size: 11px;
            color: #232326;
            margin-top: 11px;
            padding: 5px 6px;
            box-sizing: border-box
        }

        .warranty-btn:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .warranty-btn.selected {
            border: 1px solid #f23030;
            color: #f23030
        }

        .war-btn-text {
            width: 50%;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            text-align: right;
            display: inline-block;
            float: left;
            padding-right: 7px;
            box-sizing: border-box
        }

        .war-btn-price {
            display: inline-block;
            box-sizing: border-box;
            width: 50%;
            padding-left: 7px;
            text-align: left;
            border-left: 1px solid #e3e5e9;
            float: left;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap
        }

        .big-pic-popups {
            width: 100%;
            height: 100%;
            min-height: 100%;
            position: fixed;
            left: 0;
            top: 0;
            background-color: #fff;
            z-index: 300;
            display: none
        }

        .page-nub {
            height: 50px;
            width: 100%;
            line-height: 50px;
            text-align: center;
            position: relative;
            z-index: 100
        }

        .page-nub em {
            font-style: normal;
            font-size: 15px
        }

        .btn-arrow-left {
            position: absolute;
            width: 100px;
            left: 0
        }

        .btn-arrow-left i {
            display: block;
            background: url(../images/2014/ware/jd-sprites.png) no-repeat;
            background-size: 200px 200px;
            width: 20px;
            height: 20px;
            background-position: -20px 0;
            margin: 15px 0 0 12px
        }

        .scroll-big-imgs {
            text-align: center;
            width: 100%;
            position: absolute;
            left: 0;
            top: 50%;
            margin-top: -160px;
            font-size: 0
        }

        .oversea-menu-content {
            position: fixed;
            bottom: 0;
            left: 0;
            right: 0;
            width: 100%;
            height: 365px;
            background-color: #fff;
            z-index: 250;
            display: none
        }

        @media screen and (orientation:portrait) {
            .oversea-menu-content {
                height: 365px
            }@	-webkit-keyframes oversea-menu-move {
        0%{height: 0
        }

        100% {
            height: 365px
        }
        }

        @-moz-keyframes oversea-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 365px
            }
        }

        @-ms-keyframes oversea-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 365px
            }
        }

        @-o-keyframes oversea-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 365px
            }
        }

        @keyframes oversea-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 365px
            }
        }

        @-webkit-keyframes oversea-menu-back {
            0% {
                height: 365px
            }

            100% {
                height: 0
            }
        }

        @-moz-keyframes oversea-menu-back {
            0% {
                height: 365px
            }

            100% {
                height: 0
            }
        }

        @-ms-keyframes oversea-menu-back {
            0% {
                height: 365px
            }

            100% {
                height: 0
            }
        }

        @-o-keyframes oversea-menu-back {
            0% {
                height: 365px
            }

            100% {
                height: 0
            }
        }

        @keyframes oversea-menu-back {
            0% {
                height: 365px
            }

            100% {
                height: 0
            }
        }}

        @media screen and (orientation:landscape) {
            .oversea-menu-content {
                height: 275px
            }@	-webkit-keyframes oversea-menu-move {
        0%{height: 0
        }

        100% {
            height: 275px
        }
        }

        @-moz-keyframes oversea-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 275px
            }
        }

        @-ms-keyframes oversea-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 275px
            }
        }

        @-o-keyframes oversea-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 275px
            }
        }

        @keyframes oversea-menu-move {
            0% {
                height: 0
            }

            100% {
                height: 275px
            }
        }

        @-webkit-keyframes oversea-menu-back {
            0% {
                height: 275px
            }

            100% {
                height: 0
            }
        }

        @-moz-keyframes oversea-menu-back {
            0% {
                height: 275px
            }

            100% {
                height: 0
            }
        }

        @-ms-keyframes oversea-menu-back {
            0% {
                height: 275px
            }

            100% {
                height: 0
            }
        }

        @-o-keyframes oversea-menu-back {
            0% {
                height: 275px
            }

            100% {
                height: 0
            }
        }

        @keyframes oversea-menu-back {
            0% {
                height: 275px
            }

            100% {
                height: 0
            }
        }}

        .oversea-menu-wrapper {
            position: absolute;
            bottom: 43px;
            top: 45px;
            left: 0;
            right: 0
        }

        .oversea-menu-show {
            -webkit-animation: oversea-menu-move .5s 1 ease 0s;
            -moz-animation: oversea-menu-move .5s 1 ease 0s;
            -ms-animation: oversea-menu-move .5s 1 ease 0s;
            -o-animation: oversea-menu-move .5s 1 ease 0s;
            animation: oversea-menu-move .5s 1 ease 0s
        }

        .oversea-menu-hide {
            -webkit-animation: oversea-menu-back .5s 1 ease 0s;
            -moz-animation: oversea-menu-back .5s 1 ease 0s;
            -ms-animation: oversea-menu-back .5s 1 ease 0s;
            -o-animation: oversea-menu-back .5s 1 ease 0s;
            animation: oversea-menu-back .5s 1 ease 0s
        }

        .oversea-menu-btn a {
            height: 43px;
            width: 100%;
            display: block;
            font-size: 16px;
            text-align: center;
            line-height: 43px;
            background-color: #e93a3e;
            color: #fff
        }

        .rt-close-btn-wrap {
            display: block;
            width: 40px;
            height: 40px;
            position: absolute;
            top: 0;
            right: 0
        }

        .rt-close-btn-wrap .flick-menu-close {
            display: block;

            background-size: 100px 100px;
            background-position: -28px -17px;
            width: 24px;
            height: 24px;
            position: absolute;
            right: 10px;
            top: 16px
        }

        .oversea-menu-wrapper {
            padding-left: 10px;
            overflow: auto
        }

        .oversea-tip-item {
            padding: 14px 15px 14px 10px;
            font-size: 13px;
            line-height: 15px;
            color: #232326;
            border-bottom: 1px solid #efefef;
            word-wrap: break-word
        }

        .oversea-tip-item:last-child {
            border-bottom: 0 solid #efefef
        }

        .scale-box {
            display: table;
            word-wrap: break-word;
            word-break: break-all
        }

        .scale-box img {
            -webkit-user-select: none;
            -ms-user-select: none;
            user-select: none
        }

        .scale-box-ebook {
            display: table;
            word-wrap: break-word;
            word-break: break-all
        }

        .scale-box-ebook img {
            -webkit-user-select: none;
            -ms-user-select: none;
            user-select: none
        }

        .sift-mg {
            height: 100%;
            background-color: #fff
        }

        .detail-pc {
            padding: 5px 0
        }

        .detail {
            position: relative;
            padding: 5px 0;
            font-size: .75em;
            color: #5a5a5a;
            overflow: hidden;
            height: 100%;
            margin: 0 10px 12px 10px
        }

        .detail p {
            padding: 3px
        }

        #wareInfo {
            margin: 0 0;
            padding: 5px 0 0 0
        }

        #bookContent {
            margin: 0 0;
            padding: 5px 0 0 0
        }

        .book-container {
            width: 100%
        }

        .book-container .book-info-line {
            margin: 10px;
            background: #fff;
            overflow: hidden;
            margin-top: 16px
        }

        .book-container .book-container-item {
            margin: 0 10px 50px 10px
        }

        .book-info-line .info-title {
            display: inline-block;
            width: 25%;
            color: #999;
            font-size: .75em
        }

        .book-info-line .info-content {
            font-size: .75em
        }

        .book-container-item .book-item-title {
            color: #686868
        }

        .book-container-item .book-item-content {
            color: #333;
            margin-top: 10px
        }

        .err-div {
            width: 100%;
            position: relative;
            height: 150px;
            margin-top: 118px
        }

        .global-faq-wrap {
            margin: 0;
            padding: 0
        }

        .sale-service-wrap {
            margin: 0;
            padding: 0
        }

        .sale-service-grey {
            background-color: #f0f2f5;
            height: 7px
        }

        .sale-service-floor {
            padding: 14px 15px;
            background-color: #fff;
            color: #868489;
            font-size: 12px;
            line-height: 16px
        }

        .sale-service-title {
            text-align: center;
            position: relative
        }

        .sale-service-title .title-text {
            z-index: 10;
            position: relative;
            background-color: #fff;
            padding: 0 16px
        }

        .sale-service-title::after {
            content: "";
            height: 1px;
            width: 100%;
            position: absolute;
            top: 50%;
            left: 0;
            background-color: #d7d7d7;
            -webkit-transform: scaleY(.5);
            transform: scaleY(.5)
        }

        .sale-service-content {
            margin-top: 10px
        }

        .errPic-wrap {
            width: 100%;
            position: relative
        }

        .recommend-wrap {
            width: 100%;
            font-size: 0;
            overflow: hidden;
            background-color: #f0f2f5
        }

        .recommend-title {
            display: block;
            width: auto;
            height: 15px;
            margin: 9px auto
        }

        .find-similar-ul::after {
            content: "";
            display: block;
            height: 0;
            clear: both;
            visibility: hidden
        }

        .similar-li {
            float: left;
            width: 50%;
            box-sizing: border-box;
            padding-bottom: 4px;
            position: relative
        }

        .similar-li a {
            display: block
        }

        .similar-li:nth-child(2n+1) {
            padding-right: 2px
        }

        .similar-li:nth-child(2n) {
            padding-left: 2px
        }

        .similar-product {
            background-color: #fff;
            padding-bottom: 6px;
            font-size: 0
        }

        .similar-product .double-height {
            display: block;
            height: 21px
        }

        .similar-product img {
            width: 100%;
            display: block
        }

        .similar-product .product-icon {
            display: inline-block;
            position: relative;
            top: 0;
            margin-right: 4px
        }

        .similar-product .double-price-wrap {
            display: block;
            font-size: 0
        }

        .similar-product .double-price-wrap::after {
            content: "";
            display: block;
            height: 0;
            clear: both;
            visibility: hidden
        }

        .similar-product .double-price {
            display: inline-block;
            font-size: 13px;
            height: 14px;
            outline: 14px;
            margin-top: 3px;
            margin-bottom: 4px;
            padding-left: 4px;
            float: left
        }

        .similar-product .double-jx-price {
            color: #232326
        }

        .similar-product .double-sam-price {
            color: #1a68bc
        }

        .similar-product .price-icon {
            display: inline-block;
            height: 9px;
            width: auto;
            margin-top: 6px;
            float: left
        }

        .similar-product-text {
            height: 33px;
            font-size: 13px;
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            word-break: break-word;
            color: #232326;
            margin-top: 5px;
            line-height: 17px;
            margin-bottom: 3px;
            padding: 0 4px
        }

        .similar-product-price {
            color: #f23030;
            font-size: 13px;
            display: block;
            padding: 0 4px;
            position: relative;
            top: 1px;
            height: 25px;
            line-height: 25px
        }

        .similar-product-price .detail-big-price {
            font-size: 18px
        }

        .table-border {
            border-bottom: solid 1px #e7e7e7;
            border-left: solid 1px #e7e7e7;
            min-width: 100%;
            border-collapse: collapse;
            border-spacing: 0;
            word-wrap: break-word;
            word-break: break-all
        }

        .table-border td,.table-border th {
            border-top: solid 1px #e7e7e7;
            border-right: solid 1px #e7e7e7;
            padding: 10px
        }

        .table-border td {
            color: #848689;
            font-size: 12px
        }

        .table-border td strong {
            font-weight: 700;
            color: #848689
        }

        .table-border td:first-child {
            padding-left: 5%;
            width: 66px
        }

        .reply-body .tab-con {
            position: relative;
            z-index: 1000;
            height: 100%
        }

        .assess-flat {
            position: relative;
            margin-top: 10px
        }

        .assess-flat:after {
            position: absolute;
            content: '';
            -webkit-transform: scaleY(.5);
            transform: scaleY(.5);
            bottom: -1px;
            left: 0;
            width: 100%;
            height: 1px;
            border-bottom: 1px solid #e0e0e0
        }

        .assess-flat:before {
            position: absolute;
            content: '';
            -webkit-transform: scaleY(.5);
            transform: scaleY(.5);
            top: -1px;
            left: 0;
            width: 100%;
            height: 1px;
            border-top: 1px solid #e0e0e0
        }

        .tab-con-oaa .assess-flat:first-child {
            margin-top: 0
        }

        .assess-wrapper {
            display: block;
            padding-left: 10px;
            background-color: #fff
        }

        .assess-top {
            position: relative;
            padding: 11px 10px 11px 0;
            background-color: #fff
        }

        .assess-top:before {
            position: absolute;
            content: '';
            -webkit-transform: scaleY(.5);
            transform: scaleY(.5);
            bottom: 0;
            left: 0;
            width: 100%;
            height: 1px;
            border-bottom: 1px solid #e0e0e0
        }

        .assess-top:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .user-portrait img {
            display: block;
            float: left;
            width: 27px;
            height: 27px;
            border-radius: 50px
        }

        .user-name {
            font-size: 13px;
            line-height: 27px;
            float: left;
            height: 27px;
            margin-right: 5px;
            margin-left: 10px;
            color: #252525;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            max-width: 100px
        }

        .vip-icon {
            display: block;
            float: left;
            width: 15px;
            height: 15px;
            margin-top: 6px;
            border-radius: 3px;
            background: url(../images/5.4/vip-icon.png?v=1) 0 0 no-repeat;
            background-size: 75px 15px
        }

        .vip-diamond-icon {
            background-position: -60px 0
        }

        .vip-gold-icon {
            background-position: -45px 0
        }

        .vip-silver-icon {
            background-position: -30px 0
        }

        .vip-copper-icon {
            background-position: -15px 0
        }

        .vip-register-icon {
            background-position: 0 0
        }

        .assess-date,.reply-floor {
            font-size: 13px;
            line-height: 27px;
            float: right;
            height: 27px;
            color: #848689
        }

        .assess-bottom {
            padding-bottom: 12px;
            background-color: #fff
        }

        .product-item-star {
            display: inline-block;
            overflow: hidden;
            width: 75px;
            height: 11px;
            margin-top: 10px;
            margin-bottom: 10px;
            background: url(../images/5.4/comment-star.png?v=1) repeat-x 0 -11px;
            background-size: 15px 22px
        }

        .product-item-star .real-star {
            display: inline-block;
            height: 22px;
            background: url(../images/5.4/comment-star.png?v=1) repeat-x 0 0;
            background-size: 15px 22px
        }

        .jx-product-item-star {
            background: url(../images/5.4/jx-comment-star.png?v=1) repeat-x 0 -11px;
            background-size: 15px 22px
        }

        .jx-product-item-star .real-star {
            background: url(../images/5.4/jx-comment-star.png?v=1) repeat-x 0 0;
            background-size: 15px 22px
        }

        .comment-item-star {
            display: inline-block;
            overflow: hidden;
            width: 75px;
            height: 11px;
            margin-top: 10px;
            margin-bottom: -6px;
            background: url(../images/5.4/comment-star.png?v=1) repeat-x 0 -11px;
            background-size: 15px 22px
        }

        .comment-item-star .real-star {
            display: inline-block;
            height: 22px;
            background: url(../images/5.4/comment-star.png?v=1) repeat-x 0 0;
            background-size: 15px 22px
        }

        .comment-stars-width1 {
            width: 20%
        }

        .comment-stars-width2 {
            width: 40%
        }

        .comment-stars-width3 {
            width: 60%
        }

        .comment-stars-width4 {
            width: 80%
        }

        .comment-stars-width5 {
            width: 100%
        }

        .assess-content {
            font-size: 13px;
            line-height: 18px;
            margin-top: 10px;
            margin-right: 10px;
            color: #252525;
            word-wrap: break-word;
            overflow: hidden;
            text-align: justify
        }

        .product-img-module {
            font-size: 0;
            overflow: hidden;
            margin-top: 5px;
            margin-bottom: 6px;
            padding-right: 10px;
            white-space: nowrap
        }

        .product-img-module a {
            -webkit-tap-highlight-color: transparent
        }

        .product-imgs-li {
            display: block;
            float: left;
            overflow: hidden;
            width: 65px;
            height: 65px;
            border-radius: 4px
        }

        .product-imgs-li img {
            border-radius: 4px
        }

        .product-imgs-li:last-child {
            margin-right: 0
        }

        .product-img-space {
            display: block;
            float: left;
            width: 13px;
            height: 13px
        }

        .pay-date,.product-type {
            font-size: 12px;
            line-height: 12px;
            margin-top: 5px;
            color: #bfbfbf
        }

        .assess-btns-box {
            position: relative;
            padding-top: 10px;
            width: 100%;
            overflow: hidden;
            height: 29px;
            padding-bottom: 10px;
            background-color: #fff
        }

        .assess-btns-box:before {
            position: absolute;
            content: '';
            -webkit-transform: scaleY(.5);
            transform: scaleY(.5);
            top: 0;
            left: 0;
            width: 100%;
            height: 1px;
            border-top: 1px solid #e0e0e0
        }

        .assess-btns:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .assess-like-btn,.assess-reply-btn {
            font-size: 0;
            position: relative;
            width: 45%;
            text-align: center;
            border: 1px solid #e0e0e0;
            border-radius: 6px;
            -webkit-tap-highlight-color: transparent
        }

        .assess-btns-num {
            font-size: 26px;
            line-height: 58px;
            display: inline-block;
            height: 58px;
            color: #bfbfbf
        }

        .assess-like-btn {
            float: left;
            margin-left: 20px
        }

        .assess-reply-btn {
            float: right;
            margin-right: 20px
        }

        .assess-btns-icon {
            display: inline-block;
            margin-top: 16px;
            margin-right: 6px;
            vertical-align: top;
            background: url(../images/5.4/assess-btns-icon.png?v=1) 0 0 no-repeat;
            background-size: 150px 28px
        }

        .btn-like-icon {
            width: 32px;
            height: 28px;
            margin-right: 4px
        }

        .like-grey {
            background-position: 0 0
        }

        .like-red {
            background-position: -32px 0
        }

        .btn-reply-icon {
            width: 30px;
            height: 28px;
            margin-right: 6px;
            background-position: -64px 0
        }

        .btn-no-reply-icon {
            width: 30px;
            height: 28px;
            margin-right: 6px;
            background-position: -96px 0
        }

        .reply-flat {
            position: relative;
            margin-bottom: 10px;
            background-color: #fff
        }

        .reply-flat:before {
            position: absolute;
            content: '';
            -webkit-transform: scaleY(.5);
            transform: scaleY(.5);
            top: -1px;
            left: 0;
            width: 100%;
            height: 1px;
            border-top: 1px solid #e0e0e0
        }

        .reply-flat:after {
            position: absolute;
            content: '';
            -webkit-transform: scaleY(.5);
            transform: scaleY(.5);
            bottom: -1px;
            left: 0;
            width: 100%;
            height: 1px;
            border-bottom: 1px solid #e0e0e0
        }

        .reply-username {
            font-size: 13px;
            color: #848689
        }

        .reply-bottom {
            padding-bottom: 10px
        }

        .reply-bottom:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .reply-date {
            font-size: 12px;
            float: left;
            margin-top: 15px;
            color: #bfbfbf;
            line-height: 12px
        }

        .reply-btn-box {
            float: right;
            width: 70px;
            height: 25px;
            margin-top: 12px;
            margin-right: 10px
        }

        .reply-flat-bottom {
            display: none;
            padding-bottom: 10px;
            width: 93.75%;
            text-align: center;
            padding-top: 45px;
            background-color: #f0f2f5;
            margin-left: 3.125%
        }

        .no-more-border {
            width: 100%;
            height: 1px;
            background: url(../images/5.4/no-more-bord.png?v=1) 0 0 repeat-x
        }

        .no-more-icon {
            width: 80px;
            height: 54px;
            margin-top: -12px;
            background-color: #f0f2f5
        }

        .no-more-write {
            font-size: 12px;
            margin-top: 7px;
            color: #bfbfbf
        }

        .error-img {
            display: block;
            width: 100px;
            height: 62px;
            margin: 118px auto 0 auto
        }

        .error-write {
            font-size: 17px;
            margin-top: 20px;
            text-align: center;
            color: #bfbfbf
        }

        .show-all-assess {
            font-size: 14px;
            line-height: 34px;
            display: block;
            width: 39.375%;
            height: 34px;
            margin: 16px auto 0 auto;
            text-align: center;
            color: #686868;
            border: 1px solid #848689;
            -webkit-border-radius: 3px;
            border-radius: 3px;
            background-color: #fff
        }

        .tab-no-assess {
            width: 100%;
            height: 100%;
            background-color: #fff
        }

        .reply-body-color {
            background-color: #f0f2f5
        }

        .white-color {
            background-color: #fff
        }

        .prompt-window {
            position: fixed;
            z-index: 100;
            top: 50%;
            left: 50%;
            width: 144px;
            height: 88px;
            margin-top: -44px;
            margin-left: -72px;
            border-radius: 7px;
            background-color: rgba(0,0,0,.8)
        }

        .prompt-secc-icon {
            display: block;
            width: 26px;
            height: 26px;
            margin: 18px auto 0 auto;
            background: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAADgAAAB4CAYAAACn3jFyAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkQ4MzkzQzEzRjY0RjExRTU5MDI5RkQyNzk0QTQ5MjVBIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkQ4MzkzQzE0RjY0RjExRTU5MDI5RkQyNzk0QTQ5MjVBIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6RDgzOTNDMTFGNjRGMTFFNTkwMjlGRDI3OTRBNDkyNUEiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6RDgzOTNDMTJGNjRGMTFFNTkwMjlGRDI3OTRBNDkyNUEiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4a44QkAAAGEUlEQVR42uxc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) no-repeat;background-position: -1px -29px;
            background-size: 28px 60px
        }

        .prompt-err-icon {
            display: block;
            width: 26px;
            height: 26px;
            margin: 18px auto 0 auto;
            background: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAADgAAAB4CAYAAACn3jFyAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkQ4MzkzQzEzRjY0RjExRTU5MDI5RkQyNzk0QTQ5MjVBIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkQ4MzkzQzE0RjY0RjExRTU5MDI5RkQyNzk0QTQ5MjVBIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6RDgzOTNDMTFGNjRGMTFFNTkwMjlGRDI3OTRBNDkyNUEiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6RDgzOTNDMTJGNjRGMTFFNTkwMjlGRDI3OTRBNDkyNUEiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4a44QkAAAGEUlEQVR42uxc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) no-repeat;background-position: -1px -1px;
            background-size: 28px 60px
        }

        .prompt-text {
            font-size: 15px;
            display: block;
            margin-top: 9px;
            text-align: center;
            color: #fff
        }

        .useful-window {
            display: none
        }

        .assess-like-btn .like {
            font-style: normal;
            opacity: 0
        }

        .like_ani {
            font-size: 28px;
            font-weight: 700;
            position: absolute;
            top: -9px;
            right: 30px;
            -webkit-animation: myfirst .6s;
            -moz-animation: myfirst .6s;
            -ms-animation: myfirst .6s;
            -o-animation: myfirst .6s;
            animation: myfirst .6s;
            color: red
        }

        @-webkit-keyframes myfirst {
            0% {
                top: -11px;
                right: 28px;
                opacity: .1
            }

            10% {
                top: -13px;
                right: 26px;
                opacity: .2
            }

            20% {
                top: -15px;
                right: 24px;
                opacity: .4
            }

            30% {
                top: -17px;
                right: 22px;
                opacity: .6
            }

            40% {
                top: -19px;
                right: 20px;
                opacity: .8
            }

            50% {
                top: -20px;
                right: 18px;
                opacity: 1
            }

            60% {
                top: -22px;
                right: 16px;
                opacity: .8
            }

            70% {
                top: -24px;
                right: 14px;
                opacity: .6
            }

            80% {
                top: -26px;
                right: 12px;
                opacity: .4
            }

            90% {
                top: -28px;
                right: 10px;
                opacity: .2
            }

            100% {
                top: -30px;
                right: 8px;
                opacity: .1
            }
        }

        @-moz-keyframes myfirst {
            0% {
                top: -11px;
                right: 28px;
                opacity: .1
            }

            10% {
                top: -13px;
                right: 26px;
                opacity: .2
            }

            20% {
                top: -15px;
                right: 24px;
                opacity: .4
            }

            30% {
                top: -17px;
                right: 22px;
                opacity: .6
            }

            40% {
                top: -19px;
                right: 20px;
                opacity: .8
            }

            50% {
                top: -20px;
                right: 18px;
                opacity: 1
            }

            60% {
                top: -22px;
                right: 16px;
                opacity: .8
            }

            70% {
                top: -24px;
                right: 14px;
                opacity: .6
            }

            80% {
                top: -26px;
                right: 12px;
                opacity: .4
            }

            90% {
                top: -28px;
                right: 10px;
                opacity: .2
            }

            100% {
                top: -30px;
                right: 8px;
                opacity: .1
            }
        }

        @-ms-keyframes myfirst {
            0% {
                top: -11px;
                right: 28px;
                opacity: .1
            }

            10% {
                top: -13px;
                right: 26px;
                opacity: .2
            }

            20% {
                top: -15px;
                right: 24px;
                opacity: .4
            }

            30% {
                top: -17px;
                right: 22px;
                opacity: .6
            }

            40% {
                top: -19px;
                right: 20px;
                opacity: .8
            }

            50% {
                top: -20px;
                right: 18px;
                opacity: 1
            }

            60% {
                top: -22px;
                right: 16px;
                opacity: .8
            }

            70% {
                top: -24px;
                right: 14px;
                opacity: .6
            }

            80% {
                top: -26px;
                right: 12px;
                opacity: .4
            }

            90% {
                top: -28px;
                right: 10px;
                opacity: .2
            }

            100% {
                top: -30px;
                right: 8px;
                opacity: .1
            }
        }

        @-o-keyframes myfirst {
            0% {
                top: -11px;
                right: 28px;
                opacity: .1
            }

            10% {
                top: -13px;
                right: 26px;
                opacity: .2
            }

            20% {
                top: -15px;
                right: 24px;
                opacity: .4
            }

            30% {
                top: -17px;
                right: 22px;
                opacity: .6
            }

            40% {
                top: -19px;
                right: 20px;
                opacity: .8
            }

            50% {
                top: -20px;
                right: 18px;
                opacity: 1
            }

            60% {
                top: -22px;
                right: 16px;
                opacity: .8
            }

            70% {
                top: -24px;
                right: 14px;
                opacity: .6
            }

            80% {
                top: -26px;
                right: 12px;
                opacity: .4
            }

            90% {
                top: -28px;
                right: 10px;
                opacity: .2
            }

            100% {
                top: -30px;
                right: 8px;
                opacity: .1
            }
        }

        @keyframes myfirst {
            0% {
                top: -11px;
                right: 28px;
                opacity: .1
            }

            10% {
                top: -13px;
                right: 26px;
                opacity: .2
            }

            20% {
                top: -15px;
                right: 24px;
                opacity: .4
            }

            30% {
                top: -17px;
                right: 22px;
                opacity: .6
            }

            40% {
                top: -19px;
                right: 20px;
                opacity: .8
            }

            50% {
                top: -20px;
                right: 18px;
                opacity: 1
            }

            60% {
                top: -22px;
                right: 16px;
                opacity: .8
            }

            70% {
                top: -24px;
                right: 14px;
                opacity: .6
            }

            80% {
                top: -26px;
                right: 12px;
                opacity: .4
            }

            90% {
                top: -28px;
                right: 10px;
                opacity: .2
            }

            100% {
                top: -30px;
                right: 8px;
                opacity: .1
            }
        }

        .is-loading {
            display: none;
            height: 27px;
            text-align: center;
            font-size: 0
        }

        .is-loading em {
            background: url(../images/5.4/loading-animation.gif?v=1) center 2px no-repeat;
            display: inline-block;
            width: 23px;
            height: 25px;
            background-size: 23px 23px;
            vertical-align: top
        }

        .is-loading span {
            color: #b3b3b3;
            font-size: 11px;
            display: inline-block;
            line-height: 27px;
            margin-left: 10px
        }

        .jd-slider-container:after {
            font-size: 0;
            display: block;
            visibility: hidden;
            clear: both;
            height: 0;
            content: ''
        }

        .assess-btns {
            width: 200%;
            -webkit-transform: scale(.5,.5);
            transform: scale(.5,.5);
            transform-origin: left top;
            -webkit-transform-origin: left top
        }

        .reply-btn {
            font-size: 26px;
            line-height: 48px;
            width: 138px;
            height: 48px;
            -webkit-transform: scale(.5,.5);
            transform: scale(.5,.5);
            transform-origin: left top;
            -webkit-transform-origin: left top;
            text-align: center;
            color: #f15353;
            border: 1px solid #f15353;
            border-radius: 6px
        }

        .cancel-but {
            height: 81px;
            width: 233px;
            color: #686868;
            font-size: 28px;
            border: 1px solid #b9b9b9;
            border-radius: 6px;
            text-align: center;
            line-height: 81px;
            transform: scale(.5,.5);
            -webkit-transform: scale(.5,.5);
            transform-origin: left top;
            -webkit-transform-origin: left top
        }

        .errPic {
            width: 128px;
            height: 150px;
            position: absolute;
            top: 50%;
            left: 50%;
            margin-top: -75px;
            margin-left: -64px
        }

        .errPic img {
            display: block;
            margin-left: auto;
            margin-right: auto;
            margin-top: 0;
            height: 60px
        }

        .errPic .errPic-content {
            display: block;
            font-size: 17px;
            text-align: center;
            color: #bfbfbf;
            margin-top: 20px
        }

        .errPic .pro-button-box {
            margin-top: 15px;
            height: 34px;
            width: 100%;
            margin-left: auto;
            margin-right: auto;
            overflow: hidden
        }

        .errPic .pro-button {
            box-sizing: border-box;
            display: block;
            background-color: #fff;
            display: inline-block;
            margin-top: 0;
            height: 68px;
            width: 200%;
            font-size: 28px;
            color: #848689;
            text-align: center;
            -webkit-border-radius: 6px;
            border-radius: 6px;
            border: 1px solid #848689;
            line-height: 68px;
            transform: scale(.5,.5);
            -webkit-transform: scale(.5,.5);
            transform-origin: left top;
            -webkit-transform-origin: left top
        }

        .plus-icon-wrap {
            font-size: 0;
            display: inline-block;
            vertical-align: top;
            margin-top: 7px
        }

        .plus-icon {
            width: 45px;
            height: 14px;
            border-radius: 2px
        }

        .plus-icon-left {
            border-radius: 2px 0 0 2px
        }

        .plus-icon-text {
            font-size: 9px;
            line-height: 10px;
            height: 14px;
            color: #e18e0c;
            display: inline-block;
            padding: 3px 2px 1px 2px;
            vertical-align: top;
            position: relative
        }

        .plus-icon-text:before {
            content: '';
            position: absolute;
            left: 0;
            top: 0;
            height: 28px;
            width: 200%;
            border-width: 1px 1px 1px 0;
            border-style: solid;
            border-color: #e18e0c;
            border-radius: 0 4px 4px 0;
            transform: scale(.5,.5);
            -webkit-transform: scale(.5,.5);
            transform-origin: left top;
            -webkit-transform-origin: left top;
            box-sizing: border-box
        }

        .open-app-a-bottom {
            display: none;
            padding-top: 33px;
            padding-bottom: 24px;
            text-align: center
        }

        .open-app-a-text {
            font-size: 13px;
            color: #868489;
            line-height: 13px;
            margin-bottom: 12px
        }

        .open-app-a-img {
            display: block;
            position: relative;
            left: 50%;
            margin-left: -82px;
            width: 164px;
            height: 48px;
            background: url(../images/5.4/assess-open-app.png?v=1) 0 0 no-repeat;
            background-size: 164px 48px
        }

        .open-app-a-img a {
            display: block;
            width: 164px;
            height: 48px;
            border-radius: 10px;
            background-color: transparent
        }

        .open-app-a-img a:active {
            background-color: rgba(0,0,0,.1)
        }

        .x-loadBadComment {
            display: -webkit-box;
            -webkit-box-align: center;
            -webkit-box-pack: center;
            color: #232326;
            font-size: 13px;
            background-color: #fff;
            height: 44px;
            padding: 0 7px;
            margin-top: 10px
        }

        .x-loadBadComment div {
            -webkit-box-flex: 1;
            text-align: center
        }

        .x-loadBadComment i {
            display: inline-block;
            width: 15px;
            height: 15px;
            background-image: url(../images/5.4/product-detail-sprites-mjs.png);
            background-repeat: no-repeat;
            background-size: 100px 100px;
            background-position: 3px 6px
        }

        .shade-floor {
            width: 100%;
            height: 100%;
            position: fixed;
            top: 0;
            left: 0;
            text-align: center;
            z-index: 500
        }

        .message-icon {
            background: url(data:image/png;
            base64,iVBORw0KGgoAAAANSUhEUgAAADgAAAB4CAYAAACn3jFyAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkQ4MzkzQzEzRjY0RjExRTU5MDI5RkQyNzk0QTQ5MjVBIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkQ4MzkzQzE0RjY0RjExRTU5MDI5RkQyNzk0QTQ5MjVBIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6RDgzOTNDMTFGNjRGMTFFNTkwMjlGRDI3OTRBNDkyNUEiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6RDgzOTNDMTJGNjRGMTFFNTkwMjlGRDI3OTRBNDkyNUEiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4a44QkAAAGEUlEQVR42uxc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) no-repeat;background-size: 28px 60px
        }

        .content-box {
            top: 50%;
            left: 50%;
            overflow: hidden
        }

        .message-box {
            width: 145px;
            background: rgba(0,0,0,.6);
            border-radius: 8px;
            text-align: center;
            position: absolute
        }

        .message-box .message-box-icon {
            width: 26px;
            height: 26px;
            display: inline-block;
            margin: 18px 0 9px 0;
            position: relative;
            overflow: hidden;
            vertical-align: middle
        }

        .message-box .message-box-content {
            font-size: 15px;
            line-height: 20px;
            color: #fff;
            padding: 0 10px 21px 10px
        }

        .succee-icon {
            display: inline-block;
            height: 26px;
            width: 26px;
            background-position: -1px -29px
        }

        .error-icon {
            display: inline-block;
            height: 26px;
            width: 26px;
            background-position: -1px -1px
        }

        .choose-box {
            background: #fff;
            width: 272px;
            height: auto;
            position: fixed;
            top: 50%;
            left: 50%;
            border-radius: 10px;
            text-align: center;
            overflow: hidden;
            z-index: 1005
        }

        .choose-box .choose-box-title {
            padding: 27px 0 18px;
            font-size: 18px;
            color: #232326
        }

        .choose-box .message-div .message-txt {
            position: relative;
            padding: 0 12px 23px 12px;
            font-size: 16px;
            color: #232326
        }

        .choose-box .choose-box-btn {
            width: 100%;
            display: -moz-box;
            display: -webkit-box;
            display: box
        }

        .choose-box .choose-box-btn a {
            box-flex: 1;
            -moz-box-flex: 1;
            -webkit-box-flex: 1;
            box-flex: 1;
            display: block;
            position: relative;
            height: 44px;
            line-height: 44px;
            color: #232326;
            font-size: 15px
        }

        .choose-box .choose-box-btn a:before {
            position: absolute;
            content: '';
            -webkit-transform: scaleY(.5);
            transform: scaleY(.5);
            top: 0;
            left: 0;
            width: 100%;
            height: 1px;
            border-top: 1px solid #e3e5e9
        }

        .choose-box .choose-box-btn a.btn-sure {
            background: #f23030;
            position: relative;
            color: #fff
        }

        .choose-box .choose-box-btn a.btn-sure:before {
            position: absolute;
            content: '';
            -webkit-transform: scaleY(.5);
            transform: scaleY(.5);
            top: 0;
            left: 0;
            width: 100%;
            height: 1px;
            border-top: 1px solid #f23030
        }

        .shade-floor .cover-floor {
            width: 100%;
            height: 100%;
            position: fixed;
            background: rgba(0,0,0,.7);
            display: block;
            vertical-align: middle;
            z-index: 600;
            margin: 0 auto;
            font-size: .875em
        }

        .info-box {
            width: 85%;
            position: fixed;
            background: #f8f8f8;
            z-index: 700;
            border-radius: 5px
        }

        .info-box .info-box-content {
            text-align: center;
            font-size: 1.214285714285714em;
            color: #4d4d4d;
            margin-bottom: 15px
        }

        .info-box .btn-box {
            text-align: center
        }

        .info-box-content .info-box-icon {
            display: inline-block;
            vertical-align: middle;
            width: 35px;
            height: 35px
        }

        .info-box-icon img {
            width: 100%;
            height: 100%;
            display: block
        }

        .info-box-text {
            padding: 25px 15px;
            display: block
        }

        .btn-box a {
            text-decoration: none;
            cursor: pointer;
            display: inline-block;
            margin-bottom: 15px
        }

        .btn-box-item {
            width: 50%;
            float: left
        }

        .btn-box-item i {
            width: 75%;
            display: inline-block;
            font-style: normal;
            border-radius: 3px;
            height: 2.5em;
            line-height: 2.5em;
            text-align: center;
            font-size: 15px;
            float: none
        }

        .btn-box-item .btn-cancel {
            background-color: #fff;
            border: 1px solid #e0e0e0;
            color: #686868
        }

        .btn-box-item .btn-ok {
            background-color: #f35656;
            border: 1px solid #f35656;
            color: #fff
        }

        .bdb-1px:after {
            border-top: 1px solid #e0e0e0;
            content: " ";
            display: block;
            width: 85%;
            margin: 0 auto
        }

        .one-btn-tip-info {
            padding: 15px;
            text-align: center
        }

        .one-btn-tip-info span {
            display: inline-block;
            vertical-align: middle;
            font-size: 16px;
            line-height: 23px;
            color: #232326
        }

        .one-btn-tip-btn {
            height: 40px;
            font-size: 15px;
            color: #f23030;
            line-height: 40px;
            text-align: center;
            position: relative
        }

        .one-btn-tip-btn:before {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 1px;
            content: '';
            -webkit-transform: scaleY(.5);
            transform: scaleY(.5);
            border-top: 1px solid #e3e5e9
        }

        .one-btn-tip-box-tip {
            border-radius: 10px;
            background-color: #fff;
            width: 272px;
            height: auto;
            position: fixed;
            z-index: 8890;
            left: 50%;
            top: 50%;
            margin-left: -136px;
            margin-top: -70px;
            border: 1px solid #282828;
            overflow: hidden
        }

        .area-price {
            position: fixed;
            left: 50%;
            bottom: 91px;
            background-color: #232326;
            border-radius: 5px;
            font-size: 14px;
            line-height: 16px;
            min-width: 46%;
            max-width: 72%;
            padding: 12px 10px;
            z-index: 400;
            color: #fff;
            text-align: center;
            margin-left: -36%;
            opacity: .7
        }
        .spec-menu{
            display: none;
        }
    </style>
    <!-- 引入jQuery 2.0+ -->
    <script src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
    <script>
        !function(N,M){function L(){var a=I.getBoundingClientRect().width;a/F>540&&(a=540*F);var d=a/10;I.style.fontSize=d+"px",D.rem=N.rem=d}var K,J=N.document,I=J.documentElement,H=J.querySelector('meta[name="viewport"]'),G=J.querySelector('meta[name="flexible"]'),F=0,E=0,D=M.flexible||(M.flexible={});if(H){var C=H.getAttribute("content").match(/initial\-scale=([\d\.]+)/);C&&(E=parseFloat(C[1]),F=parseInt(1/E))}else{if(G){var B=G.getAttribute("content");if(B){var A=B.match(/initial\-dpr=([\d\.]+)/),z=B.match(/maximum\-dpr=([\d\.]+)/);A&&(F=parseFloat(A[1]),E=parseFloat((1/F).toFixed(2))),z&&(F=parseFloat(z[1]),E=parseFloat((1/F).toFixed(2)))}}}if(!F&&!E){var y=N.navigator.userAgent,x=(!!y.match(/android/gi),!!y.match(/iphone/gi)),w=x&&!!y.match(/OS 9_3/),v=N.devicePixelRatio;F=x&&!w?v>=3&&(!F||F>=3)?3:v>=2&&(!F||F>=2)?2:1:1,E=1/F}if(I.setAttribute("data-dpr",F),!H){if(H=J.createElement("meta"),H.setAttribute("name","viewport"),H.setAttribute("content","initial-scale="+E+", maximum-scale="+E+", minimum-scale="+E+", user-scalable=no"),I.firstElementChild){I.firstElementChild.appendChild(H)}else{var u=J.createElement("div");u.appendChild(H),J.write(u.innerHTML)}}N.addEventListener("resize",function(){clearTimeout(K),K=setTimeout(L,300)},!1),N.addEventListener("pageshow",function(b){b.persisted&&(clearTimeout(K),K=setTimeout(L,300))},!1),"complete"===J.readyState?J.body.style.fontSize=12*F+"px":J.addEventListener("DOMContentLoaded",function(){J.body.style.fontSize=12*F+"px"},!1),L(),D.dpr=N.dpr=F,D.refreshRem=L,D.rem2px=function(d){var c=parseFloat(d)*this.rem;return"string"==typeof d&&d.match(/rem$/)&&(c+="px"),c},D.px2rem=function(d){var c=parseFloat(d)/this.rem;return"string"==typeof d&&d.match(/px$/)&&(c+="rem"),c}}(window,window.lib||(window.lib={}));

    </script>

    <script>/*!
 * =====================================================
 * Ratchet v2.0.2 (http://goratchet.com)
 * Copyright 2014 Connor Sears
 * Licensed under MIT (https://github.com/twbs/ratchet/blob/master/LICENSE)
 *
 * v2.0.2 designed by @connors.
 * =====================================================
 */
    !function(){"use strict";var a=function(a){for(var b,c=document.querySelectorAll("a");a&&a!==document;a=a.parentNode)for(b=c.length;b--;)if(c[b]===a)return a},b=function(b){var c=a(b.target);return c&&c.hash?document.querySelector(c.hash):void 0};window.addEventListener("touchend",function(a){var c=b(a);c&&(c&&c.classList.contains("modal")&&c.classList.toggle("active"),a.preventDefault())})}(),!function(){"use strict";var a,b=function(a){for(var b,c=document.querySelectorAll("a");a&&a!==document;a=a.parentNode)for(b=c.length;b--;)if(c[b]===a)return a},c=function(){a.style.display="none",a.removeEventListener("webkitTransitionEnd",c)},d=function(){var b=document.createElement("div");return b.classList.add("backdrop"),b.addEventListener("touchend",function(){a.addEventListener("webkitTransitionEnd",c),a.classList.remove("visible"),a.parentNode.removeChild(d)}),b}(),e=function(c){var d=b(c.target);if(d&&d.hash&&!(d.hash.indexOf("/")>0)){try{a=document.querySelector(d.hash)}catch(e){a=null}if(null!==a&&a&&a.classList.contains("popover"))return a}},f=function(a){var b=e(a);b&&(b.style.display="block",b.offsetHeight,b.classList.add("visible"),b.parentNode.appendChild(d))};window.addEventListener("touchend",f)}(),!function(){"use strict";var a,b=function(){},c=20,d=sessionStorage,e={},f={slideIn:"slide-out",slideOut:"slide-in",fade:"fade"},g={bartab:".bar-tab",barnav:".bar-nav",barfooter:".bar-footer",barheadersecondary:".bar-header-secondary"},h=function(a,b){o.id=a.id,b&&(a=k(a.id)),d[a.id]=JSON.stringify(a),window.history.replaceState(a.id,a.title,a.url),e[a.id]=document.body.cloneNode(!0)},i=function(){var a=o.id,b=JSON.parse(d.cacheForwardStack||"[]"),e=JSON.parse(d.cacheBackStack||"[]");for(e.push(a);b.length;)delete d[b.shift()];for(;e.length>c;)delete d[e.shift()];window.history.pushState(null,"",d[o.id].url),d.cacheForwardStack=JSON.stringify(b),d.cacheBackStack=JSON.stringify(e)},j=function(a,b){var c="forward"===b,e=JSON.parse(d.cacheForwardStack||"[]"),f=JSON.parse(d.cacheBackStack||"[]"),g=c?f:e,h=c?e:f;o.id&&g.push(o.id),h.pop(),d.cacheForwardStack=JSON.stringify(e),d.cacheBackStack=JSON.stringify(f)},k=function(a){return JSON.parse(d[a]||null)||{}},l=function(b){var c=t(b.target);if(!(!c||b.which>1||b.metaKey||b.ctrlKey||a||location.protocol!==c.protocol||location.host!==c.host||!c.hash&&/#/.test(c.href)||c.hash&&c.href.replace(c.hash,"")===location.href.replace(location.hash,"")||"push"===c.getAttribute("data-ignore")))return c},m=function(a){var b=l(a);b&&(a.preventDefault(),o({url:b.href,hash:b.hash,timeout:b.getAttribute("data-timeout"),transition:b.getAttribute("data-transition")}))},n=function(a){var b,c,h,i,l,m,n,p,q=a.state;if(q&&d[q]){if(l=o.id<q?"forward":"back",j(q,l),h=k(q),i=e[q],h.title&&(document.title=h.title),"back"===l?(n=JSON.parse("back"===l?d.cacheForwardStack:d.cacheBackStack),p=k(n[n.length-1])):p=h,"back"===l&&!p.id)return o.id=q;if(m="back"===l?f[p.transition]:p.transition,!i)return o({id:h.id,url:h.url,title:h.title,timeout:h.timeout,transition:m,ignorePush:!0});if(p.transition){h=v(h,".content",i.cloneNode(!0));for(b in g)g.hasOwnProperty(b)&&(c=document.querySelector(g[b]),h[b]?r(h[b],c):c&&c.parentNode.removeChild(c))}r((h.contents||i).cloneNode(!0),document.querySelector(".content"),m),o.id=q,document.body.offsetHeight}},o=function(a){var c,d=o.xhr;a.container=a.container||a.transition?document.querySelector(".content"):document.body;for(c in g)g.hasOwnProperty(c)&&(a[c]=a[c]||document.querySelector(g[c]));d&&d.readyState<4&&(d.onreadystatechange=b,d.abort()),d=new XMLHttpRequest,d.open("GET",a.url,!0),d.setRequestHeader("X-PUSH","true"),d.onreadystatechange=function(){a._timeout&&clearTimeout(a._timeout),4===d.readyState&&(200===d.status?p(d,a):q(a.url))},o.id||h({id:+new Date,url:window.location.href,title:document.title,timeout:a.timeout,transition:null}),a.timeout&&(a._timeout=setTimeout(function(){d.abort("timeout")},a.timeout)),d.send(),d.readyState&&!a.ignorePush&&i()},p=function(a,b){var c,d,e=w(a,b);if(!e.contents)return u(b.url);if(e.title&&(document.title=e.title),b.transition)for(c in g)g.hasOwnProperty(c)&&(d=document.querySelector(g[c]),e[c]?r(e[c],d):d&&d.parentNode.removeChild(d));r(e.contents,b.container,b.transition,function(){h({id:b.id||+new Date,url:e.url,title:e.title,timeout:b.timeout,transition:b.transition},b.id),s()}),!b.ignorePush&&window._gaq&&_gaq.push(["_trackPageview"]),!b.hash},q=function(a){throw new Error("Could not get: "+a)},r=function(a,b,c,d){var e,f,g;if(c?(e=/in$/.test(c),"fade"===c&&(b.classList.add("in"),b.classList.add("fade"),a.classList.add("fade")),/slide/.test(c)&&(a.classList.add("sliding-in",e?"right":"left"),a.classList.add("sliding"),b.classList.add("sliding")),b.parentNode.insertBefore(a,b)):b?b.innerHTML=a.innerHTML:a.classList.contains("content")?document.body.appendChild(a):document.body.insertBefore(a,document.querySelector(".content")),c||d&&d(),"fade"===c){b.offsetWidth,b.classList.remove("in");var h=function(){b.removeEventListener("webkitTransitionEnd",h),a.classList.add("in"),a.addEventListener("webkitTransitionEnd",i)},i=function(){a.removeEventListener("webkitTransitionEnd",i),b.parentNode.removeChild(b),a.classList.remove("fade"),a.classList.remove("in"),d&&d()};b.addEventListener("webkitTransitionEnd",h)}if(/slide/.test(c)){var j=function(){a.removeEventListener("webkitTransitionEnd",j),a.classList.remove("sliding","sliding-in"),a.classList.remove(g),b.parentNode.removeChild(b),d&&d()};b.offsetWidth,g=e?"right":"left",f=e?"left":"right",b.classList.add(f),a.classList.remove(g),a.addEventListener("webkitTransitionEnd",j)}},s=function(){var a=new CustomEvent("push",{detail:{state:k(o.id)},bubbles:!0,cancelable:!0});window.dispatchEvent(a)},t=function(a){for(var b,c=document.querySelectorAll("a");a&&a!==document;a=a.parentNode)for(b=c.length;b--;)if(c[b]===a)return a},u=function(a){window.history.replaceState(null,"","#"),window.location.replace(a)},v=function(a,b,c){var d,e={};for(d in a)a.hasOwnProperty(d)&&(e[d]=a[d]);return Object.keys(g).forEach(function(a){var b=c.querySelector(g[a]);b&&b.parentNode.removeChild(b),e[a]=b}),e.contents=c.querySelector(b),e},w=function(a,b){var c,d,e={},f=a.responseText;if(e.url=b.url,!f)return e;/<html/i.test(f)?(c=document.createElement("div"),d=document.createElement("div"),c.innerHTML=f.match(/<head[^>]*>([\s\S.]*)<\/head>/i)[0],d.innerHTML=f.match(/<body[^>]*>([\s\S.]*)<\/body>/i)[0]):(c=d=document.createElement("div"),c.innerHTML=f),e.title=c.querySelector("title");var g="innerText"in e.title?"innerText":"textContent";return e.title=e.title&&e.title[g].trim(),b.transition?e=v(e,".content",d):e.contents=d,e};window.addEventListener("touchstart",function(){a=!1}),window.addEventListener("touchmove",function(){a=!0}),window.addEventListener("touchend",m),window.addEventListener("click",function(a){l(a)&&a.preventDefault()}),window.addEventListener("popstate",n),window.PUSH=o}(),!function(){"use strict";var a=function(a){for(var b,c=document.querySelectorAll(".segmented-control .control-item");a&&a!==document;a=a.parentNode)for(b=c.length;b--;)if(c[b]===a)return a};window.addEventListener("touchend",function(b){var c,d,e,f=a(b.target),g="active",h="."+g;if(f&&(c=f.parentNode.querySelector(h),c&&c.classList.remove(g),f.classList.add(g),f.hash&&(e=document.querySelector(f.hash)))){d=e.parentNode.querySelectorAll(h);for(var i=0;i<d.length;i++)d[i].classList.remove(g);e.classList.add(g)}}),window.addEventListener("click",function(b){a(b.target)&&b.preventDefault()})}(),!function(){"use strict";var a,b,c,d,e,f,g,h,i,j,k,l,m,n=function(a){for(var b,c=document.querySelectorAll(".slider > .slide-group");a&&a!==document;a=a.parentNode)for(b=c.length;b--;)if(c[b]===a)return a},o=function(){if("webkitTransform"in c.style){var a=c.style.webkitTransform.match(/translate3d\(([^,]*)/),b=a?a[1]:0;return parseInt(b,10)}},p=function(a){var b=a?0>d?"ceil":"floor":"round";k=Math[b](o()/(m/c.children.length)),k+=a,k=Math.min(k,0),k=Math.max(-(c.children.length-1),k)},q=function(f){if(c=n(f.target)){var k=c.querySelector(".slide");m=k.offsetWidth*c.children.length,l=void 0,j=c.offsetWidth,i=1,g=-(c.children.length-1),h=+new Date,a=f.touches[0].pageX,b=f.touches[0].pageY,d=0,e=0,p(0),c.style["-webkit-transition-duration"]=0}},r=function(h){h.touches.length>1||!c||(d=h.touches[0].pageX-a,e=h.touches[0].pageY-b,a=h.touches[0].pageX,b=h.touches[0].pageY,"undefined"==typeof l&&(l=Math.abs(e)>Math.abs(d)),l||(f=d/i+o(),h.preventDefault(),i=0===k&&d>0?a/j+1.25:k===g&&0>d?Math.abs(a)/j+1.25:1,c.style.webkitTransform="translate3d("+f+"px,0,0)"))},s=function(a){c&&!l&&(p(+new Date-h<1e3&&Math.abs(d)>15?0>d?-1:1:0),f=k*j,c.style["-webkit-transition-duration"]=".2s",c.style.webkitTransform="translate3d("+f+"px,0,0)",a=new CustomEvent("slide",{detail:{slideNumber:Math.abs(k)},bubbles:!0,cancelable:!0}),c.parentNode.dispatchEvent(a))};window.addEventListener("touchstart",q),window.addEventListener("touchmove",r),window.addEventListener("touchend",s)}(),!function(){"use strict";var a={},b=!1,c=!1,d=!1,e=function(a){for(var b,c=document.querySelectorAll(".toggle");a&&a!==document;a=a.parentNode)for(b=c.length;b--;)if(c[b]===a)return a};window.addEventListener("touchstart",function(c){if(c=c.originalEvent||c,d=e(c.target)){var f=d.querySelector(".toggle-handle"),g=d.clientWidth,h=f.clientWidth,i=d.classList.contains("active")?g-h:0;a={pageX:c.touches[0].pageX-i,pageY:c.touches[0].pageY},b=!1}}),window.addEventListener("touchmove",function(e){if(e=e.originalEvent||e,!(e.touches.length>1)&&d){var f=d.querySelector(".toggle-handle"),g=e.touches[0],h=d.clientWidth,i=f.clientWidth,j=h-i;if(b=!0,c=g.pageX-a.pageX,!(Math.abs(c)<Math.abs(g.pageY-a.pageY))){if(e.preventDefault(),0>c)return f.style.webkitTransform="translate3d(0,0,0)";if(c>j)return f.style.webkitTransform="translate3d("+j+"px,0,0)";f.style.webkitTransform="translate3d("+c+"px,0,0)",d.classList[c>h/2-i/2?"add":"remove"]("active")}}}),window.addEventListener("touchend",function(a){if(d){var e=d.querySelector(".toggle-handle"),f=d.clientWidth,g=e.clientWidth,h=f-g,i=!b&&!d.classList.contains("active")||b&&c>f/2-g/2;e.style.webkitTransform=i?"translate3d("+h+"px,0,0)":"translate3d(0,0,0)",d.classList[i?"add":"remove"]("active"),a=new CustomEvent("toggle",{detail:{isActive:i},bubbles:!0,cancelable:!0}),d.dispatchEvent(a),b=!1,d=!1}})}();</script>


 
</head>
<body>