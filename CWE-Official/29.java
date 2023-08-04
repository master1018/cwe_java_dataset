public class TrustedClass {
    private String data;

    public TrustedClass(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEquals = false;
        if (obj.getClass().getName().equals(this.getClass().getName())) {
            TrustedClass other = (TrustedClass) obj; // Cast obj to TrustedClass
            if (this.data.equals(other.data)) {       // Compare data fields
                isEquals = true;
            }
        }
        return isEquals;
    }

    public static void main(String[] args) {
        TrustedClass obj1 = new TrustedClass("Hello");
        TrustedClass obj2 = new TrustedClass("Hello");
        TrustedClass obj3 = new TrustedClass("World");

        System.out.println(obj1.equals(obj2)); // Should print true
        System.out.println(obj1.equals(obj3)); // Should print false
    }
}
