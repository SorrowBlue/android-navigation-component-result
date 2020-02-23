package com.sorrowblue.myapplication.navigation.result.demo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sorrowblue.android.navigation.result.annotations.NavigationResult
import com.sorrowblue.myapplication.R

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
			setNavigationResult(args.myArg.toString() as java.lang.String)
            findNavController().navigate(SecondFragmentDirections.firstFragment(kotlin.runCatching { args.myArg + 1 }.getOrElse { 0 }))
		}
	}
}
