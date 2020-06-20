package com.amg.recipesapp

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.amg.recipesapp.data.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatViewModel @ViewModelInject constructor(private val repository: Repository) : ViewModel() {

    private var appStartUp = true

    private val YES = "Yes"
    private val NO = "No"

//    // 25 cuisine [0-24]
//    private val cuisines = arrayOf(
//        "African", "American", "British", "Cajun", "Caribbean", "Chinese",
//        "Eastern European", "European", "French", "German", "Greek", "Indian",
//        "Irish", "Italian", "Japanese", "Korean", "Latin American", "Mediterranean",
//        "Mexican", "Middle Eastern", "Nordic", "Southern", "Spanish", "Thai", "Vietnamese"
//    )
//
//    // 10 diet programs [0-9]
//    private val dietPrograms = arrayOf(
//        "Gluten Free", "Ketogenic", "Vegetarian", "Lacto-Vegetarian",
//        "Ovo-Vegetarian", "Vegan", "Pescetarian", "Paleo", "Primal", "Whole30"
//    )

    var query = ""
    var cuisine = ""
    var diet = ""
    var excludeIngredients = ""

    // we have 6 questions query 1,2 , cuisine 3, diet 4,5, excludeIngredients 6
    var questionNumber = MutableLiveData<Int>(0)
    val question = MutableLiveData<String>(null)
    val answer = MutableLiveData<String>(null)
    val recipe = repository.recipe
    val error = repository.error

    fun answerOfQuestion(answer: String) {
        when (questionNumber.value) {
            0 -> {
                viewModelScope.launch {
                    if (appStartUp) {
                        appStartUp = false
                        delay(200)
                        question.value = "Hello, $answer"
                        delay(200)
                        question.value = "I\'m Chatbot built on spoonacular api."
                    }
                    delay(200)
                    question.value = "Do you search for specific recipe?"
                }
//                questionNumber.value?.plus(1) // next question number = 1
                //Display Yes or No
            }
            1 -> {
                this.answer.value = answer
                if (answer == YES) { // yes = 0 , no = 1
                    viewModelScope.launch {
                        delay(200)
                        question.value = "Please tell me what do you love to eat?"
                        delay(200)
                        question.value = "example: Pizza, Seafood, both."
                    }
//                    questionNumber.value?.plus(1) // next question number = 2
                    //Display textInput
                } else {
                    nextQuestion()
                    viewModelScope.launch {
                        delay(200)
                        question.value = "Ok, We will find out a special recipe for you."
                        delay(200)
                        question.value = "So, Do you prefer specific cuisine?"
                    }
//                    questionNumber.value?.plus(2) // next question number = 3
                    // Display choices (African, American, British, Cajun, Caribbean, Chinese
                    // Eastern European, European, French, German, Greek, Indian, Irish, Italian
                    // Japanese, Jewish, Korean, Latin American, Mediterranean, Mexican,
                    // Middle Eastern, Nordic, Southern, Spanish, Thai, Vietnamese)
                }
            }
            2 -> {
                this.answer.value = answer
                query = answer
                viewModelScope.launch {
                    delay(200)
                    if (answer.isNotEmpty()) question.value = "It seem a good choice."
                    else question.value = "It seem you havn\'t decided yet!"
                    delay(200)
                    question.value = "Do you prefer specific cuisine?"
                    delay(200)
                    question.value = "I recommend italian."
                }
//                questionNumber.value?.plus(1) // next question number = 3
                // Display choices (African, American, British, Cajun, Caribbean, Chinese
                // Eastern European, European, French, German, Greek, Indian, Irish, Italian
                // Japanese, Jewish, Korean, Latin American, Mediterranean, Mexican,
                // Middle Eastern, Nordic, Southern, Spanish, Thai, Vietnamese)
            }
            3 -> {
                this.answer.value = answer
                cuisine = answer
                viewModelScope.launch {
                    delay(200)
                    if (answer.isNotEmpty()) question.value = "I love Italian pizza."
                    else question.value = "It is hard to choose I know!"
                    delay(200)
                    question.value = "Are you practising specific diet program?"
                }
//                questionNumber.value?.plus(1) // next question number = 4
                //Display Yes or No
            }
            4 -> {
                this.answer.value = answer
                if (answer == YES) {
                    viewModelScope.launch {
                        delay(200)
                        question.value = "You can choose one of these diet program?"
                    }
//                    questionNumber.value?.plus(1) // next question number = 5
                    // Display choices (Gluten Free, Ketogenic, Vegetarian, Lacto-Vegetarian
                    // Ovo-Vegetarian, Vegan, Pescetarian, Paleo, Primal, Whole30)
                } else {
                    nextQuestion()
                    viewModelScope.launch {
                        delay(200)
                        question.value = "I hate diet programs, they make me feel hungry."
                        delay(200)
                        question.value = "You can exclude one or more of these Ingredients."
//                    questionNumber.value?.plus(2) // next question number = 6
                        // Display choices (Dairy, Egg, Gluten, Grain, Peanut, Seafood,
                        // Sesame, Shellfish, Soy, Sulfite, Tree Nut, Wheat)
                    }
                }
            }
            5 -> {
                this.answer.value = answer
                diet = answer
                viewModelScope.launch {
                    delay(200)
                    if (answer.isNotEmpty()) {
                        question.value =
                            "$answer is a good program here is some information about it."
                        delay(200)
                        when (answer) {
                            "Gluten Free" ->
                                question.value =
                                    "Eliminating gluten means avoiding wheat, barley, rye, and other gluten-containing grains and foods made from them (or that may have been cross contaminated)."
                            "Ketogenic" ->
                                question.value =
                                    "The keto diet is based more on the ratio of fat, protein, and carbs in the diet rather than specific ingredients. Generally speaking, high fat, protein-rich foods are acceptable and high carbohydrate foods are not."
                            "Vegetarian" ->
                                question.value =
                                    "No ingredients may contain meat or meat by-products, such as bones or gelatin."
                            "Lacto-Vegetarian" ->
                                question.value =
                                    "All ingredients must be vegetarian and none of the ingredients can be or contain egg."
                            "Ovo-Vegetarian" ->
                                question.value =
                                    "All ingredients must be vegetarian and none of the ingredients can be or contain dairy."
                            "Vegan" ->
                                question.value =
                                    "No ingredients may contain meat or meat by-products, such as bones or gelatin, nor may they contain eggs, dairy, or honey."
                            "Pescetarian" ->
                                question.value =
                                    "Everything is allowed except meat and meat by-products - some pescetarians eat eggs and dairy, some do not."
                            "Paleo" ->
                                question.value =
                                    "Allowed ingredients include meat (especially grass fed), fish, eggs, vegetables, some oils (e.g. coconut and olive oil), and in smaller quantities, fruit, nuts, and sweet potatoes. We also allow honey and maple syrup (popular in Paleo desserts, but strict Paleo followers may disagree). Ingredients not allowed include legumes (e.g. beans and lentils), grains, dairy, refined sugar, and processed foods."
                            "Primal" ->
                                question.value =
                                    "Very similar to Paleo Program, except dairy is allowed - think raw and full fat milk, butter, ghee, etc."
                            "Whole30" ->
                                question.value =
                                    "Allowed ingredients include meat, fish/seafood, eggs, vegetables, fresh fruit, coconut oil, olive oil, small amounts of dried fruit and nuts/seeds. Ingredients not allowed include added sweeteners (natural and artificial, except small amounts of fruit juice), dairy (except clarified butter or ghee), alcohol, grains, legumes (except green beans, sugar snap peas, and snow peas), and food additives, such as carrageenan, MSG, and sulfites."
                        }
                    } else {
                        question.value = "I hate diet programs, they make me feel hungry."
                    }
                    delay(200)
                    question.value = "You can exclude one or more of these Ingredients."
                }
//                questionNumber.value?.plus(1) // next question number = 5
                // Display choices (Dairy, Egg, Gluten, Grain, Peanut, Seafood,
                // Sesame, Shellfish, Soy, Sulfite, Tree Nut, Wheat)
            }
            6 -> {
                this.answer.value = answer
                excludeIngredients = answer
                getRecipe()
            }
        }
    }

    fun getRecipe() {
        repository.getRecipes(mutableListOf(query, cuisine, diet, excludeIngredients))
    }

    fun nextQuestion() {
        val num = questionNumber.value!!
        questionNumber.value = num + 1
    }
}