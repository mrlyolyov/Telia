package com.optima.cms.mapper;

import com.optima.cms.adapter.magnolia.model.MagnoliaPlan;
import com.optima.cms.model.Plan;
import org.springframework.stereotype.Component;

@Component
public class MagnoliaPlanMapper {

    public Plan map(MagnoliaPlan external) {
        Plan plan = new Plan();

        plan.setExternalId(external.getId());
        plan.setName(external.getName());

        return plan;
    }
}
