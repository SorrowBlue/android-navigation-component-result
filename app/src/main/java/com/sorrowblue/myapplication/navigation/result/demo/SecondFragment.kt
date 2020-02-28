package com.sorrowblue.myapplication.navigation.result.demo

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sorrowblue.androidx.navigation.result.NavigationResult
import com.sorrowblue.myapplication.R
import com.sorrowblue.androidx.navigation.result.setNavigationResult as setNavigationResultLib

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@NavigationResult("SecondFragmentresult", String::class)
class SecondFragment : Fragment(R.layout.fragment_second) {

	private val args: SecondFragmentArgs by navArgs()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		view.findViewById<TextView>(R.id.textview_second).text =
			getString(R.string.hello_second_fragment, args.myArg.toString())
		view.findViewById<Button>(R.id.button_second).setOnClickListener {
			findNavController().navigate(SecondFragmentDirections.firstFragment(kotlin.runCatching { args.myArg + 1 }.getOrElse { 0 }))
		}
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		requireActivity().onBackPressedDispatcher.addCallback(this) {
			setNavigationResultLib("result", args.myArg.toString())
			findNavController().navigateUp()
		}
	}
}
