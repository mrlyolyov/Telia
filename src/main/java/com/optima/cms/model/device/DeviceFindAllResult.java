package com.optima.cms.model.device;

import java.util.List;

/**
 * Result of a device catalog query, including an optional warning when requested {@code externalId}
 * values are missing from the returned set.
 */
public record DeviceFindAllResult(List<Device> devices, String warning) {
}
