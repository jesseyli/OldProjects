(define (make-tree entry children)
  (cons entry children))

(define (entry t)
  (car t))

(define (children t)
  (cdr t))

;; utility functions
(define (leaf? t)
  (null? (children t)))
