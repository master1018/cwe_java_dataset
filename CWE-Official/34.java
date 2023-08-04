public class OuterClass {
private String memberOne;
private String memberTwo;
public OuterClass(String varOne, String varTwo) {
this.memberOne = varOne;
this.memberTwo = varTwo;
}
private class InnerClass {
private String innerMemberOne;
public InnerClass(String innerVarOne) {
this.innerMemberOne = innerVarOne;
}
public String concat(String separator) {
System.out.println("Value of memberOne is: " + memberOne);
return OuterClass.this.memberTwo + separator + this.innerMemberOne;
}
}
}
