Michael's Critique

First off, adding recurrences seems pretty ambitious.  The parsing you added
doesn't seem too bad, but the semantics are likely to be difficult to pin down.

I think you need to either nail down exactly what behaviour you want out of
recurrences, and that the behaviour has to be more narrow than your current
conception of it.

The options I see you having for recurrence timing are:
* recurrences go from the start date
* recurrences go from the due date
* recurrences go from the date the first instance is completed

In the first and second cases, you might have to just have sane defaults for
tasks without start or end dates.  In the last, maybe you could just initially
generate recurring tasks from either the start or end, and update them all when
one is completed?

For instance, if I say something like 

`#XXXXXX Buy Milk @ 2014-12-02 - 2014-12-04 % every 1 weeks`

then my todo list would have "Buy Milk" on it on the 2nd, 9th, 16th, 23rd,
30th, and so on.  And then if I check off the first "Buy Milk" event on the
3rd, then the list would be updated to have "Buy Milk" on 10th, 17th...
instead.

Depending on how you represent the recurrence, it might be easier to keep track
of the "current" instance of a recurring task and base the rest of the
recurrences off that instead of trying to determine everything at the time of
creation.

As for keeping track of task completion, could you just tack something onto the
end of the string, like so?

`#XXXXXX Buy Milk @ 2014-12-02 - 2014-12-04 X 2012-12-03`

where `X <date>` means that it was completed on that day?  If I recall
correctly, that's kind of how todo.txt does it, except without the date.

Also, as another question, how granular do you want to make the dependency
deadlines?  For instance, if I have tasks A and B where A has a hard due date
at some date and time, and B is due a day after A, is B due exactly 24 hours
after A, or just anytime the day after A is due?  And what about
weeks/days/months?  Do you intend to round based on A, or B, or do something
else entirely?

Christine's Critique

Nick's Critique
I like the sound of relative to exact statement conversion.  I hope you can get that working.

Marking up the input file sounds like a better option.  You could strikethrough the event (if that's possible in the current file format), or come up with a reasonable marker for completed tasks (like a checkmark).

Creating a better regex sounds achievable without being too difficult.  Maybe there's an existing approach you can work from?  It would make things a lot nicer for your users.  I hope you can make some simple things (like the hashtag) automated as well, that sounds achieveable and useful.

It seems like you have a lot of potential tradeoffs between requireing the user to be better about their syntax and adding in better error checks.  If you can, it might be nice to make the parser more generous so punctuation can be added and accidental marks won't break parsing entirely.  However, I would make sure you don't put too much work on yourselves to get that to work.  If it's too hard to make the parser clever, just require your users to be better about syntax.

It sounds like some work to add more clear and verbose syntax in place of succinct syntax.  Personally, I would stick with the succinct syntax and make the user figure it out.  That sounds easier and not unreasonable as a program creator.  If there is a benefit to the succinct syntax, making the user use it also sounds like a good idea.
