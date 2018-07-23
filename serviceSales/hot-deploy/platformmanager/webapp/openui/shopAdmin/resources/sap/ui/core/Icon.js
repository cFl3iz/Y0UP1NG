/*!
 * UI development toolkit for HTML5 (OpenUI5)
 * (c) Copyright 2009-2018 SAP SE or an SAP affiliate company.
 * Licensed under the Apache License, Version 2.0 - see LICENSE.txt.
 */
sap.ui.define(['jquery.sap.global','../Device','./Control','./IconPool','./InvisibleText','./library',"./IconRenderer",'jquery.sap.keycodes'],function(q,D,C,I,a,l,b){"use strict";var c=l.IconColor;var d=C.extend("sap.ui.core.Icon",{metadata:{interfaces:["sap.ui.core.IFormContent"],library:"sap.ui.core",designtime:"sap/ui/core/designtime/Icon.designtime",properties:{src:{type:"sap.ui.core.URI",group:"Data",defaultValue:null},size:{type:"sap.ui.core.CSSSize",group:"Dimension",defaultValue:null},color:{type:"string",group:"Appearance",defaultValue:null},hoverColor:{type:"string",group:"Appearance",defaultValue:null},activeColor:{type:"string",group:"Appearance",defaultValue:null},width:{type:"sap.ui.core.CSSSize",group:"Dimension",defaultValue:null},height:{type:"sap.ui.core.CSSSize",group:"Dimension",defaultValue:null},backgroundColor:{type:"string",group:"Appearance",defaultValue:null},hoverBackgroundColor:{type:"string",group:"Appearance",defaultValue:null},activeBackgroundColor:{type:"string",group:"Appearance",defaultValue:null},decorative:{type:"boolean",group:"Accessibility",defaultValue:true},useIconTooltip:{type:"boolean",group:"Accessibility",defaultValue:true},alt:{type:"string",group:"Accessibility",defaultValue:null},noTabStop:{type:"boolean",group:"Accessibility",defaultValue:false}},aggregations:{_invisibleText:{type:"sap.ui.core.InvisibleText",multiple:false,visibility:"hidden"}},associations:{ariaLabelledBy:{type:"sap.ui.core.Control",multiple:true,singularName:"ariaLabelledBy"}},events:{press:{}}}});d.prototype[D.support.touch?"ontouchstart":"onmousedown"]=function(e){if(this.hasListeners("press")){e.setMarked();}var A=this.getActiveColor(),s=this.getActiveBackgroundColor(),i;if(A||s){if(!e.targetTouches||(e.targetTouches&&e.targetTouches.length===1)){i=this.$();i.addClass("sapUiIconActive");if(A){this._addColorClass(A,"color");}if(s){this._addColorClass(s,"background-color");}}}};d.prototype[D.support.touch?"ontouchend":"onmouseup"]=function(e){if(!e.targetTouches||(e.targetTouches&&e.targetTouches.length===0)){this.$().removeClass("sapUiIconActive");this._restoreColors();}};d.prototype.onmouseover=function(){var h=this.getHoverColor(),H=this.getHoverBackgroundColor();if(h){this._addColorClass(h,"color");}if(H){this._addColorClass(H,"background-color");}};d.prototype.onmouseout=function(){this._restoreColors();};d.prototype[D.support.touch&&!D.system.desktop?"ontap":"onclick"]=function(e){if(this.hasListeners("press")){e.setMarked();}this.firePress({});};d.prototype.onkeydown=function(e){if(e.which===q.sap.KeyCodes.SPACE||e.which===q.sap.KeyCodes.ENTER){e.preventDefault();var i=this.$(),A=this.getActiveColor(),s=this.getActiveBackgroundColor();i.addClass("sapUiIconActive");if(A){this._addColorClass(A,"color");}if(s){this._addColorClass(s,"background-color");}}};d.prototype.onkeyup=function(e){if(e.which===q.sap.KeyCodes.SPACE||e.which===q.sap.KeyCodes.ENTER){this.$().removeClass("sapUiIconActive");this._restoreColors();this.firePress({});}};d.prototype._restoreColors=function(){this._addColorClass(this.getColor()||"","color");this._addColorClass(this.getBackgroundColor()||"","background-color");};d.prototype.setSrc=function(s){var i=I.getIconInfo(s),$=this.$(),e,t,u,L,o;this.setProperty("src",s,!!i);if(i&&$.length){$.css("font-family",i.fontFamily);$.attr("data-sap-ui-icon-content",i.content);$.toggleClass("sapUiIconMirrorInRTL",!i.suppressMirroring);t=this.getTooltip_AsString();L=this.getAriaLabelledBy();u=this.getUseIconTooltip();e=this._getIconLabel();if(t||(u&&i.text)){$.attr("title",t||i.text);}else{$.attr("title",null);}if(L.length===0){if(e){$.attr("aria-label",e);}else{$.attr("aria-label",null);}}else{o=this.getAggregation("_invisibleText");if(o){o.setText(e);}}}return this;};d.prototype.setWidth=function(w){this.setProperty("width",w,true);this.$().css("width",w);return this;};d.prototype.setHeight=function(h){this.setProperty("height",h,true);this.$().css({"height":h,"line-height":h});return this;};d.prototype.setSize=function(s){this.setProperty("size",s,true);this.$().css("font-size",s);return this;};d.prototype.setColor=function(s){this.setProperty("color",s,true);this._addColorClass(s,"color");return this;};d.prototype._addColorClass=function(s,e){var i=this.$(),t=this;var f="";if(e==="color"){f="sapUiIconColor";}else if(e==="background-color"){f="sapUiIconBGColor";}else{return;}q.each(c,function(p,P){t.removeStyleClass(f+P);});if(s in c){i.css(e,"");this.addStyleClass(f+s);}else{i.css(e,s);}};d.prototype.setActiveColor=function(s){return this.setProperty("activeColor",s,true);};d.prototype.setHoverColor=function(s){return this.setProperty("hoverColor",s,true);};d.prototype.setBackgroundColor=function(s){this.setProperty("backgroundColor",s,true);this._addColorClass(s,"background-color");return this;};d.prototype.setActiveBackgroundColor=function(s){return this.setProperty("activeBackgroundColor",s,true);};d.prototype.setHoverBackgroundColor=function(s){return this.setProperty("hoverBackgroundColor",s,true);};d.prototype.attachPress=function(){var m=Array.prototype.slice.apply(arguments);m.unshift("press");C.prototype.attachEvent.apply(this,m);if(this.hasListeners("press")){this.$().toggleClass("sapUiIconPointer",true).attr({role:"button",tabindex:this.getNoTabStop()?undefined:0});}return this;};d.prototype.detachPress=function(){var m=Array.prototype.slice.apply(arguments);m.unshift("press");C.prototype.detachEvent.apply(this,m);if(!this.hasListeners("press")){this.$().toggleClass("sapUiIconPointer",false).attr({role:this.getDecorative()?"presentation":"img"}).removeAttr("tabindex");}return this;};d.prototype._getOutputTitle=function(){var i=I.getIconInfo(this.getSrc()),t=this.getTooltip_AsString(),u=this.getUseIconTooltip();if(t||(u&&i&&i.text)){return t||i.text;}};d.prototype._getIconLabel=function(){var i=I.getIconInfo(this.getSrc()),A=this.getAlt(),t=this.getTooltip_AsString(),u=this.getUseIconTooltip(),L=A||t||(u&&i&&(i.text||i.name)),o=this._getOutputTitle();if(L&&L!==o){return L;}};d.prototype._createInvisibleText=function(t){var i=this.getAggregation("_invisibleText");if(!i){i=new a(this.getId()+"-label",{text:t});this.setAggregation("_invisibleText",i,true);}else{i.setProperty("text",t,true);}return i;};d.prototype._getAccessibilityAttributes=function(){var L=this.getAriaLabelledBy(),A={},i=this._getIconLabel(),o;if(this.getDecorative()){A.role="presentation";A.hidden="true";}else{if(this.hasListeners("press")){A.role="button";}else{A.role="img";}}if(L.length>0){if(i){o=this._createInvisibleText(i);L.push(o.getId());}A.labelledby=L.join(" ");}else if(i){A.label=i;}return A;};d.prototype.getAccessibilityInfo=function(){if(this.getDecorative()){return null;}var h=this.hasListeners("press");var i=I.getIconInfo(this.getSrc());return{role:h?"button":"img",type:sap.ui.getCore().getLibraryResourceBundle("sap.ui.core").getText(h?"ACC_CTR_TYPE_BUTTON":"ACC_CTR_TYPE_IMAGE"),description:this.getAlt()||this.getTooltip_AsString()||(i?i.text||i.name:""),focusable:h};};return d;});
