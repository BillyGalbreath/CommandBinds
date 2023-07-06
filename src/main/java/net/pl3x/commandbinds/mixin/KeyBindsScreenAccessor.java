package net.pl3x.commandbinds.mixin;

import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(KeyBindsScreen.class)
public interface KeyBindsScreenAccessor {
    @Accessor
    KeyBindsList getKeyBindsList();

    @Invoker
    void invokeInit();
}
