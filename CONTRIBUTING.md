
:airplane: First off, thanks for taking the time to contribute!

:airplane: I'll try to keep this as simple as possible.

Steps for creating good issues or pull requests.

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

## Choose something to work on
The project backlog is kept in Github's issues tracker, please look there for something you can usefully tackle.
Please chat to me about what you intend to do before you start - I may be able to save you some time.
If you haven't contributed to a GitHub project before, this article may help you: https://akrabat.com/the-beginners-guide-to-contributing-to-a-github-project/

Soaring Coach consists of two parts:
* The front-end is Bootstrap based, kept in a separate Github repository and the development version is hosted using Github Pages
* The backend does all the analysis, is kept in this repository and the develop snapshot is hosted on Heroku.  It can be accessed at https://protected-bayou-34428.herokuapp.com/upload using a POST request that uploads an IGC file.  The analysis results are returned as a JSON sctructure.  Note that Heroku goes to sleep after 30 minutes, so the first time you submit a file it might take a minute to respond.

## Branching workflow
Soaring Coach uses a variant of GitFlow, which you can read about here: http://nvie.com/posts/a-successful-git-branching-model/

## Commit messages
Please write well formed commit messages.  
This article expands and is very helpful, as it also talks about why these things are important: https://chris.beams.io/posts/git-commit/

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
* IGC File Viewer: http://www.glidingweb.org/igcWebview/  The sources for this is on Github: https://github.com/GlidingWeb/IgcWebview2

## Links that might be useful
These are provided just in case they are helpful to you.

* MIT-licensed project on Github for a web-based IGC file analysis tool: https://github.com/alistairmgreen/jsigc. Very basic, but parsing & display logic may be useful. Live version: https://alistairmgreen.github.io/jsigc/
* FAI official spec - IGC Format for Waypoint Data 2000: http://www.fai.org/component/phocadownload/category/?download=3557:wpformat (I much prefer Ian Forster-Lewis treatment of the subject)
