package com.optima.cms.model.theme;

import lombok.Getter;
import lombok.Setter;

/** Background configured via a design token (e.g. {@code mode: color}). */
@Getter
@Setter
public class ThemeTokenBackground {

	private String mode;
	private ThemeColorToken colorToken;
}
