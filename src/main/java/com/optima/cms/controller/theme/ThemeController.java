package com.optima.cms.controller.theme;

import com.fasterxml.jackson.databind.JsonNode;
import com.optima.cms.model.theme.ThemeOverrideResponse;
import com.optima.cms.service.ThemeOverrideService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Theme / branding API (Magnolia-backed later). Mock envelope until delivery exists.
 */
@RestController
@RequestMapping("/rest/api")
@Tag(name = "Theme Override")
public class ThemeController {

	private final ThemeOverrideService themeOverrideService;

	public ThemeController(ThemeOverrideService themeOverrideService) {
		this.themeOverrideService = themeOverrideService;
	}

	/**
	 * {@code GET /rest/api/themeOverride} — mock theme payload ({@code docs}, pagination).
	 */
	@GetMapping("/themeOverride")
	public ThemeOverrideResponse themeOverride() {
		return themeOverrideService.getThemeOverride();
	}
}
