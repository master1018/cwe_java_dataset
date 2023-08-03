
package org.projectforge.web.core;
import java.util.Collection;
import org.apache.commons.lang.ObjectUtils;
public class JsonBuilder
{
  final private StringBuilder sb = new StringBuilder();
  private boolean escapeHtml;
  public static String buildToStringRows(final Collection< ? > col)
  {
    if (col == null) {
      return "[]";
    }
    final JsonBuilder builder = new JsonBuilder();
    return builder.append(col).getAsString();
  }
  public JsonBuilder setEscapeHtml(final boolean escapeHtml)
  {
    this.escapeHtml = escapeHtml;
    return this;
  }
  public String getAsString()
  {
    return sb.toString();
  }
  public JsonBuilder append(final Object[] oArray)
  {
    sb.append(" ["); 
    String separator = "";
    for (final Object obj : oArray) {
      sb.append(separator);
      separator = ",";
      sb.append(escapeString(formatValue(obj)));
    }
    sb.append("]"); 
    return this;
  }
  private String escapeString(final String string)
  {
    if (string == null || string.length() == 0) {
      return "\"\"";
    }
    char c = 0;
    int i;
    final int len = string.length();
    final StringBuilder sb = new StringBuilder(len + 4);
    String t;
    sb.append('"');
    for (i = 0; i < len; i += 1) {
      c = string.charAt(i);
      switch (c) {
        case '\\':
        case '"':
          sb.append('\\');
          sb.append(c);
          break;
        case '/':
          sb.append('\\');
          sb.append(c);
          break;
        case '\b':
          sb.append("\\b");
          break;
        case '\t':
          sb.append("\\t");
          break;
        case '\n':
          sb.append("\\n");
          break;
        case '\f':
          sb.append("\\f");
          break;
        case '\r':
          sb.append("\\r");
          break;
        default:
          if (c < ' ') {
            t = "000" + Integer.toHexString(c);
            sb.append("\\u" + t.substring(t.length() - 4));
          } else {
            if (escapeHtml == true) {
              switch (c) {
                case '<':
                  sb.append("&lt;");
                  break;
                case '>':
                  sb.append("&gt;");
                  break;
                case '&':
                  sb.append("&amp;");
                  break;
                case '"':
                  sb.append("&quot;");
                  break;
                case '\'':
                  sb.append("&#x27;");
                  break;
                case '/':
                  sb.append("&#x2F;");
                  break;
                default:
                  sb.append(c);
              }
            } else {
              sb.append(c);
            }
          }
      }
    }
    sb.append('"');
    return sb.toString();
  }
  public JsonBuilder append(final Collection< ? > col)
  {
    if (col == null) {
      sb.append("[]");
      return this;
    }
    sb.append("[\n");
    String separator = "\n";
    for (final Object os : col) {
      sb.append(separator);
      separator = ",\n";
      if (os instanceof Object[]) { 
        append((Object[]) os);
      } else { 
        append(transform(os));
      }
    }
    sb.append("]"); 
    return this;
  }
  protected String formatValue(final Object obj)
  {
    return ObjectUtils.toString(obj);
  }
  protected JsonBuilder append(final Object obj)
  {
    if (obj instanceof Object[]) {
      return append((Object[]) obj);
    }
    sb.append(" ["); 
    sb.append(escapeString(formatValue(obj)));
    sb.append("]"); 
    return this;
  }
  protected Object transform(final Object obj)
  {
    return obj;
  }
}
