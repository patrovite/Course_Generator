/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package course_generator.tiles.thunderforest;

import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.AbstractOsmTileSource;

/**
 *
 * @author pierre.delore
 */

//import org.openstreetmap.gui.jmapviewer.tilesources.AbstractOsmTileSource;
/**
 *
 * @author pierre.delore
 */
public class Thunderforest_Landscape extends AbstractOsmTileSource{
    //private static final String PATTERN = "https://%s.tile.openstreetmap.org";
    private static final String PATTERN = "http://%s.tile.thunderforest.com/landscape";
    private static final String[] SERVER = { "a", "b", "c" };

    private int SERVER_NUM = 0;

    /**
     * Constructs a new {@code "Mapnik"} tile source.
     */
    public Thunderforest_Landscape() {
        super("Landscape", PATTERN);
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
