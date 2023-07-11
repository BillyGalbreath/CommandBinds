package net.pl3x.commandbinds.mixin;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.pl3x.commandbinds.CommandBinds;
import net.pl3x.commandbinds.configuration.Config;
import net.pl3x.commandbinds.gui.CommandKey;
import net.pl3x.commandbinds.gui.Tickable;
import net.pl3x.keyboard.Keyboard;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyBindsList.KeyEntry.class)
public class KeyBindsListKeyEntryMixin implements Tickable {
    @Final
    @Shadow
    private Button changeButton;
    @Final
    @Shadow
    private Button resetButton;
    @Shadow
    private boolean hasCollision;

    @Unique
    private final List<GuiEventListener> children = new ArrayList<>();
    @Unique
    private KeyBindsList keyBindsList;
    @Unique
    private CommandKey commandKey;

    /**
     * Here we are checking if this entry is one of our
     * custom commands, and if so store it for later use.
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    private void ctor(@NotNull KeyBindsList keyBindsList, @NotNull KeyMapping mapping, @NotNull Component name, @NotNull CallbackInfo ci) {
        this.keyBindsList = keyBindsList;
        String nameKey = ((TranslatableContents) name.getContents()).getKey();
        if (nameKey.startsWith(String.format("%s.keymap.command.", CommandBinds.MODID))) {
            this.commandKey = Keyboard.getKey(nameKey) instanceof CommandKey cmdKey ? cmdKey : null;
        }
    }

    /**
     * Here we are overriding the render call of our
     * custom command entry in order to draw an editbox.
     */
    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIIIIIIZF)V", at = @At("HEAD"), cancellable = true)
    private void render(@NotNull GuiGraphics gfx, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float delta, @NotNull CallbackInfo ci) {
        if (this.commandKey != null) {
            this.commandKey.render(gfx, index, top, left, width, height, mouseX, mouseY, hovered, delta, this.keyBindsList.maxNameWidth, this.changeButton, this.resetButton, this.hasCollision);
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

    @Inject(method = "refreshEntry()V", at = @At("TAIL"))
    private void refreshEntry(@NotNull CallbackInfo ci) {
        Config.save();
    }

    @Redirect(method = "refreshEntry()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MutableComponent;append(Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;", ordinal = 0))
    private MutableComponent refreshEntry(@NotNull MutableComponent instance, @NotNull Component component) {
        if (component.getContents() instanceof TranslatableContents contents &&
                contents.getKey().startsWith(String.format(CommandBinds.KEY_NAME, "")) &&
                Keyboard.getKey(contents.getKey()) instanceof CommandKey cmdKey) {
            return instance.append(Component.literal(String.format("/%s", cmdKey.getCommand())));
        }
        return instance.append(component);
    }

    /**
     * Here we are storing all the child widgets,
     * including our editbox if it exists
     */
    @Override
    public void tickable$init() {
        this.children.clear();
        if (this.commandKey != null) {
            this.commandKey.tickable$init();
            this.children.addAll(this.commandKey.children());
        }
        this.children.add(this.changeButton);
        this.children.add(this.resetButton);
    }

    /**
     * Here we are ticking our editbox so the cursor blinks
     */
    @Override
    public void tickable$tick() {
        if (this.commandKey != null) {
            this.commandKey.tickable$tick();
        }
    }
}
