package com.emftriple.resource;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * 
 * @author ghilla01
 *
 */
public class URIBuilder {
	
	public static URI build(Resource resource, URI key) {
		final String fragment = "uri=" + key.toString().replaceAll("#", "%23");
		if (!fragment.startsWith("uri=http://")) {
			throw new IllegalArgumentException();
		}
		final URI uriFragment = resource.getURI().appendFragment(fragment);
		
		return uriFragment;
	}
}