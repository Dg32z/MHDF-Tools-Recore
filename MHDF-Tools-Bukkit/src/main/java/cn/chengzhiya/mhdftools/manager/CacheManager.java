package cn.chengzhiya.mhdftools.manager;

import cn.chengzhiya.mhdftools.listener.AbstractRedisMessageListener;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("unused")
public final class CacheManager {
    private ConcurrentHashMap<String, ConcurrentHashMap<String, String>> map = null;
    private RedisClient redisClient = null;
    private StatefulRedisConnection<String, String> redisConnection = null;
    private StatefulRedisPubSubConnection<String, String> redisPubSubConnection;

    /**
     * 获取服务器ID
     *
     * @return 服务器ID
     */
    public String getServerId() {
        return ConfigUtil.getConfig().getString("cacheSettings.server");
    }

    /**
     * 获取缓存前缀
     *
     * @return 缓存前缀
     */
    public String getPrefix() {
        return getServerId() + "mhdf-tools-";
    }

    /**
     * 初始化缓存
     */
    public void init() {
        String type = ConfigUtil.getConfig().getString("cacheSettings.type");

        if (type == null) {
            throw new RuntimeException("数据库类型未设置");
        }

        switch (type) {
            case "map" -> this.map = new ConcurrentHashMap<>();
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

                this.redisPubSubConnection = this.redisClient.connectPubSub();
                this.registerListener();
            }
            default -> throw new RuntimeException("不支持的数据库类型: " + type);
        }
    }

    /**
     * 注册监听器
     */
    private void registerListener() {
        try {
            Reflections reflections = new Reflections(AbstractRedisMessageListener.class.getPackageName());

            for (Class<? extends AbstractRedisMessageListener> clazz : reflections.getSubTypesOf(AbstractRedisMessageListener.class)) {
                if (!Modifier.isAbstract(clazz.getModifiers())) {
                    AbstractRedisMessageListener redisMessageListener = clazz.getDeclaredConstructor().newInstance();
                    if (redisMessageListener.isEnable()) {
                        this.redisPubSubConnection.async().subscribe(getPrefix() + redisMessageListener.getChanel());
                        this.redisPubSubConnection.addListener(redisMessageListener);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭缓存
     */
    public void close() {
        if (this.map != null) {
            this.map.clear();
            this.map = null;
        }
        if (this.redisPubSubConnection != null) {
            this.redisPubSubConnection.close();
            this.redisPubSubConnection = null;
        }
        if (this.redisConnection != null) {
            this.redisConnection.close();
            this.redisConnection = null;
        }
        if (this.redisClient != null) {
            this.redisClient.shutdown();
            this.redisClient = null;
        }
    }

    /**
     * 向指定频道发送指定消息
     *
     * @param chanel 指定频道
     * @param message 指定消息
     */
    public void sendMessage(String chanel, String message) {
        this.redisPubSubConnection.async().publish(getPrefix() + chanel, message);
    }

    /**
     * 修改指定表id的表下指定key的缓存数据
     *
     * @param table 表id
     * @param key   写入的key
     * @param value 写入的值
     */
    public void put(String table, String key, String value) {
        String prefix = getPrefix() + table;
        if (this.map != null) {
            ConcurrentHashMap<String, String> map = this.map.get(prefix) != null ? this.map.get(prefix) : new ConcurrentHashMap<>();
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
        String prefix = getPrefix() + table;
        if (this.map != null) {
            ConcurrentHashMap<String, String> map = this.map.get(prefix) != null ? this.map.get(prefix) : new ConcurrentHashMap<>();
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
        String prefix = getPrefix() + table;
        if (this.map != null) {
            ConcurrentHashMap<String, String> map = this.map.get(prefix) != null ? this.map.get(prefix) : new ConcurrentHashMap<>();

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
        String prefix = getPrefix() + table;
        if (this.map != null) {
            ConcurrentHashMap<String, String> map = this.map.get(prefix) != null ? this.map.get(prefix) : new ConcurrentHashMap<>();

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
