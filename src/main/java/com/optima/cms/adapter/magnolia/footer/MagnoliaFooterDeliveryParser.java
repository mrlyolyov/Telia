package com.optima.cms.adapter.magnolia.footer;

import com.fasterxml.jackson.databind.JsonNode;
import com.optima.cms.adapter.magnolia.MagnoliaDeliveryTree;
import com.optima.cms.model.theme.ThemeFooter;
import com.optima.cms.model.theme.ThemeFooterDeliveryPage;
import com.optima.cms.model.theme.ThemeLabeledLink;
import com.optima.cms.model.theme.ThemeSocialLink;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts Magnolia delivery {@code footer/v1} JCR-style trees into API {@link ThemeFooter} models.
 */
public final class MagnoliaFooterDeliveryParser {

	private MagnoliaFooterDeliveryParser() {
	}

	public static ThemeFooterDeliveryPage parse(JsonNode root) {
		if (root == null || root.isNull() || !root.isObject()) {
			return new ThemeFooterDeliveryPage(0, 0, 0, List.of());
		}
		int total = MagnoliaDeliveryTree.intOrZero(root, "total");
		int offset = MagnoliaDeliveryTree.intOrZero(root, "offset");
		int limit = MagnoliaDeliveryTree.intOrZero(root, "limit");
		List<ThemeFooter> footers = new ArrayList<>();
		JsonNode results = root.get("results");
		if (results != null && results.isArray()) {
			for (JsonNode row : results) {
				if (row != null && !row.isNull()) {
					footers.add(parseFooterRow(row));
				}
			}
		}
		return new ThemeFooterDeliveryPage(total, offset, limit, List.copyOf(footers));
	}

	private static ThemeFooter parseFooterRow(JsonNode row) {
		ThemeFooter footer = new ThemeFooter();
		footer.setCompanyUrl(MagnoliaDeliveryTree.text(row, "companyUrl"));
		footer.setCopyRightText(MagnoliaDeliveryTree.text(row, "copyRightText"));
		footer.setLinks(parseLabeledLinks(row.get("links")));
		footer.setSocialLinks(parseSocialLinks(row.get("socialLinks")));
		return footer;
	}

	private static List<ThemeLabeledLink> parseLabeledLinks(JsonNode linksRoot) {
		if (linksRoot == null || linksRoot.isNull() || !linksRoot.isObject()) {
			return null;
		}
		List<ThemeLabeledLink> out = new ArrayList<>();
		for (JsonNode n : MagnoliaDeliveryTree.childrenInNodeOrder(linksRoot)) {
			ThemeLabeledLink l = new ThemeLabeledLink();
			l.setId(MagnoliaDeliveryTree.text(n, "id"));
			l.setLabel(MagnoliaDeliveryTree.text(n, "label"));
			l.setType(MagnoliaDeliveryTree.text(n, "type"));
			l.setPath(MagnoliaDeliveryTree.text(n, "path"));
			l.setExtension(null);
			out.add(l);
		}
		return out.isEmpty() ? null : out;
	}

	private static List<ThemeSocialLink> parseSocialLinks(JsonNode socialRoot) {
		if (socialRoot == null || socialRoot.isNull() || !socialRoot.isObject()) {
			return null;
		}
		List<ThemeSocialLink> out = new ArrayList<>();
		for (JsonNode n : MagnoliaDeliveryTree.childrenInNodeOrder(socialRoot)) {
			ThemeSocialLink s = new ThemeSocialLink();
			s.setId(MagnoliaDeliveryTree.text(n, "id"));
			s.setIconName(MagnoliaDeliveryTree.text(n, "iconName"));
			s.setUrl(MagnoliaDeliveryTree.text(n, "url"));
			out.add(s);
		}
		return out.isEmpty() ? null : out;
	}
}
