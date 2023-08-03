
package com.liferay.portal.freemarker;
import com.liferay.portal.security.pacl.PACLClassLoaderUtil;
import com.liferay.portal.util.PropsValues;
import freemarker.core.Environment;
import freemarker.core.TemplateClassResolver;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.utility.ObjectConstructor;
public class LiferayTemplateClassResolver implements TemplateClassResolver {
	public Class<?> resolve(
			String className, Environment environment, Template template)
		throws TemplateException {
		if (className.equals(ObjectConstructor.class.getName())) {
			throw new TemplateException(
				"Instantiating " + className + " is not allowed in the " +
					"template for security reasons",
				environment);
		}
		for (String restrictedClassName :
				PropsValues.FREEMARKER_ENGINE_RESTRICTED_CLASSES) {
			if (className.equals(restrictedClassName)) {
				throw new TemplateException(
					"Instantiating " + className + " is not allowed in the " +
						"template for security reasons",
					environment);
			}
		}
		for (String restrictedPackageName :
				PropsValues.FREEMARKER_ENGINE_RESTRICTED_PACKAGES) {
			if (className.startsWith(restrictedPackageName)) {
				throw new TemplateException(
					"Instantiating " + className + " is not allowed in the " +
						"template for security reasons",
					environment);
			}
		}
		try {
			return Class.forName(
				className, true, PACLClassLoaderUtil.getContextClassLoader());
		}
		catch (Exception e) {
			throw new TemplateException(e, environment);
		}
	}
}