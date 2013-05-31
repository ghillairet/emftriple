package org.eclipselabs.emftriple.sesame.map;

import com.google.common.base.Objects;
import java.util.List;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.openrdf.model.Literal;
import org.openrdf.model.Model;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.URIImpl;

@SuppressWarnings("all")
class Extensions {
  public EObject getEObject(final ResourceSet resourceSet, final Value value) {
    URI _uRI = this.toURI(value);
    EObject _eObject = resourceSet.getEObject(_uRI, true);
    return _eObject;
  }
  
  public URI toURI(final Value value) {
    String _stringValue = value.stringValue();
    URI _createURI = URI.createURI(_stringValue);
    return _createURI;
  }
  
  public URIImpl toURI(final EObject eObject) {
    URI _uRI = EcoreUtil.getURI(eObject);
    URIImpl _uRI_1 = this.toURI(_uRI);
    return _uRI_1;
  }
  
  public URIImpl toURI(final URI uri) {
    String _string = uri.toString();
    URIImpl _uRIImpl = new URIImpl(_string);
    return _uRIImpl;
  }
  
  public Literal toLiteral(final Object value, final EAttribute attribute, final ValueFactory factory) {
    Literal _xblockexpression = null;
    {
      EDataType _eAttributeType = attribute.getEAttributeType();
      final String stringValue = EcoreUtil.convertToString(_eAttributeType, value);
      Literal _createLiteral = factory.createLiteral(stringValue);
      _xblockexpression = (_createLiteral);
    }
    return _xblockexpression;
  }
  
  public boolean add(final Model graph, final EObject eObject, final EAttribute feature, final Object value, final ValueFactory factory) {
    URIImpl _uRI = this.toURI(eObject);
    URIImpl _uRI_1 = this.toURI(feature);
    Literal _literal = this.toLiteral(value, feature, factory);
    boolean _add = graph.add(_uRI, _uRI_1, _literal);
    return _add;
  }
  
  public boolean add(final Model graph, final EObject eObject, final EReference feature, final EObject value) {
    URIImpl _uRI = this.toURI(eObject);
    URIImpl _uRI_1 = this.toURI(feature);
    URIImpl _uRI_2 = this.toURI(value);
    boolean _add = graph.add(_uRI, _uRI_1, _uRI_2);
    return _add;
  }
  
  public List<EObject> appendTo(final EObject object, final List<EObject> objects) {
    List<EObject> _xblockexpression = null;
    {
      boolean _notEquals = (!Objects.equal(object, null));
      if (_notEquals) {
        objects.add(object);
      }
      _xblockexpression = (objects);
    }
    return _xblockexpression;
  }
}
