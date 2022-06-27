package com.example.myshoppal.ui.fragments

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myshoppal.R
import com.example.myshoppal.databinding.FragmentDashboardBinding
import com.example.myshoppal.models.User
import com.example.myshoppal.ui.activities.DashboardActivity
import com.example.myshoppal.ui.activities.SettingsActivity
import com.example.myshoppal.ui.activities.UserProfileActivity
import com.example.myshoppal.utils.Constants
import com.google.android.material.internal.ContextUtils

class DashboardFragment : Fragment() {

    //private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       // dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard



        textView.text = "This is dashboard Fragment"

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when(id) {

            R.id.action_settings -> {

                startActivity(Intent(activity, SettingsActivity::class.java))

                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }
}