package com.chat.translator;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class TranslatorMod implements ClientModInitializer {
	public static Config config;
	private static final KeyBinding configKey = KeyBindingHelper.registerKeyBinding(
			new KeyBinding(
					"key.chattranslator.config",
					InputUtil.Type.KEYSYM,
					GLFW.GLFW_KEY_KP_ADD,
					"category.chattranslator"
			)
	);

	@Override
	public void onInitializeClient() {
		config = Config.load();

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(
					ClientCommandManager.literal("translatemessage")
							.then(
									ClientCommandManager.argument("message", StringArgumentType.greedyString())
											.executes(context -> {
												String message = StringArgumentType.getString(context, "message");
												FabricClientCommandSource source = context.getSource();
												if (config.preferredLanguage == null || config.preferredLanguage.isEmpty()) {
													source.sendError(Text.translatable("chattranslator.error.preferred_language_not_set"));
													return 0;
												}
												try {
													String sourceLang = config.manualSourceLanguage ? config.sourceLanguage : null;
													String translated = DeepLApi.translate(
															message,
															sourceLang,
															config.preferredLanguage,
															config.apiKey,
															config.isPro
													);
													source.sendFeedback(Text.translatable("chattranslator.translation.translated", translated));
												} catch (Exception e) {
													source.sendError(Text.translatable("chattranslator.translation.failed", e.getMessage()));
												}
												return 1;
											})
							)
			);
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (configKey.wasPressed()) {
				client.setScreen(new ConfigScreen());
			}
		});
	}
}