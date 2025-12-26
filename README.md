# Waystone Injector — Server Mod (Minecraft Forge 1.20.1)

This is the **server-side companion** to the Waystone Injector client mod.

It provides **Ze Voidrobe**: a per-player, 54-slot personal storage that opens as a vanilla 6-row chest menu.

## Requirements

- Minecraft **1.20.1**
- Forge **1.20.1**

Client mod is optional:
- **With the client mod**: players get a convenient UI button to open Ze Voidrobe.
- **Without the client mod**: players can still use commands (below).

## Install

1. Put the server jar in your server’s `mods/` folder.
2. Restart the server.

## How to use

### Commands

- `/vault` — Opens Ze Voidrobe.
- `/vaultclear <player>` — Clears a player’s Ze Voidrobe (admin tool).

Notes:
- These commands can be enabled/disabled and permission-gated in the server config.

### From the Waystones GUI (with the client mod)

If a player has the client mod installed, they can open Ze Voidrobe from the Waystones UI via the Ze Voidrobe button.

## Configuration

Server config is generated after first run:

- `config/waystoneinjectorserver-server.toml`

Settings include:
- Enable/disable Ze Voidrobe
- Require OP/permission level
- Allow/disallow opening while dead or in spectator
- Optional dimension allowlist

## PlayerSync / PlayerSync-Plus support

If **PlayerSync** or **PlayerSync Performance Plus** is installed, Ze Voidrobe contents are mirrored into the player’s persistent NBT so those mods can synchronize it across servers.

No extra setup required.

## Notes

- Ze Voidrobe is stored per player.
- This mod is designed to be safe to run on dedicated servers.
