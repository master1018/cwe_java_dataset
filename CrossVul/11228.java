
package ghidra.bitpatterns.info;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import ghidra.util.Msg;
import ghidra.util.xml.XmlUtilities;
public class FileBitPatternInfo {
	static final String XML_ELEMENT_NAME = "FileBitPatternInfo";
	private int numFirstBytes = 0;
	private int numFirstInstructions = 0;
	private int numPreBytes = 0;
	private int numPreInstructions = 0;
	private int numReturnBytes = 0;
	private int numReturnInstructions = 0;
	private String languageID = null;
	private String ghidraURL = null;
	private List<FunctionBitPatternInfo> funcBitPatternInfo;
	public FileBitPatternInfo() {
		funcBitPatternInfo = new ArrayList<FunctionBitPatternInfo>();
	}
	public int getNumFirstBytes() {
		return numFirstBytes;
	}
	public void setNumFirstBytes(int numFirstBytes) {
		this.numFirstBytes = numFirstBytes;
	}
	public int getNumFirstInstructions() {
		return numFirstInstructions;
	}
	public void setNumFirstInstructions(int numFirstInstructions) {
		this.numFirstInstructions = numFirstInstructions;
	}
	public int getNumPreBytes() {
		return numPreBytes;
	}
	public void setNumPreBytes(int numPreBytes) {
		this.numPreBytes = numPreBytes;
	}
	public int getNumPreInstructions() {
		return numPreInstructions;
	}
	public void setNumPreInstructions(int numPreInstructions) {
		this.numPreInstructions = numPreInstructions;
	}
	public List<FunctionBitPatternInfo> getFuncBitPatternInfo() {
		return funcBitPatternInfo;
	}
	public void setFuncBitPatternInfo(List<FunctionBitPatternInfo> funcBitPatternInfo) {
		this.funcBitPatternInfo = funcBitPatternInfo;
	}
	public String getLanguageID() {
		return languageID;
	}
	public void setLanguageID(String id) {
		this.languageID = id;
	}
	public void setGhidraURL(String url) {
		this.ghidraURL = url;
	}
	public String getGhidraURL() {
		return ghidraURL;
	}
	public int getNumReturnBytes() {
		return numReturnBytes;
	}
	public void setNumReturnBytes(int numReturnBytes) {
		this.numReturnBytes = numReturnBytes;
	}
	public int getNumReturnInstructions() {
		return numReturnInstructions;
	}
	public void setNumReturnInstructions(int numReturnInstructions) {
		this.numReturnInstructions = numReturnInstructions;
	}
	public Element toXml() {
		Element result = new Element(XML_ELEMENT_NAME);
		XmlUtilities.setStringAttr(result, "ghidraURL", ghidraURL);
		XmlUtilities.setStringAttr(result, "languageID", languageID);
		XmlUtilities.setIntAttr(result, "numFirstBytes", numFirstBytes);
		XmlUtilities.setIntAttr(result, "numFirstInstructions", numFirstInstructions);
		XmlUtilities.setIntAttr(result, "numPreBytes", numPreBytes);
		XmlUtilities.setIntAttr(result, "numPreInstructions", numPreInstructions);
		XmlUtilities.setIntAttr(result, "numReturnBytes", numReturnBytes);
		XmlUtilities.setIntAttr(result, "numReturnInstructions", numReturnInstructions);
		Element funcBitPatternInfoListEle = new Element("funcBitPatternInfoList");
		for (FunctionBitPatternInfo fbpi : funcBitPatternInfo) {
			funcBitPatternInfoListEle.addContent(fbpi.toXml());
		}
		result.addContent(funcBitPatternInfoListEle);
		return result;
	}
	public static FileBitPatternInfo fromXml(Element e) throws IOException {
		String ghidraURL = e.getAttributeValue("ghidraURL");
		String languageID = e.getAttributeValue("languageID");
		int numFirstBytes =
			XmlUtilities.parseInt(XmlUtilities.requireStringAttr(e, "numFirstBytes"));
		int numFirstInstructions =
			XmlUtilities.parseInt(XmlUtilities.requireStringAttr(e, "numFirstInstructions"));
		int numPreBytes = XmlUtilities.parseInt(XmlUtilities.requireStringAttr(e, "numPreBytes"));
		int numPreInstructions =
			XmlUtilities.parseInt(XmlUtilities.requireStringAttr(e, "numPreInstructions"));
		int numReturnBytes =
			XmlUtilities.parseInt(XmlUtilities.requireStringAttr(e, "numReturnBytes"));
		int numReturnInstructions =
			XmlUtilities.parseInt(XmlUtilities.requireStringAttr(e, "numReturnInstructions"));
		List<FunctionBitPatternInfo> funcBitPatternInfoList = new ArrayList<>();
		Element funcBitPatternInfoListEle = e.getChild("funcBitPatternInfoList");
		if (funcBitPatternInfoListEle != null) {
			for (Element childElement : XmlUtilities.getChildren(funcBitPatternInfoListEle,
				FunctionBitPatternInfo.XML_ELEMENT_NAME)) {
				funcBitPatternInfoList.add(FunctionBitPatternInfo.fromXml(childElement));
			}
		}
		FileBitPatternInfo result = new FileBitPatternInfo();
		result.setFuncBitPatternInfo(funcBitPatternInfoList);
		result.setGhidraURL(ghidraURL);
		result.setLanguageID(languageID);
		result.setNumFirstBytes(numFirstBytes);
		result.setNumFirstInstructions(numFirstInstructions);
		result.setNumPreBytes(numPreBytes);
		result.setNumPreInstructions(numPreInstructions);
		result.setNumReturnBytes(numReturnBytes);
		result.setNumReturnInstructions(numReturnInstructions);
		return result;
	}
	public void toXmlFile(File destFile) throws IOException {
		Element rootEle = toXml();
		Document doc = new Document(rootEle);
		XmlUtilities.writePrettyDocToFile(doc, destFile);
	}
	public static FileBitPatternInfo fromXmlFile(File inputFile) throws IOException {
		SAXBuilder sax = XmlUtilities.createSecureSAXBuilder(false, false);
		try (InputStream fis = new FileInputStream(inputFile)) {
			Document doc = sax.build(fis);
			Element rootElem = doc.getRootElement();
			return fromXml(rootElem);
		}
		catch (JDOMException | IOException e) {
			Msg.error(FileBitPatternInfo.class, "Bad file bit pattern file " + inputFile, e);
			throw new IOException("Failed to read file bit pattern " + inputFile, e);
		}
	}
}
