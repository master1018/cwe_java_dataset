
package io.netty.handler.codec.http.multipart;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.PlatformDependent;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import static io.netty.util.CharsetUtil.*;
import static org.junit.Assert.*;
public class AbstractMemoryHttpDataTest {
    @Test
    public void testSetContentFromFile() throws Exception {
        TestHttpData test = new TestHttpData("test", UTF_8, 0);
        try {
            File tmpFile = File.createTempFile(UUID.randomUUID().toString(), ".tmp");
            tmpFile.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tmpFile);
            byte[] bytes = new byte[4096];
            PlatformDependent.threadLocalRandom().nextBytes(bytes);
            try {
                fos.write(bytes);
                fos.flush();
            } finally {
                fos.close();
            }
            test.setContent(tmpFile);
            ByteBuf buf = test.getByteBuf();
            assertEquals(buf.readerIndex(), 0);
            assertEquals(buf.writerIndex(), bytes.length);
            assertArrayEquals(bytes, test.get());
            assertArrayEquals(bytes, ByteBufUtil.getBytes(buf));
        } finally {
            test.delete();
        }
    }
    @Test
    public void testRenameTo() throws Exception {
        TestHttpData test = new TestHttpData("test", UTF_8, 0);
        try {
            File tmpFile = File.createTempFile(UUID.randomUUID().toString(), ".tmp");
            tmpFile.deleteOnExit();
            final int totalByteCount = 4096;
            byte[] bytes = new byte[totalByteCount];
            PlatformDependent.threadLocalRandom().nextBytes(bytes);
            ByteBuf content = Unpooled.wrappedBuffer(bytes);
            test.setContent(content);
            boolean succ = test.renameTo(tmpFile);
            assertTrue(succ);
            FileInputStream fis = new FileInputStream(tmpFile);
            try {
                byte[] buf = new byte[totalByteCount];
                int count = 0;
                int offset = 0;
                int size = totalByteCount;
                while ((count = fis.read(buf, offset, size)) > 0) {
                    offset += count;
                    size -= count;
                    if (offset >= totalByteCount || size <= 0) {
                        break;
                    }
                }
                assertArrayEquals(bytes, buf);
                assertEquals(0, fis.available());
            } finally {
                fis.close();
            }
        } finally {
            test.delete();
        }
    }
    @Test
    public void testSetContentFromStream() throws Exception {
        TestHttpData test = new TestHttpData("test", UTF_8, 0);
        String contentStr = "foo_test";
        ByteBuf buf = Unpooled.wrappedBuffer(contentStr.getBytes(UTF_8));
        buf.markReaderIndex();
        ByteBufInputStream is = new ByteBufInputStream(buf);
        try {
            test.setContent(is);
            assertFalse(buf.isReadable());
            assertEquals(test.getString(UTF_8), contentStr);
            buf.resetReaderIndex();
            assertTrue(ByteBufUtil.equals(buf, test.getByteBuf()));
        } finally {
            is.close();
        }
        Random random = new SecureRandom();
        for (int i = 0; i < 20; i++) {
            int size = random.nextInt(Short.MAX_VALUE);
            byte[] bytes = new byte[size];
            random.nextBytes(bytes);
            TestHttpData data = new TestHttpData("name", UTF_8, 0);
            data.setContent(new ByteArrayInputStream(bytes));
            ByteBuf buffer = data.getByteBuf();
            assertEquals(0, buffer.readerIndex());
            assertEquals(bytes.length, buffer.writerIndex());
            assertArrayEquals(bytes, Arrays.copyOf(buffer.array(), bytes.length));
            assertArrayEquals(bytes, data.get());
        }
    }
    private static final class TestHttpData extends AbstractMemoryHttpData {
        private TestHttpData(String name, Charset charset, long size) {
            super(name, charset, size);
        }
        @Override
        public InterfaceHttpData.HttpDataType getHttpDataType() {
            throw reject();
        }
        @Override
        public HttpData copy() {
            throw reject();
        }
        @Override
        public HttpData duplicate() {
            throw reject();
        }
        @Override
        public HttpData retainedDuplicate() {
            throw reject();
        }
        @Override
        public HttpData replace(ByteBuf content) {
            return null;
        }
        @Override
        public int compareTo(InterfaceHttpData o) {
            throw reject();
        }
        @Override
        public int hashCode() {
            return super.hashCode();
        }
        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
        private static UnsupportedOperationException reject() {
            throw new UnsupportedOperationException("Should never be called.");
        }
    }
}
