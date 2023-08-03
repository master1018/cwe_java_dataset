package com.dotmarketing.factories;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dotcms.repackage.org.apache.commons.beanutils.BeanUtils;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import com.dotmarketing.beans.Host;
import com.dotmarketing.beans.Identifier;
import com.dotmarketing.beans.UserProxy;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.cms.factories.PublicAddressFactory;
import com.dotmarketing.cms.factories.PublicCompanyFactory;
import com.dotmarketing.cms.factories.PublicEncryptionFactory;
import com.dotmarketing.db.HibernateUtil;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotHibernateException;
import com.dotmarketing.exception.DotRuntimeException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.exception.WebAssetException;
import com.dotmarketing.portlets.contentlet.business.HostAPI;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.fileassets.business.FileAssetAPI;
import com.dotmarketing.portlets.files.business.FileAPI;
import com.dotmarketing.portlets.folders.model.Folder;
import com.dotmarketing.portlets.mailinglists.factories.MailingListFactory;
import com.dotmarketing.portlets.mailinglists.model.MailingList;
import com.dotmarketing.portlets.webforms.model.WebForm;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.FormSpamFilter;
import com.dotmarketing.util.InodeUtils;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.Mailer;
import com.dotmarketing.util.Parameter;
import com.dotmarketing.util.UtilMethods;
import com.dotmarketing.util.VelocityUtil;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.language.LanguageException;
import com.liferay.portal.language.LanguageUtil;
import com.liferay.portal.model.Address;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.util.FileUtil;
public class EmailFactory {
	private static Long emailTime = new Long(System.currentTimeMillis());
	public static StringBuffer alterBodyHTML(StringBuffer HTML, String serverName) {
		return new StringBuffer(alterBodyHTML(HTML.toString(), serverName));
	}
	public static String alterBodyHTML(String HTML, String serverName) {
		HTML = HTML.replaceAll("(?i)(?s)<a[^>]+href=\"([^/(http:
		"<a href=\"http:
		HTML = HTML.replaceAll(
				"(?i)(?s)<a href=\"([^#])([^>]+)\"[^>]*>(.*?)</[^>]*a[^>]*>",
				"<a href=\"http:
				+ serverName
				+ "/redirect?r=<rId>&redir=$1$2\">$3</a>")
				.replaceAll("<a href=\"http:
		HTML = alterBodyHtmlAbsolutizePaths(HTML, serverName);
		return HTML;
	}
	public static StringBuffer alterBodyHtmlAbsolutizePaths(StringBuffer HTML, String serverName)
	{
		return new StringBuffer(alterBodyHtmlAbsolutizePaths(HTML.toString(), serverName));
	}
	public static String alterBodyHtmlAbsolutizePaths(String HTML, String serverName)
	{
		String message = HTML;
		message = message
		.replaceAll(
				"<\\s*td([^>]*)background\\s*=\\s*[\"\']?([^\'\">]*)[\"\']?([^>]*)>",
				"<td$1background=\"http:
				+ serverName
				+ "$2\"$3>");
		message = message
		.replaceAll(
				"<\\s*tr([^>]*)background\\s*=\\s*[\"\']?([^\'\">]*)[\"\']?([^>]*)>",
				"<tr$1background=\"http:
				+ serverName
				+ "$2\"$3>");
		message = message.replaceAll(
				"<\\s*img([^>]*)src\\s*=\\s*[\"\']?([^\'\">]*)[\"\']?([^>]*)>",
				"<img$1src=\"http:
				+ "$2\"$3>");
		message = message
		.replaceAll(
				"<\\s*link([^>]*)href\\s*=\\s*[\"\']?([^\'\">]*)[\"\']?([^>]*)>",
				"<link$1href=\"http:
				+ serverName
				+ "$2\"$3>");
		message = message
		.replaceAll(
				"<\\s*script([^>]*)src\\s*=\\s*[\"\']?([^\'\">]*)[\"\']?([^>]*)>",
				"<script$1src=\"http:
				+ serverName
				+ "$2\"$3>");
		message = message
		.replaceAll(
				"<\\s*applet([^>]*)codebase\\s*=\\s*[\"\']?([^\'\">]*)[\"\']?([^>]*)>",
				"<applet$1codebase=\"http:
				+ serverName
				+ "$2\"$3>");
		message = message
		.replaceAll(
				"<\\s*applet(([^>][^(codebase)])*)code\\s*=\\s*[\"\']?([^\'\">]*)[\"\']?(([^>][^(codebase)])*)>",
				"<applet$1code=\"http:
				+ serverName
				+ "$4\"$5>");
		message = message
		.replaceAll(
				"<\\s*iframe([^>]*)src\\s*=\\s*[\"\']?([^\'\">]*)[\"\']?([^>]*)>",
				"<iframe$1src=\"http:
				+ serverName
				+ "$2\"$3>");
		message = message
		.replaceAll(
				"<\\s*iframe([^>]*)longdesc\\s*=\\s*[\"\']?([^\'\">]*)[\"\']?([^>]*)>",
				"<iframe$1longdesc=\"http:
				+ serverName
				+ "$2\"$3>");
		message = message
		.replaceAll(
				"<\\s*frame([^>]*)src\\s*=\\s*[\"\']?([^\'\">]*)[\"\']?([^>]*)>",
				"<frame$1src=\"http:
				+ serverName
				+ "$2\"$3>");
		message = message
		.replaceAll(
				"<\\s*frame([^>]*)longdesc\\s*=\\s*[\"\']?([^\'\">]*)[\"\']?([^>]*)>",
				"<frame$1longdesc=\"http:
				+ serverName
				+ "$2\"$3>");
		message = message
		.replaceAll(
				"<([^>]*)style\\s*=\\s*[\"\']?([^\'\">]*)url\\s*\\(\\s*([^>]*)\\s*\\)([^\'\">]*)[\"\']?([^>]*)>",
				"<$1style=\"$2url(http:
				+ serverName
				+ "$3)$4\"$5>");
		message = message.replaceAll("http:
				+ serverName + "\\s*http:
		"http:
		return message;
	}
	public static boolean sendForgotPassword(User user, String newPassword, String hostId) throws DotDataException, DotSecurityException {
		HostAPI hostAPI = APILocator.getHostAPI();
		Context context = VelocityUtil.getBasicContext();
		context.put("user", user);
		context.put("UtilMethods", new UtilMethods());
		context.put("language", Long.toString(APILocator.getLanguageAPI().getDefaultLanguage().getId()));
		context.put("password", newPassword);
		Host host = hostAPI.find(hostId, user, true);
		context.put("host", host);
		StringWriter writer = new StringWriter();
		String idInode = APILocator.getIdentifierAPI().find(host, Config
				.getStringProperty("PATH_FORGOT_PASSWORD_EMAIL")).getInode();
		String languageStr = "_" + APILocator.getLanguageAPI().getDefaultLanguage().getId();
		try {
			String message = "";
			try {
				Template t = UtilMethods.getVelocityTemplate("live/"+ idInode+ languageStr + "."+ Config.getStringProperty("VELOCITY_HTMLPAGE_EXTENSION")); 
				t.merge(context, writer);
				Logger
				.debug(EmailFactory.class, "writer:"
						+ writer.getBuffer());
				message = writer.toString().trim();
			} catch (ResourceNotFoundException ex) {
				message = "<center><b>And error has ocurred loading de message's page<b></center>";
			}
			Mailer m = new Mailer();
			m.setToEmail(user.getEmailAddress());
			m.setSubject("Your " + host.getHostname() + " Password");
			m.setHTMLBody(message);
			m.setFromEmail(Config.getStringProperty("EMAIL_SYSTEM_ADDRESS"));
			return m.sendMessage();
		} catch (Exception e) {
			Logger.warn(EmailFactory.class, e.toString(), e);
			return false;
		}
	}
	public static boolean isSubscribed(MailingList list, User s){
		UserProxy up;
		try {
			up = com.dotmarketing.business.APILocator.getUserProxyAPI().getUserProxy(s,APILocator.getUserAPI().getSystemUser(), false);
		} catch (Exception e) {
			Logger.error(EmailFactory.class, e.getMessage(), e);
			throw new DotRuntimeException(e.getMessage(), e);
		}	
		return MailingListFactory.isSubscribed(list, up);
	}
	public static WebForm sendParameterizedEmail(Map<String,Object> parameters, Set<String> spamValidation, 
			Host host, User user) throws  DotRuntimeException
			{
		if(spamValidation != null)
			if (FormSpamFilter.isSpamRequest(parameters, spamValidation)) {
				throw new DotRuntimeException("Spam detected");
			}
		String ignoreString = ":formType:formName:to:from:subject:cc:bcc:html:dispatch:order:" +
		"prettyOrder:autoReplyTo:autoReplyFrom:autoReplyText:autoReplySubject:" +
		"ignore:emailTemplate:autoReplyTemplate:autoReplyHtml:chargeCreditCard:attachFiles:";
		if(UtilMethods.isSet(getMapValue("ignore", parameters))) {
			ignoreString += getMapValue("ignore", parameters).toString().replace(",", ":") + ":";
		}
		String order = (String)getMapValue("order", parameters);
		Map<String, Object> orderedMap = new LinkedHashMap<String, Object>();
		String prettyOrder = (String)getMapValue("prettyOrder", parameters);
		Map<String, String> prettyVariableNamesMap = new LinkedHashMap<String, String>();
		String attachFiles = (String)getMapValue("attachFiles", parameters);
		if (order != null) {
			String[] orderArr = order.split("[;,]");
			String[] prettyOrderArr = prettyOrder!=null?prettyOrder.split("[;,]"):new String[0];
			for (int i = 0; i < orderArr.length; i++) {
				String orderParam = orderArr[i].trim();
				Object value = (getMapValue(orderParam, parameters) == null) ? 
						null : getMapValue(orderParam, parameters);
				if(value != null) {
					if (prettyOrderArr.length > i) 
						prettyVariableNamesMap.put(orderArr[i].trim(), prettyOrderArr[i].trim());
					else
						prettyVariableNamesMap.put(orderArr[i].trim(), orderArr[i].trim());
					orderedMap.put(orderArr[i].trim(), value);
				}
			}
		}
		for (Entry<String, Object> param : parameters.entrySet()) {
			if(!orderedMap.containsKey(param.getKey())) {
				orderedMap.put(param.getKey(), param.getValue());
				prettyVariableNamesMap.put(param.getKey(), param.getKey());
			}
		}
		StringBuffer filesLinks = new StringBuffer();
		String formType = getMapValue("formType", parameters) != null?
				(String)getMapValue("formType", parameters):(String)getMapValue("formName", parameters);
				WebForm formBean = saveFormBean(parameters, host, formType, ignoreString, filesLinks);
				String from = UtilMethods.replace((String)getMapValue("from", parameters), "spamx", "");
				String to = UtilMethods.replace((String)getMapValue("to", parameters), "spamx", "");
				String cc = UtilMethods.replace((String)getMapValue("cc", parameters), "spamx", "");
				String bcc = UtilMethods.replace((String)getMapValue("bcc", parameters), "spamx", "");
				String fromName = UtilMethods.replace((String)getMapValue("fromName", parameters), "spamx", "");
				try { from = PublicEncryptionFactory.decryptString(from); } catch (Exception e) { }
				try { to = PublicEncryptionFactory.decryptString(to); } catch (Exception e) { }
				try { cc = PublicEncryptionFactory.decryptString(cc); } catch (Exception e) { }
				try { bcc = PublicEncryptionFactory.decryptString(bcc); } catch (Exception e) { }
				try { fromName = PublicEncryptionFactory.decryptString(fromName); } catch (Exception e) { }
				String subject = (String)getMapValue("subject", parameters);
				subject = (subject == null) ? "Mail from " + host.getHostname() + "" : subject;
				String emailFolder = (String)getMapValue("emailFolder", parameters);
				boolean html = getMapValue("html", parameters) != null?Parameter.getBooleanFromString((String)getMapValue("html", parameters)):true;
				String templatePath = (String) getMapValue("emailTemplate", parameters);
				Map<String, String> emailBodies = null;
				try {
					emailBodies = buildEmail(templatePath, host, orderedMap, prettyVariableNamesMap, filesLinks.toString(), ignoreString, user);
				} catch (Exception e) {
					Logger.error(EmailFactory.class, "sendForm: Couldn't build the email body text.", e);
					throw new DotRuntimeException("sendForm: Couldn't build the email body text.", e);
				}
				try {
					String filePath = FileUtil.getRealPath(Config.getStringProperty("EMAIL_BACKUPS"));
					new File(filePath).mkdir();
					File file = null;
					synchronized (emailTime) {
						emailTime = new Long(emailTime.longValue() + 1);
						if (UtilMethods.isSet(emailFolder)) {
							new File(filePath + File.separator + emailFolder).mkdir();
							filePath = filePath + File.separator + emailFolder;
						}
						file = new File(filePath + File.separator + emailTime.toString()
								+ ".html");
					}
					if (file != null) {
						java.io.OutputStream os = new java.io.FileOutputStream(file);
						BufferedOutputStream bos = new BufferedOutputStream(os);
						if(emailBodies.get("emailHTMLBody") != null)
							bos.write(emailBodies.get("emailHTMLBody").getBytes());
						else if(emailBodies.get("emailHTMLTableBody") != null) 
							bos.write(emailBodies.get("emailHTMLTableBody").getBytes());
						else
							bos.write(emailBodies.get("emailPlainTextBody").getBytes());
						bos.flush();
						bos.close();
						os.close();
					}
				} catch (Exception e) {
					Logger.warn(EmailFactory.class, "sendForm: Couldn't save the email backup in " + Config.getStringProperty("EMAIL_BACKUPS"));
				}
				Mailer m = new Mailer();
				m.setToEmail(to);
				m.setFromEmail(from);
				m.setFromName(fromName);
				m.setCc(cc);
				m.setBcc(bcc);
				m.setSubject(subject);
				if (html) {
					if(UtilMethods.isSet(emailBodies.get("emailHTMLBody")))
						m.setHTMLBody(emailBodies.get("emailHTMLBody"));
					else
						m.setHTMLBody(emailBodies.get("emailHTMLTableBody"));
				}
				m.setTextBody(emailBodies.get("emailPlainTextBody"));
				if(attachFiles != null) {
					attachFiles = "," + attachFiles.replaceAll("\\s", "") + ",";
					for(Entry<String, Object> entry : parameters.entrySet()) {
						if(entry.getValue() instanceof File && attachFiles.indexOf("," + entry.getKey() + ",") > -1) {
							File f = (File)entry.getValue();
							m.addAttachment(f, entry.getKey() + "." + UtilMethods.getFileExtension(f.getName()));
						}
					}
				}
				if (m.sendMessage()) {
					if ((UtilMethods.isSet((String)getMapValue("autoReplyTemplate", parameters)) ||
							UtilMethods.isSet((String)getMapValue("autoReplyText", parameters)))
							&& UtilMethods.isSet((String)getMapValue("autoReplySubject", parameters))
							&& UtilMethods.isSet((String)getMapValue("autoReplyFrom", parameters))) {
						templatePath = (String) getMapValue("autoReplyTemplate", parameters);
						if(UtilMethods.isSet(templatePath)) {
							try {
								emailBodies = buildEmail(templatePath, host, orderedMap, prettyVariableNamesMap, filesLinks.toString(), ignoreString, user);
							} catch (Exception e) {
								Logger.error(EmailFactory.class, "sendForm: Couldn't build the auto reply email body text. Sending plain text.", e);
							}
						}
						m = new Mailer();
						String autoReplyTo = (String)(getMapValue("autoReplyTo", parameters) == null?getMapValue("from", parameters):getMapValue("autoReplyTo", parameters));
						m.setToEmail(UtilMethods.replace(autoReplyTo, "spamx", ""));
						m.setFromEmail(UtilMethods.replace((String)getMapValue("autoReplyFrom", parameters), "spamx", ""));
						m.setSubject((String)getMapValue("autoReplySubject", parameters));
						String autoReplyText = (String)getMapValue("autoReplyText", parameters); 
						boolean autoReplyHtml = getMapValue("autoReplyHtml", parameters) != null?Parameter.getBooleanFromString((String)getMapValue("autoReplyHtml", parameters)):html;
						if (autoReplyText != null)
						{
							if(autoReplyHtml)
							{
								m.setHTMLBody((String)getMapValue("autoReplyText", parameters));
							} else {
								m.setTextBody((String)getMapValue("autoReplyText", parameters));
							}
						}
						else
						{
							if (autoReplyHtml) 
							{
								if(UtilMethods.isSet(emailBodies.get("emailHTMLBody")))
									m.setHTMLBody(emailBodies.get("emailHTMLBody"));
								else
									m.setHTMLBody(emailBodies.get("emailHTMLTableBody"));
							}
							m.setTextBody(emailBodies.get("emailPlainTextBody"));
						}
						m.sendMessage();
					}
				} else {
					if(formBean != null){
						try {
							HibernateUtil.delete(formBean);
						} catch (DotHibernateException e) {							
							Logger.error(EmailFactory.class, e.getMessage(), e);
						}
					}
					throw new DotRuntimeException("Unable to send the email");
				}
				return formBean;
			}
	public static Map<String, String> buildEmail(String templatePath, Host host, Map<String,Object> parameters, 
			Map<String, String> prettyParametersNamesMap, String filesLinks, String ignoreString, User user) 
			throws WebAssetException, ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException, DotDataException, DotSecurityException, PortalException, SystemException
			{
		StringBuffer emailHTMLBody = new StringBuffer();
		StringBuffer emailHTMLTableBody = new StringBuffer();
		StringBuffer emailPlainTextBody = new StringBuffer();
		if(UtilMethods.isSet(templatePath))
		{
			Identifier id = APILocator.getIdentifierAPI().find(host,templatePath);
			String idInode = id.getInode();
			String languageId = Long.toString((APILocator.getLanguageAPI().getDefaultLanguage().getId()));
			try {
				if(UtilMethods.isSet(parameters.get("languageId"))) {
					languageId = (String) parameters.get("languageId");
				}
			} catch(ClassCastException e) {
				Logger.info(EmailFactory.class, "Error parsing languageId");
			}
			String languageStr = "_" + languageId;
			Template t = null;
			try {
				if(InodeUtils.isSet(idInode)) {
					t = UtilMethods.getVelocityTemplate("live/"+ idInode + languageStr + "."+ Config.getStringProperty("VELOCITY_HTMLPAGE_EXTENSION")); 
				} else {
					t = UtilMethods.getVelocityTemplate(templatePath); 
				}
			} catch (Exception e) {
			}
			if (t != null) {
				HttpServletRequest request = (HttpServletRequest) parameters.get("request"); 
				HttpServletResponse response = (HttpServletResponse) parameters.get("response");
				Context context = null;
				if(InodeUtils.isSet(idInode) && request != null && response != null)
				{
					context = VelocityUtil.getWebContext(request,response);
				}
				else
				{
					context = VelocityUtil.getBasicContext();
				}
				for(Entry<String, Object> entry : parameters.entrySet()) {
					Object value = getMapValue(entry.getKey(), parameters);
					if(entry.getKey().equals("ccNumber") && value instanceof String) {
						value = (String)UtilMethods.obfuscateCreditCard((String)value);
					}
					if(entry.getKey().contains("cvv") && value instanceof String) {
						String valueString = (String)value;
						if(valueString.length() > 3){
							value = (String)UtilMethods.obfuscateString(valueString,2);
						}
						else {
							value = (String)UtilMethods.obfuscateString(valueString,1);
						}
					}
					context.put(entry.getKey(), value);
				}
				context.put("utilMethods", new UtilMethods());
				context.put("UtilMethods", new UtilMethods());
				context.put("host", host);
				if(user != null)
					context.put("user", user);
				StringWriter writer = new StringWriter();
				t.merge(context, writer);
				String textVar = writer.toString();
				emailHTMLBody = new StringBuffer(alterBodyHtmlAbsolutizePaths(replaceTextVar(textVar, parameters, user), host.getHostname()));
			}
		}
		String subject = (String)getMapValue("subject", parameters);
		subject = (subject == null) ? "Mail from " + host.getHostname(): subject;
		emailHTMLTableBody.append("<html><style>td{font-family:arial;font-size:10pt;}</style><BODY>");
		emailHTMLTableBody.append("<TABLE bgcolor=eeeeee width=95%>");
		emailHTMLTableBody.append("<TR><TD colspan=2><strong>Information from "
				+ host.getHostname() + ": " + subject
				+ "</strong></TD></TR>");
		emailPlainTextBody.append("Information from " + host.getHostname()
				+ ": \t" + subject + "\n\n");
		Iterator<Entry<String,Object>> it = parameters.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> e = (Entry<String, Object>) it.next();
			String key = e.getKey();
			Object mapvalue = getMapValue(key, parameters);
			if (mapvalue instanceof String) {
				String value = (String)mapvalue;
				if(key.equals("ccNumber") && value instanceof String) {
					value = (String)UtilMethods.obfuscateCreditCard((String)value);
				}
				if(key.contains("cvv") && value instanceof String) {
					String valueString = (String)value;
					if(valueString.length() > 3){
						value = (String)UtilMethods.obfuscateString(valueString,2);
					}
					else {
						value = (String)UtilMethods.obfuscateString(valueString,1);
					}
				}
				if (ignoreString.indexOf(":" + key + ":") < 0 && UtilMethods.isSet(value)) {
					String prettyKey = prettyParametersNamesMap.get(key);
					String capKey = prettyKey != null?prettyKey:UtilMethods.capitalize(key);
					emailHTMLTableBody.append("<TR><TD bgcolor=white valign=top nowrap>&nbsp;" + capKey + "&nbsp;</TD>");
					emailHTMLTableBody.append("<TD bgcolor=white valign=top width=100%>" + value + "</TD></TR>");
					emailPlainTextBody.append(capKey + ":\t" + value + "\n");
				}
			}
		}
		if (UtilMethods.isSet(filesLinks)) {
			emailHTMLTableBody.append("<TR><TD bgcolor=white valign=top nowrap>&nbsp;Files&nbsp;</TD>");
			emailHTMLTableBody.append("<TD bgcolor=white valign=top width=100%>" + filesLinks + "</TD></TR>");
			emailPlainTextBody.append("Files:\t" + filesLinks + "\n");
		}
		emailHTMLTableBody.append("</TABLE></BODY></HTML>");
		Map<String, String> returnMap = new HashMap<String, String>();
		if(UtilMethods.isSet(emailHTMLBody.toString()))
			returnMap.put("emailHTMLBody", emailHTMLBody.toString());
		if(UtilMethods.isSet(emailHTMLTableBody.toString()))
			returnMap.put("emailHTMLTableBody", emailHTMLTableBody.toString());
		returnMap.put("emailPlainTextBody", emailPlainTextBody.toString());
		return returnMap;
			}
	private static WebForm saveFormBean (Map<String, Object> parameters, Host host, String formType, String ignoreString, StringBuffer filesLinks) {
		String predefinedFields = ":prefix:title:firstName:middleInitial:middleName:lastName:fullName:organization:address:address1:address2:city:state:zip:country:phone:email:";
		WebForm formBean = new WebForm();
		formBean.setFormType(formType);
		try {
			for (Entry<String, Object> param : parameters.entrySet()) {
				BeanUtils.setProperty(formBean, param.getKey(), getMapValue(param.getKey(), parameters));
			}
		} catch (Exception e1) {
			Logger.error(EmailFactory.class, "sendForm: Error ocurred trying to copy the form bean parameters", e1);
		}
		try {
			HibernateUtil.save(formBean);
		} catch (DotHibernateException e) {
			Logger.error(EmailFactory.class, e.getMessage(), e);
		}		
		String formId = formBean.getWebFormId();
		StringBuffer customFields = new StringBuffer();
		Set<Entry<String, Object>> paramSet = parameters.entrySet();
		for (Entry<String, Object> param : paramSet) {
			String key = (String) param.getKey();
			String value = null;
			Object paramValue = getMapValue(key, parameters);
			if (paramValue instanceof File) {
				File f = (File) param.getValue();
				String submittedFileName = f.getName();
				String fileName = key + "." + UtilMethods.getFileExtension(submittedFileName);
				if(getMapValue(fileName.substring(4, key.length()) + "FName", parameters) != null) {
					fileName = getMapValue(fileName.substring(4, key.length()) + "FName", parameters) + 
						"." + UtilMethods.getFileExtension(submittedFileName);
				}
				try {
					if(f.exists()) {
						String filesFolder = getMapValue("formFolder", parameters) instanceof String?(String)getMapValue("formFolder", parameters):null;
						String fileLink = saveFormFile(formId, formType, fileName, f, host, filesFolder);
						filesLinks.append(filesLinks.toString().equals("")? "http:
					}
				} catch (Exception e) {
					Logger.error(EmailFactory.class, "sendForm: couldn't saved the submitted file into the cms = " + fileName, e);					
					try {
						HibernateUtil.delete(formBean);
					} catch (DotHibernateException e1) {
						Logger.error(EmailFactory.class, e1.getMessage(), e1);						
					}
					throw new DotRuntimeException("sendForm: couldn't saved the submitted file into the cms = " + fileName, e);
				}
			} else if (paramValue instanceof String)
				value = (String)paramValue;
			List<String> cFields = new ArrayList<String>();
			if (predefinedFields.indexOf(":" + key + ":") < 0
					&& ignoreString.indexOf(":" + key + ":") < 0
					&& UtilMethods.isSet(value)) {
				value = value.replaceAll("\\|", " ").replaceAll("=", " ");
				if(key.equals("ccNumber"))
					value = UtilMethods.obfuscateCreditCard(value);
				String capKey = UtilMethods.capitalize(key);
				int aux = 2;
				String capKeyAux = capKey;
				while (cFields.contains(capKeyAux)) {
					capKeyAux = capKey + aux;
					++aux;
				}
				cFields.add(capKeyAux);
				String cField = capKeyAux + "=" + value;
				customFields.append(cField + "|");
			}
		}
		customFields.append("Files=" + filesLinks);
		formBean.setCustomFields(customFields.toString());
		formBean.setSubmitDate(new Date());
		if(UtilMethods.isSet(formType)){
			try {
				HibernateUtil.saveOrUpdate(formBean);
			} catch (DotHibernateException e) {
				throw new DotRuntimeException("Webform Save Failed");
			}
		}
		else{
			Logger.debug(EmailFactory.class, "The web form doesn't have the required formType field, the form data will not be saved in the database.");
		}
		return formBean;
	}
	private static String getFormFileFolderPath (String formType, String formInode) {
		String path = Config.getStringProperty("SAVED_UPLOAD_FILES_PATH")
		+ "/" + formType.replace(" ", "_") + "/"
		+ String.valueOf(formInode).substring(0, 1) + "/" + formInode;
		return path;
	}
	private static String saveFormFile (String formInode, String formType, 
			String fileName, File fileToSave, Host currentHost, String filesFolder) throws Exception {
		FileAPI fileAPI=APILocator.getFileAPI();
		String path;
		if(filesFolder != null)
			path = filesFolder;
		else
			path = getFormFileFolderPath(formType, formInode);
		Folder folder = APILocator.getFolderAPI().createFolders(path, currentHost, APILocator.getUserAPI().getSystemUser(), false);
		String baseFilename = fileName;
		int c = 1;
		while(fileAPI.fileNameExists(folder, fileName)) {
			fileName = UtilMethods.getFileName(baseFilename) + "-" + c + "." + UtilMethods.getFileExtension(baseFilename);
			c++;
		}
		Host host = APILocator.getHostAPI().find(folder.getHostId(), APILocator.getUserAPI().getSystemUser(), false);
		while(APILocator.getFileAssetAPI().fileNameExists(host,folder, fileName, "")) {
			fileName = UtilMethods.getFileName(baseFilename) + "-" + c + "." + UtilMethods.getFileExtension(baseFilename);
			c++;
		}
		Contentlet cont = new Contentlet();
		cont.setStructureInode(folder.getDefaultFileType());
		cont.setStringProperty(FileAssetAPI.TITLE_FIELD, UtilMethods.getFileName(fileName));
		cont.setFolder(folder.getInode());
		cont.setHost(host.getIdentifier());
		cont.setBinary(FileAssetAPI.BINARY_FIELD, fileToSave);
		APILocator.getContentletAPI().checkin(cont, APILocator.getUserAPI().getSystemUser(),false);
		return path + "/" + fileName;
	}
	private static String replaceTextVar(String template, Map<String, Object> parameters, User user) 
	{
		String finalMessageStr = template;
		Set<String> keys = parameters.keySet();
		for(String key : keys)
		{
			if(getMapValue(key, parameters) instanceof String) {
				String value = (String)getMapValue(key, parameters);
				value = (value != null ? value : "");
				finalMessageStr = finalMessageStr.replaceAll("(?i)(<|(&lt;))/"+ key +"(>|(&gt;))", "");
				finalMessageStr = finalMessageStr.replaceAll("(?i)(<|(&lt;))" + key + "(\")?( )**( )*(>|(&gt;))", (user.getFirstName()!=null) ? user.getFirstName() : "");
			finalMessageStr = finalMessageStr.replaceAll("(?i)(<|(&lt;))/varEmail(>|(&gt;))", "");
			finalMessageStr = finalMessageStr.replaceAll("(?i)(<|(&lt;))varEmail(\")?( )**( )*(>|(&gt;))", (user.getMiddleName()!=null) ? user.getMiddleName() : "");
			finalMessageStr = finalMessageStr.replaceAll("(?i)(<|(&lt;))/varLastName(>|(&gt;))", "");
			finalMessageStr = finalMessageStr.replaceAll("(?i)(<|(&lt;))varLastName(\")?( )**( )*(>|(&gt;))", (userproxy.getLastMessage()!=null) ? userproxy.getLastMessage() : "");
			finalMessageStr = finalMessageStr.replaceAll("(?i)(<|(&lt;))/varAddress1(>|(&gt;))", "");
			finalMessageStr = finalMessageStr.replaceAll("(?i)(<|(&lt;))varAddress1(\")?( )**( )*(>|(&gt;))", (address.getStreet2()!=null) ? address.getStreet2() : "");
			finalMessageStr = finalMessageStr.replaceAll("(?i)(<|(&lt;))/varPhone(>|(&gt;))", "");
			finalMessageStr = finalMessageStr.replaceAll("(?i)(<|(&lt;))varPhone(\")?( )**( )*(>|(&gt;))", (address.getState()!=null) ? address.getState() : "");
			finalMessageStr = finalMessageStr.replaceAll("(?i)(<|(&lt;))/varCity(>|(&gt;))", "");
			finalMessageStr = finalMessageStr.replaceAll("(?i)(<|(&lt;))varCity(\")?( )**( )*(>|(&gt;))", (address.getCountry()!=null) ? address.getCountry() : "");
			finalMessageStr = finalMessageStr.replaceAll("(?i)(<|(&lt;))/varZip(>|(&gt;))", "");
			finalMessageStr = finalMessageStr.replaceAll("(?i)(<|(&lt;))varZip(\")?( )**( )*(>|(&gt;))", (value != null) ? value : "");
					finalMessageStr = finalMessageStr.replaceAll("(?i)(<|(&lt;))/var" + varCounter + "(>|(&gt;))", "");
					finalMessageStr = finalMessageStr.replaceAll("(?i)(<|(&lt;))var" + varCounter + "(\")?( )*/*( )*(>|(&gt;))", (value != null) ? value : "");
				}
			} catch(LanguageException le) {
				Logger.error(EmailFactory.class, le.getMessage());
			}
		}
		return finalMessageStr;
	}
	public static Object getMapValue(String key, Map<String, Object> map) {
		try {
			try
			{
				if(((Object[]) map.get(key)).length > 1)
				{
					String returnValue = "";
					for(Object object : ((Object[]) map.get(key)))
					{
						returnValue += object.toString() + ", ";						
					}
					returnValue = returnValue.substring(0,returnValue.lastIndexOf(","));
					return returnValue;
				}
			}
			catch(Exception ex)
			{}
			return ((Object[]) map.get(key))[0];
		} catch (Exception e) {
			try {
				return (Object) map.get(key);
			} catch (Exception ex) {
				return null;
			}
		}
	}	
}
