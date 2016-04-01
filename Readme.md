# Blocking issue with user namespace

Somehow the special treatment of the `user` namespace can cause things to block indefinitely.

**To reproduce** run `lein repl` in a clean checkout of this repo. You
  should (after some messages) end up in a REPL. Now run the following:

```sh
echo "(ns user (:require [fail :as fail]))" > src/user.clj
```

Running `lein repl` again should cause output as below:

```
new thing
before block-start
```

Eventually the REPL will timeout.

**What's causing this:** If code like the one below is either in
`user.clj` directly or in any of the namespaces required by `user` the
`deref` ing of futures will block indefinitely.

```clj
(println "lets make a thing...")
(deref (future #(println "making a thing")))
(println "thing is made")
```

If another namespace like `core` in this repo requires namespaces
containing the code above everything works fine.