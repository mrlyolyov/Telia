package com.optima.cms.controller.plan;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.optima.cms.model.DocsEnvelope;
import com.optima.cms.model.plan.Allowance;
import com.optima.cms.model.plan.Attachment;
import com.optima.cms.model.plan.Characteristics;
import com.optima.cms.model.plan.Extension;
import com.optima.cms.model.plan.FindAllRequest;
import com.optima.cms.model.plan.Plan;
import com.optima.cms.service.PlanCatalogService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.optima.cms.model.plan.Allowance.createAllowance;

/**
 * Plan catalog and related HTTP API (expand with CRUD as needed).
 */
@RestController
@RequestMapping("/rest/api")
@Slf4j
public class PlanController {

	private static final String PLAN_FINDALL_FIXTURE = "mock/plan-findall.json";
	private static final TypeReference<DocsEnvelope<Plan>> PLAN_FINDALL_TYPE = new TypeReference<>() {};

	private final PlanCatalogService planCatalogService;
	private final ObjectMapper objectMapper;
	private DocsEnvelope<Plan> planFindAllFixture;

	public PlanController(PlanCatalogService planCatalogService, ObjectMapper objectMapper) {
		this.planCatalogService = planCatalogService;
		this.objectMapper = objectMapper;
	}

	@PostConstruct
	void loadPlanFindAllFixture() {
		ClassPathResource resource = new ClassPathResource(PLAN_FINDALL_FIXTURE);
		try (InputStream in = resource.getInputStream()) {
			this.planFindAllFixture = objectMapper.readValue(in, PLAN_FINDALL_TYPE);
		} catch (IOException e) {
			throw new IllegalStateException("Failed to load classpath " + PLAN_FINDALL_FIXTURE, e);
		}
	}

	@GetMapping("/ping")
	public Plan ping() {
		log.info("ping");
		return stubUnlimitedPlan();
	}

	/**
	 * {@code POST /rest/api/plan/findall} (selfservice plan catalog).
	 * Fixture from {@code classpath:mock/plan-findall.json} until Magnolia wiring is used here.
	 */
	@SuppressWarnings("unused")
	@PostMapping("/plan/findall")
	public DocsEnvelope<Plan> findAll(@RequestBody FindAllRequest request) {
		return DocsEnvelope.of(planCatalogService.listPlans(request));
//		return planFindAllFixture;
	}

	@GetMapping("/test")
	public Plan test() {
		return planCatalogService.getFirstPlanForDemo();
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
		attachment.setExtension(List.of(ext));

		plan.setAttachment(List.of(attachment));

		List<Allowance> allowances = List.of(
				createAllowance("data", "Unlimited", "6971fbd5c776b628938a09e5"),
				createAllowance("sms", "Unlimited", "6971fbe1c776b628938a09e6"),
				createAllowance("voice", "Unlimited", "6971fbe5c776b628938a09e7"),
				createAllowance("roaming-data", "Unlimited", "6971fbe9c776b628938a09e8"),
				createAllowance("roaming-voice", "Unlimited", "6971fbf1c776b628938a09e9"),
				createAllowance("international-calls", "Unlimited", "6971fbf6c776b628938a09ea"));

		Characteristics characteristics = new Characteristics();
		characteristics.setAllowance(allowances);
		characteristics.setFeatures(List.of());
		characteristics.setExtension(List.of());

		plan.setCharacteristics(characteristics);

		plan.setPrice(List.of());
		plan.setExtension(List.of());

		return plan;
	}
}
