package net.pl3x.commandbinds.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class KeyBindsModal extends ModalScreen {
    private final KeyBindsScreen keyBindsScreen;
    private final double scrollAmount;

    public KeyBindsModal(@NotNull KeyBindsScreen parent, float dialogWidth, float dialogHeight, float initialX, float initialY, boolean animate) {
        super(parent, Component.translatable("commandbinds.modal.options.title"), dialogWidth, dialogHeight, initialX, initialY, animate);
        this.keyBindsScreen = parent;
        this.scrollAmount = this.keyBindsScreen.keyBindsList.getScrollAmount();
    }

    @Override
    public void init() {
        super.init();
        this.keyBindsScreen.init();
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics gfx) {
        this.keyBindsScreen.keyBindsList.setScrollAmount(this.scrollAmount);
        super.renderBackground(gfx);
    }

    @Override
    public void onClose() {
        super.onClose();
        this.keyBindsScreen.keyBindsList.setScrollAmount(this.scrollAmount);
    }
}
