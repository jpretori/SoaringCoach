# SoaringCoach
Soaring Coach aims to help pilots improve their soaring performance by intelligently analysing IGC files.

## Vision
An app that analyses IGC files and extracts key soaring performance metrics, allowing the pilot to learn maximally from each flight as well as from trends emerging in the history of flights.

## Features
These are not all implemented yet, the list is intended to give you a general sense of the intent.
* History graphs or other visual representation for each metric
* Parse IGC file to extract various metrics
* Ability to view e.g. per-thermal stats to get e.g. BTT average when turning left vs. when turning right
* Allocate some kind of "personal soaring performance" score for a flight, based on the metrics.
* Architect the code such that it is reasonably easy to add and remove code for specific metrics. Through use of the app, we will discover new things to measure and that some things matter less than we thought.
* Allow pilot profiles. One use case is a pilot who flies in real life, but also flies using Condor soaring simulator - since the two situations are quite different, it will be useful to be able to separate them out.

##Getting a build & getting started
* Versioned builds can be downloaded from the releases tab in Github.
* Run using java -jar SoaringCoach-x.y.z.jar.  This starts up the embedded Tomcat, and will listen on port 8080.
* Send it an IGC file in a POST request, and it should respond with a JSON structure with the results of the analysis.

## Metrics
Again - these are not all implemented yet, the intent is to give some idea of what metrics could be useful.
* Bottom-to-top climb rate (BTT) for each thermal
* BTT average across the task (TTA, or Total Task Average)
* Visually contrast BTT for each thermal against TTA, on a timeline. Highlight best vs. worst BTT for the flight.
* Course deviation as a percentage: Track Distance divided by Task Distance
* Time taken to center each thermal. Again, there is a number for each thermal and thus an average for the flight. Which can be plotted and contrasted against each other on a timeline.
* Time spent in sub-standard lift at the top of each thermal
* Thermal turn duration (each thermal will produce an average - so this can go on a timeline again)
* Thermal turn size in meters (may be less relevant, in the cockpit it's a LOT easier to judge circle size by counting the seconds taken to go through 360 deg)
* % of left-handed thermals vs. right-handed thermals. Especially the trend over time.
* Amount of time spent in observation zones. Less is better.
* Some stats about the final glide - distance, duration, speed, consistency of speed, glide angle, last climb rate (BTT), some measure of how good a call was made to leave for final glide (altitude vs. speed-to-fly vs. BTT). Which of these are vanity metrics though?
* After compensating for wind drift - how round are the circles you fly when thermalling? Contrasted between L-hand and R-hand thermals?

## Contributing
Please see CONTRIBUTING.md in the project root.

### Deploying new backend versions
* Make sure "master" branch is in deployable state (i.e. make sure all the tests pass)
* Log in to Heroku on the web
* Go to "Deploy"
* Hit "Manual Deploy" - and wait a few seconds for build and deploy to finish

### Deploying new front-end versions
* Just push code to the UI repo and wait for Github pages to update.
