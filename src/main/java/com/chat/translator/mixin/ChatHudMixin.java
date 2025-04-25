package com.chat.translator.mixin;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChatHud.class)
public class ChatHudMixin {
    @ModifyVariable(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), argsOnly = true)
    private Text modifyMessage(Text message) {
        String originalText = message.getString();
        String escapedText = "\"" + originalText.replace("\"", "\\\"") + "\"";
        Text translateText = Text.translatable("chattranslator.translate_button")
                .styled(s -> s.withColor(Formatting.DARK_PURPLE))
                .styled(s -> s.withClickEvent(
                        new ClickEvent.RunCommand("/translatemessage " + escapedText)
                ));
        return Text.empty().append(message).append(" ").append(translateText);
    }
}
