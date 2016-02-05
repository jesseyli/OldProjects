;;; Test cases for Scheme.
;;;
;;; In order to run only a prefix of these examples, add the line
;;;
;;; (exit)
;;;
;;; after the last test you wish to run.

;;; *** Add more of your own here! ***

;;; These are examples from several sections of "The Structure
;;; and Interpretation of Computer Programs" by Abelson and Sussman.

;;; License: Creative Commons share alike with attribution

;;; 1.1.1

10
; expect 10

True
; expect True

False
; expect False

(not False)
; expect True

(not True)
; expect False

(pair? True)
; expect False

(list? (cons 1 (list 2)))
; expect True

(null? ())
; expect True

-
; expect #[primitive]

car
; expect #[primitive]

unbound_variable
; expect Error

(+ 137 349)
; expect 486

(- 1000 334)
; expect 666

(* 5 99)
; expect 495

(/ 10 5)
; expect 2

(+ 2.7 10)
; expect 12.7

(+ 21 35 12 7)
; expect 75

(* 25 4 12)
; expect 1200

(+ (* 3 5) (- 10 6))
; expect 19

(+ (* 3 (+ (* 2 4) (+ 3 5))) (+ (- 10 7) 6))
; expect 57

(+ (* 3
      (+ (* 2 4)
         (+ 3 5)))
   (+ (- 10 7)
      6))
; expect 57


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Move the following (exit) line to run additional tests. ;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;; 1.1.2

(define 0 2)
; expect Error

(list 1 2 3)
; expect (1 2 3)

(list 1 2 (list 3 4 (list 5 6)))
; expect (1 2 (3 4 (5 6)))

(define plus +)
; expect plus
plus
; expect #[primitive]

nil
; expect ()

