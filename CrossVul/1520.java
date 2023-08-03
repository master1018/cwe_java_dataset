
package org.opencastproject.mediapackage.identifier;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
@XmlType
@XmlAccessorType(XmlAccessType.NONE)
public class IdImpl implements Id {
  @XmlValue
  protected String id = null;
  public IdImpl() {
  }
  public IdImpl(String id) {
    this.id = id;
  }
  public String compact() {
    return id.replaceAll("/", "-").replaceAll("\\\\", "-");
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
