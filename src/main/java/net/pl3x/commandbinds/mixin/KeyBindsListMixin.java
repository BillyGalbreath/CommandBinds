package net.pl3x.commandbinds.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBindsList.class)
public class KeyBindsListMixin {
    /**
     * We add a new category with an empty title at the very
     * end to add an extra padding at the bottom of the list.
     */
    @SuppressWarnings("DataFlowIssue")
    @Inject(method = "<init>", at = @At("TAIL"))
    private void ctor(@NotNull KeyBindsScreen screen, @NotNull Minecraft client, @NotNull CallbackInfo ci) {
        KeyBindsList thisKeyBindsList = (KeyBindsList) (Object) this;
        thisKeyBindsList.addEntry(thisKeyBindsList.new CategoryEntry(Component.empty()));
    }
}
