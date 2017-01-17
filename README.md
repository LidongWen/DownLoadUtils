# DownLoadUtils
 实现文件 多线程断点下载<br/>
 基本功能 ：开始下载 暂停下载 等下载功能<br/>
 自定义： Https 证书、设置下载地址、 最大下载文件数量等...

## 示例图
<img width="320" height="520" src="https://github.com/LidongWen/DownLoadUtils/blob/master/img/GIF.gif"></img>
## 示例下载  https://fir.im/downLoad
![](img/download.png)

## 用法
```groovy
// 项目引用
dependencies {
    compile 'com.github.LidongWen:DownLoadUtils:1.0.4'
}

// 根目录下引用
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.1.0'
        classpath 'com.github.dcendents:android-maven-gradle-plugin:1.5'
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        jcenter()
        maven { url "https://www.jitpack.io" }
    }
}
```
## 目前对以下需求进行了封装
* 下载功能
> 开始下载<br/>
 暂停下载<br/>
 继续下载<br/>
 重新下载<br/>
 下载时的数据回传监听

* 文件 DB 操作
 > 数据库 DB 操作;<br/>
  获取下载列表（All）;<br/>
  获取下载列表（暂停/下载中 未完成列表）;<br/>
  获取下载列表（已完成列表）


* 下载设置
 > 设置最大下载文件数量;<br/>
 > 支持 Https 设置证书

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
// 广播的分类
IntentFilter filter = new IntentFilter();
filter.addAction(IntentAction.ACTION_UPDATE);   //
filter.addAction(IntentAction.ACTION_FINISH);   //结束时
filter.addAction(IntentAction.ACTION_PAUSE);    //暂停
filter.addAction(IntentAction.ACTION_FAILE);    //下载失败
filter.addAction(IntentAction.ACTION_WAIT_DownLoad);    //进入下载队列等待下载
registerReceiver(mReceiver, filter);

//数据参数
  /**
     * 【下载进度】的key名称
     */
    public static final String FINISHED_TAG = "finished";
    /**
     * 【下载速率】的key名称
     */
    public static final String DOWNLOAD_RATE_TAG = "rate";
    /**
     * 【文件id】的key
     */
    public static final String ID_Postion = "id";
    /**
     * 【下载文件】的key名称
     */
    public static final String FILEINFO_TAG = "fileInfo";
    /**
     * 【其他信息】
     */
    public static final String OTHER_MESSAGE = "other_Message";

```

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
# Contact me
blog: [wenld's blog](http://blog.csdn.net/sinat_15877283)
