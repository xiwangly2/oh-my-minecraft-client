package com.plusls.ommc.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@AllArgsConstructor
public class Tuple<A, B> {
    @Nullable
    private A a;
    @Nullable
    private B b;
}
