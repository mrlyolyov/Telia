package com.optima.cms.model.theme;

import lombok.Getter;
import lombok.Setter;

/**
 * Brand / neutral / feedback color system for a theme override.
 */
@Getter
@Setter
public class ThemePalette {

	private String fontRole;
	private ThemeBrandColors brand;
	private ThemeNeutralColors neutral;
	private ThemeFeedbackColors feedback;
}
