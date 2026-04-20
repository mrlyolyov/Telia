package com.optima.cms.adapter.magnolia.dto.plan;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A labeled string from Magnolia (extensions, feature rows, etc.). JSON uses {@code key}; in Java we call it {@code name}.
 */
public record MagnoliaNamedValue(
		@JsonProperty("key") String name,
		String value) {
}
