open class Deck
{
    var cards = ArrayList<Card>()

    override fun toString(): String {
        var cardListOutput = ""
        for((i, aCard) in this.cards.withIndex())
        {
            cardListOutput += "\n ${i+1} - $aCard"
        }
        return cardListOutput
    }

    fun removeCard(i: Int)
    {
        this.cards.removeAt(i)
    }
    fun getCard(i: Int): Card
    {
        return this.cards[i]
    }
    fun addCard(addCard: Card)
    {
        this.cards.add(addCard)
    }
    //Draws from the deck
    fun draw(comingFrom: Deck)
    {
        this.cards.add(comingFrom.getCard(0))
        comingFrom.removeCard(0)
    }

    fun moveAllToDeck(moveTo: Deck)
    {
        val thisDeckSize = this.cards.size
        //put cards into moveto deck
        for(n in 0 until thisDeckSize) {
            moveTo.addCard(this.getCard(n))
        }

        for(n in  0 until thisDeckSize)
        {
            this.removeCard(0)
        }
    }

    fun cardsValue(): Int
    {
        var totalValue = 0
        var aces = 0

        for(aCard: Card in this.cards)
        {
            when(aCard.value)
            {
                Value.TWO -> totalValue += 2
                Value.THREE -> totalValue += 3
                Value.FOUR -> totalValue += 4
                Value.FIVE -> totalValue += 5
                Value.SIX -> totalValue += 6
                Value.SEVEN -> totalValue += 7
                Value.EIGHT -> totalValue += 8
                Value.NINE -> totalValue += 9
                Value.TEN -> totalValue += 10
                Value.JACK -> totalValue += 10
                Value.QUEEN -> totalValue += 10
                Value.KING -> totalValue += 10
                Value.ACE -> aces += 1
            }
        }
        //determine value of the ace
        for(i in 0 until aces)
        {
            totalValue += if(totalValue>10) {
                1
            } else{
                11
            }
        }

        return totalValue

    }

    fun deckSize():Int
    {
        return this.cards.size
    }
}