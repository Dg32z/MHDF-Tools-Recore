package cn.chengzhiya.mhdftools.listener;

import cn.chengzhiya.mhdftools.interfaces.RedisMessageListener;
import io.lettuce.core.pubsub.RedisPubSubListener;
import lombok.Getter;

import java.util.Objects;

@Getter
public abstract class AbstractRedisMessageListener extends AbstractListener implements RedisPubSubListener<String, String>, RedisMessageListener {
    private final String chanel;

    public AbstractRedisMessageListener(String enableKey, String chanel) {
        super(enableKey);
        this.chanel = chanel;
    }

    @Override
    public void message(String chanel, String message) {
        if (!Objects.equals(chanel, this.chanel)) {
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
