package cn.chengzhiya.mhdftools.util.config;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.exception.DownloadException;
import cn.chengzhiya.mhdftools.util.message.LogUtil;
import cn.chengzhiya.mhdftools.util.network.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class MinecraftLangUtil {
    private static final File file = new File(ConfigUtil.getDataFolder(), "minecraftLang");
    private static YamlConfiguration data;

    /**
     * 下载MC语言文件
     */
    private static void downloadMinecraftLang() {
        if (file.exists()) {
            return;
        }

        int retryCount = 0;

        while (retryCount < 5) {
            try {
                // 读取MC版本列表
                byte[] versionManifestBytes = HttpUtil.downloadFile(HttpUtil.openConnection("https://bmclapi2.bangbang93.com/mc/game/version_manifest.json"));
                JSONObject versionManifest = JSON.parseObject(versionManifestBytes);

                String serverVersion = Main.instance.getPluginHookManager().getPacketEventsHook()
                        .getServerManager().getVersion().getReleaseName();
                byte[] versionBytes = null;

                // 读取当前服务端版本的版本配置
                for (JSONObject versions : versionManifest.getList("versions", JSONObject.class)) {
                    if (versions.getString("id").equals(serverVersion)) {
                        versionBytes = HttpUtil.downloadFile(
                                HttpUtil.openConnection(versions.getString("url"))
                        );
                    }
                }
                JSONObject version = JSON.parseObject(versionBytes);

                // 读取服务端版本的资源文件配置
                byte[] assetsBytes = HttpUtil.downloadFile(HttpUtil.openConnection(
                        Objects.requireNonNull(version).getJSONObject("assetIndex").getString("url")
                ));
                JSONObject assets = JSON.parseObject(assetsBytes).getJSONObject("objects");

                // 获取中文语言文件的哈希值
                String langHash = assets.getJSONObject("minecraft/lang/zh_cn.json") != null ?
                        assets.getJSONObject("minecraft/lang/zh_cn.json").getString("hash") :
                        assets.getJSONObject("minecraft/lang/zh_CN.lang").getString("hash");

                // 下载中文语言文件
                HttpUtil.downloadFile(
                        HttpUtil.openConnection("https://bmclapi2.bangbang93.com/assets/" + langHash.substring(0, 2) + "/" + langHash),
                        file.toPath()
                );

                LogUtil.log("语言文件下载完成!");
                break;
            } catch (DownloadException | IOException e) {
                retryCount++;
                if (retryCount < 5) {
                    LogUtil.log("下载语言文件失败,正在重试... (第{}次)", String.valueOf(retryCount));
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e2) {
                        throw new RuntimeException(e2);
                    }
                }
                LogUtil.log("下载语言文件失败,不再重试!");
            }
        }
    }

    /**
     * 保存初始MC语言文件
     */
    public static void saveDefaultMinecraftLang() {
        downloadMinecraftLang();
        reloadMinecraftLang();
    }

    /**
     * 加载音效文件
     */
    public static void reloadMinecraftLang() {
        data = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * 获取物品名称
     *
     * @param item 物品实例
     * @return 物品名称
     */
    public static String getItemName(ItemStack item) {
        if (item.getItemMeta() != null && item.getItemMeta().hasDisplayName()) {
            return item.getItemMeta().getDisplayName();
        }

        if (data == null) {
            reloadMinecraftLang();
        }

        return data.getString(getKey(item));
    }

    /**
     * 获取指定物品实例对应语言文件中的key
     *
     * @param item 物品实例
     * @return key
     */
    private static String getKey(ItemStack item) {
        StringBuilder key = new StringBuilder();

        if (item.getType().isBlock()) {
            key.append("block");
        } else {
            key.append("item");
        }

        key.append(".minecraft.").append(item.getType().getKey().getKey());

        return key.toString();
    }
}
