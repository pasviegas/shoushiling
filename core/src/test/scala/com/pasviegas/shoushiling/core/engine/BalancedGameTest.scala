// This is free and unencumbered software released into the public domain.
//
// Anyone is free to copy, modify, publish, use, compile, sell, or
// distribute this software, either in source code form or as a compiled
// binary, for any purpose, commercial or non-commercial, and by any
// means.
//
// In jurisdictions that recognize copyright laws, the author or authors
// of this software dedicate any and all copyright interest in the
// software to the public domain. We make this dedication for the benefit
// of the public at large and to the detriment of our heirs and
// successors. We intend this dedication to be an overt act of
// relinquishment in perpetuity of all present and future rights to this
// software under copyright law.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
// IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
// OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
// ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
// OTHER DEALINGS IN THE SOFTWARE.
//
// For more information, please refer to <http://unlicense.org/>
package com.pasviegas.shoushiling.core.engine

import com.pasviegas.shoushiling.core.GamePlay.{Match, Move, Player, Throw}
import org.scalatest.{FlatSpec, MustMatchers}

class BalancedGameTest extends FlatSpec with MustMatchers {

  import com.pasviegas.shoushiling.core._
  import com.pasviegas.shoushiling.core.test._

  "Any game " should "be played in a match" in {
    BalancedGame().play(Match(Player("1", Throw(Rock)) -> Player("2", Throw(Rock)))).`match` must beAMatch
  }

  "Only a game with a rule set " can "have a winner" in {
    val rockWinsOverScissors = BalancedGame(Set(
      GameRule(Rock -> "crushes" -> Scissors)
    ))

    rockWinsOverScissors.play(Match(Player("1", Throw(Rock)) -> Player("2", Throw(Scissors)))).winner must
      beThe(Player("1", Throw(Rock)))
  }

  "Any game " can "be a tie" in {
    BalancedGame().play(Match(Player("1", Throw(Rock)) -> Player("1", Throw(Rock)))) must beATie
  }

  "In a particular Game a player that throws Paper" must "win against a player that throws Scissors" in {
    val customGame = BalancedGame(Set(
      GameRule(Paper -> "envelopes" -> Scissors),
      GameRule(Scissors -> "beats" -> Rock),
      GameRule(Rock -> "smashes" -> Paper)
    ))

    customGame.play(Match(Player("1", Throw(Scissors)) -> Player("2", Throw(Paper)))).winner must
      beThe(Player("2", Throw(Paper)))
  }

  "A game with unbalanced losers" should "throw GameNotBalancedException" in {
    a[GameNotBalancedException] should be thrownBy {
      BalancedGame(Set(
        GameRule(Paper -> "envelopes" -> Scissors),
        GameRule(Paper -> "covers" -> Rock),
        GameRule(Rock -> "crushes" -> Scissors),
        GameRule(Rock -> "smashes" -> Paper)
      ))
    }
  }

  "A game with unbalanced winners" should "throw GameNotBalancedException" in {
    a[GameNotBalancedException] should be thrownBy {
      BalancedGame(Set(
        GameRule(Paper -> "envelopes" -> Scissors),
        GameRule(Paper -> "covers" -> Scissors),
        GameRule(Scissors -> "crushes" -> Rock),
        GameRule(Rock -> "smashes" -> Rock)
      ))
    }
  }

  "In a particular Game " should "be easily extensible to play new moves" in {
    val Lizard = Move("Lizard")
    val Spock = Move("Spock")

    val customGame = BalancedGame(Set(
      GameRule(Scissors -> "cuts" -> Paper),
      GameRule(Scissors -> "decapitates" -> Lizard),
      GameRule(Paper -> "covers" -> Rock),
      GameRule(Paper -> "disproves" -> Spock),
      GameRule(Rock -> "crushes" -> Scissors),
      GameRule(Rock -> "crushes" -> Lizard),
      GameRule(Lizard -> "poisons" -> Spock),
      GameRule(Lizard -> "eats" -> Paper),
      GameRule(Spock -> "smashes" -> Scissors),
      GameRule(Spock -> "vaporizes" -> Rock)
    ))

    customGame.play(Match(Player("1", Throw(Lizard)) -> Player("2", Throw(Spock)))).winner must
      beThe(Player("1", Throw(Lizard)))
  }

  "In the default Game, Rock:" must "win over Scissors" in {
    DefaultGame.play(Match(Player("1", Throw(Rock)) -> Player("2", Throw(Scissors)))).winner must
      beThe(Player("1", Throw(Rock)))
  }

  "In the default Game, Scissors:" must "win over Paper" in {
    DefaultGame.play(Match(Player("1", Throw(Scissors)) -> Player("2", Throw(Paper)))).winner must
      beThe(Player("1", Throw(Scissors)))
  }

  "In the default Game, Paper:" must "win over Rock" in {
    DefaultGame.play(Match(Player("1", Throw(Paper)) -> Player("2", Throw(Rock)))).winner must
      beThe(Player("1", Throw(Paper)))
  }

}

