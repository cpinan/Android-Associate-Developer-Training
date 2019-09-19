/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.google.android.material.snackbar.Snackbar

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class SleepTrackerFragment : Fragment() {

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_tracker, container, false)

        val application = requireNotNull(activity).application
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao

        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(SleepTrackerViewModel::class.java)

        val sleepNightAdapter = SleepNightAdapter(
                SleepNightListener { nightId ->
                    // Toast.makeText(context, "$nightId", Toast.LENGTH_LONG).show()
                    viewModel.onSleepNightClicked(nightId)
                }
        )
        val gridLayoutManager = GridLayoutManager(activity, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 3
                else -> 1
            }
        }
        binding.sleepTrackerList.layoutManager = gridLayoutManager
        binding.sleepTrackerList.adapter = sleepNightAdapter

        binding.sleepTrackerViewModel = viewModel
        binding.setLifecycleOwner(this)

        viewModel.navigateToSleepQuality.observe(
                this,
                Observer { night ->
                    night?.let {
                        this.findNavController().navigate(
                                SleepTrackerFragmentDirections
                                        .actionSleepTrackerFragmentToSleepQualityFragment(night.nightId)
                        )
                        viewModel.doneNavigatingFunction()
                    }
                }
        )

        viewModel.showSnackbarEvent.observe(
                this,
                Observer {
                    if (it == true) {
                        Snackbar.make(
                                activity!!.findViewById(android.R.id.content),
                                getString(R.string.cleared_message),
                                Snackbar.LENGTH_LONG
                        ).show()
                        viewModel.doneNavigatingFunction()
                    }
                }
        )

        viewModel.nights.observe(
                viewLifecycleOwner,
                Observer {
                    sleepNightAdapter.addHeaderAndSubmitList(it)
                }
        )

        viewModel.navigateToSleepDataQuality.observe(this, Observer { night ->
            night?.let {

                viewModel.onSleepDataQualityNavigated()
            }
        })

        return binding.root
    }
}
