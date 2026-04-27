package com.optima.cms.model.theme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

/**
 * Uploaded media (logo, SIM graphics, etc.). {@code user} and nested CMS fields are left as JSON for flexibility.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThemeMediaAsset {

	private String id;
	private String createdAt;
	private String updatedAt;
	private String alt;
	private JsonNode user;
	private String filename;
	private String mimeType;
	private Integer filesize;
	private Integer width;
	private Integer height;
	private String thumbnailURL;
	private String url;
}
