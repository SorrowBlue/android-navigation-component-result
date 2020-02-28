package com.sorrowblue.androidx.navigation.result

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController

fun <T> Fragment.observeNavigationResult(
	key: String,
	onResult: (value: T?) -> Unit
): Observer<T?>? = findNavController().currentBackStackEntry?.savedStateHandle
	?.getLiveData<T?>(key)?.observe(this.viewLifecycleOwner) { onResult.invoke(it) }
fun <T> Fragment.observeNavigationResult(
	key: String,
	initialValue: T?,
	onResult: (value: T?) -> Unit
): Observer<T?>? = findNavController().currentBackStackEntry?.savedStateHandle
	?.getLiveData(key, initialValue)?.observe(this.viewLifecycleOwner) { onResult.invoke(it) }

fun <T> Fragment.setNavigationResult(key: String, value: T?) {
	findNavController().previousBackStackEntry?.savedStateHandle?.set(key, value)
}
