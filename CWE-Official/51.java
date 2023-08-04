@Stateless
public class StockSymbolBean extends Thread implements StockSymbolRemote {
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    boolean listening = false;

    public StockSymbolBean() {
        try {
            serverSocket = new ServerSocket(Constants.SOCKET_PORT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        listening = true;
        while (listening) {
            start();
        }
    }

    public String getStockSymbol(String name) {
        // Replace with implementation
        return null;
    }

    public BigDecimal getStockValue(String symbol) {
        // Replace with implementation
        return null;
    }

    public void run() {
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Replace with implementation

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String request = reader.readLine();
            String response = processRequest(request);

            writer.println(response);

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String processRequest(String request) {
        // Replace with implementation
        return null;
    }
}
