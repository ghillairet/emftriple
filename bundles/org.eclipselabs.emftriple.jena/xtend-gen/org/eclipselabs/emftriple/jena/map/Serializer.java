package org.eclipselabs.emftriple.jena.map;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
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
import org.eclipselabs.emftriple.vocabularies.RDF;

@SuppressWarnings("all")
public class Serializer implements ISerializer<Model> {
  public Model to(final Resource resource, final Model graph) {
    Model _xblockexpression = null;
    {
      EList<EObject> _contents = resource.getContents();
      final Procedure1<EObject> _function = new Procedure1<EObject>() {
          public void apply(final EObject it) {
            Serializer.this.to(it, graph);
          }
        };
      IterableExtensions.<EObject>forEach(_contents, _function);
      _xblockexpression = (graph);
    }
    return _xblockexpression;
  }
  
  public Model to(final EObject eObject, final Model graph) {
    Model _xblockexpression = null;
    {
      final URI uri = EcoreUtil.getURI(eObject);
      EClass _eClass = eObject.eClass();
      final URI eClassURI = EcoreUtil.getURI(_eClass);
      String _string = uri.toString();
      final com.hp.hpl.jena.rdf.model.Resource subject = graph.getResource(_string);
      this.createTypeStatement(subject, eClassURI, graph);
      EClass _eClass_1 = eObject.eClass();
      EList<EAttribute> _eAllAttributes = _eClass_1.getEAllAttributes();
      final Procedure1<EAttribute> _function = new Procedure1<EAttribute>() {
          public void apply(final EAttribute it) {
            Serializer.this.serialize(it, eObject, subject, graph);
          }
        };
      IterableExtensions.<EAttribute>forEach(_eAllAttributes, _function);
      EClass _eClass_2 = eObject.eClass();
      EList<EReference> _eAllReferences = _eClass_2.getEAllReferences();
      final Procedure1<EReference> _function_1 = new Procedure1<EReference>() {
          public void apply(final EReference it) {
            Serializer.this.serialize(it, eObject, subject, graph);
          }
        };
      IterableExtensions.<EReference>forEach(_eAllReferences, _function_1);
      _xblockexpression = (graph);
    }
    return _xblockexpression;
  }
  
  private Model createTypeStatement(final com.hp.hpl.jena.rdf.model.Resource subject, final URI eClass, final Model graph) {
    Model _xblockexpression = null;
    {
      final Property predicate = graph.getProperty(RDF.type);
      String _string = eClass.toString();
      final com.hp.hpl.jena.rdf.model.Resource object = graph.getResource(_string);
      Model _add = graph.add(subject, predicate, object);
      _xblockexpression = (_add);
    }
    return _xblockexpression;
  }
  
  private Model serialize(final EAttribute attribute, final EObject eObject, final com.hp.hpl.jena.rdf.model.Resource resource, final Model graph) {
    Model _xblockexpression = null;
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
      Model _xifexpression = null;
      boolean _isMany = attribute.isMany();
      if (_isMany) {
        final Procedure1<Object> _function = new Procedure1<Object>() {
            public void apply(final Object it) {
              Serializer.this.serializeOne(it, attribute, resource, graph);
            }
          };
        IterableExtensions.<Object>forEach(((Collection<Object>) value), _function);
      } else {
        Model _serializeOne = this.serializeOne(value, attribute, resource, graph);
        _xifexpression = _serializeOne;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  private Model serializeOne(final Object value, final EAttribute attribute, final com.hp.hpl.jena.rdf.model.Resource resource, final Model graph) {
    Model _xblockexpression = null;
    {
      EDataType _eAttributeType = attribute.getEAttributeType();
      final String stringValue = EcoreUtil.convertToString(_eAttributeType, value);
      final URI propertyURI = EcoreUtil.getURI(attribute);
      String _string = propertyURI.toString();
      Property _property = graph.getProperty(_string);
      Model _add = graph.add(resource, _property, stringValue);
      _xblockexpression = (_add);
    }
    return _xblockexpression;
  }
  
  private Model serialize(final EReference reference, final EObject eObject, final com.hp.hpl.jena.rdf.model.Resource resource, final Model graph) {
    Model _xblockexpression = null;
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
      Model _xifexpression = null;
      boolean _isMany = reference.isMany();
      if (_isMany) {
        final Procedure1<Object> _function = new Procedure1<Object>() {
            public void apply(final Object it) {
              Serializer.this.serializeOne(it, reference, resource, graph);
            }
          };
        IterableExtensions.<Object>forEach(((Collection<Object>) value), _function);
      } else {
        Model _serializeOne = this.serializeOne(value, reference, resource, graph);
        _xifexpression = _serializeOne;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  private Model serializeOne(final Object value, final EReference reference, final com.hp.hpl.jena.rdf.model.Resource resource, final Model graph) {
    Model _xblockexpression = null;
    {
      final EObject eObject = ((EObject) value);
      final URI uri = EcoreUtil.getURI(eObject);
      final URI propertyURI = EcoreUtil.getURI(reference);
      boolean _isContainment = reference.isContainment();
      if (_isContainment) {
        this.to(eObject, graph);
      }
      String _string = propertyURI.toString();
      Property _property = graph.getProperty(_string);
      String _string_1 = uri.toString();
      com.hp.hpl.jena.rdf.model.Resource _resource = graph.getResource(_string_1);
      Model _add = graph.add(resource, _property, _resource);
      _xblockexpression = (_add);
    }
    return _xblockexpression;
  }
}
