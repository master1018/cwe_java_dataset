public class cwe {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    Runnable r = new Runnable() {
    public void run() {
        return;
    }
    }
    new Thread(r).start();
    }
}
