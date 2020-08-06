package com.apicatalog.alps.error;

public class DocumentException extends Exception {
    
    private static final long serialVersionUID = -1277283759805773786L;

    public DocumentException(String message) {
        super(message);
    }
    
    public DocumentException(Throwable cause) {
        super(cause);
    }
    
}
