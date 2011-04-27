package com.emftriple.sesame.mem;

import org.openrdf.repository.Repository;

import com.emftriple.datasources.IMutableNamedGraphDataSource;
import com.emftriple.sesame.SailDataSource;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.1
 */
public class SesameMem extends SailDataSource implements IMutableNamedGraphDataSource {

	protected SesameMem(String name, Repository repository) {
		super(name, repository);
	}

}
