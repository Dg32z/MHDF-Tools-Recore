package cn.chengzhiya.mhdftools.libraries;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;

public enum Dependency {
    // 依赖处理
    ASM(
            "org{}ow2{}asm",
            "asm",
            "9.7.1",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    ASM_COMMONS(
            "org{}ow2{}asm",
            "asm-commons",
            "9.7.1",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    JAR_RELOCATOR(
            "me{}lucko",
            "jar-relocator",
            "1.7",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),

    MHDF_SCHEDULER(
            "cn{}chengzhiya",
            "MHDF-Scheduler",
            "1.0.1",
            Repository.CHENGZHIMEOW,
            true
    ),
    MHDF_LANGUTIL(
            "cn{}chengzhiya",
            "MHDF-LangUtil",
            "1.2.5",
            Repository.CHENGZHIMEOW,
            true
    ),

    // JSON处理
    FAST_JSON(
            "com{}alibaba{}fastjson2",
            "fastjson2",
            "2.0.53",
            Repository.MAVEN_CENTRAL_MIRROR,
            true
    ),

    // 反射处理
    REFLECTIONS(
            "org{}reflections",
            "reflections",
            "0.10.2",
            Repository.MAVEN_CENTRAL_MIRROR,
            true
    ),
    JAVASSITST(
            "org{}javassist",
            "javassist",
            "3.28.0-GA",
            Repository.MAVEN_CENTRAL_MIRROR,
            true,
            "javassist"
    ),

    // packetevents-api
    PACKETEVENTS_API(
            "com{}github{}retrooper",
            "packetevents-api",
            "2.8.0",
            Repository.CODE_MC,
            true,
            "io{}github{}retrooper"
    ),
    PACKETEVENTS_NETTY_COMMON(
            "com{}github{}retrooper",
            "packetevents-netty-common",
            "2.8.0",
            Repository.CODE_MC,
            true
    ),
    PACKETEVENTS_SPIGOT(
            "com{}github{}retrooper",
            "packetevents-spigot",
            "2.8.0",
            Repository.CODE_MC,
            true
    ),

    // 数据库
    ORMLITE_CORE(
            "com{}j256{}ormlite",
            "ormlite-core",
            "6.1",
            Repository.MAVEN_CENTRAL_MIRROR,
            true
    ),
    ORMLITE_JDBC(
            "com{}j256{}ormlite",
            "ormlite-jdbc",
            "6.1",
            Repository.MAVEN_CENTRAL_MIRROR,
            true
    ),
    HIKARI_CP(
            "com{}zaxxer",
            "HikariCP",
            "6.1.0",
            Repository.MAVEN_CENTRAL_MIRROR,
            true
    ),
    H2_DRIVER(
            "com{}h2database",
            "h2",
            "2.3.232",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    MYSQL_DRIVER(
            "com{}mysql",
            "mysql-connector-j",
            "9.1.0",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),

    // redis
    LETTUCE_CORE(
            "io{}lettuce",
            "lettuce-core",
            "6.5.5.RELEASE",
            Repository.MAVEN_CENTRAL_MIRROR,
            true
    ),
    REACTOR_CORE(
            "io{}projectreactor",
            "reactor-core",
            "3.6.6",
            Repository.MAVEN_CENTRAL_MIRROR,
            true,
            "reactor"
    ),
    REACTIVE_STREAMS(
            "org{}reactivestreams",
            "reactive-streams",
            "1.0.4",
            Repository.MAVEN_CENTRAL_MIRROR,
            true
    ),

    EXP4J(
            "net{}objecthunter",
            "exp4j",
            "0.4.8",
            Repository.MAVEN_CENTRAL_MIRROR,
            true
    ),

    // adventure-api
    ADVENTURE_API(
            "net{}kyori",
            "adventure-api",
            "4.21.0",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    ADVENTURE_KEY(
            "net{}kyori",
            "adventure-key",
            "4.21.0",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    ADVENTURE_NBT(
            "net{}kyori",
            "adventure-nbt",
            "4.21.0",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    ADVENTURE_PLATFORM_API(
            "net{}kyori",
            "adventure-platform-api",
            "4.3.4",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    ADVENTURE_PLATFORM_BUKKIT(
            "net{}kyori",
            "adventure-platform-bukkit",
            "4.4.0",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    ADVENTURE_PLATFORM_FACET(
            "net{}kyori",
            "adventure-platform-facet",
            "4.4.0",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    ADVENTURE_PLATFORM_VIAVERSION(
            "net{}kyori",
            "adventure-platform-viaversion",
            "4.4.0",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    ADVENTURE_TEXT_LOGGER_SLF4J(
            "net{}kyori",
            "adventure-text-logger-slf4j",
            "4.21.0",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    ADVENTURE_TEXT_MINIMESSAGE(
            "net{}kyori",
            "adventure-text-minimessage",
            "4.21.0",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    ADVENTURE_TEXT_SERIALIZER_ANSI(
            "net{}kyori",
            "adventure-text-serializer-ansi",
            "4.21.0",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    ADVENTURE_TEXT_SERIALIZER_BUNGEECORD(
            "net{}kyori",
            "adventure-text-serializer-bungeecord",
            "4.4.0",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    ADVENTURE_TEXT_SERIALIZER_GSON(
            "net{}kyori",
            "adventure-text-serializer-gson",
            "4.21.0",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    ADVENTURE_TEXT_SERIALIZER_GSON_LEGACY_IMPL(
            "net{}kyori",
            "adventure-text-serializer-gson-legacy-impl",
            "4.21.0",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    ADVENTURE_TEXT_SERIALIZER_JSON(
            "net{}kyori",
            "adventure-text-serializer-json",
            "4.21.0",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    ADVENTURE_TEXT_SERIALIZER_JSON_LEGACY_IMPL(
            "net{}kyori",
            "adventure-text-serializer-json-legacy-impl",
            "4.21.0",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    ADVENTURE_TEXT_SERIALIZER_LEGACY(
            "net{}kyori",
            "adventure-text-serializer-legacy",
            "4.21.0",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    ADVENTURE_TEXT_SERIALIZER_PLAIN(
            "net{}kyori",
            "adventure-text-serializer-plain",
            "4.21.0",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    ANSI(
            "net{}kyori",
            "ansi",
            "1.0.3",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    EXAMINATION_API(
            "net{}kyori",
            "examination-api",
            "1.3.0",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    EXAMINATION_STRING(
            "net{}kyori",
            "examination-string",
            "1.3.0",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    ),
    OPTION(
            "net{}kyori",
            "option",
            "1.1.0",
            Repository.MAVEN_CENTRAL_MIRROR,
            false
    );

    private final String artifact;
    private final String version;
    @Getter
    private final Repository repository;
    @Getter
    private final String mavenRepoPath;
    @Getter
    private final String groupId;
    @Getter
    private final boolean relocatable;
    @Getter
    private final String[] relocator;

    Dependency(@NotNull String groupId, @NotNull String artifactId, @NotNull String version, @NotNull Repository repository, boolean relocatable, String... relocator) {
        this.mavenRepoPath = String.format("%s/%s/%s/%s-%s.jar",
                groupId.replace("{}", "/"),
                artifactId,
                version,
                artifactId,
                version
        );
        this.groupId = groupId.replace("{}", ".");
        this.version = version;
        this.repository = repository;
        this.artifact = artifactId;
        this.relocatable = relocatable;
        this.relocator = Arrays.stream(relocator)
                .map(s -> s.replace("{}", "."))
                .toArray(String[]::new);
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
