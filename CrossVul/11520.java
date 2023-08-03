
package org.opencastproject.mediapackage.identifier;
import java.util.regex.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
@XmlType
@XmlAccessorType(XmlAccessType.NONE)
public class IdImpl implements Id {
  private static final Pattern pattern = Pattern.compile("[\\w-_.:;()]+");
  @XmlValue
  protected String id = null;
  public IdImpl() {
  }
  public IdImpl(final String id) {
    if (!pattern.matcher(id).matches()) {
      throw new IllegalArgumentException("Id must match " + pattern);
    }
    this.id = id;
  }
  public String compact() {
    return toString();
  }
  @Override
  public String toString() {
    return id;
  }
  @Override
  public boolean equals(Object o) {
    if (o instanceof IdImpl) {
      IdImpl other = (IdImpl) o;
      return id != null && other.id != null && id.equals(other.id);
    }
    return false;
  }
  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
