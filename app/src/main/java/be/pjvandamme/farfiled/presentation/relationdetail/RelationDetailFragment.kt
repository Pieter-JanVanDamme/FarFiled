package be.pjvandamme.farfiled.presentation.relationdetail


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import be.pjvandamme.farfiled.R
import be.pjvandamme.farfiled.database.FarFiledDatabase
import be.pjvandamme.farfiled.databinding.FragmentRelationDetailBinding

/**
 * A simple [Fragment] subclass.
 */
class RelationDetailFragment : Fragment() {

    // TODO: DIFFERENTIATE BETWEEN CREATION AND EDITING
    // Probably by holding a reference to the Relation being edited.
    // If there's none: we're creating a new one

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

        val application = requireNotNull(this.activity).application

        val dataSource = FarFiledDatabase.getInstance(application).relationDao

        // TODO: pass the relationId OR null, depending on what we get in the Bundle
        val viewModelFactory =
            RelationDetailViewModelFactory(
                null,
                dataSource,
                application
            )

        val relationDetailViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(RelationDetailViewModel::class.java)

        binding.relationDetailViewModel = relationDetailViewModel

        binding.setLifecycleOwner(this)

        relationDetailViewModel.navigateToRelationsList.observe(this, Observer{
            // It appears that this method gets called multiple times, possibly due to an issue
            // with the backstack that occurs when using the back button to make the keyboard on
            // an edit text disappear. To avoid this issue, we first check the currentDestination
            // to prevent a crash due to calling this method a second time when already navigated
            // away.
            if (this.findNavController().currentDestination?.id == R.id.relationDetailFragment) {
                this.findNavController().navigate(
                    RelationDetailFragmentDirections.actionRelationDetailFragmentToRelationsListFragment()
                )
                relationDetailViewModel.doneNavigating()
            }
        })

        return binding.root
    }


}
