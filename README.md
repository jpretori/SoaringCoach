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

## Metrics
Again - these are not all implemented yet, the intent is to give some idea of what metrics could be useful in the app.
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


## Some useful links
* Ian Forster-Lewis introduction to the IGC format: http://carrier.csi.cam.ac.uk/forsterlewis/soaring/igc_file_format/
* FAI official spec - IGC Format for Waypoint Data 2000: http://www.fai.org/component/phocadownload/category/?download=3557:wpformat
* Ian Forster-Lewis' dev guide to the IGC format: http://carrier.csi.cam.ac.uk/forsterlewis/soaring/igc_file_format/igc_format_2008.html
* MIT-licensed project on Github for a web-based IGC file analysis tool: https://github.com/alistairmgreen/jsigc. Very basic, but parsing & display logic may be useful. Live version: https://alistairmgreen.github.io/jsigc/
* Visualising and hand-drawing GPS traces (for testing) at http://www.gpsvisualizer.com/draw/
* Math to calculate bearings, great-circle distances, etc: http://www.movable-type.co.uk/scripts/latlong.html
* Another IGC File Viewer: http://www.glidingweb.org/igcWebview/

## Contributing
The project backlog is kept in Github issues, please look there for something you can usefully tackle.
If you want, feel free to chat to me about what you intend to do before you start - I may be able to save you some time.
If you haven't contributed to a GitHub project before, this article may help you: https://akrabat.com/the-beginners-guide-to-contributing-to-a-github-project/
