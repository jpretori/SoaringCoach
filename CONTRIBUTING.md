:smile: First off, thanks for taking the time to contribute!  I'm glad to see you here. :airplane:

:smile: I'll try to keep this as simple and logical as possible. :airplane:

# Report a problem
If you just want to report a bug or suggest a feature, please submit an issue: https://github.com/jpretori/SoaringCoach/issues/new.

Please be as clear as possible.  If you can, try to describe it from a user's point of view, and in stepwise point form, e.g.
1. Upload attached IGC file
2. Click foo
3. Expand concertina bar
4. Reticulate splines
5. Look at time spent looping: I expected X, saw Y

(and then remember to attach the IGC file!)

# Help with development
If you'd like to help develop Soaring Coach, please take in the guidelines below.  Right at the end there are a bunch of links to resources that have been very helpful in developing Soaring Coach this far.

## Project Components
Soaring Coach consists of two parts.

### Front-end
The front-end is Bootstrap based, kept in a separate Github repository (https://github.com/jpretori/SoaringCoach-UI-Bootstrap) and the development version is hosted using Github Pages (https://jpretori.github.io/SoaringCoach-UI-Bootstrap/pages/index.html)

### Analysis Backend
The backend does all the analysis, is kept in this repository and the develop snapshot is hosted on Heroku.  It can be accessed at https://protected-bayou-34428.herokuapp.com/upload using a POST request that uploads an IGC file.  The analysis results are returned as a JSON sctructure.  Note that Heroku goes to sleep after 30 minutes, so the first time you submit a file it might take a minute to respond.

## Choose something to work on
The project backlog is kept in Github's issues tracker, please look there for something you can usefully tackle.
Please chat to me about what you intend to do before you start - I may be able to save you some time.
If you haven't contributed to a GitHub project before, this article may help you: https://akrabat.com/the-beginners-guide-to-contributing-to-a-github-project/

## Set up your development environment
Soaring Coach uses Gradle Wrapper to build, so you should be able to create a build by simply running `./gradlew` or `gradle.bat` from the source root.

Before starting development on your chosen feature / bug,set things up so you can run it all locally.  The steps to achieve this are:
- Get both repositories using Git
- Install Java if you don't have it already
- **Build the backend.**  In the `SoaringCoach` folder, run `./gradlew bootRepackage`
- **Run the server.**  Change directory to `build/libs` - then run `java -Dserver.port=9876 -jar SoaringCoach-z.y.z.jar`
- **Setup UI to connect to your local server.**  In the `SoaringCoach-UI-Bootstrap` folder, edit `dist/js/soaringcoach.js`, make sure the URL is set to `http://localhost:9876/upload`
- **Start the UI.**  Still in the `SoaringCoach-UI-Bootstrap` folder, open `pages/index.html` in your browser.

Now you should be able to upload an IGC file to your locally running analysis service, and get the results nearly instantaneously - with your latest code changes in play.


## Branching workflow
Soaring Coach uses GitFlow, which you can read about here: http://nvie.com/posts/a-successful-git-branching-model/

## Testing
Soaring Coach uses a combination of manual exploratory, that-looks-about-right type testing, and JUnit tests. Please make sure that there are unit tests to prove that important parts of the code works as part of your commit.  

You may have to create an IGC file for your testcase, either by flying the track you need in Condor (http://www.condorsoaring.com/), or by hacking it together from the existing test resources.  You could also go pin dots on a map using GPSViewer, but that is inaccurate, time consuming and boring.  
You might even want to use an IGC file from a real flight if such is useful - though it will probably be helpful to trim away most of the flight to get just the part you need for the test you're writing.

## Commit messages
Please write well formed commit messages.  

This article provides very helpful detail explanations, and most importantly it addresses the reasons why these things are important: https://chris.beams.io/posts/git-commit/ (essentially, writing good commits make it easier to work on the project in future).

In short, the minimum expected are:
* Capitalized, short (50 chars or less) summary - and avoid trailing punctuation
* Write the subject line in the imperative (i.e. complete the sentence: `If applied, this commit will...`)
* Put a blank line after the commit summary line
* Unless it's blindingly obvious, add an explanatory paragraph(s) to detail the WHAT and WHY of your change.  Wrap this at 72 characters.
* Do not combine fixes for various issues in one commit: instead, break them out and make one focused commit per issue that you're fixing
* Mention the issue number your commit addresses in the commit message somewhere, so Github can link it up

## Links that have proven useful
These resources have been extensively helpful during early development.

* Ian Forster-Lewis introduction to the IGC file format: http://carrier.csi.cam.ac.uk/forsterlewis/soaring/igc_file_format/
* Ian Forster-Lewis' dev guide to the IGC file format: http://carrier.csi.cam.ac.uk/forsterlewis/soaring/igc_file_format/igc_format_2008.html
* Visualising and hand-drawing GPS traces (for testing) at http://www.gpsvisualizer.com/draw/
* Math to calculate bearings, great-circle distances, etc: http://www.movable-type.co.uk/scripts/latlong.html.  This is also very helpful to check conversions between different lat/long encoding schemes, which can save hours when debugging certain issues.
* IGC File Viewer: http://www.glidingweb.org/igcWebview/  The sources for this is on Github: https://github.com/GlidingWeb/IgcWebview2.  It may be an option to pull this in as a display component, to show flight tracks and barograph traces.

## Links that might be useful
These are provided just in case they are helpful to you.

* MIT-licensed project on Github for a web-based IGC file analysis tool: https://github.com/alistairmgreen/jsigc. Very basic, but parsing & display logic may be useful. Live version: https://alistairmgreen.github.io/jsigc/
* FAI official spec - IGC Format for Waypoint Data 2000: http://www.fai.org/component/phocadownload/category/?download=3557:wpformat (I much prefer Ian Forster-Lewis treatment of the subject)
