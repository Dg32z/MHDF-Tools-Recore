package cn.chengzhiya.mhdftools.listener;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.interfaces.RedisMessageListener;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import io.lettuce.core.pubsub.RedisPubSubListener;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@Getter
public abstract class AbstractRedisMessageListener implements RedisPubSubListener<String, String>, RedisMessageListener {
    private final boolean enable;
    private final String chanel;

    public AbstractRedisMessageListener(@NotNull String chanel) {
        this.enable = true;
        this.chanel = chanel;
    }

    public AbstractRedisMessageListener(@NotNull String enableKey, @NotNull String chanel) {
        this.enable = ConfigUtil.getConfig().getBoolean(enableKey);
        this.chanel = chanel;
    }

    public AbstractRedisMessageListener(List<String> enableKeyList, @NotNull String chanel) {
        boolean enable = true;
        for (String enableKey : enableKeyList) {
            if (enableKey == null || enableKey.isEmpty()) {
                continue;
            }

            enable = ConfigUtil.getConfig().getBoolean(enableKey);
        }

        this.enable = enable;
        this.chanel = chanel;
    }

    @Override
    public void message(String chanel, String message) {
        if (!Objects.equals(chanel, Main.instance.getCacheManager().getRedisMessageManager().getPrefix() + this.chanel)) {
            return;
        }
        this.onMessage(message);
    }

    @Override
    public void message(String chanel, String k1, String string2) {
    }

    @Override
    public void subscribed(String string, long l) {
    }

    @Override
    public void psubscribed(String string, long l) {
    }

    @Override
    public void unsubscribed(String string, long l) {
    }

    @Override
    public void punsubscribed(String string, long l) {
    }
}
