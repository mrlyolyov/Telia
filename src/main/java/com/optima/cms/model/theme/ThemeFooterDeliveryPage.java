package com.optima.cms.model.theme;

import java.util.List;

/**
 * Paginated Magnolia {@code footer/v1} delivery: {@code results} mapped to {@link ThemeFooter} rows.
 */
public record ThemeFooterDeliveryPage(int total, int offset, int limit, List<ThemeFooter> footers) {
}
