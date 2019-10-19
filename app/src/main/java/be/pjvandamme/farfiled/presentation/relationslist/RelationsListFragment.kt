package be.pjvandamme.farfiled.presentation.relationslist


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import be.pjvandamme.farfiled.R
import be.pjvandamme.farfiled.database.FarFiledDatabase
import be.pjvandamme.farfiled.databinding.FragmentRelationsListBinding
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class RelationsListFragment : Fragment() {

    // TODO: RECYCLERVIEW
    // TODO: CLICKABLE VIEWS in RECYCLERVIEW
    // TODO: FILTER FOR RECYCLERVIEW
    // check https://stackoverflow.com/questions/30398247/how-to-filter-a-recyclerview-with-a-searchview
    // TODO: (if possible) GROUPS FOR RELATIONS

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
                .navigate(R.id.action_relationsListFragment_to_relationDetailFragment)
        }

        val application = requireNotNull(this.activity).application

        val dataSource = FarFiledDatabase.getInstance(application).relationDao

        val viewModelFactory = RelationsListViewModelFactory(dataSource, application)

        val relationsListViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(RelationsListViewModel::class.java)

        binding.relationsListViewModel = relationsListViewModel

        val adapter = RelationsListAdapter(RelationsListListener { relationId ->
            Timber.i("clicked " + relationId.toString())
        })

        binding.relationList.adapter = adapter

        relationsListViewModel.relations.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })

        binding.setLifecycleOwner(this)

        return binding.root

        // TODO: ADD SAFE ARGS TO PASS RELATION OBJECT TO CREATEEDITRELATIONFRAGMENT
    }


}
