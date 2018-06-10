/*!
 * UI development toolkit for HTML5 (OpenUI5)
 * (c) Copyright 2009-2018 SAP SE or an SAP affiliate company.
 * Licensed under the Apache License, Version 2.0 - see LICENSE.txt.
 */
sap.ui.predefine('sap/tnt/InfoLabel',["./library","sap/ui/core/Control","sap/ui/core/library","./InfoLabelRenderer"],function(l,C,a,I){"use strict";var R=l.RenderMode;var T=a.TextDirection;
var b=C.extend("sap.tnt.InfoLabel",{metadata:{interfaces:["sap.ui.core.IFormContent"],library:"sap.tnt",properties:{text:{type:"string",defaultValue:"",bindable:"bindable"},renderMode:{type:"sap.tnt.RenderMode",defaultValue:R.Loose,group:"Appearance"},colorScheme:{type:"int",group:"Misc",defaultValue:7},width:{type:"sap.ui.core.CSSSize",group:"Dimension",defaultValue:null},displayOnly:{type:"boolean",group:"Appearance",defaultValue:false},textDirection:{type:"sap.ui.core.TextDirection",group:"Appearance",defaultValue:T.Inherit}}}});
b.prototype.init=function(){if(sap.ui.getCore().getConfiguration().getAccessibility()&&!I._sAriaText){I._sAriaText=sap.ui.getCore().getLibraryResourceBundle('sap.tnt').getText("INFOLABEL_DEFAULT");I._sAriaTextEmpty=sap.ui.getCore().getLibraryResourceBundle('sap.tnt').getText("INFOLABEL_EMPTY");}};
b.prototype.setText=function(t){t=this.validateProperty("text",t);var v=this.getText();var c=this.$();if(v!==t){this.setProperty("text",t,true);if(c.length){c.find(".sapTntInfoLabelInner").text(t);if(t!==""){c.find(".sapUiPseudoInvisibleText").text(I._sAriaText);}else{c.find(".sapUiPseudoInvisibleText").text(I._sAriaTextEmpty);}}c.toggleClass("sapTntInfoLabelNoText",!t);}return this;};
b.prototype.setColorScheme=function(c){c=this.validateProperty("colorScheme",c);var i=this.getColorScheme();var $=this.$();if(i!==c){if(c>0&&c<10){this.setProperty("colorScheme",c,true);if($.length){$.removeClass("backgroundColor"+i);$.addClass("backgroundColor"+c);}}else{jQuery.sap.log.warning("colorScheme value was not set. It should be between 1 and 9");}}return this;};
b.prototype.getFormDoNotAdjustWidth=function(){return true;};
return b;});
sap.ui.predefine('sap/tnt/InfoLabelRenderer',["jquery.sap.global","./library","sap/ui/core/Renderer","sap/ui/core/library"],function(q,l,R,c){"use strict";var a=l.RenderMode;var T=c.TextDirection;var I={};
I.render=function(r,C){var i=C.getColorScheme(),s=C.getRenderMode(),t=C.getText(),b=C.getTextDirection(),w=C.getWidth(),d=C.getDisplayOnly();r.write("<div");r.writeControlData(C);r.addClass("sapTntInfoLabel");if(s===a.Narrow){r.addClass("sapTntInfoLabelRenderModeNarrow");}if(d){r.addClass("sapTntInfoLabelDisplayOnly");}if(t===""){r.addClass("sapTntInfoLabelNoText");}if(w){r.addStyle("width",w);}r.addClass("backgroundColor"+i);r.writeClasses();r.writeStyles();r.write(">");r.write("<span");r.addClass("sapTntInfoLabelInner");r.writeClasses();if(b!==T.Inherit){r.writeAttribute("dir",b.toLowerCase());}r.write(">");r.writeEscaped(t);r.write("</span>");if(I._sAriaText){r.write("<span class='sapUiPseudoInvisibleText'>");if(t===""){r.writeEscaped(I._sAriaTextEmpty);}else{r.writeEscaped(I._sAriaText);}r.write("</span>");}r.write("</div>");};
return I;},true);
sap.ui.predefine('sap/tnt/NavigationList',['jquery.sap.global','./library','sap/ui/core/Control','sap/m/Popover','sap/ui/core/delegate/ItemNavigation','sap/ui/core/InvisibleText',"./NavigationListRenderer"],function(q,l,C,P,I,a,N){"use strict";
var b=C.extend("sap.tnt.NavigationList",{metadata:{library:"sap.tnt",properties:{width:{type:"sap.ui.core.CSSSize",group:"Dimension"},expanded:{type:"boolean",group:"Misc",defaultValue:true}},defaultAggregation:"items",aggregations:{items:{type:"sap.tnt.NavigationListItem",multiple:true,singularName:"item"}},associations:{ariaDescribedBy:{type:"sap.ui.core.Control",multiple:true,singularName:"ariaDescribedBy"},ariaLabelledBy:{type:"sap.ui.core.Control",multiple:true,singularName:"ariaLabelledBy"},selectedItem:{type:"sap.tnt.NavigationListItem",multiple:false}},events:{itemSelect:{parameters:{item:{type:"sap.ui.core.Item"}}}}}});
b.prototype.init=function(){this._itemNavigation=new I();this._itemNavigation.setCycling(false);this.addEventDelegate(this._itemNavigation);this._itemNavigation.setPageSize(10);this._itemNavigation.setDisabledModifiers({sapnext:["alt","meta"],sapprevious:["alt","meta"]});this._resourceBundle=sap.ui.getCore().getLibraryResourceBundle("sap.ui.core");if(sap.ui.getCore().getConfiguration().getAccessibility()&&!b._sAriaPopupLabelId){b._sAriaPopupLabelId=new a({text:''}).toStatic().getId();}};
b.prototype.onAfterRendering=function(){this._itemNavigation.setRootDomRef(this.getDomRef());this._itemNavigation.setItemDomRefs(this._getDomRefs());if(this._selectedItem){this._selectedItem._select();}};
b.prototype._updateNavItems=function(){this._itemNavigation.setItemDomRefs(this._getDomRefs());};
b.prototype._getDomRefs=function(){var d=[];var c=this.getItems();for(var i=0;i<c.length;i++){q.merge(d,c[i]._getDomRefs());}return d;};
b.prototype._adaptPopoverPositionParams=function(){if(this.getShowArrow()){this._marginLeft=10;this._marginRight=10;this._marginBottom=10;this._arrowOffset=18;this._offsets=["0 -18","18 0","0 18","-18 0"];this._myPositions=["center bottom","begin top","center top","end top"];this._atPositions=["center top","end top","center bottom","begin top"];}else{this._marginTop=0;this._marginLeft=0;this._marginRight=0;this._marginBottom=0;this._arrowOffset=0;this._offsets=["0 0","0 0","0 0","0 0"];this._myPositions=["begin bottom","begin top","begin top","end top"];this._atPositions=["begin top","end top","begin bottom","begin top"];}};
b.prototype.exit=function(){if(this._itemNavigation){this._itemNavigation.destroy();}};
b.prototype._selectItem=function(p){this.fireItemSelect(p);var i=p.item;if(this._selectedItem){this._selectedItem._unselect();}i._select();this._selectedItem=i;this.setAssociation('selectedItem',i,true);};
b.prototype.getSelectedItem=function(){var s=this.getAssociation('selectedItem');if(!s){return null;}return sap.ui.getCore().byId(s);};
b.prototype.setSelectedItem=function(s,c){q.sap.require('sap.tnt.NavigationListItem');var n;if(this._selectedItem){this._selectedItem._unselect();}if(!s){this._selectedItem=null;return sap.ui.core.Control.prototype.setAssociation.call(this,'selectedItem',s,c);}if(typeof s!=='string'&&!(s instanceof sap.tnt.NavigationListItem)){q.sap.log.warning('Type of selectedItem association should be string or instance of sap.tnt.NavigationListItem. New value was not set.');return this;}if(typeof s==='string'){n=sap.ui.getCore().byId(s);}else{n=s;}if(n instanceof sap.tnt.NavigationListItem){n._select();this._selectedItem=n;return sap.ui.core.Control.prototype.setAssociation.call(this,'selectedItem',s,c);}else{q.sap.log.warning('Type of selectedItem association should be a valid NavigationListItem object or ID. New value was not set.');return this;}};
b.prototype._openPopover=function(s,c){var t=this;var d=c.getSelectedItem();if(d&&c.isGroupSelected){d=null;}var p=this._popover=new P({showHeader:false,horizontalScrolling:false,verticalScrolling:true,initialFocus:d,afterClose:function(){if(t._popover){t._popover.destroy();t._popover=null;}},content:c,ariaLabelledBy:[b._sAriaPopupLabelId]}).addStyleClass('sapContrast sapContrastPlus');p._adaptPositionParams=this._adaptPopoverPositionParams;p.openBy(s);};
b.prototype._closePopover=function(){if(this._popover){this._popover.close();}};
return b;},true);
/* the subsequent text seems not to contain a copyright or license statement */
sap.ui.predefine('sap/tnt/NavigationListItem',["jquery.sap.global","./library","sap/ui/core/Item",'sap/ui/core/Icon','./NavigationList','sap/ui/core/Renderer','sap/ui/core/IconPool'],function(q,l,I,a,N,R,b){"use strict";
var c=I.extend("sap.tnt.NavigationListItem",{metadata:{library:"sap.tnt",properties:{icon:{type:"sap.ui.core.URI",group:"Misc",defaultValue:''},expanded:{type:"boolean",group:"Misc",defaultValue:true},hasExpander:{type:"boolean",group:"Misc",defaultValue:true},visible:{type:"boolean",group:"Appearance",defaultValue:true}},defaultAggregation:"items",aggregations:{items:{type:"sap.tnt.NavigationListItem",multiple:true,singularName:"item"},_expandIconControl:{type:"sap.ui.core.Icon",multiple:false,visibility:"hidden"}},events:{select:{parameters:{item:{type:"sap.ui.core.Item"}}}}}});
c.expandIcon='sap-icon://navigation-right-arrow';c.collapseIcon='sap-icon://navigation-down-arrow';
c.prototype._getExpandIconControl=function(){var e=this.getAggregation('_expandIconControl');if(!e){var d=this.getExpanded();e=new a({src:d?c.collapseIcon:c.expandIcon,visible:this.getItems().length>0&&this.getHasExpander(),useIconTooltip:false,tooltip:this._getExpandIconTooltip(!d)}).addStyleClass('sapTntNavLIExpandIcon');this.setAggregation("_expandIconControl",e,true);}return e;};
c.prototype._getExpandIconTooltip=function(e){if(!this.getEnabled()){return'';}var t=e?'Icon.expand':'Icon.collapse';return this.getNavigationList()._resourceBundle.getText(t);};
c.prototype.getLevel=function(){var d=0;var p=this.getParent();if(p.getMetadata().getName()=='sap.tnt.NavigationListItem'){return p.getLevel()+1;}return d;};
c.prototype.getNavigationList=function(){var p=this.getParent();while(p&&p.getMetadata().getName()!='sap.tnt.NavigationList'){p=p.getParent();}return p;};
c.prototype.createPopupList=function(){var n=[],d=this.getNavigationList(),s=d.getSelectedItem(),p,e,f,g=this.getItems();for(var i=0;i<g.length;i++){e=g[i];if(e.getVisible()){f=new c({key:e.getId(),text:e.getText(),textDirection:e.getTextDirection(),enabled:e.getEnabled()});n.push(f);if(s==e){p=f;}}}var h=new c({expanded:true,hasExpander:false,key:this.getId(),text:this.getText(),enabled:this.getEnabled(),textDirection:this.getTextDirection(),items:n});var j=new N({itemSelect:this.onPopupItemSelect.bind(this),items:[h]}).addStyleClass('sapTntNavLIPopup');if(s==this){p=h;j.isGroupSelected=true;}j.setSelectedItem(p);return j;};
c.prototype.onPopupItemSelect=function(e){var i=e.getParameter('item');i=sap.ui.getCore().byId(i.getKey());i._selectItem(e);};
c.prototype._selectItem=function(e){var p={item:this};this.fireSelect(p);var n=this.getNavigationList();n._selectItem(p);};
c.prototype.onkeydown=function(e){if(e.isMarked('subItem')){return;}e.setMarked('subItem');if(this.getLevel()>0){return;}var i=sap.ui.getCore().getConfiguration().getRTL();if((e.shiftKey&&e.which==189)||e.which==q.sap.KeyCodes.NUMPAD_MINUS||(e.which==q.sap.KeyCodes.ARROW_RIGHT&&i)||(e.which==q.sap.KeyCodes.ARROW_LEFT&&!i)){if(this.collapse()){e.preventDefault();e.target=null;}}else if(e.which==q.sap.KeyCodes.NUMPAD_PLUS||(e.shiftKey&&e.which==q.sap.KeyCodes.PLUS)||e.which==q.sap.KeyCodes.ARROW_LEFT&&i||e.which==q.sap.KeyCodes.ARROW_RIGHT&&!i){if(this.expand()){e.preventDefault();e.target=null;}}};
c.prototype.expand=function(d){if(this.getExpanded()||!this.getHasExpander()||this.getItems().length==0||this.getLevel()>0){return;}this.setProperty('expanded',true,true);this.$().find('.sapTntNavLIGroup').attr('aria-expanded',true);var e=this._getExpandIconControl();e.setSrc(c.collapseIcon);e.setTooltip(this._getExpandIconTooltip(false));var $=this.$().find('.sapTntNavLIGroupItems');$.stop(true,true).slideDown(d||'fast',function(){$.toggleClass('sapTntNavLIHiddenGroupItems');});this.getNavigationList()._updateNavItems();return true;};
c.prototype.collapse=function(d){if(!this.getExpanded()||!this.getHasExpander()||this.getItems().length==0||this.getLevel()>0){return;}this.setProperty('expanded',false,true);this.$().find('.sapTntNavLIGroup').attr('aria-expanded',false);var e=this._getExpandIconControl();e.setSrc(c.expandIcon);e.setTooltip(this._getExpandIconTooltip(true));var $=this.$().find('.sapTntNavLIGroupItems');$.stop(true,true).slideUp(d||'fast',function(){$.toggleClass('sapTntNavLIHiddenGroupItems');});this.getNavigationList()._updateNavItems();return true;};
c.prototype.ontap=function(e){if(e.isMarked('subItem')||!this.getEnabled()){return;}e.setMarked('subItem');e.preventDefault();var n=this.getNavigationList();var s=sap.ui.getCore().byId(e.target.id);var d=this.getLevel();if(d==1){var p=this.getParent();if(this.getEnabled()&&p.getEnabled()){this._selectItem(e);}return;}if(n.getExpanded()||this.getItems().length==0){if(!s||s.getMetadata().getName()!='sap.ui.core.Icon'||!s.$().hasClass('sapTntNavLIExpandIcon')){this._selectItem(e);return;}if(this.getExpanded()){this.collapse();}else{this.expand();}}else{var f=this.createPopupList();n._openPopover(this,f);}};
c.prototype.onsapenter=c.prototype.ontap;c.prototype.onsapspace=c.prototype.ontap;
c.prototype.render=function(r,d,i,e){if(!this.getVisible()){return;}if(this.getLevel()===0){this.renderFirstLevelNavItem(r,d,i,e);}else{this.renderSecondLevelNavItem(r,d,i,e);}};
c.prototype.renderGroupItem=function(r,d,i,e){var f=d.getExpanded(),g=this.getExpanded(),t=this.getText(),h,j={level:'1',posinset:i+1,setsize:this._getVisibleItems(d).length};if(f&&this.getItems().length!==0){j.expanded=g;}r.write('<div');r.addClass("sapTntNavLIItem");r.addClass("sapTntNavLIGroup");if(!this.getEnabled()){r.addClass("sapTntNavLIItemDisabled");}else{r.write(' tabindex="-1"');}if(!f){h=this.getTooltip_AsString()||t;if(h){r.writeAttributeEscaped("title",h);}j.label=t;j.role='button';j.haspopup=true;}else{j.role='treeitem';}r.writeAccessibilityState(j);if(d.getExpanded()){h=this.getTooltip_AsString()||t;if(h){r.writeAttributeEscaped("title",h);}r.writeAttributeEscaped("aria-label",t);}r.writeClasses();r.write(">");this._renderIcon(r);if(d.getExpanded()){var k=this._getExpandIconControl();k.setVisible(this.getItems().length>0&&this.getHasExpander());k.setSrc(this.getExpanded()?c.collapseIcon:c.expandIcon);k.setTooltip(this._getExpandIconTooltip(!this.getExpanded()));this._renderText(r);r.renderControl(k);}r.write("</div>");};
c.prototype.renderFirstLevelNavItem=function(r,d,e,f){var g,h=this._getVisibleItems(this),j=h.length,k=this.getExpanded(),m=d.getExpanded();r.write('<li aria-hidden="true" ');r.writeElementData(this);if(this.getEnabled()&&!m){r.write(' tabindex="-1"');}r.write(">");this.renderGroupItem(r,d,e);if(m){r.write('<ul aria-hidden="true" ');r.writeAttribute("role","group");r.addClass("sapTntNavLIGroupItems");if(!k){r.addClass("sapTntNavLIHiddenGroupItems");}r.writeClasses();r.write(">");for(var i=0;i<j;i++){g=h[i];g.render(r,d,i,j);}r.write("</ul>");}r.write("</li>");};
c.prototype.renderSecondLevelNavItem=function(r,d,i,e){var g=this.getParent();r.write('<li');r.writeElementData(this);r.addClass("sapTntNavLIItem");r.addClass("sapTntNavLIGroupItem");if(!this.getEnabled()||!g.getEnabled()){r.addClass("sapTntNavLIItemDisabled");}else{r.write(' tabindex="-1"');}var t=this.getText();var f=this.getTooltip_AsString()||t;if(f){r.writeAttributeEscaped("title",f);}r.writeAccessibilityState({role:'treeitem',level:'2',posinset:i+1,setsize:e});r.writeClasses();r.write(">");this._renderText(r);r.write("</li>");};
c.prototype._renderIcon=function(r){var i=this.getIcon(),d=b.getIconInfo(i);if(i){r.write('<span');r.addClass("sapUiIcon");r.addClass("sapTntNavLIGroupIcon");r.writeAttribute("aria-hidden",true);if(d&&!d.suppressMirroring){r.addClass("sapUiIconMirrorInRTL");}if(d){r.writeAttribute("data-sap-ui-icon-content",d.content);r.addStyle("font-family","'"+d.fontFamily+"'");}r.writeClasses();r.writeStyles();r.write("></span>");}else{r.write('<span class="sapUiIcon sapTntNavLIGroupIcon" aria-hidden="true"></span>');}};
c.prototype._renderText=function(r){r.write('<span');r.addClass("sapMText");r.addClass("sapTntNavLIText");r.addClass("sapMTextNoWrap");r.writeClasses();var t=this.getTextDirection();if(t!==sap.ui.core.TextDirection.Inherit){r.writeAttribute("dir",t.toLowerCase());}var d=R.getTextAlign(sap.ui.core.TextAlign.Begin,t);if(d){r.addStyle("text-align",d);r.writeStyles();}r.write(">");r.writeEscaped(this.getText());r.write("</span>");};
c.prototype._unselect=function(){var $=this.$(),n=this.getNavigationList();if(!n){return;}$.removeClass('sapTntNavLIItemSelected');if(n.getExpanded()){if(this.getLevel()==0){$=$.find('.sapTntNavLIGroup');}$.removeAttr('aria-selected');}else{$.removeAttr('aria-pressed');}};
c.prototype._select=function(){var $=this.$(),n=this.getNavigationList();if(!n){return;}$.addClass('sapTntNavLIItemSelected');if(n.getExpanded()){if(this.getLevel()==0){$=$.find('.sapTntNavLIGroup');}$.attr('aria-selected',true);}else{$.attr('aria-pressed',true);n._closePopover();}};
c.prototype._getDomRefs=function(){var d=[];if(!this.getEnabled()){return d;}var $=this.$();d.push($.find('.sapTntNavLIGroup')[0]);if(this.getExpanded()){var s=$.find('.sapTntNavLIGroupItem');for(var i=0;i<s.length;i++){d.push(s[i]);}}return d;};
c.prototype._getVisibleItems=function(d){var v=[];var i=d.getItems();var e;for(var f=0;f<i.length;f++){e=i[f];if(e.getVisible()){v.push(e);}}return v;};
return c;},true);
/*!
 * UI development toolkit for HTML5 (OpenUI5)
 * (c) Copyright 2009-2018 SAP SE or an SAP affiliate company.
 * Licensed under the Apache License, Version 2.0 - see LICENSE.txt.
 */
