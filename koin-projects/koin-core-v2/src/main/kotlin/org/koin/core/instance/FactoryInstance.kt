package org.koin.core.instance

import org.koin.core.bean.BeanDefinition
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.scope.Scope

class FactoryInstance<T>(private val beanDefinition: BeanDefinition<T>) :
    Instance<T> {

    var value: T? = null

    override fun release(scope: Scope?) {}

    override fun isCreated(scope: Scope?): Boolean = false

    override fun <T> get(scope : Scope?, parameters: ParametersDefinition?): T {
        return create(beanDefinition, parameters)
    }
}