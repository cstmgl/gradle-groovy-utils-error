package com.cstmgl.gradle

import javax.net.ssl.X509TrustManager
import java.security.cert.X509Certificate

class TrustManager implements X509TrustManager {
    X509Certificate[] getAcceptedIssuers() { return null }

    void checkClientTrusted(X509Certificate[] certs, String authType) {}

    void checkServerTrusted(X509Certificate[] certs, String authType) {}
}
