/**
 * Copyright [2018] [Ole Lensmar]
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package io.nanoservices.serverless.plugins.maven;

import org.apache.maven.project.MavenProject;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Definition of provider-specific behaviour during serverless.yml generation
 */

public interface ProviderHandler {

    /**
     * Return a list of Methods from the specified class that will be configured as function handlers in serverless.yml
     *
     * @param aClass the class possibly containing function handlers
     * @return a list of found handlers, an empty list must be returned if no handlers are found
     */

    List<Method> findFunctions(Class aClass);

    /**
     * Create the corresponding YAML handler entry in serverless.yml for the specified method that was previously returned
     * by findFunctions
     *
     * @param method   a method previously found by the findFunctions method
     * @param handlers a YAML object map that will be put under the functions entry in serverless.yml
     */

    void createHandlerConfig(Method method, Map<String, Object> handlers, MavenProject project);

    /**
     * Allow post-processing of the generated serverless.yml configuration before it gets written to file
     *
     * @param config the serverless.yml YAML configuration
     */

    void enhanceConfig(Map<String, Object> config, MavenProject project);

    /**
     * Callled before the serverless framework is invoked to allow the provider to modify the generated process
     *
     * @param builder the ProcessBuilder used to start the serverless command
     */

    void beforeServerlessCli(ProcessBuilder builder);
}
