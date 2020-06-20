package com.amg.recipesapp.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.amg.recipesapp.ChatViewModel
import com.amg.recipesapp.R
import com.amg.recipesapp.databinding.*
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val viewModel: ChatViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val safeArgs: ChatFragmentArgs by navArgs()
        val name = safeArgs.name

        binding.answerLabel.setEndIconOnClickListener {

            binding.yesOrNo.yesOrNoContainer.visibility = View.GONE
            binding.cuisines.cuisinesContainer.visibility = View.GONE
            binding.dietPrograms.dietProgramsContainer.visibility = View.GONE
            binding.ingredients.ingredientsContainer.visibility = View.GONE
            binding.answerLabel.isEnabled = false
            binding.answer.isEnabled = false

            val answer = binding.answer.text.toString()

            hideKeyboard(binding.root)

            var chip: Chip
            when (viewModel.questionNumber.value) {
                0 -> {
                    for (index in 0 until binding.yesOrNo.yesOrNo.childCount) {
                        (binding.yesOrNo.yesOrNo.getChildAt(index) as Chip).isChecked = false
                    }
                }
                1 -> {
                    for (index in 0 until binding.ingredients.ingredients.childCount) {
                        chip = (binding.ingredients.ingredients.getChildAt(index) as Chip)
                        if (chip.isChecked) {
                            chip.isChecked = false
                            chip.visibility = View.GONE
                        }
                    }
                }
                2 -> {
                    for (index in 0 until binding.cuisines.cuisines.childCount) {
                        (binding.cuisines.cuisines.getChildAt(index) as Chip).isChecked = false
                    }
                }
                3 -> {
                    for (index in 0 until binding.yesOrNo.yesOrNo.childCount) {
                        (binding.yesOrNo.yesOrNo.getChildAt(index) as Chip).isChecked = false
                    }
                }
                4 -> {
                    for (index in 0 until binding.dietPrograms.dietPrograms.childCount) {
                        (binding.dietPrograms.dietPrograms.getChildAt(index) as Chip).isChecked =
                            false
                    }
                }
                5 -> {
                    for (index in 0 until binding.ingredients.ingredients.childCount) {
                        chip = (binding.ingredients.ingredients.getChildAt(index) as Chip)
                        if (chip.isChecked) {
                            chip.isChecked = false
                            chip.visibility = View.GONE
                        }
                    }
                }
            }

            binding.answer.setText("")

            viewModel.nextQuestion()
            viewModel.answerOfQuestion(answer)

            lifecycleScope.launch {
                delay(900) // average waiting time
                binding.answerLabel.isEnabled = true
            }


            binding.listContainer.post {
                binding.listContainer.fullScroll(View.FOCUS_DOWN)
            }
        }


        viewModel.questionNumber.observe(viewLifecycleOwner, Observer {
            when (it) {
                0 -> {
                    // yes or no
                    binding.yesOrNo.yesOrNoContainer.visibility = View.VISIBLE
                    binding.cuisines.cuisinesContainer.visibility = View.GONE
                    binding.dietPrograms.dietProgramsContainer.visibility = View.GONE
                    binding.ingredients.ingredientsContainer.visibility = View.GONE
                    binding.answer.isEnabled = false
                    viewModel.answerOfQuestion(name)
                }
                1 -> {
                    // query
                    binding.yesOrNo.yesOrNoContainer.visibility = View.GONE
                    binding.cuisines.cuisinesContainer.visibility = View.GONE
                    binding.dietPrograms.dietProgramsContainer.visibility = View.GONE
                    binding.ingredients.ingredientsContainer.visibility = View.VISIBLE
                    binding.answer.isEnabled = true
                }
                2 -> {
                    // cuisines
                    binding.yesOrNo.yesOrNoContainer.visibility = View.GONE
                    binding.cuisines.cuisinesContainer.visibility = View.VISIBLE
                    binding.dietPrograms.dietProgramsContainer.visibility = View.GONE
                    binding.ingredients.ingredientsContainer.visibility = View.GONE
                    binding.answer.isEnabled = false
                }
                3 -> {
                    // yes or no
                    binding.yesOrNo.yesOrNoContainer.visibility = View.VISIBLE
                    binding.cuisines.cuisinesContainer.visibility = View.GONE
                    binding.dietPrograms.dietProgramsContainer.visibility = View.GONE
                    binding.ingredients.ingredientsContainer.visibility = View.GONE
                    binding.answer.isEnabled = false
                }
                4 -> {
                    // diet programs
                    binding.yesOrNo.yesOrNoContainer.visibility = View.GONE
                    binding.cuisines.cuisinesContainer.visibility = View.GONE
                    binding.dietPrograms.dietProgramsContainer.visibility = View.VISIBLE
                    binding.ingredients.ingredientsContainer.visibility = View.GONE
                    binding.answer.isEnabled = false
                }
                5 -> {
                    // ingredients
                    binding.yesOrNo.yesOrNoContainer.visibility = View.GONE
                    binding.cuisines.cuisinesContainer.visibility = View.GONE
                    binding.dietPrograms.dietProgramsContainer.visibility = View.GONE
                    binding.ingredients.ingredientsContainer.visibility = View.VISIBLE
                    binding.answer.isEnabled = true
                }
                6 -> {
                    binding.yesOrNo.yesOrNoContainer.visibility = View.GONE
                    binding.cuisines.cuisinesContainer.visibility = View.GONE
                    binding.dietPrograms.dietProgramsContainer.visibility = View.GONE
                    binding.ingredients.ingredientsContainer.visibility = View.GONE
                    binding.answerLabel.isEnabled = false
                    binding.answer.isEnabled = false
                }
            }
        })

        viewModel.question.observe(viewLifecycleOwner, Observer {

            if (!it.isNullOrBlank()) {
                val questionView = QuestionBinding.inflate(layoutInflater)
                questionView.text.text = it
                binding.list.addView(questionView.root)

                binding.listContainer.post {
                    binding.listContainer.fullScroll(View.FOCUS_DOWN)
                }
            }

        })

        viewModel.answer.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrBlank()) {
                val answerView = AnswerBinding.inflate(layoutInflater)
                answerView.text.text = it
                binding.list.addView(answerView.root)

                binding.listContainer.post {
                    binding.listContainer.fullScroll(View.FOCUS_DOWN)
                }
            } else if (it != null && it.isEmpty()) {
                val answerView = AnswerBinding.inflate(layoutInflater)
                answerView.text.text = getString(R.string.no_answer)
                binding.list.addView(answerView.root)

                binding.listContainer.post {
                    binding.listContainer.fullScroll(View.FOCUS_DOWN)
                }
            }
        })

        viewModel.recipe.observe(viewLifecycleOwner, Observer { recipe ->
            if (recipe != null) {
                val recipeView = RecipeBinding.inflate(layoutInflater)
                recipeView.recipe = recipe
                recipeView.card.setOnClickListener {
                    if (recipe.sourceUrl.isNotEmpty()) {
                        val browserIntent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(recipe.sourceUrl))
                        startActivity(browserIntent)
                    }
                }
                binding.list.addView(recipeView.root)
                binding.listContainer.post {
                    binding.listContainer.fullScroll(View.FOCUS_DOWN)
                }
                for (index in 0 until binding.ingredients.ingredients.childCount) {
                    (binding.ingredients.ingredients.getChildAt(index) as Chip).visibility =
                        View.VISIBLE
                }
                viewModel.questionNumber.value = 0
                binding.answerLabel.isEnabled = true
            }
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrBlank()) {
                val errorView = ErrorBinding.inflate(layoutInflater)
                if (it == getString(R.string.check_network)) {
                    errorView.tryAgain.visibility = View.VISIBLE
                    errorView.tryAgain.setOnClickListener {
                        viewModel.getRecipe()
                    }
                } else {
                    for (index in 0 until binding.ingredients.ingredients.childCount) {
                        (binding.ingredients.ingredients.getChildAt(index) as Chip).visibility =
                            View.VISIBLE
                    }
                    viewModel.questionNumber.value = 0
                    binding.answerLabel.isEnabled = true
                }
                errorView.text.text = it
                binding.list.addView(errorView.root)
                binding.listContainer.post {
                    binding.listContainer.fullScroll(View.FOCUS_DOWN)
                }
            }
        })

        binding.yesOrNo.yesOrNo.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.yes || checkedId == R.id.no)
                binding.answer.setText(group.findViewById<Chip>(checkedId).text.toString())
            else
                binding.answer.setText("")
        }

        val cuisines = mutableListOf<String>()
        for (index in 0 until binding.cuisines.cuisines.childCount) {
            val chip = binding.cuisines.cuisines.getChildAt(index) as Chip
            chip.setOnCheckedChangeListener { v, isChecked ->
                if (isChecked) {
                    cuisines.add(v.text.toString())
                } else {
                    cuisines.remove(v.text.toString())
                }
                if (cuisines.size > 0) binding.answer.setText(cuisines.joinToString(","))
                else binding.answer.setText("")
            }
        }

        binding.dietPrograms.dietPrograms.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.gluten_free || checkedId == R.id.ketogenic
                || checkedId == R.id.vegetarian || checkedId == R.id.lacto_vegetarian
                || checkedId == R.id.ovo_vegetarian || checkedId == R.id.vegan
                || checkedId == R.id.pescetarian || checkedId == R.id.paleo
                || checkedId == R.id.primal || checkedId == R.id.whole30
            ) binding.answer.setText(group.findViewById<Chip>(checkedId).text.toString())
            else binding.answer.setText("")
        }

        val ingredients = mutableListOf<String>()
        for (index in 0 until binding.ingredients.ingredients.childCount) {
            val chip = binding.ingredients.ingredients.getChildAt(index) as Chip
            chip.setOnCheckedChangeListener { v, isChecked ->
                if (isChecked) {
                    ingredients.add(v.text.toString())
                } else {
                    ingredients.remove(v.text.toString())
                }
                if (ingredients.size > 0) binding.answer.setText(ingredients.joinToString(","))
                else binding.answer.setText("")
            }
        }
    }

    private fun hideKeyboard(view: View) {
        (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            view.windowToken,
            0
        )
    }
}