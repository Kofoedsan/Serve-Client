public class Book {
    private int bookid;
    private String bookTitle;
    private String bookSnippet;


    public Book(int bookid, String bookTitle, String bookSnippet) {
        this.bookid = bookid;
        this.bookTitle = bookTitle;
        this.bookSnippet = bookSnippet;
    }


    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookSnippet() {
        return bookSnippet;
    }

    public void setBookSnippet(String bookSnippet) {
        this.bookSnippet = bookSnippet;
    }
}
