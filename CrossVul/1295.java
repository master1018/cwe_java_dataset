package cz.metacentrum.perun.core.impl;
import com.zaxxer.hikari.HikariDataSource;
import cz.metacentrum.perun.core.api.Attribute;
import cz.metacentrum.perun.core.api.AttributeDefinition;
import cz.metacentrum.perun.core.api.BeansUtils;
import cz.metacentrum.perun.core.api.Destination;
import cz.metacentrum.perun.core.api.ExtSource;
import cz.metacentrum.perun.core.api.Member;
import cz.metacentrum.perun.core.api.Pair;
import cz.metacentrum.perun.core.api.PerunSession;
import cz.metacentrum.perun.core.api.User;
import cz.metacentrum.perun.core.api.UserExtSource;
import cz.metacentrum.perun.core.api.exceptions.AttributeNotExistsException;
import cz.metacentrum.perun.core.api.exceptions.ConsistencyErrorException;
import cz.metacentrum.perun.core.api.exceptions.DiacriticNotAllowedException;
import cz.metacentrum.perun.core.api.exceptions.ExtSourceExistsException;
import cz.metacentrum.perun.core.api.exceptions.ExtSourceNotExistsException;
import cz.metacentrum.perun.core.api.exceptions.IllegalArgumentException;
import cz.metacentrum.perun.core.api.exceptions.InternalErrorException;
import cz.metacentrum.perun.core.api.exceptions.MaxSizeExceededException;
import cz.metacentrum.perun.core.api.exceptions.MemberNotExistsException;
import cz.metacentrum.perun.core.api.exceptions.MinSizeExceededException;
import cz.metacentrum.perun.core.api.exceptions.NumberNotInRangeException;
import cz.metacentrum.perun.core.api.exceptions.NumbersNotAllowedException;
import cz.metacentrum.perun.core.api.exceptions.ParseUserNameException;
import cz.metacentrum.perun.core.api.exceptions.ParserException;
import cz.metacentrum.perun.core.api.exceptions.PrivilegeException;
import cz.metacentrum.perun.core.api.exceptions.SpaceNotAllowedException;
import cz.metacentrum.perun.core.api.exceptions.SpecialCharsNotAllowedException;
import cz.metacentrum.perun.core.api.exceptions.UserNotExistsException;
import cz.metacentrum.perun.core.api.exceptions.WrongAttributeAssignmentException;
import cz.metacentrum.perun.core.api.exceptions.WrongPatternException;
import cz.metacentrum.perun.core.bl.PerunBl;
import cz.metacentrum.perun.core.blImpl.ModulesUtilsBlImpl;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.text.StringCharacterIterator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Utils {
	private final static Logger log = LoggerFactory.getLogger(Utils.class);
	public final static String configurationsLocations = "/etc/perun/";
	public static final String TITLE_BEFORE = "titleBefore";
	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String TITLE_AFTER = "titleAfter";
	private static Properties properties;
	public static final Pattern emailPattern = Pattern.compile("^[-_A-Za-z0-9+']+(\\.[-_A-Za-z0-9+']+)*@[-A-Za-z0-9]+(\\.[-A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	private static final Pattern titleBeforePattern = Pattern.compile("^(([\\p{L}]+[.])|(et))$");
	private static final Pattern firstNamePattern = Pattern.compile("^[\\p{L}-']+$");
	private static final Pattern lastNamePattern = Pattern.compile("^(([\\p{L}-']+)|([\\p{L}][.]))$");
	private static final String userPhoneAttribute = "urn:perun:user:attribute-def:def:phone";
	private static final String memberPhoneAttribute = "urn:perun:member:attribute-def:def:phone";
	public static String normalizeString(String str) {
		log.trace("Entering normalizeString: str='{}'", str);
		return str.replace(':', '-').replace(' ', '_');
	}
	public static <T> boolean hasDuplicate(List<T> all) {
		Set<T> set = new HashSet<>(all.size());
		for (T each: all) if (!set.add(each)) return true;
		return false;
	}
	public static String join(Iterable<?> collection, String separator) {
		Iterator<?> oIter;
		if (collection == null || (!(oIter = collection.iterator()).hasNext()))
			return "";
		StringBuilder oBuilder = new StringBuilder(String.valueOf(oIter.next()));
		while (oIter.hasNext())
			oBuilder.append(separator).append(oIter.next());
		return oBuilder.toString();
	}
	public static List<UserExtSource> extractAdditionalUserExtSources(PerunSession sess, Map<String, String> subjectFromExtSource) throws InternalErrorException {
		List<UserExtSource> additionalUserExtSources = new ArrayList<>();
		for (String attrName : subjectFromExtSource.keySet()) {
			if(attrName != null &&
				subjectFromExtSource.get(attrName) != null &&
				attrName.startsWith(ExtSourcesManagerImpl.USEREXTSOURCEMAPPING)) {
				String login = subjectFromExtSource.get("login");
				String[] userExtSourceRaw =  subjectFromExtSource.get(attrName).split("\\|"); 
				log.debug("Processing additionalUserExtSource {}",  subjectFromExtSource.get(attrName));
				if(userExtSourceRaw.length < 3) {
					throw new InternalErrorException("There is a missing mandatory part of additional user extSource value when processing it - '" + attrName + "'");
				}
				String additionalExtSourceName = userExtSourceRaw[0];
				String additionalExtSourceType = userExtSourceRaw[1];
				String additionalExtLogin = userExtSourceRaw[2];
				int additionalExtLoa = 0;
				if (userExtSourceRaw.length>3 && userExtSourceRaw[3] != null) {
					try {
						additionalExtLoa = Integer.parseInt(userExtSourceRaw[3]);
					} catch (NumberFormatException e) {
						throw new ParserException("Subject with login [" + login + "] has wrong LoA '" + userExtSourceRaw[3] + "'.", e, "LoA");
					}
				}
				ExtSource additionalExtSource;
				if (additionalExtSourceName == null || additionalExtSourceName.isEmpty() ||
					additionalExtSourceType == null || additionalExtSourceType.isEmpty() ||
					additionalExtLogin == null || additionalExtLogin.isEmpty()) {
					log.error("User with login {} has invalid additional userExtSource defined {}.", login, userExtSourceRaw);
				} else {
					try {
						additionalExtSource = ((PerunBl) sess.getPerun()).getExtSourcesManagerBl().getExtSourceByName(sess, additionalExtSourceName);
					} catch (ExtSourceNotExistsException e) {
						try {
							additionalExtSource = new ExtSource(additionalExtSourceName, additionalExtSourceType);
							additionalExtSource = ((PerunBl) sess.getPerun()).getExtSourcesManagerBl().createExtSource(sess, additionalExtSource, null);
						} catch (ExtSourceExistsException e1) {
							throw new ConsistencyErrorException("Creating existing extSource: " + additionalExtSourceName);
						}
					}
					additionalUserExtSources.add(new UserExtSource(additionalExtSource, additionalExtLoa, additionalExtLogin));
				}
			}
		}
		return additionalUserExtSources;
	}
	public static String join(Object[] objs, String separator) {
		log.trace("Entering join: objs='{}', separator='{}'", objs, separator);
		return join(Arrays.asList(objs),separator);
	}
	public static final RowMapper<Integer> ID_MAPPER = (resultSet, i) -> resultSet.getInt("id");
	public static final RowMapper<String> STRING_MAPPER = (resultSet, i) -> resultSet.getString("value");
	public static void checkPerunSession(PerunSession sess) throws InternalErrorException {
		notNull(sess, "sess");
	}
	public static <T> Map<T, Set<T>> createDeepCopyOfMapWithSets(Map<T, Set<T>> original) {
		Map<T, Set<T>> copy = new HashMap<>();
		for (T key : original.keySet()) {
			Set<T> setCopy = original.get(key) == null ? null : new HashSet<>(original.get(key));
			copy.put(key, setCopy);
		}
		return copy;
	}
	public static void notNull(Object e, String name) throws InternalErrorException {
		if(e == null){
			throw new InternalErrorException(new NullPointerException("'" + name + "' is null"));
		}
	}
	public static void checkMinLength(String propertyName, String actualValue, int minLength) throws MinSizeExceededException {
		if (actualValue == null) {
			throw new MinSizeExceededException("The property '" + propertyName + "' does not have a minimal length equal to '" + minLength + "' because it is null.");
		}
		if (actualValue.length() < minLength) {
			throw new MinSizeExceededException("Length of '" + propertyName + "' is too short! MinLength=" + minLength + ", ActualLength=" + actualValue.length());
		}
	}
	public static void checkMaxLength(String propertyName, String actualValue, int maxLength) throws MaxSizeExceededException {
		if (actualValue == null) {
			return;
		}
		if (actualValue.length() > maxLength) {
			throw new MaxSizeExceededException("Length of '" + propertyName + "' is too long! MaxLength=" + maxLength + ", ActualLength=" + actualValue.length());
		}
	}
	public static void checkWithoutDiacritic(String name) throws DiacriticNotAllowedException{
		if(!Normalizer.isNormalized(name, Form.NFKD))throw new DiacriticNotAllowedException("Name of the entity is not in the normalized form NFKD (diacritic not allowed)!");
	}
	public static void checkWithoutSpecialChars(String name) throws SpecialCharsNotAllowedException{
		if(!name.matches("^[0-9 \\p{L}]*$")) throw new SpecialCharsNotAllowedException("The special chars in the name of entity are not allowed!");
	}
	public static void checkWithoutSpecialChars(String name, String allowedSpecialChars) throws SpecialCharsNotAllowedException{
		if(!name.matches("^([0-9 \\p{L}" + allowedSpecialChars + "])*$")) throw new SpecialCharsNotAllowedException("The special chars (except " + allowedSpecialChars + ") in the name of entity are not allowed!");
	}
	public static void checkWithoutNumbers(String name) throws NumbersNotAllowedException{
		if(!name.matches("^([^0-9])*$")) throw new NumbersNotAllowedException("The numbers in the name of entity are not allowed!");
	}
	public static void checkWithoutSpaces(String name)throws SpaceNotAllowedException{
		if(name.contains(" ")) throw new SpaceNotAllowedException("The spaces in the name of entity are not allowed!");
	}
	public static void checkRangeOfNumbers(int number, int lowestValue, int highestValue) throws NumberNotInRangeException {
		if(number<lowestValue || number>highestValue) throw new NumberNotInRangeException("Number is not in range, Lowest="+lowestValue+" < Number="+number+" < Highest="+highestValue);
	}
	public static int getNewId(JdbcTemplate jdbc, String sequenceName) throws InternalErrorException {
		String dbType;
		String url = "";
		String query;
		try {
			DataSource ds = jdbc.getDataSource();
			if (ds instanceof HikariDataSource) {
				url = ((HikariDataSource) ds).getJdbcUrl();
			}
		} catch (Exception e) {
			log.error("cannot get JDBC url", e);
		}
		if (url.contains("hsqldb")) {
			dbType = "hsqldb";
		} else if (url.contains("oracle")) {
			dbType = "oracle";
		} else if (url.contains("postgresql")) {
			dbType = "postgresql";
		} else {
			dbType = BeansUtils.getCoreConfig().getDbType();
		}
		switch (dbType) {
			case "oracle":
				query = "select " + sequenceName + ".nextval from dual";
				break;
			case "postgresql":
				query = "select nextval('" + sequenceName + "')";
				break;
			case "hsqldb":
				query = "call next value for " + sequenceName + ";";
				break;
			default:
				throw new InternalErrorException("Unsupported DB type");
		}
		try {
			Integer i = jdbc.queryForObject(query, Integer.class);
			if (i == null) {
				throw new InternalErrorException("New ID should not be null.");
			}
			return i;
		} catch (RuntimeException e) {
			throw new InternalErrorException(e);
		}
	}
	public static long startTimer() {
		return System.currentTimeMillis();
	}
	public static long getRunningTime(long startTime) {
		return System.currentTimeMillis()-startTime;
	}
	public static List<Class<?>> getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class<?>> classes = new ArrayList<>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes;
	}
	private static String limit(String s,int limit) {
		if(s==null) return null;
		return s.length() > limit ? s.substring(0, limit) : s;
	}
	public static User createUserFromNameMap(Map<String, String> name) throws InternalErrorException {
		User user = new User();
		if (name.get(FIRST_NAME) == null || name.get(LAST_NAME) == null || name.get(FIRST_NAME).isEmpty() || name.get(LAST_NAME).isEmpty()) {
			throw new InternalErrorException("First name/last name is either empty or null when creating user");
		}
		user.setTitleBefore(limit(name.get(TITLE_BEFORE),40));
		user.setFirstName(limit(name.get(FIRST_NAME),64));
		user.setLastName(limit(name.get(LAST_NAME),64));
		user.setTitleAfter(limit(name.get(TITLE_AFTER),40));
		return user;
	}
	public static User parseUserFromCommonName(String rawName, boolean fullNameRequired) throws ParseUserNameException {
		Map<String, String> m = parseCommonName(rawName, fullNameRequired);
		return createUserFromNameMap(m);
	}
	public static Map<String, String> parseCommonName(String rawName) {
		try {
			return Utils.parseCommonName(rawName, false);
		} catch (ParseUserNameException ex) {
			throw new InternalErrorException("Unexpected behavior while parsing user name without full name requirement.");
		}
	}
	public static Map<String, String> parseCommonName(String rawName, boolean fullNameRequired) throws ParseUserNameException {
		Map<String, String> parsedName = new HashMap<>();
		String titleBefore = "";
		String firstName = "";
		String lastName = "";
		String titleAfter = "";
		if (rawName != null && !rawName.isEmpty()) {
			rawName = rawName.replaceAll("[,_]", " ");
			rawName = rawName.replaceAll("\\s+", " ").trim();
			List<String> nameParts = new ArrayList<>(Arrays.asList(rawName.split(" ")));
			if(nameParts.size() == 1) {
				lastName = nameParts.get(0);
			} else {
				titleBefore = parsePartOfName(nameParts, new StringJoiner(" "), titleBeforePattern);
				if (!nameParts.isEmpty()) firstName = parsePartOfName(nameParts, new StringJoiner(" "), firstNamePattern);
				if (!nameParts.isEmpty()) lastName = parsePartOfName(nameParts, new StringJoiner(" "), lastNamePattern);
				if (!nameParts.isEmpty()) {
					StringJoiner titleAfterBuilder = new StringJoiner(" ");
					for (String namePart : nameParts) {
						titleAfterBuilder.add(namePart);
					}
					titleAfter = titleAfterBuilder.toString();
				}
			}
		}
		if (titleBefore.isEmpty()) titleBefore = null;
		parsedName.put(TITLE_BEFORE, titleBefore);
		if (firstName.isEmpty()) firstName = null;
		parsedName.put(FIRST_NAME, firstName);
		if (lastName.isEmpty()) lastName = null;
		parsedName.put(LAST_NAME, lastName);
		if (titleAfter.isEmpty()) titleAfter = null;
		parsedName.put(TITLE_AFTER, titleAfter);
		if(fullNameRequired) {
			if (parsedName.get(FIRST_NAME) == null)
				throw new ParseUserNameException("Unable to parse first name from text.", rawName);
			if (parsedName.get(LAST_NAME) == null)
				throw new ParseUserNameException("Unable to parse last name from text.", rawName);
		}
		return parsedName;
	}
	private static String parsePartOfName(List<String> nameParts, StringJoiner result, Pattern pattern) {
		Matcher matcher = pattern.matcher(nameParts.get(0));
		if (!matcher.matches()) return result.toString();
		result.add(nameParts.get(0));
		nameParts.remove(0);
		if (nameParts.isEmpty() || pattern.equals(firstNamePattern)) return result.toString();
		return parsePartOfName(nameParts, result, pattern);
	}
	private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					assert !file.getName().contains(".");
					classes.addAll(findClasses(file, packageName + "." + file.getName()));
				} else if (file.getName().endsWith(".class")) {
					classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
				}
			}
		}
		return classes;
	}
	public static boolean isEscaped(String text, int position) {
		boolean escaped = false;
		while (text.charAt(position) == '\\') {
			escaped = !escaped;
			position--;
			if (position < 0) {
				return escaped;
			}
		}
		return escaped;
	}
	public static String serializeMapToString(Map<String, String> map) {
		if(map == null) return "\\0";
		Map<String, String> attrNew = new HashMap<>(map);
		Set<String> keys = new HashSet<>(attrNew.keySet());
		for(String s: keys) {
			attrNew.put("<" + BeansUtils.createEscaping(s) + ">", "<" + BeansUtils.createEscaping(attrNew.get(s)) + ">");
			attrNew.remove(s);
		}
		return attrNew.toString();
	}
	public static Attribute copyAttributeToViAttributeWithoutValue(Attribute copyFrom, Attribute copyTo) {
		copyTo.setValueCreatedAt(copyFrom.getValueCreatedAt());
		copyTo.setValueCreatedBy(copyFrom.getValueCreatedBy());
		copyTo.setValueModifiedAt(copyFrom.getValueModifiedAt());
		copyTo.setValueModifiedBy(copyFrom.getValueModifiedBy());
		return copyTo;
	}
	public static Attribute copyAttributeToVirtualAttributeWithValue(Attribute copyFrom, Attribute copyTo) {
		copyTo.setValue(copyFrom.getValue());
		copyTo.setValueCreatedAt(copyFrom.getValueCreatedAt());
		copyTo.setValueCreatedBy(copyFrom.getValueCreatedBy());
		copyTo.setValueModifiedAt(copyFrom.getValueModifiedAt());
		copyTo.setValueModifiedBy(copyFrom.getValueModifiedBy());
		return copyTo;
	}
	public static List<String> generateStringsByPattern(String pattern) throws WrongPatternException {
		List<String> result = new ArrayList<>();
		List<String> values = new ArrayList<>(Arrays.asList(pattern.split("\\[[^]]*]")));
		List<String> generators = new ArrayList<>();
		Pattern generatorPattern = Pattern.compile("\\[([^]]*)]");
		Matcher m = generatorPattern.matcher(pattern);
		while (m.find()) {
			generators.add(m.group(1));
		}
		for (String value: values) {
			if (value.contains("]") || (value.contains("["))) {
				throw new WrongPatternException("The pattern \"" + pattern + "\" has a wrong syntax. Too much closing brackets.");
			}
		}
		for (String generator: generators) {
			if (generator.contains("]") || (generator.contains("["))) {
				throw new WrongPatternException("The pattern \"" + pattern + "\" has a wrong syntax. Too much opening brackets.");
			}
		}
		List<List<String>> listOfGenerated = new ArrayList<>();
		Pattern rangePattern = Pattern.compile("^(\\d+)-(\\d+)$");
		for (String range: generators) {
			m = rangePattern.matcher(range);
			if (m.find()) {
				String start = m.group(1);
				String end = m.group(2);
				int startNumber;
				int endNumber;
				try {
					startNumber = Integer.parseInt(start);
					endNumber = Integer.parseInt(end);
				} catch (NumberFormatException ex) {
					throw new WrongPatternException("The pattern \"" + pattern + "\" has a wrong syntax. Wrong format of the range.");
				}
				if (startNumber > endNumber) {
					throw new WrongPatternException("The pattern \"" + pattern + "\" has a wrong syntax. Start number has to be lower than end number.");
				}
				int zerosInStart = 0;
				int counter = 0;
				while ( (start.charAt(counter) == '0') && (counter < start.length()-1) ) {
					zerosInStart++;
					counter++;
				}
				String zeros = start.substring(0, zerosInStart);
				int oldNumberOfDigits = String.valueOf(startNumber).length();
				List<String> generated = new ArrayList<>();
				while (endNumber >= startNumber) {
					if (String.valueOf(startNumber).length() == oldNumberOfDigits +1) {
						if (!zeros.isEmpty()) zeros = zeros.substring(1);
					}
					generated.add(zeros + startNumber);
					oldNumberOfDigits = String.valueOf(startNumber).length();
					startNumber++;
				}
				listOfGenerated.add(generated);
			} else {
				throw new WrongPatternException("The pattern \"" + pattern + "\" has a wrong syntax. The format numer-number not found.");
			}
		}
		List<List<String>> listOfGeneratorsAndValues = new ArrayList<>();
		int index = 0;
		for (List<String> list : listOfGenerated) {
			if (index < values.size()) {
				List<String> listWithValue = new ArrayList<>();
				listWithValue.add(values.get(index));
				listOfGeneratorsAndValues.add(listWithValue);
				index++;
			}
			listOfGeneratorsAndValues.add(list);
		}
		for (int i = index; i < values.size(); i++) {
			List<String> listWithValue = new ArrayList<>();
			listWithValue.add(values.get(i));
			listOfGeneratorsAndValues.add(listWithValue);
		}
		return getCombinationsOfLists(listOfGeneratorsAndValues);
	}
	private static List<String> getCombinationsOfLists(List<List<String>> lists) {
		if (lists.isEmpty()) {
			return new ArrayList<>();
		}
		if (lists.size() == 1) {
			return lists.get(0);
		}
		List<String> result = new ArrayList<>();
		List<String> list = lists.remove(0);
		List<String> posibilities = getCombinationsOfLists(lists);
		for (String item: list) {
			if (posibilities.isEmpty()) {
				result.add(item);
			} else {
				for (String itemToConcat : posibilities) {
					result.add(item + itemToConcat);
				}
			}
		}
		return result;
	}
	public static String getMessageAuthenticationCode(String input) {
		if (input == null)
			throw new NullPointerException("input must not be null");
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(BeansUtils.getCoreConfig().getMailchangeSecretKey().getBytes(StandardCharsets.UTF_8),"HmacSHA256"));
			byte[] macbytes = mac.doFinal(input.getBytes(StandardCharsets.UTF_8));
			return new BigInteger(macbytes).toString(Character.MAX_RADIX);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static void sendValidationEmail(User user, String url, String email, int changeId, String subject, String content) throws InternalErrorException {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("localhost");
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setFrom(BeansUtils.getCoreConfig().getMailchangeBackupFrom());
		String instanceName = BeansUtils.getCoreConfig().getInstanceName();
		if (subject == null ||subject.isEmpty()) {
			message.setSubject("["+instanceName+"] New email address verification");
		} else {
			subject = subject.replace("{instanceName}", instanceName);
			message.setSubject(subject);
		}
		String i = Integer.toString(changeId, Character.MAX_RADIX);
		String m = Utils.getMessageAuthenticationCode(i);
		try {
			URL urlObject = new URL(url);
			String path = "/gui/";
			if (urlObject.getPath().contains("/krb/")) {
				path = "/krb/gui/";
			} else if (urlObject.getPath().contains("/fed/")) {
				path = "/fed/gui/";
			} else if (urlObject.getPath().contains("/cert/")) {
				path = "/cert/gui/";
			}
			StringBuilder link = new StringBuilder();
			link.append(urlObject.getProtocol());
			link.append(":
			link.append(urlObject.getHost());
			link.append(path);
			link.append("?i=");
			link.append(URLEncoder.encode(i, "UTF-8"));
			link.append("&m=");
			link.append(URLEncoder.encode(m, "UTF-8"));
			link.append("&u=" + user.getId());
			String text = "Dear "+user.getDisplayName()+",\n\nWe've received request to change your preferred email address to: "+email+"."+
					"\n\nTo confirm this change please use link below:\n\n"+link+"\n\n" +
					"Message is automatically generated." +
					"\n----------------------------------------------------------------" +
					"\nPerun - Identity & Access Management System";
			if (content == null || content.isEmpty()) {
				message.setText(text);
			} else {
				content = content.replace("{link}",link);
				message.setText(content);
			}
			mailSender.send(message);
		} catch (UnsupportedEncodingException ex) {
			throw new InternalErrorException("Unable to encode validation URL for mail change.", ex);
		} catch (MalformedURLException ex) {
			throw new InternalErrorException("Not valid URL of running Perun instance.", ex);
		}
	}
	public static void sendPasswordResetEmail(User user, String email, String namespace, String url, int id, String messageTemplate, String subject) throws InternalErrorException {
		JavaMailSender mailSender = BeansUtils.getDefaultMailSender();
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setFrom(BeansUtils.getCoreConfig().getMailchangeBackupFrom());
		String instanceName = BeansUtils.getCoreConfig().getInstanceName();
		if (subject == null) {
			message.setSubject("[" + instanceName + "] Password reset in namespace: " + namespace);
		} else {
			subject = subject.replace("{namespace}", namespace);
			subject = subject.replace("{instanceName}", instanceName);
			message.setSubject(subject);
		}
		String i = cipherInput(String.valueOf(user.getId()), false);
		String m = cipherInput(String.valueOf(id), false);
		try {
			URL urlObject = new URL(url);
			StringBuilder link = new StringBuilder();
			link.append(urlObject.getProtocol());
			link.append(":
			link.append(urlObject.getHost());
			link.append("/non/pwd-reset/");
			link.append("?i=");
			link.append(URLEncoder.encode(i, "UTF-8"));
			link.append("&m=");
			link.append(URLEncoder.encode(m, "UTF-8"));
			link.append("&login-namespace=");
			link.append(URLEncoder.encode(namespace, "UTF-8"));
			String validity = Integer.toString(BeansUtils.getCoreConfig().getPwdresetValidationWindow());
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime localDateTime = LocalDateTime.now().plusHours(Integer.parseInt(validity));
			String validityFormatted = dtf.format(localDateTime);
			String textEn = "Dear " + user.getDisplayName() + ",\n\nWe've received request to reset your password in namespace \"" + namespace + "\"." +
					"\n\nPlease visit the link below, where you can set new password:\n\n" + link + "\n\n" +
					"Link is valid till " + validityFormatted + "\n\n" +
					"Message is automatically generated." +
					"\n----------------------------------------------------------------" +
					"\nPerun - Identity & Access Management System";
			if (messageTemplate == null) {
				message.setText(textEn);
			} else {
				if (messageTemplate.contains("{link-")) {
					Pattern pattern = Pattern.compile("\\{link-[^}]+}");
					Matcher matcher = pattern.matcher(messageTemplate);
					while (matcher.find()) {
						String toSubstitute = matcher.group(0);
						String langLink = link.toString();
						Pattern namespacePattern = Pattern.compile("-(.*?)}");
						Matcher m2 = namespacePattern.matcher(toSubstitute);
						if (m2.find()) {
							String lang = m2.group(1);
							langLink = langLink + "&locale=" + lang;
						}
						messageTemplate = messageTemplate.replace(toSubstitute, langLink);
					}
				} else {
					messageTemplate = messageTemplate.replace("{link}", link);
				}
				messageTemplate = messageTemplate.replace("{displayName}", user.getDisplayName());
				messageTemplate = messageTemplate.replace("{namespace}", namespace);
				messageTemplate = messageTemplate.replace("{validity}", validityFormatted);
				message.setText(messageTemplate);
			}
			mailSender.send(message);
		} catch (MailException ex) {
			throw new InternalErrorException("Unable to send mail for password reset.", ex);
		} catch (UnsupportedEncodingException ex) {
			throw new InternalErrorException("Unable to encode URL for password reset.", ex);
		} catch (MalformedURLException ex) {
			throw new InternalErrorException("Not valid URL of running Perun instance.", ex);
		}
	}
	public static void sendPasswordResetConfirmationEmail(User user, String email, String namespace, String login, String subject, String content) {
		JavaMailSender mailSender = BeansUtils.getDefaultMailSender();
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setFrom(BeansUtils.getCoreConfig().getMailchangeBackupFrom());
		String instanceName = BeansUtils.getCoreConfig().getInstanceName();
		if (subject == null || subject.isEmpty()) {
			message.setSubject("["+instanceName+"] Password reset in namespace: "+namespace);
		} else {
			subject = subject.replace("{namespace}", namespace);
			subject = subject.replace("{instanceName}", instanceName);
			message.setSubject(subject);
		}
		String text = "Dear "+user.getDisplayName()+",\n\nyour password in namespace \""+namespace+"\" was successfully reset."+
				"\n\nThis message is automatically sent to all your email addresses registered in "+instanceName+" in order to prevent malicious password reset without your knowledge.\n\n" +
				"If you didn't request / perform password reset, please notify your administrators and support at "+BeansUtils.getCoreConfig().getMailchangeBackupFrom()+" to resolve this security issue.\n\n" +
				"Message is automatically generated." +
				"\n----------------------------------------------------------------" +
				"\nPerun - Identity & Access Management System";
		if (content == null || content.isEmpty()) {
			message.setText(text);
		} else {
			content = content.replace("{displayName}", user.getDisplayName());
			content = content.replace("{namespace}", namespace);
			content = content.replace("{login}", login);
			content = content.replace("{instanceName}", instanceName);
			message.setText(content);
		}
		mailSender.send(message);
	}
	public static String cipherInput(String plainText, boolean decrypt) throws InternalErrorException {
		try {
			String encryptionKey = BeansUtils.getCoreConfig().getPwdresetSecretKey();
			String initVector = BeansUtils.getCoreConfig().getPwdresetInitVector();
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			SecretKeySpec k = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
			c.init((decrypt) ? Cipher.DECRYPT_MODE : Cipher.ENCRYPT_MODE, k, new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8)));
			if (decrypt) {
				byte[] bytes = Base64.decodeBase64(plainText.getBytes(StandardCharsets.UTF_8));
				return new String(c.doFinal(bytes), StandardCharsets.UTF_8);
			} else {
				byte[] bytes = Base64.encodeBase64(c.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
				return new String(bytes, StandardCharsets.UTF_8);
			}
		} catch (Exception ex) {
			throw new InternalErrorException("Error when encrypting message", ex);
		}
	}
	public static void checkDestinationType(Destination destination) throws InternalErrorException, WrongPatternException  {
		if (destination == null) {
			throw new InternalErrorException("Destination is null.");
		}
		String destinationType = destination.getType();
		if ((!Objects.equals(destinationType, Destination.DESTINATIONHOSTTYPE)
				&& (!Objects.equals(destinationType, Destination.DESTINATIONEMAILTYPE))
				&& (!Objects.equals(destinationType, Destination.DESTINATIONSEMAILTYPE))
				&& (!Objects.equals(destinationType, Destination.DESTINATIONURLTYPE))
				&& (!Objects.equals(destinationType, Destination.DESTINATIONUSERHOSTTYPE))
				&& (!Objects.equals(destinationType, Destination.DESTINATIONUSERHOSTPORTTYPE))
				&& (!Objects.equals(destinationType, Destination.DESTINATIONSERVICESPECIFICTYPE))
				&& (!Objects.equals(destinationType, Destination.DESTINATIONWINDOWS))
				&& (!Objects.equals(destinationType, Destination.DESTINATIONWINDOWSPROXY)))) {
			throw new WrongPatternException("Destination type " + destinationType + " is not supported.");
		}
	}
	public static void sendSMS(PerunSession sess, User user, String message) throws InternalErrorException, PrivilegeException, UserNotExistsException {
		if (user == null) {
			throw new cz.metacentrum.perun.core.api.exceptions.IllegalArgumentException("user is null");
		}
		if (message == null) {
			throw new cz.metacentrum.perun.core.api.exceptions.IllegalArgumentException("message is null");
		}
		String telNumber;
		try {
			telNumber = (String) sess.getPerun().getAttributesManager().getAttribute(sess, user, userPhoneAttribute).getValue();
		} catch (AttributeNotExistsException ex ) {
			log.error("Sendig SMS with text \"{}\" to user {} failed: cannot get tel. number.", message, user );
			throw new InternalErrorException("The attribute " + userPhoneAttribute + " has not been found.", ex);
		} catch (WrongAttributeAssignmentException ex) {
			log.error("Sendig SMS with text \"{}\" to user {} failed: cannot get tel. number.", message, user );
			throw new InternalErrorException("The attribute " + userPhoneAttribute + " has not been found in user attributes.", ex);
		}
		sendSMS(telNumber, message);
	}
	public static void sendSMS(PerunSession sess, Member member, String message) throws InternalErrorException, PrivilegeException, MemberNotExistsException {
		String telNumber;
		try {
			telNumber = (String) sess.getPerun().getAttributesManager().getAttribute(sess, member, memberPhoneAttribute).getValue();
		} catch (AttributeNotExistsException ex) {
			log.error("Sendig SMS with text \"{}\" to member {} failed: cannot get tel. number.", message, member );
			throw new InternalErrorException("The attribute " + memberPhoneAttribute + " has not been found.", ex);
		} catch (WrongAttributeAssignmentException ex) {
			log.error("Sendig SMS with text \"{}\" to member {} failed: cannot get tel. number.", message, member );
			throw new InternalErrorException("The attribute " + memberPhoneAttribute + " has not been found in user attributes.", ex);
		}
		sendSMS(telNumber, message);
	}
	public static void sendSMS(String telNumber, String message) throws InternalErrorException {
		log.debug("Sending SMS with text \"{}\" to tel. number {}.", message, telNumber);
		try {
			List<String> processProperties = new ArrayList<>();
			processProperties.add(BeansUtils.getCoreConfig().getSmsProgram());
			processProperties.add("-p");
			processProperties.add(telNumber);
			processProperties.add("-m");
			processProperties.add(message);
			ProcessBuilder pb = new ProcessBuilder(processProperties);
			Process process;
			process = pb.start();
			int exitValue;
			try {
				exitValue = process.waitFor();
			} catch (InterruptedException ex) {
				String errMsg = "The external process for sending sms was interrupted.";
				log.error("Sending SMS with text \"{}\" to tel. number {} failed.", message, telNumber);
				throw new InternalErrorException(errMsg, ex);
			}
			if (exitValue == 0) {
				log.debug("SMS with text \"{}\" to tel. number {} successfully sent.", message, telNumber);
			} else if ((exitValue == 1) || (exitValue == 2)) {
				String errMsg = getStringFromInputStream(process.getErrorStream());
				log.error("Sending SMS with text \"{}\" to tel. number {} failed.", message, telNumber);
				throw new cz.metacentrum.perun.core.api.exceptions.IllegalArgumentException(errMsg);
			} else if (exitValue > 2) {
				String errMsg = getStringFromInputStream(process.getErrorStream());
				log.error("Sending SMS with text \"{}\" to tel. number {} failed.", message, telNumber);
				throw new InternalErrorException(errMsg);
			}
		} catch (IOException ex) {
			log.warn("Sending SMS with text \"{}\" to tel. number {} failed.", message, telNumber);
			throw new InternalErrorException("Cannot access the sms external application.", ex);
		}
	}
	public static String bigDecimalBytesToReadableStringWithMetric(BigDecimal quota) throws InternalErrorException {
		if(quota == null) throw new InternalErrorException("Quota in BigDecimal can't be null if we want to convert it to number with metric.");
		String stringWithMetric;
		if(!quota.divide(BigDecimal.valueOf(ModulesUtilsBlImpl.E)).stripTrailingZeros().toPlainString().contains(".")) {
			stringWithMetric = quota.divide(BigDecimal.valueOf(ModulesUtilsBlImpl.E)).stripTrailingZeros().toPlainString() + "E";
		} else if(!quota.divide(BigDecimal.valueOf(ModulesUtilsBlImpl.P)).stripTrailingZeros().toPlainString().contains(".")) {
			stringWithMetric = quota.divide(BigDecimal.valueOf(ModulesUtilsBlImpl.P)).stripTrailingZeros().toPlainString() + "P";
		} else if(!quota.divide(BigDecimal.valueOf(ModulesUtilsBlImpl.T)).stripTrailingZeros().toPlainString().contains(".")) {
			stringWithMetric = quota.divide(BigDecimal.valueOf(ModulesUtilsBlImpl.T)).stripTrailingZeros().toPlainString() + "T";
		} else if(!quota.divide(BigDecimal.valueOf(ModulesUtilsBlImpl.G)).stripTrailingZeros().toPlainString().contains(".")) {
			stringWithMetric = quota.divide(BigDecimal.valueOf(ModulesUtilsBlImpl.G)).stripTrailingZeros().toPlainString() + "G";
		} else if(!quota.divide(BigDecimal.valueOf(ModulesUtilsBlImpl.M)).stripTrailingZeros().toPlainString().contains(".")) {
			stringWithMetric = quota.divide(BigDecimal.valueOf(ModulesUtilsBlImpl.M)).stripTrailingZeros().toPlainString() + "M";
		} else {
			stringWithMetric = quota.toBigInteger().toString() + "K";
		}
		return stringWithMetric;
	}
	private static String getStringFromInputStream(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder out = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			out.append(line);
		}
		return out.toString();
	}
	public synchronized static String utftoasci(String s){
		final StringBuilder sb = new StringBuilder( s.length() * 2 );
		final StringCharacterIterator iterator = new StringCharacterIterator( s );
		char ch = iterator.current();
		while( ch != StringCharacterIterator.DONE ){
			if(Character.getNumericValue(ch)>=0){
				sb.append( ch );
			}else{
				boolean f=false;
				if(Character.toString(ch).equals("Ê")){sb.append("E");f=true;}
				if(Character.toString(ch).equals("È")){sb.append("E");f=true;}
				if(Character.toString(ch).equals("ë")){sb.append("e");f=true;}
				if(Character.toString(ch).equals("é")){sb.append("e");f=true;}
				if(Character.toString(ch).equals("è")){sb.append("e");f=true;}
				if(Character.toString(ch).equals("Â")){sb.append("A");f=true;}
				if(Character.toString(ch).equals("ä")){sb.append("a");f=true;}
				if(Character.toString(ch).equals("ß")){sb.append("ss");f=true;}
				if(Character.toString(ch).equals("Ç")){sb.append("C");f=true;}
				if(Character.toString(ch).equals("Ö")){sb.append("O");f=true;}
				if(Character.toString(ch).equals("º")){sb.append("");f=true;}
				if(Character.toString(ch).equals("ª")){sb.append("");f=true;}
				if(Character.toString(ch).equals("º")){sb.append("");f=true;}
				if(Character.toString(ch).equals("Ñ")){sb.append("N");f=true;}
				if(Character.toString(ch).equals("É")){sb.append("E");f=true;}
				if(Character.toString(ch).equals("Ä")){sb.append("A");f=true;}
				if(Character.toString(ch).equals("Å")){sb.append("A");f=true;}
				if(Character.toString(ch).equals("Ü")){sb.append("U");f=true;}
				if(Character.toString(ch).equals("ö")){sb.append("o");f=true;}
				if(Character.toString(ch).equals("ü")){sb.append("u");f=true;}
				if(Character.toString(ch).equals("á")){sb.append("a");f=true;}
				if(Character.toString(ch).equals("Ó")){sb.append("O");f=true;}
				if(Character.toString(ch).equals("ě")){sb.append("e");f=true;}
				if(Character.toString(ch).equals("Ě")){sb.append("E");f=true;}
				if(Character.toString(ch).equals("š")){sb.append("s");f=true;}
				if(Character.toString(ch).equals("Š")){sb.append("S");f=true;}
				if(Character.toString(ch).equals("č")){sb.append("c");f=true;}
				if(Character.toString(ch).equals("Č")){sb.append("C");f=true;}
				if(Character.toString(ch).equals("ř")){sb.append("r");f=true;}
				if(Character.toString(ch).equals("Ř")){sb.append("R");f=true;}
				if(Character.toString(ch).equals("ž")){sb.append("z");f=true;}
				if(Character.toString(ch).equals("Ž")){sb.append("Z");f=true;}
				if(Character.toString(ch).equals("ý")){sb.append("y");f=true;}
				if(Character.toString(ch).equals("Ý")){sb.append("Y");f=true;}
				if(Character.toString(ch).equals("í")){sb.append("i");f=true;}
				if(Character.toString(ch).equals("Í")){sb.append("I");f=true;}
				if(Character.toString(ch).equals("ó")){sb.append("o");f=true;}
				if(Character.toString(ch).equals("ú")){sb.append("u");f=true;}
				if(Character.toString(ch).equals("Ú")){sb.append("u");f=true;}
				if(Character.toString(ch).equals("ů")){sb.append("u");f=true;}
				if(Character.toString(ch).equals("Ů")){sb.append("U");f=true;}
				if(Character.toString(ch).equals("Ň")){sb.append("N");f=true;}
				if(Character.toString(ch).equals("ň")){sb.append("n");f=true;}
				if(Character.toString(ch).equals("Ť")){sb.append("T");f=true;}
				if(Character.toString(ch).equals("ť")){sb.append("t");f=true;}
				if(Character.toString(ch).equals(" ")){sb.append(" ");f=true;}
				if(!f){
					sb.append("?");
				}
			}
			ch = iterator.next();
		}
		return sb.toString();
	}
	public synchronized static String toASCII(String input, Character replacement) {
		String normalizedOutput = "";
		for ( int i=0; i<input.length(); i++ ) {
			char c = input.charAt(i);
			String normalizedChar = Normalizer.normalize(String.valueOf(c) , Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
			if ( ! normalizedChar.isEmpty() ) {
				normalizedOutput += normalizedChar;
			} else {
				normalizedOutput += replacement;
			}
		}
		return normalizedOutput;
	}
	public static boolean isLargeAttribute(PerunSession sess, AttributeDefinition attribute) {
		return (attribute.getType().equals(LinkedHashMap.class.getName()) ||
				attribute.getType().equals(BeansUtils.largeStringClassName) ||
				attribute.getType().equals(BeansUtils.largeArrayListClassName));
	}
	public static LocalDate extendDateByPeriod(LocalDate localDate, String period) throws InternalErrorException {
		Pattern p = Pattern.compile("\\+([0-9]+)([dmy]?)");
		Matcher m = p.matcher(period);
		if (m.matches()) {
			String countString = m.group(1);
			int amount = Integer.valueOf(countString);
			String dmyString = m.group(2);
			switch (dmyString) {
				case "d":
					return localDate.plusDays(amount);
				case "m":
					return localDate.plusMonths(amount);
				case "y":
					return localDate.plusYears(amount);
				default:
					throw new InternalErrorException("Wrong format of period. Period: " + period);
			}
		} else {
			throw new InternalErrorException("Wrong format of period. Period: " + period);
		}
	}
	public static LocalDate getClosestExpirationFromStaticDate(Matcher matcher) {
		int day = Integer.parseInt(matcher.group(1));
		int month = Integer.parseInt(matcher.group(2));
		LocalDate extensionDate = LocalDate.of(2000, month, day);
		boolean extensionInThisYear = LocalDate.of(2000, LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth()).isBefore(extensionDate);
		int year = LocalDate.now().getYear();
		if (!extensionInThisYear) {
			year++;
		}
		if (day == 29 && month == 2 && !LocalDate.of(year, 1,1).isLeapYear()) {
			extensionDate = LocalDate.of(year, 2, 28);
		} else {
			extensionDate = LocalDate.of(year, month, day);
		}
		return extensionDate;
	}
	public static Pair<Integer, TemporalUnit> prepareGracePeriodDate(Matcher matcher) throws InternalErrorException {
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Wrong format of gracePeriod.");
		}
		String countString = matcher.group(1);
		int amount = Integer.valueOf(countString);
		TemporalUnit field;
		String dmyString = matcher.group(2);
		switch (dmyString) {
			case "d":
				field = ChronoUnit.DAYS;
				break;
			case "m":
				field = ChronoUnit.MONTHS;
				break;
			case "y":
				field = ChronoUnit.YEARS;
				break;
			default:
				throw new InternalErrorException("Wrong format of gracePeriod.");
		}
		return new Pair<>(amount, field);
	}
}
