package com.optima.cms.service;

import com.optima.cms.model.content.ContentCardPage;
import com.optima.cms.model.content.ContentCardResponse;
import com.optima.cms.port.ContentCardCatalogPort;
import org.springframework.stereotype.Service;

/**
 * Content card sections per UI page (Magnolia delivery {@code content-card/v1}).
 */
@Service
public class ContentCardService {

	private final ContentCardCatalogPort contentCardCatalogPort;

	public ContentCardService(ContentCardCatalogPort contentCardCatalogPort) {
		this.contentCardCatalogPort = contentCardCatalogPort;
	}

	/**
	 * @param page resolved UI page; {@code null} (missing or unknown query value) returns an empty {@code docs} envelope.
	 */
	public ContentCardResponse getContentCards(ContentCardPage page) {
		return contentCardCatalogPort.listByPage(page);
	}
}
