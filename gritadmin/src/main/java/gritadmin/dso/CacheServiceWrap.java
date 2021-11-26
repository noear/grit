package gritadmin.dso;

import org.noear.solon.Solon;
import org.noear.solon.data.cache.CacheService;
import org.noear.weed.cache.ICacheServiceEx;

/**
 * @author noear 2021/11/26 created
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
        return real.get(key);
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
