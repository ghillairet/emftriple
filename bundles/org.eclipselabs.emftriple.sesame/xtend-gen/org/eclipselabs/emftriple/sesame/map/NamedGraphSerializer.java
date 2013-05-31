package org.eclipselabs.emftriple.sesame.map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipselabs.emftriple.sesame.map.Serializer;
import org.openrdf.model.Literal;
import org.openrdf.model.Model;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.RDF;

@SuppressWarnings("all")
public class NamedGraphSerializer extends Serializer {
  public boolean createTypeStatement(final EObject eObject, final Model graph, final ValueFactory factory) {
    URIImpl _uRI = this.toURI(eObject);
    EClass _eClass = eObject.eClass();
    URIImpl _uRI_1 = this.toURI(_eClass);
    Resource _eResource = eObject.eResource();
    URI _uRI_2 = _eResource.getURI();
    URIImpl _uRI_3 = this.toURI(_uRI_2);
    boolean _add = graph.add(_uRI, 
      RDF.TYPE, _uRI_1, _uRI_3);
    return _add;
  }
  
  public boolean add(final Model graph, final EObject eObject, final EAttribute feature, final Object value, final ValueFactory factory) {
    URIImpl _uRI = this.toURI(eObject);
    URIImpl _uRI_1 = this.toURI(feature);
    Literal _literal = this.toLiteral(value, feature, factory);
    Resource _eResource = eObject.eResource();
    URI _uRI_2 = _eResource.getURI();
    URIImpl _uRI_3 = this.toURI(_uRI_2);
    boolean _add = graph.add(_uRI, _uRI_1, _literal, _uRI_3);
    return _add;
  }
  
  public boolean add(final Model graph, final EObject eObject, final EReference feature, final EObject value) {
    URIImpl _uRI = this.toURI(eObject);
    URIImpl _uRI_1 = this.toURI(feature);
    URIImpl _uRI_2 = this.toURI(value);
    Resource _eResource = eObject.eResource();
    URI _uRI_3 = _eResource.getURI();
    URIImpl _uRI_4 = this.toURI(_uRI_3);
    boolean _add = graph.add(_uRI, _uRI_1, _uRI_2, _uRI_4);
    return _add;
  }
}
