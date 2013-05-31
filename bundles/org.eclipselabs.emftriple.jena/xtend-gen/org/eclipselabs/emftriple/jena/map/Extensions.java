package org.eclipselabs.emftriple.jena.map;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

@SuppressWarnings("all")
class Extensions {
  public EObject create(final EClass eClass) {
    EObject _create = EcoreUtil.create(eClass);
    return _create;
  }
  
  public URI URI(final EObject eObject) {
    URI _uRI = EcoreUtil.getURI(eObject);
    return _uRI;
  }
  
  public String toURI(final EObject eObject) {
    URI _URI = this.URI(eObject);
    String _uRI = this.toURI(_URI);
    return _uRI;
  }
  
  public String toURI(final URI uri) {
    String _string = uri.toString();
    return _string;
  }
  
  public Resource getResource(final EObject eObject, final Model graph) {
    String _uRI = this.toURI(eObject);
    Resource _resource = graph.getResource(_uRI);
    return _resource;
  }
  
  public Property getProperty(final EStructuralFeature eObject, final Model graph) {
    String _uRI = this.toURI(eObject);
    Property _property = graph.getProperty(_uRI);
    return _property;
  }
  
  public Literal getLiteral(final Object value, final EAttribute attribute, final Model graph) {
    Literal _xblockexpression = null;
    {
      EDataType _eAttributeType = attribute.getEAttributeType();
      final String stringValue = EcoreUtil.convertToString(_eAttributeType, value);
      Literal _createLiteral = graph.createLiteral(stringValue);
      _xblockexpression = (_createLiteral);
    }
    return _xblockexpression;
  }
  
  public EObject getEObject(final ResourceSet resourceSet, final Resource res) {
    EObject _xblockexpression = null;
    {
      String _uRI = res.getURI();
      final URI eURI = URI.createURI(_uRI);
      EObject _eObject = resourceSet.getEObject(eURI, true);
      _xblockexpression = (_eObject);
    }
    return _xblockexpression;
  }
}
