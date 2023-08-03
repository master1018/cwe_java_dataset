
package org.ajax4jsf.resource;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
public class UserResource extends InternetResourceBase {
	private String contentType;
	public UserResource(boolean cacheable, boolean session, String mime) {
		super();		
		setCacheable(cacheable);
		setSessionAware(session);
		setContentType(mime);
	}
	public String getContentType(ResourceContext resourceContext) {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public Object getDataToStore(FacesContext context, Object data) {
		UriData dataToStore = null;
		if (data instanceof ResourceComponent2) {
			ResourceComponent2 resource = (ResourceComponent2) data;
			dataToStore = new UriData();
			dataToStore.value = resource.getValue();
			dataToStore.createContent = UIComponentBase.saveAttachedState(context,resource.getCreateContentExpression());
			if (data instanceof UIComponent) {
				UIComponent component = (UIComponent) data;
				ValueExpression expires = component.getValueExpression("expires");
				if (null != expires) {
					dataToStore.expires = UIComponentBase.saveAttachedState(context,expires);
				}
				ValueExpression lastModified = component.getValueExpression("lastModified");
				if (null != lastModified) {
					dataToStore.modified = UIComponentBase.saveAttachedState(context,lastModified);
				}
			}
		}
		return dataToStore;
	}
	public void send(ResourceContext context) throws IOException {
		UriData data = (UriData) restoreData(context);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (null != data && null != facesContext ) {
			ELContext elContext = facesContext.getELContext();
			OutputStream out = context.getOutputStream();
			MethodExpression send = (MethodExpression) UIComponentBase.restoreAttachedState(facesContext,data.createContent);
			send.invoke(elContext,new Object[]{out,data.value});
		}
	}
	@Override
	public Date getLastModified(ResourceContext resourceContext) {
		UriData data = (UriData) restoreData(resourceContext);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (null != data && null != facesContext ) {
			ELContext elContext = facesContext.getELContext();
			if(data.modified != null){
			ValueExpression binding = (ValueExpression) UIComponentBase.restoreAttachedState(facesContext,data.modified);
			Date modified = (Date) binding.getValue(elContext);
			if (null != modified) {
				return modified;
			}
		}
		}
		return super.getLastModified(resourceContext);
	}
	@Override
	public long getExpired(ResourceContext resourceContext) {
		UriData data = (UriData) restoreData(resourceContext);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (null != data && null != facesContext ) {
			ELContext elContext = facesContext.getELContext();
			if(data.expires != null){
			ValueExpression binding = (ValueExpression) UIComponentBase.restoreAttachedState(facesContext,data.expires);
			Date expires = (Date) binding.getValue(elContext);
			if (null != expires) {
				return expires.getTime()-System.currentTimeMillis();
			}
		}
		}
		return super.getExpired(resourceContext);
	}
	public boolean requireFacesContext() {
		return true;
	}
	public static class UriData implements Serializable {
		private static final long serialVersionUID = 1258987L;
		private Object value;
		private Object createContent;
		private Object expires;
		private Object modified;
	}
}
