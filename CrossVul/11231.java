
package ghidra.bitpatterns.info;
import java.math.BigInteger;
import java.util.*;
public class ContextRegisterFilter {
	private Set<String> contextRegisters;
	private Map<String, BigInteger> values;
	public ContextRegisterFilter() {
		contextRegisters = new HashSet<String>();
		values = new HashMap<String, BigInteger>();
	}
	public void addRegAndValueToFilter(String contextRegister, BigInteger value) {
		if (contextRegisters.contains(contextRegister)) {
			throw new IllegalStateException("Filter can have only one value per register!");
		}
		contextRegisters.add(contextRegister);
		values.put(contextRegister, value);
	}
	public boolean allows(List<ContextRegisterInfo> contextRegisterInfos) {
		for (ContextRegisterInfo cInfo : contextRegisterInfos) {
			if (contextRegisters.contains(cInfo.getContextRegister())) {
				if (!values.get(cInfo.getContextRegister()).equals(cInfo.getValue())) {
					return false;
				}
			}
		}
		return true;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Context Register Filter: \n");
		for (String cReg : contextRegisters) {
			sb.append(cReg);
			sb.append(": ");
			sb.append(values.get(cReg).toString());
			sb.append("\n");
		}
		sb.append("\n");
		return sb.toString();
	}
	public String getCompactString() {
		StringBuilder sb = new StringBuilder();
		String[] registers = contextRegisters.toArray(new String[0]);
		for (int i = 0, max = registers.length; i < max; ++i) {
			sb.append(registers[i]);
			sb.append("=");
			sb.append(values.get(registers[i]).toString());
			if (i < max - 1) {
				sb.append(";");
			}
		}
		return sb.toString();
	}
	@Override
	public int hashCode() {
		int hash = 17;
		hash = 31 * hash + contextRegisters.hashCode();
		hash = 31 * hash + values.hashCode();
		return hash;
	}
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ContextRegisterFilter)) {
			return false;
		}
		ContextRegisterFilter otherFilter = (ContextRegisterFilter) o;
		if (!otherFilter.contextRegisters.equals(contextRegisters)) {
			return false;
		}
		if (!otherFilter.values.equals(values)) {
			return false;
		}
		return true;
	}
	public Map<String, BigInteger> getValueMap() {
		return values;
	}
}
