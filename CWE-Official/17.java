public class cwe {
    Public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // do somethings
        } catch (ApplicationSpecificException ase) {
        logger.error("Caught: " + ase.toString());
        System.exit(1);
        }
    }
}