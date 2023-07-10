package net.pl3x.commandbinds.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import net.pl3x.commandbinds.CommandBinds;
import net.pl3x.commandbinds.gui.screen.KeyBindsModal;
import net.pl3x.keyboard.Key;
import org.jetbrains.annotations.NotNull;

public class CustomKey extends Key implements Tickable {
    public static final String CATEGORY = String.format("%s.title", CommandBinds.MODID);
    public static final String NAME = String.format("%s.keymap.command.%%s", CommandBinds.MODID);
    private static final AtomicInteger NEXT_ID = new AtomicInteger(0);

    public final List<GuiEventListener> children = new ArrayList<>();

    private String command;
    private EditBox editbox;
    private CustomButton removeBtn;

    public CustomKey(int keycode, boolean alt, boolean ctrl, boolean meta, boolean shift, @NotNull String command) {
        super(CATEGORY, String.format(NAME, NEXT_ID.getAndIncrement()), keycode, alt, ctrl, meta, shift);
        setCommand(command);
    }

    @Override
    public @NotNull Action getAction() {
        return client -> {
            if (client.player != null) {
                client.player.connection.sendCommand(getCommand());
            }
        };
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void execute(@NotNull Minecraft client) {
        super.execute(client);
    }

    public void tickable$init() {
        this.removeBtn = new CustomButton(0, 0, 16, 16, 16, 0, 32, 16, Component.empty(), btn -> {
            //Keyboard.unbindKey(this);
            //CommandBinds.refreshKeyBindScreen();
            Minecraft client = Minecraft.getInstance();
            if (client.screen instanceof KeyBindsScreen screen) {
                float w = btn.getWidth() / 2F;
                client.setScreen(new KeyBindsModal(screen, 250, 100, (int) (btn.getX() + w), (int) (btn.getY() + w), true));
            }
        });

        this.editbox = new EditBox(Minecraft.getInstance().font, 0, 0, 0, 0, Component.empty());
        this.editbox.setMaxLength(1024);
        this.editbox.setValue(String.format("/%s", getCommand()));
        this.editbox.setResponder(value -> setCommand(value.startsWith("/") ? value.substring(1) : value));

        this.children.clear();
        this.children.add(this.removeBtn);
        this.children.add(this.editbox);
    }

    public List<? extends GuiEventListener> children() {
        return this.children;
    }

    public void tickable$tick() {
        if (this.editbox != null) {
            this.editbox.tick();
        }
    }

    public void render(@NotNull GuiGraphics gfx, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float delta, int maxWidth, Button changeButton, Button resetButton, boolean hasCollision) {
        if (this.editbox != null) {
            this.editbox.setX(left + 90 - maxWidth);
            this.editbox.setY((top + height / 2) - 6);
            this.editbox.setWidth(maxWidth + 3);
            this.editbox.height = height;
            this.editbox.setValue(String.format("/%s", getCommand()));
            this.editbox.render(gfx, mouseX, mouseY, delta);
        }

        this.removeBtn.setX(left + 67 - maxWidth);
        this.removeBtn.setY(top + 3);
        this.removeBtn.render(gfx, mouseX, mouseY, delta);

        resetButton.setX(left + 190);
        resetButton.setY(top);
        resetButton.render(gfx, mouseX, mouseY, delta);

        changeButton.setX(left + 105);
        changeButton.setY(top);
        changeButton.render(gfx, mouseX, mouseY, delta);

        if (hasCollision) {
            int q = changeButton.getX() - 6;
            gfx.fill(q, top + 2, q + 3, top + height + 2, 0xFFFF5555);
        }
    }
}
