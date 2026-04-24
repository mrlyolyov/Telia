package com.optima.cms.adapter.magnolia;

import org.springframework.web.reactive.function.client.WebClientRequestException;

import javax.net.ssl.SSLException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.channels.ClosedChannelException;
import java.util.concurrent.TimeoutException;

/**
 * Detects transport-level failures when calling Magnolia over HTTP (as opposed to HTTP 4xx/5xx bodies).
 */
final class MagnoliaConnectionFailures {

	private MagnoliaConnectionFailures() {
	}

	static boolean isConnectionFailure(Throwable throwable) {
		for (Throwable t = throwable; t != null; t = t.getCause()) {
			if (t instanceof WebClientRequestException) {
				return true;
			}
			if (t instanceof ConnectException || t instanceof UnknownHostException) {
				return true;
			}
			if (t instanceof SocketTimeoutException || t instanceof TimeoutException) {
				return true;
			}
			if (t instanceof ClosedChannelException) {
				return true;
			}
			if (t instanceof SSLException) {
				return true;
			}
			if ("io.netty.handler.timeout.ReadTimeoutException".equals(t.getClass().getName())
					|| "io.netty.handler.timeout.WriteTimeoutException".equals(t.getClass().getName())) {
				return true;
			}
		}
		return false;
	}
}
