package main.java.restcloud.rest;

import com.google.common.collect.ImmutableSet;
import restx.factory.*;
import main.java.restcloud.rest.KeyResourceRouter;

@Machine
public class KeyResourceRouterFactoryMachine extends SingleNameFactoryMachine<KeyResourceRouter> {
    public static final Name<KeyResourceRouter> NAME = Name.of(KeyResourceRouter.class, "KeyResourceRouter");

    public KeyResourceRouterFactoryMachine() {
        super(0, new StdMachineEngine<KeyResourceRouter>(NAME, BoundlessComponentBox.FACTORY) {
private final Factory.Query<main.java.restcloud.rest.KeyResource> resource = Factory.Query.byClass(main.java.restcloud.rest.KeyResource.class).mandatory();
private final Factory.Query<restx.entity.EntityRequestBodyReaderRegistry> readerRegistry = Factory.Query.byClass(restx.entity.EntityRequestBodyReaderRegistry.class).mandatory();
private final Factory.Query<restx.entity.EntityResponseWriterRegistry> writerRegistry = Factory.Query.byClass(restx.entity.EntityResponseWriterRegistry.class).mandatory();
private final Factory.Query<restx.converters.MainStringConverter> converter = Factory.Query.byClass(restx.converters.MainStringConverter.class).mandatory();
private final Factory.Query<javax.validation.Validator> validator = Factory.Query.byClass(javax.validation.Validator.class).mandatory();
private final Factory.Query<restx.security.RestxSecurityManager> securityManager = Factory.Query.byClass(restx.security.RestxSecurityManager.class).mandatory();

            @Override
            public BillOfMaterials getBillOfMaterial() {
                return new BillOfMaterials(ImmutableSet.<Factory.Query<?>>of(
resource,
readerRegistry,
writerRegistry,
converter,
validator,
securityManager
                ));
            }

            @Override
            protected KeyResourceRouter doNewComponent(SatisfiedBOM satisfiedBOM) {
                return new KeyResourceRouter(
satisfiedBOM.getOne(resource).get().getComponent(),
satisfiedBOM.getOne(readerRegistry).get().getComponent(),
satisfiedBOM.getOne(writerRegistry).get().getComponent(),
satisfiedBOM.getOne(converter).get().getComponent(),
satisfiedBOM.getOne(validator).get().getComponent(),
satisfiedBOM.getOne(securityManager).get().getComponent()
                );
            }
        });
    }

}
