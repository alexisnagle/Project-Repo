{-
 - Author: Alexis Nagle, anagle2020@my.fit.edu
 - Author: Hannah Callihan, hcallihan2020@my.fit.edu
 - Course: CSE 4250, Fall 2021
 - Project: Proj4, Tautology Checker
 - Language implementation: Glorious Glasgow Haskell Compilation System, version 9.2.5
 - Referenced https://rosettacode.org/wiki/Parsing/RPN_to_infix_conversion#Haskell --- To structure stack implementation
 - Referenced https://stackoverflow.com/questions/37119541/expression-tree-to-list-of-string --- To structure Expression data type
 -}

-- Expression data type acts as a tree to organize the expressions
data Expression = Var Char | A Expression Expression | C Expression Expression | D Expression Expression
                           | E Expression Expression | J Expression Expression | K Expression Expression | N Expression

-- Convert postfix to infix notation for propositional formulas --
postfixToInfix :: String -> Expression
postfixToInfix = head . foldl buildExp [] 

-- Build an expression --
buildExp :: [Expression] -> Char -> [Expression]
buildExp stack x
  | (x == 'A') = (A l r) : rest           -- build disjunction expression with two operands from the stack
  | (x == 'C') = (C l r) : rest           -- build implication expression with two operands from the stack
  | (x == 'D') = (D l r) : rest           -- build nand expression with two operands from the stack
  | (x == 'E') = (E l r) : rest           -- build equivalenceexpression with two operands from the stack
  | (x == 'J') = (J l r) : rest           -- build xor expression with two operands from the stack
  | (x == 'K') = (K l r) : rest           -- build conjunction expression with two operands from the stack
  | (x == 'N') = (negateExp(r)) : rest1   -- build negation expression with one operand from the stack -- call negateExp so it distributes
  | otherwise = Var x : stack             -- push constants to the stack
  where
    r = case stack of
      [] -> Var ' '                       -- if stack is empty, r is an empty var
      right:_ -> right                    -- if not empty remove r
    rest1 = case stack of
      [] -> []                            -- if stack is empty, the reminder is also empty
      _:tempRest -> tempRest              -- if not empty, remainer has one less value
    l = case rest1 of
      [] -> Var ' '                       -- if current remainder is empty, l is an empty var
      left:_ -> left                      -- if not empty remove l
    rest = case rest1 of
      [] -> []                            -- if current remainder is empty, the reminder is also empty
      _:tempRest -> tempRest              -- if not empty, remainer has one less value

-- Call functions on the input --
main :: IO ()
main = interact (unlines. map(isTautology.conjunctiveNorm.conjunctiveNorm.normalNeg.postfixToInfix).lines)

-- Convert Expression into a string --
dispExpr :: Expression -> String
dispExpr(Var l) = l : []                                                            -- char to string
dispExpr(A l r) = "(" ++ dispExpr l ++ " v " ++ dispExpr r ++ ")"                   -- disjunction   PvQ   
dispExpr(K l r) = "(" ++ dispExpr l ++ " & " ++ dispExpr r ++ ")"                   -- conjunction   P&Q
dispExpr(N r) = "~" ++ dispExpr r                                                   -- negation      ~Q
dispExpr _ = ""

-- NNF -- Get rid of implications, equivalance, nand, & xor and distribute the negative 
-- recursively call on each left and right 
-- we call negateExp instead of N so we can distribute the negation
normalNeg :: Expression -> Expression
normalNeg(A r l) = A (normalNeg r) (normalNeg l)                                    -- recursively go through the tree
normalNeg(K r l) = K (normalNeg r) (normalNeg l)                                    -- recursively go through the tree
normalNeg(C r l) = A (normalNeg(N r)) (normalNeg l)                                 -- implication   ~PvQ 
normalNeg(D r l) = A (normalNeg(N r)) (normalNeg(N l))                              -- nand          ~(P&Q) == (~Pv~Q) 
normalNeg(E r l) = K (normalNeg(C r l)) (normalNeg(C l r))                          -- equivalence   (P->Q)&(Q->P)
normalNeg(J r l) = A (normalNeg(K r (N l))) (normalNeg(K (N r) l))                  -- xor           (P&~Q)v(~P&Q)
normalNeg(N x) = negateExp(normalNeg x)                                             -- distNeg ~(P&Q) == ~Pv~Q AND ~(PvQ) == ~P&~Q
normalNeg x = x

