package com.optima.cms.model.theme;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

/** Neutral palette: fixed {@code common} swatches plus a {@code gray} scale (dynamic keys {@code s50}…{@code s1000}). */
@Getter
@Setter
public class ThemeNeutralColors {

	private ThemeCommonColors common;
	private JsonNode gray;
}
