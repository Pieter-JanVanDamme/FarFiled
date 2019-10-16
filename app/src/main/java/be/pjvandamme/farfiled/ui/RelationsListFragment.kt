package be.pjvandamme.farfiled.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController

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
        val binding = DataBindingUtil.inflate<FragmentRelationsListBinding>(
            inflater,
            R.layout.fragment_relations_list,
            container,
            false
        )
        binding.createRelationFloatingActionButton.setOnClickListener{ view: View ->
            view.findNavController()
                .navigate(R.id.action_relationsListFragment_to_createEditRelationFragment)
        }
        return binding.root

        // TODO: ADD SAFE ARGS TO PASS RELATION OBJECT TO CREATEEDITRELATIONFRAGMENT
    }


}
