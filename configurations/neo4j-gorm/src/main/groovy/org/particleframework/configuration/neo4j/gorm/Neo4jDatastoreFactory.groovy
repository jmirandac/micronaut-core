/*
 * Copyright 2018 original authors
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
package org.particleframework.configuration.neo4j.gorm

import grails.gorm.annotation.Entity
import grails.neo4j.Neo4jEntity
import groovy.transform.CompileStatic
import org.grails.datastore.gorm.neo4j.Neo4jDatastore
import org.grails.datastore.gorm.neo4j.Neo4jDatastoreTransactionManager
import org.neo4j.driver.v1.Driver
import org.particleframework.configuration.neo4j.gorm.event.ConfigurableEventPublisherAdapter
import org.particleframework.context.ApplicationContext
import org.particleframework.context.annotation.Bean
import org.particleframework.context.annotation.Context
import org.particleframework.context.annotation.Factory
import org.particleframework.context.annotation.Requires
import org.particleframework.context.env.Environment
import org.particleframework.spring.core.env.PropertyResolverAdapter

import javax.inject.Singleton
import java.util.stream.Stream

/**
 * A factory for configuring GORM for Neo4j
 *
 * @author graemerocher
 * @since 1.0
 */
@CompileStatic
@Factory
@Requires(beans = Driver)
class Neo4jDatastoreFactory {

    @Context
    @Bean(preDestroy = "close")
    Neo4jDatastore neo4jDatastore(Driver driver, ApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment()
        Stream<Class> entities = environment.scan(Entity)
                                            .filter({ Class  c -> Neo4jEntity.isAssignableFrom(c) })
        Class[] classes = entities.toArray() as Class[]
        Neo4jDatastore datastore = new Neo4jDatastore(
                driver,
                new PropertyResolverAdapter(applicationContext),
                new ConfigurableEventPublisherAdapter(applicationContext),
                classes
        )
        for(o in datastore.getServices()) {
            applicationContext.inject(o)
            applicationContext.registerSingleton(o)
        }
        return datastore
    }

    @Singleton
    @Bean
    Neo4jDatastoreTransactionManager neo4jDatastoreTransactionManager(Neo4jDatastore datastore) {
        return datastore.getTransactionManager()
    }
}