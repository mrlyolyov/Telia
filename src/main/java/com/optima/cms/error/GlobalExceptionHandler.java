package com.optima.cms.error;

import com.optima.cms.exception.MagnoliaUnavailableException;
import com.optima.cms.exception.MagnoliaUpstreamException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;

/**
 * Maps domain exceptions to HTTP responses.
 * <p>
 * Handled exceptions are not logged by Spring by default; this class logs each mapped failure so
 * operations and log aggregation see the same errors clients receive.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	/**
	 * Stable problem type identifiers
	 */
	private static final URI TYPE_MAGNOLIA_UNAVAILABLE = URI.create("optima-cms:magnolia-unavailable");
	private static final URI TYPE_MAGNOLIA_UPSTREAM = URI.create("optima-cms:magnolia-upstream");

	@ExceptionHandler(MagnoliaUnavailableException.class)
	public ResponseEntity<ProblemDetail> magnoliaUnavailable(MagnoliaUnavailableException ex) {
		log.warn("Magnolia upstream unavailable: {}", ex.getMessage(), ex);
		ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
		detail.setType(TYPE_MAGNOLIA_UNAVAILABLE);
		detail.setTitle("Magnolia unavailable");
		detail.setProperty("upstream", "magnolia");
		Throwable cause = ex.getCause();
		if (cause != null) {
			Throwable root = cause;
			while (root.getCause() != null && root.getCause() != root) {
				root = root.getCause();
			}
			detail.setProperty("causeType", root.getClass().getSimpleName());
		}
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(detail);
	}

	@ExceptionHandler(MagnoliaUpstreamException.class)
	public ResponseEntity<ProblemDetail> magnoliaUpstream(MagnoliaUpstreamException ex) {
		HttpStatus status = ex.resolveHttpStatus();
		if (status == null) {
			status = HttpStatus.BAD_GATEWAY;
		}
		log.warn("Magnolia upstream HTTP error [{}]: {}", ex.getHttpStatus(), ex.getMessage(), ex);
		ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
		detail.setType(TYPE_MAGNOLIA_UPSTREAM);
		detail.setTitle("Magnolia request failed");
		detail.setProperty("upstream", "magnolia");
		detail.setProperty("magnoliaStatus", ex.getHttpStatus());
		if (ex.getCause() instanceof WebClientResponseException w) {
			detail.setProperty("magnoliaStatusText", w.getStatusText());
		}
		return ResponseEntity.status(status).body(detail);
	}
}
