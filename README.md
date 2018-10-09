# RPG_Scratchpad
Open Source test UI for doing various simulations and tests in RPG systems.

## ABOUT

Whether testing a particular situation or prototyping an entire campaign, players and GMs alike can often end up in the situation where they'd like to run through the motions of a game by themselves, while still having named conversations and actions as if it was playing out in a regular setting. While this kind of situation can still be done in popular online tools for running text based roleplaying games, a minimalist and generalized tool still holds a lot of advantages towards streamlining the process.

RPG_Scratchpad aims to provide just enough functionality/complexity to fit most game systems, while leaving the interface simple, easy to use, and as quick to use as possible. Self-tests tend to be a bit harder on focus and mental stamina, both from the lack of social feedback normally provided by a group as well as the need to pretend to play both sides of the table as both player and GM. Continued use and testing has tweaked the experience for the Scratchpad to make the UI as shortcut and hotkey friendly as possible as well as providing as many tools as possible to automate repetive actions. 

As far as code organization : most code is intended to follow proper documentation and best practices, giving explanation in file when unusual seeming choices have been made. 

## Setup

At this time, this is still a largely work in process project. 

### If you use Eclipse

The project contains a valid .classpath and .project

Use Eclipse's 'Import Projects'->Projects from git

### If you don't use Eclipse
To run it, check out the entire repo, with existing structure left intact.

The project is setup with maven. Follow normal procedures to build.

The main classes for ui are in target\classes\com\github\distanteye\rpg_scratchpad\ui
See runnable programs (below for the names)

## RUNNABLE PROGRAMS

1) RPG_ScratchPad : Only current program. Runs a windowed display (best viewed maximized and at high resolution). Will automatically create log files in the same folder run from. Supports saving and loading most settings to xml file (although will not save logs in that xml file)

## Design Choices

### Poi & Skills

While sometimes mentioned in file comments, it deserves special mention that most of the oddities in Poi/Skill class behaviors are UI driven. We want users to be able to redefine the public facing names of Skills, but keep the system meaning/mapping known and consistent, so there's a functional separation between the string that maps a skill and the label the skill may display as. Similarly, the save/load process as well as the frequent creation and deletion of different Pois during UI runs means that a degree of access safety and flexibility is needed for Skill lookups. This is why most Poi methods for Skills will autocreate Skill mappings rather than give "not found errors" when a search fails to find a mapped result. Because the current in-house UI the only conceivable user for these model objects, we can make these kinds of shortcuts that wouldn't be acceptable in projects that are larger or have more generalized usage needs.

### Reused Swing Extension Classes

Currently, RPG Scratchpad reuses a lot of UI elements also defined in the EP Utilities repository. Eventually, it'd be desirable to move those files out of both projects and into it's own library project, but that goal is always further and further out into the future since it would be best done with making further extensions and improvements to concept, making it into a large Swing add-on, and I'm still unsure I want to commit to doing that much work on an old and outdated UI framework. Time will tell.