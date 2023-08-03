
package com.facebook.buck.parser;
import com.facebook.buck.parser.thrift.RemoteDaemonicParserState;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.HashSet;
import java.util.Set;
public class ParserStateObjectInputStream extends ObjectInputStream {
  private Set<String> whitelist;
  public ParserStateObjectInputStream(InputStream inputStream) throws IOException {
    super(inputStream);
    whitelist = new HashSet<>();
    whitelist.add(RemoteDaemonicParserState.class.getName());
  }
  @Override
  protected Class<?> resolveClass(ObjectStreamClass desc)
      throws IOException, ClassNotFoundException {
    if (!whitelist.contains(desc.getName())) {
      throw new InvalidClassException(desc.getName(), "Can't deserialize this class");
    }
    return super.resolveClass(desc);
  }
}
