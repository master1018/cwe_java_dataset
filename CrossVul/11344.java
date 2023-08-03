
package io.goobi.viewer.controller;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.unigoettingen.sub.commons.contentlib.exceptions.ContentLibException;
import de.unigoettingen.sub.commons.contentlib.exceptions.ContentNotFoundException;
import de.unigoettingen.sub.commons.contentlib.exceptions.ServiceNotAllowedException;
import io.goobi.viewer.api.rest.AbstractApiUrlManager;
import io.goobi.viewer.api.rest.resourcebuilders.TextResourceBuilder;
import io.goobi.viewer.exceptions.AccessDeniedException;
import io.goobi.viewer.exceptions.DAOException;
import io.goobi.viewer.exceptions.HTTPException;
import io.goobi.viewer.exceptions.IndexUnreachableException;
import io.goobi.viewer.exceptions.PresentationException;
import io.goobi.viewer.exceptions.ViewerConfigurationException;
import io.goobi.viewer.managedbeans.utils.BeanUtils;
public class DataFileTools {
    private static final Logger logger = LoggerFactory.getLogger(DataFileTools.class);
    public static String getDataRepositoryPathForRecord(String pi) throws PresentationException, IndexUnreachableException {
        String dataRepositoryPath = DataManager.getInstance().getSearchIndex().findDataRepositoryName(pi);
        return getDataRepositoryPath(dataRepositoryPath);
    }
    static String getDataRepositoryPath(String dataRepositoryPath) {
        if (StringUtils.isBlank(dataRepositoryPath)) {
            return DataManager.getInstance().getConfiguration().getViewerHome();
        }
        if (Paths.get(FileTools.adaptPathForWindows(dataRepositoryPath)).isAbsolute()) {
            return dataRepositoryPath + '/';
        }
        return DataManager.getInstance().getConfiguration().getDataRepositoriesHome() + dataRepositoryPath + '/';
    }
    public static Path getMediaFolder(String pi) throws PresentationException, IndexUnreachableException {
        return getDataFolder(pi, DataManager.getInstance().getConfiguration().getMediaFolder());
    }
    public static Map<String, Path> getDataFolders(String pi, String... dataFolderNames) throws PresentationException, IndexUnreachableException {
        if (pi == null) {
            throw new IllegalArgumentException("pi may not be null");
        }
        if (dataFolderNames == null) {
            throw new IllegalArgumentException("dataFolderNames may not be null");
        }
        String dataRepositoryName = DataManager.getInstance().getSearchIndex().findDataRepositoryName(pi);
        Map<String, Path> ret = new HashMap<>(dataFolderNames.length);
        for (String dataFolderName : dataFolderNames) {
            ret.put(dataFolderName, getDataFolder(pi, dataFolderName, dataRepositoryName));
        }
        return ret;
    }
    public static Path getDataFolder(String pi, String dataFolderName) throws PresentationException, IndexUnreachableException {
        if (pi == null) {
            throw new IllegalArgumentException("pi may not be null");
        }
        String dataRepository = DataManager.getInstance().getSearchIndex().findDataRepositoryName(pi);
        return getDataFolder(pi, dataFolderName, dataRepository);
    }
    public static Path getDataFolder(String pi, String dataFolderName, String dataRepositoryFolder) {
        Path repository;
        if (StringUtils.isBlank(dataRepositoryFolder)) {
            repository = Paths.get(DataManager.getInstance().getConfiguration().getViewerHome());
        } else if (Paths.get(FileTools.adaptPathForWindows(dataRepositoryFolder)).isAbsolute()) {
            repository = Paths.get(dataRepositoryFolder);
        } else {
            repository = Paths.get(DataManager.getInstance().getConfiguration().getDataRepositoriesHome(), dataRepositoryFolder);
        }
        Path folder = repository.resolve(dataFolderName).resolve(pi);
        return folder;
    }
    public static Path getDataFilePath(String pi, String dataFolderName, String altDataFolderName, String fileName)
            throws PresentationException, IndexUnreachableException {
        fileName = Paths.get(fileName).getFileName().toString();
        java.nio.file.Path dataFolderPath = getDataFolder(pi, dataFolderName);
        if (StringUtils.isNotBlank(fileName)) {
            dataFolderPath = dataFolderPath.resolve(fileName);
        }
        if (StringUtils.isNotBlank(altDataFolderName) && !Files.exists(dataFolderPath)) {
            return getDataFilePath(pi, altDataFolderName, null, fileName);
        }
        return dataFolderPath;
    }
    public static Path getDataFilePath(String pi, String relativeFilePath) throws PresentationException, IndexUnreachableException {
        if (pi == null) {
            throw new IllegalArgumentException("pi may not be null");
        }
        String dataRepositoryName = DataManager.getInstance().getSearchIndex().findDataRepositoryName(pi);
        String dataRepositoryPath = getDataRepositoryPath(dataRepositoryName);
        return Paths.get(dataRepositoryPath, relativeFilePath);
    }
    public static String getSourceFilePath(String fileName, String format) throws PresentationException, IndexUnreachableException {
        String pi = FilenameUtils.getBaseName(fileName);
        String dataRepository = DataManager.getInstance().getSearchIndex().findDataRepositoryName(pi);
        return getSourceFilePath(fileName, dataRepository, format);
    }
    public static String getSourceFilePath(String fileName, String dataRepository, String format) {
        if (StringUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException("fileName may not be null or empty");
        }
        if (StringUtils.isEmpty(format)) {
            throw new IllegalArgumentException("format may not be null or empty");
        }
        switch (format) {
            case SolrConstants._METS:
            case SolrConstants._LIDO:
            case SolrConstants._DENKXWEB:
            case SolrConstants._WORLDVIEWS:
            case SolrConstants._DUBLINCORE:
                break;
            default:
                throw new IllegalArgumentException("format must be: METS | LIDO | DENKXWEB | DUBLINCORE | WORLDVIEWS");
        }
        StringBuilder sb = new StringBuilder(getDataRepositoryPath(dataRepository));
        switch (format) {
            case SolrConstants._METS:
                sb.append(DataManager.getInstance().getConfiguration().getIndexedMetsFolder());
                break;
            case SolrConstants._LIDO:
                sb.append(DataManager.getInstance().getConfiguration().getIndexedLidoFolder());
                break;
            case SolrConstants._DENKXWEB:
                sb.append(DataManager.getInstance().getConfiguration().getIndexedDenkxwebFolder());
                break;
            case SolrConstants._DUBLINCORE:
                sb.append(DataManager.getInstance().getConfiguration().getIndexedDublinCoreFolder());
                break;
            case SolrConstants._WORLDVIEWS:
                sb.append(DataManager.getInstance().getConfiguration().getIndexedMetsFolder());
                break;
        }
        sb.append('/').append(fileName);
        return sb.toString();
    }
    public static String getTextFilePath(String pi, String fileName, String format) throws PresentationException, IndexUnreachableException {
        if (StringUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException("fileName may not be null or empty");
        }
        if (StringUtils.isEmpty(format)) {
            throw new IllegalArgumentException("format may not be null or empty");
        }
        String dataFolderName = null;
        switch (format) {
            case SolrConstants.FILENAME_ALTO:
                dataFolderName = DataManager.getInstance().getConfiguration().getAltoFolder();
                break;
            case SolrConstants.FILENAME_FULLTEXT:
                dataFolderName = DataManager.getInstance().getConfiguration().getFulltextFolder();
                break;
            case SolrConstants.FILENAME_TEI:
                dataFolderName = DataManager.getInstance().getConfiguration().getTeiFolder();
                break;
        }
        return getDataFilePath(pi, dataFolderName, null, fileName).toAbsolutePath().toString();
    }
    public static Path getTextFilePath(String pi, String relativeFilePath) throws PresentationException, IndexUnreachableException {
        if (StringUtils.isBlank(relativeFilePath)) {
            return null;
        }
        String dataRepository = DataManager.getInstance().getSearchIndex().findDataRepositoryName(pi);
        Path filePath = Paths.get(getDataRepositoryPath(dataRepository), relativeFilePath);
        return filePath;
    }
    public static String loadFulltext(String altoFilePath, String fulltextFilePath, boolean mergeLineBreakWords,
            HttpServletRequest request)
            throws AccessDeniedException, FileNotFoundException, IOException, IndexUnreachableException, DAOException, ViewerConfigurationException {
        TextResourceBuilder builder = new TextResourceBuilder(BeanUtils.getRequest(), null);
        if (altoFilePath != null) {
            try {
                String alto = builder.getAltoDocument(FileTools.getBottomFolderFromPathString(altoFilePath),
                        FileTools.getFilenameFromPathString(altoFilePath));
                if (alto != null) {
                    return ALTOTools.getFullText(alto, mergeLineBreakWords, request);
                }
            } catch (ContentNotFoundException e) {
                throw new FileNotFoundException(e.getMessage());
            } catch (ServiceNotAllowedException e) {
                throw new AccessDeniedException("fulltextAccessDenied");
            } catch (PresentationException e) {
                logger.error(e.getMessage());
            }
        }
        if (fulltextFilePath != null) {
            try {
                String fulltext = builder.getFulltext(FileTools.getBottomFolderFromPathString(fulltextFilePath),
                        FileTools.getFilenameFromPathString(fulltextFilePath));
                if (fulltext != null) {
                    return fulltext;
                }
            } catch (ContentNotFoundException e) {
                throw new FileNotFoundException(e.getMessage());
            } catch (ServiceNotAllowedException e) {
                throw new AccessDeniedException("fulltextAccessDenied");
            } catch (PresentationException e) {
                logger.error(e.getMessage());
            }
        }
        return null;
    }
    public static String loadAlto(String altoFilePath)
            throws  ContentNotFoundException, AccessDeniedException, IndexUnreachableException, DAOException, PresentationException {
        TextResourceBuilder builder = new TextResourceBuilder(BeanUtils.getRequest(), null);
        if (altoFilePath != null) {
            try {
                String alto = builder.getAltoDocument(FileTools.getBottomFolderFromPathString(altoFilePath),
                        FileTools.getFilenameFromPathString(altoFilePath));
                return alto;
            } catch (ServiceNotAllowedException e) {
                throw new AccessDeniedException("fulltextAccessDenied");
            }
        } else throw new ContentNotFoundException("Alto file " + altoFilePath + " not found");
    }
    public static String loadTei(String pi, String language)
            throws AccessDeniedException, FileNotFoundException, IOException, ViewerConfigurationException {
        logger.trace("loadTei: {}/{}", pi, language);
        if (pi == null) {
            return null;
        }
        TextResourceBuilder builder = new TextResourceBuilder(BeanUtils.getRequest(), null);
        try {
            return builder.getTeiDocument(pi, language);
        } catch (PresentationException | IndexUnreachableException | DAOException | ContentLibException e) {
            logger.error(e.toString());
            return null;
        }
    }
}
