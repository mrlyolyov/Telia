package com.optima.cms.adapter.magnolia.header;

import com.fasterxml.jackson.databind.JsonNode;
import com.optima.cms.adapter.magnolia.MagnoliaDeliveryTree;
import com.optima.cms.model.plan.Extension;
import com.optima.cms.model.theme.ThemeHeader;
import com.optima.cms.model.theme.ThemeHeaderDeliveryPage;
import com.optima.cms.model.theme.ThemeLabeledLink;
import com.optima.cms.model.theme.ThemeMenuCard;
import com.optima.cms.model.theme.ThemeMenuCardButton;
import com.optima.cms.model.theme.ThemeNavSectionItem;
import com.optima.cms.model.theme.ThemeNavTab;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts Magnolia delivery {@code header/v1} JCR-style trees ({@code navTabs0}, {@code items0}, {@code @nodes} order)
 * into API {@link ThemeHeader} models.
 */
public final class MagnoliaHeaderDeliveryParser {

	private MagnoliaHeaderDeliveryParser() {
	}

	public static ThemeHeaderDeliveryPage parse(JsonNode root) {
		if (root == null || root.isNull() || !root.isObject()) {
			return new ThemeHeaderDeliveryPage(0, 0, 0, List.of());
		}
		int total = MagnoliaDeliveryTree.intOrZero(root, "total");
		int offset = MagnoliaDeliveryTree.intOrZero(root, "offset");
		int limit = MagnoliaDeliveryTree.intOrZero(root, "limit");
		List<ThemeHeader> headers = new ArrayList<>();
		JsonNode results = root.get("results");
		if (results != null && results.isArray()) {
			for (JsonNode row : results) {
				if (row != null && !row.isNull()) {
					headers.add(parseHeaderRow(row));
				}
			}
		}
		return new ThemeHeaderDeliveryPage(total, offset, limit, List.copyOf(headers));
	}

	private static ThemeHeader parseHeaderRow(JsonNode row) {
		ThemeHeader header = new ThemeHeader();
		JsonNode navTabsRoot = row.get("navTabs");
		if (navTabsRoot != null && navTabsRoot.isObject()) {
			header.setNavTabs(parseNavTabs(navTabsRoot));
		}
		return header;
	}

	private static List<ThemeNavTab> parseNavTabs(JsonNode navTabsRoot) {
		List<ThemeNavTab> tabs = new ArrayList<>();
		for (JsonNode tabNode : MagnoliaDeliveryTree.childrenInNodeOrder(navTabsRoot)) {
			tabs.add(parseNavTab(tabNode));
		}
		return tabs.isEmpty() ? null : tabs;
	}

	private static ThemeNavTab parseNavTab(JsonNode tabNode) {
		ThemeNavTab tab = new ThemeNavTab();
		tab.setId(MagnoliaDeliveryTree.text(tabNode, "id"));
		tab.setTab(MagnoliaDeliveryTree.text(tabNode, "tab"));
		tab.setItems(parseNavSectionItems(tabNode.get("items")));
		tab.setMenuCard(parseMenuCard(tabNode.get("menuCard")));
		tab.setExtension(parseExtensions(tabNode.get("extension")));
		return tab;
	}

	private static List<ThemeNavSectionItem> parseNavSectionItems(JsonNode itemsRoot) {
		if (itemsRoot == null || itemsRoot.isNull() || !itemsRoot.isObject()) {
			return null;
		}
		List<ThemeNavSectionItem> items = new ArrayList<>();
		for (JsonNode itemNode : MagnoliaDeliveryTree.childrenInNodeOrder(itemsRoot)) {
			items.add(parseNavSectionItem(itemNode));
		}
		return items.isEmpty() ? null : items;
	}

	private static ThemeNavSectionItem parseNavSectionItem(JsonNode itemNode) {
		ThemeNavSectionItem item = new ThemeNavSectionItem();
		item.setId(MagnoliaDeliveryTree.text(itemNode, "id"));
		item.setMainItem(MagnoliaDeliveryTree.text(itemNode, "mainItem"));
		item.setChildItems(parseChildLinks(itemNode.get("childitems")));
		item.setExtension(parseExtensions(itemNode.get("extension")));
		return item;
	}

	private static List<ThemeLabeledLink> parseChildLinks(JsonNode childitemsRoot) {
		if (childitemsRoot == null || childitemsRoot.isNull() || !childitemsRoot.isObject()) {
			return null;
		}
		List<ThemeLabeledLink> links = new ArrayList<>();
		for (JsonNode linkNode : MagnoliaDeliveryTree.childrenInNodeOrder(childitemsRoot)) {
			ThemeLabeledLink link = new ThemeLabeledLink();
			link.setId(MagnoliaDeliveryTree.text(linkNode, "id"));
			link.setLabel(MagnoliaDeliveryTree.text(linkNode, "label"));
			link.setType(MagnoliaDeliveryTree.text(linkNode, "type"));
			link.setPath(MagnoliaDeliveryTree.text(linkNode, "path"));
			link.setExtension(parseExtensions(linkNode.get("extension")));
			links.add(link);
		}
		return links.isEmpty() ? null : links;
	}

	private static ThemeMenuCard parseMenuCard(JsonNode node) {
		if (node == null || node.isNull() || !node.isObject()) {
			return null;
		}
		ThemeMenuCard card = new ThemeMenuCard();
		card.setEnabled(MagnoliaDeliveryTree.booleanOrNull(node, "enabled"));
		card.setMode(MagnoliaDeliveryTree.text(node, "mode"));
		card.setTitle(MagnoliaDeliveryTree.text(node, "title"));
		card.setDescription(MagnoliaDeliveryTree.text(node, "description"));
		JsonNode btn = node.get("button");
		if (btn != null && btn.isObject() && !btn.isNull()) {
			ThemeMenuCardButton b = new ThemeMenuCardButton();
			b.setLabel(MagnoliaDeliveryTree.text(btn, "label"));
			b.setType(MagnoliaDeliveryTree.text(btn, "type"));
			b.setPath(MagnoliaDeliveryTree.text(btn, "path"));
			card.setButton(b);
		}
		JsonNode cc = node.get("customContent");
		if (cc != null && !cc.isNull()) {
			card.setCustomContent(cc);
		}
		return card;
	}

	private static List<Extension> parseExtensions(JsonNode extRoot) {
		if (extRoot == null || extRoot.isNull() || !extRoot.isObject()) {
			return null;
		}
		List<Extension> list = new ArrayList<>();
		for (JsonNode n : MagnoliaDeliveryTree.childrenInNodeOrder(extRoot)) {
			Extension e = new Extension();
			e.setKey(MagnoliaDeliveryTree.text(n, "key"));
			e.setValue(MagnoliaDeliveryTree.text(n, "value"));
			e.setId(MagnoliaDeliveryTree.text(n, "id"));
			list.add(e);
		}
		return list.isEmpty() ? null : list;
	}

}
