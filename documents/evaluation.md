# Preliminary evaluation


## Provide some analysis of the work you've done so far. Be sure to address the following issues:

### What works well? What are you particularly pleased with?

Thus far, we have basic implementations of our parser, intermediate representation, semantics, and Sublime Text plugin. We are particularly pleased with the sublime plugin which provides a clean and aesthetically pleasing interface for the users. We also particularly like the minimalist syntax we have concocted for our users.

### What could be improved? For example, how could the user's experience be better? How might your implementation be simpler or more cohesive?

We still have a decent amount that we need to improve in order to fulfil our original goals for this project. The parsing of dates is currently done with a regular expression that is not flexible and we need to figure out a better way of doing this. We also have not implemented recurrences or dependencies yet. We also have not implemented some features of the sublime plugin that we ultimately want such as replacing ‘tomorrow’ as typed by the user with the actual date. In addition, we have not implemented a method of completing an event. We need to come up with a way of handling this.

We also learned a lot about what we need to improve when we did user testing in class. The input for the date and time of a task is very bulky and unintuitive as we are using a standard that most people are not familiar with typing. This is an issue with our parser that we need to fix. The hashtags are also not clear to first time users and they do not like that they need to be typed manually. We hope to have the sublime plugin automatically generate those as well. We also had issues when the user tried to put an end date before the start date of the task. This did not result in an error even though it should. The times that are assigned to the task (if not input by the user) seem to be random - either seven or eight AM. The language for the tasks is somewhat limited, i.e. you cannot include some punctuation. Some other minor problems are that the language also allows you to have the same task twice on your task list, tasks require a due date, dates cannot be entered with slashes, the user has to type the year, there is no way to say when a task has been completed, and how the language will display overdue tasks. 

One of the most significant issues that we faced in user testing was the readability of the language. The users did not like the succinct nature of the language because it was hard to understand exactly what information was being conveyed. We had considered a more clear and verbose syntax before deciding that the succinct version would be more useful. However, now we are reconsidering which type of syntax would be best. One option would be to give the user the option between the two ways of displaying tasks. This would give the language much more flexibility. However, this would also add a significant amount of complexity to the front-end of our language. We would like to avoid this if possible. In addition, we feel like that the slightly steeper learning curve of our reduced syntax adds a significant benefit in the long term.

### Re-visit your evaluation plan from the beginning of the project. Which tools have you used to evaluate the quality of your design? What have you learned from these evaluations? Have you made any significant changes as a result of these tools, the critiques, or user tests?

The main tools that we have used to evaluate the quality of our language include the in-class user testing and our own personal usage of the language. We have learned a lot from the user testing - this is described in the section above. We have plans to address the questions and issues from this testing, but we have not yet figured out the specifics in each case. 

As we begin to become closer to feature complete, we will also rely more on unit tests to judge the correctness of our language. Hopefully, we will be able to come up with a number of pathological cases that show off and test most of the interesting parts of our language.

### Where did you run into trouble and why? For example, did you come up with some syntax that you found difficult to implement, given your host language choice? Did you want to support multiple features, but you had trouble getting them to play well together?

At the beginning of our project, we ran into trouble with our initial choice of python as a host language. The library that allowed us to use case classes resulted in code that was extremely verbose and hard to understand. We fixed this by switching to Scala. The code is much shorter and cleaner. Now, one of the main problems that we are having is in regards to the parsing of dates and times in our language. 

We are also not completely satisfied with how our language interacts with the user. Currently, we only run our interpreter when the user’s todo file has changed. And, when we run it, we recompute everything, not just the changed portions. We would like to come up with a method of only having to compute changes. This would also likely make it more straightforward to implement done tasks.

### What's left to accomplish before the end of the project?

We need to implement dependencies and recurrences since this is a main feature of our language. This will be our main focus, but we also want to address some of the issues that came up during user testing such as whether or not we want to change the syntax. We hope to fix most of the minor issues that our users encountered while using our language as well if we finish the dependencies and recurrences features with time remaining. 

Dealing with completed tasks will also be important. We were asked this question during user testing but we have not discussed as a group how we would like to do this. Some aspects of this that we need to consider how the sublime plugin will display a completed task, how a user will tell the language that a task will be completed, and if we want the user to be able to look at a list of tasks that have been completed.

### If you worked as a pair, describe how you have divided your labor and whether that division has worked well.

As we write our weekly work logs individually, it should be fairly clear from those how our labor division worked. We roughly divided implementation into front-end, parser, and semantics. Almost all other tasks we completed together. The division has worked well for our purposes.
