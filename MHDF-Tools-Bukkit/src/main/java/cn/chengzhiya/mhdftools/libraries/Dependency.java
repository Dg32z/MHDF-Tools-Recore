package cn.chengzhiya.mhdftools.libraries;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum Dependency {
    // 依赖处理
    ASM(
            "org{}ow2{}asm",
            "asm",
            "9.7.1",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ASM_COMMONS(
            "org{}ow2{}asm",
            "asm-commons",
            "9.7.1",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    JAR_RELOCATOR(
            "me{}lucko",
            "jar-relocator",
            "1.7",
            Repository.MAVEN_CENTRAL_MIRROR
    ),

    // JSON处理
    FAST_JSON(
            "com{}alibaba{}fastjson2",
            "fastjson2",
            "2.0.53",
            Repository.MAVEN_CENTRAL_MIRROR
    ),

    // 反射处理
    REFLECTIONS(
            "org{}reflections",
            "reflections",
            "0.10.2",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    JAVASSITST(
            "org{}javassist",
            "javassist",
            "3.28.0-GA",
            Repository.MAVEN_CENTRAL_MIRROR
    ),

    // packetevents-api
    PACKETEVENTS_API(
            "com{}github{}retrooper",
            "packetevents-api",
            "2.7.0",
            Repository.CODE_MC
    ),
    PACKETEVENTS_NETTY_COMMON(
            "com{}github{}retrooper",
            "packetevents-netty-common",
            "2.7.0",
            Repository.CODE_MC
    ),
    PACKETEVENTS_SPIGOT(
            "com{}github{}retrooper",
            "packetevents-spigot",
            "2.7.0",
            Repository.CODE_MC
    ),

    // item-nbt-api
    ITEM_NBT_API(
            "de{}tr7zw",
            "item-nbt-api",
            "2.14.1",
            Repository.CODE_MC
    ),

    // 数据库
    ORMLITE_CORE(
            "com{}j256{}ormlite",
            "ormlite-core",
            "6.1",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ORMLITE_JDBC(
            "com{}j256{}ormlite",
            "ormlite-jdbc",
            "6.1",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    HIKARI_CP(
            "com{}zaxxer",
            "HikariCP",
            "6.1.0",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    H2_DRIVER(
            "com{}h2database",
            "h2",
            "2.3.232",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    MYSQL_DRIVER(
            "com{}mysql",
            "mysql-connector-j",
            "9.1.0",
            Repository.MAVEN_CENTRAL_MIRROR
    ),

    // redis
    LETTUCE_CORE(
            "io{}lettuce",
            "lettuce-core",
            "6.5.3.RELEASE",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    REACTOR_CORE(
            "io{}projectreactor",
            "reactor-core",
            "3.6.6",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    REACTIVE_STREAMS(
            "org{}reactivestreams",
            "reactive-streams",
            "1.0.4",
            Repository.MAVEN_CENTRAL_MIRROR
    ),

    // adventure-api
    ADVENTURE_API(
            "net{}kyori",
            "adventure-api",
            "4.18.0",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_KEY(
            "net{}kyori",
            "adventure-key",
            "4.18.0",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_NBT(
            "net{}kyori",
            "adventure-nbt",
            "4.18.0",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_PLATFORM_API(
            "net{}kyori",
            "adventure-platform-api",
            "4.3.4",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_PLATFORM_BUKKIT(
            "net{}kyori",
            "adventure-platform-bukkit",
            "4.3.4",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_PLATFORM_FACET(
            "net{}kyori",
            "adventure-platform-facet",
            "4.3.4",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_PLATFORM_VIAVERSION(
            "net{}kyori",
            "adventure-platform-viaversion",
            "4.3.4",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_TEXT_LOGGER_SLF4J(
            "net{}kyori",
            "adventure-text-logger-slf4j",
            "4.18.0",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_TEXT_MINIMESSAGE(
            "net{}kyori",
            "adventure-text-minimessage",
            "4.18.0",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_TEXT_SERIALIZER_ANSI(
            "net{}kyori",
            "adventure-text-serializer-ansi",
            "4.18.0",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_TEXT_SERIALIZER_BUNGEECORD(
            "net{}kyori",
            "adventure-text-serializer-bungeecord",
            "4.3.4",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_TEXT_SERIALIZER_GSON(
            "net{}kyori",
            "adventure-text-serializer-gson",
            "4.18.0",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_TEXT_SERIALIZER_GSON_LEGACY_IMPL(
            "net{}kyori",
            "adventure-text-serializer-gson-legacy-impl",
            "4.18.0",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_TEXT_SERIALIZER_JSON(
            "net{}kyori",
            "adventure-text-serializer-json",
            "4.18.0",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_TEXT_SERIALIZER_LEGACY(
            "net{}kyori",
            "adventure-text-serializer-legacy",
            "4.18.0",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_TEXT_SERIALIZER_PLAIN(
            "net{}kyori",
            "adventure-text-serializer-plain",
            "4.18.0",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ANSI(
            "net{}kyori",
            "ansi",
            "1.0.3",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    EXAMINATION_API(
            "net{}kyori",
            "examination-api",
            "1.3.0",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    EXAMINATION_STRING(
            "net{}kyori",
            "examination-string",
            "1.3.0",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    OPTION(
            "net{}kyori",
            "option",
            "1.0.0",
            Repository.MAVEN_CENTRAL_MIRROR
    );

    private final String artifact;
    private final String version;
    @Getter
    private final Repository repository;
    @Getter
    private final String mavenRepoPath;
    @Getter
    private final String groupId;

    Dependency(@NotNull String groupId, @NotNull String artifactId, @NotNull String version, @NotNull Repository repository) {
        this.mavenRepoPath = String.format("%s/%s/%s/%s-%s.jar",
                groupId.replace("{}", ".").replace(".", "/"),
                artifactId,
                version,
                artifactId,
                version
        );
        this.groupId = groupId.replace("{}", ".");
        this.version = version;
        this.repository = repository;
        this.artifact = artifactId;
    }

    /**
     * 获取依赖文件名称
     *
     * @return 文件名称
     */
    public String getFileName() {
        String name = artifact.toLowerCase(Locale.ROOT).replace('_', '-');
        return name + "-" + this.version + ".jar";
    }
}
