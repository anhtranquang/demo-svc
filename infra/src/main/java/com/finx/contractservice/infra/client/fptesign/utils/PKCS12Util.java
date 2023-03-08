package com.finx.contractservice.infra.client.fptesign.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;
import java.util.Enumeration;

public class PKCS12Util {

    /**
     * Signed data by PKCS12 keystore
     *
     * @param data
     * @param relyingPartyKeyStore Path of key store file
     * @param relyingPartyKeyStorePassword key store passphrase
     * @return Base64 signed data
     * @throws Exception
     */
    public static String getSignature(
            String data, String relyingPartyKeyStore, String relyingPartyKeyStorePassword)
            throws Exception {
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        InputStream is = new FileInputStream(relyingPartyKeyStore);
        keystore.load(is, relyingPartyKeyStorePassword.toCharArray());

        Enumeration<String> e = keystore.aliases();
        PrivateKey key = null;
        String aliasName = "";
        while (e.hasMoreElements()) {
            aliasName = e.nextElement();
            key = (PrivateKey) keystore.getKey(aliasName, relyingPartyKeyStorePassword.toCharArray());
            if (key != null) {
                break;
            }
        }

        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initSign(key);
        sig.update(data.getBytes());
        return Base64.getEncoder().encodeToString(sig.sign());
    }
}
