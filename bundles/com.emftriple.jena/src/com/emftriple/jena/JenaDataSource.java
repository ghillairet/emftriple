package com.emftriple.jena;

import org.eclipse.emf.common.util.URI;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.rdf.model.Model;

public interface JenaDataSource {

	Model getModel();
	
	Model getModel(URI graph);
	
	QueryExecution getQueryExecution(String query, Model model);
	
}
