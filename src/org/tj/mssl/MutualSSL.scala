package org.tj.mssl

import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import java.security.KeyStore
import java.io.FileInputStream
import java.io.File
import org.apache.http.conn.ssl.TrustStrategy
import java.security.cert.X509Certificate
import org.apache.http.ssl.SSLContexts
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.impl.client.HttpClients

object MutualSSL {

  def buildHTTPClient(useSSL: Boolean): CloseableHttpClient = {
    if (!useSSL) {
      HttpClientBuilder.create().build()
    } else {
      val keyStorePassword = "[your keystore password]".toCharArray()
      val importedKeyPassword = "[your imported key password]".toCharArray()
      val keyStore = KeyStore.getInstance(KeyStore.getDefaultType)
      val keyStorePath = new FileInputStream(new File("[your key store path .jks]"))
      keyStore.load(keyStorePath, keyStorePassword)
      keyStorePath.close()
      
      val trustStrategy = new TrustStrategy() {
        override def isTrusted(x509Certs: Array[X509Certificate], s: String): Boolean = true
      }
      
      val sslContext = SSLContexts.custom()
                                  .loadKeyMaterial(keyStore, importedKeyPassword)
                                  .build()
                                  
      val sslSocketFactory = new SSLConnectionSocketFactory(sslContext)
      
      HttpClients.custom().setSSLSocketFactory(sslSocketFactory).build()
    }  
  }
  
  def main(args: Array[String]): Unit = {
    
  }
}