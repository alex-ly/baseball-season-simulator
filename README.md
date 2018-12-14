# baseball-season-simulator
A simulation of a baseball season.

This program will simulate a baseball season with the number of teams the user entered.

All teams will play each other once during the season.

The highest power of 2 that is less than the number of teams that have the best record (ratio of wins to losses) will advance to the playoffs. 

For example, if there are 5 teams, the top 4 teams make the playoffs. If there are 4 teams, the top 2 teams make the playoffs.

In the case of ties, the tiebreaker will be run differental \[`number of runs scored - number of runs allowed`]

In the playoffs, the highest seeded team will face the lowest seeded team, while the 2nd highest seeded team faces the 2nd lowest seeded team and so on. The teams that win advances to the next round while the teams who lost are eliminated.

This process repeats until there is 1 team remaining where they are declared as the champions.

# Simulation mechanics

Each game is logged in the `gameLogs` folder where every at-bat is logged.

Each playoff game is logged in the `playoffLogs` folder.

To determine if batter gets a base hit, a random number is generated (25% chance of a hit).

Should the batter get a base hit, another random number is generated to determine what kind of hit (single, double, triple, home run).

```
Single - all runners advance 1 base. Runner on 3rd base scores a run. Batter occupies 1st base.

Double - all runners advance 2 bases. Runners on 2nd and 3rd base score. Batter occupies 2nd base.

Triple - all runners advance 3 bases. All runners score. Batter occupies 3rd base.

Home run - all runners score along with the batter.
```

If not, a random number is generated to determine what non-hit event occurs (walk, strikeout, popout, foulout, groundout, flyout).

All batter and team stats are stored in the `stats` folder.

The batter stats recorded are:
```
avg - number of hits divided by number of at-bats
hr - number of home runs hit
rbi - number of runs driven in by the batter
```

The team stats recorded are:
```
Wins
Losses
Ties
Run Differential
```

# Team files are stored in the "teams" folder
To add a team, make a `.txt` file with the filename of the next number after the highest team number in the folder.

# Format of the team files
Format should be

\[Team name]

\[Name of 1st player]

\[Name of 2nd player]

...

\[Name of 9th player]

# Running the simulator
Compile and run `Main.java`. The program should prompt for the number of teams playing in the simulation.

The number of teams entered cannot exceed the number of teams in the teams folder.

