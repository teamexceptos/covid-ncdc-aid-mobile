package com.exceptos.covidaid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.highlights_bottomsheet.*

class ModalBottomSheet (ncdc_highlights_value: String): BottomSheetDialogFragment() {

    private lateinit var dismissButton: ImageView
    private var highlights_value = ncdc_highlights_value

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.highlights_bottomsheet, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dismissButton = view.findViewById(R.id.highlight_close)
        dismissButton.setOnClickListener {
            dismiss()
        }

        if(!highlights_value.isNullOrEmpty()) {

            highlight_progress.visibility = View.GONE
            ncdc_highlights.text = highlights_value

        }
    }

}