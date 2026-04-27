package com.optima.cms.model.theme;

import lombok.Getter;
import lombok.Setter;

/** Semantic token plus resolved color for header/footer text. */
@Getter
@Setter
public class ThemeResolvedToken {

	private String token;
	private String resolved;
}
