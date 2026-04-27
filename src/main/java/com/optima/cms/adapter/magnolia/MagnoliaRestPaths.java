package com.optima.cms.adapter.magnolia;

/**
 * Magnolia REST paths relative to {@code magnolia.base.url}. Defined in code so delivery endpoints stay
 * versioned with the adapter (not scattered through environment property files).
 */
public record MagnoliaRestPaths(
		String deliveryPlanCatalog,
		String deliveryDeviceCatalog,
		String deliveryContentCardCatalog,
		String deliveryHeaderCatalog,
		String deliveryFooterCatalog,
		String deliveryThemeCatalog,
		String nodesPlanProbe) {

	public static MagnoliaRestPaths defaults() {
		return new MagnoliaRestPaths(
				"/.rest/delivery/plan/v1",
				"/.rest/delivery/device/v1",
				"/.rest/delivery/content-card/v1",
				"/.rest/delivery/header/v1",
				"/.rest/delivery/footer/v1",
				"/.rest/delivery/theme/v1",
				"/.rest/nodes/v1/plan/Gold-5G-Unlimited");
	}
}
