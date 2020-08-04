package com.apicatalog.alps;

public enum AlpsErrorCode {

    DUPLICATED_ID,
    
    /** provided document is not ALPS document, does not contain 'alps' root */
    INVALID_DOCUMENT,

    /** ALPS document is not well-formed */
    MALFORMED_DOCUMENT,

    MALFORMED_URI,
    
    UNSUPPORTED_MEDIA_TYPE,
    
    PARSER_ERROR,

    ID_REQUIRED,
 
    HREF_REQUIRED,
 
    REL_REQUIRED,
    
    @Deprecated
    MALFORMED
}
