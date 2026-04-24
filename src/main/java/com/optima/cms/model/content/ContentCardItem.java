package com.optima.cms.model.content;

import com.optima.cms.model.plan.Extension;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * One HTML/content block inside a {@link ContentCard#getCards()} list.
 */
@Getter
@Setter
public class ContentCardItem {

	private String content;
	private Boolean active;
	private Integer sortOrder;
	private List<Extension> extension;
	private String id;
}
