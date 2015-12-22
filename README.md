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
- Compile locally running: `sh scripts/play.sh` 
- Get the deployed version: `wget https://dl.bintray.com/pasviegas/maven/com/pasviegas/shoushiling_2.11/0.2.0/shoushiling_2.11-0.2.0-runnable.jar && java -jar shoushiling_2.11-0.2.0-runnable.jar`  

## Development

Run `activator ~test`

## Deployment

Just push to master, be aware that you have to bump the version in order to do so.

Don't forget to change scripts/play.sh to the new version before pushing (or be a better person and have a commit hook :P)

