/*!
 * UI development toolkit for HTML5 (OpenUI5)
 * (c) Copyright 2009-2018 SAP SE or an SAP affiliate company.
 * Licensed under the Apache License, Version 2.0 - see LICENSE.txt.
 */
sap.ui.define(['jquery.sap.global','sap/ui/core/format/NumberFormat','sap/ui/model/SimpleType','sap/ui/model/FormatException','sap/ui/model/ParseException','sap/ui/model/ValidateException'],function(q,N,S,F,P,V){"use strict";var a=S.extend("sap.ui.model.type.Float",{constructor:function(){S.apply(this,arguments);this.sName="Float";}});a.prototype.formatValue=function(v,i){var f=v;if(v==undefined||v==null){return null;}if(this.oInputFormat){f=this.oInputFormat.parse(v);if(f==null){throw new F("Cannot format float: "+v+" has the wrong format");}}switch(this.getPrimitiveType(i)){case"string":return this.oOutputFormat.format(f);case"int":return Math.floor(f);case"float":case"any":return f;default:throw new F("Don't know how to format Float to "+i);}};a.prototype.parseValue=function(v,i){var r,b;switch(this.getPrimitiveType(i)){case"string":r=this.oOutputFormat.parse(v);if(isNaN(r)){b=sap.ui.getCore().getLibraryResourceBundle();throw new P(b.getText("Float.Invalid"));}break;case"int":case"float":r=v;break;default:throw new P("Don't know how to parse Float from "+i);}if(this.oInputFormat){r=this.oInputFormat.format(r);}return r;};a.prototype.validateValue=function(v){if(this.oConstraints){var b=sap.ui.getCore().getLibraryResourceBundle(),c=[],m=[],f=v;if(this.oInputFormat){f=this.oInputFormat.parse(v);}q.each(this.oConstraints,function(n,C){switch(n){case"minimum":if(f<C){c.push("minimum");m.push(b.getText("Float.Minimum",[C]));}break;case"maximum":if(f>C){c.push("maximum");m.push(b.getText("Float.Maximum",[C]));}}});if(c.length>0){throw new V(m.join(" "),c);}}};a.prototype.setFormatOptions=function(f){this.oFormatOptions=f;this._createFormats();};a.prototype._handleLocalizationChange=function(){this._createFormats();};a.prototype._createFormats=function(){var s=this.oFormatOptions.source;this.oOutputFormat=N.getFloatInstance(this.oFormatOptions);if(s){if(q.isEmptyObject(s)){s={groupingEnabled:false,groupingSeparator:",",decimalSeparator:"."};}this.oInputFormat=N.getFloatInstance(s);}};return a;});
