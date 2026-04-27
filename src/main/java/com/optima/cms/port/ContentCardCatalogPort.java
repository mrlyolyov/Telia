package com.optima.cms.port;

import com.optima.cms.model.content.ContentCardPage;
import com.optima.cms.model.content.ContentCardResponse;

/**
 * Page-scoped content cards for the UI.
 */
public interface ContentCardCatalogPort {

	/**
	 * Cards for the given UI page; empty envelope when {@code page} is null or nothing matches.
	 */
	ContentCardResponse listByPage(ContentCardPage page);
}
