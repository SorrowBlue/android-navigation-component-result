package com.sorrowblue.androidx.navigation.result

import kotlin.reflect.KClass
import kotlin.reflect.KType

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class NavigationResult(val key: String, val type: KClass<*>)

