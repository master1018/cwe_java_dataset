@Stateless
public class InterestRateBean implements InterestRateRemote {
    private Document interestRateXMLDocument = null;
    private File interestRateFile = null;

    public InterestRateBean() {
        try {
            interestRateFile = new File(Constants.INTEREST_RATE_FILE);
            if (interestRateFile.exists()) {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                interestRateXMLDocument = db.parse(interestRateFile);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public BigDecimal getInterestRate(Integer points) {
        return getInterestRateFromXML(points);
    }

    private BigDecimal getInterestRateFromXML(Integer points) {
        // Replace with implementation
        return null;
    }
}
