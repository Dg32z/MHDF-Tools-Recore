package cn.chengzhiya.mhdftools.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class DatabaseConfig {
    private String type;
    private String host;
    private String database;
    private String filePath;
    private String user;
    private String password;
}
