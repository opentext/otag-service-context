/**
 * Copyright © 2016 Open Text.  All Rights Reserved.
 */
package com.opentext.otag.service.context.error;

import com.opentext.otag.service.context.components.AWComponentContext;

import java.util.NoSuchElementException;

/**
 * Thrown when we detect that the {@link AWComponentContext} does not contain a component
 * that an AppWorks service has asked for. It is expected that AppWorks services are aware
 * of what they have injected into the context, and that the absence of such a component
 * is unexpected.
 *
 * @author Rhys Evans rhyse@opentext.com
 * @version 16.2
 *
 * @see AWComponentContext#getComponent(Class)
 */
public class AWComponentNotFoundException extends NoSuchElementException {

    public AWComponentNotFoundException(String componentClassName) {
        super(componentClassName + " was not found in the AWComponentContext");
    }

}
