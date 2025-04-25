# Chat Translator

Ever wished you could chat with Minecraft players from all over the world without a language barrier? Or maybe impress your friends by sending messages in their native tongue? Say hello to the **Chat Translator**! This neat little mod brings DeepL's top-notch translation services straight into your Minecraft chat. Translate incoming messages with a click or send your own in any supported language—global gaming just got a whole lot more fun!

---

## How to Use

### 1. Setup
Before you start chatting across borders, you'll need to set things up:

- **Get a DeepL API Key**: Head to the [DeepL API](https://www.deepl.com/en/pro-api), sign up, and grab your API key.
- **Open the Config Screen**: Hit the default config key (numpad `+`, or `KP_ADD`) to pop open the settings. If you have Mod Menu, you can also access it via the interface.
- **Enter Your API Key**: Paste your key in the field and choose if you’re using the free or pro version of DeepL.
- **Pick Your Language**: Set your preferred language for translations—like `EN` for English or `FR` for French.
- **Optional Tweaks**: Want to specify the source language manually? Toggle that option and pick a source language too.

Save your settings, and you’re good to go!

---

### 2. Translating Incoming Messages
Chatting with someone in a language you don’t speak? No sweat:

- In the chat, look for the **"Translate"** button next to each message (it’s in dark purple).
- Click it, and the message will instantly translate into your preferred language using DeepL’s magic. Done!

---

### 3. Sending Translated Messages
Want to reply in their language? Here’s how:

- Open the chat screen and spot the new **"Translation Input"** field.
- **Just Translate**: Type your message followed by the source and target language codes in angle brackets. For example:  
  `"Hello EN FR"` translates "Hello" from English to French and shows you the result.
- **Translate and Send**: Use the `/send` command like this:  
  `"/send Hello EN FR"` translates "Hello" to French and sends it straight to the chat.

Check the [DeepL Supported Languages](https://developers.deepl.com/docs/getting-started/supported-languages) for a full list of language codes (e.g., `ES` for Spanish, `JA` for Japanese).

---

## Notes
- **Internet Required**: This mod needs an active connection to talk to DeepL’s servers.
- **Usage Limits**: If you’re on the free DeepL tier, keep an eye on their limits—pro users get more wiggle room.
- **Multilingual**: As you can see from the [src\main\resources\assets\chattranslator\lang](https://github.com/scravonix/chat-translator/tree/main/src/main/resources/assets/chattranslator/lang) directory, the mod supports 20 languages. Whichever language you are using in Minecraft, the texts will automatically change if the language is supported.
