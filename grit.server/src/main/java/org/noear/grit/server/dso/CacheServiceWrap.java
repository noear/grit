package org.noear.grit.server.dso;

import org.noear.solon.Solon;
import org.noear.solon.data.cache.CacheService;
import org.noear.wood.cache.ICacheServiceEx;

/**
 * @author noear
 * @since 1.0
 */
public class CacheServiceWrap implements CacheService, ICacheServiceEx {
    CacheService real;

    public CacheServiceWrap(CacheService real) {
        this.real = real;
    }

    @Override
    public void store(String key, Object obj, int seconds) {
        real.store(key, obj, seconds);
    }

    @Override
    public Object get(String key) {
        return get(key, Object.class);
    }

    @Override
    public <T> T get(String key, Class<T> clz) {
        return real.get(key, clz);
    }

    @Override
    public void remove(String key) {
        real.remove(key);
    }

    @Override
    public int getDefalutSeconds() {
        return 30;
    }

    @Override
    public String getCacheKeyHead() {
        return Solon.cfg().appName();
    }
}
