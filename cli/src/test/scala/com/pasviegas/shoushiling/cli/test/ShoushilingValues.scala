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
package com.pasviegas.shoushiling.cli.test

import com.pasviegas.shoushiling.cli.system.GameSystem
import com.pasviegas.shoushiling.core.DefaultGame
import com.pasviegas.shoushiling.core.GamePlay.{Match, Player, Throw}
import com.pasviegas.shoushiling.{cli, core}

import scala.util.Random

trait ShoushilingValues {
  val TestGameSystem = GameSystem(new Random)

  val SinglePlayer = cli.GameMode("single")
  val MultiPlayer = cli.GameMode("multi")

  val RockPaperScissors = Some(DefaultGame)

  val Rock = core.Rock
  val Paper = core.Paper
  val Scissors = core.Scissors

  val ATie = Some(Match(
    Player("1", Throw(Paper)),
    Player("2", Throw(Paper))
  ))

  val correctGame = getClass.getResource("/correct.game").getPath
  val wrongGame = getClass.getResource("/wrong.game").getPath
}
