public class BookStore {
private BookStoreInventory inventory;
private SalesDBManager sales;
public BookStore() {
this.inventory = new BookStoreInventory();
this.sales = new SalesDBManager();
}
public void updateSalesAndInventoryForBookSold(String bookISBN) {
Book book = inventory.getBookWithISBN(bookISBN);
sales.updateSalesInformation(book);
inventory.updateInventory(book);
}
}
public class Book {
private String title;
private String author;
private String isbn;
}
