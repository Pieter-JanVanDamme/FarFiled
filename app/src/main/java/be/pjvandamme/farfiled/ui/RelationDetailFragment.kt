package be.pjvandamme.farfiled.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import be.pjvandamme.farfiled.R
import be.pjvandamme.farfiled.databinding.FragmentCreateEditRelationBinding
import be.pjvandamme.farfiled.databinding.FragmentRelationDetailBinding

/**
 * A simple [Fragment] subclass.
 */
class RelationDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentRelationDetailBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_relation_detail,
            container,
            false
        )
        return binding.root

        // TODO: ADD AN IMPLICIT INTENT
        // TODO: ADD SAFE ARGS TO PASS RELATION OBJECT TO CREATEEDITRELATIONFRAGMENT
    }


}
