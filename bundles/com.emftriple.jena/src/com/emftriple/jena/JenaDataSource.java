package com.emftriple.jena;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.rdf.model.Model;

public interface JenaDataSource {

	Model getModel();
	
	Model getModel(String graph);
	
	QueryExecution getQueryExecution(String query, Model model);
	
}
