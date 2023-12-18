package com.birdushenin.newssphere.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.birdushenin.newssphere.R
import com.birdushenin.newssphere.data.FilterState
import com.birdushenin.newssphere.databinding.FragmentFiltersBinding
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FiltersFragment : Fragment(), FragmentScreen {

    private val filterViewModel: FilterViewModel by activityViewModels()
    private val calendarViewModel: CalendarViewModel by activityViewModels()
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioGroupLang: RadioGroup
    private var tempSelectedFilter: String? = null
    private var tempSelectedFilterLang: String? = null
    private var tempStartDate: String? = null
    private var tempEndDate: String? = null

    override fun createFragment(factory: FragmentFactory): Fragment {
        return FiltersFragment()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFiltersBinding.inflate(layoutInflater)
        val imageCalendar = binding.imageCalendar
        val applyButton = binding.btnCheck
        val btnBack = binding.btnBack
        val chooseDate = binding.chooseDate

        calendarViewModel.filterState.observe(viewLifecycleOwner, Observer { filterState ->
            chooseDate.text = "${filterState.startDate} - ${filterState.endDate}"
            chooseDate.setTextColor(filterState.textColor)
            imageCalendar.setImageResource(filterState.imageResource)

            val layoutParams = imageCalendar.layoutParams
            layoutParams.width = filterState.imageWidth
            layoutParams.height = filterState.imageHeight
            imageCalendar.layoutParams = layoutParams
        })

        imageCalendar.setOnClickListener {
            showDatePicker()
        }

        filterViewModel.selectedFilterPosition.observe(
            viewLifecycleOwner,
            Observer { selectedFilterPosition ->
                restoreSelectedFilter(selectedFilterPosition)
            })

        filterViewModel.selectedFilterPositionLang.observe(
            viewLifecycleOwner,
            Observer { selectedFilterPositionLang ->
                restoreSelectedFilterLang(selectedFilterPositionLang)
            })


        radioGroup = binding.radioButton
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            tempSelectedFilter = when (checkedId) {
                R.id.rbLeft -> "popular"
                R.id.rbCenter -> "publishedAt"
                R.id.rbRight -> "relevancy"
                else -> null
            }
        }

        radioGroupLang = binding.buttonLang
        radioGroupLang.setOnCheckedChangeListener { _, checkedId ->
            tempSelectedFilterLang = when (checkedId) {
                R.id.buttonRussian -> "ru"
                R.id.buttonEnglish -> "en"
                R.id.buttonDeutsch -> "de"
                else -> null
            }
        }

        applyButton.setOnClickListener {

            tempSelectedFilter?.let {
//                selectedFilter = it
                filterViewModel.selectFilterPosition(it)
            }

            tempSelectedFilterLang?.let { catch ->
//                selectedFilter2 = catch
                filterViewModel.selectFilterPositionLang(catch)
            }

            tempStartDate?.let { startDate ->
                tempEndDate?.let { endDate ->
                    chooseDate.text = "$startDate - $endDate"
                    val textColor = ContextCompat.getColor(requireContext(), R.color.blue)
                    val imageResource = R.drawable.calendar_clicked
                    val newWidthInDp = 32
                    val newHeightInDp = 40
                    val density = resources.displayMetrics.density
                    val newWidth = (newWidthInDp * density).toInt()
                    val newHeight = (newHeightInDp * density).toInt()

                    val filterStates = FilterState(
                        startDate,
                        endDate,
                        chooseDate,
                        textColor,
                        imageResource,
                        newWidth,
                        newHeight,
                        density,
                        newWidthInDp,
                        newHeightInDp
                    )
                    calendarViewModel.setFilterState(filterStates)
                }
            }

            filterViewModel.setFilter(
                tempSelectedFilter,
                tempStartDate,
                tempEndDate,
                tempSelectedFilterLang
            )

            activity?.onBackPressed()
        }

        btnBack.setOnClickListener {
            activity?.onBackPressed()
        }

        return binding.root
    }

    private fun showDatePicker() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener { selection ->
            val startDateMillis = selection.first
            val endDateMillis = selection.second
            val startDate = Date(startDateMillis)
            val endDate = Date(endDateMillis)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val chooseDate: TextView = requireView().findViewById(R.id.chooseDate)
            val calendar: ImageView = requireView().findViewById(R.id.imageCalendar)

            tempStartDate = dateFormat.format(startDate)
            tempEndDate = dateFormat.format(endDate)
            chooseDate.text = "$tempStartDate - $tempEndDate"
            chooseDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
            calendar.setImageResource(R.drawable.calendar_clicked)
            val newWidthInDp = 32
            val newHeightInDp = 40
            val density = resources.displayMetrics.density
            val newWidth = (newWidthInDp * density).toInt()
            val newHeight = (newHeightInDp * density).toInt()
            val layoutParams = calendar.layoutParams
            layoutParams.width = newWidth
            layoutParams.height = newHeight
            calendar.layoutParams = layoutParams
        }

        picker.show(requireActivity().supportFragmentManager, picker.toString())
    }

    private fun restoreSelectedFilter(savedFilter: String?) {
        when (savedFilter) {
            "popular" -> radioGroup.check(R.id.rbLeft)
            "publishedAt" -> radioGroup.check(R.id.rbCenter)
            "relevancy" -> radioGroup.check(R.id.rbRight)
        }
    }

    private fun restoreSelectedFilterLang(savedFilter: String?) {
        when (savedFilter) {
            "ru" -> radioGroupLang.check(R.id.buttonRussian)
            "en" -> radioGroupLang.check(R.id.buttonEnglish)
            "de" -> radioGroupLang.check(R.id.buttonDeutsch)
        }
    }
}