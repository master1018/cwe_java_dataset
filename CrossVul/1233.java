
import java.beans.XMLEncoder;
import java.io.*;
import java.util.List;
import ghidra.app.script.GhidraScript;
import ghidra.bitpatterns.info.*;
import ghidra.program.model.address.AddressSetView;
import ghidra.program.model.listing.Function;
import ghidra.program.model.listing.FunctionIterator;
import ghidra.util.Msg;
public class DumpFunctionPatternInfoScript extends GhidraScript {
	private static int totalFuncs = 0;
	private static int programsAnalyzed = 0;
	@Override
	protected void run() throws Exception {
		if (!isRunningHeadless()) {
			totalFuncs = 0;
			programsAnalyzed = 0;
		}
		int numFirstBytes = askInt("Number of first bytes", "bytes");
		int numFirstInstructions = askInt("Number of first instructions", "instructions");
		int numPreBytes = askInt("Number of pre bytes", "bytes");
		int numPreInstructions = askInt("Number of pre instructions", "instructions");
		int numReturnBytes = askInt("Number of return bytes", "bytes");
		int numReturnInstructions = askInt("Number of return instructions", "instructions");
		String saveDirName = askString("Directory to save results", "directory");
		String contextRegsCSV = askString("Context register csv", "csv");
		File saveDir = new File(saveDirName);
		if (!saveDir.isDirectory()) {
			Msg.info(this, "Invalid save directory: " + saveDirName);
			return;
		}
		List<String> contextRegisters = DataGatheringParams.getContextRegisterList(contextRegsCSV);
		programsAnalyzed++;
		if (currentProgram == null) {
			Msg.info(this, "null current program: try again with the -process option");
			return;
		}
		if (currentProgram.getFunctionManager().getFunctionCount() == 0) {
			Msg.info(this, "No functions found in " + currentProgram.getName() + ", skipping.");
			return;
		}
		FunctionIterator fIter = currentProgram.getFunctionManager().getFunctions(true);
		DataGatheringParams params = new DataGatheringParams();
		params.setNumPreBytes(numPreBytes);
		params.setNumFirstBytes(numFirstBytes);
		params.setNumReturnBytes(numReturnBytes);
		params.setNumPreInstructions(numPreInstructions);
		params.setNumFirstInstructions(numFirstInstructions);
		params.setNumReturnInstructions(numReturnInstructions);
		params.setContextRegisters(contextRegisters);
		FileBitPatternInfo funcPatternList = new FileBitPatternInfo();
		funcPatternList.setLanguageID(currentProgram.getLanguageID().getIdAsString());
		funcPatternList.setGhidraURL("TODO: url");
		funcPatternList.setNumPreBytes(numPreBytes);
		funcPatternList.setNumPreInstructions(numPreInstructions);
		funcPatternList.setNumFirstBytes(numFirstBytes);
		funcPatternList.setNumFirstInstructions(numFirstInstructions);
		funcPatternList.setNumReturnBytes(numReturnBytes);
		funcPatternList.setNumReturnInstructions(numReturnInstructions);
		AddressSetView initialized = currentProgram.getMemory().getLoadedAndInitializedAddressSet();
		while (fIter.hasNext()) {
			monitor.checkCanceled();
			Function func = fIter.next();
			if (func.isThunk()) {
				continue;
			}
			if (func.isExternal()) {
				continue;
			}
			if (!initialized.contains(func.getEntryPoint())) {
				continue;
			}
			if (currentProgram.getListing().getInstructionAt(func.getEntryPoint()) == null) {
				continue;
			}
			FunctionBitPatternInfo fStart =
				new FunctionBitPatternInfo(currentProgram, func, params);
			if (fStart.getFirstBytes() != null) {
				funcPatternList.getFuncBitPatternInfo().add(fStart);
				totalFuncs++;
			}
		}
		File savedFile = new File(saveDir.getAbsolutePath() + File.separator +
			currentProgram.getDomainFile().getPathname().replaceAll("/", "_") + "_" +
			currentProgram.getExecutableMD5() + "_funcInfo.xml");
		try (XMLEncoder xmlEncoder =
			new XMLEncoder(new BufferedOutputStream(new FileOutputStream(savedFile)))) {
			xmlEncoder.writeObject(funcPatternList);
		}
		Msg.info(this,
			"Programs analyzed: " + programsAnalyzed + "; total functions: " + totalFuncs);
	}
}
