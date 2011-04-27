
emfriple lets you store and retrieve EMF domain models from RDF repositories.

googlecode project : http://code.google.com/a/eclipselabs.org/p/emftriple/

# Basic Usage

<<<<<<< HEAD
Init emftriple:
    
    Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put("emftriple", new TDBResourceFactory());
    ETriple.Registry.INSTANCE.register(ModelPackage.eINSTANCE);

Storing objects:
    
    ResourceSet resourceSet = new ResourceSetImpl();
    Resource resource = resourceSet.createResource(URI.createURI("emftriple://data?graph=http://graph"));
    	
    Person person = ModelFactory.eINSTANCE.createPerson();
    person.setName("John Doe");
    
    resource.getContents().add(person);
    resource.save(null);

Loading objects:
    
    ResourceSet resourceSet = new ResourceSetImpl();
    Resource resource = resourceSet.createResource(URI.createURI("emftriple://data?graph=http://graph"));
    resource.load(null);

    Person obj = (Person) EcoreUtil.getObjectByType(resource.getContents(), ModelPackage.eINSTANCE.getPerson());
=======
Init emftriple
	Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put("emftriple", new TDBResourceFactory());
	ETriple.Registry.INSTANCE.register(ModelPackage.eINSTANCE);

Storing objects
	ResourceSet resourceSet = new ResourceSetImpl();
	Resource resource = resourceSet.createResource(URI.createURI("emftriple://data?graph=http://graph"));
	
	Person person = ModelFactory.eINSTANCE.createPerson();
	person.setName("John Doe");
>>>>>>> 22210ba095fcd80ab31817ac769790599af8bb6a


<<<<<<< HEAD
=======
Loading objects
	ResourceSet resourceSet = new ResourceSetImpl();
	Resource resource = resourceSet.createResource(URI.createURI("emftriple://data?graph=http://graph"));
	resource.load(null);

	Person obj = (Person) EcoreUtil.getObjectByType(resource.getContents(), ModelPackage.eINSTANCE.getPerson());


>>>>>>> 22210ba095fcd80ab31817ac769790599af8bb6a
