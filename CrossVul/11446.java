
package io.netty.handler.codec.http;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedInput;
import io.netty.handler.stream.ChunkedNioFile;
import io.netty.handler.stream.ChunkedNioStream;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.internal.PlatformDependent;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import static org.junit.Assert.*;
public class HttpChunkedInputTest {
    private static final byte[] BYTES = new byte[1024 * 64];
    private static final File TMP;
    static {
        for (int i = 0; i < BYTES.length; i++) {
            BYTES[i] = (byte) i;
        }
        FileOutputStream out = null;
        try {
            TMP = PlatformDependent.createTempFile("netty-chunk-", ".tmp", null);
            TMP.deleteOnExit();
            out = new FileOutputStream(TMP);
            out.write(BYTES);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }
    @Test
    public void testChunkedStream() {
        check(new HttpChunkedInput(new ChunkedStream(new ByteArrayInputStream(BYTES))));
    }
    @Test
    public void testChunkedNioStream() {
        check(new HttpChunkedInput(new ChunkedNioStream(Channels.newChannel(new ByteArrayInputStream(BYTES)))));
    }
    @Test
    public void testChunkedFile() throws IOException {
        check(new HttpChunkedInput(new ChunkedFile(TMP)));
    }
    @Test
    public void testChunkedNioFile() throws IOException {
        check(new HttpChunkedInput(new ChunkedNioFile(TMP)));
    }
    @Test
    public void testWrappedReturnNull() throws Exception {
        HttpChunkedInput input = new HttpChunkedInput(new ChunkedInput<ByteBuf>() {
            @Override
            public boolean isEndOfInput() throws Exception {
                return false;
            }
            @Override
            public void close() throws Exception {
            }
            @Override
            public ByteBuf readChunk(ChannelHandlerContext ctx) throws Exception {
                return null;
            }
            @Override
            public ByteBuf readChunk(ByteBufAllocator allocator) throws Exception {
                return null;
            }
            @Override
            public long length() {
                return 0;
            }
            @Override
            public long progress() {
                return 0;
            }
        });
        assertNull(input.readChunk(ByteBufAllocator.DEFAULT));
    }
    private static void check(ChunkedInput<?>... inputs) {
        EmbeddedChannel ch = new EmbeddedChannel(new ChunkedWriteHandler());
        for (ChunkedInput<?> input : inputs) {
            ch.writeOutbound(input);
        }
        assertTrue(ch.finish());
        int i = 0;
        int read = 0;
        HttpContent lastHttpContent = null;
        for (;;) {
            HttpContent httpContent = ch.readOutbound();
            if (httpContent == null) {
                break;
            }
            if (lastHttpContent != null) {
                assertTrue("Chunk must be DefaultHttpContent", lastHttpContent instanceof DefaultHttpContent);
            }
            ByteBuf buffer = httpContent.content();
            while (buffer.isReadable()) {
                assertEquals(BYTES[i++], buffer.readByte());
                read++;
                if (i == BYTES.length) {
                    i = 0;
                }
            }
            buffer.release();
            lastHttpContent = httpContent;
        }
        assertEquals(BYTES.length * inputs.length, read);
        assertSame("Last chunk must be LastHttpContent.EMPTY_LAST_CONTENT",
                LastHttpContent.EMPTY_LAST_CONTENT, lastHttpContent);
    }
}
