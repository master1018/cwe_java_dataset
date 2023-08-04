public class cwe {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Socket sock = null;
        try {
            sock = new Socket(remoteHostname, 3000);
        } catch (Exception e) {
            // print error
        }
    }
}