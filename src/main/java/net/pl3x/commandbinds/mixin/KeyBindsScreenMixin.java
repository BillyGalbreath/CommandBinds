package net.pl3x.commandbinds.mixin;

import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import net.pl3x.commandbinds.gui.Tickable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBindsScreen.class)
public abstract class KeyBindsScreenMixin extends OptionsSubScreen {
    @Shadow
    public KeyBindsList keyBindsList;

    @Unique
    private double scrollAmount;

    public KeyBindsScreenMixin(@NotNull Screen screen, @NotNull Options options) {
        super(screen, options, Component.translatable("controls.keybinds.title"));
    }

    @Inject(method = "init()V", at = @At("HEAD"))
    private void initHead(@NotNull CallbackInfo ci) {
        this.scrollAmount = this.keyBindsList == null ? 0 : this.keyBindsList.getScrollAmount();
    }

    @Inject(method = "init()V", at = @At("TAIL"))
    private void initTail(@NotNull CallbackInfo ci) {
        this.keyBindsList.setScrollAmount(this.scrollAmount);

        this.keyBindsList.children().forEach(child -> {
            if (child instanceof Tickable tickable) {
                tickable.tickable$init();
            }
        });
    }

    @Override
    public void tick() {
        this.keyBindsList.children().forEach(child -> {
            if (child instanceof Tickable tickable) {
                tickable.tickable$tick();
            }
        });
    }
}
