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
        // Implementation for getting stock symbol based on the name
        return stockSymbol;
    }

    public BigDecimal getStockValue(String symbol) {
        // Implementation for getting stock value based on the symbol
        return stockValue;
    }

    public void run() {
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Handle clientSocket communication here
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
        // Your logic to process the client's request and generate a response
        String response = "Processed: " + request;
        return response;
    }
}