-- Distribute Negatives -- recursively call until we cant distribute anymore
negateExp :: Expression -> Expression
negateExp(A r l) = K (negateExp r) (negateExp l)                                    -- ~(PvQ) == ~P&~Q
negateExp(K r l) = A (negateExp r) (negateExp l)                                    -- ~(P&Q) == ~Pv~Q
negateExp(N x) = x                                                                  -- ~(~P) == P
negateExp x = N x                                                                   -- ~(P) == ~P

-- CNF --distribute the or's (A) over the and's (K)  --  (P & (Q v R)) == (P v Q) & (P v R)
conjunctiveNorm :: Expression -> Expression
conjunctiveNorm(A r (K r2 l2)) = K (conjunctiveNorm(A r r2)) (conjunctiveNorm(A r l2))
conjunctiveNorm(A (K r2 l2) l) = K (conjunctiveNorm(A r2 l)) (conjunctiveNorm(A l2 l))
conjunctiveNorm(A l r) = A (conjunctiveNorm(l)) (conjunctiveNorm(r))                -- recursively go through the tree 
conjunctiveNorm(K l r) = K (conjunctiveNorm(l)) (conjunctiveNorm(r))                -- recursively go through the tree 
conjunctiveNorm(N r) = N (conjunctiveNorm(r))
conjunctiveNorm x = x

-- Check if expression is a tautology and output answer
isTautology :: Expression -> String
isTautology(x) 
  | determineTautology(x) = "true " ++ dispExpr(x)
  | otherwise = "false " ++ dispExpr(x)

-- Determine Tautology -- true when the expression always evaluates to 1 -- needs P v ~P on each side of the &
determineTautology :: Expression -> Bool
determineTautology(K l r) = determineTautology(l) && determineTautology(r)          -- if and, then both sides must equal 1
determineTautology(A l r)                                                           -- if or, the only one side needs to equal 1
  | notAnd(l) && notAnd(r) = checkEquivalance(negateExp(l), r) || checkEquivalance(l, negateExp(r)) -- negate one side and check equivalance
  | otherwise = determineTautology(l) || determineTautology(r)
determineTautology _ = False

-- determine if the expression is not an and expression
notAnd :: Expression -> Bool
notAnd(K _ _) = False
notAnd _ = True

-- Check if the characters in an or statement are the same
checkEquivalance :: (Expression, Expression) -> Bool
checkEquivalance(Var r, Var l) = r == l                                                       -- P = P
checkEquivalance((N r), (N l)) = checkEquivalance(r,l)                                        -- ~P = ~P
checkEquivalance((K l r), Var x) = checkEquivalance(l, Var x) || checkEquivalance(r, Var x) 
                                || checkEquivalance(l,negateExp(r)) || checkEquivalance(negateExp(l), r)   -- (P & Q), P -- P=P || P=Q
checkEquivalance(Var x, (K l r)) = checkEquivalance(l, Var x) || checkEquivalance(r, Var x) 
                                || checkEquivalance(l,negateExp(r)) || checkEquivalance(negateExp(l), r)   -- P, (P & Q) -- P=P || P=Q
checkEquivalance((A l r), Var x) = checkEquivalance(l, Var x) || checkEquivalance(r, Var x) 
                                || checkEquivalance(l,negateExp(r)) || checkEquivalance(negateExp(l), r)   -- (P v Q), P -- P=P || P=Q
checkEquivalance(Var x, (A l r)) = checkEquivalance(l, Var x) || checkEquivalance(r, Var x) 
                                || checkEquivalance(l,negateExp(r)) || checkEquivalance(negateExp(l), r)   -- P, (P v Q) -- P=P || P=Q
checkEquivalance((K l1 r1), (A l2 r2)) = checkEquivalance(l1, l2) || checkEquivalance(l1, r2) 
                                      || checkEquivalance(r1, l2) || checkEquivalance(r1, r2)
                                      || checkEquivalance(l1, r1) || checkEquivalance(l2, r2) -- (P & Q), (P v K) -- P=P ||P=Q || P=K || Q=K
checkEquivalance((A l1 r1), (K l2 r2)) = checkEquivalance(l1, l2) || checkEquivalance(l1, r2) 
                                      || checkEquivalance(r1, l2) || checkEquivalance(r1, r2)
                                      || checkEquivalance(l1, r1) || checkEquivalance(l2, r2) -- (P & Q), (P v K) -- P=P ||P=Q || P=K || Q=K
checkEquivalance _ = False
