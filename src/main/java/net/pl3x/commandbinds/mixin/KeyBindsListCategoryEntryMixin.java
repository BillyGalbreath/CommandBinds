package net.pl3x.commandbinds.mixin;

import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.network.chat.Component;
import net.pl3x.commandbinds.gui.CategoryEntryRenderer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyBindsList.CategoryEntry.class)
public class KeyBindsListCategoryEntryMixin {
    @Unique
    private CategoryEntryRenderer categoryEntryRenderer;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void ctor(@NotNull KeyBindsList keyBindsList, @NotNull Component component, @NotNull CallbackInfo ci) {
        this.categoryEntryRenderer = new CategoryEntryRenderer(keyBindsList, component);
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIIIIIIZF)V", at = @At("HEAD"), cancellable = true)
    private void render(@NotNull GuiGraphics gfx, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float delta, @NotNull CallbackInfo ci) {
        this.categoryEntryRenderer.render(gfx, top, left, height, mouseX, mouseY, delta);
        ci.cancel();
    }

    @Inject(method = "children()Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    private void children(@NotNull CallbackInfoReturnable<List<? extends GuiEventListener>> cir) {
        cir.setReturnValue(this.categoryEntryRenderer.children());
    }
}
