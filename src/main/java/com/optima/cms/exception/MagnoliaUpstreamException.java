package com.optima.cms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.nio.charset.StandardCharsets;

/**
 * Magnolia responded on the wire but with a non-success HTTP status (4xx/5xx). Surfaced to API clients
 * with the same status code where possible, instead of a generic 500.
 */
public class MagnoliaUpstreamException extends RuntimeException {

	private final int httpStatus;

	public MagnoliaUpstreamException(int httpStatus, String message, Throwable cause) {
		super(message, cause);
		this.httpStatus = httpStatus;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	/**
	 * @param endpointLabel short label for logs / detail, e.g. {@code "delivery planSchema"}
	 */
	public static MagnoliaUpstreamException from(WebClientResponseException ex, String endpointLabel) {
		int code = ex.getStatusCode().value();
		String statusText = ex.getStatusText() != null ? ex.getStatusText() : "";
		String body = ex.getResponseBodyAsString(StandardCharsets.UTF_8);
		String preview = truncateOneLine(body, 512);
		String detail = "Magnolia (" + endpointLabel + ") returned HTTP " + code
				+ (statusText.isEmpty() ? "" : " " + statusText);
		if (preview != null && !preview.isEmpty()) {
			detail += ". Body (truncated): " + preview;
		}
		return new MagnoliaUpstreamException(code, detail, ex);
	}

	private static String truncateOneLine(String s, int maxLen) {
		if (s == null || s.isBlank()) {
			return null;
		}
		String oneLine = s.replaceAll("\\s+", " ").trim();
		return oneLine.length() <= maxLen ? oneLine : oneLine.substring(0, maxLen) + "…";
	}

	/** Resolved {@link HttpStatus} for Spring MVC, or empty if non-standard code. */
	public HttpStatus resolveHttpStatus() {
		return HttpStatus.resolve(httpStatus);
	}
}
