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
