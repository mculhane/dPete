# Language design and implementation overview

## Language design

* How does a user write programs in your language (e.g., do they type in commands, use a visual/graphical tool, speak, etc.)?
  * With our language, the user writes programs (which consist of specifications of tasks and their dependencies) in plain text. While this is a bit of a stretch goal for the project, we hope to provide a Sublime Text plugin that will aid in the process of writing programs. Such a plugin could assist with generating hashes for new tasks. Furthermore, it could provide syntax highlighting and formatting assistance to the user. Finally, a plugin could help the user write programs by allowing us to support relative dates like “tomorrow”. (We cannot allow for relative dates in our syntax, though, as the meaning of “tomorrow” is dependent on the time at which the task was created. Thus, such dates relative to “now” must be resolved to unambiguous dates at exactly the time the user specifies those values.)

* What is the basic computation that your language performs (i.e., what is the computational model)?
  * Our core goal is to be able to compute the next n things that a user should be working on, and to deemphasize the things that a user cannot currently be working on. Once we load a program into our intermediate representation, though, we can imagine that be would be able to provide additional functionality to the user (such as, for example, which tasks are due on a particular date or which tasks have been completed in the past week). We will leave this additional functionality as a stretch goal, though. These computations will be triggered via our Sublime Text plugin, but we hope to create a clear enough API that other interfaces, like a command line interface or a web interface, could also surface the computation to the user. (For this project, though, we will only develop the Sublime Text plugin.)

* What are the basic data structures in your DSL, if any? How does a the user create and manipulate data?
  * We will be creating a dependency graph that holds all the information about the tasks. The graph will be implemented, effectively, as an adjacency list. In particular, each task will have an associated hash. Then, other tasks will be referenced by their hashes, and a task can be looked up in a dictionary using its hash. In this way, we can easily follow a chain of dependencies without needing to create and maintain a complicated system of objects and pointers.
What are the basic control structures in your DSL, if any? How does the user specify or manipulate control flow?
There is no manipulable control flow. A user enters statements that all have the same basic class. The only control flow occurs when a user desires dependencies or recurrences. In the case of dependencies, tasks’ start and due dates will depend on computations in the tasks that they depend on. Furthermore, recurrences provide a sort of looping, as recurrences specify the interval of repetition for a task. (Interestingly enough, our language only includes recurrence interval specification, but not anything regarding the number of recurrences, so all recurrences will recur infinitely. Because of how a program is run, though, this will not cause any sort of infinite looping behavior.)

* What kind(s) of input does a program in your DSL require? What kind(s) of output does a program produce?
  * Our program requires a number of lines of text as input and produces lines of text as output. If one was to give our program an arbitrary set of strings, they would likely be printed out just as they had been input. Only when keywords appear does our language start to do things to the input. More generally, though, the input to our language is a series of tasks specified with their dependencies and recurrences (in plain text), and the output is a plain text list of the 10 tasks due next (and perhaps other useful views of the data, as time allows).

* Error handling: How can programs go wrong, and how does your language communicate those errors to the user?
  * We are trying to minimize any potential error. Misunderstanding, which occurs when the program’s behavior and the user’s intent are different, is our largest concern. A user can tolerate an error---they would be notified that something was wrong immediately---but if the system misunderstands the user then we could not produce a reminder when the user is expecting one, losing their trust forever. 
  * We will try to make our syntax as unambiguous as possible in order to minimize misunderstandings. This will require some rigidities of expression. We have already eliminated a few language features which we thought would introduce unnecessary confusion. Furthermore, when designing our semantics, we have had this issue in mind. We initially thought about how we should interpret syntactically correct expressions with unclear meaning (such as a task that starts on Monday and is due on Friday, yet repeats every Tuesday). After spending some time constructing ridiculous potential interpretations, we realized that whatever decision we made would not be obvious to the user. In this particular case, we have decided that a recurrence interval for a task must be larger than the difference between the start and due dates for that task. So, if you specify a task that starts and is due in 6 days, then the recurrence interval has to be weekly or monthly rather than hourly or daily. In general, though, we decided that we will construct our semantics such that we will recognize these unclear cases and surface errors about them to the user instead of making decisions without their knowledge and potentially introducing confusion.

* What tool support (e.g., error-checking, development environments) does your project provide?
  * Our tool will hopefully provide both error checking and a development environment for the user. Error checking will be performed during runtime at both the syntactic level and the semantic level. (We do not intend to have any sort of live error checking or linting, though.) Furthermore, though our program is run via a Sublime Text plugin, which surfaces the results of computations, we hope that this plugin will also be a development environment of sorts for the user. The development environment would take care of some of the more tedious parts of writing in our language like choosing hashes for tasks. Furthermore, a development environment would allow us to support dates relative to the current time (like “tomorrow”). (Without such a development environment, such dates would clearly change based on when the file is viewed, so we would not be able to support them.) These features of our Sublime Text plugin are not core to our language, however, so we will leave them as a stretch goal for now.

