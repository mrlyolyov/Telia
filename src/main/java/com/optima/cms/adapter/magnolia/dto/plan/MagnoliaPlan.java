package com.optima.cms.adapter.magnolia.dto.plan;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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
