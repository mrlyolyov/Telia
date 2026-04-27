package com.optima.cms.model.theme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * Tenant / brand theme configuration (logos, palette, header/footer chrome, nav structure).
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThemeOverride {

	private String id;
	private String createdAt;
	private String updatedAt;
	private String name;
	private Boolean active;

	private ThemeMediaAsset logo;
	private ThemeTokenBackground headerBackground;
	private ThemePalette palette;
	private ThemeMediaAsset simCardWithLogo;
	private ThemeMediaAsset simLearnMore;
	private ThemeFooter footer;
	private ThemeTokenBackground footerBackground;
	private ThemeResolvedToken footerText;

	private ThemeHeader header;
	private ThemeResolvedToken headerText;

	private String buttonShape;
	private String iconButtonShape;
}
