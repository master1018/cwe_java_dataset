
package ghidra.bitpatterns.info;
import java.util.ArrayList;
import java.util.List;
public class FileBitPatternInfo {
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
}
