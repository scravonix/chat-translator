package com.chat.translator.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.chat.translator.TranslatorMod;
import com.chat.translator.DeepLApi;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * Mixin for ChatScreen to add a DeepL translation input field
 */
@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {
	private TextFieldWidget translationField;
	private TextRenderer textRenderer;

	@Shadow private TextFieldWidget chatField;

	@Inject(method = "init", at = @At("TAIL"))
	private void initTranslationField(CallbackInfo ci) {
		ChatScreen screen = (ChatScreen) (Object) this;
		int y = screen.height - 28;
		textRenderer = screen.getTextRenderer();

		translationField = new TextFieldWidget(
				textRenderer,
				50,
				y,
				screen.width - 100,
				12,
				Text.translatable("chattranslator.translation.input")
		) {
			@Override
			public void renderWidget(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
				if (this.isVisible()) {
					context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x80000000);
					context.drawTextWithShadow(textRenderer, getText(), getX() + 4, getY() + (getHeight() - 8) / 2, 0xFFFFFF);
					if (this.isFocused()) {
						int cursorPos = getCursor();
						String text = getText();
						if (cursorPos >= 0 && cursorPos <= text.length()) {
							int x = getX() + 4 + textRenderer.getWidth(text.substring(0, cursorPos));
							int y = getY() + (getHeight() - 8) / 2;
							context.fill(x, y, x + 1, y + 8, 0xFFFFFFFF);
						}
					}
				}
			}
		};
		translationField.setMaxLength(256);

		((ScreenInvoker) screen).invokeAddDrawableChild(translationField);
	}

	@Inject(method = "keyPressed(III)Z", at = @At("HEAD"), cancellable = true)
	private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
		if (translationField != null && translationField.isFocused()) {
			if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
				String input = translationField.getText().trim();
				if (!input.isEmpty()) {
					processTranslationInput(input);
					translationField.setText("");
					MinecraftClient.getInstance().setScreen(null);
					cir.setReturnValue(true);
				}
			} else if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
				MinecraftClient.getInstance().setScreen(null);
				cir.setReturnValue(true);
			} else {
				boolean handled = translationField.keyPressed(keyCode, scanCode, modifiers);
				cir.setReturnValue(handled);
			}
		}
	}

	@Inject(method = "mouseClicked(DDI)Z", at = @At("HEAD"), cancellable = true)
	private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
		if (translationField != null && chatField != null) {
			if (translationField.mouseClicked(mouseX, mouseY, button)) {
				translationField.setFocused(true);
				chatField.setFocused(false);
				((ChatScreen) (Object) this).setFocused(translationField);
				cir.setReturnValue(true);
			}
			else if (chatField.mouseClicked(mouseX, mouseY, button)) {
				chatField.setFocused(true);
				translationField.setFocused(false);
				((ChatScreen) (Object) this).setFocused(chatField);
				cir.setReturnValue(true);
			}
		}
	}

	private void processTranslationInput(String input) {
		if (input.startsWith("/send ")) {
			String[] parts = input.substring(6).trim().split(" ");
			if (parts.length >= 3) {
				String text = String.join(" ", Arrays.copyOfRange(parts, 0, parts.length - 2));
				String sourceLang = parts[parts.length - 2].replaceAll("[<>]", "");
				String targetLang = parts[parts.length - 1].replaceAll("[<>]", "");
				CompletableFuture.runAsync(() -> {
					try {
						String translated = DeepLApi.translate(
								text,
								sourceLang,
								targetLang,
								TranslatorMod.config.apiKey,
								TranslatorMod.config.isPro
						);
						MinecraftClient.getInstance().execute(() -> {
							MinecraftClient.getInstance().getNetworkHandler().sendChatMessage(translated);
							MinecraftClient.getInstance().setScreen(null);
						});
					} catch (Exception e) {
						MinecraftClient.getInstance().execute(() -> {
							MinecraftClient.getInstance().player.sendMessage(
									Text.translatable("chattranslator.translation.failed", e.getMessage()), false
							);
							MinecraftClient.getInstance().setScreen(null);
						});
					}
				});
			}
		} else {
			String[] parts = input.trim().split(" ");
			if (parts.length >= 3) {
				String text = String.join(" ", Arrays.copyOfRange(parts, 0, parts.length - 2));
				String sourceLang = parts[parts.length - 2].replaceAll("[<>]", "");
				String targetLang = parts[parts.length - 1].replaceAll("[<>]", "");
				CompletableFuture.runAsync(() -> {
					try {
						String translated = DeepLApi.translate(
								text,
								sourceLang,
								targetLang,
								TranslatorMod.config.apiKey,
								TranslatorMod.config.isPro
						);
						MinecraftClient.getInstance().execute(() -> {
							MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(
									Text.translatable("chattranslator.translation.result", translated)
							);
							MinecraftClient.getInstance().setScreen(null);
						});
					} catch (Exception e) {
						MinecraftClient.getInstance().execute(() -> {
							MinecraftClient.getInstance().player.sendMessage(
									Text.translatable("chattranslator.translation.failed", e.getMessage()), false
							);
							MinecraftClient.getInstance().setScreen(null);
						});
					}
				});
			}
		}
	}
}
