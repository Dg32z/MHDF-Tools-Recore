package cn.chengzhiya.mhdftools.text;

import net.kyori.adventure.text.Component;

@SuppressWarnings("UnusedReturnValue")
public final class TextComponentBuilder {
    private TextComponent textComponent;

    public TextComponentBuilder() {
        this.textComponent = (TextComponent) Component.empty();
    }

    public TextComponentBuilder(Component component) {
        this.textComponent = (TextComponent) component;
    }

    public TextComponentBuilder append(Component component) {
        this.textComponent = (TextComponent) this.textComponent.append(component);
        return this;
    }

    public TextComponentBuilder appendNewline() {
        this.textComponent = (TextComponent) this.textComponent.appendNewline();
        return this;
    }

    public TextComponent build() {
        return this.textComponent;
    }
}
