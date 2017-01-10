package com.wenld.downloadutils.db.dbhelper;


import android.database.sqlite.SQLiteDatabase;

/**
 * Created by wenld- on 2015/12/24.
 */
public abstract class AbstractMigratorHelper {
    public abstract void onUpgrade(SQLiteDatabase db);
}
