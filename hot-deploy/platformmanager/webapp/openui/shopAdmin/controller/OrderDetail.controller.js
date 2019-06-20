sap.ui.define([
	"sap/ui/demo/toolpageapp/controller/BaseController",
	"sap/ui/model/json/JSONModel",
	"sap/ui/core/routing/History",
	"sap/ui/demo/toolpageapp/model/formatter"
], function (BaseController, JSONModel, History, formatter) {
	"use strict";
	return BaseController.extend("sap.ui.demo.walkthrough.controller.Detail", {
		formatter: formatter,
		
		onInit: function () {
			this._aValidKeys = ["shipping", "processor"];
			var oViewModel = new JSONModel({
				busy : false,
				delay : 0,
				lineItemListTitle : this.getResourceBundle().getText("detailLineItemTableHeading"),
				// Set fixed currency on view model (as the OData service does not provide a currency).
				currency : "CNY",
				// the sum of all items of this order
				totalOrderAmount: 0,
				selectedTab: ""
			});
			var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
			oRouter.getRoute("orderDetail").attachPatternMatched(this._onObjectMatched, this);

			this.setModel(oViewModel, "orderDetailView");
			// this.getOwnerComponent().getModel().metadataLoaded().then(this._onMetadataLoaded.bind(this));
		},
		_onObjectMatched: function (oEvent) {
			this.getView().bindElement({
				path: "/" + oEvent.getParameter("arguments").orderPath,
				model: "orders"
			});
			
			/**
			var oArguments = oEvent.getParameter("arguments");
			this._sObjectId = oArguments.objectId;
			this.getModel().metadataLoaded().then( function() {
				var sObjectPath = this.getModel().createKey("Orders", {
					OrderID :  this._sObjectId
				});
				this._bindView("/" + sObjectPath);
			}.bind(this));
			var oQuery = oArguments["?query"];
			if (oQuery && this._aValidKeys.indexOf(oQuery.tab) >= 0){
				this.getView().getModel("detailView").setProperty("/selectedTab", oQuery.tab);
				this.getRouter().getTargets().display(oQuery.tab);
			} else {
				this.getRouter().navTo("object", {
					objectId: this._sObjectId,
					query: {
						tab: "shipping"
					}
				}, true);
			}
			**/
			
		},
		_onMetadataLoaded : function () {
			// Store original busy indicator delay for the detail view
			var iOriginalViewBusyDelay = this.getView().getBusyIndicatorDelay(),
				oViewModel = this.getModel("detailView"),
				oLineItemTable = this.byId("lineItemsList"),
				iOriginalLineItemTableBusyDelay = oLineItemTable.getBusyIndicatorDelay();

			// Make sure busy indicator is displayed immediately when
			// detail view is displayed for the first time
			oViewModel.setProperty("/delay", 0);
			oViewModel.setProperty("/lineItemTableDelay", 0);

			oLineItemTable.attachEventOnce("updateFinished", function() {
				// Restore original busy indicator delay for line item table
				oViewModel.setProperty("/lineItemTableDelay", iOriginalLineItemTableBusyDelay);
			});

			// Binding the view will set it to not busy - so the view is always busy if it is not bound
			oViewModel.setProperty("/busy", true);
			// Restore original busy indicator delay for the detail view
			oViewModel.setProperty("/delay", iOriginalViewBusyDelay);
		},
		onNavBack: function () {
			var oHistory = History.getInstance();
			var sPreviousHash = oHistory.getPreviousHash();

			if (sPreviousHash !== undefined) {
				window.history.go(-1);
			} else {
				var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
				oRouter.navTo("orders", {}, true);
			}
		}
	});
});