package com.emftriple.sesame;

import org.openrdf.repository.Repository;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.1
 */
public class SesameNative extends SailDataSource {

	protected SesameNative(String name, Repository repository) {
		super(name, repository);
	}
	
}
