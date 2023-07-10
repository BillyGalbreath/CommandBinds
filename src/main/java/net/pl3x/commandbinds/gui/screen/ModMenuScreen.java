package net.pl3x.commandbinds.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.pl3x.commandbinds.gui.CategoryEntryRenderer;
import net.pl3x.commandbinds.gui.CustomKey;
import net.pl3x.lib.gui.animation.Animation;
import net.pl3x.lib.gui.animation.Easing;
import org.jetbrains.annotations.NotNull;

public class ModMenuScreen extends KeyBindsScreen {
    private final Animation scrollAnimation = new Animation(0, 1, 25, Easing.Circular.inOut);

    private int targetScrollAmount;

    public ModMenuScreen(Screen parent) {
        super(parent, Minecraft.getInstance().options);
    }

    @Override
    public void init() {
        super.init();

        if (this.scrollAnimation.isFinished()) {
            return;
        }

        int i = -1;
        //keyBindsList.children().indexOf()
        for (KeyBindsList.Entry entry : this.keyBindsList.children()) {
            i++;
            if (!(entry instanceof KeyBindsList.CategoryEntry category)) {
                continue;
            }
            if (!((TranslatableContents) category.name.getContents()).getKey().equals(CustomKey.CATEGORY)) {
                continue;
            }
            this.targetScrollAmount = (int) (this.keyBindsList.itemHeight * i + this.keyBindsList.itemHeight - this.height / 2F);
            return;
        }
    }

    @Override
    public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float delta) {
        if (!this.scrollAnimation.isFinished()) {
            this.keyBindsList.setScrollAmount(this.scrollAnimation.getValue() * this.targetScrollAmount);
        } else if (CategoryEntryRenderer.HELLO == null) {
            CategoryEntryRenderer.HELLO = new Animation(0, 1, 20, Easing.Cubic.inOut);
        }

        super.render(gfx, mouseX, mouseY, delta);
    }
}
