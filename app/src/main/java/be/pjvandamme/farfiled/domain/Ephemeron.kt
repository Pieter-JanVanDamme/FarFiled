package be.pjvandamme.farfiled.domain

data class Ephemeron(
    val relation: Relation,
    val title: String,
    val content: String
){
    companion object{
        val description: String = "currently relevant topics"
        val detailedDescription: String = "currently relevant projects/circumstances or latest " +
                "developments in some area, topics of conversation, anything important for " +
                "future interactions (promises/debts/favors owed, important dates in the " +
                "near-future)"
    }
}