package be.pjvandamme.farfiled.model

enum class LifeArea(
    val title: String,
    val description: String,
    val detailedDescription: String,
    val sequenceNumber: Int
){
//    SYNOPSIS(
//        "summary",
//        "short description",
//        "short description of this relation, usually including their primary " +
//                "role(s) in life, their value to the user, and perhaps a context or memory aid",
//        -1
//    ),
    EPHEMERA(
        "now",
        "currently relevant topics",
        "currently relevant projects/circumstances or latest " +
                "developments in some area, topics of conversation, anything important for " +
                "future interactions (promises/debts/favors owed, important dates in the " +
                "near-future)",
        0),
    PERSONAL(
        "self",
        "physical and mental attributes",
        "personality, values and beliefs (including faith/spirituality), personal " +
                "likes and dislikes (as well as gift ideas), vocabulary, body language & " +
                "idiosyncracies, body, health & medical history",
        1),
    VOCATION(
        "work",
        "education and occupation",
        "educational background and career trajectory, occupation/way of making " +
                "a living or primary time commitment in life (e.g. independently wealthy " +
                "philanthropist), life's work",
        2),
    DOMESTIC(
        "home",
        "family and home life",
        "housing & home life, relationship to significant other (including a " +
                "brief history), children (including current relationship, brief history, and " +
                "anything noteworthy such as hobbies, interests, character, life at school " +
                "etc.), childhood and relationship to parents",
        3),
    COMMUNITY(
        "circle",
        "other significant relationships",
        "primary relationships (friends & family) with brief history (notable " +
                "events or turning points) and last known situation, any noteworthy relationship " +
                "to specific groups/organisations outside of work/hobbies",
        4),
    LEISURE(
        "fun",
        "leisure activities",
        "manner of spending free time, hobbies, sports, travel, how they " +
                "relax/wind down, volunteering/charity, engagement with (local) politics, " +
                "participation in (local) (recurring) cultural or other events",
        5)
}