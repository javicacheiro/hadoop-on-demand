package main.java.restcloud.rest;

import com.google.common.collect.ImmutableSet;
import restx.factory.*;
import main.java.restcloud.rest.IpResource;

@Machine
public class IpResourceFactoryMachine extends SingleNameFactoryMachine<IpResource> {
    public static final Name<IpResource> NAME = Name.of(IpResource.class, "IpResource");

    public IpResourceFactoryMachine() {
        super(0, new StdMachineEngine<IpResource>(NAME, BoundlessComponentBox.FACTORY) {


            @Override
            public BillOfMaterials getBillOfMaterial() {
                return new BillOfMaterials(ImmutableSet.<Factory.Query<?>>of(

                ));
            }

            @Override
            protected IpResource doNewComponent(SatisfiedBOM satisfiedBOM) {
                return new IpResource(

                );
            }
        });
    }

}
