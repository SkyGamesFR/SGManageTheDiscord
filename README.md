# SGManageTheDiscord

## Launch Procedure

1. Clone this repository to your local machine.
2. Create a `global.properties` file in the root directory of the project.
3. Add the following lines to the `global.properties` file:
    ```
    bot_token=
    bad_words=one,two,three,one a, two b, three c...
    ```
4. Add the following lines to the `sql.properties` file:
   ```
   # url properties
    host=127.0.0.1
    port=3306
    database=
    # additional options
    user=root
    password=
   ```
5. Launch the bot by running `java -jar SGManageTheDiscord-x.x.jar"`.
6Your bot is now running!

## Commands

### Basic Command
- `/help` - responds with all bot commands.
- `/ping` - responds with the bot's current latency.

### Moderation Commands
- `/kick` - kicks a member from the server.
- `/ban` - bans a member from the server.
- `/mute` - mutes a member in the server.
- `/unmute` - unmutes a member in the server.

### Auto Mod
This bot includes an auto moderation feature that automatically deletes messages containing inappropriate content. To enable auto mod, simply run `/auto-mod` on in your server.

### Music Command
- `/join` - join a voice channel.
- `/nowplaying` - displays the currently playing song.
- `/play` - plays a song in a voice channel.
- `/queue` - displays the current queue.
- `/pause` - pauses the currently playing song.
- `/resume` - resumes the currently paused song.
- `/skip` - skips the currently playing song.
- `/stop` - stops the currently playing song and clears the queue.
- `/volume` - changes the volume of the currently playing song.
