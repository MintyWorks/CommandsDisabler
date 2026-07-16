# CommandsDisabler

A tiny Paper plugin that blocks configurable commands (`/help`, `/plugins`, `/version`, etc.)
for players without a bypass permission.

Targets **Paper 26.2** (Minecraft 26.2 / "Chaos Cubed") and **Java 25**. I'll upload older versions later since this plugin was mainly for my own personal purposes.

## Configuration

`src/main/resources/config.yml`:

```yaml
deny-message: "&cThat command is disabled on this server."
vanilla-style: "simple"

disabled-commands:
  - "?"
  - "help"
  - "pl"
  - "about"
  - "version"
  - "ver"
  - "plugins"
  - "lay"
  - "sitandlay"
```

`deny-message` set to `""` falls back to a vanilla-style message instead, controlled by
`vanilla-style`:

- `"simple"` -> `Unknown command. Type "/help" for help.`
- `"detailed"` -> the same line, plus a second line echoing what was typed with a
  `<--[HERE]` marker, matching how vanilla points at command parse errors:
  ```
  Unknown command. Type "/help" for help.
  /plugins<--[HERE]
  ```

Add or remove entries (no leading `/`), then run `/commandsdisabler reload` (aliases:
`/cd reload`, `/cmdisabler reload`) as an op or console.
