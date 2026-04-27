package com.optima.cms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Wraps catalog payloads so the JSON shape matches {@code { "docs": [ ... ] }}.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocsEnvelope<T> {

	private List<T> docs;

	/** Optional pagination (Magnolia / Payload-style); omitted from JSON when null. */
	private Integer totalDocs;
	private Integer limit;
	private Integer totalPages;
	private Integer page;
	private Integer pagingCounter;
	private Boolean hasPrevPage;
	private Boolean hasNextPage;
	private Integer prevPage;
	private Integer nextPage;

	/** Human-readable notice (e.g. missing requested {@code externalId} values); omitted from JSON when null. */
	private String warning;

	public static <T> DocsEnvelope<T> of(List<T> docs) {
		return of(docs, null);
	}

	public static <T> DocsEnvelope<T> of(List<T> docs, String warning) {
		return new DocsEnvelope<>(docs != null ? docs : List.of(), null, null, null, null, null, null, null, null, null, warning);
	}
}
