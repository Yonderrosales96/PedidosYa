package com.example.pedidosya.utils

import androidx.fragment.app.FragmentManager

interface DialogControl {

    fun dismissDialogFragment()
    fun showDialogFragment(fragmentManager: FragmentManager)

}