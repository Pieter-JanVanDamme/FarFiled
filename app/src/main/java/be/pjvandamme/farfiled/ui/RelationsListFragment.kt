package be.pjvandamme.farfiled.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import be.pjvandamme.farfiled.R
import be.pjvandamme.farfiled.databinding.FragmentRelationsListBinding

/**
 * A simple [Fragment] subclass.
 */
class RelationsListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_relations_list, container, false)
        val binding = DataBindingUtil.inflate<FragmentRelationsListBinding>(
            inflater,
            R.layout.fragment_relations_list,
            container,
            false
        )
        return binding.root
    }


}
