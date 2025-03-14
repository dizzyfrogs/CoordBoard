package me.dizzyfrogs.coordBoard;

import me.dizzyfrogs.coordBoard.menu.MainMenu;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.Arrays;

public class CoordsCommand extends SimpleCommand {
    private final CoordBoard plugin;
    public CoordsCommand(CoordBoard plugin) {
        super("coords");
        this.plugin = plugin;
        setDescription("Opens the Coord Board GUI");
        setAliases(Arrays.asList("coordboard", "places"));
    }

    @Override
    protected void onCommand() {
        if (!(sender instanceof Player)) {
            tellError("You must be a player to use this command.");
            return;
        }

        Player player = (Player) sender;
        new MainMenu(plugin).displayTo(player);
    }
}
