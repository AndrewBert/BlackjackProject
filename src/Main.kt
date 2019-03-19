import java.util.Scanner

fun main()
{

    println("Welcome to Blackjack!")

    val userInput = Scanner(System.`in`)

    var playWithHumans = false

    println("Would you like to play with AI(1) or humans(2)?")
    if (userInput.nextInt() == 2)
    {
        playWithHumans = true
    }

    //Create playing deck
    val playingDeck = FullDeck()
    //Create a deck for the player
    val playerDeck = Deck()
    //Create a deck for the dealer
    val dealerDeck = Deck()

    var playerMoney = 1000

    //Game loop
    if(playWithHumans) {
        println("Enter number of players: ")
        val numberOfPlayers = userInput.nextInt()
        val playerList = mutableListOf<Player>()
        var endGame = false

        for (n in 0 until numberOfPlayers) {
            playerList.add(Player())
        }
        var numberOfOutPlayers = 0
            while (!endGame) {
                //Play on!
                for (n in 0 until numberOfPlayers) {
                    //Take players bet
                    println("Player ${n+1} you have ${playerList[n].playerMoney}, how much would you like to bet?")
                    playerList[n].playerBet = userInput.nextInt()
                    //todo add a try-catch
                    if (playerList[n].playerBet > playerList[n].playerMoney) {
                        println("You cannot bet more than you have. Please leave")
                        break
                    }
                }

                //Start Dealing
                for (n in 0 until numberOfPlayers) {
                    //Player gets two cards
                    repeat(2)
                    { playerList[n].playerDeck.draw(playingDeck) }
                }

                //Dealer gets two cards
                repeat(2)
                { dealerDeck.draw(playingDeck) }
                var allPlayersBusted = false
                var dealerTurnOver = false
                for (n in 0 until numberOfPlayers) {
                    playerList[n].endRound = false
                    while (true) {

                        println("\n**Player ${n + 1}'s** hand: ${playerList[n].playerDeck}")
                        println("Your hand is valued at: ${playerList[n].playerDeck.cardsValue()}\n")

                        //Display dealer hand
                        println("Dealer Hand: ${dealerDeck.getCard(0)} and [Hidden]")

                        //What does the player want to do?
                        println("Would you like to (1)Hit or (2)Stand?")
                        val response = userInput.nextInt()

                        //They hit
                        if (response == 1) {
                            playerList[n].playerDeck.draw(playingDeck)
                            println("You draw a: ${playerList[n].playerDeck.getCard(playerList[n].playerDeck.deckSize() - 1)}")
                            //Bust if >21
                            if (playerList[n].playerDeck.cardsValue() > 21) {
                                println("Bust. Currently valued at: ${playerList[n].playerDeck.cardsValue()}")
                                playerList[n].playerMoney -= playerList[n].playerBet
                                playerList[n].endRound = true
                                playerList[n].isBusted = true
                                break
                            }
                        }
                        //stand
                        if (response == 2) {
                            break
                        }
                    }
                }
                for(n in 0 until numberOfPlayers) {
                    //Reveal dealers cards
                    if(!playerList[n].isBusted) {
                        println("Dealer cards: $dealerDeck")
                    }
                    //See if dealer has more points than player
                    //Determine if busted
                    var bustedPlayers = 0

                    if (dealerDeck.cardsValue() > playerList[n].playerDeck.cardsValue() && dealerDeck.cardsValue() <= 21 && !playerList[n].endRound) {
                        println("Dealer beats Player ${n + 1}")
                        playerList[n].playerMoney -= playerList[n].playerBet
                        playerList[n].endRound = true
                        bustedPlayers++
                    }
                    if (bustedPlayers == numberOfPlayers) {
                        allPlayersBusted = true
                    }
                    //dealer draws for all players one time
                    if (n == 0) {
                        //Dealer hits at 16, stand at 17
                        while (dealerDeck.cardsValue() < 17 && !allPlayersBusted) {
                            dealerDeck.draw(playingDeck)
                            println("\nDealer draws ${dealerDeck.getCard(dealerDeck.deckSize() - 1)}")
                        }
                    }
                    //Display total value for dealer
                    println("Dealer's Hand is valued at ${dealerDeck.cardsValue()}\n")
                    if (dealerDeck.cardsValue() > 21 && !playerList[n].endRound) {
                        println("Dealer busts. Player ${n + 1} wins!\n")
                        playerList[n].playerMoney += playerList[n].playerBet
                        playerList[n].endRound = true
                    }


                    //Determine if push
                    if (playerList[n].playerDeck.cardsValue() == dealerDeck.cardsValue()&& !playerList[n].endRound) {
                        println("Dealer beats Player ${n + 1}")
                        playerList[n].playerMoney -= playerList[n].playerBet
                        playerList[n].endRound = true
                    }

                    if (playerList[n].playerDeck.cardsValue() > dealerDeck.cardsValue() && !playerList[n].endRound) {
                        println("Player ${n + 1} wins the hand!\n")
                        playerList[n].playerMoney += playerList[n].playerBet
                        playerList[n].endRound = true
                    } else if (!playerList[n].endRound) {
                        println("Player ${n + 1} loses the hand\n")
                        playerList[n].playerMoney -= playerList[n].playerBet
                        playerList[n].endRound = true
                    }

                    playerList[n].playerDeck.moveAllToDeck(playingDeck)
                }
                dealerDeck.moveAllToDeck(playingDeck)
                //Check to see if players still have money
                for(n in 0 until numberOfPlayers) {
                    if (playerList[n].playerMoney == 0) {
                        playerList[n].isOut = true
                        numberOfOutPlayers++
                    }
                    if (numberOfOutPlayers == numberOfPlayers) {
                        endGame = true
                    }
                }
                println("End of hand.\n")
            }
        println("Game over! All players out of money.")
    }

    else if(!playWithHumans)
    {
        playWithAI(playerDeck,playingDeck,dealerDeck)
    }
}

