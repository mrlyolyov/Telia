package com.optima.cms.adapter.magnolia;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Helpers for Magnolia delivery JCR-style JSON ({@code @nodes} ordered children, scalar fields).
 */
public final class MagnoliaDeliveryTree {

	private MagnoliaDeliveryTree() {
	}

	/** Repeatable nodes: follow {@code @nodes} array keys in order. */
	public static List<JsonNode> childrenInNodeOrder(JsonNode parent) {
		if (parent == null || !parent.isObject()) {
			return List.of();
		}
		JsonNode nodesArr = parent.get("@nodes");
		List<JsonNode> out = new ArrayList<>();
		if (nodesArr != null && nodesArr.isArray()) {
			for (JsonNode n : nodesArr) {
				if (!n.isTextual()) {
					continue;
				}
				String key = n.asText();
				JsonNode child = parent.get(key);
				if (child != null && !child.isNull()) {
					out.add(child);
				}
			}
		}
		return out;
	}

	public static String text(JsonNode node, String field) {
		if (node == null || !node.has(field) || node.get(field).isNull()) {
			return null;
		}
		JsonNode v = node.get(field);
		if (v.isTextual()) {
			return v.asText();
		}
		if (v.isNumber() || v.isBoolean()) {
			return v.asText();
		}
		return null;
	}

	public static Boolean booleanOrNull(JsonNode node, String field) {
		if (node == null || !node.has(field) || node.get(field).isNull()) {
			return null;
		}
		JsonNode v = node.get(field);
		if (v.isBoolean()) {
			return v.booleanValue();
		}
		return null;
	}

	public static int intOrZero(JsonNode node, String field) {
		if (node == null || !node.has(field) || node.get(field).isNull()) {
			return 0;
		}
		JsonNode v = node.get(field);
		return v.isInt() ? v.intValue() : v.asInt(0);
	}
}
