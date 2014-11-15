# Christine's Critique

I think that this project is shaping up to be pretty cool and the features sound super useful. 
A lot of your questions about IR and recurrences were discussed on class on Monday - but we 
still have not come up with a defined IR for our syntax. However, I think that we are going 
in the right direction and once we have figured out the IR we can do some really cool things
with the language. We definitely have to discuss this part as a team, but as far as the 
questions about the Sublime text, I personally think that we should not use it as 
only a database for loading/storing tasks. I thing that it would be cooler, but also more
intuitive to users, if we use sublime to provide an interface that abstracts away the underlying
language. I think that both ideas could work though. Awesome work this week! 


# Michael's Critique

This week, I'm primarily critiquing the work that Christine and Nick did. This is necessarily
reflective in nature because much of the brainstorming this past week was done as a larger
team.

In his notebook, Nick brings up some interesting points about "the trade-off between more
expressiveness and more syntax". This is something we struggled with a lot. At some level,
 we want to make sure that our language is full-featured enough to be useful, but at the
 same time we also want to avoid developing a language that's too feature heavy. We've spent
 a lot of time thinking through our desired functionality and pared down the feature set to
 something more manageable. For example, we were struggling quite a bit with what the start and
 end times on a recurring task would mean (and how they would interact with the from/until dates
 of a recurrence). After thinking about it for a while, though, we realized that any decision
 we made on the issue wouldn't resolve the confusion for the end user, so we decided to scrap
 from/until dates altogether. The result is a language and feature set that is much more
 straightforward and predictable, just like it should be. We need to constantly be on the lookout
 to remove unnecessary features wherever possible. There are plenty of overly-featured task
 management solutions out there; we don't need to be one of them.
 
 Stepping back and looking at the project as a whole, I think the feature set is probably the
 one component of the project that is most likely to get out of hand if left unchecked. Thus
 far, our process has been to dream up an ideal feature set and then construct an IR to
 manage it. While we have restricted our focus to time-awareness dependency awareness, we have
 tried to be comprehensive within those focus areas. As a result, we have rarely turned down
 a topical feature request. Moving forward, we need to better consider the tradeoff between
 language implementation and language design. That will help us rein in the project scope and
 end up with something that we can actually build.
