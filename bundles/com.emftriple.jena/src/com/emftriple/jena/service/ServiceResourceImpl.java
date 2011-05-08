package com.emftriple.jena.service;

import java.util.Map;

import org.eclipse.emf.common.util.URI;

import com.emftriple.datasources.IDataSource;
import com.emftriple.jena.JenaResourceImpl;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;

public class ServiceResourceImpl 
	extends JenaResourceImpl {

	public ServiceResourceImpl(URI uri) {
		super(uri);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IDataSource<Model, Statement, RDFNode, com.hp.hpl.jena.rdf.model.Resource, Literal> 
		getDataSource(Map<?, ?> options) {
		
		
		return null;
	}

	@Override
	public IDataSource<Model, Statement, RDFNode, com.hp.hpl.jena.rdf.model.Resource, Literal> getDataSource() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
