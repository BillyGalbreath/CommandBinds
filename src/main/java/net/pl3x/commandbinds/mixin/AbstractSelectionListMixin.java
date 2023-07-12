package net.pl3x.commandbinds.mixin;

import java.util.List;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSelectionList.class)
public class AbstractSelectionListMixin {
    @Shadow
    protected int y0;
    @Shadow
    protected int headerHeight;
    @Final
    @Shadow
    public int itemHeight;

    @Shadow
    public double getScrollAmount() {
        throw new AssertionError();
    }

    @Shadow
    protected int getItemCount() {
        throw new AssertionError();
    }

    @Shadow
    public @NotNull List<AbstractSelectionList.Entry<?>> children() {
        throw new AssertionError();
    }

    @Inject(method = "getEntryAtPosition(DD)Lnet/minecraft/client/gui/components/AbstractSelectionList$Entry;", at = @At("HEAD"), cancellable = true)
    private void getEntryAtPosition(double mouseX, double mouseY, @NotNull CallbackInfoReturnable<AbstractSelectionList.Entry<?>> cir) {
        int m = Mth.floor(mouseY - (double) this.y0) - this.headerHeight + (int) getScrollAmount() - 4;
        int n = m / this.itemHeight;
        cir.setReturnValue(n >= 0 && m >= 0 && n < getItemCount() ? children().get(n) : null);
    }
}
