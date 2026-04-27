package com.optima.cms.adapter.magnolia.dto.device;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.optima.cms.adapter.magnolia.dto.plan.MagnoliaExtensionRow;
import com.optima.cms.adapter.magnolia.dto.plan.MagnoliaExtensionRowListDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class MagnoliaDeviceAttachment {

	private String id;
	private String name;
	private String mimeType;
	/** DAM base path from Magnolia (e.g. {@code /magnolia/dam/}). */
	private String url;
	/** Magnolia may send a JCR id string or a nested media object with an {@code id} field. */
	private JsonNode file;
	private String content;
	private String attachmentType;
	private JsonNode validFor;
	@JsonDeserialize(using = MagnoliaExtensionRowListDeserializer.class)
	private List<MagnoliaExtensionRow> extension;
}
