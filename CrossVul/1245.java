
package org.bigbluebutton.presentation.imp;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.bigbluebutton.presentation.ConversionMessageConstants;
import org.bigbluebutton.presentation.SupportedFileTypes;
import org.bigbluebutton.presentation.UploadedPresentation;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
public class OfficeToPdfConversionService {
  private static Logger log = LoggerFactory.getLogger(OfficeToPdfConversionService.class);
  private OfficeDocumentValidator2 officeDocumentValidator;
  private final OfficeManager officeManager;
  private final LocalConverter documentConverter;
  private boolean skipOfficePrecheck = false;
  public OfficeToPdfConversionService() throws OfficeException {
    officeManager = LocalOfficeManager
      .builder()
      .portNumbers(8100, 8101, 8102, 8103, 8104)
      .build();
    documentConverter = LocalConverter
      .builder()
      .officeManager(officeManager)
      .filterChain(new OfficeDocumentConversionFilter())
      .build();
  }
  public UploadedPresentation convertOfficeToPdf(UploadedPresentation pres) {
    initialize(pres);
    if (SupportedFileTypes.isOfficeFile(pres.getFileType())) {
      if (!skipOfficePrecheck && officeDocumentValidator.isValid(pres)) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("meetingId", pres.getMeetingId());
        logData.put("presId", pres.getId());
        logData.put("filename", pres.getName());
        logData.put("logCode", "problems_office_to_pdf_validation");
        logData.put("message", "Problems detected prior to converting the file to PDF.");
        Gson gson = new Gson();
        String logStr = gson.toJson(logData);
        log.warn(" --analytics-- data={}", logStr);
        pres.setConversionStatus(ConversionMessageConstants.OFFICE_DOC_CONVERSION_INVALID_KEY);
        return pres;
      }
      File pdfOutput = setupOutputPdfFile(pres);
      if (convertOfficeDocToPdf(pres, pdfOutput)) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("meetingId", pres.getMeetingId());
        logData.put("presId", pres.getId());
        logData.put("filename", pres.getName());
        logData.put("logCode", "office_to_pdf_success");
        logData.put("message", "Successfully converted office file to pdf.");
        Gson gson = new Gson();
        String logStr = gson.toJson(logData);
        log.info(" --analytics-- data={}", logStr);
        makePdfTheUploadedFileAndSetStepAsSuccess(pres, pdfOutput);
      } else {
        Map<String, Object> logData = new HashMap<>();
        logData.put("meetingId", pres.getMeetingId());
        logData.put("presId", pres.getId());
        logData.put("filename", pres.getName());
        logData.put("logCode", "office_to_pdf_failed");
        logData.put("message", "Failed to convert " + pres.getUploadedFile().getAbsolutePath() + " to Pdf.");
        Gson gson = new Gson();
        String logStr = gson.toJson(logData);
        log.warn(" --analytics-- data={}", logStr);
        pres.setConversionStatus(ConversionMessageConstants.OFFICE_DOC_CONVERSION_FAILED_KEY);
        return pres;
      }
    }
    return pres;
  }
  public void initialize(UploadedPresentation pres) {
    pres.setConversionStatus(ConversionMessageConstants.OFFICE_DOC_CONVERSION_FAILED_KEY);
  }
  private File setupOutputPdfFile(UploadedPresentation pres) {
    File presentationFile = pres.getUploadedFile();
    String filenameWithoutExt = presentationFile.getAbsolutePath().substring(0,
        presentationFile.getAbsolutePath().lastIndexOf('.'));
    return new File(filenameWithoutExt + ".pdf");
  }
  private boolean convertOfficeDocToPdf(UploadedPresentation pres,
      File pdfOutput) {
    Office2PdfPageConverter converter = new Office2PdfPageConverter();
    return converter.convert(pres.getUploadedFile(), pdfOutput, 0, pres, documentConverter);
  }
  private void makePdfTheUploadedFileAndSetStepAsSuccess(UploadedPresentation pres, File pdf) {
    pres.setUploadedFile(pdf);
    pres.setConversionStatus(ConversionMessageConstants.OFFICE_DOC_CONVERSION_SUCCESS_KEY);
  }
  public void setOfficeDocumentValidator(OfficeDocumentValidator2 v) {
    officeDocumentValidator = v;
  }
  public void setSkipOfficePrecheck(boolean skipOfficePrecheck) {
    this.skipOfficePrecheck = skipOfficePrecheck;
  }
  public void start() {
    try {
      officeManager.start();
    } catch (OfficeException e) {
      log.error("Could not start Office Manager", e);
    }
  }
  public void stop() {
    try {
      officeManager.stop();
    } catch (OfficeException e) {
      log.error("Could not stop Office Manager", e);
    }
  }
}