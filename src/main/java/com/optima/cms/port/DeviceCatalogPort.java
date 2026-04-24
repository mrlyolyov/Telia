package com.optima.cms.port;

import com.fasterxml.jackson.databind.JsonNode;
import com.optima.cms.model.plan.FindAllRequest;

/**
 * Application port: device catalog as exposed to the UI, independent of CMS vendor.
 */
public interface DeviceCatalogPort {

	/**
	 * Full device catalog response (Magnolia-style envelope: {@code docs}, pagination, optional {@code warning}).
	 */
	JsonNode getDeviceCatalog(FindAllRequest request);

}
