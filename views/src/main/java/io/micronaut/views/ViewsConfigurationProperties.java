/*
 * Copyright 2017-2018 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.micronaut.views;

import io.micronaut.context.annotation.ConfigurationProperties;

/**
 * Implementation of {@link ViewsConfiguration}. Views configuration properties.
 * @author Sergio del Amo
 * @since 1.0
 */
@ConfigurationProperties(ViewsConfigurationProperties.PREFIX)
public class ViewsConfigurationProperties implements ViewsConfiguration {
    public static final String PREFIX = "micronaut.views";

    public static final String DEFAULT_FOLDER = "views";

    protected boolean enabled = true;

    protected String folder = DEFAULT_FOLDER;

    /**
     * enabled getter.
     * @return boolean flag indicating whether the security features are enabled.
     */
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     *
     * @return The resources folder where views should be searched for. By default {@value #DEFAULT_FOLDER}
     */
    @Override
    public String getFolder() {
        return this.folder;
    }
}