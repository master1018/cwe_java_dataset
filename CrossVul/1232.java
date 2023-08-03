
package ghidra.bitpatterns.info;
import java.math.BigInteger;
public class ContextRegisterInfo {
	String contextRegister;
	String value;
	BigInteger valueAsBigInteger;
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
	public BigInteger getValueAsBigInteger() {
		return valueAsBigInteger;
	}
	public void setValue(BigInteger valueAsBigInteger) {
		this.valueAsBigInteger = valueAsBigInteger;
		this.value = valueAsBigInteger.toString();
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
		this.valueAsBigInteger = new BigInteger(value);
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
}
