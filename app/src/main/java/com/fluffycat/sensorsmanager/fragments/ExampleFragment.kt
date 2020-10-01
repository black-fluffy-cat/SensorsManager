package com.fluffycat.sensorsmanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fluffycat.sensorsmanager.R
import kotlinx.android.synthetic.main.example_fragment.*

class ExampleFragment(private val textToWrite: String) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.example_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exampleFragmentText.text = textToWrite
    }
}
