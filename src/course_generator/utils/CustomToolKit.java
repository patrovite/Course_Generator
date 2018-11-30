package course_generator.utils;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

public class CustomToolKit extends HTMLEditorKit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static HTMLFactory factory = null;


	@Override
	public ViewFactory getViewFactory() {
		if (factory == null) {
			factory = new HTMLFactory() {

				@Override
				public View create(Element elem) {
					AttributeSet attrs = elem.getAttributes();
					Object elementName = attrs.getAttribute(AbstractDocument.ElementNameAttribute);
					Object o = (elementName != null) ? null : attrs.getAttribute(StyleConstants.NameAttribute);
					if (o instanceof HTML.Tag) {
						HTML.Tag kind = (HTML.Tag) o;
						if (kind == HTML.Tag.IMG) {
							BASE64ImageView ff = new BASE64ImageView(elem);
							return new BASE64ImageView(elem);
						}
					}
					return super.create(elem);
				}
			};
		}
		return factory;
	}

}
