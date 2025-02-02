package cn.ChengZhiYa.MHDFTools.manager;

import cn.ChengZhiYa.MHDFTools.interfaces.Init;
import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.UnifiedJedis;

import java.net.URI;
import java.util.HashMap;

@SuppressWarnings("unused")
public final class CacheManager implements Init {
    private HashMap<String, String> map = null;
    private UnifiedJedis jedis = null;

    /**
     * 初始化缓存
     */
    @Override
    public void init() {
        String type = ConfigUtil.getConfig().getString("cacheSettings.type");

        if (type == null) {
            throw new RuntimeException("数据库类型未设置");
        }

        switch (type) {
            case "map" -> this.map = new HashMap<>();
            case "redis" -> {
                String host = ConfigUtil.getConfig().getString("cacheSettings.redis.host");
                if (host == null) {
                    return;
                }

                JedisClientConfig config = new JedisClientConfig() {
                    @Override
                    public String getUser() {
                        return ConfigUtil.getConfig().getString("cacheSettings.redis.user");
                    }

                    @Override
                    public String getPassword() {
                        return ConfigUtil.getConfig().getString("cacheSettings.redis.password");
                    }
                };

                this.jedis = new UnifiedJedis(URI.create("redis://" + host), config);
            }
            default -> throw new RuntimeException("不支持的数据库类型: " + type);
        }
    }

    /**
     * 关闭缓存
     */
    public void close() {
        if (map != null) {
            map.clear();
            map = null;
        }
        if (jedis != null) {
            jedis.close();
            jedis = null;
        }
    }

    /**
     * 写入数据至缓存
     *
     * @param key   写入的key
     * @param value 写入的值
     */
    public void put(String key, String value) {
        if (this.jedis != null) {
            this.jedis.set("mhdf-tools:" + key, value);
        }
        if (this.map != null) {
            this.map.put(key, value);
        }
    }

    /**
     * 读取指定key的缓存数据
     *
     * @param key key
     * @return 缓存数据
     */
    public String get(String key) {
        if (this.jedis != null) {
            return this.map.get("mhdf-tools:" + key);
        }
        if (this.map != null) {
            this.map.get(key);
        }
        return null;
    }
}
