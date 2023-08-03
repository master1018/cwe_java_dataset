
package com.accenture.examples.rest;
import org.platformlambda.core.exception.AppException;
import org.platformlambda.core.models.EventEnvelope;
import org.platformlambda.core.models.Kv;
import org.platformlambda.core.system.Platform;
import org.platformlambda.core.system.PostOffice;
import org.platformlambda.models.ObjectWithGenericType;
import org.platformlambda.models.SamplePoJo;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
@Path("/hello")
public class HelloPoJo {
    @GET
    @Path("/pojo/{id}")
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    public Object getPoJo(@PathParam("id") Integer id) throws TimeoutException, AppException, IOException {
        PostOffice po = PostOffice.getInstance();
        EventEnvelope response = po.request("hello.pojo", 3000, new Kv("id", id));
        if (response.getBody() instanceof SamplePoJo) {
            return response.getBody();
        } else {
            if (response.getBody() == null) {
                throw new AppException(500, "Invalid service response. Expect: " +
                        SamplePoJo.class.getName() + ", actual: null");
            } else {
                throw new AppException(500, "Invalid service response. Expect: " +
                        SamplePoJo.class.getName() + ", actual: " + response.getBody().getClass().getName());
            }
        }
    }
    @GET
    @Path("/generic/{id}")
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    public Object getGeneric(@PathParam("id") Integer id) throws TimeoutException, AppException, IOException {
        PostOffice po = PostOffice.getInstance();
        EventEnvelope response = po.request("hello.generic", 3000, new Kv("id", id.toString()));
        if (response.hasError()) {
            throw new AppException(response.getStatus(), response.getError());
        }
        if (response.getBody() instanceof ObjectWithGenericType) {
            ObjectWithGenericType result = (ObjectWithGenericType) response.getBody();
            if (result.getContent() instanceof SamplePoJo) {
                return response.getBody();
            } else {
                throw new AppException(500, "Invalid service response. Expect: " +
                        SamplePoJo.class.getSimpleName() + " as a parametric type of " +
                        ObjectWithGenericType.class.getName()+", actual: " + response.getBody().getClass().getName());
            }
        } else {
            if (response.getBody() == null) {
                throw new AppException(500, "Invalid service response. Expect: " +
                        ObjectWithGenericType.class.getName() + ", actual: null");
            } else {
                throw new AppException(500, "Invalid service response. Expect: " +
                        ObjectWithGenericType.class.getName() + ", actual: " + response.getBody().getClass().getName());
            }
        }
    }
    @POST
    @Path("/echo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Object echoPojo(SamplePoJo pojo) {
        pojo.setOrigin(Platform.getInstance().getOrigin());
        return pojo;
    }
}
