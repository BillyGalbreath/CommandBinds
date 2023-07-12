package net.pl3x.commandbinds.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.pl3x.commandbinds.CommandBinds;
import net.pl3x.lib.gui.GL;
import net.pl3x.lib.gui.animation.Animation;
import net.pl3x.lib.gui.animation.Easing;
import org.jetbrains.annotations.NotNull;

public class CustomButton extends Button {
    private final float u0, v0, u1, v1;

    private Animation animation = new Animation(0, 0, 0, Easing.Linear.flat);
    private boolean activated = false;

    public CustomButton(float x, float y, float width, float height, float u0, float v0, float u1, float v1, @NotNull Component text, @NotNull OnPress onPress) {
        super((int) x, (int) y, (int) width, (int) height, text, onPress, Button.DEFAULT_NARRATION);
        this.u0 = u0;
        this.v0 = v0;
        this.u1 = u1;
        this.v1 = v1;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics gfx, int mouseX, int mouseY, float delta) {
        if (isHoveredOrFocused() != this.activated) {
            this.activated = isHoveredOrFocused();
            this.animation = this.activated ?
                    new Animation(0, 1, 3, Easing.Linear.flat) :
                    new Animation(1, 0, 3, Easing.Linear.flat);
        }

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        // normal
        GL.drawTexture(gfx, CommandBinds.TEXTURES_LOCATION,
                getX(), getY(), getWidth(), getHeight(),
                this.u0 / (float) CommandBinds.TEXTURES_SIZE,
                this.v0 / (float) CommandBinds.TEXTURES_SIZE,
                this.u1 / (float) CommandBinds.TEXTURES_SIZE,
                this.v1 / (float) CommandBinds.TEXTURES_SIZE
        );

        RenderSystem.setShaderColor(1, 1, 1, this.animation.getValue());

        // hovered
        GL.drawTexture(gfx, CommandBinds.TEXTURES_LOCATION,
                getX(), getY(), getWidth(), getHeight(),
                this.u0 / (float) CommandBinds.TEXTURES_SIZE,
                (this.v0 + getHeight()) / (float) CommandBinds.TEXTURES_SIZE,
                this.u1 / (float) CommandBinds.TEXTURES_SIZE,
                (this.v1 + getHeight()) / (float) CommandBinds.TEXTURES_SIZE
        );

        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
}
