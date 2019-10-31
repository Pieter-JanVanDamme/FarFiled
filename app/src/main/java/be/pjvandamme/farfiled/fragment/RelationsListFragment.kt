package be.pjvandamme.farfiled.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import be.pjvandamme.farfiled.R
import be.pjvandamme.farfiled.persistence.FarFiledDatabase
import be.pjvandamme.farfiled.databinding.FragmentRelationsListBinding
import be.pjvandamme.farfiled.adapter.RelationsListAdapter
import be.pjvandamme.farfiled.adapter.RelationsListListener
import be.pjvandamme.farfiled.persistence.repository.RelationRepository
import be.pjvandamme.farfiled.ui.RelationsListViewModelFactory
import be.pjvandamme.farfiled.ui.RelationsListViewModel
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class RelationsListFragment : Fragment() {

    // TODO: FILTER FOR RECYCLERVIEW
    // check https://stackoverflow.com/questions/30398247/how-to-filter-a-recyclerview-with-a-searchview

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
            Timber.i("Create Relation action button clicked!")
            this.findNavController().navigate(
                RelationsListFragmentDirections.actionRelationsListFragmentToRelationDetailFragment(
                    -1L
                )
            )
        }

        val application = requireNotNull(this.activity).application

        val dataSource = RelationRepository(FarFiledDatabase.getInstance(application))

        val viewModelFactory =
            RelationsListViewModelFactory(
                dataSource,
                application
            )

        val relationsListViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(RelationsListViewModel::class.java)

        binding.relationsListViewModel = relationsListViewModel

        val adapter =
            RelationsListAdapter(RelationsListListener { relationId ->
                relationsListViewModel.onRelationClicked(relationId)
            })

        binding.relationList.adapter = adapter

        binding.generateRandomRelationsButton.setOnClickListener{view: View ->
            Timber.i("Generate Random Relations clicked.")

            relationsListViewModel.fillRelationList()
        }

        relationsListViewModel.navigateToRelationDetail.observe(viewLifecycleOwner, Observer{
            relation ->
                relation?.let{
                    this.findNavController().navigate(
                        RelationsListFragmentDirections.actionRelationsListFragmentToRelationDetailFragment(
                            relation
                        )
                    )
                    relationsListViewModel.onRelationDetailNavigated()
                }
        })

        relationsListViewModel.showGenerateRelationsListButton.observe(viewLifecycleOwner, Observer{
            show ->
            if(show){
                binding.generateRandomRelationsButton.visibility = View.VISIBLE
            }
            else
                binding.generateRandomRelationsButton.visibility = View.GONE
        })

        relationsListViewModel.showCouldNotPopulateRelationsListSnackbar.observe(viewLifecycleOwner, Observer{
            if(it){
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    getString(R.string.couldNotGenerateMessage),
                    Snackbar.LENGTH_LONG
                ).show()
                relationsListViewModel.doneShowingCouldNotPopulateSnackbar()
            }
        })

        // Todo: BROKEN IMAGE when avatar not available etc.

        relationsListViewModel.relations.observe(viewLifecycleOwner, Observer {
            if(!it.isNullOrEmpty()){
                relationsListViewModel.hideGenerateRandomRelationsButton()
                it?.let {
                    adapter.submitList(it)
                }
            }
        })

        relationsListViewModel.faces.observe(viewLifecycleOwner, Observer{
            if(relationsListViewModel.relations.value.isNullOrEmpty()
                    && !relationsListViewModel.faces.value.isNullOrEmpty())
                relationsListViewModel.showGenerateRandomRelationsButton()

            it.forEach{
                Timber.i("Observed face: $it")
            }
        })

        binding.setLifecycleOwner(this)

        return binding.root
    }


}
