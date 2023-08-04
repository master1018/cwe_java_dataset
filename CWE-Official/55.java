@Stateless
public class InterestRateBean implements InterestRateRemote {
private Document interestRateXMLDocument = null;
public InterestRateBean() {
try {
ClassLoader loader = this.getClass().getClassLoader();
InputStream in = loader.getResourceAsStream(Constants.INTEREST_RATE_FILE);
DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
DocumentBuilder db = dbf.newDocumentBuilder();
interestRateXMLDocument = db.parse(interestRateFile);
} catch (IOException ex) {...}
}
public BigDecimal getInterestRate(Integer points) {
return getInterestRateFromXML(points);
}
private BigDecimal getInterestRateFromXML(Integer points) {...}
}
