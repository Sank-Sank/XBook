package sank.xbook.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import sank.xbook.database.greendao.DaoSession;
import sank.xbook.database.greendao.ChaptersDao;
import sank.xbook.database.greendao.BooksDao;

@Entity
public class Books{
    @Id
    private Long id;
    private String bookName;
    private String bookType;
    private String bookAuthor;
    private String bookSynopsis;
    private String bookImage;
    private String updateTime;
    @ToMany(referencedJoinProperty = "customId")
    private List<Chapters> chapters;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 843792491)
    private transient BooksDao myDao;
    @Generated(hash = 774424706)
    public Books(Long id, String bookName, String bookType, String bookAuthor,
            String bookSynopsis, String bookImage, String updateTime) {
        this.id = id;
        this.bookName = bookName;
        this.bookType = bookType;
        this.bookAuthor = bookAuthor;
        this.bookSynopsis = bookSynopsis;
        this.bookImage = bookImage;
        this.updateTime = updateTime;
    }
    @Generated(hash = 2016280518)
    public Books() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBookName() {
        return this.bookName;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
    public String getBookType() {
        return this.bookType;
    }
    public void setBookType(String bookType) {
        this.bookType = bookType;
    }
    public String getBookAuthor() {
        return this.bookAuthor;
    }
    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }
    public String getBookSynopsis() {
        return this.bookSynopsis;
    }
    public void setBookSynopsis(String bookSynopsis) {
        this.bookSynopsis = bookSynopsis;
    }
    public String getBookImage() {
        return this.bookImage;
    }
    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }
    public String getUpdateTime() {
        return this.updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1232387490)
    public List<Chapters> getChapters() {
        if (chapters == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ChaptersDao targetDao = daoSession.getChaptersDao();
            List<Chapters> chaptersNew = targetDao._queryBooks_Chapters(id);
            synchronized (this) {
                if (chapters == null) {
                    chapters = chaptersNew;
                }
            }
        }
        return chapters;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 936914273)
    public synchronized void resetChapters() {
        chapters = null;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 910064890)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBooksDao() : null;
    }
}
