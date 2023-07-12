package net.pl3x.commandbinds.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.pl3x.commandbinds.CommandBinds;
import net.pl3x.commandbinds.gui.CustomButton;
import net.pl3x.lib.gui.GL;
import net.pl3x.lib.gui.animation.Animation;
import net.pl3x.lib.gui.animation.Easing;
import net.pl3x.lib.util.Mathf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModalScreen extends Screen {
    protected final Screen parent;

    protected final float modalWidth;
    protected final float modalHeight;
    protected final float initialX;
    protected final float initialY;

    protected final Animation cubicOut;
    protected final Animation elasticOut;
    protected final boolean animate;

    protected int centerX;
    protected int centerY;
    protected int top;
    protected int left;

    public ModalScreen(@Nullable KeyBindsScreen parent, @NotNull Component title, float modalWidth, float modalHeight, float initialX, float initialY, boolean animate) {
        super(title);
        this.parent = parent;

        this.modalWidth = modalWidth;
        this.modalHeight = modalHeight;

        this.initialX = initialX;
        this.initialY = initialY;

        this.cubicOut = new Animation(0, 1, 10, Easing.Cubic.out);
        this.elasticOut = new Animation(0, 1, 20, Easing.Elastic.out);

        this.animate = animate;
    }

    @Override
    public void init() {
        super.init();

        if (this.parent != null) {
            this.parent.init();
        }

        this.centerX = (int) (this.width / 2F);
        this.centerY = (int) (this.height / 2F);

        this.top = (int) (this.centerY - this.modalHeight / 2F);
        this.left = (int) (this.centerX - this.modalWidth / 2F);

        CustomButton closeBtn = new CustomButton(this.left + this.modalWidth - 16, this.top + 3, 13, 13, 32, 3, 45, 16, Component.literal("x"), btn -> onClose());
        closeBtn.setTooltip(Tooltip.create(Component.translatable("commandbinds.button.close")));
        addRenderableWidget(closeBtn);
    }

    @Override
    public void render(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float delta) {
        renderBackground(gfx);

        gfx.pose().pushPose();

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1, 1, 1, this.animate ? this.cubicOut.getValue() : 1);

        if (!this.elasticOut.isFinished()) {
            GL.scaleScene(gfx,
                    Mathf.lerp(this.initialX, this.width / 2F, this.cubicOut.getValue()),
                    Mathf.lerp(this.initialY, this.height / 2F, this.cubicOut.getValue()),
                    this.elasticOut.getValue());
        }

        renderModal(gfx);

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1, 1, 1, this.animate ? this.cubicOut.getValue() : 1);
        super.render(gfx, mouseX, mouseY, delta);

        gfx.pose().popPose();
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics gfx) {
        gfx.pose().pushPose();
        gfx.pose().translate(0, 0, -100);

        // todo  - blur parent screen
        if (this.parent != null) {
            this.parent.render(gfx, -1, -1, -1);
        }

        RenderSystem.setShaderColor(1, 1, 1, this.animate ? this.cubicOut.getValue() : 1);
        gfx.fillGradient(RenderType.guiOverlay(), 0, 0, this.width, this.height, 0xC0101010, 0xD0101010, 0);

        gfx.pose().popPose();
    }

    public void renderModal(@NotNull GuiGraphics gfx) {
        int x = (int) (this.width / 2F - this.modalWidth / 2F);
        int y = (int) (this.height / 2F - this.modalWidth / 2F);

        GL.nineSlice(gfx, CommandBinds.TEXTURES_LOCATION,
                x, y,
                (int) this.modalWidth, (int) this.modalWidth,
                6, 0, 32, 16, 48,
                CommandBinds.TEXTURES_WIDTH,
                CommandBinds.TEXTURES_HEIGHT
        );

        GL.drawSolidRect(gfx, x + 3, y + 3, this.modalWidth - 6, 13, 0xFFB0B0B0);

        gfx.drawString(this.font, this.title,
                (int) (this.width / 2F - this.font.width(this.title) / 2F),
                y + 6,
                0x3F3F3F, false);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }
}
