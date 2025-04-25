![Preview](https://media1.giphy.com/media/v1.Y2lkPTc5MGI3NjExaXZoZXFrejIxMmlwOHdhcmQwNTEwdmpjd3Y1aHl3MXY5eW4zejU2ZyZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/Oos9G6aDVRQSkHoEqK/giphy.gif)

# Chat Translator Mod

**Chat Translator** is a Minecraft mod that enhances your chat experience by integrating real-time translation capabilities using the DeepL API. Whether you're playing on a multilingual server or connecting with friends who speak different languages, this mod allows you to translate chat messages seamlessly, making communication effortless and inclusive.

#### Requirements
- [DeepL API](https://www.deepl.com/en/pro-api)
- [Mod Menu](https://modrinth.com/mod/modmenu) (optional)

#### Installation Steps
1. **Install Fabric Loader**: Download and install it from [fabricmc.net](https://fabricmc.net/use/installer/). When you install, use minimum 0.16.10
2. **Install Fabric API**: Get it from [Modrinth](https://modrinth.com/mod/fabric-api) and place it in your `mods` folder.
3. **Download Chat Translator**: Grab the latest JAR file from the [releases page](https://modrinth.com/mod/chat-translator/versions).
4. **Add to Mods Folder**: Move the JAR file into your Minecraft `mods` folder.
5. **Launch Minecraft**: Start the game using the Fabric profile.

### Configuration

Access the configuration screen in-game by pressing the default keybind `KP_ADD` (numpad +) or via the Mod Menu if installed.

- **API Key**: Enter your DeepL API key here. If a key is already set, it appears masked (e.g., `******`). You can overwrite it with a new key.
- **Free / Pro**: Toggles between the free and pro DeepL API modes. Free mode has basic features, while pro mode offers enhanced capabilities and higher limits.
- **Test API Key**: Tests your API key. The result (success or failure) appears in the result field.
- **Result Field**: Displays the outcome of the API key test (e.g., a translated sample or an error message).
- **Save**: Saves your settings and closes the configuration screen.

See messages sent to chat in the language of your choice:
- **Chat translation**: Sets your preferred language for translations (e.g., "EN" for English, "FR" for French).
- **Manually set source language**: Toggles whether the source language is manually specified. Shows "Yes" or "No" accordingly.
- **Source language**: Active only if manual source language is enabled. Selects the language of the text to be translated (e.g., "ES" for Spanish).

The message you want to translate is indicated in purple with [] at the end of each message in the chat.

### In-Game Usage

Located above the chat bar, this field lets you type text for translation. Press `Enter` to process:
  - Only you can see: `<text> <sourceLang> <targetLang>` (e.g., `Hello EN ES` translates "Hello" from English to Spanish).
  - Sends to chat: `/send <text> <sourceLang> <targetLang>` (e.g., `/send Bonjour FR EN` sends "Hello").

See the languages supported by DeepL, along with their language codes: [Supported Languages](https://developers.deepl.com/docs/getting-started/supported-languages)

### Multilingual

- This mod supports the following languages: Arabic, Azerbaijani, Danish, German, English (US), Spanish (ES & MX), Finnish, French, Hindi, Dutch, Polish, Portuguese (BR), Russian, Swedish, Turkish, Ukrainian, and Chinese (ZH & TW). It will automatically switch to the language you are using in Minecraft.
