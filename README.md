I KNOW I PUSHED AGAIN AT LIKE 12! BUT WE FINISHED UP EARLIER AND DIDN'T CHANGE ANYTHING ABOUT THE GAME ITSELF!!!
IT WAS JUST THAT WE FORGOT TO ADD A QUICK INTRO COMMENT TO THE PROGRAM ON THE TOP


Also i thought that maybe it's easier for you to read the READme.TXT if i just all have it on github

– Hints on how to play (skip levels?)

 Our game called 3072 is basically the same as 2048. The 5 by 5 board starts with 3 random blocks of 3 on them. Every time the time board changes, which includes a merge happens or a block shifts to a new place, a new block will spawn.

You keep playing till you get to the end goal of merging a block of 3072! Or you stop playing till you somehow fill out the entire and are not able to merge anything. 

When you win, a winner screen will pop up, after like 4 seconds, that disappears, and a new board is made, which you then can keep playing. 

A similar thing happens when you lose; a loser screen will pop up, after 4 seconds, it disappears, and a new board is made as well, which then you can keep playing.

There’s also a button that allows you to just make a new board if you don’t like the current one that you are playing. 

– Any functionalities missing from your original plan for the game

The one function that’s missing from the original plan from the game is that we planned to add a high score that would work like how you expect it would. 

Originally, the high score should have been read from a file that kept track of the high score over every single game played.

However, we kind of struggled to implement the file reading and writing. We adapted and decided instead that high scores still can be stored, the high score will only stay if you keep the game open. If you close the game, the high score will reset to 0. 

– Any additional functionalities added from your original plan

I think we added two new functionalities that wasn’t originally planned. The first is anti-key spamming. Without this feature, if you decided to not play this game like a normal person and spam the keys, it would break the board animation and basically the entire game would crash due to way too many inputs at once.

With this anti-spam, you have to wait 0.2 seconds before you can press a key again.

The second feature is the hovering of buttons. Doesn’t add to the functionality of the base mechanics of the game. However! This small feature took us a lot of work and coding, which wasn’t originally planned. 

– Known bugs / errors in your game
The one known bug is that the animation for the board occasionally glitches, causing the board pieces to fly off the board for no apparent reason. It would quickly move back to correct itself.

The annoying part is that sometimes the animation works perfectly fine. Sometimes the animation would tweak out consecutively. I’ve tried debugging this and identifying logical issues with my code. Couldn’t find it. 

Just an annoying little bug, doesn’t affect the functionality of the game though. 


Another bug would be that when the winner screen or the lost screen pops up, I think you are still able to click around, even though you can’t physically see anything else. This technically shouldn’t be allowed as it might mess something up. 


– Any other important info for me to play/mark your game

To test the winner screen!! 
--> press control f and type if (board[i][j] == 3072) 
--> you can change 3072 to something 6,12,24
--> HOWEVER!! if you have the fillBoardNoMerges method enable, you should only use 24 and above because it will display that you won instead that you lost! 
--> this is because the fillBoardNoMerges method fills the board with 3,6, and 12's. If you put 6 or 12 as winning condition, it's gonna say you won

To test the lost screen!! 
--> press control f and type fillBoardNoMerges
--> uncomment this code to test the lost screen. It will automatically fill the entire board after one move
