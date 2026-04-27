package com.optima.cms.model.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Card layout / purpose (JSON {@code cardType}).
 */
public enum ContentCardType {
	DISCOUNT("discount"),
	OFFER("offer"),
	PROMOTION("promotion"),
	HERO_SECTION("hero-section"),
	USP("usp"),
	HELP_SECTION("help-section"),
	QUICK_ACCESS("quick-access"),
	TOP_ACTIONS("top-actions"),
	SHOPPING_CATEGORIES("shopping-categories"),
	MORE_INFO("more-info"),
	NEXT_STEPS("next-steps"),
	MY_PROFILE("my-profile");

	private final String wire;

	ContentCardType(String wire) {
		this.wire = wire;
	}

	@JsonValue
	public String getWire() {
		return wire;
	}

	@JsonCreator
	public static ContentCardType fromWire(String value) {
		ContentCardType t = tryParse(value);
		if (t == null && value != null && !value.isBlank()) {
			throw new IllegalArgumentException("Unknown content card type: " + value);
		}
		return t;
	}

	public static ContentCardType tryParse(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		String v = value.trim();
		for (ContentCardType t : values()) {
			if (t.wire.equalsIgnoreCase(v)) {
				return t;
			}
		}
		return null;
	}
}
