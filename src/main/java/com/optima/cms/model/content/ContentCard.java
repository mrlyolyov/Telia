package com.optima.cms.model.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.optima.cms.model.plan.Extension;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * A content-card document (section on a page: hero, FAQ, promotion, etc.).
 */
@Getter
@Setter
public class ContentCard {

	private String id;
	private String createdAt;
	private String updatedAt;
	private String title;
	private String description;
	private String page;
	private String cardType;
	private List<ContentCardItem> cards;
	private Boolean active;
	@JsonInclude(JsonInclude.Include.ALWAYS)
	private Integer section;
	private List<Extension> extension;
}
