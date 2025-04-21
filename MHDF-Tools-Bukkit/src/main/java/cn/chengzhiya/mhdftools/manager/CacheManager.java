package cn.chengzhiya.mhdftools.manager;

import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("unused")
public final class CacheManager {
    private HashMap<String, HashMap<String, String>> map = null;
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
     * 获取服务器ID
     *
     * @return 服务器ID
     */
    public String getServerId() {
        return ConfigUtil.getConfig().getString("cacheSettings.server");
    }

    /**
     * 修改指定表id的表下指定key的缓存数据
     *
     * @param table 表id
     * @param key   写入的key
     * @param value 写入的值
     */
    public void put(String table, String key, String value) {
        String prefix = getServerId() + "mhdf-tools" + "-" + table;
        if (this.map != null) {
            HashMap<String, String> map = this.map.get(prefix) != null ? this.map.get(prefix) : new HashMap<>();
            map.put(key, value);

            this.map.put(prefix, map);
        }
        if (this.redisConnection != null) {
            RedisAsyncCommands<String, String> sync = this.redisConnection.async();
            sync.set(prefix + ":" + key, value);
        }
    }

    /**
     * 删除指定表id的表下指定key的缓存数据
     *
     * @param table 表id
     * @param key   删除的key
     */
    public void remove(String table, String key) {
        String prefix = getServerId() + "mhdf-tools" + "-" + table;
        if (this.map != null) {
            HashMap<String, String> map = this.map.get(prefix) != null ? this.map.get(prefix) : new HashMap<>();
            map.remove(key);

            this.map.put(prefix, map);
        }
        if (this.redisConnection != null) {
            RedisAsyncCommands<String, String> sync = this.redisConnection.async();
            sync.del(prefix + ":" + key);
        }
    }

    /**
     * 读取指定表id的表下指定key的缓存数据
     *
     * @param table 表id
     * @param key   key
     * @return 缓存数据
     */
    public String get(String table, String key) {
        String prefix = getServerId() + "mhdf-tools" + "-" + table;
        if (this.map != null) {
            HashMap<String, String> map = this.map.get(prefix) != null ? this.map.get(prefix) : new HashMap<>();

            return map.get(key);
        }
        if (this.redisClient != null) {
            try {
                RedisAsyncCommands<String, String> sync = this.redisConnection.async();
                return sync.get(prefix + ":" + key).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    /**
     * 读取指定表id的表 的缓存key列表
     *
     * @param table 表id
     * @return 缓存key列表
     */
    public Set<String> keys(String table) {
        String prefix = getServerId() + "mhdf-tools" + "-" + table;
        if (this.map != null) {
            HashMap<String, String> map = this.map.get(prefix) != null ? this.map.get(prefix) : new HashMap<>();

            return map.keySet();
        }
        if (this.redisClient != null) {
            try {
                RedisAsyncCommands<String, String> sync = this.redisConnection.async();
                return new HashSet<>(sync.keys(prefix + ":*").get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        return new HashSet<>();
    }
}
