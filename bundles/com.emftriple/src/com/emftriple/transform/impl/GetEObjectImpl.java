package com.emftriple.transform.impl;

import static com.emftriple.transform.impl.GetUtil.getURI;
import static com.emftriple.util.EntityUtil.URI;
import static com.emftriple.util.SparqlQueries.selectAllTypes;
import static com.emftriple.util.SparqlQueries.selectObjectByClass;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.emf4sw.rdf.Literal;
import com.emf4sw.rdf.Node;
import com.emf4sw.rdf.Resource;
import com.emf4sw.rdf.URIElement;
import com.emf4sw.rdf.operations.DatatypeConverter;
import com.emftriple.ETriple;
import com.emftriple.datasources.INamedGraphDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.datasources.IResultSet.Solution;
import com.emftriple.resource.ETripleResource;
import com.emftriple.transform.IGetObject;
import com.emftriple.util.EntityUtil;
import com.google.inject.internal.Maps;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class GetEObjectImpl extends AbstractGetObject implements IGetObject {

	private final GetProxyObjectImpl proxyFactory;

	public GetEObjectImpl(ETripleResource resource) {
		super(resource);
		this.proxyFactory = new GetProxyObjectImpl(resource);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> entityClass, URI key) {
		if (cache.hasKey(key.toString())) {
			EObject obj = cache.getObjectByKey(key.toString());
			if (!obj.eIsProxy() && (entityClass.isInstance(obj))) {
				return (T) obj;
			} else {
				return (T) getProxy(obj, getEClass(key), key);
			}
		}

		final EClass aClass = getEClass(key);
		final EClass requestedEClass = ETriple.mapping.getEClass(entityClass);

		T object = null;
		if (aClass.equals(requestedEClass) || aClass.getESuperTypes().contains(requestedEClass)) {
			object = (T) doGet( aClass, key );
		}

		return object;
	}

	private EClass getEClass(URI key) {
		return ETriple.mapping.findEClassByRdfType(
				selectAllTypes(dataSource, key.toString(), resource.getGraph()));
	}

	@Override
	public EObject get(EClass eClass, URI key) {
		if (cache.hasKey(key.toString())) {
			EObject obj = cache.getObjectByKey(key.toString());
			if (!obj.eIsProxy()) {
				return obj;
			} else {
				return getProxy(obj, eClass, key);
			}
		}

		return doGet( eClass, key );
	}

	@Override
	public EObject getProxy(EObject obj, EClass eClass, URI key) {
		final IResultSet resultSet; 
		if (resource.getGraph() != null) {
			resultSet = ((INamedGraphDataSource)dataSource).selectQuery(selectObjectByClass(eClass, key.toString()), resource.getGraph());
		} else {
			resultSet = dataSource.selectQuery(selectObjectByClass(eClass, key.toString()));
		}
		
		final EAttribute attrId = EntityUtil.getId(eClass);
		setIdValue(obj, key.toString(), attrId);
		((InternalEObject)obj).eSetProxyURI(null);

		final Map<EStructuralFeature, String> previous = Maps.newHashMap();
		for (;resultSet.hasNext();) {
			Solution sol = resultSet.next();
			for (EStructuralFeature feature: eClass.getEAllStructuralFeatures()) {
				Node node = sol.get(feature.getName());
				if (node != null) {
					if (feature instanceof EAttribute && node instanceof Literal) {
						if (feature.isMany()) {
							if (!previous.containsKey(feature)) {
								doEAttribute(obj, (EAttribute)feature, (Literal)node);
							}
							else if (!previous.get(feature).equals(((Literal) node).getLexicalForm())) {
								doEAttribute(obj, (EAttribute)feature, (Literal)node);
							}
							previous.put(feature, ((Literal) node).getLexicalForm());
						} else {
							doEAttribute(obj, (EAttribute)feature, (Literal)node);
						}
					} else if (node instanceof Resource && node instanceof Resource){
						if (feature.isMany()) {
							if (!previous.containsKey(feature)) {
								doEReference(obj, (EReference)feature, (Resource)node);
							}
							else if (!previous.get(feature).equals(((URIElement) node).getURI())) {
								doEReference(obj, (EReference)feature, (Resource)node);								
							}
							previous.put(feature, ((URIElement) node).getURI());
						} else {
							doEReference(obj, (EReference)feature, (Resource)node);
						}
					}
				}
			}
		}

		return obj;
	}

	private EObject doGet(EClass eClass, URI key) {
		final IResultSet resultSet; 
		if (resource.getGraph() != null) {
			resultSet = ((INamedGraphDataSource)dataSource).selectQuery(selectObjectByClass(eClass, key.toString()), resource.getGraph());
		} else {
			resultSet = dataSource.selectQuery(selectObjectByClass(eClass, key.toString()));
		}
		
		final EObject returnedObject = EcoreUtil.create(eClass);
		final EAttribute attrId = EntityUtil.getId(eClass);
		setIdValue(returnedObject, key.toString(), attrId);

		resource.getContents().add(returnedObject);

		final Map<EStructuralFeature, String> previous = Maps.newHashMap();

		for (;resultSet.hasNext();) {
			Solution sol = resultSet.next();
			for (EStructuralFeature feature: eClass.getEAllStructuralFeatures()) {
				Node node = sol.get(feature.getName());
				if (node != null) {
					if (feature instanceof EAttribute && node instanceof Literal) {
						if (feature.isMany()) {
							if (!previous.containsKey(feature)) {
								doEAttribute(returnedObject, (EAttribute)feature, (Literal)node);
							}
							else if (!previous.get(feature).equals(((Literal) node).getLexicalForm())) {
								doEAttribute(returnedObject, (EAttribute)feature, (Literal)node);
							}
							previous.put(feature, ((Literal) node).getLexicalForm());
						} else {
							doEAttribute(returnedObject, (EAttribute)feature, (Literal)node);
						}
					} else if (node instanceof Resource && node instanceof Resource){
						if (feature.isMany()) {
							if (!previous.containsKey(feature)) {
								doEReference(returnedObject, (EReference)feature, (Resource)node);
							}
							else if (!previous.get(feature).equals(((URIElement) node).getURI())) {
								doEReference(returnedObject, (EReference)feature, (Resource)node);								
							}
							previous.put(feature, ((URIElement) node).getURI());
						} else {
							doEReference(returnedObject, (EReference)feature, (Resource)node);
						}
					}
				}
			}
		}

		cache.cache(key.toString(), returnedObject);

		return returnedObject;
	}

	private void doEReference(final EObject returnedObject, EReference feature, Resource node) {
		if (feature.isMany()) {
			@SuppressWarnings("unchecked")
			final EList<Object> list = (EList<Object>) returnedObject.eGet(feature);

			if (feature.isContainment()) {
				list.add( get(getClass(node, (EClass) feature.getEType()), URI(node.getURI())) );
			} else {
				EObject prox = doProxy(node, getClass(node, (EClass) feature.getEType()));
				list.add( prox );
			}
		} else {
			if (feature.isContainment()) {
				returnedObject.eSet(feature, get(getClass(node, (EClass) feature.getEType()), URI(node.getURI())));
			} else {
				returnedObject.eSet(feature, doProxy(node, getClass(node, (EClass) feature.getEType())));
			}
		}
	}


	private void doEAttribute(EObject returnedObject, EAttribute feature, Literal node) {	
		if (feature.isMany()) {
			@SuppressWarnings("unchecked")
			final EList<Object> list = (EList<Object>) returnedObject.eGet(feature);
			//			final String aStringValue;
			//			if (isLangSpecific(feature)) {
			//				aStringValue = getValue(triples, getLang(attribute));
			//			} else {
			//				aStringValue = getValue(triples.get(0).getObject());
			//			}
			final Object value = DatatypeConverter.convert((EDataType) feature.getEType(), node.getLexicalForm());
			if (value != null) list.add(value);
		} else {
			final Object value = DatatypeConverter.convert((EDataType) feature.getEType(), node.getLexicalForm());
			if (value != null) returnedObject.eSet(feature, value);
		}
	}

	private EObject doProxy(Node node, EClass eType) {
		final URI nodeURI = getURI(node);

		if (cache.hasKey(nodeURI.toString())) {
			return cache.getObjectByKey(nodeURI.toString());
		} else {
			return proxyFactory.get(eType, nodeURI);
		}
	}

	private boolean isLangSpecific(EAttribute attribute) {
		EAnnotation ann = EntityUtil.getETripleAnnotation(attribute, "DataProperty");
		if (ann != null) {
			return ann.getDetails().containsKey("lang");
		}
		ann = EntityUtil.getETripleAnnotation(attribute, "rdf");
		if (ann != null) {
			return ann.getDetails().containsKey("lang");
		}
		return false;
	}

	private String getLang(EAttribute attribute) {
		EAnnotation ann = EntityUtil.getETripleAnnotation(attribute, "DataProperty");
		if (ann != null) {
			return ann.getDetails().get("lang");
		}
		ann = EntityUtil.getETripleAnnotation(attribute, "rdf");
		if (ann != null) {
			return ann.getDetails().get("lang");
		}
		return null;
	}

	private EClass getClass(Node node, EClass eType) {
		checkNotNull(eType);

		return 	(node instanceof URIElement) ?
				ETriple.mapping.findEClassByRdfType( selectAllTypes(dataSource, ((URIElement) node).getURI(), resource.getGraph()))
				: null;
	}

}
