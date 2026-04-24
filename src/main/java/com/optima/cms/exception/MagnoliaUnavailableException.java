package com.optima.cms.exception;

/**
 * Thrown when the adapter cannot reach Magnolia (network, DNS, timeout, connection refused, etc.).
 */
public class MagnoliaUnavailableException extends RuntimeException {

	public MagnoliaUnavailableException(String message) {
		super(message);
	}

	public MagnoliaUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}
}
