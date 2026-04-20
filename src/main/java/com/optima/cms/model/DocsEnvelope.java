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

	public static <T> DocsEnvelope<T> of(List<T> docs) {
		return new DocsEnvelope<>(docs != null ? docs : List.of());
	}
}
