# DownLoadUtils
 实现文件 多线程断点下载

## 示例图

<img width="320" height="548" src="https://github.com/LidongWen/DownLoadUtils/blob/master/img/GIF.gif"></img>

## 用法

```groovy
dependencies {
    compile 'com.github.LidongWen:DownLoadUtils:1.0.3'
}
```
## 目前对以下需求进行了封装
* 开始下载
* 暂停下载
* 继续下载
* 重新下载
* 下载时的数据回传监听
* 文件 DB 操作
 > 数据库 DB 操作
 > 获取下载列表（All）
 > 获取下载列表（暂停||下载中 未完成列表）
 > 获取下载列表（已完成列表）


* 下载设置
 > 设置最大下载文件数量
 > 支持 Https

##初始化
```java
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DownloadUtils.initDataBase(this);
 //       DownloadUtils.changeFileDir(fileDir);//设置默认地址
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


##重新下载
```java
/**
 * 重新下载
 */
DownloadUtils.ReDownLoad(MainActivity.this, item, otherMessage);
```
## 下载时的数据回传监听
实时 获取下载文件的进度,这边通过广播来接收下载进度
```java
/**
 * 如果想知道下载的情况，需要注册该广播
 */
// 注册广播接收器，接收下载进度信息和结束信息
IntentFilter filter = new IntentFilter();
filter.addAction(IntentAction.ACTION_UPDATE);   //
filter.addAction(IntentAction.ACTION_FINISH);   //结束时
filter.addAction(IntentAction.ACTION_PAUSE);    //暂停
filter.addAction(IntentAction.ACTION_FAILE);    //下载失败
filter.addAction(IntentAction.ACTION_WAIT_DownLoad);    //进入下载队列等待下载
registerReceiver(mReceiver, filter);


## 文件 DB 操作
```java
DownloadUtils.getFileDB().selectByPrimaryKey();
List list= DownloadUtils.getFileDB().getQueryBuilder().where(FileInfoDao.Properties.Id.eq(threeModel.getId())).list();
//等等一系列的数据库操作
```
## 获取下载列表
```java
DownloadUtils.getAllFileInfos();
```

## 获取下载列表（暂停||下载中 未完成列表）
```java
DownloadUtils.getFileInfosByDownLoading();
```
## 获取下载列表（已完成列表）
```java
DownloadUtils.getFileInfosByFinished();
```

## 支持 Https
* 设置支持所有的https
```java
DownloadUtils.setSsl(null,null,null);
```
* 设置具体的证书
```java
DownloadUtils.setSsl(证书的inputstream, null, null);
```
* 双向认证
```java
DownloadUtils.setSsl(证书的inputstream,
    本地证书的inputstream,
    本地证书的密码)
```

