package cn.chengzhiya.mhdftools.listener;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import lombok.Getter;

@Getter
public abstract class AbstractPacketListener extends AbstractListener implements PacketListener {
    private final PacketListenerPriority priority;

    public AbstractPacketListener(String enableKey, PacketListenerPriority priority) {
        super(enableKey);
        this.priority = priority;
    }
}
