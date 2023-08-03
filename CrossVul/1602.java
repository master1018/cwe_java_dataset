
package org.richfaces.renderkit.html;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.FacesException;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import org.ajax4jsf.resource.GifRenderer;
import org.ajax4jsf.resource.ImageRenderer;
import org.ajax4jsf.resource.InternetResourceBase;
import org.ajax4jsf.resource.JpegRenderer;
import org.ajax4jsf.resource.PngRenderer;
import org.ajax4jsf.resource.ResourceContext;
import org.ajax4jsf.resource.ResourceRenderer;
import org.ajax4jsf.util.HtmlColor;
import org.richfaces.component.UIPaint2D;
public class Paint2DResource extends InternetResourceBase {
	private static final ImageRenderer[] _renderers= {new GifRenderer(), new JpegRenderer(), new PngRenderer()};
	public ResourceRenderer getRenderer() {
		return _renderers[0];
	}
	public ResourceRenderer getRenderer(ResourceContext context) {
		ImageData data = null;
		if (context != null) {
			data = (ImageData) restoreData(context);
		}
		ImageRenderer renderer = _renderers[null==data?0:data._format];
		return renderer;
	}
	public boolean isCacheable() {
		return false;
	}
	public boolean isCacheable(ResourceContext resourceContext) {
		ImageData data = (ImageData) restoreData(resourceContext);
		return data.cacheable;
	}
	public boolean requireFacesContext() {
		return true;
	}
	protected Object getDataToStore(FacesContext context, Object data) {
		if (data instanceof UIPaint2D) {
			UIPaint2D paint2D = (UIPaint2D) data;
			ImageData dataToStore = new ImageData();
			dataToStore._width = paint2D.getWidth();
			dataToStore._height = paint2D.getHeight();
			dataToStore._data = paint2D.getData();
			dataToStore._paint = UIComponentBase.saveAttachedState(context, paint2D.getPaint());
			String format = paint2D.getFormat();
			if("jpeg".equalsIgnoreCase(format)) {
				dataToStore._format = 1;
			} else if("png".equalsIgnoreCase(format)) {
				dataToStore._format = 2;
			}
			String bgColor = paint2D.getBgcolor();
			try {
				dataToStore._bgColor = HtmlColor.decode(bgColor).getRGB();
			} catch (Exception e) {}
			dataToStore.cacheable = paint2D.isCacheable();
			return dataToStore;
		} else {
			throw new FacesException("Data for painting image resource not instance of UIPaint2D");
		}
	}
	private static final class ImageData implements Serializable {
		private static final long serialVersionUID = 4452040100045367728L;
		int _width=1;
		int _height = 1;
		Object _data;
		int _format = 0;
		Object _paint;
		boolean cacheable = false;
		int _bgColor = 0;
	}
	public Dimension getDimensions(FacesContext facesContext, Object data){
		if (data instanceof UIPaint2D) {
			UIPaint2D paint2D = (UIPaint2D) data;
			return new Dimension(paint2D.getWidth(),paint2D.getHeight());
		}
		return new Dimension(1,1);
	}
	protected Dimension getDimensions(ResourceContext resourceContext){
		ImageData data = (ImageData) restoreData(resourceContext);
		return new Dimension(data._width,data._height);
	}
	public void send(ResourceContext context) throws IOException {
		ImageData data = (ImageData) restoreData(context);
		ImageRenderer renderer = (ImageRenderer) getRenderer(context);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		try {
			BufferedImage image = renderer.createImage(data._width,data._height);
			Graphics2D graphics = image.createGraphics();
			try {
				if (data._bgColor != 0) {
					Color color = new Color(data._bgColor);
					graphics.setBackground(color);
					graphics.clearRect(0, 0, data._width, data._height);
				}
				MethodBinding paint = (MethodBinding) UIComponentBase.restoreAttachedState(facesContext, data._paint);
				paint.invoke(facesContext, new Object[] {graphics,data._data});
			} finally {
				if (graphics != null) {
					graphics.dispose();
				}
			}
			renderer.sendImage(context, image);
		} catch (Exception e) {
			throw new FacesException("Error send image ",e);
		}
	}
}
