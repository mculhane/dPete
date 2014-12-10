##Michael Critique

Nice, looks like you got a lot done this week!  I think all your design
decisions make sense.  Pinning down a concrete subset of behaviors that are
allowed makes recurrences and dependencies seem a lot more manageable, and it
seems like you've decided on a pretty broad but doable set.

Some cases that I can think of:
* Can you have dependence chains?  ie. A depends on B depends on C?  Would that
* just be specified in
three lines, like (pseudocoded) `A ^ B; B ^ C; C`?
* I'm a bit unclear as to the behavior of the depencency in general.  If I say 
`#AAAAAA Turn in completed assignment ^ #BBBBBB Due + 3 days, Start + 3 days`,
does that mean `#AAAAAA`'s start date is three days after `#BBBBBB`'s, and
likewise, `#AAAAAA`'s due date is three days after `#BBBBBB`'s?  I think that
sounds right, but if it's based on `#AAAAAA`'s completion date instead, it
seems that this could cause problems where if you finish your assignment a day
later than you'd planned (but still before the due date), you might have it be
legal to turn in the assignment one day late as well (for instance).
* On the other hand, having it based on completion date would be kind of nice
for some tasks, ones that have to be completed in order but with looser time
constraints.  For instance, it makes a lot more sense to have `wash dishes`
appear on the todo list once `cook dinner` is completed, instead of some 
arbitrary amount of time afterwards.  But this might be hard.

## Nick Critique

Glad to hear you have human-readable dates, that sounds like it will be useful for your users.  I'm not familiar with date conversion, so I don't have suggestions, but the way you're doing it sounds reasonable.  Converting from what the user inputs to a standard format sounds like it will make it easier for you to parse dates.

I like the sound of the snippets.

I feel like it would be useful to give errors as early as possible in the process.  If you can get the parser to tell the user they've made a mistake, do.  I would tell the user immediately about every error.  You might still allow the input through, depending on what the error is, but you should definitely let the user know what has happened.  Perhaps an option could control this?  It sounds more difficult to deal with misinterpreted input, because if input is misinterpreted there might not necessarily be a way for the program to know that.  Perhaps there could be optional output of what the program saw, a bit fancier than simple print statements?  This way if the user wanted to be sure what was going on, they could turn the option on and be sure that they knew what was going on when they input tasks into the program.

I think I misunderstood the exact issues with marking done tasks, oops.  Repeat tasks were something I hadn't considered.  If it works at all in the output I guess that's a good way to handle it until you can put more thought into how you can handle the issue.

Best of luck on the last week!
