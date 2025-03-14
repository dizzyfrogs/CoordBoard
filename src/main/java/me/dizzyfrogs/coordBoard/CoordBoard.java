package me.dizzyfrogs.coordBoard;

import me.dizzyfrogs.coordBoard.place.PlaceManager;
import org.mineacademy.fo.menu.button.ButtonReturnBack;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.util.Arrays;

public final class CoordBoard extends SimplePlugin {

    private PlaceManager placeManager;

    @Override
    public void onPluginStart() {
        // Plugin startup logic
        initPlaceManager();

        registerCommand(new CoordsCommand(this));

        ButtonReturnBack.setTitle("&c&lReturn");
        ButtonReturnBack.setLore(Arrays.asList("","&7Return to last page"));
    }

    @Override
    public void onPluginStop() {
        // Plugin shutdown logic
        placeManager.savePlaces();
    }

    public void initPlaceManager() {
        this.placeManager = new PlaceManager(this);
        this.placeManager.reload();
    }

    public PlaceManager getPlaceManager() {
        return this.placeManager;
    }
}
