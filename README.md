# CoordBoard

CoordBoard is a lightweight Minecraft plugin that provides an easy way to share and manage coordinates in-game. Designed for small servers that want to keep a vanilla feel without relying on teleport commands like `/home`, this plugin makes it simple to track and share important locations.

## Features

- **`/coords` Command** – Opens the CoordBoard interface.
- **Create and Manage Posts** – Players can create coordinate posts with:
  - Custom **names**
  - Selectable **icons**
  - Specific **coordinates**
- **Public & Private Posts** – Players can:
  - View public posts from others
  - Manage their own public and private posts
- **Edit & Delete** – Users can modify or remove their own posts.
- **Vanilla-Friendly** – Keeps gameplay immersive without the need for teleport commands, making it easy to share locations without external note-taking or Discord messages.

## Installation

1. Download the latest version of **CoordBoard**.
2. Place the `.jar` file in your server's `plugins` folder.
3. Restart the server.
4. You're ready to go! Use `/coords` in-game to start sharing locations.

## Built With

- CoordBoard utilizes the **Foundation API** by [Kangarko](https://github.com/kangarko/Foundation) to streamline development and enhance functionality.

## Usage

- Type `/coords` to open the coordinate board.
- Add a new coordinate post with a name, icon, and coordinates.
- View public posts shared by other players.
- Manage your own posts (edit, delete, or keep them private).

## Permissions

- **`coordboard.command.coords`** – Allows players to use `/coords` to view and manage coordinate posts.
- **`coordboard.admin`** – Grants administrative privileges, such as viewing private posts.

## How to Add Permissions (Without a Permission Manager)

If you're not using a permission manager like LuckPerms or PermissionsEx, you can still grant players access to commands and manage permissions manually through the **`permissions.yml`** file in your server.

Here’s how to add permissions for players manually:

1. **Locate the `permissions.yml` file** in your server’s root directory. This file is used to define permissions without relying on a plugin-based permission system.
   
2. **Open `permissions.yml`** in a text editor.

3. **Add the necessary permissions**. To allow all players to use the `/coords` command, add the following lines to the file:


   ```yaml
   groups:
     default:
       permissions:
         - coordboard.command.coords
   ```
		 
This grants **all players** (the default group) permission to use the `/coords` command.

### Add Admin Permissions
If you want to give admins access to special commands like viewing private posts, you can grant them the `coordboard.admin` permission. You can add this to the `admins` group or manually to specific players:

   ```yaml
   groups:
     admin:
       permissions:
         - coordboard.command.coords
	     - coordboard.admin
   ```
	
Save the `permissions.yml` file and restart the server.

## Support & Feedback
If you have suggestions or need help, feel free to reach out!
