package cn.chengzhiya.mhdftools.manager;

import cn.chengzhiya.mhdftools.exception.FileException;
import cn.chengzhiya.mhdftools.exception.ResourceException;
import cn.chengzhiya.mhdftools.util.config.*;

@SuppressWarnings("unused")
public final class ConfigManager {
    /**
     * 初始化默认配置文件
     */
    public void init() {
        try {
            FileUtil.createFolder(ConfigUtil.getDataFolder());

            ConfigUtil.saveDefaultConfig();
            LangUtil.saveDefaultLang();
            SoundUtil.saveDefaultSound();
            CustomMenuConfigUtil.saveDefaultCustomMenu();
            MenuConfigUtil.saveDefaultMenu();
        } catch (ResourceException | FileException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 重载所有配置文件
     */
    public void reloadAll() {
        ConfigUtil.reloadConfig();
        LangUtil.reloadLang();
        SoundUtil.reloadSound();
        CustomMenuConfigUtil.reloadCustomMenu();
        MenuConfigUtil.reloadMenu();
    }
}
