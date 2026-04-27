package com.optima.cms.adapter.magnolia;

import com.optima.cms.adapter.magnolia.content.MagnoliaContentCardTranslator;
import com.optima.cms.adapter.magnolia.dto.content.MagnoliaContentCard;
import com.optima.cms.model.content.ContentCard;
import com.optima.cms.model.content.ContentCardPage;
import com.optima.cms.model.content.ContentCardResponse;
import com.optima.cms.port.ContentCardCatalogPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class MagnoliaContentCardCatalogAdapter implements ContentCardCatalogPort {

	private final MagnoliaClient magnoliaClient;
	private final MagnoliaContentCardTranslator magnoliaContentCardTranslator;

	public MagnoliaContentCardCatalogAdapter(MagnoliaClient magnoliaClient, MagnoliaContentCardTranslator magnoliaContentCardTranslator) {
		this.magnoliaClient = magnoliaClient;
		this.magnoliaContentCardTranslator = magnoliaContentCardTranslator;
	}

	@Override
	public ContentCardResponse listByPage(ContentCardPage page) {
		if (page == null) {
			return ContentCardResponse.empty();
		}
		String wire = page.getWire();
		List<MagnoliaContentCard> all = magnoliaClient.getContentCards();
		List<ContentCard> filtered = all.stream()
				.filter(Objects::nonNull)
				.filter(c -> wire.equalsIgnoreCase(trim(c.getPage())))
				.map(magnoliaContentCardTranslator::adapt)
				.toList();
		return toPagedResponse(filtered);
	}

	private static String trim(String s) {
		return s == null ? "" : s.trim();
	}

	private static ContentCardResponse toPagedResponse(List<ContentCard> docs) {
		ContentCardResponse r = new ContentCardResponse();
		r.setDocs(docs);
		int n = docs.size();
		r.setTotalDocs(n);
		r.setLimit(10);
		r.setTotalPages(n == 0 ? 0 : 1);
		r.setPage(1);
		r.setPagingCounter(n > 0 ? 1 : 0);
		r.setHasPrevPage(false);
		r.setHasNextPage(false);
		r.setPrevPage(null);
		r.setNextPage(null);
		return r;
	}
}
