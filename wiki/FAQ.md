# Frequently Asked Questions (FAQ)

## General Questions

### What is Match Mania?

Match Mania is an open-source card matching game for Android, inspired by classic card games like UNO. It features AI opponents, customizable rules, player statistics, and true random card shuffling.

### Is Match Mania free?

Yes! Match Mania is completely free and open-source under the Apache License 2.0. There are no ads, in-app purchases, or hidden costs.

### What devices are supported?

Match Mania supports Android devices running Android 5.0 (API 21) or higher. It works on both phones and tablets in portrait and landscape orientations.

### Does it work offline?

Yes! Match Mania is completely offline and requires no internet connection. All game data is stored locally on your device.

### Can I play with real people?

Currently, Match Mania only supports playing against AI opponents. Multiplayer functionality may be added in future versions based on user demand.

## Gameplay Questions

### How many players can play?

Each game has 4 players total: you and 3 AI opponents.

### Can I change the number of AI players?

Not currently. Future versions may support 2-10 players with adjustable AI count.

### How does the AI work?

AI players use strategic decision-making algorithms that evaluate legal moves, prioritize beneficial actions, and adapt to the game state. See [AI Players](AI-Players) for more details.

### Is the card shuffling truly random?

Yes! Match Mania uses `SecureRandom` for cryptographically secure randomness, ensuring fair and unpredictable card distribution.

### Can I customize the rules?

Yes! Go to Settings > Custom Rules to enable or disable various rule options like Stacking, Draw to Match, and Force Play. See [Custom Rules](Custom-Rules) for details.

### What happens when I have one card left?

The game automatically announces for you - no need to manually call out!

### Can the draw pile run out?

If the draw pile is exhausted, the discard pile (except the top card) is automatically shuffled and becomes the new draw pile.

## Technical Questions

### Why won't the APK install?

Common causes:
- Version mismatch (mixing debug and release builds)
- Insufficient storage space
- Security settings blocking installation
- Corrupted download

See [Installation Guide](Installation-Guide) for solutions.

### How do I update to a new version?

Download the new APK from GitHub Releases and install it over your existing installation. Your settings and statistics will be preserved.

### Where is my data stored?

All game data (settings, statistics, achievements) is stored locally on your device in the app's private storage.

### Can I backup my statistics?

Currently, there's no built-in backup feature. You can use Android's backup functionality or manually copy app data (requires root access). Cloud backup is planned for future releases.

### Does Match Mania collect any data?

No! Match Mania collects no data whatsoever. It requires no permissions beyond basic app functionality and sends no information anywhere.

## Feature Questions

### Can I customize my avatar?

Yes! Go to Settings > Player Customization to choose from various avatar options and change your name.

### How are statistics calculated?

Statistics track:
- Games played and won
- Win percentage
- Cards played
- Special cards used
- Average game length
- And more!

See [Statistics](Statistics) for full details.

### Are there achievements?

Yes! Match Mania includes various achievements for reaching milestones. Check your progress in the Statistics screen.

### Can I change the card design?

Currently, card design is fixed. Customizable card themes may be added in future versions.

### Is there a tutorial?

The game interface is intuitive with visual cues for playable cards. A formal tutorial may be added in future updates.

## Troubleshooting

### The game is running slowly

Try:
- Closing other apps
- Restarting the device
- Clearing the app cache
- Ensuring you have adequate free storage

### Cards aren't displaying correctly

This might be a screen resolution issue. Try:
- Rotating your device
- Restarting the app
- Checking for updates

### The AI is making invalid moves

This should not happen. If you observe this:
1. Note the game state (card played, current card, etc.)
2. Check your active rules in Settings
3. Report the bug with details on GitHub

### My statistics reset

Statistics should persist across sessions. If they reset:
- You may have uninstalled/reinstalled the app
- App data may have been cleared
- There may be a bug (please report it)

## Development Questions

### Is Match Mania open source?

Yes! The source code is available at [github.com/ssfdre38/match-mania](https://github.com/ssfdre38/match-mania) under the Apache License 2.0.

### Can I contribute?

Absolutely! See our [Contributing Guide](Contributing) for details on how to help.

### How do I build from source?

See [Building from Source](Building-from-Source) for complete instructions.

### Can I use Match Mania code in my project?

Yes, under the terms of the Apache License 2.0. You must include the license and copyright notices. See [LICENSE](https://github.com/ssfdre38/match-mania/blob/main/LICENSE) for full details.

### Will there be an iOS version?

There are no immediate plans for an iOS version. The current focus is on improving the Android version.

## Legal Questions

### Why isn't it called UNO?

"UNO" is a registered trademark of Mattel. Match Mania is an independent implementation of similar game mechanics with its own branding and design.

### Is Match Mania legal?

Yes. Match Mania implements generic card game mechanics and uses original artwork and branding. It does not use any trademarked names, logos, or designs.

### Can I distribute Match Mania?

You can share links to the official GitHub repository. Redistributing modified versions requires compliance with the Apache License 2.0.

## Future Plans

### What features are planned?

Possible future additions:
- Real multiplayer (local and online)
- Additional rule variations
- Card theme customization
- Tournament mode
- Cloud save/sync
- More AI difficulty levels
- Accessibility improvements

### When will feature X be added?

Development priorities are based on:
- User requests and feedback
- Technical feasibility
- Available development time

Check the [GitHub Issues](https://github.com/ssfdre38/match-mania/issues) for planned features and vote/comment on ones you want!

### Can I request a feature?

Yes! Please [submit a feature request](https://github.com/ssfdre38/match-mania/issues/new?template=feature_request.md) on GitHub.

## Still Have Questions?

- Check the [Troubleshooting Guide](Troubleshooting)
- Browse [GitHub Issues](https://github.com/ssfdre38/match-mania/issues)
- Start a [Discussion](https://github.com/ssfdre38/match-mania/discussions)
- Email: ssfdre38@gmail.com

---

**Last Updated**: January 2025  
**Related**: [Installation Guide](Installation-Guide) | [Troubleshooting](Troubleshooting) | [Contributing](Contributing)

### Where can I download Match Mania?

You can download Match Mania from:
- **Official Website**: [https://matchmaina.ssfdre38.xyz](https://matchmaina.ssfdre38.xyz) - All versions, changelog, and documentation
- **GitHub Releases**: [Latest Release](https://github.com/ssfdre38/match-mania/releases/latest)

The official website is recommended as it provides direct downloads, complete version history, and comprehensive documentation.

### What's the latest version?

The current version is **v2.3.3** (October 2025), which includes automatic background downloads, APK verification, and enhanced OTA update features.
