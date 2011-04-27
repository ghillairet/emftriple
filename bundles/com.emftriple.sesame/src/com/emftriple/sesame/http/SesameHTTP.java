package com.emftriple.sesame.http;

import org.openrdf.repository.Repository;

import com.emftriple.sesame.SailDataSource;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.1
 */
public class SesameHTTP extends SailDataSource {

	protected SesameHTTP(String name, Repository repository) {
		super(name, repository);
	}

}
