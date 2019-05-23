package sank.xbook.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class HistoryRecord {
    @Id(autoincrement = true)
    private Long id;
    private String time;
    private String image;
    private String bookName;
    private String bookAuthor;
    private String bookType;
    private String bookSynopsis;
    private String bookUpdateTime;
    @Generated(hash = 1505734338)
    public HistoryRecord(Long id, String time, String image, String bookName,
            String bookAuthor, String bookType, String bookSynopsis,
            String bookUpdateTime) {
        this.id = id;
        this.time = time;
        this.image = image;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookType = bookType;
        this.bookSynopsis = bookSynopsis;
        this.bookUpdateTime = bookUpdateTime;
    }
    @Generated(hash = 725453896)
    public HistoryRecord() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getImage() {
        return this.image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getBookName() {
        return this.bookName;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
    public String getBookAuthor() {
        return this.bookAuthor;
    }
    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }
    public String getBookType() {
        return this.bookType;
    }
    public void setBookType(String bookType) {
        this.bookType = bookType;
    }
    public String getBookSynopsis() {
        return this.bookSynopsis;
    }
    public void setBookSynopsis(String bookSynopsis) {
        this.bookSynopsis = bookSynopsis;
    }
    public String getBookUpdateTime() {
        return this.bookUpdateTime;
    }
    public void setBookUpdateTime(String bookUpdateTime) {
        this.bookUpdateTime = bookUpdateTime;
    }
}
