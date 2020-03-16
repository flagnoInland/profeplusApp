package com.equipu.profeplus.controllers;

import android.content.Context;

import com.equipu.profeplus.LearnApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Herbert Caller on 21/04/2018.
 */
public class BaseController {

    public static final int DONE = 0;
    public static final int FAILED = 1;
    public static final int CONNECTION = 2;
    public static final int NOT_FOUND = 3;
    public static final int CANCEL = 4;

    private boolean CONFIG_NETWORK = LearnApp.SETUP_NETWORK;
    public final static int TIMEOUT = 60000;// 60 seconds
    public final static int READTIMEOUT = 10000; /// 10 seconds
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    int status;
    Context context;
    OkHttpClient client = new OkHttpClient();
    InputStream inputStream;
    BufferedReader bufferedReader;

    Request request;
    Response response;
    RequestBody body;
    boolean socketOn = true;

    public BaseController() {
        try {
            client = new OkHttpClient.Builder()
                    .sslSocketFactory(new AppSSLSocketFactory(), new AppX509TrustManager())
                    .build();
        } catch (Exception e) {}

    }

    public void configConnection(HttpURLConnection httpURLConnection){
        if (CONFIG_NETWORK){
            //httpURLConnection.setConnectTimeout(TIMEOUT);
            httpURLConnection.setReadTimeout(READTIMEOUT);
            httpURLConnection.setRequestProperty("User-Agent", LearnApp.mAgent);
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Keep-Alive","timeout=5, max=50");
        }
    }

    public class AppX509TrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    public class AppSSLSocketFactory extends SSLSocketFactory {

        private SSLSocketFactory baseSSLSocketFactory;

        public AppSSLSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
            baseSSLSocketFactory = sslContext.getSocketFactory();
        }

        private Socket enableTLSOnSocket(Socket socket) {
            if(socket != null && (socket instanceof SSLSocket)) {
                ((SSLSocket)socket).setEnabledProtocols(new String[] {"TLSv1.1", "TLSv1.2"});
            }
            return socket;
        }

        @Override
        public String[] getDefaultCipherSuites() {
            return baseSSLSocketFactory.getDefaultCipherSuites();
        }

        @Override
        public String[] getSupportedCipherSuites() {
            return baseSSLSocketFactory.getSupportedCipherSuites();
        }

        @Override
        public Socket createSocket(Socket socket, String s, int i, boolean b) throws IOException {
            return enableTLSOnSocket(baseSSLSocketFactory.createSocket(socket, s, i, b));
        }

        @Override
        public Socket createSocket(String s, int i) throws IOException, UnknownHostException {
            return enableTLSOnSocket(baseSSLSocketFactory.createSocket(s, i));
        }

        @Override
        public Socket createSocket(String s, int i, InetAddress inetAddress, int i1) throws IOException, UnknownHostException {
            return enableTLSOnSocket(baseSSLSocketFactory.createSocket(s, i, inetAddress, i1));
        }

        @Override
        public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
            return enableTLSOnSocket(baseSSLSocketFactory.createSocket(inetAddress, i));
        }

        @Override
        public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress1, int i1) throws IOException {
            return enableTLSOnSocket(baseSSLSocketFactory.createSocket(inetAddress, i, inetAddress1, i1));
        }
    }
}
