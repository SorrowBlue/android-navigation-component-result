package com.sorrowblue.myapplication.navigation.result.demo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sorrowblue.android.navigation.result.annotations.NavigationResult
import com.sorrowblue.myapplication.R

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@NavigationResult("test", String::class)
class FirstFragment : AppFragment(R.layout.fragment_first) {

	private val args: FirstFragmentArgs by navArgs()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		SecondFragmentNavigationResult.observe(this) {
			Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
		}
		kotlin.runCatching {
			Toast.makeText(requireContext(), args.inte.toString(), Toast.LENGTH_SHORT).show()
		}
		view.findViewById<Button>(R.id.button_first).setOnClickListener {
			val action =
				FirstFragmentDirections.secondFragment(kotlin.runCatching { args.inte  + 1}.getOrElse { 0 })
			findNavController().navigate(action)
		}
	}
}

abstract class AppFragment(id: Int) : Fragment(id)