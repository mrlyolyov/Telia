package com.optima.cms.controller.content;

import com.optima.cms.model.content.ContentCardResponse;
import com.optima.cms.service.ContentCardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Page-scoped content cards (Magnolia-backed later). Mock until delivery exists.
 */
@RestController
@RequestMapping("/rest/api")
public class ContentCardController {

	private final ContentCardService contentCardService;

	public ContentCardController(ContentCardService contentCardService) {
		this.contentCardService = contentCardService;
	}

	/**
	 * {@code GET /rest/api/contentCard?page=…} — mock content cards ({@code docs}, pagination).
	 * Known pages: {@code shop-home}, {@code plan-gallery} (classpath mocks); anything else returns an empty {@code docs} envelope.
	 */
	@GetMapping("/contentCard")
	public ContentCardResponse contentCard(@RequestParam(value = "page", required = false) String page) {
		return contentCardService.getContentCards(page);
	}
}
