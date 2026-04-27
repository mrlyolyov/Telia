package com.optima.cms.model.theme;

import java.util.List;

/**
 * Paginated Magnolia {@code header/v1} delivery: {@code results} mapped to {@link ThemeHeader} rows.
 */
public record ThemeHeaderDeliveryPage(int total, int offset, int limit, List<ThemeHeader> headers) {
}
