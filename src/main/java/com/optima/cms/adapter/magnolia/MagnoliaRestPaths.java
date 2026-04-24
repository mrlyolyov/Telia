package com.optima.cms.adapter.magnolia;

/**
 * Magnolia REST paths relative to {@code magnolia.base.url}. Defined in code so endpoints stay
 * versioned with the adapter (not scattered through environment property files).
 */
public record MagnoliaRestPaths(String deliveryPlanCatalog, String nodesPlanProbe) {

	public static MagnoliaRestPaths defaults() {
		return new MagnoliaRestPaths(
				"/.rest/delivery/plan/v1",
				"/.rest/nodes/v1/plan/Gold-5G-Unlimited");
	}
}
