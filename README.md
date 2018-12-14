# baseball-season-simulator
A simulation of a baseball season.

This program will simulate a baseball season with the number of teams the user entered.

All teams will play each other once during the season.

The highest power of 2 that is less than the number of teams that have the best record (ratio of wins to losses) will advance to the playoffs. 

For example, if there are 5 teams, the top 4 teams make the playoffs. If there are 4 teams, the top 2 teams make the playoffs.

In the case of ties, the tiebreaker will be run differental `number of runs scored - number of runs allowed`

In the playoffs, the highest seeded team will face the lowest seeded team, while the 2nd highest seeded team faces the 2nd lowest seeded team and so on. The teams that win advances to the next round while the teams who lost are eliminated.

This process repeats until there is 1 team remaining where they are declared as the champions.



# Team files are stored in the "teams" folder
To add a team, make a .txt file with the filename of the next number after the highest team number in the folder.

# Format of the team files
Format should be

\[Team name]

\[Name of 1st player]

\[Name of 2nd player]

...

\[Name of 9th player]

# Running the simulator
Compile and run Main.java. The program should prompt for the number of teams playing in the simulation.

The number of teams entered cannot exceed the number of teams in the teams folder.

