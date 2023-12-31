package org.junit.rules;
import static org.junit.Assert.fail;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Rule;
public class TemporaryFolder extends ExternalResource {
    private final File parentFolder;
    private final boolean assureDeletion;
    private File folder;
    private static final int TEMP_DIR_ATTEMPTS = 10000;
    private static final String TMP_PREFIX = "junit";
    public TemporaryFolder() {
        this((File) null);
    }
    public TemporaryFolder(File parentFolder) {
        this.parentFolder = parentFolder;
        this.assureDeletion = false;
    }
    protected TemporaryFolder(Builder builder) {
        this.parentFolder = builder.parentFolder;
        this.assureDeletion = builder.assureDeletion;
    }
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private File parentFolder;
        private boolean assureDeletion;
        protected Builder() {}
        public Builder parentFolder(File parentFolder) {
            this.parentFolder = parentFolder;
            return this;
        }
        public Builder assureDeletion() {
            this.assureDeletion = true;
            return this;
        }
        public TemporaryFolder build() {
            return new TemporaryFolder(this);
        }
    }
    @Override
    protected void before() throws Throwable {
        create();
    }
    @Override
    protected void after() {
        delete();
    }
    public void create() throws IOException {
        folder = createTemporaryFolderIn(parentFolder);
    }
    public File newFile(String fileName) throws IOException {
        File file = new File(getRoot(), fileName);
        if (!file.createNewFile()) {
            throw new IOException(
                    "a file with the name \'" + fileName + "\' already exists in the test folder");
        }
        return file;
    }
    public File newFile() throws IOException {
        return File.createTempFile(TMP_PREFIX, null, getRoot());
    }
    public File newFolder(String path) throws IOException {
        return newFolder(new String[]{path});
    }
    public File newFolder(String... paths) throws IOException {
        if (paths.length == 0) {
            throw new IllegalArgumentException("must pass at least one path");
        }
        File root = getRoot();
        for (String path : paths) {
            if (new File(path).isAbsolute()) {
                throw new IOException("folder path \'" + path + "\' is not a relative path");
            }
        }
        File relativePath = null;
        File file = root;
        boolean lastMkdirsCallSuccessful = true;
        for (String path : paths) {
            relativePath = new File(relativePath, path);
            file = new File(root, relativePath.getPath());
            lastMkdirsCallSuccessful = file.mkdirs();
            if (!lastMkdirsCallSuccessful && !file.isDirectory()) {
                if (file.exists()) {
                    throw new IOException(
                            "a file with the path \'" + relativePath.getPath() + "\' exists");
                } else {
                    throw new IOException(
                            "could not create a folder with the path \'" + relativePath.getPath() + "\'");
                }
            }
        }
        if (!lastMkdirsCallSuccessful) {
            throw new IOException(
                    "a folder with the path \'" + relativePath.getPath() + "\' already exists");
        }
        return file;
    }
    public File newFolder() throws IOException {
        return createTemporaryFolderIn(getRoot());
    }
    private static File createTemporaryFolderIn(File parentFolder) throws IOException {
        try {
            return createTemporaryFolderWithNioApi(parentFolder);
        } catch (ClassNotFoundException ignore) {
            return createTemporaryFolderWithFileApi(parentFolder);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            IOException exception = new IOException("Failed to create temporary folder in " + parentFolder);
            exception.initCause(cause);
            throw exception;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create temporary folder in " + parentFolder, e);
        }
    }
    private static File createTemporaryFolderWithNioApi(File parentFolder) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> filesClass = Class.forName("java.nio.file.Files");
        Object fileAttributeArray = Array.newInstance(Class.forName("java.nio.file.attribute.FileAttribute"), 0);
        Class<?> pathClass = Class.forName("java.nio.file.Path");
        Object tempDir;
        if (parentFolder != null) {
            Method createTempDirectoryMethod = filesClass.getDeclaredMethod("createTempDirectory", pathClass, String.class, fileAttributeArray.getClass());
            Object parentPath = File.class.getDeclaredMethod("toPath").invoke(parentFolder);
            tempDir = createTempDirectoryMethod.invoke(null, parentPath, TMP_PREFIX, fileAttributeArray);
        } else {
            Method createTempDirectoryMethod = filesClass.getDeclaredMethod("createTempDirectory", String.class, fileAttributeArray.getClass());
            tempDir = createTempDirectoryMethod.invoke(null, TMP_PREFIX, fileAttributeArray);
        }
        return (File) pathClass.getDeclaredMethod("toFile").invoke(tempDir);
    }
    private static File createTemporaryFolderWithFileApi(File parentFolder) throws IOException {
        File createdFolder = null;
        for (int i = 0; i < TEMP_DIR_ATTEMPTS; ++i) {
            String suffix = ".tmp";
            File tmpFile = File.createTempFile(TMP_PREFIX, suffix, parentFolder);
            String tmpName = tmpFile.toString();
            String folderName = tmpName.substring(0, tmpName.length() - suffix.length());
            createdFolder = new File(folderName);
            if (createdFolder.mkdir()) {
                tmpFile.delete();
                return createdFolder;
            }
            tmpFile.delete();
        }
        throw new IOException("Unable to create temporary directory in: "
            + parentFolder.toString() + ". Tried " + TEMP_DIR_ATTEMPTS + " times. "
            + "Last attempted to create: " + createdFolder.toString());
    }
    public File getRoot() {
        if (folder == null) {
            throw new IllegalStateException(
                    "the temporary folder has not yet been created");
        }
        return folder;
    }
    public void delete() {
        if (!tryDelete()) {
            if (assureDeletion) {
                fail("Unable to clean up temporary folder " + folder);
            }
        }
    }
    private boolean tryDelete() {
        if (folder == null) {
            return true;
        }
        return recursiveDelete(folder);
    }
    private boolean recursiveDelete(File file) {
        if (file.delete()) {
            return true;
        }
        File[] files = file.listFiles();
        if (files != null) {
            for (File each : files) {
                if (!recursiveDelete(each)) {
                    return false;
                }
            }
        }
        return file.delete();
    }
}
