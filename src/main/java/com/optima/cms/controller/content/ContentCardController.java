package com.optima.cms.controller.content;

import com.optima.cms.model.content.ContentCardPage;
import com.optima.cms.model.content.ContentCardResponse;
import com.optima.cms.service.ContentCardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Page-scoped content cards (Magnolia-backed later). Mock until delivery exists.
 */
@RestController
@RequestMapping("/rest/api")
@Tag(name = "Content Card")
public class ContentCardController {

	private final ContentCardService contentCardService;

	public ContentCardController(ContentCardService contentCardService) {
		this.contentCardService = contentCardService;
	}

	/**
	 * {@code GET /rest/api/contentCard?page=…} — mock content cards ({@code docs}, pagination).
	 * {@code page} is a {@link ContentCardPage} wire value (e.g. {@code device-gallery}); unknown values return an empty {@code docs} envelope.
	 */
	@GetMapping("/contentCard")
	public ContentCardResponse contentCard(@RequestParam(value = "page", required = false) String page) {
		return contentCardService.getContentCards(ContentCardPage.tryParse(page));
	}
}
