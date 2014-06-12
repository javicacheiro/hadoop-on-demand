package main.java.restcloud.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.base.Optional;
import static com.google.common.base.Preconditions.checkNotNull;

import restx.common.Types;
import restx.*;
import restx.entity.*;
import restx.http.*;
import restx.factory.*;
import restx.security.*;
import static restx.security.Permissions.*;
import restx.description.*;
import restx.converters.MainStringConverter;
import static restx.common.MorePreconditions.checkPresent;

import javax.validation.Validator;
import static restx.validation.Validations.checkValid;

import java.io.IOException;
import java.io.PrintWriter;

@Component(priority = 0)

public class UserResourceRouter extends RestxRouter {

    public UserResourceRouter(
                    final UserResource resource,
                    final EntityRequestBodyReaderRegistry readerRegistry,
                    final EntityResponseWriterRegistry writerRegistry,
                    final MainStringConverter converter,
                    final Validator validator,
                    final RestxSecurityManager securityManager) {
        super(
            "default", "UserResourceRouter", new RestxRoute[] {
        new StdEntityRoute<main.java.restcloud.domain.Message, main.java.restcloud.domain.Message>("default#UserResource#test",
                readerRegistry.<main.java.restcloud.domain.Message>build(main.java.restcloud.domain.Message.class, Optional.<String>absent()),
                writerRegistry.<main.java.restcloud.domain.Message>build(main.java.restcloud.domain.Message.class, Optional.<String>absent()),
                new StdRestxRequestMatcher("POST", "/test"),
                HttpStatus.OK, RestxLogLevel.DEFAULT) {
            @Override
            protected Optional<main.java.restcloud.domain.Message> doRoute(RestxRequest request, RestxRequestMatch match, main.java.restcloud.domain.Message body) throws IOException {
                securityManager.check(request, open());
                return Optional.of(resource.test(
                        /* [BODY] msg */ checkValid(validator, body)
                ));
            }

            @Override
            protected void describeOperation(OperationDescription operation) {
                super.describeOperation(operation);
                                OperationParameterDescription msg = new OperationParameterDescription();
                msg.name = "msg";
                msg.paramType = OperationParameterDescription.ParamType.body;
                msg.dataType = "Message";
                msg.schemaKey = "main.java.restcloud.domain.Message";
                msg.required = true;
                operation.parameters.add(msg);


                operation.responseClass = "Message";
                operation.inEntitySchemaKey = "main.java.restcloud.domain.Message";
                operation.outEntitySchemaKey = "main.java.restcloud.domain.Message";
                operation.sourceLocation = "main.java.restcloud.rest.UserResource#test(main.java.restcloud.domain.Message)";
            }
        },
        new StdEntityRoute<main.java.restcloud.domain.Login, main.java.restcloud.domain.Message>("default#UserResource#userRegister",
                readerRegistry.<main.java.restcloud.domain.Login>build(main.java.restcloud.domain.Login.class, Optional.<String>absent()),
                writerRegistry.<main.java.restcloud.domain.Message>build(main.java.restcloud.domain.Message.class, Optional.<String>absent()),
                new StdRestxRequestMatcher("POST", "/users"),
                HttpStatus.OK, RestxLogLevel.DEFAULT) {
            @Override
            protected Optional<main.java.restcloud.domain.Message> doRoute(RestxRequest request, RestxRequestMatch match, main.java.restcloud.domain.Login body) throws IOException {
                securityManager.check(request, open());
                return Optional.of(resource.userRegister(
                        /* [BODY] login */ checkValid(validator, body)
                ));
            }

            @Override
            protected void describeOperation(OperationDescription operation) {
                super.describeOperation(operation);
                                OperationParameterDescription login = new OperationParameterDescription();
                login.name = "login";
                login.paramType = OperationParameterDescription.ParamType.body;
                login.dataType = "Login";
                login.schemaKey = "main.java.restcloud.domain.Login";
                login.required = true;
                operation.parameters.add(login);


                operation.responseClass = "Message";
                operation.inEntitySchemaKey = "main.java.restcloud.domain.Login";
                operation.outEntitySchemaKey = "main.java.restcloud.domain.Message";
                operation.sourceLocation = "main.java.restcloud.rest.UserResource#userRegister(main.java.restcloud.domain.Login)";
            }
        },
        });
    }

}
