import java.util.*

class FullDeck(var numberOfDecks: Int = 1) : Deck()
{
    init {
        val userInput = Scanner(System.`in`)
        //supports playing with multiple decks
        //println("How many decks would you like to play with(1-8)?")
        numberOfDecks = 1
        //creates full deck
        for(x in 0 until numberOfDecks) {
            for (n in 0 until numberOfDecks) {
                for (cardSuit in Suit.values()) {
                    for (cardValue in Value.values()) {
                        this.cards.add(Card(cardSuit, cardValue))
                    }
                }
            }
        }
        //shuffles cards
        this.cards.shuffle()

    }
}