package be.pjvandamme.farfiled.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController

import be.pjvandamme.farfiled.R
import be.pjvandamme.farfiled.database.FarFiledDatabase
import be.pjvandamme.farfiled.databinding.FragmentCreateEditRelationBinding
import be.pjvandamme.farfiled.databinding.FragmentRelationsListBinding
import be.pjvandamme.farfiled.viewmodels.CreateEditRelationViewModel
import be.pjvandamme.farfiled.viewmodels.CreateEditRelationViewModelFactory

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

        val application = requireNotNull(this.activity).application

        val dataSource = FarFiledDatabase.getInstance(application).relationDao

        // TODO: pass the relationId OR null, depending on what we get in the Bundle
        val viewModelFactory = CreateEditRelationViewModelFactory(
            null,
            dataSource,
            application
        )

        val createEditRelationViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(CreateEditRelationViewModel::class.java)

        binding.createEditRelationViewModel = createEditRelationViewModel

        binding.setLifecycleOwner(this)

        return binding.root
    }


}
