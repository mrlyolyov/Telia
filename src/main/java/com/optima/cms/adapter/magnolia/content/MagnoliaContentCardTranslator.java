package com.optima.cms.adapter.magnolia.content;

import com.optima.cms.adapter.magnolia.dto.content.MagnoliaContentCard;
import com.optima.cms.adapter.magnolia.dto.content.MagnoliaContentCardItem;
import com.optima.cms.adapter.magnolia.dto.plan.MagnoliaExtensionRow;
import com.optima.cms.model.content.ContentCard;
import com.optima.cms.model.content.ContentCardItem;
import com.optima.cms.model.content.ContentCardPage;
import com.optima.cms.model.content.ContentCardType;
import com.optima.cms.model.plan.Extension;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MagnoliaContentCardTranslator {

	public ContentCard adapt(MagnoliaContentCard external) {
		ContentCard out = new ContentCard();
		if (external == null) {
			return out;
		}
		out.setId(resolveId(external));
		out.setCreatedAt(external.getCreatedAt());
		out.setUpdatedAt(external.getUpdatedAt());
		String title = blankToNull(external.getTitle());
		out.setTitle(title != null ? title : blankToNull(external.getName()));
		out.setDescription(blankToNull(external.getDescription()));
		out.setPage(ContentCardPage.tryParse(external.getPage()));
		out.setCardType(ContentCardType.tryParse(external.getCardType()));
		out.setActive(external.getActive());
		out.setSection(external.getSection());
		out.setCards(mapItems(external.getCards()));
		out.setExtension(mapExtensions(external.getExtension()));
		return out;
	}

	private static String resolveId(MagnoliaContentCard external) {
		String plain = blankToNull(external.getId());
		if (plain != null) {
			return plain;
		}
		return blankToNull(external.getAtId());
	}

	private static List<ContentCardItem> mapItems(List<MagnoliaContentCardItem> items) {
		if (items == null || items.isEmpty()) {
			return null;
		}
		List<ContentCardItem> out = new ArrayList<>();
		for (MagnoliaContentCardItem mi : items) {
			if (mi == null) {
				continue;
			}
			ContentCardItem c = new ContentCardItem();
			c.setId(blankToNull(mi.getId()));
			c.setContent(mi.getContent());
			c.setActive(mi.getActive());
			c.setSortOrder(resolveSortOrder(mi));
			c.setExtension(mapExtensions(mi.getExtension()));
			out.add(c);
		}
		return out.isEmpty() ? null : out;
	}

	private static Integer resolveSortOrder(MagnoliaContentCardItem mi) {
		if (mi.getSortOrder() != null) {
			return mi.getSortOrder();
		}
		if (mi.getExtension() == null) {
			return null;
		}
		for (MagnoliaExtensionRow r : mi.getExtension()) {
			if (r == null || r.getKey() == null) {
				continue;
			}
			if ("sortOrder".equalsIgnoreCase(r.getKey().trim()) && r.getValue() != null) {
				try {
					return Integer.parseInt(r.getValue().trim());
				} catch (NumberFormatException ignored) {
					return null;
				}
			}
		}
		return null;
	}

	private static List<Extension> mapExtensions(List<MagnoliaExtensionRow> rows) {
		if (rows == null || rows.isEmpty()) {
			return null;
		}
		List<Extension> out = new ArrayList<>();
		for (MagnoliaExtensionRow r : rows) {
			if (r == null) {
				continue;
			}
			Extension e = new Extension();
			e.setKey(r.getKey());
			e.setValue(r.getValue());
			e.setId(r.getId());
			out.add(e);
		}
		return out.isEmpty() ? null : out;
	}

	private static String blankToNull(String s) {
		if (s == null) {
			return null;
		}
		String t = s.trim();
		return t.isEmpty() ? null : t;
	}
}
