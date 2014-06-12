package main.java.restcloud.rest;

import com.google.common.collect.ImmutableSet;
import restx.factory.*;
import main.java.restcloud.rest.UserResource;

@Machine
public class UserResourceFactoryMachine extends SingleNameFactoryMachine<UserResource> {
    public static final Name<UserResource> NAME = Name.of(UserResource.class, "UserResource");

    public UserResourceFactoryMachine() {
        super(0, new StdMachineEngine<UserResource>(NAME, BoundlessComponentBox.FACTORY) {


            @Override
            public BillOfMaterials getBillOfMaterial() {
                return new BillOfMaterials(ImmutableSet.<Factory.Query<?>>of(

                ));
            }

            @Override
            protected UserResource doNewComponent(SatisfiedBOM satisfiedBOM) {
                return new UserResource(

                );
            }
        });
    }

}
