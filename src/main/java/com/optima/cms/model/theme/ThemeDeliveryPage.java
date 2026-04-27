package com.optima.cms.model.theme;

import java.util.List;

/**
 * Paginated Magnolia {@code theme/v1} delivery: {@code results} mapped to {@link ThemeOverride} body fields
 * (palette, logos, backgrounds — not header/footer nav, which come from separate endpoints).
 */
public record ThemeDeliveryPage(int total, int offset, int limit, List<ThemeOverride> themes) {
}
