package com.optima.cms.port;

import com.optima.cms.model.plan.FindAllRequest;
import com.optima.cms.model.plan.Plan;

import java.util.List;

/**
 * Application port: plan catalog as exposed to the UI, independent of CMS vendor.
 */
public interface PlanCatalogPort {

	List<Plan> listPlans(FindAllRequest request);

	/**
	 * Returns the first plan from the catalog, or an empty {@link Plan}, for demo / smoke paths.
	 */
	Plan getFirstPlanForDemo();
}
