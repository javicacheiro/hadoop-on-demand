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

public class KeyResourceRouter extends RestxRouter {

    public KeyResourceRouter(
                    final KeyResource resource,
                    final EntityRequestBodyReaderRegistry readerRegistry,
                    final EntityResponseWriterRegistry writerRegistry,
                    final MainStringConverter converter,
                    final Validator validator,
                    final RestxSecurityManager securityManager) {
        super(
            "default", "KeyResourceRouter", new RestxRoute[] {
        new StdEntityRoute<main.java.restcloud.domain.SSHKey, main.java.restcloud.domain.Message>("default#KeyResource#addSSHKey",
                readerRegistry.<main.java.restcloud.domain.SSHKey>build(main.java.restcloud.domain.SSHKey.class, Optional.<String>absent()),
                writerRegistry.<main.java.restcloud.domain.Message>build(main.java.restcloud.domain.Message.class, Optional.<String>absent()),
                new StdRestxRequestMatcher("POST", "/key"),
                HttpStatus.OK, RestxLogLevel.DEFAULT) {
            @Override
            protected Optional<main.java.restcloud.domain.Message> doRoute(RestxRequest request, RestxRequestMatch match, main.java.restcloud.domain.SSHKey body) throws IOException {
                securityManager.check(request, open());
                return Optional.of(resource.addSSHKey(
                        /* [BODY] key */ checkValid(validator, body)
                ));
            }

            @Override
            protected void describeOperation(OperationDescription operation) {
                super.describeOperation(operation);
                                OperationParameterDescription key = new OperationParameterDescription();
                key.name = "key";
                key.paramType = OperationParameterDescription.ParamType.body;
                key.dataType = "SSHKey";
                key.schemaKey = "main.java.restcloud.domain.SSHKey";
                key.required = true;
                operation.parameters.add(key);


                operation.responseClass = "Message";
                operation.inEntitySchemaKey = "main.java.restcloud.domain.SSHKey";
                operation.outEntitySchemaKey = "main.java.restcloud.domain.Message";
                operation.sourceLocation = "main.java.restcloud.rest.KeyResource#addSSHKey(main.java.restcloud.domain.SSHKey)";
            }
        },
        new StdEntityRoute<main.java.restcloud.domain.SSHKey, main.java.restcloud.domain.Message>("default#KeyResource#delKey",
                readerRegistry.<main.java.restcloud.domain.SSHKey>build(main.java.restcloud.domain.SSHKey.class, Optional.<String>absent()),
                writerRegistry.<main.java.restcloud.domain.Message>build(main.java.restcloud.domain.Message.class, Optional.<String>absent()),
                new StdRestxRequestMatcher("DELETE", "/key"),
                HttpStatus.OK, RestxLogLevel.DEFAULT) {
            @Override
            protected Optional<main.java.restcloud.domain.Message> doRoute(RestxRequest request, RestxRequestMatch match, main.java.restcloud.domain.SSHKey body) throws IOException {
                securityManager.check(request, open());
                return Optional.of(resource.delKey(
                        /* [BODY] key */ checkValid(validator, body)
                ));
            }

            @Override
            protected void describeOperation(OperationDescription operation) {
                super.describeOperation(operation);
                                OperationParameterDescription key = new OperationParameterDescription();
                key.name = "key";
                key.paramType = OperationParameterDescription.ParamType.body;
                key.dataType = "SSHKey";
                key.schemaKey = "main.java.restcloud.domain.SSHKey";
                key.required = true;
                operation.parameters.add(key);


                operation.responseClass = "Message";
                operation.inEntitySchemaKey = "main.java.restcloud.domain.SSHKey";
                operation.outEntitySchemaKey = "main.java.restcloud.domain.Message";
                operation.sourceLocation = "main.java.restcloud.rest.KeyResource#delKey(main.java.restcloud.domain.SSHKey)";
            }
        },
        new StdEntityRoute<main.java.restcloud.domain.SSHKeys, main.java.restcloud.domain.Message>("default#KeyResource#multidelKey",
                readerRegistry.<main.java.restcloud.domain.SSHKeys>build(main.java.restcloud.domain.SSHKeys.class, Optional.<String>absent()),
                writerRegistry.<main.java.restcloud.domain.Message>build(main.java.restcloud.domain.Message.class, Optional.<String>absent()),
                new StdRestxRequestMatcher("DELETE", "/keys"),
                HttpStatus.OK, RestxLogLevel.DEFAULT) {
            @Override
            protected Optional<main.java.restcloud.domain.Message> doRoute(RestxRequest request, RestxRequestMatch match, main.java.restcloud.domain.SSHKeys body) throws IOException {
                securityManager.check(request, open());
                return Optional.of(resource.multidelKey(
                        /* [BODY] keys */ checkValid(validator, body)
                ));
            }

            @Override
            protected void describeOperation(OperationDescription operation) {
                super.describeOperation(operation);
                                OperationParameterDescription keys = new OperationParameterDescription();
                keys.name = "keys";
                keys.paramType = OperationParameterDescription.ParamType.body;
                keys.dataType = "SSHKeys";
                keys.schemaKey = "main.java.restcloud.domain.SSHKeys";
                keys.required = true;
                operation.parameters.add(keys);


                operation.responseClass = "Message";
                operation.inEntitySchemaKey = "main.java.restcloud.domain.SSHKeys";
                operation.outEntitySchemaKey = "main.java.restcloud.domain.Message";
                operation.sourceLocation = "main.java.restcloud.rest.KeyResource#multidelKey(main.java.restcloud.domain.SSHKeys)";
            }
        },
        new StdEntityRoute<Void, main.java.restcloud.domain.SSHKeys>("default#KeyResource#getKeysForUser",
                readerRegistry.<Void>build(Void.class, Optional.<String>absent()),
                writerRegistry.<main.java.restcloud.domain.SSHKeys>build(main.java.restcloud.domain.SSHKeys.class, Optional.<String>absent()),
                new StdRestxRequestMatcher("GET", "/keys"),
                HttpStatus.OK, RestxLogLevel.DEFAULT) {
            @Override
            protected Optional<main.java.restcloud.domain.SSHKeys> doRoute(RestxRequest request, RestxRequestMatch match, Void body) throws IOException {
                securityManager.check(request, open());
                return Optional.of(resource.getKeysForUser(
                        /* [QUERY] user */ checkPresent(request.getQueryParam("user"), "query param user is required")
                ));
            }

            @Override
            protected void describeOperation(OperationDescription operation) {
                super.describeOperation(operation);
                                OperationParameterDescription user = new OperationParameterDescription();
                user.name = "user";
                user.paramType = OperationParameterDescription.ParamType.query;
                user.dataType = "string";
                user.schemaKey = "";
                user.required = true;
                operation.parameters.add(user);


                operation.responseClass = "SSHKeys";
                operation.inEntitySchemaKey = "";
                operation.outEntitySchemaKey = "main.java.restcloud.domain.SSHKeys";
                operation.sourceLocation = "main.java.restcloud.rest.KeyResource#getKeysForUser(java.lang.String)";
            }
        },
        });
    }

}
