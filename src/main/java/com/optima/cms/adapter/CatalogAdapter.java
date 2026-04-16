package com.optima.cms.adapter;

import com.optima.cms.model.Plan;
import com.optima.cms.model.PlanFindAllRequest;

import java.util.List;

public interface CatalogAdapter {
    List<Plan> findPlans(PlanFindAllRequest request);

    Plan testCall();
}