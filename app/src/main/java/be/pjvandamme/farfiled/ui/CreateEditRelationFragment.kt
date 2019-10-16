package be.pjvandamme.farfiled.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController

import be.pjvandamme.farfiled.R
import be.pjvandamme.farfiled.databinding.FragmentCreateEditRelationBinding
import be.pjvandamme.farfiled.databinding.FragmentRelationsListBinding

/**
 * A simple [Fragment] subclass.
 */
class CreateEditRelationFragment : Fragment() {

    // TODO: DIFFERENTIATE BETWEEN CREATION AND EDITING
    // Probably by holding a reference to the Relation being edited.
    // If there's none: we're creating a new one

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentCreateEditRelationBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_edit_relation,
            container,
            false
        )
        binding.saveButton.setOnClickListener { view: View ->
            // TODO: NAVIGATION ACCORDING TO RELATION REFERENCE
            // IF NULL: CREATE NEW ONE, ADD TO ROOM, NAVIGATE TO RELATIONSLIST
            // ELSE: UPDATE ATTRIBUTES, NAVIGATE TO ITS RELATIONDETAILFRAGMENT
            view.findNavController()
                .navigate(R.id.action_createEditRelationFragment_to_relationsListFragment)
        }
        binding.cancelButton.setOnClickListener { view: View ->
            // TODO: NAVIGATION ACCORDING TO RELATION REFERENCE
            // IF NULL: NAVIGATE TO RELATIONSLIST
            // ELSE: NAVIHATE TO ITS RELATIONSDETAILFRAGMENT
            view.findNavController()
                .navigate(R.id.action_createEditRelationFragment_to_relationsListFragment)
        }
        return binding.root
    }


}
