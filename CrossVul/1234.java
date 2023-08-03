
package ghidra.bitpatterns.info;
import java.math.BigInteger;
import java.util.*;
public class ContextRegisterExtent {
	private Set<String> contextRegisters;
	private Map<String, Set<BigInteger>> regsToValues;
	public ContextRegisterExtent() {
		contextRegisters = new HashSet<String>();
		regsToValues = new HashMap<String, Set<BigInteger>>();
	}
	public void addContextInfo(List<ContextRegisterInfo> contextRegisterInfo) {
		if ((contextRegisterInfo == null) || (contextRegisterInfo.isEmpty())) {
			return;
		}
		for (ContextRegisterInfo cRegInfo : contextRegisterInfo) {
			addRegisterAndValue(cRegInfo.getContextRegister(), cRegInfo.getValueAsBigInteger());
		}
	}
	private void addRegisterAndValue(String register, BigInteger value) {
		if (!contextRegisters.contains(register)) {
			contextRegisters.add(register);
			Set<BigInteger> valueSet = new HashSet<BigInteger>();
			regsToValues.put(register, valueSet);
		}
		regsToValues.get(register).add(value);
	}
	public List<String> getContextRegisters() {
		List<String> contextRegisterList = new ArrayList<String>(contextRegisters.size());
		contextRegisterList.addAll(contextRegisters);
		Collections.sort(contextRegisterList);
		return contextRegisterList;
	}
	public List<BigInteger> getValuesForRegister(String register) {
		List<BigInteger> valuesList = new ArrayList<BigInteger>();
		Set<BigInteger> values = regsToValues.get(register);
		if ((register != null) && (values != null)) {
			valuesList.addAll(values);
			Collections.sort(valuesList);
		}
		return valuesList;
	}
	@Override
	public String toString() {
		if (getContextRegisters().isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		List<String> registers = getContextRegisters();
		for (String register : registers) {
			sb.append("Register: ");
			sb.append(register);
			sb.append("\n");
			sb.append("Values: ");
			List<BigInteger> values = getValuesForRegister(register);
			for (int i = 0; i < values.size(); ++i) {
				sb.append(values.get(i));
				if (i != (values.size() - 1)) {
					sb.append(", ");
				}
				else {
					sb.append("\n\n");
				}
			}
		}
		return sb.toString();
	}
}
