package cn.chengzhiya.mhdftools.enums;

import lombok.Getter;

@Getter
public enum TeleportRequestType {
    TPA("tpa.yml"),
    TPAHERE("tpahere.yml");

    private final String menu;

    TeleportRequestType(String menu) {
        this.menu = menu;
    }
}
