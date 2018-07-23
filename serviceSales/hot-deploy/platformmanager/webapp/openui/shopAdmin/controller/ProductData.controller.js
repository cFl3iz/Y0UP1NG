sap.ui.define([
	'sap/ui/demo/toolpageapp/controller/BaseController',
	'sap/m/MessageToast',
	'sap/ui/model/json/JSONModel',
	'sap/ui/model/Filter',
	'sap/ui/model/FilterOperator',
	"sap/ui/demo/toolpageapp/model/formatter"
], function (BaseController, MessageToast, JSONModel, Filter, FilterOperator, formatter) {
	"use strict";
	return BaseController.extend("sap.ui.demo.toolpageapp.controller.ProductData", {

		formatter: formatter,

		onInit: function () {
			var oViewModel = new JSONModel({
					currentUser: "Administrator",
					lastLogin: new Date(Date.now() - 86400000)
				});

			this.setModel(oViewModel, "view");
			MessageToast.show("OrderHeaders was pressed");
		},

		onFilterOrders : function (oEvent) {

			// build filter array
			var aFilter = [];
			var sQuery = oEvent.getParameter("query");
			if (sQuery) {
				aFilter.push(new Filter("orderId", FilterOperator.Contains, sQuery));
			}

			// filter binding
			var oList = this.byId("orderList");
			var oBinding = oList.getBinding("items");
			oBinding.filter(aFilter);
		},
		
		onPress: function (oEvent) {
			var oItem = oEvent.getSource();
			var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
			oRouter.navTo("orderDetail", {
				orderPath: oItem.getBindingContext("orders").getPath().substr(1)
			});
		},
		
		onSavePressed: function () {
			MessageToast.show("Save was pressed");
		},

		onCancelPressed: function () {
			MessageToast.show("Cancel was pressed");
		},
		onNavButtonPress: function  () {
			this.getOwnerComponent().myNavBack();
		}
	});
});