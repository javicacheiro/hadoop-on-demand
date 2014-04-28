package main.java.restcloud.rest;

import com.google.common.collect.ImmutableSet;
import restx.factory.*;
import main.java.restcloud.rest.ClusterResource;

@Machine
public class ClusterResourceFactoryMachine extends SingleNameFactoryMachine<ClusterResource> {
    public static final Name<ClusterResource> NAME = Name.of(ClusterResource.class, "ClusterResource");

    public ClusterResourceFactoryMachine() {
        super(0, new StdMachineEngine<ClusterResource>(NAME, BoundlessComponentBox.FACTORY) {


            @Override
            public BillOfMaterials getBillOfMaterial() {
                return new BillOfMaterials(ImmutableSet.<Factory.Query<?>>of(

                ));
            }

            @Override
            protected ClusterResource doNewComponent(SatisfiedBOM satisfiedBOM) {
                return new ClusterResource(

                );
            }
        });
    }

}
