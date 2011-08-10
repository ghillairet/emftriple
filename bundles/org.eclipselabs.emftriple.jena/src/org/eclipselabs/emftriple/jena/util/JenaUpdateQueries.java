/**
 * 
 */
package org.eclipselabs.emftriple.jena.util;

import org.eclipselabs.emftriple.internal.util.UpdateQueries;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

/**
 * @author ghillairet
 *
 */
public class JenaUpdateQueries extends UpdateQueries<Statement> {

	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.internal.util.UpdateQueries#getTriples(java.lang.Object)
	 */
	@Override
	protected String getTriples(Statement triple) {
		Resource sbj = triple.getSubject();
		Property prop = triple.getPredicate();
		RDFNode obj = triple.getObject();
		
		String result = "";
		if (sbj.isURIResource())
			result+="<"+sbj.getURI()+"> <"+prop.getURI()+"> ";
		
		if (obj.isURIResource())
			result+="<"+obj.asResource().getURI()+"> .";
		else if (obj.isLiteral()) {
			Literal lit = obj.asLiteral();
			result+="\""+lit.getLexicalForm()+"\"";
			String dt = lit.getDatatypeURI();
			if (dt != null)
				result+="^^<"+dt+"> . ";
			else
				result+=" . ";
			
		}
		return result;
	}
	
}
