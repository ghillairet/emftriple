RDF Binding for EMF
---

## Installation


## Getting Started

```java
Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new RDFResourceFactory());

ResourceSet resourceSet = new ResourceSetImpl();
Resource r = resourceSet.createResource(URI.createURI("my.rdf"));

Book b = ModelFactory.eINSTANCE.createBook();
b.setTitle("The Book");
b.getTags().add("SciFI");
b.getTags().add("Fantasy");

r.getContents().add(b);

r.save(null);
```
Resulting RDF/XML document:

```xml
<rdf:RDF
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:j.0="http://www.eclipselabs.org/emf/junit#//"
    xmlns:j.1="http://www.eclipselabs.org/emf/junit#//Book/" >
  <rdf:Description rdf:about="http://m.rdf#/">
    <j.1:tags>Fantasy</j.1:tags>
    <j.1:tags>SciFI</j.1:tags>
    <j.1:title>The Book</j.1:title>
    <rdf:type rdf:resource="http://www.eclipselabs.org/emf/junit#//Book"/>
  </rdf:Description>
</rdf:RDF>
```

## Development


