/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pierre.delore
 */

package course_generator.tiles.thunderforest;

import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.AbstractOsmTileSource;


public class Thunderforest_Outdoors extends AbstractOsmTileSource{
    //private static final String PATTERN = "https://%s.tile.openstreetmap.org";
    private static final String PATTERN = "http://%s.tile.thunderforest.com/outdoors";
    private static final String[] SERVER = { "a", "b", "c" };

    private int SERVER_NUM = 0;

    /**
     * Constructs a new {@code "Mapnik"} tile source.
     */
    public Thunderforest_Outdoors() {
        super("Outdoors", PATTERN);
    }

    @Override
    public String getBaseUrl() {
        String url = String.format(this.baseUrl, new Object[] { SERVER[SERVER_NUM] });
        SERVER_NUM = (SERVER_NUM + 1) % SERVER.length;
        return url;
    }

    @Override
    public int getMaxZoom() {
        return 18;
    }

    public TileSource.TileUpdate getTileUpdate() {
        return TileSource.TileUpdate.IfNoneMatch;
    }    
}
