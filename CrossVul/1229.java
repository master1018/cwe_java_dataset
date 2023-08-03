
package ghidra.bitpatterns.info;
import java.awt.Component;
import java.beans.XMLDecoder;
import java.io.*;
import java.util.*;
import org.apache.commons.io.FileUtils;
import ghidra.program.model.address.AddressSetView;
import ghidra.program.model.listing.*;
import ghidra.util.Msg;
import ghidra.util.task.*;
public class FileBitPatternInfoReader {
	private List<FunctionBitPatternInfo> fInfoList; 
	private List<Long> startingAddresses; 
	private ContextRegisterExtent registerExtent;
	private int numFuncs = 0;
	private int numFiles = 0;
	private DataGatheringParams params;
	public FileBitPatternInfoReader(Program program, DataGatheringParams params, Component parent) {
		startingAddresses = new ArrayList<Long>();
		fInfoList = new ArrayList<FunctionBitPatternInfo>();
		registerExtent = new ContextRegisterExtent();
		this.params = params;
		numFiles = 1;
		MineProgramTask mineTask = new MineProgramTask(program);
		new TaskLauncher(mineTask, parent);
	}
	public FileBitPatternInfoReader(File xmlDir, Component parent) {
		if (!xmlDir.isDirectory()) {
			throw new IllegalArgumentException(xmlDir.getName() + " is not a directory");
		}
		startingAddresses = new ArrayList<Long>();
		fInfoList = new ArrayList<FunctionBitPatternInfo>();
		registerExtent = new ContextRegisterExtent();
		File[] dataFiles = xmlDir.listFiles();
		ReadDirectoryTask readTask = new ReadDirectoryTask(dataFiles);
		new TaskLauncher(readTask, parent);
	}
	FileBitPatternInfoReader(File xmlDir) {
		if (!xmlDir.isDirectory()) {
			throw new IllegalArgumentException(xmlDir.getName() + " is not a directory");
		}
		startingAddresses = new ArrayList<Long>();
		fInfoList = new ArrayList<FunctionBitPatternInfo>();
		registerExtent = new ContextRegisterExtent();
		params = null;
		Iterator<File> dataFiles = FileUtils.iterateFiles(xmlDir, null, false);
		while (dataFiles.hasNext()) {
			File dataFile = dataFiles.next();
			processXmlFile(dataFile);
		}
	}
	private void processFBPIList(List<FunctionBitPatternInfo> fList) {
		for (FunctionBitPatternInfo fInfo : fList) {
			numFuncs++;
			fInfoList.add(fInfo);
			startingAddresses.add(Long.parseUnsignedLong(fInfo.getAddress(), 16));
			registerExtent.addContextInfo(fInfo.getContextRegisters());
		}
	}
	public List<Long> getStartingAddresses() {
		return startingAddresses;
	}
	public int getNumFuncs() {
		return numFuncs;
	}
	public int getNumFiles() {
		return numFiles;
	}
	public ContextRegisterExtent getContextRegisterExtent() {
		return registerExtent;
	}
	public List<FunctionBitPatternInfo> getFInfoList() {
		return fInfoList;
	}
	public List<Long> getFilteredAddresses(ContextRegisterFilter registerFilter) {
		List<Long> filteredAddresses = new ArrayList<Long>();
		for (FunctionBitPatternInfo fInfo : fInfoList) {
			if (registerFilter.allows(fInfo.getContextRegisters())) {
				filteredAddresses.add(Long.parseUnsignedLong(fInfo.getAddress(), 16));
			}
		}
		return filteredAddresses;
	}
	public DataGatheringParams getDataGatheringParams() {
		return params;
	}
	private void processXmlFile(File dataFile) {
		if (!dataFile.getName().endsWith(".xml")) {
			Msg.info(this, "Skipping " + dataFile.getName());
			return;
		}
		numFiles++;
		FileBitPatternInfo fileInfo = null;
		try (XMLDecoder xmlDecoder = new XMLDecoder(new FileInputStream(dataFile))) {
			fileInfo = (FileBitPatternInfo) xmlDecoder.readObject();
		}
		catch (ArrayIndexOutOfBoundsException e) {
		}
		catch (IOException e) {
			Msg.error(this, "IOException", e);
		}
		if (fileInfo == null) {
			Msg.info(this, "null FileBitPatternInfo for " + dataFile);
			return;
		}
		if (fileInfo.getFuncBitPatternInfo() == null) {
			Msg.info(this, "fList.getFuncBitPatternInfoList null for " + dataFile);
			return;
		}
		if (params == null) {
			params = new DataGatheringParams();
			params.setNumFirstBytes(fileInfo.getNumFirstBytes());
			params.setNumPreBytes(fileInfo.getNumPreBytes());
			params.setNumReturnBytes(fileInfo.getNumReturnBytes());
			params.setNumFirstInstructions(fileInfo.getNumFirstInstructions());
			params.setNumPreInstructions(fileInfo.getNumPreInstructions());
			params.setNumReturnInstructions(fileInfo.getNumReturnInstructions());
		}
		processFBPIList(fileInfo.getFuncBitPatternInfo());
	}
	class MineProgramTask extends Task {
		private AddressSetView initialized;
		private FunctionIterator fIter;
		private List<FunctionBitPatternInfo> fList;
		private Program program;
		public MineProgramTask(Program program) {
			super("Mining Program", true, true, true);
			this.program = program;
			initialized = program.getMemory().getLoadedAndInitializedAddressSet();
			fIter = program.getFunctionManager().getFunctions(true);
			fList = new ArrayList<>();
		}
		@Override
		public void run(TaskMonitor monitor) {
			monitor.setMaximum(program.getFunctionManager().getFunctionCount());
			while (fIter.hasNext() && !monitor.isCancelled()) {
				monitor.incrementProgress(1);
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
				if (program.getListing().getInstructionAt(func.getEntryPoint()) == null) {
					continue;
				}
				FunctionBitPatternInfo fStart = new FunctionBitPatternInfo(program, func, params);
				if (fStart.getFirstBytes() != null) {
					fList.add(fStart);
				}
			}
			processFBPIList(fList);
		}
	}
	class ReadDirectoryTask extends Task {
		private File[] dataFiles;
		public ReadDirectoryTask(File[] dataFiles) {
			super("Reading XML", true, true, true);
			this.dataFiles = dataFiles;
		}
		@Override
		public void run(TaskMonitor monitor) {
			monitor.setMaximum(dataFiles.length);
			params = null;
			for (File dataFile : dataFiles) {
				monitor.incrementProgress(1);
				processXmlFile(dataFile);
			}
		}
	}
}
