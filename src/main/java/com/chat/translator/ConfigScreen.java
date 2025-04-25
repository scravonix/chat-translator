package com.chat.translator;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.client.resource.language.I18n;

import java.util.Arrays;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen {
    private TextFieldWidget apiKeyField;
    private ButtonWidget proButton;
    private ButtonWidget testButton;
    private TextFieldWidget resultField;
    private String actualApiKey;
    private ButtonWidget manualSourceLanguageButton;
    private CyclingButtonWidget<String> sourceLanguageButton;

    private static final List<String> LANGUAGE_LIST = Arrays.asList(
            "AR", "BG", "CS", "DA", "DE", "EL", "EN", "EN-GB", "EN-US", "ES", "ET", "FI", "FR", "HU", "ID",
            "IT", "JA", "KO", "LT", "LV", "NB", "NL", "PL", "PT", "PT-BR", "PT-PT", "RO", "RU", "SK", "SL",
            "SV", "TR", "UK", "ZH", "ZH-HANS", "ZH-HANT"
    );

    public ConfigScreen() {
        super(Text.translatable("chattranslator.config.title"));
    }

    @Override
    protected void init() {
        int x = this.width / 2 - 100;
        int y = this.height / 4;

        // Grup 1: API Anahtarı, Pro/Ücretsiz, Test, Sonuç, Kaydet
        // API Key Field
        this.apiKeyField = new TextFieldWidget(this.textRenderer, x, y, 200, 20, Text.translatable("chattranslator.config.apiKey"));
        this.actualApiKey = TranslatorMod.config.apiKey;
        this.apiKeyField.setText(this.actualApiKey.isEmpty() ? "" : "******");
        this.apiKeyField.setMaxLength(100);
        this.apiKeyField.setChangedListener(text -> this.actualApiKey = text.equals("******") ? this.actualApiKey : text);
        this.addDrawableChild(this.apiKeyField);

        // Pro/Free Button
        this.proButton = ButtonWidget.builder(
                        Text.translatable(TranslatorMod.config.isPro ? "chattranslator.config.pro" : "chattranslator.config.free"),
                        button -> {
                            TranslatorMod.config.isPro = !TranslatorMod.config.isPro;
                            button.setMessage(Text.translatable(TranslatorMod.config.isPro ? "chattranslator.config.pro" : "chattranslator.config.free"));
                        })
                .dimensions(x, y + 30, 200, 20)
                .build();
        this.addDrawableChild(this.proButton);

        // Test Button
        this.testButton = ButtonWidget.builder(
                        Text.translatable("chattranslator.config.test"),
                        button -> this.resultField.setText(testApiKey(this.actualApiKey.trim(), TranslatorMod.config.isPro)))
                .dimensions(x, y + 60, 200, 20)
                .build();
        this.addDrawableChild(this.testButton);

        // Result Field
        this.resultField = new TextFieldWidget(this.textRenderer, x, y + 90, 200, 20, Text.translatable("chattranslator.config.result"));
        this.resultField.setEditable(false);
        this.resultField.setMaxLength(5000);
        this.addDrawableChild(this.resultField);

        // Save Button
        this.addDrawableChild(ButtonWidget.builder(
                        Text.translatable("chattranslator.config.save"),
                        button -> {
                            TranslatorMod.config.apiKey = this.actualApiKey.trim();
                            TranslatorMod.config.save();
                            this.client.setScreen(null);
                        })
                .dimensions(x, y + 120, 200, 20)
                .build());

        // Grup 2: Sohbet Çevirisi, Kaynak Dili Manuel Ayarla, Kaynak Dil
        // Grup 1 ile Grup 2 arasında 50 piksel boşluk
        int group2Y = y + 170;  // Grup 1'in son widget'ı y + 120, 50 piksel boşluk için y + 170

        // Language Selection Button (Sohbet Çevirisi)
        CyclingButtonWidget.Builder<String> builder = new CyclingButtonWidget.Builder<String>(
                code -> Text.translatable("chattranslator.language." + code)
        );
        builder.values(LANGUAGE_LIST);
        builder.initially(TranslatorMod.config.preferredLanguage);
        CyclingButtonWidget<String> languageButton = builder.build(
                x, group2Y, 200, 20,
                Text.translatable("chattranslator.config.language"),
                (button, value) -> TranslatorMod.config.preferredLanguage = value
        );
        this.addDrawableChild(languageButton);

        // Manual Source Language Button
        this.manualSourceLanguageButton = ButtonWidget.builder(
                Text.translatable("chattranslator.config.manual_source_language",
                        TranslatorMod.config.manualSourceLanguage ?
                                Text.translatable("chattranslator.config.yes") :
                                Text.translatable("chattranslator.config.no")),
                button -> {
                    TranslatorMod.config.manualSourceLanguage = !TranslatorMod.config.manualSourceLanguage;
                    button.setMessage(Text.translatable("chattranslator.config.manual_source_language",
                            TranslatorMod.config.manualSourceLanguage ?
                                    Text.translatable("chattranslator.config.yes") :
                                    Text.translatable("chattranslator.config.no")));
                    this.sourceLanguageButton.active = TranslatorMod.config.manualSourceLanguage;
                }
        ).dimensions(x, group2Y + 30, 200, 20).build();
        this.addDrawableChild(this.manualSourceLanguageButton);

        // Source Language Selection Button
        this.sourceLanguageButton = CyclingButtonWidget.builder((String code) -> Text.translatable("chattranslator.language." + code))
                .values(LANGUAGE_LIST)
                .initially(TranslatorMod.config.sourceLanguage)
                .build(x, group2Y + 60, 200, 20, Text.translatable("chattranslator.config.source_language"),
                        (button, code) -> {
                            TranslatorMod.config.sourceLanguage = code;
                        });
        this.sourceLanguageButton.active = TranslatorMod.config.manualSourceLanguage;
        this.addDrawableChild(this.sourceLanguageButton);
    }

    private String testApiKey(String apiKey, boolean isPro) {
        try {
            String result = DeepLApi.translate("I love Minecraft!", "EN", "TR", apiKey, isPro);
            return I18n.translate("chattranslator.config.test_success", result);
        } catch (Exception e) {
            return I18n.translate("chattranslator.config.test_failed", e.getMessage());
        }
    }
}