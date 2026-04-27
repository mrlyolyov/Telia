package com.optima.cms.adapter.magnolia.theme;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.optima.cms.adapter.magnolia.MagnoliaDeliveryTree;
import com.optima.cms.model.theme.ThemeColorToken;
import com.optima.cms.model.theme.ThemeDeliveryPage;
import com.optima.cms.model.theme.ThemeMediaAsset;
import com.optima.cms.model.theme.ThemeOverride;
import com.optima.cms.model.theme.ThemePalette;
import com.optima.cms.model.theme.ThemeResolvedToken;
import com.optima.cms.model.theme.ThemeTokenBackground;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts Magnolia delivery {@code theme/v1} rows into {@link ThemeOverride} body fields.
 */
@Slf4j
public final class MagnoliaThemeDeliveryParser {

	private static final String DEFAULT_MAGNOLIA_DAM_PREFIX = "/magnolia/dam/";

	private MagnoliaThemeDeliveryParser() {
	}

	public static ThemeDeliveryPage parse(JsonNode root, ObjectMapper objectMapper) {
		if (root == null || root.isNull() || !root.isObject()) {
			return new ThemeDeliveryPage(0, 0, 0, List.of());
		}
		int total = MagnoliaDeliveryTree.intOrZero(root, "total");
		int offset = MagnoliaDeliveryTree.intOrZero(root, "offset");
		int limit = MagnoliaDeliveryTree.intOrZero(root, "limit");
		List<ThemeOverride> themes = new ArrayList<>();
		JsonNode results = root.get("results");
		if (results != null && results.isArray()) {
			for (JsonNode row : results) {
				if (row != null && !row.isNull()) {
					themes.add(parseThemeRow(row, objectMapper));
				}
			}
		}
		return new ThemeDeliveryPage(total, offset, limit, List.copyOf(themes));
	}

	private static ThemeOverride parseThemeRow(JsonNode row, ObjectMapper objectMapper) {
		ThemeOverride t = new ThemeOverride();
		t.setId(MagnoliaDeliveryTree.text(row, "id"));
		t.setName(MagnoliaDeliveryTree.text(row, "name"));
		JsonNode activeNode = row.get("active");
		if (activeNode != null && !activeNode.isNull() && activeNode.isBoolean()) {
			t.setActive(activeNode.booleanValue());
		}
		t.setButtonShape(MagnoliaDeliveryTree.text(row, "buttonShape"));
		t.setIconButtonShape(MagnoliaDeliveryTree.text(row, "iconButtonShape"));
		t.setPalette(parsePalette(row, objectMapper));
		t.setLogo(parseMediaAsset(row.get("logo")));
		t.setHeaderBackground(parseTokenBackground(row.get("headerBackground")));
		t.setFooterBackground(parseTokenBackground(row.get("footerBackground")));
		t.setHeaderText(parseResolvedToken(row.get("headerText")));
		t.setFooterText(parseResolvedToken(row.get("footerText")));
		t.setSimCardWithLogo(parseMediaAsset(row.get("simCardWithLogo")));
		t.setSimLearnMore(parseMediaAsset(row.get("simLearnMore")));
		return t;
	}

	private static ThemePalette parsePalette(JsonNode row, ObjectMapper objectMapper) {
		String raw = MagnoliaDeliveryTree.text(row, "palette");
		if (raw == null || raw.isBlank()) {
			return null;
		}
		String json = HtmlUtils.htmlUnescape(raw.trim());
		try {
			return objectMapper.readValue(json, ThemePalette.class);
		} catch (JsonProcessingException e) {
			log.warn("Theme palette string is not valid JSON after HTML unescape: {}", e.getMessage());
			return null;
		}
	}

	private static ThemeTokenBackground parseTokenBackground(JsonNode node) {
		if (node == null || node.isNull() || !node.isObject()) {
			return null;
		}
		ThemeTokenBackground bg = new ThemeTokenBackground();
		bg.setMode(MagnoliaDeliveryTree.text(node, "mode"));
		JsonNode ct = node.get("colorToken");
		if (ct != null && ct.isObject() && !ct.isNull()) {
			ThemeColorToken colorToken = new ThemeColorToken();
			colorToken.setToken(MagnoliaDeliveryTree.text(ct, "token"));
			colorToken.setResolved(MagnoliaDeliveryTree.text(ct, "resolved"));
			bg.setColorToken(colorToken);
		}
		return bg;
	}

	private static ThemeResolvedToken parseResolvedToken(JsonNode node) {
		if (node == null || node.isNull() || !node.isObject()) {
			return null;
		}
		ThemeResolvedToken r = new ThemeResolvedToken();
		r.setToken(MagnoliaDeliveryTree.text(node, "token"));
		r.setResolved(MagnoliaDeliveryTree.text(node, "resolved"));
		return r;
	}

	private static ThemeMediaAsset parseMediaAsset(JsonNode node) {
		if (node == null || node.isNull() || !node.isObject()) {
			return null;
		}
		ThemeMediaAsset m = new ThemeMediaAsset();
		m.setAlt(MagnoliaDeliveryTree.text(node, "alt"));
		String file = MagnoliaDeliveryTree.text(node, "file");
		if (file != null) {
			m.setFilename(file);
			m.setUrl(damUrlForThemeFile(file));
		}
		return m;
	}

	/**
	 * Theme delivery exposes {@code file} as a JCR id (e.g. {@code jcr:…}); UI expects a DAM path under the default prefix.
	 */
	private static String damUrlForThemeFile(String file) {
		if (file == null) {
			return null;
		}
		String f = file.trim();
		if (f.isEmpty()) {
			return null;
		}
		if (f.startsWith("http://") || f.startsWith("https://") || f.startsWith("/")) {
			return f;
		}
		return DEFAULT_MAGNOLIA_DAM_PREFIX + f;
	}
}
