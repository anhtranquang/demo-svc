package com.finx.contractservice.infra.client.fptesign.constant;

public class ESignCloudConstant {
    public static final int AUTHORISATION_METHOD_SMS = 1;
    public static final int AUTHORISATION_METHOD_EMAIL = 2;
    public static final int AUTHORISATION_METHOD_MOBILE = 3;
    public static final int AUTHORISATION_METHOD_PASSCODE = 4;
    public static final int AUTHORISATION_METHOD_UAF = 5;

    public static final int ASYNCHRONOUS_CLIENTSERVER = 1;
    public static final int ASYNCHRONOUS_SERVERSERVER = 2;
    public static final int SYNCHRONOUS = 3;

    public static final String MIMETYPE_PDF = "application/pdf";
    public static final String MIMETYPE_XML = "application/xml";
    public static final String MIMETYPE_XHTML_XML = "application/xhtml+xml";

    public static final String MIMETYPE_BINARY_WORD = "application/msword";
    public static final String MIMETYPE_OPENXML_WORD =
            "application/ vnd.openxmlformats-officedocument.wordprocessingml.document";
    public static final String MIMETYPE_BINARY_POWERPOINT = "application/vnd.ms-powerpoint";
    public static final String MIMETYPE_OPENXML_POWERPOINT =
            "application/vnd.openxmlformats-officedocument.presentationml.presentation";
    public static final String MIMETYPE_BINARY_EXCEL = "application/vnd.ms-excel";
    public static final String MIMETYPE_OPENXML_EXCEL =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String MIMETYPE_MSVISIO = "application/vnd.visio";

    public static final String MIMETYPE_SHA1 = "application/sha1-binary";
    public static final String MIMETYPE_SHA256 = "application/sha256-binary";
    public static final String MIMETYPE_SHA384 = "application/sha384-binary";
    public static final String MIMETYPE_SHA512 = "application/sha512-binary";
}
