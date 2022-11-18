package be.vinci.api;

import be.vinci.classes.User;
import be.vinci.instances.InstanceGraph1;
import be.vinci.services.ClassAnalyzer;
import be.vinci.services.InstancesAnalyzer;
import jakarta.json.JsonStructure;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jdk.javadoc.internal.doclets.toolkit.builders.ConstructorBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
        // chercher la classe
        // chercher le constructeur
        // invoquer le constructeur pour avoir l'objet
        Class classe;
        try {
            classe = Class.forName(builderClassname);
        } catch (ClassNotFoundException e) {
            throw new WebApplicationException(404);
        }
        Constructor constructeur = classe.getDeclaredConstructors()[0];
        Object objet;
        try {
            objet = constructeur.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        // TODO change this line to use the query parameter, and generate dynamically the builder
        // TODO change this line to avoid calling initInstanceGraph() directly

        InstanceGraph1 builder = new InstanceGraph1();
        Object instanceGraph = builder.initInstanceGraph();
        InstancesAnalyzer analyzer = new InstancesAnalyzer(instanceGraph);
        return analyzer.getFullInfo();
    }
}
