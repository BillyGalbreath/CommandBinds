package net.pl3x.commandbinds.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.pl3x.commandbinds.mixin.KeyBindsScreenAccessor;
import net.pl3x.lib.animation.Animation;
import net.pl3x.lib.animation.Easing;
import org.jetbrains.annotations.NotNull;

public class ModalScreen extends Screen {
    private static final ResourceLocation RECIPE_BOOK = new ResourceLocation("textures/gui/recipe_book.png");

    private final Screen parent;
    private final KeyBindsScreenAccessor accessor;
    private final double scrollAmount;
    private final Animation fadeIn;

    public ModalScreen(@NotNull KeyBindsScreen parent) {
        super(Component.empty());
        this.parent = parent;
        this.accessor = (KeyBindsScreenAccessor) parent;
        this.scrollAmount = this.accessor.getKeyBindsList().getScrollAmount();
        this.fadeIn = new Animation(0, 1, 10, Easing.Quintic.out);
    }

    @Override
    public void init() {
        this.accessor.invokeInit();
    }

    @Override
    public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float delta) {
        renderBackground(gfx);
        RenderSystem.setShaderColor(1, 1, 1, 1);



        super.render(gfx, mouseX, mouseY, delta);
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics gfx) {
        // todo  - blur parent screen
        this.accessor.getKeyBindsList().setScrollAmount(this.scrollAmount);
        this.parent.render(gfx, -1, -1, -1);

        RenderSystem.setShaderColor(1, 1, 1, this.fadeIn.getValue());
        gfx.fillGradient(RenderType.guiOverlay(), 0, 0, this.width, this.height, 0xC0101010, 0xD0101010, 0);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
            this.accessor.getKeyBindsList().setScrollAmount(this.scrollAmount);
        }
    }
}
