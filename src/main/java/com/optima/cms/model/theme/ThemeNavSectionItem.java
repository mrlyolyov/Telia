package com.optima.cms.model.theme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.optima.cms.model.plan.Extension;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * One column under a nav tab (e.g. “Guided Assistant”) with nested links.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThemeNavSectionItem {

	private String mainItem;
	@JsonProperty("childitems")
	private List<ThemeLabeledLink> childItems;
	private List<Extension> extension;
	private String id;
}
