![logo](https://cloud.githubusercontent.com/assets/3872922/5234323/f236f8f4-7777-11e4-8e00-8461dde11a38.JPG)

# dPete


Task management DSL with a focus on convenient specification of recurrences and dependencies.

The project is currently in the initial prototyping phase.

The project is named dPete (a combination of the word "dependency" and the second syllable of "repeat"---get it?). The "d" is silent.


# Installing dPete

For dPete to work, you'll need Java all set up and ready to go. If you don't have Java, you'll need to install it using the normal means. You'll also need Sublime Text. (We've tested things on Sublime Text 3.)

1. Download `Pete.sublime-package` from this repository.
2. Navigate to your Sublime `Installed Packages` directory. On Mac, you can click (in Sublime) `Sublime Text > Preferences > Browse Packages`, which will take you to the `Packages` directory. The `Installed Packages` directory is at the same level as `Packages`.
3. Copy `Pete.sublime-package` into `Installed Packages`.
4. Extract `Pete.sublime-package` into a directory named `Pete` in the `Packages` directory. (`Packages` is at the same level as `Installed Packages`.)
  * Despite its unfamiliar extension, `Pete.sublime-package` is just a zip file. You may need to do an "open-with" to force your unarchiver to open it (or just use your favorite command-line zip extractor).
5. Restart Sublime Text.

If everything went correctly, then dPete has been installed correctly!


# Opening dPete

1. Open Sublime Text.
2. Click `Project > dPete`. This should open up the dPete interface.


# Using dPete

The dPete interface is a split-screen interface. The top half of the screen is where your tasks will go, and the bottom half of the screen displays output. To run one of the sample programs, just copy and paste the program into the top window and save the file. The results (active tasks sorted by due date) should appear in the bottom half of the window.

To add a standard task (without a dependency), type CTRL-A (on Mac) or ALT-A (on Linux). A snippet should appear in the window with the basic syntax. To fill it in, replace the highlighted bit and use the tab key to highlight the next part of the snippet. Do this until the line is correctly filled out, then save the file to refresh the bottom panel. To add a task with a dependency, type CTRL-SHIFT-A (on Mac) or ALT-SHIFT-A (on Linux) and follow the same process.

Feel free to save and close dPete at any time. The top panel (with your task list) will persist across sessions (presuming you save it).
