package cn.ChengZhiYa.MHDFTools.manager;

import cn.ChengZhiYa.MHDFTools.exception.FileException;
import cn.ChengZhiYa.MHDFTools.exception.ResourceException;
import cn.ChengZhiYa.MHDFTools.interfaces.Init;
import cn.ChengZhiYa.MHDFTools.util.config.*;

@SuppressWarnings("unused")
public final class ConfigManager implements Init {
    /**
     * 初始化默认配置文件
     */
    @Override
    public void init() {
        try {
            FileUtil.createFolder(ConfigUtil.getDataFolder());

            ConfigUtil.saveDefaultConfig();
            ConfigUtil.saveDefaultConfig();

            LangUtil.saveDefaultLang();
            LangUtil.reloadLang();

            SoundUtil.saveDefaultSound();
            SoundUtil.reloadSound();

            CustomMenuConfigUtil.saveDefaultCustomMenu();
        } catch (ResourceException | FileException e) {
            throw new RuntimeException(e);
        }
    }
}
