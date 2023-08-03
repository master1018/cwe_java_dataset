
package com.liferay.portal.freemarker;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.security.lang.PortalSecurityManagerThreadLocal;
import com.liferay.portal.security.pacl.PACLPolicy;
import com.liferay.portal.template.TemplateContextHelper;
import freemarker.template.Configuration;
import java.io.Writer;
import java.util.Map;
public class PACLFreeMarkerTemplate extends FreeMarkerTemplate {
	public PACLFreeMarkerTemplate(
		String templateId, String templateContent, String errorTemplateId,
		String errorTemplateContent, Map<String, Object> context,
		Configuration configuration,
		TemplateContextHelper templateContextHelper,
		StringTemplateLoader stringTemplateLoader, PACLPolicy paclPolicy) {
		super(
			templateId, templateContent, errorTemplateId, errorTemplateContent,
			context, configuration, templateContextHelper,
			stringTemplateLoader);
		_paclPolicy = paclPolicy;
	}
	@Override
	public boolean processTemplate(Writer writer) throws TemplateException {
		PACLPolicy initialPolicy =
			PortalSecurityManagerThreadLocal.getPACLPolicy();
		try {
			PortalSecurityManagerThreadLocal.setPACLPolicy(_paclPolicy);
			return super.processTemplate(writer);
		}
		finally {
			PortalSecurityManagerThreadLocal.setPACLPolicy(initialPolicy);
		}
	}
	private PACLPolicy _paclPolicy;
}