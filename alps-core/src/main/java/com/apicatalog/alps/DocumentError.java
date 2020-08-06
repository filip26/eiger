package com.apicatalog.alps;

public enum DocumentError {

    DUPLICATED_ID,
    
    /** provided document is not ALPS document, does not contain 'alps' root */
    INVALID_DOCUMENT,

    /** ALPS document is not well-formed */
    MALFORMED_DOCUMENT,

    MALFORMED_URI,

    @Deprecated
    UNSUPPORTED_MEDIA_TYPE,
    
    @Deprecated
    PARSER_ERROR,

    ID_REQUIRED,
 
    HREF_REQUIRED,
 
    REL_REQUIRED,
    
    @Deprecated
    MALFORMED
}
