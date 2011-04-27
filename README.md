# EmfTriple

EmfTriple lets you store and retrieve EMF domain models from RDF repositories.

## Basic Usage

Storing objects

	Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put("emftriple", new TDBResourceFactory());
	ETriple.Registry.INSTANCE.register(ModelPackage.eINSTANCE);
	
	ResourceSet resourceSet = new ResourceSetImpl();
	Resource resource = resourceSet.createResource(URI.createURI("emftriple://data?graph=http://graph"));
	
	Person person = ModelFactory.eINSTANCE.createPerson();
	person.setName("John Doe");

	resource.getContents().add(person);
	resource.save(null);

	
googlecode project : http://code.google.com/a/eclipselabs.org/p/emftriple/
