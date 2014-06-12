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

public class IpResourceRouter extends RestxRouter {

    public IpResourceRouter(
                    final IpResource resource,
                    final EntityRequestBodyReaderRegistry readerRegistry,
                    final EntityResponseWriterRegistry writerRegistry,
                    final MainStringConverter converter,
                    final Validator validator,
                    final RestxSecurityManager securityManager) {
        super(
            "default", "IpResourceRouter", new RestxRoute[] {
        new StdEntityRoute<main.java.restcloud.domain.IP, main.java.restcloud.domain.Message>("default#IpResource#addSSHKey",
                readerRegistry.<main.java.restcloud.domain.IP>build(main.java.restcloud.domain.IP.class, Optional.<String>absent()),
                writerRegistry.<main.java.restcloud.domain.Message>build(main.java.restcloud.domain.Message.class, Optional.<String>absent()),
                new StdRestxRequestMatcher("POST", "/ip"),
                HttpStatus.OK, RestxLogLevel.DEFAULT) {
            @Override
            protected Optional<main.java.restcloud.domain.Message> doRoute(RestxRequest request, RestxRequestMatch match, main.java.restcloud.domain.IP body) throws IOException {
                securityManager.check(request, open());
                return Optional.of(resource.addSSHKey(
                        /* [BODY] ip */ checkValid(validator, body)
                ));
            }

            @Override
            protected void describeOperation(OperationDescription operation) {
                super.describeOperation(operation);
                                OperationParameterDescription ip = new OperationParameterDescription();
                ip.name = "ip";
                ip.paramType = OperationParameterDescription.ParamType.body;
                ip.dataType = "IP";
                ip.schemaKey = "main.java.restcloud.domain.IP";
                ip.required = true;
                operation.parameters.add(ip);


                operation.responseClass = "Message";
                operation.inEntitySchemaKey = "main.java.restcloud.domain.IP";
                operation.outEntitySchemaKey = "main.java.restcloud.domain.Message";
                operation.sourceLocation = "main.java.restcloud.rest.IpResource#addSSHKey(main.java.restcloud.domain.IP)";
            }
        },
        new StdEntityRoute<main.java.restcloud.domain.IP, main.java.restcloud.domain.Message>("default#IpResource#delIp",
                readerRegistry.<main.java.restcloud.domain.IP>build(main.java.restcloud.domain.IP.class, Optional.<String>absent()),
                writerRegistry.<main.java.restcloud.domain.Message>build(main.java.restcloud.domain.Message.class, Optional.<String>absent()),
                new StdRestxRequestMatcher("DELETE", "/ip"),
                HttpStatus.OK, RestxLogLevel.DEFAULT) {
            @Override
            protected Optional<main.java.restcloud.domain.Message> doRoute(RestxRequest request, RestxRequestMatch match, main.java.restcloud.domain.IP body) throws IOException {
                securityManager.check(request, open());
                return Optional.of(resource.delIp(
                        /* [BODY] ip */ checkValid(validator, body)
                ));
            }

            @Override
            protected void describeOperation(OperationDescription operation) {
                super.describeOperation(operation);
                                OperationParameterDescription ip = new OperationParameterDescription();
                ip.name = "ip";
                ip.paramType = OperationParameterDescription.ParamType.body;
                ip.dataType = "IP";
                ip.schemaKey = "main.java.restcloud.domain.IP";
                ip.required = true;
                operation.parameters.add(ip);


                operation.responseClass = "Message";
                operation.inEntitySchemaKey = "main.java.restcloud.domain.IP";
                operation.outEntitySchemaKey = "main.java.restcloud.domain.Message";
                operation.sourceLocation = "main.java.restcloud.rest.IpResource#delIp(main.java.restcloud.domain.IP)";
            }
        },
        new StdEntityRoute<Void, main.java.restcloud.domain.IPs>("default#IpResource#getIpsForUser",
                readerRegistry.<Void>build(Void.class, Optional.<String>absent()),
                writerRegistry.<main.java.restcloud.domain.IPs>build(main.java.restcloud.domain.IPs.class, Optional.<String>absent()),
                new StdRestxRequestMatcher("GET", "/ip"),
                HttpStatus.OK, RestxLogLevel.DEFAULT) {
            @Override
            protected Optional<main.java.restcloud.domain.IPs> doRoute(RestxRequest request, RestxRequestMatch match, Void body) throws IOException {
                securityManager.check(request, open());
                return Optional.of(resource.getIpsForUser(
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


                operation.responseClass = "IPs";
                operation.inEntitySchemaKey = "";
                operation.outEntitySchemaKey = "main.java.restcloud.domain.IPs";
                operation.sourceLocation = "main.java.restcloud.rest.IpResource#getIpsForUser(java.lang.String)";
            }
        },
        });
    }

}
