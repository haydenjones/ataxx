# Ataxx

A java implementation of the strategy game with both Swing and JavaFX frontends.

## Philosophical Approach

>**Blackadder:** Baldrick, why are you dressed like that?
>
>**Baldrick:** Do you want the long answer or the short answer?
>
>**Blackadder:** Mm, the short answer.
>
>**Baldrick:** Whim.
>
>**Blackadder:** *Whim*. And the long answer?
>
>**Baldrick:** It was a whim, my lord.


## Download

Download the latest JAR file from the [Releases](../../releases) page and run with:
```bash
java -jar ataxx-0.1.jar
```

## Running from Source

### JavaFX Version (Recommended)
```bash
mvn clean compile
mvn javafx:run
```

### Swing Version
```bash
mvn clean compile exec:java -Dexec.mainClass="ca.jhayden.whim.ataxx.swing.AtaxxSwingLauncher"
```

### Prerequisites
- Java 21 or higher
- Maven

## Features

- Clean JavaFX interface with FXML
- 2-player and 4-player game support
- Visual game board with piece movement
- Score tracking for up to 4 players
- Game over detection
- AI opponents

## Controls

1. Click on your piece to select it (highlighted in green)
2. Click on an empty square to move/jump
3. Click the selected piece again to deselect

The game follows standard Ataxx rules where pieces can either:
- **Clone** to adjacent squares (piece duplicates)
- **Jump** up to 2 squares away (piece moves)

Adjacent enemy pieces are captured after each move.

## To do

* Declare a winner when the game ends.
* Enhance the UI with animations.