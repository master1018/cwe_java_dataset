package org.uberfire.io.regex;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.uberfire.io.CommonIOServiceDotFileTest;
import org.uberfire.io.IOService;
import org.uberfire.io.impl.IOServiceDotFileImpl;
import org.uberfire.java.nio.file.Path;
import org.uberfire.java.nio.file.Paths;
import static org.uberfire.io.regex.AntPathMatcher.*;
public class AntPathMatcherTest {
    final static IOService ioService = new IOServiceDotFileImpl();
    private static File path = null;
    @BeforeClass
    public static void setup() throws IOException {
        path = CommonIOServiceDotFileTest.createTempDirectory();
        System.setProperty( "org.uberfire.nio.git.dir", path.getAbsolutePath() );
        System.out.println( ".niogit: " + path.getAbsolutePath() );
        final URI newRepo = URI.create( "git:
        ioService.newFileSystem( newRepo, new HashMap<String, Object>() );
    }
    @AfterClass
    @BeforeClass
    public static void cleanup() {
        if ( path != null ) {
            FileUtils.deleteQuietly( path );
        }
    }
    @Test
    public void testIncludes() {
        final Collection<String> patterns = new ArrayList<String>() {{
            add( "git:
            add( "**/reporeporeporeporeporeporeporepo/**" );
        }};
        Assert.assertTrue( excludes( patterns, URI.create( "file:
        Assert.assertFalse( excludes( patterns, URI.create( "git:
        Assert.assertTrue( excludes( patterns, URI.create( "git:
    }
    @Test
    public void testFilterUri() {
        final Collection<String> includes = new ArrayList<String>() {{
            add( "git:
        }};
        final Collection<String> excludes = new ArrayList<String>() {{
            add( "file:
        }};
        Assert.assertFalse( filter( includes, excludes, URI.create( "file:
        Assert.assertTrue( filter( includes, excludes, URI.create( "git:
        Assert.assertTrue( filter( includes, excludes, URI.create( "git:
        Assert.assertTrue( filter( Collections.<String>emptyList(), Collections.<String>emptyList(), URI.create( "file:
        Assert.assertTrue( filter( Collections.<String>emptyList(), Collections.<String>emptyList(), URI.create( "git:
    }
}
