package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractPacketListener;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.YamlUtil;
import cn.chengzhiya.mhdftools.util.feature.MotdUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.status.server.WrapperStatusServerResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;
import java.util.Random;

public final class Motd extends AbstractPacketListener {
    public Motd() {
        super(
                "motdSettings.enable",
                PacketListenerPriority.NORMAL
        );
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Status.Server.RESPONSE) {
            return;
        }

        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("motdSettings");
        if (config == null) {
            return;
        }

        WrapperStatusServerResponse packet = new WrapperStatusServerResponse(event);
        JsonObject data = packet.getComponent();

        ConfigurationSection version = config.getConfigurationSection("version");
        if (version != null) {
            JsonObject versionData = new JsonObject();

            String name = Main.instance.getPluginHookManager().getPlaceholderAPIHook()
                    .placeholder(null, version.getString("name", ""));
            versionData.addProperty("name", ColorUtil.color(name).toLegacyString());
            versionData.addProperty("protocol", 5835);

            data.add("version", versionData);
        }

        List<YamlConfiguration> descriptionList = YamlUtil.getConfigurationSectionList(config, "description");
        {
            YamlConfiguration description = descriptionList.get(new Random().nextInt(descriptionList.size()));
            JsonArray descriptionData = new JsonArray();

            String line1 = Main.instance.getPluginHookManager().getPlaceholderAPIHook()
                    .placeholder(null, description.getString("line1", ""));
            descriptionData.addAll(MotdUtil.getMessageJsonArray(ColorUtil.color(line1)));

            JsonObject nextLine = new JsonObject();
            nextLine.addProperty("text", "\n");
            nextLine.addProperty("color", "white");

            descriptionData.add(nextLine);

            String line2 = Main.instance.getPluginHookManager().getPlaceholderAPIHook()
                    .placeholder(null, description.getString("line2", ""));
            descriptionData.addAll(MotdUtil.getMessageJsonArray(ColorUtil.color(line2)));


            data.add("description", descriptionData);
        }

        packet.setComponentJson(data.toString());

        event.setLastUsedWrapper(packet);
        event.markForReEncode(true);
    }
}
