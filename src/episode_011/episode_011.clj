(ns episode-011)

(defn newton-step
  [f f-prime]
  (fn[x]
    (- x 
       (/ (f x) 
	  (f-prime x)))))
     
(defn parabola
  [x]
  (- (* x x) 5))

(defn parabola-prime
  [x]
  (* 2 x))

(defn newton
  "Creates a newton iterator.  If start is not provided, zero is assumed."
  ([f f-prime] (newton f f-prime 0))
  ([f f-prime start]
     (iterate (fn[x]
		(- x 
		   (/ (f x) 
		      (f-prime x))))
	      start)))

(defn forward-diff
  "Creates a closure that computes the derivative using a simple forward
difference."
  [f Δ]
  (fn[x](/
	 (-
	  (f (+ x Δ))
	  (f x))
	 Δ)))

(defn rk4
  "Starts at some a, and progresses by an amount Δ"
  ([f Δ] (rk4 f Δ 0))
  ([f Δ start]
     (iterate (fn [[x y] & more]
		(let [k1 (f x)
		      k2 (f (+ x (/ Δ 2)))
		      k3 (f (+ x (/ Δ 2)))
		      k4 (f (+ x Δ))]
		  [(+ x Δ)
		   (+ (* 1/6 Δ (+ k1 (* 2 k2) (* 2 k3) k4)) y)]))
	      [start 0])))

(defn -sqr [a b] (let [d (- a b)] (* d d)))
 
(defn -norm [a b] (Math/sqrt (-sqr a b)))

(defn simple-converge
  "Finds the first element of coll where the norm of two consecutive
elements is less than ε."
  ([ε coll] (simple-converge ε -norm coll))
  ([ε norm coll]
     (ffirst 
      (drop-while 
       (fn [[a b] & more] (< ε (norm a b)))
       (partition 2 1 coll)))))