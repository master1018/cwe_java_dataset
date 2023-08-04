public class Kibitzer {
    public Object clone() throws CloneNotSupportedException {
        Object returnMe = new Kibitzer();
        // Your additional cloning logic for Kibitzer here
        return returnMe;
    }
}

public class FancyKibitzer extends Kibitzer {
    public Object clone() throws CloneNotSupportedException {
        Object returnMe = super.clone();
        // Your additional cloning logic for FancyKibitzer here
        return returnMe;
    }
}
