package be.vinci.api;

import be.vinci.classes.User;
import be.vinci.instances.InstanceGraph1;
import be.vinci.services.ClassAnalyzer;
import be.vinci.services.InstancesAnalyzer;
import be.vinci.utils.InstanceGraphBuilder;
import jakarta.json.JsonStructure;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
//import jdk.javadoc.internal.doclets.toolkit.builders.ConstructorBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Send instances graph data to make object diagrams
 * <p>
 * The instances graphs are initialized by a class containing the "initInstanceGraph" method,
 * building the instance graph, and returning it.
 * <p>
 * The "instance builder class name" must be given and present into the "instances" package
 */
@Path("instances")
public class Instances {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonStructure getInstanceGraphInfo(@QueryParam("builderclassname") String builderClassname) {
        try {
           Class builderClass = Class.forName("be.vinci.instances"+builderClassname);
           Object builderObject = builderClass.getConstructor().newInstance();
            for (Method m : builderClass.getDeclaredMethods()) {
                if(m.isAnnotationPresent(InstanceGraphBuilder.class)){
                    Object instanceGraph = m.invoke(builderObject);
                    InstancesAnalyzer analyze = new InstancesAnalyzer(instanceGraph);
                    return analyze.getFullInfo();
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignore) {
            throw new InternalError();
        }
        throw new WebApplicationException(404);
    }

}
