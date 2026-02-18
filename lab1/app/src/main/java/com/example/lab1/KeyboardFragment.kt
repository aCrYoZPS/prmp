package com.example.lab1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment

class KeyboardFragment : Fragment() {
    interface KeyboardListener {
        fun onInsert(key: String)
        fun onDelete()
    }

    private var listener: KeyboardListener? = null

    fun setListener(listener: KeyboardListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_keyboard, container, false)
        val gridLayout =
            view.findViewById<GridLayout>(R.id.keyboard_grid)

        for (child in gridLayout.children) {
            if (child is Button) {
                child.setOnClickListener { handleButtonClick(child) }
            }
        }

        return view
    }

    private fun handleButtonClick(button: Button) {
        val tag = button.tag.toString()

        if (tag == "backspace") {
            listener?.onDelete()
        } else {
            listener?.onInsert(tag)
        }
    }
}