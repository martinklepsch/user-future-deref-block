(ns fail)

(println "lets make a thing...")
(deref (future #(println "making a thing")))
(println "thing is made")
