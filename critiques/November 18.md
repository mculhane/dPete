(Short critique because after in-class discussion on Monday the group
switched languages, eliminating most of the semantics implementation
concerns they had, so most of my original critique material is no
longer relevant.)

# IR

My only real concern about the IR is the syntax coupling I mentioned
last week in my critique for Nick. I think it seems like a perfectly
usable way of structuring your IR, and I can't think of any particular
difficulties it would cause you. The concern about checking types
isn't relevant anymore since y'all are using Scala now :)

# Semantics

As I noted above, the concerns I had about semantics are really no
longer relevant because they were primarily about the workarounds to
build the language in Python.

# Error Handling

I am a little worried about your plan for error handling. It seems
like you don't really consider the possibility of, e.g., a misparse,
or have a plan for how you will inform the user about sources of parse
errors. As far as I can tell there's a serious risk that a bad (but
successful) parse will not fail at the semantic level. Compare
e.g. PLs, where misparses happen but virtually always fail at the
semantic level except in JavaScript (in which it's okay because
executing JavaScript is already doing the wrong thing).