sap.ui.predefine('sap/tnt/NavigationListRenderer',['jquery.sap.global','sap/ui/core/Renderer'],function(q,R){"use strict";var N={};
N.render=function(r,c){var a,v,g=c.getItems(),e=c.getExpanded(),b=[];r.write("<ul");r.writeControlData(c);var w=c.getWidth();if(w&&e){r.addStyle("width",w);}r.writeStyles();r.addClass("sapTntNavLI");if(!e){r.addClass("sapTntNavLICollapsed");}r.writeClasses();a=e?'tree':'toolbar';r.writeAttribute("role",a);r.write(">");g.forEach(function(d){if(d.getVisible()){b.push(d);}});b.forEach(function(d,i){d.render(r,c,i,v);});r.write("</ul>");};
return N;},true);
sap.ui.predefine('sap/tnt/SideNavigation',['jquery.sap.global','./library','sap/ui/core/Control','sap/ui/core/ResizeHandler','sap/ui/core/Icon','sap/ui/core/delegate/ScrollEnablement',"./SideNavigationRenderer"],function(q,l,C,R,I,S,a){'use strict';
var b=C.extend('sap.tnt.SideNavigation',{metadata:{library:'sap.tnt',properties:{expanded:{type:'boolean',group:'Misc',defaultValue:true}},defaultAggregation:"item",aggregations:{item:{type:'sap.tnt.NavigationList',multiple:false,bindable:"bindable"},fixedItem:{type:'sap.tnt.NavigationList',multiple:false},footer:{type:'sap.tnt.NavigationList',multiple:false},_topArrowControl:{type:"sap.ui.core.Icon",multiple:false,visibility:"hidden"},_bottomArrowControl:{type:"sap.ui.core.Icon",multiple:false,visibility:"hidden"}},associations:{selectedItem:{type:"sap.tnt.NavigationListItem",multiple:false}},events:{itemSelect:{parameters:{item:{type:'sap.ui.core.Item'}}}}}});
b.prototype.init=function(){this._scroller=new S(this,this.getId()+"-Flexible-Content",{horizontal:false,vertical:true});this.data('sap-ui-fastnavgroup','true',true);};
b.prototype.setAggregation=function(c,o,s){if(o&&o.attachItemSelect){o.attachItemSelect(this._itemSelectionHandler.bind(this));}return sap.ui.base.ManagedObject.prototype.setAggregation.apply(this,arguments);};
b.prototype.setExpanded=function(i){if(this.getExpanded()===i){return this;}this.setProperty('expanded',i,true);if(!this.getDomRef()){return this;}var t=this,$=this.$(),w;if(t._hasActiveAnimation){t._finishAnimation(!i);$.stop();}if(i){t.$().toggleClass('sapTntSideNavigationNotExpanded',!i);if(t.getAggregation('item')){t.getAggregation('item').setExpanded(i);}if(t.getAggregation('fixedItem')){t.getAggregation('fixedItem').setExpanded(i);}}t._hasActiveAnimation=true;w=i?'15rem':'3rem';$.animate({width:w},{duration:300,complete:function(){var i=t.getExpanded();t._finishAnimation(i);}});return this;};
b.prototype._finishAnimation=function(i){if(!this._hasActiveAnimation||!this.getDomRef()){return;}this.$().toggleClass('sapTntSideNavigationNotExpandedWidth',!i);if(!i){this.$().toggleClass('sapTntSideNavigationNotExpanded',!i);if(this.getAggregation('item')){this.getAggregation('item').setExpanded(i);}if(this.getAggregation('fixedItem')){this.getAggregation('fixedItem').setExpanded(i);}}this.$().css('width','');this._hasActiveAnimation=false;this._toggleArrows();};
b.prototype.onBeforeRendering=function(){var s=this.getSelectedItem();if(s){this.setSelectedItem(s,true);}this._deregisterControl();};
b.prototype.onAfterRendering=function(){this._ResizeHandler=R.register(this.getDomRef(),this._toggleArrows.bind(this));this._toggleArrows();};
b.prototype.setSelectedItem=function(s,c){var n=this.getAggregation('item');var f=this.getAggregation('fixedItem');var d;if(!s){if(n.setSelectedItem){n.setSelectedItem(null,true);}if(f.setSelectedItem){f.setSelectedItem(null,true);}}if(typeof s==='string'){d=sap.ui.getCore().byId(s);}else{d=s;}var e=d&&d.getNavigationList&&d.getNavigationList()===n;var g=d&&d.getNavigationList&&d.getNavigationList()===f;if(e){n.setSelectedItem(d,c);if(f){f.setSelectedItem(null,true);}}if(g){f.setSelectedItem(d,c);n.setSelectedItem(null,true);}return sap.ui.core.Control.prototype.setAssociation.call(this,'selectedItem',d,true);};
b.prototype.exit=function(){if(this._scroller){this._scroller.destroy();this._scroller=null;}this._deregisterControl();};
b.prototype._itemSelectionHandler=function(e){var c=e.getSource().getId();var i=this.getAggregation('item');var f=this.getAggregation('fixedItem');var d=e.getParameter('item');if(i&&f&&c===i.getId()){f.setSelectedItem(null);}if(i&&f&&c===f.getId()){i.setSelectedItem(null);}sap.ui.core.Control.prototype.setAssociation.call(this,'selectedItem',d,true);this.fireItemSelect({item:d});};
b.prototype._deregisterControl=function(){if(this._ResizeHandler){R.deregister(this._ResizeHandler);this._ResizeHandler=null;}};
b.prototype._getTopArrowControl=function(){var i=this.getAggregation('_topArrowControl');var t=this;if(!i){i=new I({src:'sap-icon://navigation-up-arrow',noTabStop:true,useIconTooltip:false,tooltip:'',press:this._arrowPress.bind(t)}).addStyleClass('sapTntSideNavigationScrollIcon sapTntSideNavigationScrollIconUp');this.setAggregation("_topArrowControl",i,true);}return i;};
b.prototype._getBottomArrowControl=function(){var i=this.getAggregation('_bottomArrowControl');var t=this;if(!i){i=new I({src:'sap-icon://navigation-down-arrow',noTabStop:true,useIconTooltip:false,tooltip:'',press:this._arrowPress.bind(t)}).addStyleClass('sapTntSideNavigationScrollIcon sapTntSideNavigationScrollIconDown');this.setAggregation("_bottomArrowControl",i,true);}return i;};
b.prototype._toggleArrows=function(){var d=this.getDomRef();if(!d){return;}var s=this.$('Flexible')[0];var c=this.$('Flexible-Content')[0];var i=this.getExpanded();if(this._hasActiveAnimation){d.querySelector('.sapTntSideNavigationScrollIconUp').style.display='none';d.querySelector('.sapTntSideNavigationScrollIconDown').style.display='none';return;}if((c.offsetHeight>s.offsetHeight)&&!i){d.querySelector('.sapTntSideNavigationScrollIconUp').style.display='block';d.querySelector('.sapTntSideNavigationScrollIconDown').style.display='block';d.querySelector('.sapTntSideNavigationScrollIconDown').classList.remove('sapTntSideNavigationScrollIconDisabled');}else{d.querySelector('.sapTntSideNavigationScrollIconUp').style.display='none';d.querySelector('.sapTntSideNavigationScrollIconDown').style.display='none';}};
b.prototype._arrowPress=function(e){e.preventDefault();var s=document.getElementById(e.oSource.sId);var i=s.classList.contains('sapTntSideNavigationScrollIconDown')?true:false;var $=this.$('Flexible');var c=i?40:-40;$[0].scrollTop+=c;};
return b;},true);
sap.ui.predefine('sap/tnt/SideNavigationRenderer',[],function(){'use strict';var S={};
S.render=function(r,c){this.startSideNavigation(r,c);this.renderArrowUp(r,c);this.renderItem(r,c);this.renderArrowDown(r,c);this.renderFixedItem(r,c);this.renderFooter(r,c);this.endSideNavigation(r,c);};
S.startSideNavigation=function(r,c){var i=c.getAggregation('item');var f=c.getAggregation('fixedItem');var a=c.getExpanded();r.write('<div');r.writeControlData(c);r.writeAttribute("role",'navigation');r.addClass('sapTntSideNavigation');r.addClass("sapContrast sapContrastPlus");if(!a){r.addClass('sapTntSideNavigationNotExpanded');r.addClass('sapTntSideNavigationNotExpandedWidth');}if(!a&&i){i.setExpanded(false);}if(!a&&f){f.setExpanded(false);}r.writeClasses();r.write('>');};
S.endSideNavigation=function(r,c){r.write('</div>');};
S.renderArrowUp=function(r,c){r.renderControl(c._getTopArrowControl());};
S.renderArrowDown=function(r,c){r.renderControl(c._getBottomArrowControl());};
S.renderItem=function(r,c){var i=c.getAggregation('item');r.write('<div id="'+c.getId()+'-Flexible" tabindex="-1" class="sapTntSideNavigationFlexible sapTntSideNavigationVerticalScrolling">');r.write('<div id="'+c.getId()+'-Flexible-Content" class="sapTntSideNavigationFlexibleContent">');r.renderControl(i);r.write('</div></div>');};
S.renderFixedItem=function(r,c){var f=c.getAggregation('fixedItem');if(f===null){return;}if(f.getExpanded()===false){f.setExpanded(false);}r.write('<div class="sapTntSideNavigationSeparator" role="separator" aria-orientation="horizontal"></div>');r.write('<div class="sapTntSideNavigationFixed">');r.renderControl(f);r.write('</div>');};
S.renderFooter=function(r,c){if(c.getAggregation('footer')){r.write('<footer class="sapTntSideNavigationFooter">');r.renderControl(c.getAggregation('footer'));r.write('</footer>');}};
return S;},true);
sap.ui.predefine('sap/tnt/ToolHeader',['jquery.sap.global','./library','sap/ui/core/Control','sap/m/OverflowToolbar','sap/m/OverflowToolbarAssociativePopover',"./ToolHeaderRenderer"],function(q,l,C,O,a,T){"use strict";
var b=O.extend("sap.tnt.ToolHeader",{metadata:{library:"sap.tnt",properties:{},aggregations:{}}});
b.prototype.init=function(){O.prototype.init.apply(this,arguments);this.addStyleClass('sapTntToolHeader sapContrast sapContrastPlus');};
b.prototype._getPopover=function(){var p;if(!this.getAggregation("_popover")){p=new a(this.getId()+"-popover",{showHeader:false,showArrow:sap.ui.Device.system.phone?false:true,modal:false,horizontalScrolling:sap.ui.Device.system.phone?false:true,contentWidth:sap.ui.Device.system.phone?"100%":"auto"}).addStyleClass('sapTntToolHeaderPopover sapContrast sapContrastPlus');p.oControlsManager._preProcessSapMButton=this._preProcessPopoverControlsSapMButton.bind(p.oControlsManager);if(sap.ui.Device.system.phone){p.attachBeforeOpen(this._shiftPopupShadow,this);p.attachAfterOpen(this._shiftPopupShadow,this);}p.attachAfterClose(this._popOverClosedHandler,this);this.setAggregation("_popover",p,true);}return this.getAggregation("_popover");};
b.prototype._preProcessPopoverControlsSapMButton=function(c){this._mControlsCache[c.getId()]={buttonType:c.getType()};if(c.getIcon()){c.addStyleClass("sapMOTAPButtonWithIcon");}else{c.addStyleClass("sapMOTAPButtonNoIcon");}c.attachEvent("_change",this._onSapMButtonUpdated,this);};
b.prototype._getBestActionSheetPlacement=function(){return sap.m.PlacementType.Bottom;};
return b;},true);
sap.ui.predefine('sap/tnt/ToolHeaderRenderer',['sap/ui/core/Renderer','sap/m/OverflowToolbarRenderer','sap/m/BarInPageEnabler'],function(R,O,B){"use strict";var T=R.extend(O);
T.renderBarContent=function(r,t){var o=false;var i;t._getVisibleContent().forEach(function(c){i=c.getMetadata().getName()=='sap.tnt.ToolHeaderUtilitySeparator';if(!o&&i&&t._getOverflowButtonNeeded()){T.renderOverflowButton(r,t);o=true;}B.addChildClassTo(c,t);r.renderControl(c);});if(!o&&t._getOverflowButtonNeeded()){T.renderOverflowButton(r,t);}};
return T;},true);
sap.ui.predefine('sap/tnt/ToolHeaderUtilitySeparator',['jquery.sap.global','./library','sap/ui/core/Control'],function(q,l,C){"use strict";
var T=C.extend("sap.tnt.ToolHeaderUtilitySeparator",{
metadata:{library:"sap.tnt",properties:{}},
renderer:{render:function(){}}
});
return T;},true);
sap.ui.predefine('sap/tnt/ToolPage',['./library','sap/ui/core/Control','sap/ui/Device','sap/ui/core/ResizeHandler',"./ToolPageRenderer"],function(l,C,D,R,T){'use strict';
var a=C.extend('sap.tnt.ToolPage',{metadata:{library:'sap.tnt',properties:{sideExpanded:{type:'boolean',group:'Misc',defaultValue:true}},aggregations:{header:{type:'sap.tnt.ToolHeader',multiple:false},sideContent:{type:'sap.tnt.SideNavigation',multiple:false},mainContents:{type:'sap.ui.core.Control',multiple:true,singularName:'mainContent'}},events:{}}});
a.prototype.toggleSideContentMode=function(){return this.setSideExpanded(!this.getSideExpanded());};
a.prototype.setSideExpanded=function(i){var s=this.getAggregation('sideContent');var d=this.getDomRef();this.setProperty('sideExpanded',i,true);if(s){var n=D.system.phone?true:i;s.setExpanded(n);}if(!d){return this;}if(i){d.querySelector('.sapTntToolPageContentWrapper').classList.remove('sapTntToolPageAsideCollapsed');}else{d.querySelector('.sapTntToolPageContentWrapper').classList.add('sapTntToolPageAsideCollapsed');}return this;};
a.prototype.onBeforeRendering=function(){this._deregisterControl();};
a.prototype.onAfterRendering=function(){this._ResizeHandler=R.register(this.getDomRef(),this._mediaQueryHandler.bind(this));this._updateLastMediaQuery();};
a.prototype.exit=function(){this._deregisterControl();};
a.prototype._deregisterControl=function(){if(this._ResizeHandler){R.deregister(this._ResizeHandler);this._ResizeHandler=null;}};
a.prototype._mediaQueryHandler=function(){var s=this.getAggregation('sideContent');if(s===null){return;}this._currentMediaQuery=this._getDeviceAsString();if(this._getLastMediaQuery()===this._currentMediaQuery){return;}switch(this._currentMediaQuery){case'Combi':this.setSideExpanded(true);break;case'Tablet':this.setSideExpanded(false);break;case'Phone':this.setSideExpanded(false);s.setExpanded(true);break;default:this.setSideExpanded(true);break;}this._updateLastMediaQuery();};
a.prototype._getLastMediaQuery=function(){return this._lastMediaQuery;};
a.prototype._updateLastMediaQuery=function(){this._lastMediaQuery=this._getDeviceAsString();return this;};
a.prototype._getDeviceAsString=function(){if(D.system.combi){return'Combi';}if(D.system.phone){return'Phone';}if(D.system.tablet){return'Tablet';}return'Desktop';};
return a;},true);
sap.ui.predefine('sap/tnt/ToolPageRenderer',[],function(){'use strict';var T={};
T.render=function(r,c){var h=c.getAggregation('header');r.write('<div');r.writeControlData(c);r.addClass('sapTntToolPage');if(h){r.addClass('sapTntToolPageWithHeader');}r.writeClasses();r.write('>');if(h){r.write("<header>");r.write('<div id="'+c.getId()+'-header" class="sapTntToolPageHeader">');r.renderControl(h);r.write('</div>');r.write("</header>");}this.renderContentWrapper(r,c);r.write('</div>');};
T.renderContentWrapper=function(r,c){var i=sap.ui.Device.system.desktop;r.write('<div class="sapTntToolPageContentWrapper');if(!i||!c.getSideExpanded()){r.write(' sapTntToolPageAsideCollapsed');}r.write('">');this.renderAsideContent(r,c);this.renderMainContent(r,c);r.write('</div>');};
T.renderAsideContent=function(r,c){var i=sap.ui.Device.system.desktop;var s=c.getAggregation('sideContent');var a=c.getSideExpanded();r.write('<aside id="'+c.getId()+'-aside" class="sapTntToolPageAside">');r.write('<div class="sapTntToolPageAsideContent">');if(s&&s.getExpanded()!==a){s.setExpanded(a);}if(!i){c.setSideExpanded(false);}r.renderControl(s);r.write('</div>');r.write('</aside>');};
T.renderMainContent=function(r,c){var m=c.getAggregation('mainContents');if(m){r.write('<div id="'+c.getId()+'-main" class="sapTntToolPageMain">');r.write('<div class="sapTntToolPageMainContent">');r.write('<div class="sapTntToolPageMainContentWrapper">');m.forEach(r.renderControl);r.renderControl();r.write('</div>');r.write('</div>');r.write('</div>');}};
return T;},true);
sap.ui.predefine('sap/tnt/library',['jquery.sap.global','sap/ui/core/library','sap/m/library'],function(q){'use strict';sap.ui.getCore().initLibrary({name:'sap.tnt',version:'1.54.5',dependencies:['sap.ui.core','sap.m'],types:[],interfaces:[],controls:['sap.tnt.NavigationList','sap.tnt.ToolHeaderUtilitySeparator','sap.tnt.ToolHeader','sap.tnt.SideNavigation','sap.tnt.ToolPage','sap.tnt.InfoLabel'],elements:["sap.tnt.NavigationListItem"]});sap.tnt.RenderMode={Narrow:"Narrow",Loose:"Loose"};return sap.tnt;});
jQuery.sap.registerPreloadedModules({
"name":"sap/tnt/library-preload",
"version":"2.0",
"modules":{
	"sap/tnt/manifest.json":'{"_version":"1.9.0","sap.app":{"id":"sap.tnt","type":"library","embeds":[],"applicationVersion":{"version":"1.54.5"},"title":"SAPUI5 library with responsive controls.","description":"SAPUI5 library with responsive controls.","ach":"CA-UI5-CTR","resources":"resources.json","offline":true},"sap.ui":{"technology":"UI5","supportedThemes":["base","sap_hcb"]},"sap.ui5":{"dependencies":{"minUI5Version":"1.54","libs":{"sap.ui.core":{"minVersion":"1.54.5"},"sap.m":{"minVersion":"1.54.5"}}},"library":{"i18n":"messagebundle.properties","content":{"controls":["sap.tnt.NavigationList","sap.tnt.ToolHeaderUtilitySeparator","sap.tnt.ToolHeader","sap.tnt.SideNavigation","sap.tnt.ToolPage","sap.tnt.InfoLabel"],"elements":["sap.tnt.NavigationListItem"],"types":[],"interfaces":[]}}}}'
}});
//# sourceMappingURL=library-preload.js.map