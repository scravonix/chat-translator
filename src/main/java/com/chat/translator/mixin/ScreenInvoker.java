package com.chat.translator.mixin;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Invoker mixin to expose the protected addDrawableChild method on Screen
 */
@Mixin(Screen.class)
public interface ScreenInvoker {
    /**
     * Exposed Screen.addDrawableChild(Element & Drawable & Selectable)
     */
    @Invoker("addDrawableChild")
    <T extends Element & Drawable & Selectable> T invokeAddDrawableChild(T widget);
}