package net.pl3x.commandbinds.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.pl3x.commandbinds.CommandBinds;
import net.pl3x.commandbinds.gui.CategoryEntryRenderer;
import net.pl3x.lib.gui.animation.Animation;
import net.pl3x.lib.gui.animation.Easing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModMenuScreen extends KeyBindsScreen {
    private final Animation scrollAnimation;

    private int targetScrollAmount;

    public ModMenuScreen(@Nullable Screen parent) {
        super(parent, Minecraft.getInstance().options);
        this.scrollAnimation = new Animation(0, 1, 15, Easing.Exponential.inOut);
        CategoryEntryRenderer.HELLO = null;
    }

    @Override
    public void init() {
        @SuppressWarnings("ConstantValue") // this really can be null before first init() call
        double scrollAmount = this.keyBindsList != null ? this.keyBindsList.getScrollAmount() : 0;
        super.init();
        this.keyBindsList.setScrollAmount(scrollAmount);

        if (this.scrollAnimation.isFinished()) {
            return;
        }

        int i = -1;
        for (KeyBindsList.Entry entry : this.keyBindsList.children()) {
            i++;
            if (!(entry instanceof KeyBindsList.CategoryEntry category)) {
                continue;
            }
            if (!(category.name.getContents() instanceof TranslatableContents contents)) {
                continue;
            }
            if (!contents.getKey().equals(CommandBinds.CATEGORY_TITLE)) {
                continue;
            }
            this.targetScrollAmount = (int) (this.keyBindsList.itemHeight * i + this.keyBindsList.itemHeight - this.height / 4F);
            return;
        }
    }

    @Override
    public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float delta) {
        if (!this.scrollAnimation.isFinished() && this.keyBindsList.getScrollAmount() < this.keyBindsList.getMaxScroll()) {
            this.keyBindsList.setScrollAmount(this.scrollAnimation.getValue() * this.targetScrollAmount);
        } else if (CategoryEntryRenderer.HELLO == null) {
            CategoryEntryRenderer.HELLO = new Animation(0, 1, 15, Easing.Cubic.inOut);
        }

        super.render(gfx, mouseX, mouseY, delta);
    }
}
