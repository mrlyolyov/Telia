package com.optima.cms.model.device;

import com.fasterxml.jackson.databind.JsonNode;
import com.optima.cms.model.plan.Extension;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeviceAttachment {

	private String id;
	private String name;
	private String mimeType;
	/**
	 * Media payload for binary attachments: object shape from Magnolia, with {@code url} set to the DAM fetch path
	 * ({@code attachment.url} + JCR id). No top-level {@code url} on the attachment.
	 */
	private JsonNode file;
	private String content;
	private String attachmentType;
	private JsonNode validFor;
	private List<Extension> extension;
}
