package com.optima.cms.model.theme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

/**
 * Per-tab mega-menu card (structured copy or Lexical {@code customContent} when {@code mode} is {@code custom}).
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThemeMenuCard {

	private Boolean enabled;
	private String mode;
	private String title;
	private String description;
	private ThemeMenuCardButton button;
	/** Lexical (or similar) rich document; shape varies by editor version. */
	private JsonNode customContent;
}
