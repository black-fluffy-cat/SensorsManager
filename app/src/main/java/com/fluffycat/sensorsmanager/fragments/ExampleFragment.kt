package com.fluffycat.sensorsmanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fluffycat.sensorsmanager.databinding.ExampleFragmentBinding

class ExampleFragment(private val textToWrite: String) : Fragment() {

    private var binding: ExampleFragmentBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ExampleFragmentBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.exampleFragmentText?.text = textToWrite
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}
