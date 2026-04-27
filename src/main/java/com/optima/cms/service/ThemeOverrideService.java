package com.optima.cms.service;

import com.optima.cms.model.theme.ThemeDeliveryPage;
import com.optima.cms.model.theme.ThemeFooter;
import com.optima.cms.model.theme.ThemeFooterDeliveryPage;
import com.optima.cms.model.theme.ThemeHeader;
import com.optima.cms.model.theme.ThemeHeaderDeliveryPage;
import com.optima.cms.model.theme.ThemeOverride;
import com.optima.cms.model.theme.ThemeOverrideResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Builds {@link ThemeOverrideResponse} from Magnolia delivery APIs (theme body, header, footer).
 */
@Service
public class ThemeOverrideService {

	private final HeaderCatalogService headerCatalogService;
	private final FooterCatalogService footerCatalogService;
	private final ThemeCatalogService themeCatalogService;

	public ThemeOverrideService(
			HeaderCatalogService headerCatalogService,
			FooterCatalogService footerCatalogService,
			ThemeCatalogService themeCatalogService) {
		this.headerCatalogService = headerCatalogService;
		this.footerCatalogService = footerCatalogService;
		this.themeCatalogService = themeCatalogService;
	}

	public ThemeOverrideResponse getThemeOverride() {
		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
			CompletableFuture<ThemeHeaderDeliveryPage> headerFuture =
					CompletableFuture.supplyAsync(headerCatalogService::getHeaderDelivery, executor);
			CompletableFuture<ThemeFooterDeliveryPage> footerFuture =
					CompletableFuture.supplyAsync(footerCatalogService::getFooterDelivery, executor);
			CompletableFuture<ThemeDeliveryPage> themeFuture =
					CompletableFuture.supplyAsync(themeCatalogService::getThemeDelivery, executor);
			CompletableFuture.allOf(headerFuture, footerFuture, themeFuture).join();
			return assembleResponse(headerFuture.join(), footerFuture.join(), themeFuture.join());
		}
	}

	private static ThemeOverrideResponse assembleResponse(
			ThemeHeaderDeliveryPage headerPage,
			ThemeFooterDeliveryPage footerPage,
			ThemeDeliveryPage themePage) {
		ThemeOverrideResponse response = new ThemeOverrideResponse();
		applyMagnoliaPaging(response, headerPage, footerPage, themePage);

		List<ThemeOverride> docs = new ArrayList<>();
		ThemeHeader header = firstHeader(headerPage);
		ThemeFooter footer = firstFooter(footerPage);
		ThemeOverride theme = firstTheme(themePage);
		if (theme != null || header != null || footer != null) {
			ThemeOverride doc = theme != null ? theme : new ThemeOverride();
			if (header != null) {
				doc.setHeader(header);
			}
			if (footer != null) {
				doc.setFooter(footer);
			}
			docs.add(doc);
		}
		response.setDocs(List.copyOf(docs));
		return response;
	}

	private static ThemeHeader firstHeader(ThemeHeaderDeliveryPage headerPage) {
		if (headerPage == null || headerPage.headers() == null || headerPage.headers().isEmpty()) {
			return null;
		}
		return headerPage.headers().get(0);
	}

	private static ThemeFooter firstFooter(ThemeFooterDeliveryPage footerPage) {
		if (footerPage == null || footerPage.footers() == null || footerPage.footers().isEmpty()) {
			return null;
		}
		return footerPage.footers().get(0);
	}

	private static ThemeOverride firstTheme(ThemeDeliveryPage themePage) {
		if (themePage == null || themePage.themes() == null || themePage.themes().isEmpty()) {
			return null;
		}
		return themePage.themes().get(0);
	}

	/**
	 * Prefer header envelope for paging when it has rows; otherwise footer, then theme body.
	 */
	private static void applyMagnoliaPaging(
			ThemeOverrideResponse response,
			ThemeHeaderDeliveryPage headerPage,
			ThemeFooterDeliveryPage footerPage,
			ThemeDeliveryPage themePage) {
		boolean headerHasRows = headerPage != null && headerPage.headers() != null && !headerPage.headers().isEmpty();
		if (headerHasRows) {
			applyMagnoliaPaging(response, headerPage.total(), headerPage.offset(), headerPage.limit());
			return;
		}
		boolean footerHasRows = footerPage != null && footerPage.footers() != null && !footerPage.footers().isEmpty();
		if (footerHasRows) {
			applyMagnoliaPaging(response, footerPage.total(), footerPage.offset(), footerPage.limit());
			return;
		}
		boolean themeHasRows = themePage != null && themePage.themes() != null && !themePage.themes().isEmpty();
		if (themeHasRows) {
			applyMagnoliaPaging(response, themePage.total(), themePage.offset(), themePage.limit());
			return;
		}
		if (headerPage != null) {
			applyMagnoliaPaging(response, headerPage.total(), headerPage.offset(), headerPage.limit());
			return;
		}
		if (footerPage != null) {
			applyMagnoliaPaging(response, footerPage.total(), footerPage.offset(), footerPage.limit());
			return;
		}
		if (themePage != null) {
			applyMagnoliaPaging(response, themePage.total(), themePage.offset(), themePage.limit());
			return;
		}
		applyMagnoliaPaging(response, 0, 0, 10);
	}

	private static void applyMagnoliaPaging(ThemeOverrideResponse response, int total, int offset, int limitRaw) {
		int limit = limitRaw > 0 ? limitRaw : 10;
		response.setTotalDocs(total);
		response.setLimit(limit);
		int totalPages = limit > 0 ? (int) Math.ceil((double) total / (double) limit) : 0;
		response.setTotalPages(totalPages);
		int page = limit > 0 ? (offset / limit) + 1 : 1;
		response.setPage(page);
		response.setPagingCounter(offset + 1);
		response.setHasPrevPage(offset > 0);
		response.setHasNextPage(offset + limit < total);
		response.setPrevPage(response.isHasPrevPage() ? page - 1 : null);
		response.setNextPage(response.isHasNextPage() ? page + 1 : null);
	}
}
