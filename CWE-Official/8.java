public class cwe {
    public void updateSalesForProduct(String productID, int amountSold) {
        int productCount = inventory.getProductCount(productID);
        short count = (short) productCount;
        short sold = (short) amountSold;
        sales.updateSalesCount(productID, count, sold);
    }
}
