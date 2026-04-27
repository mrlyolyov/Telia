package com.optima.cms.model.theme;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ThemeFooter {

	private String companyUrl;
	private String copyRightText;
	private List<ThemeLabeledLink> links;
	private List<ThemeSocialLink> socialLinks;
}
