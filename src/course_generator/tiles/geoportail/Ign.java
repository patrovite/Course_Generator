package course_generator.tiles.geoportail;

import java.io.IOException;

import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.AbstractTMSTileSource;

public class Ign extends AbstractTMSTileSource {
	private static final String PATTERN = 
			"https://wxs.ign.fr/choisirgeoportail/geoportail/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=GEOGRAPHICALGRIDSYSTEMS.MAPS&STYLE=normal&FORMAT=image/jpeg&TILEMATRIXSET=PM&TILEMATRIX=%s&TILEROW=%s&TILECOL=%s)";

	/**
	 * Constructs a new {@code "Ign"} tile source.
	 */
	public Ign() {
		super("IGN", PATTERN);
	}


	@Override
	public String getTileUrl(int zoom, int tilex, int tiley) throws IOException {
		return String.format(this.baseUrl, zoom, tiley, tilex);
	}


	@Override
	public int getMaxZoom() {
		return 18;
	}


	public TileSource.TileUpdate getTileUpdate() {
		return TileSource.TileUpdate.IfNoneMatch;
	}

}
