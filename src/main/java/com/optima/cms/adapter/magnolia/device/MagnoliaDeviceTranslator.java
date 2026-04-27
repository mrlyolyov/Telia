package com.optima.cms.adapter.magnolia.device;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.optima.cms.adapter.magnolia.dto.device.MagnoliaDevice;
import com.optima.cms.adapter.magnolia.dto.device.MagnoliaDeviceAttachment;
import com.optima.cms.adapter.magnolia.dto.plan.MagnoliaExtensionRow;
import com.optima.cms.model.device.Device;
import com.optima.cms.model.device.DeviceAttachment;
import com.optima.cms.model.plan.Extension;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Maps Magnolia device delivery payloads to the API {@link Device} model.
 */
@Component
public class MagnoliaDeviceTranslator {

	/** Magnolia often wraps rich-text fragments in {@code <p>…</p>} with inner tags entity-escaped. */
	private static final Pattern MAGNOLIA_P_WRAPPER = Pattern.compile(
			"^\\s*<p[^>]*>\\s*(.*)\\s*</p>\\s*$",
			Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	private static final String DEFAULT_MAGNOLIA_DAM_PREFIX = "/magnolia/dam/";

	public Device adaptDevice(MagnoliaDevice external) {
		Device device = new Device();
		if (external == null) {
			return device;
		}
		device.setName(external.getName());
		device.setDescription(external.getDescription());
		device.setDisplayName(external.getDisplayName());
		device.setExternalId(external.getExternalId());
		List<DeviceAttachment> attachments = mapAttachments(external.getAttachment());
		device.setAttachment(attachments == null || attachments.isEmpty() ? null : attachments);
		device.setCharacteristics(external.getCharacteristics());
		return device;
	}

	private static List<DeviceAttachment> mapAttachments(List<MagnoliaDeviceAttachment> attachments) {
		if (attachments == null || attachments.isEmpty()) {
			return null;
		}
		List<DeviceAttachment> out = new ArrayList<>();
		for (MagnoliaDeviceAttachment ma : attachments) {
			if (ma == null) {
				continue;
			}
			DeviceAttachment a = new DeviceAttachment();
			a.setId(ma.getId());
			a.setName(ma.getName());
			a.setMimeType(ma.getMimeType());
			String jcrRef = resolveJcrFileReference(ma.getFile());
			String damUrl = combineAssetUrl(ma.getUrl(), jcrRef);
			a.setFile(buildApiFileNode(ma.getFile(), damUrl, ma.getMimeType(), ma.getAttachmentType(), ma.getName()));
			a.setContent(normalizeAttachmentContent(ma.getContent()));
			a.setAttachmentType(ma.getAttachmentType());
			a.setValidFor(normalizeValidFor(ma.getValidFor()));
			a.setExtension(mapExtensionRows(ma.getExtension()));
			out.add(a);
		}
		return out;
	}

	/**
	 * Decodes HTML entities Magnolia stores in {@code content} and drops a single outer {@code <p>…</p>} wrapper
	 * so the UI receives real markup (e.g. {@code <ul>} instead of {@code &lt;ul&gt;} inside {@code <p>}).
	 */
	private static String normalizeAttachmentContent(String raw) {
		if (raw == null || raw.isBlank()) {
			return null;
		}
		String decoded = HtmlUtils.htmlUnescape(raw.trim());
		Matcher m = MAGNOLIA_P_WRAPPER.matcher(decoded);
		if (m.matches()) {
			String inner = m.group(1).trim();
			return inner.isEmpty() ? null : inner;
		}
		return decoded;
	}

	/**
	 * Normalizes Magnolia {@code file} to the UI string form {@code jcr:…}: either a textual JCR reference or
	 * {@code "jcr:" + id} when {@code file} is an object with {@code id}.
	 */
	private static String resolveJcrFileReference(JsonNode file) {
		if (file == null || file.isNull() || file.isMissingNode()) {
			return null;
		}
		if (file.isTextual()) {
			return blankToNull(file.asText());
		}
		if (file.isObject()) {
			JsonNode idNode = file.get("id");
			if (idNode == null || !idNode.isTextual()) {
				return null;
			}
			String id = idNode.asText().trim();
			if (id.isEmpty()) {
				return null;
			}
			if (id.startsWith("jcr:")) {
				return id;
			}
			return "jcr:" + id;
		}
		return null;
	}

	/**
	 * UI expects {@code file} as an object; DAM URL is written to {@code file.url}. Passes through Magnolia’s object
	 * fields when {@code file} is an object; otherwise builds a minimal {@code { id, url [, mimeType ] }}.
	 * For {@code attachmentType} {@code picture}, sets {@code file.filename} from the attachment {@code name}.
	 */
	private static JsonNode buildApiFileNode(
			JsonNode magnoliaFile,
			String damUrl,
			String attachmentMimeType,
			String attachmentType,
			String attachmentName) {
		if (magnoliaFile != null && magnoliaFile.isObject()) {
			ObjectNode o = (ObjectNode) magnoliaFile.deepCopy();
			if (damUrl != null) {
				o.put("url", damUrl);
			}
			applyPictureFilename(o, attachmentType, attachmentName);
			return o;
		}
		if (magnoliaFile != null && magnoliaFile.isTextual()) {
			ObjectNode o = JsonNodeFactory.instance.objectNode();
			String id = blankToNull(magnoliaFile.asText());
			if (id != null) {
				o.put("id", id);
			}
			if (damUrl != null) {
				o.put("url", damUrl);
			}
			String mt = blankToNull(attachmentMimeType);
			if (mt != null) {
				o.put("mimeType", mt);
			}
			applyPictureFilename(o, attachmentType, attachmentName);
			return o.isEmpty() ? null : o;
		}
		if (damUrl != null || blankToNull(attachmentMimeType) != null) {
			ObjectNode o = JsonNodeFactory.instance.objectNode();
			if (damUrl != null) {
				o.put("url", damUrl);
			}
			String mt = blankToNull(attachmentMimeType);
			if (mt != null) {
				o.put("mimeType", mt);
			}
			applyPictureFilename(o, attachmentType, attachmentName);
			return o.isEmpty() ? null : o;
		}
		return null;
	}

	private static void applyPictureFilename(ObjectNode fileNode, String attachmentType, String attachmentName) {
		if (fileNode == null || attachmentType == null || !"picture".equalsIgnoreCase(attachmentType.trim())) {
			return;
		}
		String n = blankToNull(attachmentName);
		if (n != null) {
			fileNode.put("filename", n);
		}
	}

	/**
	 * Magnolia DAM base {@code url} on the attachment plus JCR id → single path for {@code file.url}.
	 * When Magnolia omits the base path, {@code /magnolia/dam/} is used so bare {@code jcr:…} segments are not returned alone.
	 */
	private static String combineAssetUrl(String urlPrefix, String jcrFile) {
		String u = blankToNull(urlPrefix);
		String f = blankToNull(jcrFile);
		if (f == null) {
			return u;
		}
		if (u == null && !isAlreadyAbsoluteAssetPath(f)) {
			u = DEFAULT_MAGNOLIA_DAM_PREFIX;
		}
		if (u == null) {
			return f;
		}
		boolean prefixEndsSlash = u.endsWith("/");
		boolean fileStartsSlash = f.startsWith("/");
		if (prefixEndsSlash && fileStartsSlash) {
			return u + f.substring(1);
		}
		if (!prefixEndsSlash && !fileStartsSlash) {
			return u + "/" + f;
		}
		return u + f;
	}

	private static boolean isAlreadyAbsoluteAssetPath(String f) {
		return f.startsWith("/") || f.startsWith("http://") || f.startsWith("https://");
	}

	private static String blankToNull(String s) {
		if (s == null) {
			return null;
		}
		String t = s.trim();
		return t.isEmpty() ? null : t;
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
}
