package net.pl3x.commandbinds.gui;

import java.util.Collections;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.pl3x.commandbinds.CommandBinds;
import net.pl3x.commandbinds.configuration.Config;
import net.pl3x.keyboard.Keyboard;
import org.jetbrains.annotations.NotNull;

public class KeyEntryRenderer implements Tickable {
    private final KeyBindsList keyBindsList;
    private final List<GuiEventListener> children;

    private CommandKey commandKey;

    public KeyEntryRenderer(@NotNull KeyBindsList keyBindsList, @NotNull KeyMapping mapping, @NotNull Component name) {
        this.keyBindsList = keyBindsList;

        if (mapping.getName().startsWith(String.format(CommandBinds.KEY_NAME, ""))) {
            this.commandKey = Keyboard.getKey(mapping.getName()) instanceof CommandKey cmdKey ? cmdKey : null;
        }

        if (this.commandKey != null) {
            this.children = this.commandKey.children();
        } else {
            this.children = Collections.emptyList();
        }
    }

    public boolean render(@NotNull GuiGraphics gfx, int top, int left, int height, int mouseX, int mouseY, float delta) {
        if (this.commandKey != null) {
            this.commandKey.render(gfx, top, left, this.keyBindsList.maxNameWidth, height, mouseX, mouseY, delta);
            return true;
        }
        return false;
    }

    public @NotNull MutableComponent refreshEntry(@NotNull MutableComponent instance, @NotNull Component name) {
        Config.save();
        if (name.getContents() instanceof TranslatableContents contents &&
                contents.getKey().startsWith(String.format(CommandBinds.KEY_NAME, "")) &&
                Keyboard.getKey(contents.getKey()) instanceof CommandKey cmdKey) {
            return instance.append(Component.literal(String.format("/%s", cmdKey.getCommand())));
        }
        return instance.append(name);
    }

    public @NotNull List<GuiEventListener> children(@NotNull List<GuiEventListener> list) {
        list.addAll(0, this.children);
        return list;
    }

    @Override
    public void tickable$init() {
        if (this.commandKey != null) {
            this.commandKey.tickable$init();
        }
    }

    @Override
    public void tickable$tick() {
        if (this.commandKey != null) {
            this.commandKey.tickable$tick();
        }
    }
}
