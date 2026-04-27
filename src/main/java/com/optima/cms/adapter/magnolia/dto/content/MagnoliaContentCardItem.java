package com.optima.cms.adapter.magnolia.dto.content;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.optima.cms.adapter.magnolia.dto.plan.MagnoliaExtensionRow;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MagnoliaContentCardItem {

	private String id;
	private String content;
	private Boolean active;
	private Integer sortOrder;
	private List<MagnoliaExtensionRow> extension;
}
