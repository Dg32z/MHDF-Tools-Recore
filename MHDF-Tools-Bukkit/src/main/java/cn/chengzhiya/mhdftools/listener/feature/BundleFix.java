package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractPacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSelectBundleItem;

public class BundleFix extends AbstractPacketListener {

    public BundleFix() {
        super(
                "bundleFixSettings.enable",
                PacketListenerPriority.LOW
        );
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.SELECT_BUNDLE_ITEM) {
            return;
        }

        if (Main.instance.getPluginHookManager().getPacketEventsHook().getServerVersion().isOlderThanOrEquals(ServerVersion.V_1_21)) {
            return;
        }

        final WrapperPlayClientSelectBundleItem wrapper = new WrapperPlayClientSelectBundleItem(event);

        final int selItem = wrapper.getSelectedItemIndex();

        if (selItem < -1) { // patch for https://github.com/PaperMC/Folia/issues/356
            return;
        }

        event.setCancelled(true); // cancel the packet (:
    }
}
