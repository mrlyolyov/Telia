package com.optima.cms.adapter.magnolia.plan;

import com.fasterxml.jackson.databind.JsonNode;
import com.optima.cms.adapter.magnolia.dto.plan.MagnoliaAllowanceEntry;
import com.optima.cms.adapter.magnolia.dto.plan.MagnoliaExtensionRow;
import com.optima.cms.adapter.magnolia.dto.plan.MagnoliaPlan;
import com.optima.cms.adapter.magnolia.dto.plan.MagnoliaNamedValue;
import com.optima.cms.adapter.magnolia.dto.plan.MagnoliaMoneyAmount;
import com.optima.cms.adapter.magnolia.dto.plan.MagnoliaPlanAttachment;
import com.optima.cms.adapter.magnolia.dto.plan.MagnoliaPlanCharacteristics;
import com.optima.cms.adapter.magnolia.dto.plan.MagnoliaPriceEntry;
import com.optima.cms.model.plan.Allowance;
import com.optima.cms.model.plan.Attachment;
import com.optima.cms.model.plan.Characteristics;
import com.optima.cms.model.plan.Extension;
import com.optima.cms.model.plan.Plan;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Maps Magnolia plan payloads ({@link MagnoliaPlan}) to the API {@link Plan} model.
 */
@Component
public class MagnoliaPlanTranslator {

	public Plan adaptPlan(MagnoliaPlan externalPlan) {
		Plan plan = new Plan();
		if (externalPlan == null) {
			return plan;
		}
		plan.setId(externalPlan.getId());
		plan.setCreatedAt(externalPlan.getCreatedAt());
		plan.setUpdatedAt(externalPlan.getUpdatedAt());
		plan.setExternalId(externalPlan.getExternalId());
		plan.setName(externalPlan.getName());
		plan.setDescription(externalPlan.getDescription());
		plan.setPrice(mapPrices(externalPlan.getPrice()));
		plan.setExtension(mapNamedValuesToObjects(externalPlan.getExtension()));
		List<Attachment> attachments = mapAttachments(externalPlan.getAttachment());
		plan.setAttachment(attachments == null || attachments.isEmpty() ? null : attachments);
		if (externalPlan.getCharacteristics() != null) {
			plan.setCharacteristics(mapCharacteristics(externalPlan.getCharacteristics()));
		}
		return plan;
	}

	private static List<Object> mapPrices(List<MagnoliaPriceEntry> prices) {
		if (prices == null || prices.isEmpty()) {
			return null;
		}
		List<Object> out = new ArrayList<>();
		for (MagnoliaPriceEntry p : prices) {
			Map<String, Object> row = new LinkedHashMap<>();
			if (p.getId() != null) {
				row.put("id", p.getId());
			}
			if (p.getType() != null) {
				row.put("type", p.getType());
			}
			MagnoliaMoneyAmount amount = p.getAmount();
			if (amount != null) {
				Map<String, String> amountMap = new LinkedHashMap<>();
				if (amount.getValue() != null) {
					amountMap.put("value", amount.getValue());
				}
				if (amount.getCurrency() != null) {
					amountMap.put("currency", amount.getCurrency());
				}
				if (!amountMap.isEmpty()) {
					row.put("amount", amountMap);
				}
			}
			out.add(row);
		}
		return out;
	}

	private static List<Object> mapNamedValuesToObjects(List<MagnoliaNamedValue> entries) {
		if (entries == null || entries.isEmpty()) {
			return null;
		}
		List<Object> out = new ArrayList<>();
		for (MagnoliaNamedValue row : entries) {
			Extension ext = new Extension();
			ext.setKey(row.name());
			ext.setValue(row.value());
			out.add(ext);
		}
		return out;
	}

	private static List<Attachment> mapAttachments(List<MagnoliaPlanAttachment> attachments) {
		if (attachments == null || attachments.isEmpty()) {
			return null;
		}
		List<Attachment> out = new ArrayList<>();
		for (MagnoliaPlanAttachment ma : attachments) {
			Attachment a = new Attachment();
			a.setId(ma.getId());
			a.setName(ma.getName());
			a.setAttachmentType(ma.getAttachmentType());
			a.setContent(ma.getContent());
			a.setUrl(ma.getUrl());
			a.setValidFor(normalizeValidFor(ma.getValidFor()));
//			List<Extension> extList = mapExtensionRows(ma.getExtension());
			List<Extension> extList = new ArrayList<>();
			Extension extension = new Extension();
			extension.setId("ext-promo-up-001");
			extension.setKey("position");
			extension.setValue("promotion");
			extList.add(extension);
			a.setExtension(extList);
//			a.setExtension(extList == null || extList.isEmpty() ? null : extList);
			out.add(a);
		}
		return out;
	}

	private static JsonNode normalizeValidFor(JsonNode raw) {
		if (raw == null || raw.isNull() || raw.isMissingNode()) {
			return null;
		}
		if (raw.isObject() && raw.isEmpty()) {
			return null;
		}
		return raw;
	}

	private static List<Extension> mapExtensionRows(List<MagnoliaExtensionRow> rows) {
		if (rows == null || rows.isEmpty()) {
			return null;
		}
		List<Extension> out = new ArrayList<>();
		for (MagnoliaExtensionRow r : rows) {
			if (r == null) {
				continue;
			}
			Extension e = new Extension();
			e.setKey(r.getKey());
			e.setValue(r.getValue());
			e.setId(r.getId());
			out.add(e);
		}
		return out.isEmpty() ? null : out;
	}

	private static List<Object> mapExtensionRowsAsObjects(List<MagnoliaExtensionRow> rows) {
		List<Extension> list = mapExtensionRows(rows);
		if (list == null) {
			return null;
		}
		List<Object> objects = new ArrayList<>();
		objects.addAll(list);
		return objects.isEmpty() ? null : objects;
	}

	private static Characteristics mapCharacteristics(MagnoliaPlanCharacteristics ch) {
		if (ch == null) {
			return null;
		}
		Characteristics characteristics = new Characteristics();
		List<Allowance> allowance = mapAllowances(ch.getAllowance());
		characteristics.setAllowance(allowance == null || allowance.isEmpty() ? null : allowance);
		characteristics.setFeatures(mapNamedValuesToObjects(ch.getFeatures()));
		characteristics.setExtension(mapNamedValuesToObjects(ch.getExtension()));
		if (characteristics.getAllowance() == null
				&& characteristics.getFeatures() == null
				&& characteristics.getExtension() == null) {
			return null;
		}
		return characteristics;
	}

	private static List<Allowance> mapAllowances(List<MagnoliaAllowanceEntry> allowances) {
		if (allowances == null || allowances.isEmpty()) {
			return null;
		}
		List<Allowance> out = new ArrayList<>();
		for (MagnoliaAllowanceEntry ma : allowances) {
			Allowance a = new Allowance();
			a.setType(ma.getType());
			a.setValue(ma.getValue());
			a.setId(ma.getId());
			a.setExtension(mapExtensionRowsAsObjects(ma.getExtension()));
			out.add(a);
		}
		return out;
	}
}
