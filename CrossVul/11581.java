
package org.jboss.weld.context.cache;
import java.util.LinkedList;
import java.util.List;
public class RequestScopedCache {
    private static final ThreadLocal<List<RequestScopedItem>> CACHE = new ThreadLocal<List<RequestScopedItem>>();
    private RequestScopedCache() {
    }
    public static boolean isActive() {
        return CACHE.get() != null;
    }
    private static void checkCacheForAdding(final List<RequestScopedItem> cache) {
        if (cache == null) {
            throw new IllegalStateException("Unable to add request scoped cache item when request cache is not active");
        }
    }
    public static void addItem(final RequestScopedItem item) {
        final List<RequestScopedItem> cache = CACHE.get();
        checkCacheForAdding(cache);
        cache.add(item);
    }
    public static boolean addItemIfActive(final RequestScopedItem item) {
        final List<RequestScopedItem> cache = CACHE.get();
        if (cache != null) {
            cache.add(item);
            return true;
        }
        return false;
    }
    public static boolean addItemIfActive(final ThreadLocal<?> item) {
        final List<RequestScopedItem> cache = CACHE.get();
        if (cache != null) {
            cache.add(new RequestScopedItem() {
                public void invalidate() {
                    item.remove();
                }
            });
            return true;
        }
        return false;
    }
    public static void beginRequest() {
        endRequest();
        CACHE.set(new LinkedList<RequestScopedItem>());
    }
    public static void endRequest() {
        final List<RequestScopedItem> result = CACHE.get();
        if (result != null) {
            CACHE.remove();
            for (final RequestScopedItem item : result) {
                item.invalidate();
            }
        }
    }
    public static void invalidate() {
        if (isActive()) {
            endRequest();
            beginRequest();
        }
    }
}
