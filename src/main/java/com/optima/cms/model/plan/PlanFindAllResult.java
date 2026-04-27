package com.optima.cms.model.plan;

import java.util.List;

/**
 * Result of a plan catalog query, including an optional warning when requested {@code externalId}
 * values are missing from the returned set.
 */
public record PlanFindAllResult(List<Plan> plans, String warning) {
}
