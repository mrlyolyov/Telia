package com.optima.cms.controller;

import java.util.List;

import com.optima.cms.model.*;
import com.optima.cms.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import static com.optima.cms.model.Allowance.createAllowance;

@RestController
@RequestMapping("/rest/api")
@Slf4j
public class RestEntrypoint {

	@Autowired
	CatalogService catalogService;

	//	@GetMapping("/ping")
//	public Map<String, String> ping() {
//		log.info("ping");
//		return Map.of("status", "ok");
//	}
	@GetMapping("/ping")
	public Plan ping() {
		log.info("ping");
		return stubUnlimitedPlan();
	}

	/**
	 * Stub for {@code POST /rest/api/plan/findall} (selfservice plan catalog).
	 */
	@PostMapping("/plan/findall")
	public List<Plan> findAll(@RequestBody PlanFindAllRequest request) {
		return catalogService.getPlans(request);
	}

	private static Plan stubUnlimitedPlan() {
		Plan plan = new Plan();
		plan.setId("6971fc0103cc513f4e2794d1");
		plan.setCreatedAt("2026-01-22T10:29:21.854Z");
		plan.setUpdatedAt("2026-02-18T20:44:23.749Z");
		plan.setExternalId("2000253628");
		plan.setName("Unlimited Plan");

		Extension ext = new Extension();
		ext.setKey("position");
		ext.setValue("promotion");
		ext.setId("ext-promo-up-001");

		Attachment attachment = new Attachment();
		attachment.setId("promo-unlimited-plan-001");
		attachment.setName("Premium extras");
		attachment.setAttachmentType("text");
		attachment.setContent("<ul><li>Free streaming subscription for 3 months</li><li>Priority customer support 24/7</li><li>Unlimited hotspot tethering</li></ul>");
		attachment.setValidFor(new ValidFor());
		attachment.setExtension(List.of(ext));

		plan.setAttachment(List.of(attachment));

		List<Allowance> allowances = List.of(
				createAllowance("data", "Unlimited", "6971fbd5c776b628938a09e5"),
				createAllowance("sms", "Unlimited", "6971fbe1c776b628938a09e6"),
				createAllowance("voice", "Unlimited", "6971fbe5c776b628938a09e7"),
				createAllowance("roaming-data", "Unlimited", "6971fbe9c776b628938a09e8"),
				createAllowance("roaming-voice", "Unlimited", "6971fbf1c776b628938a09e9"),
				createAllowance("international-calls", "Unlimited", "6971fbf6c776b628938a09ea")
		);

		Characteristics characteristics = new Characteristics();
		characteristics.setAllowance(allowances);
		characteristics.setFeatures(List.of());
		characteristics.setExtension(List.of());

		plan.setCharacteristics(characteristics);

		plan.setPrice(List.of());
		plan.setExtension(List.of());

		return plan;
	}

	@GetMapping("/test")
	public Plan test() {
		return catalogService.test();
	}
}

