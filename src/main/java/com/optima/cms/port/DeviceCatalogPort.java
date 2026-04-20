package com.optima.cms.port;

import com.optima.cms.model.device.Device;
import com.optima.cms.model.plan.FindAllRequest;

import java.util.List;

/**
 * Application port: device catalog as exposed to the UI, independent of CMS vendor.
 */
public interface DeviceCatalogPort {

	List<Device> listDevices(FindAllRequest request);

}
