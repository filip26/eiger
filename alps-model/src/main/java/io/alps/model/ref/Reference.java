package io.alps.model.ref;

public interface Reference {

	boolean isLocal();

	boolean isRemote();
	
	RemoteReference asRemote();
	
	LocalReference asLocal();
}
