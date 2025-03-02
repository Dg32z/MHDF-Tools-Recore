package cn.chengzhiya.mhdftools.hook.impl;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.placeholder.AbstractPlaceholder;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public final class PlaceholderApiImpl extends PlaceholderExpansion {
    private static final List<AbstractPlaceholder> placeholderList = new ArrayList<>();

    public PlaceholderApiImpl() {
        try {
            Reflections reflections = new Reflections(AbstractPlaceholder.class.getPackageName());

            for (Class<? extends AbstractPlaceholder> clazz : reflections.getSubTypesOf(AbstractPlaceholder.class)) {
                if (!Modifier.isAbstract(clazz.getModifiers())) {
                    AbstractPlaceholder abstractPlaceholder = clazz.getDeclaredConstructor().newInstance();
                    if (abstractPlaceholder.isEnable()) {
                        placeholderList.add(abstractPlaceholder);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.register();
    }

    public void unhook() {
        this.unregister();
    }

    /**
     * 处理PAPI变量
     *
     * @param player  玩家实例
     * @param message 要处理的文本
     * @return 处理过后的文本
     */
    public String placeholder(OfflinePlayer player, String message) {
        return PlaceholderAPI.setPlaceholders(player, message);
    }

    @Override
    public @NotNull String getIdentifier() {
        return Main.instance.getDescription().getName();
    }

    @Override
    public @NotNull String getAuthor() {
        return "白神遥桌上の橙汁";
    }

    @Override
    public @NotNull String getVersion() {
        return Main.instance.getDescription().getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        for (AbstractPlaceholder placeholder : placeholderList) {
            return placeholder.onPlaceholder(player, params);
        }
        return null;
    }
}
