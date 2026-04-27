package com.optima.cms.model.theme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.optima.cms.model.plan.Extension;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Top-level header tab (e.g. {@code support}, {@code my-account}, {@code shop}).
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThemeNavTab {

	private String tab;
	private List<ThemeNavSectionItem> items;
	private ThemeMenuCard menuCard;
	private List<Extension> extension;
	private String id;
}
