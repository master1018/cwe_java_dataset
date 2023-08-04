@Stateless
public class StockSymbolBean implements StockSymbolRemote {
    ServerSocket serverSocket = null;
    Socket clientSocket = null;

    public StockSymbolBean() {
        try {
            serverSocket = new ServerSocket(Constants.SOCKET_PORT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
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

    private void processClientInputFromSocket() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                // Process input from the clientSocket
                // Handle the input as needed
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