* Are there any other DSLs for this domain? If so, what are they, and how does your language compare to these other languages?
  * The most notable DSL for this domain is todo.txt, which is a language that allows for plain text specification of tasks. While todo.txt has some basic support for projects and contexts, it has no support for dates and dependencies. Our language was initially going to be built on top of todo.txt, but we have since decided to build a separate language. In any case, the core ideas of todo.txt (a human readable and editable plain text task list specified in a straightforward way) are ideas that we have tried to preserve in our language.
  * In addition to TODO.txt, which is a more clear-cut language, we have also looked at several examples of task management software to garner an understanding of how they use dates and dependencies. For example, Google Tasks provides a simple way to assign tasks to dates, but it does not support dependencies or recurring tasks. OmniFocus provides a model for dependencies that is based on a hierarchical listing of tasks that breaks sublists into parallel components (which can be completed simultaneously) and serial components (where each task is dependent on the previous). While this model of dependencies is certainly more robust than most solutions, it still does not allow for the user to easily represent more complicated (yet common) dependencies (such as a task that cannot start until two others have been completed). Finally, Taskwarrior is a surprisingly full-featured task management system for the command line, which has robust support for recurrences. It handles dependencies as well by allowing the user to specify a list of tasks which a task depends on, which is very similar to the type of behavior we would like to support. In a sense, then, this analysis has demonstrated to us that we would like to support the dependencies and recurrences with the robustness of Taskwarrior, but with the concise and intuitive syntax of todo.txt.


## Language implementation

* Your choice of an internal vs. external implementation and how and why you made that choice.
  * We chose to implement an external language in order to allow for more freedom with regards to language features. In particular, we wanted to support a syntax that is close to how someone would likely specify their todo list in a text file, and we felt that an internal DSL would not allow easily for that level of expression. From the implementation perspective, we are envisioning a very simple and straightforward syntax, which would probably be just as easy (or even easier) to parse than it would be to coerce the host language into natively supporting our syntax.
Your choice of a host language and how and why you made that choice.
  * We chose Python as a host language for a few reasons. The primary reason we chose Python is that we would like to build a plugin for Sublime Text. Because Sublime Text is written in Python, we thought it would be easier to use Python than it would be to switch between languages. Our choice of Python was also influenced by the fact that Python (or its associated libraries) has all of the functionality we would need, including some robust parsing tools. Finally, we felt that Python would be a good choice of language because we all have some familiarity with it.

* Any significant syntax design decisions you've made and the reasons for those decisions.
  * We originally started our work thinking that we would build upon the syntax of todo.txt, so we assumed todo.txt’s syntax and philosophies at the outset. Since then, though, we have made the decision to deviate from todo.txt’s rule of keeping each task to one line. We did this because we felt that trying to specify recurrences and dependencies with a task in just one line would be rather difficult to read. Our new approach places each piece of data about a task on its own indented line, which creates a much more clear structure. It also allows for the language to be more easily extended to add new features (such as notes, for example). This structure is also extraordinarily easy to parse. Furthermore, for a very simple task with no dependencies or recurrences, we expect that our syntax will still allow the task to be specified in just one line.

* An overview of the architecture of your system.
  * The system will be designed to be a self contained program that accepts an input of a file containing the task list and some parameters. This program will be utilized in our sample by a Sublime Text plugin that will allow us to make live edits to the task list and see the results in real time. Although we would like a user to be able to hand edit the tasks file, we feel that it will be desirable for the user to use an intermediary such as our Sublime Text plugin. Otherwise, we would have to worry about what relative time expressions mean when we don’t get to evaluate them immediately.
  * When the Sublime Text plugin loads, it will parse the text of the program into the intermediate representation, which loosely consists of a collection of task objects. Then, when a computation (such as a list of the 10 soonest due tasks) is requested via the plugin, the result is computed by examining each task and computing a solution. Dependencies are easily handled by traversing the dependency chain. (For example, if task 2’s due date depends on task 1’s due date, task 2’s due date is calculated by looking at task 1’s due date.) Recurrences are handled using a generator pattern, in which each task in the recurrence is generated as required.
  * We hope that this computational model will be relatively straightforward conceptually, but it is unlikely to be the most efficient. This is not a concern for us, though, as it is unlikely that a user would ever have more than a couple hundred tasks. Furthermore, we would expect that most of those tasks would not be recurring, and we would expect that most dependency chains would be relatively short on average. Thus, we can confidently deprioritize computational efficiency (though we certainly won’t ignore it altogether).


### Preliminary Intermediate Representation
The following is a preliminary version of our intermediate representation. It is clearly not in EBNF, but it should get the point across.

```
Object Task {
	Expr Start
	Expr End
	Blob Metadata
	Recur Recurrence
	Hash Name
}

Parsing of expression:

Expr := before(Expr, Offset) | after(Expr, Offset) | start(Hash) 
| due(Hash) | done(Hash) | TimeLiteral | next(Hash)

TimeLiteral := MWF/every 15th of month/3rd Thurs/something similar 
| relative expression | time stamp

Recurrence := every(expr)

Offset := quantity, unit
```

