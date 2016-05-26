/**
 * Copyright © 2016 Open Text.  All Rights Reserved.
 */
package com.opentext.otag.service.context.components;

/**
 * AppWorks Service Component base type. Access to the support functionality provided for
 * AppWorks Services by the AppWorks Gateway is facilitated by the implementation of
 * standard types. <strong>The class must have a default no-args constructor</strong>
 * to be instantiated in this way.
 * <p>
 * Types auto-instantiated by the platform currently include;
 * - com.opentext.otag.sdk.handlers.AWServiceContextHandler (starup/shutdown)
 * - com.opentext.otag.sdk.handlers.OtagMessageHandler (config settings and lifecycle changes)
 * - com.opentext.otag.sdk.handlers.AuthRequestHandler
 * - com.opentext.otag.sdk.connector.EIMConnectorService
 * <p>
 * Note: Any sub-type of {@code AWComponent} can be added to the {@link AWComponentContext} by a
 * service at runtime.
 *
 * @author Rhys Evans rhyse@opentext.com
 * @version 16.0.1
 */
public interface AWComponent {

    /**
     * The management agent will create an instance of your component for you, unless
     * you tell it explicitly not to. If this method returns false you must register
     * the component yourself in the {@code AWComponentContext}.
     *
     * @return true if this class should be instantiated by the framework
     * @see AWComponentContext#add(AWComponent...)
     */
    default boolean autoDeploy() {
        return true;
    }

}
