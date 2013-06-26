package org.eclipselabs.emftriple.sesame.map;

import java.util.Collection;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipselabs.emftriple.sesame.map.Extensions;
import org.openrdf.model.Model;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.model.vocabulary.RDF;

@SuppressWarnings("all")
public class Serializer {
  @Extension
  private Extensions extensions = new Function0<Extensions>() {
    public Extensions apply() {
      Extensions _extensions = new Extensions();
      return _extensions;
    }
  }.apply();
  
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
      final URIImpl subject = this.extensions.toURI(eObject);
      final URI predicate = RDF.TYPE;
      EClass _eClass = eObject.eClass();
      final URIImpl object = this.extensions.toURI(_eClass);
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
              Serializer.this.extensions.add(graph, eObject, attribute, it, factory);
            }
          };
        IterableExtensions.<Object>forEach(((Collection<Object>) value), _function);
      } else {
        boolean _add = this.extensions.add(graph, eObject, attribute, value, factory);
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
      boolean _add = this.extensions.add(graph, subject, reference, value);
      _xblockexpression = (_add);
    }
    return _xblockexpression;
  }
}
