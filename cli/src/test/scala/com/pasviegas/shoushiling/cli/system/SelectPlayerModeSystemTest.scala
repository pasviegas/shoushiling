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
package com.pasviegas.shoushiling.cli.system

import com.pasviegas.shoushiling.cli.system.exceptions.UnknownGameModeSelected
import com.pasviegas.shoushiling.cli.system.inputs.SelectPlayerMode
import com.pasviegas.shoushiling.cli.system.messages.{MultiPlayerSelectedMessage, SinglePlayerSelectedMessage}
import com.pasviegas.shoushiling.cli.system.stages.HomePlayerChooseMoveToThrow
import com.pasviegas.shoushiling.cli.{GameMode, GameState}
import org.scalatest.{FlatSpec, MustMatchers}

import scala.util.Failure

class SelectPlayerModeSystemTest extends FlatSpec with MustMatchers {

  "A player" must "be able to choose to play alone" in {
    import com.pasviegas.shoushiling.core._
    val state = GameState(game = Some(DefaultGame))

    (SelectPlayerModeSystem request SelectPlayerMode(state, GameMode("single")))
      .get.mode must be(Some(GameMode("single")))
  }

  "When the player selects single player, he" should "receive a feedback message" in {
    import com.pasviegas.shoushiling.core._
    val state = GameState(game = Some(DefaultGame))
    (SelectPlayerModeSystem request SelectPlayerMode(state, GameMode("single")))
      .get.message must be(Some(SinglePlayerSelectedMessage(state)))
  }

  "After the player chooses single player" must "choose its move" in {
    import com.pasviegas.shoushiling.core._
    val state = GameState(game = Some(DefaultGame))
    (SelectPlayerModeSystem request SelectPlayerMode(state, GameMode("single")))
      .get.nextStage must be(HomePlayerChooseMoveToThrow())
  }

  "A player" must "be able to choose to play with another player" in {
    import com.pasviegas.shoushiling.core._
    val state = GameState(game = Some(DefaultGame))
    (SelectPlayerModeSystem request SelectPlayerMode(state, GameMode("multi")))
      .get.mode must be(Some(GameMode("multi")))
  }

  "When the player selects multi player, he" should "receive a feedback message" in {
    import com.pasviegas.shoushiling.core._
    val state = GameState(game = Some(DefaultGame))
    (SelectPlayerModeSystem request SelectPlayerMode(state, GameMode("multi")))
      .get.message must be(Some(MultiPlayerSelectedMessage(state)))
  }

  "After the player chooses multi player" must "choose its move" in {
    import com.pasviegas.shoushiling.core._
    val state = GameState(game = Some(DefaultGame))
    (SelectPlayerModeSystem request SelectPlayerMode(state, GameMode("multi")))
      .get.nextStage must be(HomePlayerChooseMoveToThrow())
  }

  "When the user selects an unknown game mode, it" should "not be able to play it" in {
    val mmorpgGameMode: SelectPlayerMode = SelectPlayerMode(GameState(), GameMode("MMORPG"))
    (SelectPlayerModeSystem request mmorpgGameMode) must be(Failure(UnknownGameModeSelected))
  }
}

