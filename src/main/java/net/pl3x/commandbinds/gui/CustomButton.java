package net.pl3x.commandbinds.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.pl3x.commandbinds.CommandBinds;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class CustomButton extends Button {
    private static final ResourceLocation BTN_TEX_LOC = new ResourceLocation(CommandBinds.MODID, "textures/buttons.png");
    private static final float BTN_TEX_WIDTH = 32F;
    private static final float BTN_TEX_HEIGHT = 32F;

    public static int IDK_HOW_ELSE_TO_PASS_THIS_MAXWIDTH;

    private final float u0, v0, u1, v1;

    public CustomButton(int x, int y, int width, int height, int u0, int v0, int u1, int v1, @NotNull OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.u0 = u0 / BTN_TEX_WIDTH;
        this.v0 = v0 / BTN_TEX_HEIGHT;
        this.u1 = (u0 + u1) / BTN_TEX_HEIGHT;
        this.v1 = (v0 + v1) / BTN_TEX_HEIGHT;
    }

    @Override
    protected void renderWidget(GuiGraphics gfx, int mouseX, int mouseY, float delta) {
        int x0 = getX();
        int y0 = getY();
        int x1 = x0 + getWidth();
        int y1 = y0 + getHeight();

        float offsetY = (isHoveredOrFocused() ? 0 : 16) / BTN_TEX_HEIGHT;

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, BTN_TEX_LOC);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        Matrix4f model = gfx.pose().last().pose();
        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buf.vertex(model, x0, y0, 0).uv(this.u0, this.v0 + offsetY).endVertex();
        buf.vertex(model, x0, y1, 0).uv(this.u0, this.v1 + offsetY).endVertex();
        buf.vertex(model, x1, y1, 0).uv(this.u1, this.v1 + offsetY).endVertex();
        buf.vertex(model, x1, y0, 0).uv(this.u1, this.v0 + offsetY).endVertex();
        BufferUploader.drawWithShader(buf.end());
    }
}
