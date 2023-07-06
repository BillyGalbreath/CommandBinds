package net.pl3x.commandbinds.mixin;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.pl3x.commandbinds.CommandBinds;
import net.pl3x.commandbinds.gui.CustomKey;
import net.pl3x.commandbinds.gui.Tickable;
import net.pl3x.keyboard.Keyboard;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyBindsList.KeyEntry.class)
public class KeyBindsListKeyEntryMixin implements Tickable {
    @Shadow
    private Button changeButton;
    @Shadow
    private Button resetButton;
    @Shadow
    private boolean hasCollision;

    private final List<GuiEventListener> children = new ArrayList<>();

    private KeyBindsList keyBindsList;
    private CustomKey customKey;

    /**
     * Here we are checking if this entry is one of our
     * custom commands, and if so store it for later use.
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    private void ctor(@NotNull KeyBindsList keyBindsList, @NotNull KeyMapping mapping, @NotNull Component name, @NotNull CallbackInfo ci) {
        this.keyBindsList = keyBindsList;
        String nameKey = ((TranslatableContents) name.getContents()).getKey();
        if (nameKey.startsWith(String.format("%s.keymap.command.", CommandBinds.MODID))) {
            this.customKey = Keyboard.getKey(nameKey) instanceof CustomKey key ? key : null;
        }
    }

    /**
     * Here we are overriding the render call of our
     * custom command entry in order to draw an editbox.
     */
    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIIIIIIZF)V", at = @At("HEAD"), cancellable = true)
    private void render(@NotNull GuiGraphics gfx, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float delta, @NotNull CallbackInfo ci) {
        if (this.customKey != null) {
            int maxWidth = ((KeyBindsListAccessor) this.keyBindsList).getMaxNameWidth();
            this.customKey.render(gfx, index, top, left, width, height, mouseX, mouseY, hovered, delta, maxWidth, this.changeButton, this.resetButton, this.hasCollision);
            ci.cancel();
        }
    }

    /**
     * Here we are returning a full list of
     * child widgets, including our editbox.
     */
    @Inject(method = "children()Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    private void children(@NotNull CallbackInfoReturnable<List<? extends GuiEventListener>> cir) {
        cir.setReturnValue(this.children);
    }

    /**
     * Here we are storing all the child widgets,
     * including our editbox if it exists
     */
    @Override
    public void init() {
        this.children.clear();
        if (this.customKey != null) {
            this.customKey.init();
            this.children.addAll(this.customKey.children());
        }
        this.children.add(this.changeButton);
        this.children.add(this.resetButton);
    }

    /**
     * Here we are ticking our editbox so the cursor blinks
     */
    @Override
    public void tick() {
        if (this.customKey != null) {
            this.customKey.tick();
        }
    }
}