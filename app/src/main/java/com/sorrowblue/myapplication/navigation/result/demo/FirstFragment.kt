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
        view.findViewById<TextView>(R.id.textview_first).text =
            getString(R.string.hello_first_fragment, inte.toString())
        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            val action =
                FirstFragmentDirections.secondFragment(inte + 1 )
            findNavController().navigate(action)
        }
    }

    val inte get() =  kotlin.runCatching { args.inte }.getOrElse { 0 }
}

abstract class AppFragment(id: Int) : Fragment(id)