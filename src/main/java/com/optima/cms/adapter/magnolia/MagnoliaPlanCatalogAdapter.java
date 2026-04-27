package com.optima.cms.adapter.magnolia;

import com.optima.cms.adapter.magnolia.dto.plan.MagnoliaPlan;
import com.optima.cms.adapter.magnolia.plan.MagnoliaPlanTranslator;
import com.optima.cms.model.plan.Allowance;
import com.optima.cms.model.plan.Extension;
import com.optima.cms.model.plan.FindAllRequest;
import com.optima.cms.model.plan.Plan;
import com.optima.cms.model.plan.PlanFindAllResult;
import com.optima.cms.port.PlanCatalogPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.optima.cms.model.plan.Allowance.createAllowance;

/**
 * Magnolia-backed implementation of {@link PlanCatalogPort}: HTTP client + translation to API models.
 */
@Component
@Slf4j
public class MagnoliaPlanCatalogAdapter implements PlanCatalogPort {

	private final MagnoliaClient magnoliaClient;
	private final MagnoliaPlanTranslator magnoliaPlanTranslator;

	public MagnoliaPlanCatalogAdapter(MagnoliaClient magnoliaClient, MagnoliaPlanTranslator magnoliaPlanTranslator) {
		this.magnoliaClient = magnoliaClient;
		this.magnoliaPlanTranslator = magnoliaPlanTranslator;
	}

	@Override
	public PlanFindAllResult listPlans(FindAllRequest request) {
		List<MagnoliaPlan> all = magnoliaClient.getPlans(request);
		List<String> requestedIds = request != null ? request.getExternalId() : null;
		Set<String> wanted = toWantedExternalIds(requestedIds);
		List<MagnoliaPlan> selected = applyExternalIdFilter(all, request);
		String warning = buildMissingExternalIdWarning(wanted, selected, requestedIds);
		List<Plan> plans = selected.stream().map(magnoliaPlanTranslator::adaptPlan).toList();
		return new PlanFindAllResult(plans, warning);
	}

	/**
	 * When the request lists {@code externalId} values, keep only Magnolia plans whose {@code externalId}
	 * is in that set (trimmed, exact match). If nothing matches, returns an empty list. When {@code externalId}
	 * is null or empty, returns {@code all} unchanged.
	 */
	private List<MagnoliaPlan> applyExternalIdFilter(List<MagnoliaPlan> all, FindAllRequest request) {
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
		List<MagnoliaPlan> matched = all.stream()
				.filter(Objects::nonNull)
				.filter(p -> p.getExternalId() != null && wanted.contains(p.getExternalId().trim()))
				.toList();
		if (matched.isEmpty()) {
			log.info("No plans matched externalId filter {}; returning empty docs", wanted);
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
	 * Requested {@code externalId} values that do not appear on any plan in {@code selected} (the response rows).
	 */
	private static String buildMissingExternalIdWarning(Set<String> wanted, List<MagnoliaPlan> selected, List<String> requestedOrder) {
		if (wanted.isEmpty()) {
			return null;
		}
		Set<String> returned = selected.stream()
				.filter(Objects::nonNull)
				.map(MagnoliaPlan::getExternalId)
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
		return "Some plans not found: " + String.join(", ", missing);
	}

	private static Extension ext(String key, String value, String id) {
		Extension e = new Extension();
		e.setKey(key);
		e.setValue(value);
		e.setId(id);
		return e;
	}
	private static Allowance allowance(String type, String value, String id) {
		return Allowance.createAllowance(type, value, id);
	}
	private static Allowance allowanceWithExtensions(String type, String value, String id, List<Object> extension) {
		Allowance a = new Allowance();
		a.setType(type);
		a.setValue(value);
		a.setId(id);
		a.setExtension(extension);
		return a;
	}

	@Override
	public Plan getFirstPlanForDemo() {
		List<Plan> plans = magnoliaClient.getPlans2(new FindAllRequest()).stream()
				.map(magnoliaPlanTranslator::adaptPlan)
				.toList();
		return plans.isEmpty() ? new Plan() : plans.getFirst();
	}
}
