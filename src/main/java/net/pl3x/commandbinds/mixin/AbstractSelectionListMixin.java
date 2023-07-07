package net.pl3x.commandbinds.mixin;

import java.util.List;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSelectionList.class)
public class AbstractSelectionListMixin {
    @Shadow
    private int y0;
    @Shadow
    private int headerHeight;
    @Shadow
    private int itemHeight;

    @Shadow
    public double getScrollAmount() {
        throw new AssertionError();
    }

    @Shadow
    public int getScrollbarPosition() {
        throw new AssertionError();
    }

    @Shadow
    public int getItemCount() {
        throw new AssertionError();
    }

    @Shadow
    public List<AbstractSelectionList.Entry<?>> children() {
        throw new AssertionError();
    }

    @Shadow
    public int addEntry(AbstractSelectionList.Entry<?> entry) {
        throw new AssertionError();
    }

    /**
     * Here we are removing the left/right boundary checks
     * in order to be able to click the entire editbox.
     */
    @Inject(method = "getEntryAtPosition(DD)Lnet/minecraft/client/gui/components/AbstractSelectionList$Entry;", at = @At("HEAD"), cancellable = true)
    private void getEntryAtPosition(double mouseX, double mouseY, CallbackInfoReturnable<AbstractSelectionList.Entry<?>> cir) {
        int m = Mth.floor(mouseY - (double) this.y0) - this.headerHeight + (int) getScrollAmount() - 4;
        int n = m / this.itemHeight;
        cir.setReturnValue(mouseX < (double) getScrollbarPosition() && n >= 0 && m >= 0 && n < getItemCount() ? children().get(n) : null);
    }
}
