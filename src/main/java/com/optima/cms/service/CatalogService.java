package com.optima.cms.service;

import com.optima.cms.adapter.CatalogAdapter;
import com.optima.cms.model.Plan;
import com.optima.cms.model.PlanFindAllRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogService {

    private final CatalogAdapter adapter;

    public CatalogService(CatalogAdapter adapter) {
        this.adapter = adapter;
    }

    public List<Plan> getPlans(PlanFindAllRequest request) {
        return adapter.findPlans(request);
    }

    public Plan test() {
        return adapter.testCall();
    }
}
