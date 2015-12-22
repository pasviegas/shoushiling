## Shoushiling - (Rock, Paper, Scissors) [![Coverage Status](https://coveralls.io/repos/pasviegas/shoushiling/badge.svg?branch=master&service=github)](https://coveralls.io/github/pasviegas/shoushiling?branch=master) [![Build Status](https://travis-ci.org/pasviegas/shoushiling.svg?branch=master)](https://travis-ci.org/pasviegas/shoushiling)

The first known mention of the game was in the 
book Wuzazu (zh) (simplified Chinese: 五杂俎; traditional Chinese: 五雜組) by the Chinese 
Ming-dynasty writer Xie Zhaozhi (谢肇淛; fl. ca. 1600), who wrote that the game dated back 
to the time of the Chinese Han dynasty (206 BC – 220 AD).[6] In the book, the game was 
called **shoushiling** (手势令; lit "hand command"). Li Rihua's (李日华) book Note of Liuyanzhai (六砚斋笔记) 
also mentions this game, calling it shoushiling (手势令), huozhitou (豁指头), or huoquan (豁拳).

## Requirements

You'll need at least Java SDK 7 to run.

## Play!

You can either:

- Compile locally running: `sh scripts/play.sh` (or `sh scripts/play.sh rpsls.game` for a custom game) 

- Get the deployed version: `wget https://dl.bintray.com/pasviegas/maven/com/pasviegas/shoushiling_2.11/0.2.0/shoushiling_2.11-0.2.0-runnable.jar && java -jar shoushiling_2.11-0.2.0-runnable.jar`  

## Development

Run `activator ~test`

## Deployment

Just push to master, be aware that you have to bump the version in order to do so.

Don't forget to change scripts/play.sh to the new version before pushing (or be a better person and have a commit hook :P)

## Design

![diagram](https://raw.githubusercontent.com/pasviegas/shoushiling/master/docs/shoushilingDiagram.png)


### GameLoop

The GameLoop is the interface between the GameSystem and the User. It acts as a stream of interactions, ending 
when there is no other next stage in the GameState.
 
The way it interacts with the user is not defined by it, so it can be easily tested, with no need for integration 
or functional tests. 

### GameSystem

The GameSystem is a partial function that transforms a GameInput holding the current GameState into a possible next
GameState. It is mostly stateless, except for the Random generator that is held in case of a player vs computer game.

In case of not being able to compute the next Game State, it will return a Failure.

As it is mostly stateless, testing it (or playing/starting the game) at any given stage is trivial.  

### GameState

GameState holds all computed data for the current game. As the game progresses, the interaction between 
the User and the GameSystem transforms the GameState until it reaches a conclusion, marked by the stage GameOver.

### BalancedGame

A BalancedGame is described as:
 - A two players match;
 - As long as the number of moves is an odd number and that each move defeats exactly half of the other moves 
while being defeated by the other half, any combination of moves will function as a game.

In this way there will be always a rule that matches the chosen throws with either a winner or a tie.


