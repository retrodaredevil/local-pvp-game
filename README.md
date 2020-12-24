# Lunar Periphery
An outer space game with pvp.

### Local multiplayer goals:
* Can be hosted by a player or a dedicated server (will be called the server)
* All packets are sent to the server
* The server sends packets back to the clients

Port: 12288

Client Side:
* Location and movement of player

Server Side:
* Entity collisions

Controls:
* Support keyboard and mouse at first, support controllers later
* Controllers and keyboard/mouse can be used at the same time
* We don't have to worry about same screen multiplayer, so code to handle inputs can be simpler

UIs to use
* https://github.com/czyzby/gdx-skins/
* sgx, freezing, quantum horizon, shade, star soldier

Possibilities:
* Use TiledMaps
* There can be different terrains. In space, use a jetpack to move around. On land, walk.

Code Style
* Use `.editorconfig` file and enable continuation indent for everything that you can enable in IntelliJ
* Use trailing commas for lists that span multiple lines
