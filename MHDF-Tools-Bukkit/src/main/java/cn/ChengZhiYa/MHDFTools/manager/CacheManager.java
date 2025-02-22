package cn.ChengZhiYa.MHDFTools.manager;

import cn.ChengZhiYa.MHDFTools.util.config.ConfigUtil;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("unused")
public final class CacheManager {
    private HashMap<String, String> map = null;
    private RedisClient redisClient = null;
    private StatefulRedisConnection<String, String> redisConnection = null;

    /**
     * 初始化缓存
     */
    public void init() {
        String type = ConfigUtil.getConfig().getString("cacheSettings.type");

        if (type == null) {
            throw new RuntimeException("数据库类型未设置");
        }

        switch (type) {
            case "map" -> this.map = new HashMap<>();
            case "redis" -> {
                String host = ConfigUtil.getConfig().getString("cacheSettings.redis.host");
                String user = ConfigUtil.getConfig().getString("cacheSettings.redis.user");
                String password = ConfigUtil.getConfig().getString("cacheSettings.redis.password");
                if (host == null) {
                    return;
                }

                String[] hostSplit = host.split(":");

                RedisURI.Builder uriBuilder = RedisURI.Builder
                        .redis(hostSplit[0])
                        .withPort(Integer.parseInt(hostSplit[1]));

                if (user != null && !user.isEmpty() && password != null && !password.isEmpty()) {
                    uriBuilder.withAuthentication(user, password.toCharArray());
                }

                this.redisClient = RedisClient.create(uriBuilder.build());
                this.redisConnection = this.redisClient.connect();
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
        if (redisConnection != null) {
            redisConnection.close();
            redisConnection = null;
        }
        if (redisClient != null) {
            redisClient.shutdown();
            redisClient = null;
        }
    }

    /**
     * 写入数据至缓存
     *
     * @param key   写入的key
     * @param value 写入的值
     */
    public void put(String key, String value) {
        if (this.map != null) {
            this.map.put(key, value);
        }
        if (this.redisConnection != null) {
            RedisAsyncCommands<String, String> sync = this.redisConnection.async();
            sync.set("mhdf-luckcreate:" + key, value);
        }
    }

    /**
     * 从缓存中删除数据
     *
     * @param key 删除的key
     */
    public void remove(String key) {
        if (this.map != null) {
            this.map.remove(key);
        }
        if (this.redisConnection != null) {
            RedisAsyncCommands<String, String> sync = this.redisConnection.async();
            sync.del("mhdf-luckcreate:" + key);
        }
    }

    /**
     * 读取指定key的缓存数据
     *
     * @param key key
     * @return 缓存数据
     */
    public String get(String key) {
        if (this.map != null) {
            this.map.get(key);
        }
        if (this.redisClient != null) {
            try {
                RedisAsyncCommands<String, String> sync = this.redisConnection.async();
                return sync.get("mhdf-luckcreate:" + key).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
