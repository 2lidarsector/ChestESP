# ChestESP (Fabric) — Client-side Chest Highlight Mod

A small Fabric client-side mod for Minecraft 1.21.10 that highlights chest block entities. Toggling is done with `L`.

Features
- Client-only: does not send data to the server.
- Toggles on/off with the `L` key (configurable by changing the keybinding in-game).
- Uses Fabric API 0.135.0+1.21.10.

Requirements
- Java 17
- Gradle (Gradle wrapper included if you add it)
- Fabric Loom plugin configured in `build.gradle` (version may need adjusting)
- Minecraft 1.21.10 mappings and Fabric Loader compatible with your Loom version

Build
1. Place the project files in a directory.
2. Adjust Loom/mappings/loader versions in `gradle.properties` if required by your environment.
3. Run:
   ./gradlew build

The resulting jar will be in `build/libs/ChestESP-1.0.0.jar`. Put that jar in your Fabric client's `mods` folder.

Troubleshooting
- If the Loom plugin version in `build.gradle` is not compatible with your environment or Gradle version, change it to one compatible with your setup.
- If mappings (yarn) versions differ, update `yarn_mappings` in `gradle.properties`.
- If you see obfuscation/mapping compile errors, ensure you have matching mappings/loader versions (these change across Fabric/Loom versions).

License: MIT