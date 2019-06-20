sap.ui.define([
	], function () {
		"use strict";

		return {
			/**
			 * @public
			 * @param {boolean} bIsPhone the value to be checked
			 * @returns {string} path to image
			 */
			srcImageValue : function (bIsPhone) {
				var sImageSrc = "";
				if (bIsPhone === false) {
					sImageSrc = "./images/homeImage.jpg";
				} else {
					sImageSrc = "./images/homeImage_small.jpg";
				}
				return sImageSrc;
			},
			/**
			 * Provides a text to indicate the delivery status based on shipped and required dates
			 *
			 * @public
			 * @param {object} oRequiredDate required date of the order
			 * @param {object} oShippedDate shipped date of the order
			 * @returns {string} delivery status text from the resource bundle
			 */
			statusText: function (oStatusId) {
				var oResourceBundle = this.getModel("i18n").getResourceBundle();
	
				if (oStatusId === null) {
					return "None";
				}
	
				// delivery is urgent (takes more than 7 days)
				if (oStatusId === "ORDER_APPROVED") {
					return oResourceBundle.getText("orderApproved");
				} else if (oStatusId === "ORDER_CANCELLED") { //d elivery is too late
					return oResourceBundle.getText("orderCancelled");
				} else if (oStatusId === "ORDER_COMPLETED") { // order completed
					return oResourceBundle.getText("orderCompleted");
				} else { // delivery is in time
					return oResourceBundle.getText("orderNone");
				}
			},
	
			/**
			 * Provides a semantic state to indicate the delivery status based on shipped and required dates
			 *
			 * @public
			 * @param {object} oRequiredDate required date of the order
			 * @param {object} oShippedDate shipped date of the order
			 * @returns {string} semantic state of the order
			 */
			statusState: function (oStatusId) {
				if (oStatusId === null) {
					return "None";
				}
	
				if (oStatusId === "ORDER_APPROVED") { // order approved
					return "Warning";
				} else if (oStatusId === "ORDER_CANCELLED") { // order cancelled
					return "Error";
				} else if (oStatusId === "ORDER_COMPLETED") { // order completed
					return "Success";
				} else { // anything else
					return "None";
				}
			}
		};
	}
);