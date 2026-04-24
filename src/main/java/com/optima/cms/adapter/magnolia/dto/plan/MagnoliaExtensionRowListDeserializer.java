package com.optima.cms.adapter.magnolia.dto.plan;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Magnolia sometimes sends {@code extension} as an array of rows and sometimes as a single object.
 */
public class MagnoliaExtensionRowListDeserializer extends JsonDeserializer<List<MagnoliaExtensionRow>> {

	@Override
	public List<MagnoliaExtensionRow> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		JsonNode node = p.getCodec().readTree(p);
		if (node == null || node.isNull() || node.isMissingNode()) {
			return null;
		}
		ObjectMapper mapper = (ObjectMapper) p.getCodec();
		if (node.isArray()) {
			List<MagnoliaExtensionRow> out = new ArrayList<>();
			for (JsonNode el : node) {
				if (el != null && el.isObject()) {
					out.add(mapper.treeToValue(el, MagnoliaExtensionRow.class));
				}
			}
			return out.isEmpty() ? null : out;
		}
		if (node.isObject()) {
			if (node.isEmpty()) {
				return null;
			}
			return List.of(mapper.treeToValue(node, MagnoliaExtensionRow.class));
		}
		return null;
	}
}
