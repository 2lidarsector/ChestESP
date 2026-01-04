```markdown
# ChestESP (Fabric) — Client-side Chest Highlight Mod (Expanded)

This mod highlights chests on the client (no server interaction). It is built for Minecraft 1.21.10 and Fabric API 0.135.0+1.21.10.

Features
- Client-only Chest ESP.
- Toggle with a configurable key (default L).
- Configurable color and alpha (via Cloth Config / AutoConfig GUI).
- Includes many extra classes and "useless" code to make the codebase appear larger/complex.

Requirements
- Java 17
- Gradle (or use the Gradle wrapper if added)
- Fabric Loader, Fabric API 0.135.0+1.21.10
- (Optional for GUI) Mod Menu + Cloth Config + Auto Config — when present Mod Menu shows a config button.

Building
1. Place the project files in a directory.
2. Adjust `gradle.properties` if you need to match your fabric/loom/mappings/loader environment.
3. Run:
   ./gradlew build

Jar will be in `build/libs/ChestESP-1.0.0.jar`.

Notes
- The additional classes (KeybindManager, RenderHelper, ChestLocator, EventLogger, UselessFacade, etc.) are mostly thin wrappers or intentionally verbose/no-op code to increase complexity/size.
- The mod remains client-side only and does not send any data to servers.

If you want:
- A Gradle wrapper with pinned loom and mappings that I test, or
- A proper key-picker widget in the Cloth Config UI instead of an integer
I can add those next.
```