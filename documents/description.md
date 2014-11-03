# Project description and plan

## Motivation

The project is useful because project and task management are integral to the
success of both students and professionals. Project management is interesting
to us as a group because we have not yet found a tool that solves project
management challenges (such as team scheduling) in a satisfactory way. We
both find the prospect of solving this problem very motivating. Christine
would like to pursue project management in industry (making this domain
extraordinarily relevant), and Michael has a near-obsessive fascination with
project and task management software.

While there are a large number of problems in project and task management,
we have decided to focus on a few specific challenges in order to come up
with a more targeted solution (and to comply with the “small bite, long chew”
philosophy of this course). In particular, we have chosen to focus on the
challenge of dependency modeling in project management. We think that this is
a small enough area for us to come up with a great DSL, but a large enough area
for a DSL to be useful. The area is also large enough that we would be able to
expand the functionality of our DSL without leaving the domain if we find that
we have time to do so.

We think that a DSL is an appropriate solution to the challenge of dependency
management because the specification of project dependencies is currently a
very involved and verbose process with existing tools. In other words, many
current tools either limit the sorts of dependencies that can be expressed
or make the process so tedious that specifying dependencies isn’t worth the
effort. We hope that a simpler and more intuitive syntax will make dependency
specification a useful endeavor for project managers.


## Language domain

The domain that this language addresses is that of project and task management,
and more specifically, of task dependency modelling. This domain is useful in
all business models in order to ensure the efficient completion of projects
within a company. It is also useful for group projects in academic situations
and even other types of domestic projects (such as cooking) that might involved
complicated and important dependencies. Both project managers and team members
would benefit from our language because it will enable teams to produce easily
understandable documentation of required tasks and their dependencies on one
another. Individuals working on complex projects would also benefit from this
language.

While we can’t say for sure, we are not aware of (and have been unable to find)
any other DSLs in the domain of dependency specification for project
management. There are certainly other DSLs that are at least tangentially
related to ours, though. For example, todo.txt is a DSL for specifying todo
lists in plain text. The key benefit of todo.txt is that a program (in this
case, a todo list) is clear enough that it can be read in plain text by a
human, but also structured enough that it can be run to produce a different
user experience. Todo.txt will likely influence our language design heavily,
as we envision creating a syntax that is extraordinarily human-readable in
addition to being easy to program. Another language that is somewhat related
to our DSL is GNU Make. Like our DSL, GNU Make is concerned with the idea of
dependencies, namely dependencies that must be resolved to construct
executables for programs. The key feature of GNU Make that we find compelling
is that even though it operates upon a DAG of dependencies, that DAG is not
explicitly provided by the programmer. Rather, the programmer lists out the
dependencies for each node, one at a time. Perhaps most importantly, the
syntax of GNU Make has given us the insight that a dependency graph need not
be explicitly specified as such. Instead, we can think about how the
dependencies can be most easily specified by the user. It may be cognitively
simpler, for example, to specify dependencies task by task instead of trying
to specify the entire dependency graph at once.


## Language design

A program in this language would be a description of a project as defined by
the tasks and their dependencies within the project. When a program in our
language runs, the tasks and their dependencies are compared and evaluated so
that a useful output can be provided to the user. The inputs will be the tasks
for the project and some information about these tasks (we aren’t yet sure if
the information should be the dependencies directly or if the language should
be able to compute the dependencies from a different set of information such
as completion date or resources required). As output, we would like to provide
a graphic depiction of the dependency graph and additional useful information
such as, for example, which of the currently unblocked tasks would unblock the
most tasks if completed. While we haven’t worked out the details of our
language yet, we imagine that we will represent the dependency graph as a
collection of nodes (representing the tasks) with pointers between them
(representing the dependencies).

There are several sorts of issues that might be encountered with a program in
our domain. If we decide to infer dependencies between tasks, there may be
errors concerning the program’s interpretation of relationships between tasks
that would be used to calculate those dependencies. In order to prevent errors
like this we have to make sure that the way in which the inputs are formatted
and the information that is required by the users must be clearly articulated
so that the language can parse the inputs and produce an accurate and useful
dependency model. It may also be possible that a user specifies a dependency
between tasks that are not otherwise defined, which should likely be caught
as either a compile-time or runtime error. Furthermore, it is possible that
a user could define cyclical dependencies, which would create a sort of
deadlock. We cannot assume cyclical dependencies to be an error, though, as
it’s feasible that such a problematic situation truly exists. Thus, cyclical
dependencies should be handled at runtime. In all of these cases, there are
ways of defining the syntax that could prevent the errors from occurring,
and we will keep this in mind when designing our syntax.


## Example computations

What follows are a few examples of computations we would hope for our language
to support. Because we have not yet decided on a syntax (or even the level of
specificity that will be required of the input), these examples use different
syntax.

### Example 1 - Constructing Widgets
```
Starting Parts:
Widget A (x2)
Widget B (x2)
Widget C (x2)

Widget D:
Screw Widget B into Widget C

Widget E:
Nail Widget A into Widget D

Widget F:
Stack Widget E on top of Widget E
```

The output is a dependency graph highlighting the order in which the various
steps must be completed, similar to the following:
```
A------\
        \
B--\     E-\
    D---/   \
C--/         \
              F
A------\     /
        \   /
B--\     E-/
    D---/   
C--/

```

### Example 2 - Founding Fathers Report
```
(1) Get Ben Franklin book from library

(2) Get George Washington book from library

(3) Conduct analysis of Ben Franklin
Depends on: (1)

(4) Conduct analysis of George Washington
Depends on: (4)

(5) Compare and contrast awesomeness of Ben Franklin and George Washington
Depends on (3), (4)

(6) Compile Final Report
Depends on: (5)
```

The output is a dependency graph highlighting the order in which the various
steps must be completed, similar to the following:
```
(1)---(3)---\
            (5)---(6)
(2)---(4)---/
```
