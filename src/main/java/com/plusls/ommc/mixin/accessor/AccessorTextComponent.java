package com.plusls.ommc.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC > 12002
import net.minecraft.network.chat.contents.PlainTextContents;
//#else
//$$ import net.minecraft.network.chat.contents.LiteralContents;
//#endif

@Mixin(
        //#if MC > 12002
        PlainTextContents.LiteralContents.class
        //#else
        //$$ LiteralContents.class
        //#endif
)
public interface AccessorTextComponent {
    @Accessor
    String getText();

    @Mutable
    @Accessor
    void setText(String text);
}
