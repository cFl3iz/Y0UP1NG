/*!
 * UI development toolkit for HTML5 (OpenUI5)
 * (c) Copyright 2009-2018 SAP SE or an SAP affiliate company.
 * Licensed under the Apache License, Version 2.0 - see LICENSE.txt.
 */
sap.ui.define(function(){"use strict";var O={};O.render=function(r,c){var a,h,H,b,u,d;if(!c.getVisible()||!c._getInternalVisible()){return;}a=c.getActions()||[];b=a.length>0;h=(c._getInternalTitleVisible()&&(c.getTitle().trim()!==""));H=h||b;d=c._hasVisibleActions();r.write("<div ");r.writeAttribute("role","region");r.writeControlData(c);r.addClass("sapUxAPObjectPageSubSection");r.addClass("ui-helper-clearfix");r.writeClasses(c);r.writeClasses();r.write(">");if(H){r.write("<div");r.addClass("sapUxAPObjectPageSubSectionHeader");if(!h&&!d){r.addClass("sapUiHidden");}u=c._getUseTitleOnTheLeft();if(u&&c._onDesktopMediaRange()){r.addClass("titleOnLeftLayout");}r.writeAttributeEscaped("id",c.getId()+"-header");r.writeClasses();r.write(">");r.write("<div");if(h){r.writeAttribute("role","heading");r.writeAttribute("aria-level",c._getARIALevel());}r.addClass('sapUxAPObjectPageSubSectionHeaderTitle');if(c.getTitleUppercase()){r.addClass("sapUxAPObjectPageSubSectionHeaderTitleUppercase");}r.writeAttributeEscaped("id",c.getId()+"-headerTitle");r.writeClasses();r.writeAttribute("data-sap-ui-customfastnavgroup",true);r.write(">");if(h){r.writeEscaped(c.getTitle());}r.write("</div>");if(b){r.write("<div");r.addClass('sapUxAPObjectPageSubSectionHeaderActions');r.writeClasses();r.writeAttribute("data-sap-ui-customfastnavgroup",true);r.write(">");a.forEach(r.renderControl);r.write("</div>");}r.write("</div>");}r.write("<div");r.addClass("ui-helper-clearfix");r.addClass("sapUxAPBlockContainer");r.addClass("sapUxAPBlockContainer"+c._getMediaString());r.writeClasses();if(c._isHidden){r.addStyle("display","none");}r.writeStyles();r.write(">");r.renderControl(c._getGrid());r.write("<div");r.addClass("sapUxAPSubSectionSeeMoreContainer");r.writeClasses();r.write(">");r.renderControl(c._getSeeMoreButton());r.write("</div>");r.write("</div>");r.write("</div>");};return O;},true);
