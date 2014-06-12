package main.java.restcloud.rest;

import com.google.common.collect.ImmutableSet;
import restx.factory.*;
import main.java.restcloud.rest.KeyResource;

@Machine
public class KeyResourceFactoryMachine extends SingleNameFactoryMachine<KeyResource> {
    public static final Name<KeyResource> NAME = Name.of(KeyResource.class, "KeyResource");

    public KeyResourceFactoryMachine() {
        super(0, new StdMachineEngine<KeyResource>(NAME, BoundlessComponentBox.FACTORY) {


            @Override
            public BillOfMaterials getBillOfMaterial() {
                return new BillOfMaterials(ImmutableSet.<Factory.Query<?>>of(

                ));
            }

            @Override
            protected KeyResource doNewComponent(SatisfiedBOM satisfiedBOM) {
                return new KeyResource(

                );
            }
        });
    }

}
