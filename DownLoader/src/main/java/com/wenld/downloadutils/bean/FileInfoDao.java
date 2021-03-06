package com.wenld.downloadutils.bean;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "FILE_INFO".
*/
public class FileInfoDao extends AbstractDao<FileInfo, String> {

    public static final String TABLENAME = "FILE_INFO";

    /**
     * Properties of entity FileInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property Length = new Property(1, Integer.class, "length", false, "LENGTH");
        public final static Property Url = new Property(2, String.class, "url", false, "URL");
        public final static Property FileName = new Property(3, String.class, "fileName", false, "FILE_NAME");
        public final static Property Md5 = new Property(4, String.class, "md5", false, "MD5");
        public final static Property Finished = new Property(5, Integer.class, "finished", false, "FINISHED");
        public final static Property Rate = new Property(6, String.class, "rate", false, "RATE");
        public final static Property Over = new Property(7, Boolean.class, "over", false, "OVER");
        public final static Property Overtime = new Property(8, String.class, "overtime", false, "OVERTIME");
        public final static Property IsDownload = new Property(9, boolean.class, "isDownload", false, "IS_DOWNLOAD");
    };


    public FileInfoDao(DaoConfig config) {
        super(config);
    }
    
    public FileInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"FILE_INFO\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "\"LENGTH\" INTEGER," + // 1: length
                "\"URL\" TEXT NOT NULL ," + // 2: url
                "\"FILE_NAME\" TEXT NOT NULL ," + // 3: fileName
                "\"MD5\" TEXT NOT NULL ," + // 4: md5
                "\"FINISHED\" INTEGER," + // 5: finished
                "\"RATE\" TEXT," + // 6: rate
                "\"OVER\" INTEGER," + // 7: over
                "\"OVERTIME\" TEXT," + // 8: overtime
                "\"IS_DOWNLOAD\" INTEGER NOT NULL );"); // 9: isDownload
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"FILE_INFO\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, FileInfo entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        Integer length = entity.getLength();
        if (length != null) {
            stmt.bindLong(2, length);
        }
        stmt.bindString(3, entity.getUrl());
        stmt.bindString(4, entity.getFileName());
        stmt.bindString(5, entity.getMd5());
 
        Integer finished = entity.getFinished();
        if (finished != null) {
            stmt.bindLong(6, finished);
        }
 
        String rate = entity.getRate();
        if (rate != null) {
            stmt.bindString(7, rate);
        }
 
        Boolean over = entity.getOver();
        if (over != null) {
            stmt.bindLong(8, over ? 1L: 0L);
        }
 
        String overtime = entity.getOvertime();
        if (overtime != null) {
            stmt.bindString(9, overtime);
        }
        stmt.bindLong(10, entity.getIsDownload() ? 1L: 0L);
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public FileInfo readEntity(Cursor cursor, int offset) {
        FileInfo entity = new FileInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // length
            cursor.getString(offset + 2), // url
            cursor.getString(offset + 3), // fileName
            cursor.getString(offset + 4), // md5
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // finished
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // rate
            cursor.isNull(offset + 7) ? null : cursor.getShort(offset + 7) != 0, // over
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // overtime
            cursor.getShort(offset + 9) != 0 // isDownload
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, FileInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setLength(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setUrl(cursor.getString(offset + 2));
        entity.setFileName(cursor.getString(offset + 3));
        entity.setMd5(cursor.getString(offset + 4));
        entity.setFinished(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setRate(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setOver(cursor.isNull(offset + 7) ? null : cursor.getShort(offset + 7) != 0);
        entity.setOvertime(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setIsDownload(cursor.getShort(offset + 9) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(FileInfo entity, long rowId) {
        return entity.getId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(FileInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
