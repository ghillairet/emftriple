package org.eclipselabs.emftriple.sesame.map;

import java.util.Collection;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipselabs.emftriple.map.ISerializer;
import org.openrdf.model.Literal;
import org.openrdf.model.Model;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.model.vocabulary.RDF;

@SuppressWarnings("all")
public class Serializer implements ISerializer<Model> {
  public Model to(final Resource resource, final Model graph) {
    Model _xblockexpression = null;
    {
      EList<EObject> _contents = resource.getContents();
      final Procedure1<EObject> _function = new Procedure1<EObject>() {
          public void apply(final EObject it) {
            ValueFactoryImpl _instance = ValueFactoryImpl.getInstance();
            Serializer.this.to(it, graph, _instance);
          }
        };
      IterableExtensions.<EObject>forEach(_contents, _function);
      _xblockexpression = (graph);
    }
    return _xblockexpression;
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
  
  public Model to(final EObject eObject, final Model graph, final ValueFactory factory) {
    Model _xblockexpression = null;
    {
      this.createTypeStatement(eObject, graph, factory);
      EClass _eClass = eObject.eClass();
      EList<EAttribute> _eAllAttributes = _eClass.getEAllAttributes();
      final Procedure1<EAttribute> _function = new Procedure1<EAttribute>() {
          public void apply(final EAttribute it) {
            Serializer.this.serialize(it, eObject, graph, factory);
          }
        };
      IterableExtensions.<EAttribute>forEach(_eAllAttributes, _function);
      EClass _eClass_1 = eObject.eClass();
      EList<EReference> _eAllReferences = _eClass_1.getEAllReferences();
      final Procedure1<EReference> _function_1 = new Procedure1<EReference>() {
          public void apply(final EReference it) {
            Serializer.this.serialize(it, eObject, graph, factory);
          }
        };
      IterableExtensions.<EReference>forEach(_eAllReferences, _function_1);
      _xblockexpression = (graph);
    }
    return _xblockexpression;
  }
  
  protected boolean createTypeStatement(final EObject eObject, final Model graph, final ValueFactory factory) {
    boolean _xblockexpression = false;
    {
      final URIImpl subject = this.toURI(eObject);
      final org.openrdf.model.URI predicate = RDF.TYPE;
      EClass _eClass = eObject.eClass();
      final URIImpl object = this.toURI(_eClass);
      boolean _add = graph.add(subject, predicate, object);
      _xblockexpression = (_add);
    }
    return _xblockexpression;
  }
  
  private Boolean serialize(final EAttribute attribute, final EObject eObject, final Model graph, final ValueFactory factory) {
    boolean _xblockexpression = false;
    {
      boolean _or = false;
      boolean _or_1 = false;
      boolean _isDerived = attribute.isDerived();
      if (_isDerived) {
        _or_1 = true;
      } else {
        boolean _isTransient = attribute.isTransient();
        _or_1 = (_isDerived || _isTransient);
      }
      if (_or_1) {
        _or = true;
      } else {
        boolean _eIsSet = eObject.eIsSet(attribute);
        boolean _not = (!_eIsSet);
        _or = (_or_1 || _not);
      }
      if (_or) {
        return null;
      }
      final Object value = eObject.eGet(attribute);
      boolean _xifexpression = false;
      boolean _isMany = attribute.isMany();
      if (_isMany) {
        final Procedure1<Object> _function = new Procedure1<Object>() {
            public void apply(final Object it) {
              Serializer.this.add(graph, eObject, attribute, it, factory);
            }
          };
        IterableExtensions.<Object>forEach(((Collection<Object>) value), _function);
      } else {
        boolean _add = this.add(graph, eObject, attribute, value, factory);
        _xifexpression = _add;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  private Boolean serialize(final EReference reference, final EObject eObject, final Model graph, final ValueFactory factory) {
    boolean _xblockexpression = false;
    {
      boolean _or = false;
      boolean _or_1 = false;
      boolean _isDerived = reference.isDerived();
      if (_isDerived) {
        _or_1 = true;
      } else {
        boolean _isTransient = reference.isTransient();
        _or_1 = (_isDerived || _isTransient);
      }
      if (_or_1) {
        _or = true;
      } else {
        boolean _eIsSet = eObject.eIsSet(reference);
        boolean _not = (!_eIsSet);
        _or = (_or_1 || _not);
      }
      if (_or) {
        return null;
      }
      final Object value = eObject.eGet(reference);
      boolean _xifexpression = false;
      boolean _isMany = reference.isMany();
      if (_isMany) {
        final Procedure1<Object> _function = new Procedure1<Object>() {
            public void apply(final Object it) {
              Serializer.this.serializeOne(eObject, reference, ((EObject) it), graph, factory);
            }
          };
        IterableExtensions.<Object>forEach(((Collection<Object>) value), _function);
      } else {
        boolean _serializeOne = this.serializeOne(eObject, reference, ((EObject) value), graph, factory);
        _xifexpression = _serializeOne;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  private boolean serializeOne(final EObject subject, final EReference reference, final EObject value, final Model graph, final ValueFactory factory) {
    boolean _xblockexpression = false;
    {
      boolean _isContainment = reference.isContainment();
      if (_isContainment) {
        this.to(value, graph, factory);
      }
      boolean _add = this.add(graph, subject, reference, value);
      _xblockexpression = (_add);
    }
    return _xblockexpression;
  }
}
