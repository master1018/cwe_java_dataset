package cz.metacentrum.perun.core.impl;
import cz.metacentrum.perun.core.api.ExtSource;
import cz.metacentrum.perun.core.api.GroupsManager;
import cz.metacentrum.perun.core.api.exceptions.ExtSourceUnsupportedOperationException;
import cz.metacentrum.perun.core.api.exceptions.InternalErrorException;
import cz.metacentrum.perun.core.api.exceptions.SubjectNotExistsException;
import cz.metacentrum.perun.core.blImpl.PerunBlImpl;
import cz.metacentrum.perun.core.implApi.ExtSourceApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ExtSourceLdap extends ExtSource implements ExtSourceApi {
	protected Map<String, String> mapping;
	protected final static Logger log = LoggerFactory.getLogger(ExtSourceLdap.class);
	protected DirContext dirContext = null;
	protected String filteredQuery = null;
	protected DirContext getContext() throws InternalErrorException {
		if (dirContext == null) {
			initContext();
		}
		return dirContext;
	}
	private static PerunBlImpl perunBl;
	public static PerunBlImpl setPerunBlImpl(PerunBlImpl perun) {
		perunBl = perun;
		return perun;
	}
	@Override
	public List<Map<String,String>> findSubjectsLogins(String searchString) throws InternalErrorException {
		return findSubjectsLogins(searchString, 0);
	}
	@Override
	public List<Map<String,String>> findSubjectsLogins(String searchString, int maxResults) throws InternalErrorException {
		String query = getAttributes().get("query");
		if (query == null) {
			throw new InternalErrorException("query attributes is required");
		}
		query = query.replace("?", Utils.escapeStringForLDAP(searchString));
		String base = getAttributes().get("base");
		if (base == null) {
			throw new InternalErrorException("base attributes is required");
		}
		return this.querySource(query, base, maxResults);
	}
	@Override
	public Map<String, String> getSubjectByLogin(String login) throws InternalErrorException, SubjectNotExistsException {
		String query = getAttributes().get("loginQuery");
		if (query == null) {
			throw new InternalErrorException("loginQuery attributes is required");
		}
		query = query.replace("?", Utils.escapeStringForLDAP(login));
		String base = getAttributes().get("base");
		if (base == null) {
			throw new InternalErrorException("base attributes is required");
		}
		List<Map<String, String>> subjects = this.querySource(query, base, 0);
		if (subjects.size() > 1) {
			throw new SubjectNotExistsException("There are more than one results for the login: " + login);
		}
		if (subjects.size() == 0) {
			throw new SubjectNotExistsException(login);
		}
		return subjects.get(0);
	}
	@Override
	public List<Map<String, String>> getGroupSubjects(Map<String, String> attributes) throws InternalErrorException {
		List<String> ldapGroupSubjects = new ArrayList<>();
		String ldapGroupName = attributes.get(GroupsManager.GROUPMEMBERSQUERY_ATTRNAME);
		String filter = attributes.get(GroupsManager.GROUPMEMBERSFILTER_ATTRNAME);
		try {
			log.trace("LDAP External Source: searching for group subjects [{}]", ldapGroupName);
			String attrName;
			attrName = getAttributes().getOrDefault("memberAttribute", "uniqueMember");
			List<String> retAttrs = new ArrayList<>();
			retAttrs.add(attrName);
			String[] retAttrsArray = retAttrs.toArray(new String[0]);
			Attributes attrs = getContext().getAttributes(ldapGroupName, retAttrsArray);
			Attribute ldapAttribute = null;
			if (attrs.get(attrName) != null) {
				ldapAttribute = attrs.get(attrName);
			}
			if (ldapAttribute != null) {
				for (int i=0; i < ldapAttribute.size(); i++) {
					String ldapSubjectDN = (String) ldapAttribute.get(i);
					ldapGroupSubjects.add(ldapSubjectDN);
					log.trace("LDAP External Source: found group subject [{}].", ldapSubjectDN);
				}
			}
			List<Map<String, String>> subjects = new ArrayList<>();
			if(filter == null) filter = filteredQuery;
			for (String ldapSubjectName : ldapGroupSubjects) {
				subjects.addAll(this.querySource(filter, ldapSubjectName, 0));
			}
			return subjects;
		} catch (NamingException e) {
			log.error("LDAP exception during running query '{}'", ldapGroupName);
			throw new InternalErrorException("Entry '"+ldapGroupName+"' was not found in LDAP." , e);
		}
	}
	@Override
	public List<Map<String, String>> getUsersSubjects() throws ExtSourceUnsupportedOperationException {
		throw new ExtSourceUnsupportedOperationException();
	}
	protected void initContext() throws InternalErrorException {
		Hashtable<String,String> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		if (getAttributes().containsKey("referral")) {
			env.put(Context.REFERRAL, getAttributes().get("referral"));
		}
		if (getAttributes().containsKey("url")) {
			env.put(Context.PROVIDER_URL, getAttributes().get("url"));
		} else {
			throw new InternalErrorException("url attributes is required");
		}
		if (getAttributes().containsKey("user")) {
			env.put(Context.SECURITY_PRINCIPAL, getAttributes().get("user"));
		}
		if (getAttributes().containsKey("password")) {
			env.put(Context.SECURITY_CREDENTIALS, getAttributes().get("password"));
		}
		if (getAttributes().containsKey("filteredQuery")) {
			filteredQuery = getAttributes().get("filteredQuery");
		}
		try {
			if (getAttributes().get("ldapMapping") == null) {
				throw new InternalErrorException("ldapMapping attributes is required");
			}
			String[] ldapMapping = getAttributes().get("ldapMapping").trim().split(",\n");
			mapping = new HashMap<>();
			for (String entry: ldapMapping) {
				String[] values = entry.trim().split("=", 2);
				mapping.put(values[0].trim(), values[1].trim());
			}
			this.dirContext = new InitialDirContext(env);
		} catch (NamingException e) {
			log.error("LDAP exception during creating the context.");
			throw new InternalErrorException(e);
		}
	}
	protected Map<String,String> getSubjectAttributes(Attributes attributes) throws InternalErrorException {
		Pattern pattern = Pattern.compile("\\{([^}])*}");
		Map<String, String> map = new HashMap<>();
		for (String key: mapping.keySet()) {
			Matcher matcher = pattern.matcher(mapping.get(key));
			String value = mapping.get(key);
			while (matcher.find()) {
				String ldapAttributeNameRaw = matcher.group();
				String ldapAttributeName = ldapAttributeNameRaw.replaceAll("\\{([^}]*)}", "$1"); 
				value = value.replace(ldapAttributeNameRaw, getLdapAttributeValue(attributes, ldapAttributeName));
				log.trace("ExtSourceLDAP: Retrieved value {} of attribute {} for {} and storing into the key {}.", value, ldapAttributeName, ldapAttributeNameRaw, key);
			}
			map.put(key, value);
		}
		return map;
	}
	protected String getLdapAttributeValue(Attributes attributes, String ldapAttrNameRaw)  throws InternalErrorException {
		String ldapAttrName;
		String rule = null;
		Matcher matcher;
		String attrValue = "";
		if (ldapAttrNameRaw.contains("|")) {
			int splitter = ldapAttrNameRaw.indexOf('|');
			ldapAttrName = ldapAttrNameRaw.substring(0,splitter);
			rule = ldapAttrNameRaw.substring(splitter+1);
		} else {
			ldapAttrName = ldapAttrNameRaw;
		}
		int attributeValueIndex = -1;
		if (ldapAttrNameRaw.contains("[")) {
			Pattern indexPattern = Pattern.compile("^(.*)\\[([0-9]+)]$");
			Matcher indexMatcher = indexPattern.matcher(ldapAttrNameRaw);
			if (indexMatcher.find()) {
				ldapAttrName = indexMatcher.group(1);
				attributeValueIndex = Integer.parseInt(indexMatcher.group(2));
			} else {
				throw new InternalErrorException("Wrong attribute name format for attribute: " + ldapAttrNameRaw + ", it should be name[0-9+]");
			}
		}
		Attribute attr = attributes.get(ldapAttrName);
		if (attr != null) {
			for (int i = 0; i < attr.size(); i++) {
				if (attributeValueIndex != -1 && attributeValueIndex != i) {
					continue;
				}
				String tmpAttrValue;
				try {
					if(attr.get() instanceof byte[]) {
						char[] encodedValue = Base64Coder.encode((byte[]) attr.get());
						tmpAttrValue = new String(encodedValue);
					} else {
						tmpAttrValue = (String) attr.get(i);
					}
				} catch (NamingException e) {
					throw new InternalErrorException(e);
				}
				if (rule != null) {
					if(rule.contains("#")) {
						String regex = rule.substring(0, rule.indexOf('#'));
						String replacement = rule.substring(rule.indexOf('#')+1);
						tmpAttrValue = tmpAttrValue.replaceAll(regex, replacement);
					} else {
						Pattern pattern = Pattern.compile(rule);
						matcher = pattern.matcher(tmpAttrValue);
						if (matcher.matches()) {
							tmpAttrValue = matcher.group(1);
						}
					}
				}
				if (i == 0 || attributeValueIndex != -1) {
					attrValue += tmpAttrValue;
				} else {
					attrValue += AttributesManagerImpl.LIST_DELIMITER + tmpAttrValue;
				}
			}
			if (attrValue.isEmpty()) {
				return "";
			} else {
				return attrValue;
			}
		} else {
			return "";
		}
	}
	protected List<Map<String,String>> querySource(String query, String base, int maxResults) throws InternalErrorException {
		NamingEnumeration<SearchResult> results = null;
		List<Map<String, String>> subjects = new ArrayList<>();
		try {
			if (query == null) {
				log.trace("search base [{}]", base);
				Attributes ldapAttributes = getContext().getAttributes(base);
				if (ldapAttributes.size() > 0) {
					Map<String, String> attributes = this.getSubjectAttributes(ldapAttributes);
					if (!attributes.isEmpty()) {
						subjects.add(attributes);
					}
				}
			} else {
				log.trace("search string [{}]", query);
				SearchControls controls = new SearchControls();
				controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
				controls.setTimeLimit(5000);
				if (maxResults > 0) {
					controls.setCountLimit(maxResults);
				}
				if (base == null) base = "";
				results = getContext().search(base, query, controls);
				while (results.hasMore()) {
					SearchResult searchResult = results.next();
					Attributes attributes = searchResult.getAttributes();
					Map<String,String> subjectAttributes = this.getSubjectAttributes(attributes);
					if (!subjectAttributes.isEmpty()) {
						subjects.add(subjectAttributes);
					}
				}
			}
			log.trace("Returning [{}] subjects", subjects.size());
			return subjects;
		} catch (NamingException e) {
			log.error("LDAP exception during running query '{}'", query);
			throw new InternalErrorException("LDAP exception during running query: "+query+".", e);
		} finally {
			try {
				if (results != null) { results.close(); }
			} catch (Exception e) {
				log.error("LDAP exception during closing result, while running query '{}'", query);
				throw new InternalErrorException(e);
			}
		}
	}
	@Override
	public void close() throws InternalErrorException {
		if (this.dirContext != null) {
			try {
				this.dirContext.close();
				this.dirContext = null;
			} catch (NamingException e) {
				throw new InternalErrorException(e);
			}
		}
	}
	@Override
	public List<Map<String, String>> getSubjectGroups(Map<String, String> attributes) throws ExtSourceUnsupportedOperationException {
		throw new ExtSourceUnsupportedOperationException();
	}
	@Override
	public List<Map<String, String>> findSubjects(String searchString) throws InternalErrorException {
		return findSubjects(searchString, 0);
	}
	@Override
	public List<Map<String, String>> findSubjects(String searchString, int maxResults) throws InternalErrorException {
		return findSubjectsLogins(searchString, maxResults);
	}
	protected Map<String,String> getAttributes() throws InternalErrorException {
		return perunBl.getExtSourcesManagerBl().getAttributes(this);
	}
}
