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
package com.pasviegas.shoushiling.core

import org.scalatest.{FlatSpec, MustMatchers}

class GameRulesTest extends FlatSpec with MustMatchers {

  import com.pasviegas.shoushiling.core.GameRules._

  "Game Rule: Rock crushes Scissors" must "have Rock as winner and Scissors as loser" in {
    GameRule(Rock -> "crushes" -> Scissors) match {
      case GameRule(winner, _, loser) =>
        winner must be(Rock)
        loser must be(Scissors)
    }
  }

  "Game Rule: Scissors cuts Paper" must "have Scissors as winner and Paper as loser" in {
    GameRule(Scissors -> "cuts" -> Paper) match {
      case GameRule(winner, _, loser) =>
        winner must be(Scissors)
        loser must be(Paper)
    }
  }

  "Game Rule: Paper covers Scissors" must "have Paper as winner and Scissors as loser" in {
    GameRule(Paper -> "cuts" -> Scissors) match {
      case GameRule(winner, _, loser) =>
        winner must be(Paper)
        loser must be(Scissors)
    }
  }

}

