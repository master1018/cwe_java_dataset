public class TrustedClass {
...
@Override
public boolean equals(Object obj) {
boolean isEquals = false;
if (obj.getClass().getName().equals(this.getClass().getName())) {
...
if (...) {
isEquals = true;
}
}
return isEquals;
}
...
}
