package com.launcher.application.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.launcher.application.R

class HomescreenFragment : Fragment(),
  View.OnClickListener {
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_homescreen, container, false)
  }

  override fun onClick(v: View) {}
}