
package io.micronaut.http.netty;
import io.micronaut.core.annotation.Internal;
import io.micronaut.core.convert.ArgumentConversionContext;
import io.micronaut.core.convert.ConversionService;
import io.micronaut.http.MutableHttpHeaders;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
@Internal
public class NettyHttpHeaders implements MutableHttpHeaders {
    io.netty.handler.codec.http.HttpHeaders nettyHeaders;
    final ConversionService<?> conversionService;
    public NettyHttpHeaders(io.netty.handler.codec.http.HttpHeaders nettyHeaders, ConversionService conversionService) {
        this.nettyHeaders = nettyHeaders;
        this.conversionService = conversionService;
    }
    public NettyHttpHeaders() {
        this.nettyHeaders = new DefaultHttpHeaders(false);
        this.conversionService = ConversionService.SHARED;
    }
    public io.netty.handler.codec.http.HttpHeaders getNettyHeaders() {
        return nettyHeaders;
    }
    void setNettyHeaders(io.netty.handler.codec.http.HttpHeaders headers) {
        this.nettyHeaders = headers;
    }
    @Override
    public <T> Optional<T> get(CharSequence name, ArgumentConversionContext<T> conversionContext) {
        List<String> values = nettyHeaders.getAll(name);
        if (values.size() > 0) {
            if (values.size() == 1 || !isCollectionOrArray(conversionContext.getArgument().getType())) {
                return conversionService.convert(values.get(0), conversionContext);
            } else {
                return conversionService.convert(values, conversionContext);
            }
        }
        return Optional.empty();
    }
    private boolean isCollectionOrArray(Class<?> clazz) {
        return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
    }
    @Override
    public List<String> getAll(CharSequence name) {
        return nettyHeaders.getAll(name);
    }
    @Override
    public Set<String> names() {
        return nettyHeaders.names();
    }
    @Override
    public Collection<List<String>> values() {
        Set<String> names = names();
        List<List<String>> values = new ArrayList<>();
        for (String name : names) {
            values.add(getAll(name));
        }
        return Collections.unmodifiableList(values);
    }
    @Override
    public String get(CharSequence name) {
        return nettyHeaders.get(name);
    }
    @Override
    public MutableHttpHeaders add(CharSequence header, CharSequence value) {
        nettyHeaders.add(header, value);
        return this;
    }
    @Override
    public MutableHttpHeaders remove(CharSequence header) {
        nettyHeaders.remove(header);
        return this;
    }
}
