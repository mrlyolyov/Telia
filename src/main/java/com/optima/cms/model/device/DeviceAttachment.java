package com.optima.cms.model.device;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeviceAttachment {

	private String id;
	private String name;
	private String attachmentType;
	private String mimeType;
	/** Media asset id when {@code attachmentType} is {@code picture} (or similar). */
	private String file;
	private String content;
	private List<DeviceKeyValue> extension;

}
