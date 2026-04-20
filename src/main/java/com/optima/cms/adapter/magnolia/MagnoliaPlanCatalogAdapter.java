package com.optima.cms.adapter.magnolia;

import com.optima.cms.adapter.magnolia.plan.MagnoliaPlanTranslator;
import com.optima.cms.model.plan.*;
import com.optima.cms.port.PlanCatalogPort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.optima.cms.model.plan.Allowance.createAllowance;

/**
 * Magnolia-backed implementation of {@link PlanCatalogPort}: HTTP client + translation to API models.
 */
@Component
public class MagnoliaPlanCatalogAdapter implements PlanCatalogPort {

	private final MagnoliaClient magnoliaClient;
	private final MagnoliaPlanTranslator magnoliaPlanTranslator;

	public MagnoliaPlanCatalogAdapter(MagnoliaClient magnoliaClient, MagnoliaPlanTranslator magnoliaPlanTranslator) {
		this.magnoliaClient = magnoliaClient;
		this.magnoliaPlanTranslator = magnoliaPlanTranslator;
	}

	@Override
	public List<Plan> listPlans(FindAllRequest request) {
		return magnoliaClient.getPlans(request).stream()
				.map(magnoliaPlanTranslator::adaptPlan)
				.toList();
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
