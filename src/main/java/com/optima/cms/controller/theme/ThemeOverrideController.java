package com.optima.cms.controller.theme;

import com.optima.cms.model.theme.ThemeOverrideResponse;
import com.optima.cms.service.ThemeOverrideService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Aggregated theme / branding payload for the UI (Magnolia delivery). Header is sourced from
 * {@code /.rest/delivery/header/v1}; footer and core theme fields will be added when their Magnolia endpoints are wired.
 */
@RestController
@RequestMapping("/rest/api")
@Tag(name = "Theme Override")
public class ThemeOverrideController {

	private final ThemeOverrideService themeOverrideService;

	public ThemeOverrideController(ThemeOverrideService themeOverrideService) {
		this.themeOverrideService = themeOverrideService;
	}

	/**
	 * {@code GET /rest/api/theme/override} — {@link ThemeOverrideResponse}; currently populates {@code header} from Magnolia header delivery.
	 */
	@GetMapping("/theme/override")
	public ThemeOverrideResponse getThemeOverride() {
		return themeOverrideService.getThemeOverride();
	}
}
