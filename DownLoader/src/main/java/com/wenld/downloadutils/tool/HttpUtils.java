package com.wenld.downloadutils.tool;

import android.net.Uri;
import android.os.Build;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpUtils {
    protected static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    protected static final String CHARSET = "UTF-8";
    /**
     * 建立连接的超时时间
     */
    protected static final int connectTimeout = 5 * 1000;
    /**
     * 建立到资源的连接后从 input 流读入时的超时时间
     */
    protected static final int readTimeout = 10 * 1000;

    private static HttpUtils instance;

    public static void setSslParams(SSLParams sslParams) {
        HttpUtils.sslParams = sslParams;
    }

    private static SSLParams sslParams;
//    private TrustManager[] trustAllCerts = {new X509TrustManager() {
//
//        public X509Certificate[] getAcceptedIssuers() {
//            return new java.security.cert.X509Certificate[]{};
//        }
//
//        public void checkClientTrusted(X509Certificate[] certs, String authType) {
//
//        }
//
//        public void checkServerTrusted(X509Certificate[] certs, String authType) {
//
//        }
//    }};

    public static HttpUtils getInstance() {
        if (instance == null) {
            synchronized (HttpUtils.class) {
                if (instance == null) {
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }

//
//    public void setSSL(InputStream caInput) {
//        try {
//            CertificateFactory cf = null;
//            cf = CertificateFactory.getInstance("X.509");
//            Certificate ca;
//            ca = cf.generateCertificate(caInput);
//            String keyStoreType = KeyStore.getDefaultType();
//            KeyStore keyStore = null;
//            keyStore = KeyStore.getInstance(keyStoreType);
//            keyStore.load(null, null);
//            keyStore.setCertificateEntry("ca", ca);
//
//            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//            tmf.init(keyStore);
//
//            context = SSLContext.getInstance("TLS");
//            context.init(null, tmf.getTrustManagers(), null);
//
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private HttpURLConnection setVerifier(URL ur) {
        HttpsURLConnection conn = null;
        try {
            conn = (HttpsURLConnection) ur
                    .openConnection();
            if (sslParams == null) {
                sslParams = SSLParams.getSslSocketFactory(null, null, null);
            }
            conn.setDefaultHostnameVerifier(new SSLParams.UnSafeHostnameVerifier());
            conn.setDefaultSSLSocketFactory(sslParams.sSLSocketFactory);
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public HttpURLConnection createConnection(String url) throws IOException {
        String encodedUrl = Uri.encode(url, ALLOWED_URI_CHARS);
        URL ur = new URL(encodedUrl);
        if (ur.getProtocol().toLowerCase().equals("https")) {
            return setVerifier(ur);
        }
        HttpURLConnection conn = (HttpURLConnection) ur
                .openConnection();
        conn.setConnectTimeout(connectTimeout);
        conn.setReadTimeout(readTimeout);
        return conn;
    }

    public InputStream getInputStream(String url) {
        InputStream is = null;

        try {
            HttpURLConnection conn = createConnection(url);
            conn.setRequestMethod("GET");
            is = conn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return is;
    }

    public String getString(String url) {
        String result = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            is = getInputStream(url);
            br = new BufferedReader(new InputStreamReader(is, CHARSET));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
            }
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
        }

        return result;
    }

    public String postString(String url, String params) {
        String result = null;
        OutputStream os = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            HttpURLConnection conn = createConnection(url);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);// POST方式不能缓存数据
            // conn.setRequestProperty(field, newValue);//header

            conn.setRequestProperty("Content-Type", "application/json; charset=" + CHARSET);

            // // 设置请求的头
            // conn.setRequestProperty("Connection", "keep-alive");
            // // 设置请求的头
            // conn.setRequestProperty("User-Agent",
            // "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");

            if (params != null) {
                os = conn.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                dos.write(params.getBytes(CHARSET));
                dos.flush();
                dos.close();
            }

            is = conn.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, CHARSET));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
            }
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
            }
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
        }

        return result;
    }

    protected boolean shouldBeProcessed(HttpURLConnection conn)
            throws IOException {
        return conn.getResponseCode() == 200;
    }

    protected void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }
}