# DownLoadUtils
 实现文件 多线程断点下载
 ## 用法
```groovy
compile 'com.github.LidongWen:DownLoadUtils:1.0.1'
```
## 目前对以下需求进行了封装
* 下载文件
* 暂停下载
* 继续下载
* 重新下载
* 文件 DB 操作

##初始化
```java
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DownloadUtils.initDataBase(this);
    }
}
```
##下载文件

```java
  /**
   * 开始下载/继续下载
   */
  DownloadUtils.startDownload(MainActivity.this, item, otherMessage);
```
##暂停
```java
/**
 * 暂停下载
 */
DownloadUtils.stop(item, null);
```

##继续下载
```java
/**
 * 继续下载
 */
DownloadUtils.startDownload(MainActivity.this, item, otherMessage);
```


##继续下载
```java
/**
 * 重新下载
 */
DownloadUtils.ReDownLoad(MainActivity.this, item, otherMessage);
```

## 文件 DB 操作
```java
DownloadUtils.getFileDB().selectByPrimaryKey();
List list= DownloadUtils.getFileDB().getQueryBuilder().where(FileInfoDao.Properties.Id.eq(threeModel.getId())).list();
//等等一系列的数据库操作
```
