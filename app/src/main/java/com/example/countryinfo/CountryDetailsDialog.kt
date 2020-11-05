package com.example.countryinfo

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.util.*

class CountryDetailsDialog : DialogFragment() {

    companion object {
        private const val ARG_TITLE = "argTitle"
        private const val ARG_MESSAGE = "argCountry"

        fun newInstance(title: DetailsStates, messages: ArrayList<String>) =
            CountryDetailsDialog().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_TITLE, title)
                    putStringArrayList(ARG_MESSAGE, messages)
                }
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val state = arguments?.get(ARG_TITLE) as DetailsStates
        val messages = arguments?.get(ARG_MESSAGE) as ArrayList<String>
        var message = ""
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(state.title)
        for (i in messages.indices) {
            message += messages[i] + "\n"
        }
        builder.setMessage(message)
        builder.setPositiveButton("Ok", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dismiss()
            }
        })
        return builder.create()
    }
}