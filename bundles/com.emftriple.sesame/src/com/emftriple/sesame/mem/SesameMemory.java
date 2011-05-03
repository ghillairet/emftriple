/*******************************************************************************
 * Copyright (c) 2011 Guillaume Hillairet.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Guillaume Hillairet - initial API and implementation
 *******************************************************************************/
package com.emftriple.sesame.mem;

import org.openrdf.repository.Repository;

import com.emftriple.datasources.IMutableNamedGraphDataSource;
import com.emftriple.sesame.SailDataSource;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.1
 */
public class SesameMemory extends SailDataSource implements IMutableNamedGraphDataSource {

	protected SesameMemory(Repository repository) {
		super(repository);
	}

}
