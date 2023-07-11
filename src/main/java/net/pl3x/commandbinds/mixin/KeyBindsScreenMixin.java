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

    public KeyBindsScreenMixin(Screen screen, Options options) {
        super(screen, options, Component.translatable("controls.keybinds.title"));
    }

    /**
     * Here we are capturing the current scroll amount in order to re-apply it after the
     * init method is finished in order to keep the scroll position on window resizing
     */
    @Inject(method = "init()V", at = @At("HEAD"))
    private void initHead(CallbackInfo ci) {
        this.scrollAmount = this.keyBindsList != null ? this.keyBindsList.getScrollAmount() : 0;
    }

    /**
     * Here we are re-applying the captured scroll amount from above
     * and ensuring our widgets call init() when the screen does.
     */
    @Inject(method = "init()V", at = @At("TAIL"))
    private void initTail(CallbackInfo ci) {
        this.keyBindsList.setScrollAmount(this.scrollAmount);

        this.keyBindsList.children().forEach(child -> {
            if (child instanceof Tickable tickable) {
                tickable.tickable$init();
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
                tickable.tickable$tick();
            }
        });
    }
}
