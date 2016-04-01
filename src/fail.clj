(ns fail)

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
