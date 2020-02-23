package com.sorrowblue.android.navigation.result.annotations

import kotlin.reflect.KClass
import kotlin.reflect.KType

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class NavigationResult(val key: String, val type: KClass<*>)