fun playWithAI(playerDeck: Deck, playingDeck: FullDeck, dealerDeck: Deck)
{
    var playerMoney = 1000
    var roundCount = 0
    var highestBankInRound = playerMoney
    var wins = 0
    while (playerMoney > 0&&roundCount <100) {
        //Play on!

        //Take players bet
        println("You have $playerMoney, how much would you like to bet?")
        //Chooses random value between 5 and 20
        var playerBet = (5..20).random()
        if (playerBet > playerMoney) {
            playerBet = playerMoney
        }
        println(playerBet)

        var endRound = false
        //Start Dealing
        //Player gets two cards
        repeat(2)
        { playerDeck.draw(playingDeck) }

        //Dealer gets two cards
        repeat(2)
        { dealerDeck.draw(playingDeck) }

        while (true) {
            println("Your hand: ${playerDeck.toString()}")
            println("Your deck is valued at: ${playerDeck.cardsValue()}")

            //Display dealer hand
            println("Dealer Hand: ${dealerDeck.getCard(0).toString()} and [Hidden]")

            //What does the player want to do?
            println("Would you like to (1)Hit or (2)Stand?")
            var response = 0
            var dealerCardsValue = 2

            if (playerDeck.cardsValue() <= 11) {
                response = 1
            } else {
                //gets the value of the dealers visible card
                when (dealerDeck.getCard(0).value) {
                    Value.TWO -> dealerCardsValue = 2
                    Value.THREE -> dealerCardsValue = 3
                    Value.FOUR -> dealerCardsValue = 4
                    Value.FIVE -> dealerCardsValue = 5
                    Value.SIX -> dealerCardsValue = 6
                    Value.SEVEN -> dealerCardsValue = 7
                    Value.EIGHT -> dealerCardsValue = 8
                    Value.NINE -> dealerCardsValue = 9
                    Value.TEN, Value.JACK, Value.QUEEN, Value.KING -> dealerCardsValue = 10
                    Value.ACE -> dealerCardsValue = 1

                }
                when (playerDeck.cardsValue()) {
                    12 -> when (dealerCardsValue) {
                        1, 2, 3, 7, 8, 9, 10 -> response = 1
                        4, 5, 6 -> response = 2
                        else -> response = 2
                    }
                    13, 14, 15, 16, 17 -> when (dealerCardsValue) {
                        1, 2, 3, 4, 5, 6 -> response = 2
                        else -> response = 1
                    }
                    else -> response = 2

                }
            }

            println(response)

            //They hit
            if (response == 1) {
                playerDeck.draw(playingDeck)
                println("You draw a: ${playerDeck.getCard(playerDeck.deckSize() - 1).toString()}")
                //Bust if >21
                if (playerDeck.cardsValue() > 21) {
                    println("Bust. Currently valued at: ${playerDeck.cardsValue()}")
                    playerMoney -= playerBet
                    endRound = true
                    break
                }
            }
            //stand
            if (response == 2) {
                break
            }
        }
        //Reveal dealers cards
        println("Dealer cards: $dealerDeck")
        //See if dealer has more points than player
        //Determine if busted
        if (dealerDeck.cardsValue() > playerDeck.cardsValue() && !endRound) {
            println("Dealer beats you")
            playerMoney -= playerBet
            endRound = true
        }
        //Dealer hits at 16, stand at 17
        while (dealerDeck.cardsValue() < 17 && !endRound) {
            dealerDeck.draw(playingDeck)
            println("Dealer draws ${dealerDeck.getCard(dealerDeck.deckSize() - 1).toString()}")
        }
        //Display total value for dealer
        println("Dealer's Hand is valued at ${dealerDeck.cardsValue()}")
        if (dealerDeck.cardsValue() > 21 && !endRound) {
            println("Dealer busts. You win!")
            playerMoney += playerBet
            wins++
            endRound = true
        }


        //Determine if push
        if (playerDeck.cardsValue() == dealerDeck.cardsValue()) {
            println("Dealer beats you")
            playerMoney -= playerBet
            endRound = true
        }

        if (playerDeck.cardsValue() > dealerDeck.cardsValue() && !endRound) {
            println("You win the hand!")
            playerMoney += playerBet
            wins++
            endRound = true
        } else if (!endRound) {
            println("You lose the hand")
            playerMoney -= playerBet
            endRound = true
        }

        playerDeck.moveAllToDeck(playingDeck)
        dealerDeck.moveAllToDeck(playingDeck)

        println("End of hand.")
        roundCount++
    }
    println("Game over! You are out of money.")

    if (playerMoney > highestBankInRound) {
        highestBankInRound = playerMoney
    }
    //if you would like to require user to press a button to go to next round, put userInput here

    println("That took $roundCount rounds!")
    println("Your final bank balance was: $playerMoney")
    //println("Highest bank balance was: $highestBankInRound")
    println("You won $wins times")
    println("Win percentage: ${(wins.toDouble()/roundCount.toDouble())*100}")
}



