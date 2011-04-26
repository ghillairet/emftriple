package com.emftriple.query;

import org.eclipse.emf.common.util.URI;

public class ETripleQueryImpl implements ETripleQuery {

	private final String query;
	private final URI resource;

	public ETripleQueryImpl(String query, URI resource) {
		this.query = query;
		this.resource = resource;
	}

	@Override
	public URI toURI() {
		return URI.createURI(resource+"&query="+query);
	}
}
