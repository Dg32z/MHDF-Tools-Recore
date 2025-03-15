package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.Random;

public final class BlackWord extends AbstractListener {
    public BlackWord() {
        super(
                "blackWordSettings.enable"
        );
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();

        String type = ConfigUtil.getConfig().getString("blackWordSettings.replace.type");
        if (type == null) {
            return;
        }

        for (String s : ConfigUtil.getConfig().getStringList("blackWordSettings.word")) {
            if (message.contains(s)) {
                switch (type) {
                    case "line" -> {
                        List<String> lineList = ConfigUtil.getConfig().getStringList("blackWordSettings.replace.lineList");
                        if (lineList.isEmpty()) {
                            return;
                        }

                        message = message.replaceAll(s, lineList.get(new Random().nextInt(lineList.size())));
                    }
                    case "word" -> {
                        String word = ConfigUtil.getConfig().getString("blackWordSettings.replace.word");
                        if (word == null) {
                            return;
                        }

                        message = message.replaceAll(s, word);
                    }
                }
            }
        }

        event.setMessage(message);
    }
}
