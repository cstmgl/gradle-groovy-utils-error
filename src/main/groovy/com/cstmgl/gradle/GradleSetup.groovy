package com.cstmgl.gradle

import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import java.security.SecureRandom

class GradleSetup {
    static ignoreCertificateCheck() {
        TrustManager[] trustAllCerts = new TrustManager[1]
        trustAllCerts[0] = new TrustManager()
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        LinkedHashMap<String, Closure<Boolean>> nullHostnameVerifier = [
                verify: { hostname, session -> true }
        ]
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(nullHostnameVerifier as javax.net.ssl.HostnameVerifier)
    }
}