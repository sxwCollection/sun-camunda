# what

in this demo it is shown how to create camunda 7 external task handlers in a Spring boot App.

# why camunda

the tool camunda could be used to distribute processes and automate them.
Camunda could be used as an implementation of the Orchestration-based sagas pattern,  
but the task process latency and the process through put in the performance tests are shown bad,  
it does not suit user requests, which should be served in seconds.   
In camunda 8 the gRpc is in used, it is still open, if this problem could be resolved.

## openApi with camunda

the openApi (contract first approach) could be used with camunda,  
which means to us:  
1 public Api definitions with openApi standards    
2 client code generation

# installation

initialize the server project with https://start.camunda.com/, download it, and run.  
deploy the BPM definition (src/test/resources/camunda.cfg.xml) to the camunda server (could use the Camunda Modeler).
run this project and trigger the endpoints, which is defined in
src/main/java/camu/openapi/client/controller/LottoController.java.

# technical details

use maven plug-in "openapi-generator-maven-plugin" to
generate Camunda openAPI client codes:  
mvn clean compile

For sending Objects in variables the JSON string is used.  
the ObjectValue of Camunda is not used, it is not recommended, because the serialization depends on Camunda-Spin:
objectValue(new CustomClass("Fool")).serializationDataFormat(JSON).create();

ExternalTask tests:  
use Mockito mock tests.  
Process tests:  
use ProcessEngineExtension to test unit functions.  