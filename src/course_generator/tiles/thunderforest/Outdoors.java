package course_generator.tiles.thunderforest;

import java.io.IOException;

import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.AbstractOsmTileSource;

import course_generator.settings.CgSettings;

public class Outdoors extends AbstractOsmTileSource {
	private static final String PATTERN = "https://tile.thunderforest.com/outdoors";
	private static final String[] SERVER = { "a", "b", "c" };

	private int SERVER_NUM = 0;

	private CgSettings settings;


	/**
	 * Constructs a new {@code "Outdoors"} tile source.
	 */
	public Outdoors(CgSettings settings) {
		super("Outdoors", PATTERN);
		this.settings = settings;
	}


	@Override
	public String getBaseUrl() {
		String url = String.format(this.baseUrl, new Object[] { SERVER[SERVER_NUM] });
		SERVER_NUM = (SERVER_NUM + 1) % SERVER.length;
		return url;
	}


	@Override
	public String getTileUrl(int zoom, int tilex, int tiley) throws IOException {
		String tileUrl = super.getTileUrl(zoom, tilex, tiley) + "?apikey=" + settings.getThunderForestApiKey();

		return tileUrl;
	}


	@Override
	public int getMaxZoom() {
		return 18;
	}


	public TileSource.TileUpdate getTileUpdate() {
		return TileSource.TileUpdate.IfNoneMatch;
	}

}
