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
	/** Target UI page (required in contracts; may be null if omitted in a payload). */
	private ContentCardPage page;
	/** Card layout / purpose (required in contracts; may be null if omitted in a payload). */
	private ContentCardType cardType;
	private List<ContentCardItem> cards;
	private Boolean active;
	@JsonInclude(JsonInclude.Include.ALWAYS)
	private Integer section;
	private List<Extension> extension;
}
