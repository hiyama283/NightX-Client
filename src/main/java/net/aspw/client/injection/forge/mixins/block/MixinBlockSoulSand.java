package net.aspw.client.injection.forge.mixins.block;

import net.aspw.client.Launch;
import net.aspw.client.features.module.impl.movement.NoSlow;
import net.minecraft.block.BlockSoulSand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(BlockSoulSand.class)
public class MixinBlockSoulSand {

    @Inject(method = "onEntityCollidedWithBlock", at = @At("HEAD"), cancellable = true)
    private void onEntityCollidedWithBlock(CallbackInfo callbackInfo) {
        final NoSlow noSlow = Objects.requireNonNull(Launch.moduleManager.getModule(NoSlow.class));

        if (noSlow.getState() && noSlow.getSoulsandValue().get())
            callbackInfo.cancel();
    }
}