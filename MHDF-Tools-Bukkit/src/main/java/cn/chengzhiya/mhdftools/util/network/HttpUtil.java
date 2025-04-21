package cn.chengzhiya.mhdftools.util.network;

import cn.chengzhiya.mhdftools.exception.DownloadException;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import com.google.common.io.ByteStreams;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class HttpUtil {
    /**
     * 通过URL地址打开URL连接
     *
     * @param urlString URL地址
     * @return URL连接
     */
    public static URLConnection openConnection(String urlString) throws IOException {
        URL url = new URL(urlString);

        Proxy proxy = Proxy.NO_PROXY;
        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("httpSettings");
        if (config != null) {
            if (config.getBoolean("proxy.enable")) {
                String type = config.getString("proxy.type");
                String host = config.getString("proxy.host");
                int port = config.getInt("proxy.port");

                proxy = new Proxy(Proxy.Type.valueOf(type), new InetSocketAddress(Objects.requireNonNull(host), port));
            }
        }

        URLConnection connection = url.openConnection(proxy);
        connection.setConnectTimeout((int) TimeUnit.SECONDS.toMillis(20));
        connection.setReadTimeout((int) TimeUnit.SECONDS.toMillis(20));
        return connection;
    }

    /**
     * 通过URL连接下载文件
     *
     * @param connection URL连接
     * @return 文件数据
     */
    public static byte[] downloadFile(URLConnection connection) throws DownloadException {
        try {
            try (InputStream in = connection.getInputStream()) {
                byte[] bytes = ByteStreams.toByteArray(in);
                if (bytes.length == 0) {
                    throw new DownloadException("无可下载文件");
                }
                return bytes;
            }
        } catch (Exception e) {
            throw new DownloadException(e);
        }
    }

    /**
     * 通过URL连接下载并保存文件
     *
     * @param connection URL连接
     * @param savePath   保存目录
     */
    public static void downloadFile(URLConnection connection, Path savePath) throws DownloadException {
        try {
            Files.write(savePath, downloadFile(connection));
        } catch (IOException e) {
            throw new DownloadException(e);
        }
    }
}
