package net.pl3x.commandbinds.gui;

import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.pl3x.commandbinds.CommandBinds;
import net.pl3x.commandbinds.configuration.Config;
import net.pl3x.keyboard.Keyboard;
import net.pl3x.lib.gui.GL;
import net.pl3x.lib.gui.animation.Animation;
import net.pl3x.lib.util.Mathf;
import org.jetbrains.annotations.NotNull;

public class CategoryEntryRenderer {
    public static Animation HELLO;

    private final List<? extends GuiEventListener> children;

    private final KeyBindsList keyBindsList;
    private final CustomButton addBtn;
    private final Component name;

    private int width;

    public CategoryEntryRenderer(@NotNull KeyBindsList keyBindsList, @NotNull Component name) {
        this.keyBindsList = keyBindsList;
        this.name = name;
        this.width = Minecraft.getInstance().font.width(this.name);

        if (this.name.getContents() instanceof TranslatableContents translatable && translatable.getKey().equals(CommandBinds.CATEGORY_TITLE)) {
            this.addBtn = new CustomButton(0, 0, 16, 16, 0, 0, 16, 16, Component.empty(), btn -> {
                CommandKey key = new CommandKey(-1, "");
                Config.COMMAND_KEYBINDS.add(key);
                Config.save();
                Keyboard.bindKey(key);
                CommandBinds.refreshKeyBindScreen();
            });
            this.addBtn.setTooltip(Tooltip.create(Component.translatable("commandbinds.button.add")));
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
            this.width = Minecraft.getInstance().font.width(this.name);

            this.addBtn.setX(left + 67 - this.keyBindsList.maxNameWidth);
            this.addBtn.setY(top + 3);
            this.addBtn.render(gfx, mouseX, mouseY, delta);
        }

        gfx.pose().pushPose();

        float x = this.keyBindsList.keyBindsScreen.width / 2F;
        int y = top + height - 10;

        if (HELLO != null && !HELLO.isFinished()) {
            gfx.pose().translate(0, 0, 100);
            GL.scaleScene(gfx, x, y, 1 + Mathf.sin(Mathf.PI * HELLO.getValue()) / 4);
            GL.rotateScene(gfx, x, y, Mathf.sin(Mathf.PI * 4 * HELLO.getValue()) * 30);
        }

        gfx.drawString(Minecraft.getInstance().font, this.name, (int) (x - this.width / 2F), y, 0xFFFFFF, false);

        gfx.pose().popPose();
    }
}
