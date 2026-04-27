package com.optima.cms.adapter.magnolia.dto.content;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.optima.cms.adapter.magnolia.dto.plan.MagnoliaExtensionRow;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Magnolia delivery {@code content-card/v1} document.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MagnoliaContentCard {

	@JsonProperty("@id")
	private String atId;
	private String id;
	private String name;
	private String title;
	private String description;
	private String page;
	private String cardType;
	private Boolean active;
	private Integer section;
	private String createdAt;
	private String updatedAt;
	private List<MagnoliaContentCardItem> cards;
	private List<MagnoliaExtensionRow> extension;
}
