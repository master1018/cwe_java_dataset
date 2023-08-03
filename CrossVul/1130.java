
package com.liferay.portal.freemarker;
import com.liferay.portal.kernel.templateparser.TemplateContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Theme;
import com.liferay.portal.template.TemplateContextHelper;
import com.liferay.portal.template.TemplatePortletPreferences;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.WebKeys;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.utility.ObjectConstructor;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
public class FreeMarkerTemplateContextHelper extends TemplateContextHelper {
	@Override
	public Map<String, Object> getHelperUtilities() {
		Map<String, Object> helperUtilities = super.getHelperUtilities();
		helperUtilities.put(
			"enumUtil", BeansWrapper.getDefaultInstance().getEnumModels());
		helperUtilities.put("objectUtil", new ObjectConstructor());
		helperUtilities.put(
			"freeMarkerPortletPreferences", new TemplatePortletPreferences());
		helperUtilities.put(
			"staticUtil", BeansWrapper.getDefaultInstance().getStaticModels());
		return helperUtilities;
	}
	@Override
	public Set<String> getRestrictedVariables() {
		return SetUtil.fromArray(
			PropsValues.JOURNAL_TEMPLATE_FREEMARKER_RESTRICTED_VARIABLES);
	}
	@Override
	public void prepare(
		TemplateContext templateContext, HttpServletRequest request) {
		super.prepare(templateContext, request);
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);
		if (themeDisplay != null) {
			Theme theme = themeDisplay.getTheme();
			String servletContextName = GetterUtil.getString(
				theme.getServletContextName());
			templateContext.put(
				"fullCssPath",
				StringPool.SLASH + servletContextName +
					theme.getFreeMarkerTemplateLoader() + theme.getCssPath());
			templateContext.put(
				"fullTemplatesPath",
				StringPool.SLASH + servletContextName +
					theme.getFreeMarkerTemplateLoader() +
						theme.getTemplatesPath());
			templateContext.put(
				"init",
				StringPool.SLASH + themeDisplay.getPathContext() +
					FreeMarkerTemplateLoader.SERVLET_SEPARATOR +
						"/html/themes/_unstyled/templates/init.ftl");
		}
		Map<String, Object> ftlVariables =
			(Map<String, Object>)request.getAttribute(WebKeys.FTL_VARIABLES);
		if (ftlVariables != null) {
			for (Map.Entry<String, Object> entry : ftlVariables.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (Validator.isNotNull(key)) {
					templateContext.put(key, value);
				}
			}
		}
	}
}