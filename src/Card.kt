class Card(val suit: Suit, val value: Value)
{
    override fun toString(): String {
        return "Card($suit, $value)"
    }

}