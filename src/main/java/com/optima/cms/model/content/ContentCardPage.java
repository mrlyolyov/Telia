package com.optima.cms.model.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Which UI page a content card section belongs to (query {@code ?page=} and JSON {@code page}).
 */
public enum ContentCardPage {
	SHOP_HOME("shop-home"),
	DEVICE_GALLERY("device-gallery"),
	DEVICE_DETAILS("device-details"),
	PLAN_GALLERY("plan-gallery"),
	PLAN_DETAILS("plan-details"),
	ADDON_GALLERY("addon-gallery"),
	CUSTOMER_360("customer-360"),
	MY_ACCOUNT("my-account"),
	ORDER_SUMMARY("order-summary");

	private final String wire;

	ContentCardPage(String wire) {
		this.wire = wire;
	}

	@JsonValue
	public String getWire() {
		return wire;
	}

	@JsonCreator
	public static ContentCardPage fromWire(String value) {
		ContentCardPage p = tryParse(value);
		if (p == null && value != null && !value.isBlank()) {
			throw new IllegalArgumentException("Unknown content card page: " + value);
		}
		return p;
	}

	/** Safe parse for HTTP query params: unknown or blank values yield {@code null}. */
	public static ContentCardPage tryParse(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		String v = value.trim();
		for (ContentCardPage p : values()) {
			if (p.wire.equalsIgnoreCase(v)) {
				return p;
			}
		}
		return null;
	}
}
