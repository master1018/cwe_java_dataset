
package org.elasticsearch.transport.local;
import org.elasticsearch.Version;
import org.elasticsearch.common.io.ThrowableObjectOutputStream;
import org.elasticsearch.common.io.stream.BytesStreamOutput;
import org.elasticsearch.common.io.stream.HandlesStreamOutput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.transport.*;
import org.elasticsearch.transport.support.TransportStatus;
import java.io.IOException;
import java.io.NotSerializableException;
public class LocalTransportChannel implements TransportChannel {
    private final LocalTransport sourceTransport;
    private final TransportServiceAdapter sourceTransportServiceAdapter;
    private final LocalTransport targetTransport;
    private final String action;
    private final long requestId;
    private final Version version;
    public LocalTransportChannel(LocalTransport sourceTransport, TransportServiceAdapter sourceTransportServiceAdapter, LocalTransport targetTransport, String action, long requestId, Version version) {
        this.sourceTransport = sourceTransport;
        this.sourceTransportServiceAdapter = sourceTransportServiceAdapter;
        this.targetTransport = targetTransport;
        this.action = action;
        this.requestId = requestId;
        this.version = version;
    }
    @Override
    public String action() {
        return action;
    }
    @Override
    public void sendResponse(TransportResponse response) throws IOException {
        sendResponse(response, TransportResponseOptions.EMPTY);
    }
    @Override
    public void sendResponse(TransportResponse response, TransportResponseOptions options) throws IOException {
        BytesStreamOutput bStream = new BytesStreamOutput();
        StreamOutput stream = new HandlesStreamOutput(bStream);
        stream.setVersion(version);
        stream.writeLong(requestId);
        byte status = 0;
        status = TransportStatus.setResponse(status);
        stream.writeByte(status); 
        response.writeTo(stream);
        stream.close();
        final byte[] data = bStream.bytes().toBytes();
        targetTransport.workers().execute(new Runnable() {
            @Override
            public void run() {
                targetTransport.messageReceived(data, action, sourceTransport, version, null);
            }
        });
        sourceTransportServiceAdapter.onResponseSent(requestId, action, response, options);
    }
    @Override
    public void sendResponse(Throwable error) throws IOException {
        BytesStreamOutput stream = new BytesStreamOutput();
        if (ThrowableObjectOutputStream.canSerialize(error) == false) {
            assert false : "Can not serialize exception: " + error; 
            error = new NotSerializableTransportException(error);
        }
        try {
            writeResponseExceptionHeader(stream);
            RemoteTransportException tx = new RemoteTransportException(targetTransport.nodeName(), targetTransport.boundAddress().boundAddress(), action, error);
            ThrowableObjectOutputStream too = new ThrowableObjectOutputStream(stream);
            too.writeObject(tx);
            too.close();
        } catch (NotSerializableException e) {
            stream.reset();
            writeResponseExceptionHeader(stream);
            RemoteTransportException tx = new RemoteTransportException(targetTransport.nodeName(), targetTransport.boundAddress().boundAddress(), action, new NotSerializableTransportException(error));
            ThrowableObjectOutputStream too = new ThrowableObjectOutputStream(stream);
            too.writeObject(tx);
            too.close();
        }
        final byte[] data = stream.bytes().toBytes();
        targetTransport.workers().execute(new Runnable() {
            @Override
            public void run() {
                targetTransport.messageReceived(data, action, sourceTransport, version, null);
            }
        });
        sourceTransportServiceAdapter.onResponseSent(requestId, action, error);
    }
    private void writeResponseExceptionHeader(BytesStreamOutput stream) throws IOException {
        stream.writeLong(requestId);
        byte status = 0;
        status = TransportStatus.setResponse(status);
        status = TransportStatus.setError(status);
        stream.writeByte(status);
    }
}
