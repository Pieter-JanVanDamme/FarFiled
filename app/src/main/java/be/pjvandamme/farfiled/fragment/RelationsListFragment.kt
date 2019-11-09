package be.pjvandamme.farfiled.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import be.pjvandamme.farfiled.persistence.FarFiledDatabase
import be.pjvandamme.farfiled.databinding.FragmentRelationsListBinding
import be.pjvandamme.farfiled.adapter.RelationsListAdapter
import be.pjvandamme.farfiled.adapter.RelationsListListener
import be.pjvandamme.farfiled.persistence.repository.RelationRepository
import be.pjvandamme.farfiled.ui.RelationsListViewModelFactory
import be.pjvandamme.farfiled.ui.RelationsListViewModel
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import android.widget.NumberPicker
import be.pjvandamme.farfiled.R
import be.pjvandamme.farfiled.util.MAX_FACES_GENERATED
import be.pjvandamme.farfiled.util.MIN_FACES_GENERATED

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

        binding.createRelationFloatingActionButton.setOnClickListener{
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

        binding.generateRandomRelationsButton.setOnClickListener{
            // TODO: test generate Random Relations Button & numberpicker
            // on 06.11.'19 the API service was down
            Timber.i("Generate Random Relations clicked.")

            var facesSize = relationsListViewModel.faces.value?.size ?: 0
            Timber.i("Faces Size: $facesSize")

            if(facesSize>0) {


                val builder = AlertDialog.Builder(this.context!!)
                builder.setTitle(R.string.addRandomRelationsTitle)

                val numberPicker = NumberPicker(activity)
                numberPicker.maxValue =
                    minOf(MAX_FACES_GENERATED, facesSize)
                numberPicker.minValue = MIN_FACES_GENERATED

                builder.setView(numberPicker)

                builder.setPositiveButton(R.string.addRandomRelationsOK)
                { _, _ -> relationsListViewModel.addFacesToRelationsList(numberPicker.value) }
                builder.setNegativeButton(R.string.addRandomRelationsCancel)
                { dialog, _ -> dialog.cancel() }

                builder.show()
            }
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

        relationsListViewModel.faces.observe(viewLifecycleOwner, Observer{
            if(it.isNullOrEmpty())
                binding.generateRandomRelationsButton.visibility = View.GONE
            else
                binding.generateRandomRelationsButton.visibility = View.VISIBLE
        })

//        relationsListViewModel.showGenerateRelationsListButton.observe(viewLifecycleOwner, Observer{
//            show ->
//            if(show){
//                binding.generateRandomRelationsButton.visibility = View.VISIBLE
//            }
//            else
//                binding.generateRandomRelationsButton.visibility = View.GONE
//        })

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
//                relationsListViewModel.hideGenerateRandomRelationsButton()
                it.let {
                    adapter.submitList(it)
                }
            }
        })

//        relationsListViewModel.faces.observe(viewLifecycleOwner, Observer{
//            if(/*relationsListViewModel.relations.value.isNullOrEmpty()
//                    &&*/ !relationsListViewModel.faces.value.isNullOrEmpty())
//                relationsListViewModel.showGenerateRandomRelationsButton()
//
//            it.forEach{
//                Timber.i("Observed face: $it")
//            }
//        })

        binding.setLifecycleOwner(this)

        return binding.root
    }

}
