package net.pl3x.commandbinds.mixin;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.pl3x.commandbinds.gui.KeyEntryRenderer;
import net.pl3x.commandbinds.gui.Tickable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyBindsList.KeyEntry.class)
public class KeyBindsListKeyEntryMixin implements Tickable {
    @Unique
    private KeyEntryRenderer keyEntryRenderer;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void ctor(@NotNull KeyBindsList keyBindsList, @NotNull KeyMapping mapping, @NotNull Component name, @NotNull CallbackInfo ci) {
        this.keyEntryRenderer = new KeyEntryRenderer(keyBindsList, mapping, name);
    }

    @Redirect(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIIIIIIZF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I"))
    private int render(@NotNull GuiGraphics instance, @NotNull Font font, @NotNull Component component, int x, int y, int color, boolean shadow, @NotNull GuiGraphics gfx, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float delta) {
        return this.keyEntryRenderer.render(gfx, top, left, height, mouseX, mouseY, delta) ? 1 : instance.drawString(font, component, x, y, color, shadow);
    }

    @Redirect(method = "refreshEntry()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MutableComponent;append(Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;", ordinal = 0))
    private @NotNull MutableComponent refreshEntry(@NotNull MutableComponent instance, @NotNull Component name) {
        return this.keyEntryRenderer.refreshEntry(instance, name);
    }

    @Inject(method = "children()Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    private void children(@NotNull CallbackInfoReturnable<List<GuiEventListener>> cir) {
        cir.setReturnValue(this.keyEntryRenderer.children(new ArrayList<>(cir.getReturnValue())));
    }

    @Override
    public void tickable$init() {
        this.keyEntryRenderer.tickable$init();
    }

    @Override
    public void tickable$tick() {
        this.keyEntryRenderer.tickable$tick();
    }
}
