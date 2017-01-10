package com.wenld.downloadutils.db;

import com.wenld.downloadutils.bean.ThreadInfo;

import de.greenrobot.dao.AbstractDao;

/**
 * Created by wenld on 2017/1/1.
 */
public class ThreadInfoDB extends AbstractDatabaseManager<ThreadInfo,String> {
    @Override
    AbstractDao<ThreadInfo, String> getAbstractDao() {
        return daoSession.getThreadInfoDao();
    }
}
