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
package org.eclipselabs.emftriple.vocabularies;

/**
 * RDFS vocabulary
 * 
 * @author ghillairet
 * @since 0.5.0
 */
public final class RDFS {

	public static final String NS = "http://www.w3.org/2000/01/rdf-schema#";

	public static final String Resource = NS + "Resource";

	public static final String Class = NS + "Class";

	public static final String Literal = NS + "Literal";

	public static final String Datatype = NS + "Datatype";

	public static final String range = NS + "range";

	public static final String domain = NS + "domain";

	public static final String subClassOf = NS + "subClassOf";

	public static final String subPropertyOf = NS + "subPropertyOf";

	public static final String label = NS + "label";

	public static final String comment = NS + "comment";

	public static final String Container = NS + "Container";

	public static final String ContainerMembershipProperty = NS + "ContainerMembershipProperty";

	public static final String member = NS + "member";
	
	public static final String seeAlso = NS + "seeAlso";
	
	public static final String isDefinedBy = NS + "isDefinedBy";

}
