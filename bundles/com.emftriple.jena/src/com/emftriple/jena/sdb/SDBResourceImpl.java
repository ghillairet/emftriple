package com.emftriple.jena.sdb;

import java.util.Map;

import org.eclipse.emf.common.util.URI;

import com.emftriple.datasources.IDataSource;
import com.emftriple.jena.JenaResourceImpl;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

public class SDBResourceImpl 
	extends JenaResourceImpl {

	public SDBResourceImpl(URI uri) {
		super(uri);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IDataSource<Model, Statement, RDFNode, Resource, Literal> getDataSource(
			Map<?, ?> options) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDataSource<Model, Statement, RDFNode, Resource, Literal> getDataSource() {
		// TODO Auto-generated method stub
		return null;
	}

}
