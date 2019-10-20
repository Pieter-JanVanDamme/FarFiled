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
import android.text.Editable
import android.text.TextWatcher
import android.widget.LinearLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_relation_detail.*


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

        val application = requireNotNull(this.activity).application

        val arguments = RelationDetailFragmentArgs.fromBundle(arguments!!)

        val relationDataSource = FarFiledDatabase.getInstance(application).relationDao

        val relationLifeAreaDataSource = FarFiledDatabase.getInstance(application).relationLifeAreaDao

        // TODO: pass the relationId OR null, depending on what we get in the Bundle
        val viewModelFactory =
            RelationDetailViewModelFactory(
                arguments.relationId,
                relationDataSource,
                relationLifeAreaDataSource,
                application
            )

        val relationDetailViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(RelationDetailViewModel::class.java)

        binding.relationDetailViewModel = relationDetailViewModel

        binding.setLifecycleOwner(this)

        val lifeAreaLayouts = mutableListOf(
            binding.synopsisLayout,
            binding.nowLayout,
            binding.selfLayout,
            binding.workLayout,
            binding.homeLayout,
            binding.circleLayout,
            binding.funLayout
        )

        binding.editTextChips.setOnCheckedChangeListener { chipGroup, i ->
            when(i){
                synopsisChip.id -> hideLifeAreasLayoutsExcept(binding.synopsisLayout, lifeAreaLayouts)
                nowChip.id -> hideLifeAreasLayoutsExcept(binding.nowLayout, lifeAreaLayouts)
                selfChip.id -> hideLifeAreasLayoutsExcept(binding.selfLayout, lifeAreaLayouts)
                workChip.id -> hideLifeAreasLayoutsExcept(binding.workLayout, lifeAreaLayouts)
                homeChip.id -> hideLifeAreasLayoutsExcept(binding.homeLayout, lifeAreaLayouts)
                circleChip.id -> hideLifeAreasLayoutsExcept(binding.circleLayout, lifeAreaLayouts)
                funChip.id -> hideLifeAreasLayoutsExcept(binding.funLayout, lifeAreaLayouts)
                else -> hideLifeAreasLayoutsExcept(null, lifeAreaLayouts)
            }
        }

        val textWatcher = object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            override fun afterTextChanged(s: Editable?) {
                relationDetailViewModel.onEditRelation(
                    relationNameEditText.text.toString(),
                    relationSynopsisEditText.text.toString(),
                    lifeAreaNowEditText.text.toString(),
                    lifeAreaSelfEditText.text.toString(),
                    lifeAreaWorkEditText.text.toString(),
                    lifeAreaHomeEditText.text.toString(),
                    lifeAreaCircleEditText.text.toString(),
                    lifeAreaFunEditText.text.toString()
                )
            }
        }

        binding.saveButton.isEnabled = false

        binding.relationNameEditText.addTextChangedListener(textWatcher)
        binding.relationSynopsisEditText.addTextChangedListener(textWatcher)
        binding.lifeAreaNowEditText.addTextChangedListener(textWatcher)
        binding.lifeAreaSelfEditText.addTextChangedListener(textWatcher)
        binding.lifeAreaWorkEditText.addTextChangedListener(textWatcher)
        binding.lifeAreaHomeEditText.addTextChangedListener(textWatcher)
        binding.lifeAreaCircleEditText.addTextChangedListener(textWatcher)
        binding.lifeAreaFunEditText.addTextChangedListener(textWatcher)

        relationDetailViewModel.enableSaveButton.observe(this, Observer{enable ->
            enable?.let{
                binding.saveButton.isEnabled = enable
            }
        })

        relationDetailViewModel.showNameEmptySnackbar.observe(this, Observer{show ->
            if(show){
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    getString(R.string.nameEmptyMessage),
                    Snackbar.LENGTH_LONG
                ).show()
                relationDetailViewModel.doneShowingNameEmptySnackbar()
            }

        })

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

    private fun hideLifeAreasLayoutsExcept(visibleLayout: LinearLayout?,
                                           allLayouts: List<LinearLayout>
    ){
        allLayouts.forEach{it ->
            if(it != visibleLayout)
                it.visibility = View.GONE
            else
                it.visibility = View.VISIBLE
        }
    }
}