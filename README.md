# JAVA-RhythmMachine

As the description suggests, this repository is purely for my own experimental code. Things that I'm playing around with and try to learn more about. Below I will list what is currently being experimented with with the newest addition being added at the bottom of the list.

## Paint, canvas and sine waves.

I've been looking into how to properly make GUIs within Java but got side tracked into making a video game (which gave me a simple type of engine to work with for constant updates) and now how to simply make a GUI using 2D graphics and such. Taking it one step at a time or a few steps in this case. :)

## TravisCI, how to actually implement.

[![Build Status](https://travis-ci.org/Galaxiosaurus/JAVA-RhythmMachine.svg?branch=master)](https://travis-ci.org/Galaxiosaurus/JAVA-RhythmMachine)

Note: To get travis working, you need to create a .travis.yml file in the project root, generate an ant build, (named build.xml for ease of use) edit the build.xml file and add <target name="test"></target> to appease Travis, Push a commit to trigger a Travis build and then upload the badge icon via markdown to the README.md file.

## KeyListeners and then some.

I always wanted to be able to give some kind of input to a program. This time around I was able to achieve this and I wanted to press how far I could take that in the short amount of time I have been looking into it. From this and using techniques I have previously learnt I've started to create a Dwarf Fortress type game out of ascii characters. So far you can move around and I've already implemented objects you can't pass but I'll see where I can take this.

## Entities, inheritance and randomness.

As I'm kind of developing this into a mini video game type thing, I thought that the playable character deserved some data of their own. Introducing entities -> players, npcs and items. Now everything that is on the map that isn't terrain will have some amount of information describing it, rather than just appearing as the letter 'H' for example. Nothing really new in terms of things I haven't seen before. Looked more into randoms for dynamic looking water. (It went really well!)

## IO, cleaning, maps and serializing.

At this stage, I've gone full into game development mode. I've completely re(designed) a set of classes to make everything run smoothly, following on from the last commit. Now I've implemented a map system which can load external maps using serialization and such, doing this had made the rendering a bit buggy but I figured out a fix while writing this entry which essentially involved duplicating the world map and using that as a tick/render base that everything else then renders over.
EDIT: I've now also added the ability to move from map to map. For example, move to the edge of a map and attempt to walk off and it will load the map next to it!

## Making color, colour again.

It's no secret that that base awt colours are a little lacking when it comes to public field colours such as Color.RED, for example. What better way to practice the use of public static final fields than to minify awt.color and add many, many more colours to save the need in a month to find obscure hex or rgb values for colours. (I don't currently have 'many, many more', but I will!)