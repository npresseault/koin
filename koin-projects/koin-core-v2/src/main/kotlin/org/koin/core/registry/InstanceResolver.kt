package org.koin.core.registry

import org.koin.core.bean.BeanDefinition
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.scope.Scope
import java.util.*

class InstanceResolver {

    private val callStack = Stack<BeanDefinition<*>>()

    inline fun <reified T> resolveInstance(
        definition: BeanDefinition<*>,
        scope: Scope?,
        noinline parameters: ParametersDefinition?
    ): T {
        checkForCycle(definition)

        prepareCallStack(definition)

        val instance: T = getInstance(definition, scope, parameters)

        cleanCallStack(definition)
        return instance
    }

    fun prepareCallStack(definition: BeanDefinition<*>?) {
        callStack.add(definition)
    }

    inline fun <reified T> getInstance(
        definition: BeanDefinition<*>,
        scope: Scope?,
        noinline parameters: ParametersDefinition?
    ): T {
        return definition.instance.get(scope, parameters)
    }

    fun cleanCallStack(definition: BeanDefinition<*>?) {
        val pop = callStack.pop()
        if (pop != definition) {
            error("CallStack integrity error while resolving $definition")
        }
    }

    fun checkForCycle(definition: BeanDefinition<*>?) {
        if (callStack.any { it == definition }) {
            error("CallStack cycle detected for $definition")
        }
    }

    fun close() {
        callStack.clear()
    }
}