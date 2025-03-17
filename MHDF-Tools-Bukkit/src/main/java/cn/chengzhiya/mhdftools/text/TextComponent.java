package cn.chengzhiya.mhdftools.text;

import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;

public abstract class TextComponent implements net.kyori.adventure.text.TextComponent {
    /**
     * 替换文本实例中的字符串
     *
     * @param target      被替换的字符串
     * @param replacement 替换后的字符串
     * @return 替换完成后的文本实例
     */
    public TextComponent replace(String target, String replacement) {
        TextReplacementConfig replacementConfig = TextReplacementConfig.builder()
                .matchLiteral(target)
                .replacement(replacement)
                .build();

        return (TextComponent) replaceText(replacementConfig);
    }

    /**
     * 替换文本实例中的字符串
     *
     * @param target      被替换的字符串
     * @param replacement 替换后的文本实例
     * @return 替换完成后的文本实例
     */
    public TextComponent replace(String target, TextComponent replacement) {
        TextReplacementConfig replacementConfig = TextReplacementConfig.builder()
                .matchLiteral(target)
                .replacement(replacement)
                .build();

        return (TextComponent) replaceText(replacementConfig);
    }

    /**
     * 转换为miniMessage字符串
     *
     * @return miniMessage字符串
     */
    public String toMiniMessageString() {
        return MiniMessage.miniMessage().serialize(this);
    }
}
