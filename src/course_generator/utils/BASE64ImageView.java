package course_generator.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.ImageView;

public class BASE64ImageView extends ImageView {

	private URL url;


	public BASE64ImageView(Element elmnt) {
		super(elmnt);
		populateImage();
	}


	private void populateImage() {
		@SuppressWarnings("unchecked")
		Dictionary<URL, Image> cache = (Dictionary<URL, Image>) getDocument().getProperty("imageCache");
		if (cache == null) {
			cache = new Hashtable<>();
			getDocument().putProperty("imageCache", cache);
		}

		URL src = getImageURL();
		cache.put(src, loadImage());

	}


	private Image loadImage() {
		String b64 = getBASE64Image();
		BufferedImage newImage = null;
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(org.apache.commons.codec.binary.Base64.decodeBase64(b64.getBytes()));
			newImage = ImageIO.read(bais);
			bais.close();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		return newImage;
	}


	@Override
	public URL getImageURL() {
		String src = (String) getElement().getAttributes().getAttribute(HTML.Attribute.SRC);
		if (isBase64Encoded(src)) {

			this.url = BASE64ImageView.class.getProtectionDomain().getCodeSource().getLocation();

			return this.url;
		}
		return super.getImageURL();
	}


	private boolean isBase64Encoded(String src) {
		return src != null && src.contains("base64,");
	}


	private String getBASE64Image() {
		String src = (String) getElement().getAttributes().getAttribute(HTML.Attribute.SRC);
		if (!isBase64Encoded(src)) {
			return null;
		}
		return src.substring(src.indexOf("base64,") + 7, src.length() - 1);
	}

}