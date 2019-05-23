package sank.xbook.database.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import sank.xbook.database.Books;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BOOKS".
*/
public class BooksDao extends AbstractDao<Books, Long> {

    public static final String TABLENAME = "BOOKS";

    /**
     * Properties of entity Books.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property BookName = new Property(1, String.class, "bookName", false, "BOOK_NAME");
        public final static Property BookType = new Property(2, String.class, "bookType", false, "BOOK_TYPE");
        public final static Property BookAuthor = new Property(3, String.class, "bookAuthor", false, "BOOK_AUTHOR");
        public final static Property BookSynopsis = new Property(4, String.class, "bookSynopsis", false, "BOOK_SYNOPSIS");
        public final static Property BookImage = new Property(5, String.class, "bookImage", false, "BOOK_IMAGE");
        public final static Property UpdateTime = new Property(6, String.class, "updateTime", false, "UPDATE_TIME");
    }

    private DaoSession daoSession;


    public BooksDao(DaoConfig config) {
        super(config);
    }
    
    public BooksDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BOOKS\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"BOOK_NAME\" TEXT," + // 1: bookName
                "\"BOOK_TYPE\" TEXT," + // 2: bookType
                "\"BOOK_AUTHOR\" TEXT," + // 3: bookAuthor
                "\"BOOK_SYNOPSIS\" TEXT," + // 4: bookSynopsis
                "\"BOOK_IMAGE\" TEXT," + // 5: bookImage
                "\"UPDATE_TIME\" TEXT);"); // 6: updateTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BOOKS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Books entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String bookName = entity.getBookName();
        if (bookName != null) {
            stmt.bindString(2, bookName);
        }
 
        String bookType = entity.getBookType();
        if (bookType != null) {
            stmt.bindString(3, bookType);
        }
 
        String bookAuthor = entity.getBookAuthor();
        if (bookAuthor != null) {
            stmt.bindString(4, bookAuthor);
        }
 
        String bookSynopsis = entity.getBookSynopsis();
        if (bookSynopsis != null) {
            stmt.bindString(5, bookSynopsis);
        }
 
        String bookImage = entity.getBookImage();
        if (bookImage != null) {
            stmt.bindString(6, bookImage);
        }
 
        String updateTime = entity.getUpdateTime();
        if (updateTime != null) {
            stmt.bindString(7, updateTime);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Books entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String bookName = entity.getBookName();
        if (bookName != null) {
            stmt.bindString(2, bookName);
        }
 
        String bookType = entity.getBookType();
        if (bookType != null) {
            stmt.bindString(3, bookType);
        }
 
        String bookAuthor = entity.getBookAuthor();
        if (bookAuthor != null) {
            stmt.bindString(4, bookAuthor);
        }
 
        String bookSynopsis = entity.getBookSynopsis();
        if (bookSynopsis != null) {
            stmt.bindString(5, bookSynopsis);
        }
 
        String bookImage = entity.getBookImage();
        if (bookImage != null) {
            stmt.bindString(6, bookImage);
        }
 
        String updateTime = entity.getUpdateTime();
        if (updateTime != null) {
            stmt.bindString(7, updateTime);
        }
    }

    @Override
    protected final void attachEntity(Books entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Books readEntity(Cursor cursor, int offset) {
        Books entity = new Books( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // bookName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // bookType
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // bookAuthor
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // bookSynopsis
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // bookImage
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // updateTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Books entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setBookName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setBookType(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setBookAuthor(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setBookSynopsis(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setBookImage(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setUpdateTime(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Books entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Books entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Books entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}