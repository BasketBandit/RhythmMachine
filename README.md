# JAVA-RhythmMachine (0.8.6)

[![Build Status](https://travis-ci.org/Galaxiosaurus/JAVA-RhythmMachine.svg?branch=master)](https://travis-ci.org/Galaxiosaurus/JAVA-RhythmMachine)

As the description suggests, this repository is purely for my own experimental code. Things that I'm playing around with and try to learn more about. Below I will list what is currently being experimented with with the newest addition being added at the top of the list.

## Saving, updating, loading and reworking. (0.8.1)

I've been reworking a lot of the earlier stuff already to make them generally better classes and have better features. After a week of working on the project and /only/ being as far as I am which is 3 NPCs, 2 test maps and some basic features, I can really start to appreciate why large scale projects such as video games take years from start to finish. I will keep working on this project in my spare time since it is by far the most in depth thing I've done in Java. I recently looked into JavaFX which seemingly would be much better than how I'm currently doing things (swing) but we're too far in to convert now! I'm committed!

## Yep, you will be able to save... and go anywhere. (0.8.0)

Saving the game is something I've wanted to do since deciding that building a game is the direction I'm taking this. I've finally started to tackle that problem and in doing so I've seen other flaws in the project and tackled those too. The biggest of those is map switching... before it wasn't really going to work at all realistically. I've now re-worked that to allow maps in any direction and inter map (so houses for example), accessed by their filename.

## Night time... and time in general. (0.7.0)

So I remembered that night time was a thing... so that's a thing in the game now and may actually be quite a core mechanic. More importantly, I've implemented time into the world. The default time in ticks that I've set is 36000, which is equivalent to 10 minutes at 60 ticks per second. I've come to realise the kind of direction that I'm moving in and that it's to incorporate as many different entities and mechanics, etc, as I can without making the game feel bloated. I have a couple of friends who have expressed interest in making maps, etc. So I want to make a platform that is rich with features for them to take advantage of.

## File paths, jars and production execution. (0.6.2)

Since the start of the project, I have been referencing files from their source locations and an oversight of mine is that this clearly doesn't work in production so now I've fixed that. Using input streams for files as well as what was there before. (I can't remember off the top of my head) However, this does mean that jar files can be executed now and everything will be as it should! (Also more cleaning, obviously)

## Lambda, further cleaning, further refactoring. (0.6.1)

Before I continue, I must mention that each of the previous update notes doesn't include everything that has been done, merely what I can remember at the time. However, this time I have just been doing some more refactoring where I can, various bits of code cleanup and important comments to try and keep things organised as I reach the point where I can't just glance and understand exactly what is going on. I've also changed Application.java from implementing Runnable and instead used a runnable lambda function.

## Bridges, paths, further map extension and readable code. (0.6.0)

Quite a lot has been added today. These things are: bridges, cows, paths, windows and entrances (can go inside building). I've changed banks to a lower case E (e) because it looks softer which in this case is better. I've added more colours, and tried to texture the grass in whichever ways I could. I've also extended the map size from 50x50 to 60x60 and have changed the font size/spacing to give the game a better look. A relatively important thing I have started also doing with my refactoring is making methods name that you can read and make logical sense. For example, instead of doing something like if(npc.getNpcType == 1), it is now if(npc.canSwim()) which is much better!

## Heavy refactoring, almost destroying everything. (0.5.3)

To make everything efficient and to keep code duplication to a minimum, I am constantly looking at how I can refactor the code, whether it be with inheritance or breaking larger methods into smaller ones. This time around I also wanted to add a new field to the tiles so that each individual tile held its own colour, allowing independent tiles to have new colours. While doing this and refactoring, I managed to mess something up and shit really did hit the fan. Thankfully I could see my history thanks to version controlling and was able to fix it without so much of a headache.

## Inventory, odd-maps and ducks. (0.5.0)

I haven't been able to do much today due to lectures and such but what I have done is the following. I've started to set up the inventory system to the point where it exists and you can add/remove/shuffle items around. There just aren't many items yet. I've added ducks and refined where sheep can stand. I've also added the ability to create maps which aren't squares... are at least they appear that way. I changed the encoding type of the map files so I can use utf-8 and I use a dark-grey coloured '▒' as a kind of marker for blank spaces. Finally, I've added a spreadsheet with all the characters and the information surrounding them for obvious reasons.

## Version controlling, increasing base size. (0.4.1)

In an effort to become more professional with my work, despite having this log, (not dated) I've properly set up Git on my IDE which apparently, I hadn't done before. In relation to the game which has no name currently, I've increased default map size to 50x50. In the future, I hope to have non-standard map sizes and shapes... in fact as I'm writing this and thinking about how I'd do that, I can just add some kind of character that represents blank and colour it to the background colour. Look out for that in 0.5.0!

## Making color, colour again. (0.4.0)

It's no secret that that base awt colours are a little lacking when it comes to public field colours such as Color.RED, for example. What better way to practice the use of public static final fields than to minify awt.color and add many, many more colours to save the need in a month to find obscure hex or rgb values for colours. (I don't currently have 'many, many more', but I will!)

## IO, cleaning, maps and serializing. (0.3.0)

At this stage, I've gone full into game development mode. I've completely re(designed) a set of classes to make everything run smoothly, following on from the last commit. Now I've implemented a map system which can load external maps using serialization and such, doing this had made the rendering a bit buggy but I figured out a fix while writing this entry which essentially involved duplicating the world map and using that as a tick/render base that everything else then renders over.
EDIT: I've now also added the ability to move from map to map. For example, move to the edge of a map and attempt to walk off and it will load the map next to it!

## Entities, inheritance and randomness. (0.2.0)

As I'm kind of developing this into a mini video game type thing, I thought that the playable character deserved some data of their own. Introducing entities -> players, npcs and items. Now everything that is on the map that isn't terrain will have some amount of information describing it, rather than just appearing as the letter 'H' for example. Nothing really new in terms of things I haven't seen before. Looked more into randoms for dynamic looking water. (It went really well!)

## KeyListeners and then some. (0.1.0)

I always wanted to be able to give some kind of input to a program. This time around I was able to achieve this and I wanted to press how far I could take that in the short amount of time I have been looking into it. From this and using techniques I have previously learnt I've started to create a Dwarf Fortress type game out of ascii characters. So far you can move around and I've already implemented objects you can't pass but I'll see where I can take this.

## TravisCI, how to actually implement.

[![Build Status](https://travis-ci.org/Galaxiosaurus/JAVA-RhythmMachine.svg?branch=master)](https://travis-ci.org/Galaxiosaurus/JAVA-RhythmMachine)

Note: To get travis working, you need to create a .travis.yml file in the project root, generate an ant build, (named build.xml for ease of use) edit the build.xml file and add <target name="test"></target> to appease Travis, Push a commit to trigger a Travis build and then upload the badge icon via markdown to the README.md file.

## Paint, canvas and sine waves.

I've been looking into how to properly make GUIs within Java but got side tracked into making a video game (which gave me a simple type of engine to work with for constant updates) and now how to simply make a GUI using 2D graphics and such. Taking it one step at a time or a few steps in this case. :)