package com.optima.cms.port;

import com.optima.cms.model.device.DeviceFindAllResult;
import com.optima.cms.model.plan.FindAllRequest;

/**
 * Application port: device catalog as exposed to the UI, independent of CMS vendor.
 */
public interface DeviceCatalogPort {

	/**
	 * Device catalog entries (Magnolia delivery shape, mapped to {@link Device}).
	 */
	DeviceFindAllResult listDevices(FindAllRequest request);

	/**
	 * Devices whose {@code externalId} equals the given value (trimmed). Empty {@code devices} and a
	 * {@link DeviceFindAllResult#warning} when nothing matches Magnolia delivery.
	 */
	DeviceFindAllResult findDevicesByExternalId(String externalId);

}
