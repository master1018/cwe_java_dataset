
package ghidra.bitpatterns.info;
import java.math.BigInteger;
import org.jdom.Element;
public class ContextRegisterInfo {
	static final String XML_ELEMENT_NAME = "ContextRegisterInfo";
	String contextRegister;
	BigInteger value;
	public ContextRegisterInfo() {
	}
	public ContextRegisterInfo(String contextRegister) {
		this.contextRegister = contextRegister;
	}
	public String getContextRegister() {
		return contextRegister;
	}
	public void setContextRegister(String contextRegister) {
		this.contextRegister = contextRegister;
	}
	public void setValue(BigInteger value) {
		this.value = value;
	}
	public BigInteger getValue() {
		return value;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(contextRegister);
		sb.append(" ");
		sb.append(value);
		return sb.toString();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ContextRegisterInfo other = (ContextRegisterInfo) obj;
		if (!contextRegister.equals(other.getContextRegister())) {
			return false;
		}
		if (value == null) {
			return (other.getValue() == null);
		}
		if (other.getValue() == null) {
			return false;
		}
		return value.equals(other.getValue());
	}
	@Override
	public int hashCode() {
		int hashCode = 17;
		hashCode = 31 * hashCode + contextRegister.hashCode();
		hashCode = 31 * hashCode + value.hashCode();
		return hashCode;
	}
	public static ContextRegisterInfo fromXml(Element ele) {
		String contextRegister = ele.getAttributeValue("contextRegister");
		String value = ele.getAttributeValue("value");
		ContextRegisterInfo result = new ContextRegisterInfo();
		result.setContextRegister(contextRegister);
		result.setValue(value != null ? new BigInteger(value) : null);
		return result;
	}
	public Element toXml() {
		Element e = new Element(XML_ELEMENT_NAME);
		e.setAttribute("contextRegister", contextRegister);
		if (value != null) {
			e.setAttribute("value", value.toString());
		}
		return e;
	}
}
