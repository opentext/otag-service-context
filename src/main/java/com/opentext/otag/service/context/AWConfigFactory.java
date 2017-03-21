/**
 * Copyright © 2016 Open Text.  All Rights Reserved.
 */
package com.opentext.otag.service.context;

/**
 * AppWorks configuration loader factory. Implementations should be able to
 * construct instance of {@link AWConfig}.
 *
 * @author Rhys Evans rhyse@opentext.com
 * @version 16.2
 *
 * @see AWConfig
 */
public interface AWConfigFactory {

    /**
     * Load a new configuration instance re-reading the deployment data from disk.
     *
     * @return config instance
     */
    AWConfig getConfig();

    /**
     * The default factory simply returns a real {@link AWConfig} instance that will
     * inspect the physical file system for configuration information.
     *
     * @return configuration instance factory
     */
    static AWConfigFactory defaultFactory() {
        return AWConfig::new;
    }

}
