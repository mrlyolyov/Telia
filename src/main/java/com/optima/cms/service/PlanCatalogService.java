package com.optima.cms.service;

import com.optima.cms.model.plan.FindAllRequest;
import com.optima.cms.model.plan.Plan;
import com.optima.cms.model.plan.PlanFindAllResult;
import com.optima.cms.port.PlanCatalogPort;
import org.springframework.stereotype.Service;

@Service
public class PlanCatalogService {

	private final PlanCatalogPort planCatalogPort;

	public PlanCatalogService(PlanCatalogPort planCatalogPort) {
		this.planCatalogPort = planCatalogPort;
	}

	public PlanFindAllResult listPlans(FindAllRequest request) {
		return planCatalogPort.listPlans(request);
	}

	public Plan getFirstPlanForDemo() {
		return planCatalogPort.getFirstPlanForDemo();
	}
}
