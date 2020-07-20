package com.apicatalog.alps.dom.ref;

public interface Reference {

	boolean isLocal();

	boolean isRemote();
	
	RemoteReference asRemote();
	
	LocalReference asLocal();
}
