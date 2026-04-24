package com.optima.cms.adapter.magnolia;

import com.optima.cms.adapter.magnolia.dto.plan.MagnoliaPlan;
import com.optima.cms.adapter.magnolia.plan.MagnoliaPlanTranslator;
import com.optima.cms.model.plan.*;
import com.optima.cms.port.PlanCatalogPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
	public List<Plan> listPlans(FindAllRequest request) {
		List<MagnoliaPlan> all = magnoliaClient.getPlans(request);
		List<MagnoliaPlan> selected = applyExternalIdFilter(all, request);
		return selected.stream().map(magnoliaPlanTranslator::adaptPlan).toList();
	}

	/**
	 * When the request lists {@code externalId} values, keep only Magnolia plans whose {@code externalId}
	 * is in that set (trimmed, exact match). If that yields no rows, returns the full {@code all} list.
	 * When {@code externalId} is null or empty, returns {@code all} unchanged.
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
		if (!matched.isEmpty()) {
			return matched;
		}
		log.info("No plans matched externalId filter {}; returning full catalog ({} plans)", wanted, all.size());
		return all;
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
