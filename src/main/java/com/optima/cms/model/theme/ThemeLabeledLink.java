package com.optima.cms.model.theme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.optima.cms.model.plan.Extension;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/** Footer link or nav {@code childitems} entry ({@code label}, {@code type}, {@code path}). */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThemeLabeledLink {

	private String id;
	private String label;
	private String type;
	private String path;
	private List<Extension> extension;
}
