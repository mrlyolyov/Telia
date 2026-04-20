package com.optima.cms.adapter.magnolia.dto.plan;

import lombok.Getter;
import lombok.Setter;

/**
 * Magnolia extension row (attachment or allowance) with optional {@code id}.
 */
@Getter
@Setter
public class MagnoliaExtensionRow {

	private String key;
	private String value;
	private String id;

}
