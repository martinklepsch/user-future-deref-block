# Blocking issue with user namespace

Somehow the special treatment of the `user` namespace can cause things to block indefinitely.

```sh
echo "(ns user (:require [fail :as fail]))" >> src/user.clj
```

If code like the one below is either in `user.clj` directly or in any
of the namespaces required by `user` the `deref` ing of futures will
block indefinitely.

```clj
(let [cnt     (atom 0)
      size    5
      open    (fn open [] (let [x (swap! cnt inc)] (println "new thing\n" x) x))
      openers (doall
               (map (fn open-pool [_]
                      (future (open)))
                    (range size)))]
  (println "before block-start")
  (doseq [worker openers] @worker)
  (println "after block-start"))
```

If another namespace like `core` in this repo requires namespaces
containing the code above everything works fine.