
package io.netty.channel;
import io.netty.util.internal.PlatformDependent;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
public class DefaultFileRegionTest {
    private static final byte[] data = new byte[1048576 * 10];
    static {
        PlatformDependent.threadLocalRandom().nextBytes(data);
    }
    private static File newFile() throws IOException {
        File file = File.createTempFile("netty-", ".tmp");
        file.deleteOnExit();
        final FileOutputStream out = new FileOutputStream(file);
        out.write(data);
        out.close();
        return file;
    }
    @Test
    public void testCreateFromFile() throws IOException  {
        File file = newFile();
        try {
            testFileRegion(new DefaultFileRegion(file, 0, data.length));
        } finally {
            file.delete();
        }
    }
    @Test
    public void testCreateFromFileChannel() throws IOException  {
        File file = newFile();
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        try {
            testFileRegion(new DefaultFileRegion(randomAccessFile.getChannel(), 0, data.length));
        } finally {
            randomAccessFile.close();
            file.delete();
        }
    }
    private static void testFileRegion(FileRegion region) throws IOException  {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        WritableByteChannel channel = Channels.newChannel(outputStream);
        try {
            assertEquals(data.length, region.count());
            assertEquals(0, region.transferred());
            assertEquals(data.length, region.transferTo(channel, 0));
            assertEquals(data.length, region.count());
            assertEquals(data.length, region.transferred());
            assertArrayEquals(data, outputStream.toByteArray());
        } finally {
            channel.close();
        }
    }
    @Test
    public void testTruncated() throws IOException  {
        File file = newFile();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        WritableByteChannel channel = Channels.newChannel(outputStream);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        try {
            FileRegion region = new DefaultFileRegion(randomAccessFile.getChannel(), 0, data.length);
            randomAccessFile.getChannel().truncate(data.length - 1024);
            assertEquals(data.length, region.count());
            assertEquals(0, region.transferred());
            assertEquals(data.length - 1024, region.transferTo(channel, 0));
            assertEquals(data.length, region.count());
            assertEquals(data.length - 1024, region.transferred());
            try {
                region.transferTo(channel, data.length - 1024);
                fail();
            } catch (IOException expected) {
            }
        } finally {
            channel.close();
            randomAccessFile.close();
            file.delete();
        }
    }
}