(list 1 2 3 'a nil)
; expect (1 2 3 a ())

(list unbound_variable)
; expect Error

(define size 2)
; expect size
size
; expect 2

(* 5 size)
; expect 10

(define pi 3.14159)
(define radius 10)
(* pi (* radius radius))
; expect 314.159

(define circumference (* 2 pi radius))
circumference
; expect 62.8318

(define random-equation (* radius (+ 1 3 4 (/ 9 3))))
; expect random-equation
random-equation
; expect 110

'unbound_variable
; expect unbound_variable

(eval (cons 'cdr '('(7 1 2))))
; expect (1 2)

(car '(a b c))
; expect a

(cdr '(a b c))
; expect (b c)

;;; 1.1.4

(define (adder x total) (+ x total))
;expect adder
(adder 5 (adder 5 12))
; expect 22

(begin (display 'hello) (display 'begin) (display 'is) (display 'working))
; expect hellobeginisworkingokay

(define hello (begin (display 'foo) (newline) (* 3 3)))
; expect foo ; hello
hello
; expect 9

(begin 10 10 20 50 'hello)
; expect hello

(begin 10 10 20 50 unbound_variable)
; expect Error

(define (square x) (* x x))
; expect square
(square 21)
; expect 441

((lambda (pi r) (* pi (* r r))) 3.14 3)
; expect 28.26

((lambda (x) (display 'lambda) 
			  (display 'is) 
			  (display 'working) 
			  (newline) 
			  x) 7)
; expect lambdaisworking ; 7

(lambda (y) 15 (* y 7))
; expect (lambda (y) (begin 15 (* y 7)))

(lambda (y) 15)
; expect (lambda (y) 15)

(define square (lambda (x) (* x x))) ; See Section 1.3.2
(square 21)
; expect 441

(square (+ 2 5))
; expect 49

(square (square 3))
; expect 81

(define (sum-of-squares x y)
  (+ (square x) (square y)))
(sum-of-squares 3 4)
; expect 25

(define (f x y z) 
	(display x) 
	(display y) 
	(display z))
; expect f

(f 1 2)
; expect Error

(define (hello x x) x)
; expect Error

(define (0 x) x)
; expect Error

(define (hello x y 0))
; expect Error

(define (hello x) (+ x 6))
; expect hello
(hello 2)
; expect 8

(define (cube x) (* x x x))
; expect cube
cube
; expect (lambda (x) (* x x x))
(cube 3)
; expect 27

(define hello (lambda (x) (+ x 6)))
; expect hello
(hello 3)
; expect 9

(define (f a)
  (sum-of-squares (+ a 1) (* a 2)))
(f 5)
; expect 136

;;; 1.1.6

(if (= 1 0) true)
; expect okay

(if nil true false)
; expect True

(if true (+ 1 1) false)
; expect 2

(and)
; expect True

(or)
; expect False

(and 1 2 3 4 5)
; expect 5

(or false false 5)
; expect 5

(or 6 (display 'notworking))
; expect 6

(and false (display 'notworking))
; expect False

(cond ((= 1 2) 'nope)
      ((= 0 0) 'hi)
      (else 'wait))
; expect hi

(cond ((= 1 2) 'nope)
	  ((= 1 1))
	  (else 'wait))
; expect True

(cond ((= 1 1) (display 'working) 5)
	  (else 'bad 'bad 'bad))
; expect working5

(cond ((= 1 1) (begin (display 'working) 5))
	  (else 'bad 'bad 'bad))
; expect working5

(cond (false) 
	  ('good))
; expect good

(cond)
; expect okay

(define x 'good)
; expect x

(define y 'day)
; expect y

(let ((x 10)
      (y (* 2 10)))
  (list x y))
; expect (10 20)

(list x y)
; expect (good day)

(let ((x 10)) x (display 'hello) 12)
; expect hello12

(let ((x 2) (y 8)) (+ x y))
; expect 10

((lambda (x y) (+ x y)) 2 8)
; expect 10

(define f (mu (x) (+ x y)))
; expect f

(define g (lambda (x y) (f (+ x x))))
; expect g

(g 3 7)
; expect 13

(define y 3)
; expect y

(f 0)
; expect 3

(define (abs x)
  (cond ((> x 0) x)
        ((= x 0) 0)
        ((< x 0) (- x))))
(abs -3)
; expect 3

(abs 0)
; expect 0

(abs 3)
; expect 3

(define (a-plus-abs-b a b)
  ((if (> b 0) + -) a b))
(a-plus-abs-b 3 -2)
; expect 5

;;; 1.1.7

(define (sqrt-iter guess x)
  (if (good-enough? guess x)
      guess
      (sqrt-iter (improve guess x)
                 x)))
(define (improve guess x)
  (average guess (/ x guess)))
(define (average x y)
  (/ (+ x y) 2))
(define (good-enough? guess x)
  (< (abs (- (square guess) x)) 0.001))
(define (sqrt x)
  (sqrt-iter 1.0 x))
(sqrt 9)
; expect 3.00009155413138

(sqrt (+ 100 37))
; expect 11.704699917758145

(sqrt (+ (sqrt 2) (sqrt 3)))
; expect 1.7739279023207892

(square (sqrt 1000))
; expect 1000.000369924366

;;; 1.1.8

(define (sqrt x)
  (define (good-enough? guess)
    (< (abs (- (square guess) x)) 0.001))
  (define (improve guess)
    (average guess (/ x guess)))
  (define (sqrt-iter guess)
    (if (good-enough? guess)
        guess
        (sqrt-iter (improve guess))))
  (sqrt-iter 1.0))
(sqrt 9)
; expect 3.00009155413138

(sqrt (+ 100 37))
; expect 11.704699917758145

(sqrt (+ (sqrt 2) (sqrt 3)))
; expect 1.7739279023207892

(square (sqrt 1000))
; expect 1000.000369924366

;;; 1.3.1

(define (cube x) (* x x x))
(define (sum term a next b)
  (if (> a b)
      0
      (+ (term a)
         (sum term (next a) next b))))
(define (inc n) (+ n 1))
(define (sum-cubes a b)
  (sum cube a inc b))
(sum-cubes 1 10)
; expect 3025

(define (identity x) x)
(define (sum-integers a b)
  (sum identity a inc b))
(sum-integers 1 10)
; expect 55

;;; 1.3.2

((lambda (x y z) (+ x y (square z))) 1 2 3)
; expect 12

(define (f x y)
  (let ((a (+ 1 (* x y)))
        (b (- 1 y)))
    (+ (* x (square a))
       (* y b)
       (* a b))))
(f 3 4)
; expect 456

(define x 5)
(+ (let ((x 3))
     (+ x (* x 10)))
   x)
; expect 38

(let ((x 3)
      (y (+ x 2)))
  (* x y))
; expect 21

;;; 2.1.1

(define (add-rat x y)
  (make-rat (+ (* (numer x) (denom y))
               (* (numer y) (denom x)))
            (* (denom x) (denom y))))
(define (sub-rat x y)
  (make-rat (- (* (numer x) (denom y))
               (* (numer y) (denom x)))
            (* (denom x) (denom y))))
(define (mul-rat x y)
  (make-rat (* (numer x) (numer y))
            (* (denom x) (denom y))))
(define (div-rat x y)
  (make-rat (* (numer x) (denom y))
            (* (denom x) (numer y))))
(define (equal-rat? x y)
  (= (* (numer x) (denom y))
     (* (numer y) (denom x))))

(define x (cons 1 2))
(car x)
; expect 1

(cdr x)
; expect 2

(define x (cons 1 2))
(define y (cons 3 4))
(define z (cons x y))
(car (car z))
; expect 1

(car (cdr z))
; expect 3

z
; expect ((1 . 2) 3 . 4)

(define (make-rat n d) (cons n d))
(define (numer x) (car x))
(define (denom x) (cdr x))
(define (print-rat x)
  (display (numer x))
  (display '/)
  (display (denom x))
  (newline))
(define one-half (make-rat 1 2))
(print-rat one-half)
; expect 1/2 ; okay

(define one-third (make-rat 1 3))
(print-rat (add-rat one-half one-third))
; expect 5/6 ; okay

(print-rat (mul-rat one-half one-third))
; expect 1/6 ; okay

(print-rat (add-rat one-third one-third))
; expect 6/9 ; okay

(define (gcd a b)
  (if (= b 0)
      a
      (gcd b (remainder a b))))
(define (make-rat n d)
  (let ((g (gcd n d)))
    (cons (/ n g) (/ d g))))
(print-rat (add-rat one-third one-third))
; expect 2/3 ; okay

(define one-through-four (list 1 2 3 4))
one-through-four
; expect (1 2 3 4)

(car one-through-four)
; expect 1

(cdr one-through-four)
; expect (2 3 4)

(car (cdr one-through-four))
; expect 2

(cons 10 one-through-four)
; expect (10 1 2 3 4)

(cons 5 one-through-four)
; expect (5 1 2 3 4)

(define (map proc items)
  (if (null? items)
      nil
      (cons (proc (car items))
            (map proc (cdr items)))))
(map abs (list -10 2.5 -11.6 17))
; expect (10 2.5 11.6 17)

(map (lambda (x) (* x x))
     (list 1 2 3 4))
; expect (1 4 9 16)

(define (scale-list items factor)
  (map (lambda (x) (* x factor))
       items))
(scale-list (list 1 2 3 4 5) 10)
; expect (10 20 30 40 50)

(define (count-leaves x)
  (cond ((null? x) 0)
        ((not (pair? x)) 1)
        (else (+ (count-leaves (car x))
                 (count-leaves (cdr x))))))
(define x (cons (list 1 2) (list 3 4)))
(count-leaves x)
; expect 4

(count-leaves (list x x))
; expect 8

;;; 2.2.3

(define (odd? x) (= 1 (remainder x 2)))
(define (filter predicate sequence)
  (cond ((null? sequence) nil)
        ((predicate (car sequence))
         (cons (car sequence)
               (filter predicate (cdr sequence))))
        (else (filter predicate (cdr sequence)))))
(filter odd? (list 1 2 3 4 5))
; expect (1 3 5)

(define (accumulate op initial sequence)
  (if (null? sequence)
      initial
      (op (car sequence)
          (accumulate op initial (cdr sequence)))))
(accumulate + 0 (list 1 2 3 4 5))
; expect 15

(accumulate * 1 (list 1 2 3 4 5))
; expect 120

(accumulate cons nil (list 1 2 3 4 5))
; expect (1 2 3 4 5)

(define (enumerate-interval low high)
  (if (> low high)
      nil
      (cons low (enumerate-interval (+ low 1) high))))
(enumerate-interval 2 7)
; expect (2 3 4 5 6 7)

(define (enumerate-tree tree)
  (cond ((null? tree) nil)
        ((not (pair? tree)) (list tree))
        (else (append (enumerate-tree (car tree))
                      (enumerate-tree (cdr tree))))))
(enumerate-tree (list 1 (list 2 (list 3 4)) 5))
; expect (1 2 3 4 5)

;;; 2.3.1

(define a 1)

(define b 2)

(list a b)
; expect (1 2)

(list 'a 'b)
; expect (a b)

(list 'a b)
; expect (a 2)

(car '(a b c))
; expect a

(cdr '(a b c))
; expect (b c)

(define (memq item x)
  (cond ((null? x) false)
        ((eq? item (car x)) x)
        (else (memq item (cdr x)))))
(memq 'apple '(pear banana prune))
; expect False

(memq 'apple '(x (apple sauce) y apple pear))
; expect (apple pear)

(define (equal? x y)
  (cond ((pair? x) (and (pair? y)
                        (equal? (car x) (car y))
                        (equal? (cdr x) (cdr y))))
        ((null? x) (null? y))
        (else (eq? x y))))
(equal? '(1 2 (three)) '(1 2 (three)))
; expect True

(equal? '(1 2 (three)) '(1 2 three))
; expect False

(equal? '(1 2 three) '(1 2 (three)))
; expect False

;;; Peter Norvig tests (http://norvig.com/lispy2.html)

(define double (lambda (x) (* 2 x)))
(double 5)
; expect 10

(define compose (lambda (f g) (lambda (x) (f (g x)))))
((compose list double) 5)
; expect (10)

(define apply-twice (lambda (f) (compose f f)))
((apply-twice double) 5)
; expect 20

((apply-twice (apply-twice double)) 5)
; expect 80

(define fact (lambda (n) (if (<= n 1) 1 (* n (fact (- n 1))))))
(fact 3)
; expect 6

(fact 50)
; expect 30414093201713378043612608166064768844377641568960512000000000000

(define (combine f)
  (lambda (x y)
    (if (null? x) nil
      (f (list (car x) (car y))
         ((combine f) (cdr x) (cdr y))))))
(define zip (combine cons))
(zip (list 1 2 3 4) (list 5 6 7 8))
; expect ((1 5) (2 6) (3 7) (4 8))

(define riff-shuffle (lambda (deck) (begin
    (define take (lambda (n seq) (if (<= n 0) (quote ()) (cons (car seq) (take (- n 1) (cdr seq))))))
    (define drop (lambda (n seq) (if (<= n 0) seq (drop (- n 1) (cdr seq)))))
    (define mid (lambda (seq) (/ (length seq) 2)))
    ((combine append) (take (mid deck) deck) (drop (mid deck) deck)))))
(riff-shuffle (list 1 2 3 4 5 6 7 8))
; expect (1 5 2 6 3 7 4 8)

((apply-twice riff-shuffle) (list 1 2 3 4 5 6 7 8))
; expect (1 3 5 7 2 4 6 8)

(riff-shuffle (riff-shuffle (riff-shuffle (list 1 2 3 4 5 6 7 8))))
; expect (1 2 3 4 5 6 7 8)

;;; Additional tests

(apply square '(2))
; expect 4

(apply + '(1 2 3 4))
; expect 10

(apply (if false + append) '((1 2) (3 4)))
; expect (1 2 3 4)

(if 0 1 2)
; expect 1

(if '() 1 2)
; expect 1

(or false true)
; expect True

(or)
; expect False

(and)
; expect True

(or 1 2 3)
; expect 1

(and 1 2 3)
; expect 3

(and False (/ 1 0))
; expect False

(and True (/ 1 0))
; expect Error

(or 3 (/ 1 0))
; expect 3

(or False (/ 1 0))
; expect Error

(or (quote hello) (quote world))
; expect hello

(if nil 1 2)
; expect 1

(if 0 1 2)
; expect 1

(if (or false False #f) 1 2)
; expect 2

(define (loop) (loop))
(cond (false (loop))
      (12))
; expect 12

((lambda (x) (display x) (newline) x) 2)
; expect 2 ; 2

(define g (mu () x))
(define (high f x)
  (f))

(high g 2)
; expect 2

(define (print-and-square x)
  (print x)
  (square x))
(print-and-square 12)
; expect 12 ; 144

(/ 1 0)
; expect Error

(define addx (mu (x) (+ x y)))
(define add2xy (lambda (x y) (addx (+ x x))))
(add2xy 3 7)
; expect 13


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Scheme Implementations ;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; len outputs the length of list s
(define (len s)
  (if (eq? s '())
    0
    (+ 1 (len (cdr s)))))
(len '(1 2 3 4))
; expect 4


; Problem 18

;; Merge two lists LIST1 and LIST2 according to COMP and return
;; the merged lists.
(define (merge comp list1 list2)
    (cond ((null? list1) list2)
          ((null? list2) list1)
          ((comp (car list1) (car list2))
          (cons (car list1) (merge comp (cdr list1) list2)))
          (else (cons (car list2) (merge comp (cdr list2) list1)))))

(merge < '(1 5 7 9) '(4 8 10))
; expect (1 4 5 7 8 9 10)
(merge > '(9 7 5 1) '(10 8 4 3))
; expect (10 9 8 7 5 4 3 1)
(merge < '(5 4 3 2 1) '())
; expect (5 4 3 2 1)
(merge > () '(1 2 3 4 5))
; expect (1 2 3 4 5)

;; Sort a list of lists of numbers to be in decreasing lexicographic
;; order. Relies on a correct implementation of merge.
(define (sort-lists lsts)
  (if (or (null? lsts) (null? (cdr lsts)))
      lsts
      (let ((sublsts (split lsts)))
        (merge greater-list
               (sort-lists (car sublsts))
               (sort-lists (cdr sublsts))))))

(define (greater-list x y)
  (cond ((null? y) #t)
        ((null? x) #f)
        ((> (car x) (car y)) #t)
        ((> (car y) (car x)) #f)
        (else (greater-list (cdr x) (cdr y)))))

(define (split x)
  (cond ((or (null? x) (null? (cdr x))) (cons x nil))
        (else (let ((sublsts (split (cdr (cdr x)))))
                (cons (cons (car x) (car sublsts))
                      (cons (car (cdr x)) (cdr sublsts)))))))

(merge greater-list '((3 2 1) (1 1) (0)) '((4 0) (3 2 0) (3 2) (1)))
; expect ((4 0) (3 2 1) (3 2 0) (3 2) (1 1) (1) (0))


; Problem 19

;; A list of all ways to partition TOTAL, where  each partition must
;; be at most MAX-VALUE and there are at most MAX-PIECES partitions.
(define (list-partitions total max-pieces max-value)
  (filter (lambda (partition) 
  	        (<= (len partition) max-pieces)) 
  	          (partitions total max-value)))

(define (add-on max-value partitions) 
	(map (lambda (part) (append (list max-value) part)) partitions))

(define (partitions total max-value)
  	(cond ((or (< total 1) (= max-value 0)) ())
  		  ((= total max-value) (cons (list max-value)
  				        			(partitions total (- max-value 1))))
  		  (else (append
  				  (add-on max-value (partitions (- total max-value) max-value)) 
				    (partitions total (- max-value 1))))))

; Problem 19 tests rely on correct Problem 18.
(sort-lists (list-partitions 5 2 4))
; expect ((4 1) (3 2))
(sort-lists (list-partitions 7 3 5))
; expect ((5 2) (5 1 1) (4 3) (4 2 1) (3 3 1) (3 2 2))
(sort-lists (list-partitions 3 3 2))
; expect ((2 1) (1 1 1))


; Problem 20

; Tree Abstract Data Implementation
(define (make_tree entry children)
  (cons entry children))

(define (entry tree)
  (car tree))

(define (children tree)
  (cdr tree))

(define tree
  (make_tree 5 (list
                (make_tree 6 (list
                              (make_tree 9 nil)
                              (make_tree 8 nil)))
                (make_tree 7 (list
                              (make_tree 1 nil)))
                (make_tree 2 (list
                              (make_tree 6 (list
                                            (make_tree 3 nil)))
                              (make_tree 4 nil))))))

;;                5
;;       +--------+--------+
;;       |        |        |
;;       6        7        2
;;    +--+--+     |     +--+--+
;;    |     |     |     |     |
;;    9     8     1     6     4
;;                      |
;;                      |
;;                      3


;; Takes a TREE of numbers and outputs a list of sums from following each
;; possible path from root to leaf.

(define (tree-sum-list tree) 
  (cond ((null? (children tree)) (list (entry tree)))
  		(else (map (lambda (x) (+ (entry tree) x)) ;what to use for variable x????
  				(flatten (map tree-sum-list (children tree)))))))

(define (flatten lst)
  (if (null? lst)
      '()
      (append (car lst)
              (flatten (cdr lst)))))

(tree-sum-list tree)
; expect (20 19 13 16 11)

(tree-sum-list (make_tree 5 (list 
								(make_tree 3 nil) 
								(make_tree 2 nil))))
; expect (8 7)


; Problem 21

; Draw the hax image using turtle graphics.
(define (hax d k)
  ; *** YOUR CODE HERE ***
  nil)


;;;;;;;;;;;;;;;;;;;;
;;; Extra credit ;;;
;;;;;;;;;;;;;;;;;;;;


; Tail call optimization test
(define (sum n total)
  (if (zero? n) total
    (sum (- n 1) (+ n total))))
(sum 1001 0)
; expect 501501
