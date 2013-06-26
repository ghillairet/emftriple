RDF Binding for EMF
---

## About

EMFTriple is an RDF binding for EMF that will help you persist your EMF models in RDF instead of XMI. EMFTriple can work on 
regular RDF files and RDF data stores such as the various Sesame implementations (SailRepository) and Jena TDB. 

EMFTriple comes with two independent implementation, the first makes use of Jena, the second of Sesame. Both 
implementations provide basic support for reading and writing RDF files in different formats (RDF/XML, Turtle, N-Triples). 
The Sesame implementation can also work over any SailRepository implementations, while the Jena one 
can also work over the TDB store. 

## Installation

Releases:

 - Update Site: http://ghillairet.github.com/p2 (Composite site with Jena and Sesame OSGI Bunbles) 

Snapshots

 - Update Site: http://ghillairet.github.com/p2/emftriple/snapshots

## Getting Started

Register the desired Resource.Factory, this one is for RDF/XML files. Registering a Resource.Factory is the only 
thing you have to do to work with RDF.

```java
Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new RDFResourceFactory());
```

Create your resources and fill it with some objects.

```java
ResourceSet resourceSet = new ResourceSetImpl();
Resource r = resourceSet.createResource(URI.createURI("my.rdf"));

Book b = ModelFactory.eINSTANCE.createBook();
b.setTitle("The Book");
b.getTags().add("SciFI");
b.getTags().add("Fantasy");

r.getContents().add(b);
```

Save it.

```java
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

Get the source code:

```
git clone https://github.com/ghillairet/emftriple.git
```

Import the root project in eclipse using Import Maven Projects.

Compile and generate update site:

```
mvn clean install
```
