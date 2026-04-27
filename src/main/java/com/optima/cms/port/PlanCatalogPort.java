package com.optima.cms.port;

import com.optima.cms.model.plan.FindAllRequest;
import com.optima.cms.model.plan.Plan;
import com.optima.cms.model.plan.PlanFindAllResult;

/**
 * Application port: plan catalog as exposed to the UI, independent of CMS vendor.
 */
public interface PlanCatalogPort {

	PlanFindAllResult listPlans(FindAllRequest request);

	/**
	 * Returns the first plan from the catalog, or an empty {@link Plan}, for demo / smoke paths.
	 */
	Plan getFirstPlanForDemo();
}
