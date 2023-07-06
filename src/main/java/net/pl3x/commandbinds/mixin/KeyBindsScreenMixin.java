package net.pl3x.commandbinds.mixin;

import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import net.pl3x.commandbinds.gui.Tickable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBindsScreen.class)
public abstract class KeyBindsScreenMixin extends OptionsSubScreen {
    @Shadow
    private KeyBindsList keyBindsList;

    public KeyBindsScreenMixin(Screen screen, Options options) {
        super(screen, options, Component.translatable("controls.keybinds.title"));
    }

    /**
     * Here we are ensuring our widgets call init() when the screen does.
     */
    @Inject(method = "init()V", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        this.keyBindsList.children().forEach(child -> {
            if (child instanceof Tickable tickable) {
                tickable.init();
            }
        });
    }

    /**
     * Here we are ensuring our widgets call tick() when the screen does.
     */
    @Override
    public void tick() {
        this.keyBindsList.children().forEach(child -> {
            if (child instanceof Tickable tickable) {
                tickable.tick();
            }
        });
    }
}
