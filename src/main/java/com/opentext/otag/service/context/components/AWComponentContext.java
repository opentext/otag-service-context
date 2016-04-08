/**
 * Copyright © 2016 Open Text.  All Rights Reserved.
 */
package com.opentext.otag.service.context.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * AppWorks Component Context:
 * <p>
 * The context used to store single instances of developer supplied,
 * service-management-agent instantiated, {@link AWComponent}s. These
 * components allow service developers to hook into some of the AppWorks
 * Gateways support/utility API's. We provide centralised access to these
 * components within the AppWorks Services' Classloader context, this class
 * should <strong>not</strong> be deployed at the container level i.e.
 * in the host containers own /lib directory.
 *
 * @author Rhys Evans rhyse@opentext.com
 * @version 16.0.0
 */
public class AWComponentContext {

    private static final Logger LOG = LoggerFactory.getLogger(AWComponentContext.class);

    private static final Map<Class<? extends AWComponent>, AWComponent> ctx = new HashMap<>();

    /**
     * Retrieve an instance by its type, null will be returned if this repository does
     * not contain an instance of that type.
     *
     * @param type the type of component you are attempting to retrieve
     * @param <T>  a type that extends {@code AWComponent}
     * @return component instance
     */
    public static <T extends AWComponent> T getComponent(Class<T> type) {
        return type.cast(ctx.get(type));
    }


    /**
     * Add entries to the component context. If we already have the instance (some components
     * may implement multiple {@code AWComponent} interfaces) then we retain it.
     *
     * @param toAdd components to add to the registry
     * @return true if all supplied components were added (i.e. did not already exist in the context)
     */
    public static boolean add(AWComponent... toAdd) {
        final boolean[] success = {true};
        Arrays.stream(toAdd).forEach(awComponent -> {
            if (awComponent != null) {
                if (ctx.get(awComponent.getClass()) != null) {
                    LOG.warn("Context already contains this an instance of this " +
                            "type, " + toAdd.getClass());
                    success[0] = false;
                }
                synchronized (ctx) {
                    ctx.put(awComponent.getClass(), awComponent);
                }
            } else {
                LOG.warn("null component received, ignoring ...");
            }
        });
        return success[0];
    }

    public static Collection<? extends AWComponent> getComponents() {
        return ctx.values();
    }

    @SuppressWarnings("unchecked")
    public static <T extends AWComponent> List<T> getComponents(Class<T> type) {
        Collection<? extends AWComponent> components = getComponents();
        List<AWComponent> returnList = new ArrayList<>();

        for (AWComponent component : components) {
            if (type.isInstance(component))
                returnList.add(component);
        }

        return (List<T>) returnList;
    }

    /**
     * Empty the context, useful for testing, destructive if called during
     * the operation of a service!
     */
    public static void clear() {
        ctx.clear();
    }

}
