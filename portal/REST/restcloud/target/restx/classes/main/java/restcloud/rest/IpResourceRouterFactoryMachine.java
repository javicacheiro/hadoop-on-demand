package main.java.restcloud.rest;

import com.google.common.collect.ImmutableSet;
import restx.factory.*;
import main.java.restcloud.rest.IpResourceRouter;

@Machine
public class IpResourceRouterFactoryMachine extends SingleNameFactoryMachine<IpResourceRouter> {
    public static final Name<IpResourceRouter> NAME = Name.of(IpResourceRouter.class, "IpResourceRouter");

    public IpResourceRouterFactoryMachine() {
        super(0, new StdMachineEngine<IpResourceRouter>(NAME, BoundlessComponentBox.FACTORY) {
private final Factory.Query<main.java.restcloud.rest.IpResource> resource = Factory.Query.byClass(main.java.restcloud.rest.IpResource.class).mandatory();
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
            protected IpResourceRouter doNewComponent(SatisfiedBOM satisfiedBOM) {
                return new IpResourceRouter(
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
