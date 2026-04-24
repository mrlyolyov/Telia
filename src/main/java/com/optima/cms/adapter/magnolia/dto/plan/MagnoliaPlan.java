package com.optima.cms.adapter.magnolia.dto.plan;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class MagnoliaPlan {

	@JsonAlias({ "@id", "uuid", "jcrUuid" })
	private String id;

	@JsonAlias({ "creationDate", "dateCreated", "created" })
	private String createdAt;

	@JsonAlias({ "lastModificationDate", "dateUpdated", "lastModified", "modified" })
	private String updatedAt;

    private String description;
    private String name;
    private String externalId;

    private List<MagnoliaPriceEntry> price;
    private MagnoliaPlanCharacteristics characteristics;
    private List<MagnoliaNamedValue> extension;
    private List<MagnoliaPlanAttachment> attachment;

}
