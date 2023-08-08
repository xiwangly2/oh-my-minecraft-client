package com.plusls.ommc.mixin.feature.worldEaterMineHelper.sodium;

import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.preprocess.api.DummyClass;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependency;

@Dependencies(and = @Dependency(value = "sodium", versionPredicate = ">0.4.10 <0.5"))
@Mixin(DummyClass.class)
public class MixinBlockRenderer_0_4_11 {
}
