package com.optima.cms.adapter.magnolia;

import com.optima.cms.adapter.magnolia.device.MagnoliaDeviceTranslator;
import com.optima.cms.adapter.magnolia.dto.device.MagnoliaDevice;
import com.optima.cms.model.device.Device;
import com.optima.cms.model.device.DeviceFindAllResult;
import com.optima.cms.model.plan.FindAllRequest;
import com.optima.cms.port.DeviceCatalogPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Magnolia-backed implementation of {@link DeviceCatalogPort}: HTTP client + translation to API models
 * (same flow as {@link MagnoliaPlanCatalogAdapter}).
 */
@Component
@Slf4j
public class MagnoliaDeviceCatalogAdapter implements DeviceCatalogPort {

	private final MagnoliaClient magnoliaClient;
	private final MagnoliaDeviceTranslator magnoliaDeviceTranslator;

	public MagnoliaDeviceCatalogAdapter(MagnoliaClient magnoliaClient, MagnoliaDeviceTranslator magnoliaDeviceTranslator) {
		this.magnoliaClient = magnoliaClient;
		this.magnoliaDeviceTranslator = magnoliaDeviceTranslator;
	}

	@Override
	public DeviceFindAllResult findDevicesByExternalId(String externalId) {
		String id = externalId == null ? "" : externalId.trim();
		if (id.isEmpty()) {
			return new DeviceFindAllResult(List.of(), null);
		}
		List<MagnoliaDevice> all = magnoliaClient.getDevices(null);
		if (all == null || all.isEmpty()) {
			return new DeviceFindAllResult(List.of(), "Some devices not found: " + id);
		}
		List<MagnoliaDevice> matched = all.stream()
				.filter(Objects::nonNull)
				.filter(d -> d.getExternalId() != null && id.equals(d.getExternalId().trim()))
				.toList();
		List<Device> devices = matched.stream().map(magnoliaDeviceTranslator::adaptDevice).toList();
		String warning = devices.isEmpty() ? "Some devices not found: " + id : null;
		return new DeviceFindAllResult(devices, warning);
	}

	@Override
	public DeviceFindAllResult listDevices(FindAllRequest request) {
		List<MagnoliaDevice> all = magnoliaClient.getDevices(request);
		List<String> requestedIds = request != null ? request.getExternalId() : null;
		Set<String> wanted = toWantedExternalIds(requestedIds);
		List<MagnoliaDevice> selected = applyExternalIdFilter(all, request);
		String warning = buildMissingExternalIdWarning(wanted, selected, requestedIds);
		List<Device> devices = selected.stream().map(magnoliaDeviceTranslator::adaptDevice).toList();
		return new DeviceFindAllResult(devices, warning);
	}

	/**
	 * When the request lists {@code externalId} values, keep only devices whose {@code externalId}
	 * is in that set (trimmed, exact match). If nothing matches, returns an empty list (same idea as
	 * {@link #findDevicesByExternalId}). When {@code externalId} is null or empty, returns {@code all} unchanged.
	 */
	private List<MagnoliaDevice> applyExternalIdFilter(List<MagnoliaDevice> all, FindAllRequest request) {
		if (all == null || all.isEmpty()) {
			return List.of();
		}
		List<String> requestedIds = request != null ? request.getExternalId() : null;
		if (requestedIds == null || requestedIds.isEmpty()) {
			return all;
		}
		Set<String> wanted = requestedIds.stream()
				.filter(Objects::nonNull)
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toCollection(LinkedHashSet::new));
		if (wanted.isEmpty()) {
			return all;
		}
		List<MagnoliaDevice> matched = all.stream()
				.filter(Objects::nonNull)
				.filter(d -> d.getExternalId() != null && wanted.contains(d.getExternalId().trim()))
				.toList();
		if (matched.isEmpty()) {
			log.info("No devices matched externalId filter {}; returning empty docs", wanted);
		}
		return matched;
	}

	private static Set<String> toWantedExternalIds(List<String> requestedIds) {
		if (requestedIds == null || requestedIds.isEmpty()) {
			return Set.of();
		}
		return requestedIds.stream()
				.filter(Objects::nonNull)
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	/**
	 * Requested {@code externalId} values that do not appear on any device in {@code selected} (the response rows).
	 */
	private static String buildMissingExternalIdWarning(Set<String> wanted, List<MagnoliaDevice> selected, List<String> requestedOrder) {
		if (wanted.isEmpty()) {
			return null;
		}
		Set<String> returned = selected.stream()
				.filter(Objects::nonNull)
				.map(MagnoliaDevice::getExternalId)
				.filter(Objects::nonNull)
				.map(String::trim)
				.collect(Collectors.toSet());
		List<String> missing = new ArrayList<>();
		if (requestedOrder != null) {
			for (String raw : requestedOrder) {
				if (raw == null) {
					continue;
				}
				String id = raw.trim();
				if (id.isEmpty() || !wanted.contains(id) || returned.contains(id) || missing.contains(id)) {
					continue;
				}
				missing.add(id);
			}
		} else {
			for (String id : wanted) {
				if (!returned.contains(id)) {
					missing.add(id);
				}
			}
		}
		if (missing.isEmpty()) {
			return null;
		}
		return "Some devices not found: " + String.join(", ", missing);
	}
}
