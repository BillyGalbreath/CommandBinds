package net.pl3x.commandbinds.gui;

import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.pl3x.commandbinds.CommandBinds;
import net.pl3x.commandbinds.mixin.KeyBindsListAccessor;
import net.pl3x.keyboard.Keyboard;
import org.jetbrains.annotations.NotNull;

public class CategoryEntryRenderer {
    private final KeyBindsListAccessor keyBindsList;
    private final Component name;
    private final int width;
    private final CustomButton addBtn;

    private final List<? extends GuiEventListener> children;

    public CategoryEntryRenderer(@NotNull KeyBindsList keyBindsList, @NotNull Component name) {
        this.keyBindsList = (KeyBindsListAccessor) keyBindsList;
        this.name = name;
        this.width = Minecraft.getInstance().font.width(this.name);

        if (this.name.getContents() instanceof TranslatableContents translatable && translatable.getKey().equals(CustomKey.CATEGORY)) {
            this.addBtn = new CustomButton(0, 0, 16, 16, 0, 0, 16, 16, btn -> {
                Keyboard.bindKey(new CustomKey(-1, false, false, false, false, ""));
                CommandBinds.refreshKeyBindScreen();
            });
            this.children = ImmutableList.of(this.addBtn);
        } else {
            this.addBtn = null;
            this.children = Collections.emptyList();
        }
    }

    public List<? extends GuiEventListener> children() {
        return this.children;
    }

    public void render(GuiGraphics gfx, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float delta) {
        if (this.addBtn != null) {
            this.addBtn.setX(left + 67 - this.keyBindsList.getMaxNameWidth());
            this.addBtn.setY(top + 3);
            this.addBtn.render(gfx, mouseX, mouseY, delta);
        }

        gfx.drawString(Minecraft.getInstance().font, this.name, this.keyBindsList.getKeyBindsScreen().width / 2 - this.width / 2, top + height - 10, 0xFFFFFF, false);
    }
}
