/*!
 * UI development toolkit for HTML5 (OpenUI5)
 * (c) Copyright 2009-2018 SAP SE or an SAP affiliate company.
 * Licensed under the Apache License, Version 2.0 - see LICENSE.txt.
 */
sap.ui.define(['sap/base/log','sap/ui/thirdparty/URI','sap/base/util/now'],function(l,U,n){"use strict";var U=window.URI;function P(){function M(i,s,S,E,C){this.id=i;this.info=s;this.start=S;this.end=E;this.pause=0;this.resume=0;this.duration=0;this.time=0;this.categories=C;this.average=false;this.count=0;this.completeDuration=0;}function m(C){if(!r){return true;}if(!C){return r===null;}for(var i=0;i<r.length;i++){if(C.indexOf(r[i])>-1){return true;}}return false;}function c(C){if(!C){C=["javascript"];}C=typeof C==="string"?C.split(","):C;if(!m(C)){return null;}return C;}function h(f,C){for(var i=0;i<C.length;i++){if(f.categories.indexOf(C[i])>-1){return true;}}return C.length===0;}var a=false,x=XMLHttpRequest,r=null,A=[],o=[],b={},d={};this.getActive=function(){return a;};this.setActive=function(O,C){var E,s;if(!C){C=null;}else if(typeof C==="string"){C=C.split(",");}r=C;if(a===O){return;}a=O;if(a){for(var N in b){this[N]=b[N].bind(this);}b={};E=this.end;s=this.start;XMLHttpRequest=function(){var X=new x(),f=X.open,g;X.open=function(){g=new U(arguments[1],new U(document.baseURI).search("")).href();s(g,"Request for "+g,"xmlhttprequest");X.addEventListener("loadend",E.bind(null,g));f.apply(this,arguments);};return X;};}else{XMLHttpRequest=x;}return a;};b["start"]=function(i,s,C){if(!a){return;}C=c(C);if(!C){return;}var t=n(),f=new M(i,s,t,0,C);if(l.getLevel("sap.ui.Performance")>=4&&window.console&&console.time){console.time(s+" - "+i);}l.info("Performance measurement start: "+i+" on "+t);if(f){d[i]=f;return this.getMeasurement(f.id);}else{return false;}};b["pause"]=function(i){if(!a){return;}var t=n();var f=d[i];if(f&&f.end>0){return false;}if(f&&f.pause==0){f.pause=t;if(f.pause>=f.resume&&f.resume>0){f.duration=f.duration+f.pause-f.resume;f.resume=0;}else if(f.pause>=f.start){f.duration=f.pause-f.start;}}if(f){l.info("Performance measurement pause: "+i+" on "+t+" duration: "+f.duration);return this.getMeasurement(f.id);}else{return false;}};b["resume"]=function(i){if(!a){return;}var t=n();var f=d[i];if(f&&f.pause>0){f.pause=0;f.resume=t;}if(f){l.info("Performance measurement resume: "+i+" on "+t+" duration: "+f.duration);return this.getMeasurement(f.id);}else{return false;}};b["end"]=function(i){if(!a){return;}var t=n();var f=d[i];l.info("Performance measurement end: "+i+" on "+t);if(f&&!f.end){f.end=t;if(f.end>=f.resume&&f.resume>0){f.duration=f.duration+f.end-f.resume;f.resume=0;}else if(f.pause>0){f.pause=0;}else if(f.end>=f.start){if(f.average){f.completeDuration+=(f.end-f.start);f.count++;f.duration=f.completeDuration/f.count;f.start=t;}else{f.duration=f.end-f.start;}}if(f.end>=f.start){f.time=f.end-f.start;}}if(f){if(l.getLevel("sap.ui.Performance")>=4&&window.console&&console.timeEnd){console.timeEnd(f.info+" - "+i);}return this.getMeasurement(i);}else{return false;}};b["clear"]=function(){d={};};b["remove"]=function(i){delete d[i];};b["add"]=function(i,s,S,E,t,D,C){if(!a){return;}C=c(C);if(!C){return false;}var f=new M(i,s,S,E,C);f.time=t;f.duration=D;if(f){d[i]=f;return this.getMeasurement(f.id);}else{return false;}};b["average"]=function(i,s,C){if(!a){return;}C=c(C);if(!C){return;}var f=d[i],t=n();if(!f||!f.average){this.start(i,s,C);f=d[i];f.average=true;}else{if(!f.end){f.completeDuration+=(t-f.start);f.count++;}f.start=t;f.end=0;}return this.getMeasurement(f.id);};this.getMeasurement=function(i){var f=d[i];if(f){var C={};for(var p in f){C[p]=f[p];}return C;}else{return false;}};this.getAllMeasurements=function(C){return this.filterMeasurements(function(f){return f;},C);};this.filterMeasurements=function(){var f,v,i=0,g=[],F=typeof arguments[i]==="function"?arguments[i++]:undefined,C=typeof arguments[i]==="boolean"?arguments[i++]:undefined,j=Array.isArray(arguments[i])?arguments[i]:[];for(var s in d){f=this.getMeasurement(s);v=(C===false&&f.end===0)||(C!==false&&(!C||f.end));if(v&&h(f,j)&&(!F||F(f))){g.push(f);}}return g;};this.registerMethod=function(i,O,s,C){var f=O[s];if(f&&typeof f==="function"){var F=A.indexOf(f)>-1;if(!F){o.push({func:f,obj:O,method:s,id:i});var t=this;O[s]=function(){t.average(i,i+" method average",C);var g=f.apply(this,arguments);t.end(i);return g;};A.push(O[s]);return true;}}else{l.debug(s+" in not a function. Measurement.register failed");}return false;};this.unregisterMethod=function(i,O,s){var f=O[s],g=A.indexOf(f);if(f&&g>-1){O[s]=o[g].func;A.splice(g,1);o.splice(g,1);return true;}return false;};this.unregisterAllMethods=function(){while(o.length>0){var O=o[0];this.unregisterMethod(O.id,O.obj,O.method);}};var e=location.search.match(/sap-ui-measure=([^\&]*)/);if(e&&e[1]){if(e[1]==="true"||e[1]==="x"||e[1]==="X"){this.setActive(true);}else{this.setActive(true,e[1]);}}else{var I=function(){return null;};for(var N in b){this[N]=I;}}}return new P();});
