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

public class ClusterResourceRouter extends RestxRouter {

    public ClusterResourceRouter(
                    final ClusterResource resource,
                    final EntityRequestBodyReaderRegistry readerRegistry,
                    final EntityResponseWriterRegistry writerRegistry,
                    final MainStringConverter converter,
                    final Validator validator,
                    final RestxSecurityManager securityManager) {
        super(
            "default", "ClusterResourceRouter", new RestxRoute[] {
        new StdEntityRoute<Void, main.java.restcloud.domain.ClusterList>("default#ClusterResource#listClusters",
                readerRegistry.<Void>build(Void.class, Optional.<String>absent()),
                writerRegistry.<main.java.restcloud.domain.ClusterList>build(main.java.restcloud.domain.ClusterList.class, Optional.<String>absent()),
                new StdRestxRequestMatcher("GET", "/clusters"),
                HttpStatus.OK, RestxLogLevel.DEFAULT) {
            @Override
            protected Optional<main.java.restcloud.domain.ClusterList> doRoute(RestxRequest request, RestxRequestMatch match, Void body) throws IOException {
                securityManager.check(request, open());
                return Optional.of(resource.listClusters(
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


                operation.responseClass = "ClusterList";
                operation.inEntitySchemaKey = "";
                operation.outEntitySchemaKey = "main.java.restcloud.domain.ClusterList";
                operation.sourceLocation = "main.java.restcloud.rest.ClusterResource#listClusters(java.lang.String)";
            }
        },
        new StdEntityRoute<Void, main.java.restcloud.domain.ClusterInfo>("default#ClusterResource#obtainClusterInfo",
                readerRegistry.<Void>build(Void.class, Optional.<String>absent()),
                writerRegistry.<main.java.restcloud.domain.ClusterInfo>build(main.java.restcloud.domain.ClusterInfo.class, Optional.<String>absent()),
                new StdRestxRequestMatcher("GET", "/clusters/{id}"),
                HttpStatus.OK, RestxLogLevel.DEFAULT) {
            @Override
            protected Optional<main.java.restcloud.domain.ClusterInfo> doRoute(RestxRequest request, RestxRequestMatch match, Void body) throws IOException {
                securityManager.check(request, open());
                return Optional.of(resource.obtainClusterInfo(
                        /* [PATH] id */ match.getPathParam("id")
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


                operation.responseClass = "ClusterInfo";
                operation.inEntitySchemaKey = "";
                operation.outEntitySchemaKey = "main.java.restcloud.domain.ClusterInfo";
                operation.sourceLocation = "main.java.restcloud.rest.ClusterResource#obtainClusterInfo(java.lang.String)";
            }
        },
        new StdEntityRoute<main.java.restcloud.domain.HadoopStartRequest, main.java.restcloud.domain.Message>("default#ClusterResource#createHadoopCluster",
                readerRegistry.<main.java.restcloud.domain.HadoopStartRequest>build(main.java.restcloud.domain.HadoopStartRequest.class, Optional.<String>absent()),
                writerRegistry.<main.java.restcloud.domain.Message>build(main.java.restcloud.domain.Message.class, Optional.<String>absent()),
                new StdRestxRequestMatcher("POST", "/clusters"),
                HttpStatus.OK, RestxLogLevel.DEFAULT) {
            @Override
            protected Optional<main.java.restcloud.domain.Message> doRoute(RestxRequest request, RestxRequestMatch match, main.java.restcloud.domain.HadoopStartRequest body) throws IOException {
                securityManager.check(request, open());
                return Optional.of(resource.createHadoopCluster(
                        /* [BODY] hsr */ checkValid(validator, body)
                ));
            }

            @Override
            protected void describeOperation(OperationDescription operation) {
                super.describeOperation(operation);
                                OperationParameterDescription hsr = new OperationParameterDescription();
                hsr.name = "hsr";
                hsr.paramType = OperationParameterDescription.ParamType.body;
                hsr.dataType = "HadoopStartRequest";
                hsr.schemaKey = "main.java.restcloud.domain.HadoopStartRequest";
                hsr.required = true;
                operation.parameters.add(hsr);


                operation.responseClass = "Message";
                operation.inEntitySchemaKey = "main.java.restcloud.domain.HadoopStartRequest";
                operation.outEntitySchemaKey = "main.java.restcloud.domain.Message";
                operation.sourceLocation = "main.java.restcloud.rest.ClusterResource#createHadoopCluster(main.java.restcloud.domain.HadoopStartRequest)";
            }
        },
        new StdEntityRoute<Void, main.java.restcloud.domain.Message>("default#ClusterResource#deleteHadoopCluster",
                readerRegistry.<Void>build(Void.class, Optional.<String>absent()),
                writerRegistry.<main.java.restcloud.domain.Message>build(main.java.restcloud.domain.Message.class, Optional.<String>absent()),
                new StdRestxRequestMatcher("DELETE", "/clusters/{id}"),
                HttpStatus.OK, RestxLogLevel.DEFAULT) {
            @Override
            protected Optional<main.java.restcloud.domain.Message> doRoute(RestxRequest request, RestxRequestMatch match, Void body) throws IOException {
                securityManager.check(request, open());
                return Optional.of(resource.deleteHadoopCluster(
                        /* [PATH] id */ match.getPathParam("id")
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


                operation.responseClass = "Message";
                operation.inEntitySchemaKey = "";
                operation.outEntitySchemaKey = "main.java.restcloud.domain.Message";
                operation.sourceLocation = "main.java.restcloud.rest.ClusterResource#deleteHadoopCluster(java.lang.String)";
            }
        },
        });
    }

}
