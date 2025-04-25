package cn.chengzhiya.mhdftools.interfaces;

public interface RedisMessageListener {
    void onMessage(String message);
}
