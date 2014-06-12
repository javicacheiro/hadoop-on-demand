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

public class HelloResourceRouter extends RestxRouter {

    public HelloResourceRouter(
                    final HelloResource resource,
                    final EntityRequestBodyReaderRegistry readerRegistry,
                    final EntityResponseWriterRegistry writerRegistry,
                    final MainStringConverter converter,
                    final Validator validator,
                    final RestxSecurityManager securityManager) {
        super(
            "default", "HelloResourceRouter", new RestxRoute[] {
        new StdEntityRoute<Void, main.java.restcloud.domain.Message>("default#HelloResource#sayHello",
                readerRegistry.<Void>build(Void.class, Optional.<String>absent()),
                writerRegistry.<main.java.restcloud.domain.Message>build(main.java.restcloud.domain.Message.class, Optional.<String>absent()),
                new StdRestxRequestMatcher("GET", "/message"),
                HttpStatus.OK, RestxLogLevel.DEFAULT) {
            @Override
            protected Optional<main.java.restcloud.domain.Message> doRoute(RestxRequest request, RestxRequestMatch match, Void body) throws IOException {
                securityManager.check(request, hasRole("hello"));
                return Optional.of(resource.sayHello(
                        
                ));
            }

            @Override
            protected void describeOperation(OperationDescription operation) {
                super.describeOperation(operation);
                

                operation.responseClass = "Message";
                operation.inEntitySchemaKey = "";
                operation.outEntitySchemaKey = "main.java.restcloud.domain.Message";
                operation.sourceLocation = "main.java.restcloud.rest.HelloResource#sayHello()";
            }
        },
        new StdEntityRoute<Void, main.java.restcloud.domain.Message>("default#HelloResource#helloPublic",
                readerRegistry.<Void>build(Void.class, Optional.<String>absent()),
                writerRegistry.<main.java.restcloud.domain.Message>build(main.java.restcloud.domain.Message.class, Optional.<String>absent()),
                new StdRestxRequestMatcher("GET", "/hello"),
                HttpStatus.OK, RestxLogLevel.DEFAULT) {
            @Override
            protected Optional<main.java.restcloud.domain.Message> doRoute(RestxRequest request, RestxRequestMatch match, Void body) throws IOException {
                securityManager.check(request, open());
                return Optional.of(resource.helloPublic(
                        /* [QUERY] who */ checkPresent(request.getQueryParam("who"), "query param who is required")
                ));
            }

            @Override
            protected void describeOperation(OperationDescription operation) {
                super.describeOperation(operation);
                                OperationParameterDescription who = new OperationParameterDescription();
                who.name = "who";
                who.paramType = OperationParameterDescription.ParamType.query;
                who.dataType = "string";
                who.schemaKey = "";
                who.required = true;
                operation.parameters.add(who);


                operation.responseClass = "Message";
                operation.inEntitySchemaKey = "";
                operation.outEntitySchemaKey = "main.java.restcloud.domain.Message";
                operation.sourceLocation = "main.java.restcloud.rest.HelloResource#helloPublic(java.lang.String)";
            }
        },
        new StdEntityRoute<main.java.restcloud.domain.Message, main.java.restcloud.domain.Message>("default#HelloResource#sayHello",
                readerRegistry.<main.java.restcloud.domain.Message>build(main.java.restcloud.domain.Message.class, Optional.<String>absent()),
                writerRegistry.<main.java.restcloud.domain.Message>build(main.java.restcloud.domain.Message.class, Optional.<String>absent()),
                new StdRestxRequestMatcher("POST", "/message/{id}"),
                HttpStatus.OK, RestxLogLevel.DEFAULT) {
            @Override
            protected Optional<main.java.restcloud.domain.Message> doRoute(RestxRequest request, RestxRequestMatch match, main.java.restcloud.domain.Message body) throws IOException {
                securityManager.check(request, isAuthenticated());
                return Optional.of(resource.sayHello(
                        /* [PATH] id */ match.getPathParam("id"),
                        /* [BODY] msg */ checkValid(validator, body)
                ));
            }

            @Override
            protected void describeOperation(OperationDescription operation) {
                super.describeOperation(operation);
                                OperationParameterDescription id = new OperationParameterDescription();
                id.name = "id";
                id.paramType = OperationParameterDescription.ParamType.path;
                id.dataType = "string";
                id.schemaKey = "";
                id.required = true;
                operation.parameters.add(id);

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
                operation.sourceLocation = "main.java.restcloud.rest.HelloResource#sayHello(java.lang.String,main.java.restcloud.domain.Message)";
            }
        },
        });
    }

}
