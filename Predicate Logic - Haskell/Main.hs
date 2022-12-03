{-
 - Author: Alexis Nagle, anagle2020@my.fit.edu
 - Author:
 - Course: CSE 4250, Fall 2021
 - Project: Proj4, Tautology Checker
 - Language implementation: Glorious Glasgow Haskell Compilation System, version 9.2.5
 - Referenced https://rosettacode.org/wiki/Parsing/RPN_to_infix_conversion#Haskell --- To structure stack implementation
 - Reference https://stackoverflow.com/questions/37119541/expression-tree-to-list-of-string --- To structure Expression data type
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
    r : rest1 = stack                     -- pop right operands off the stack 
    l : rest = rest1                      -- pop left operand off the stack

-- Call functions on the input --
main :: IO ()
main = interact (unlines. map(isTautology.conjunctiveNorm.conjunctiveNorm.normalNeg.postfixToInfix).lines)

-- Convert Expression into a string --
dispExpr :: Expression -> String
dispExpr(Var l) = l : []                                                            -- char to string
dispExpr(A l r) = "(" ++ dispExpr l ++ " v " ++ dispExpr r ++ ")"                   -- disjunction   PvQ   
dispExpr(K l r) = "(" ++ dispExpr l ++ " & " ++ dispExpr r ++ ")"                   -- conjunction   P&Q
dispExpr(N r) = "~" ++ dispExpr r                                                   -- negation      ~Q

-- NNF -- Get rid of implications, equivalance, nand, & xor and distribute the negative 
-- we call negateExp instead of N so we can distribute the negation
normalNeg :: Expression -> Expression
normalNeg(A r l) = A (normalNeg(r)) (normalNeg(l))                                  -- recursively go through the tree
normalNeg(K r l) = K (normalNeg(r)) (normalNeg(l))                                  -- recursively go through the tree
normalNeg(C r l) = A (negateExp r) l                                                -- implication   ~PvQ 
normalNeg(D r l) = A (negateExp r) (negateExp l)                                    -- nand          ~(P&Q) == (~Pv~Q) 
normalNeg(E r l) = K (A (negateExp r) l) (A (negateExp l) r)                        -- equivalence   (~PvQ)&(~QvP)
normalNeg(J r l) = A (K r (negateExp l)) (K (negateExp r) l)                        -- xor           (P&~Q)v(~P&Q)
normalNeg(N x) = negateExp x                                                        -- distNeg ~(P&Q) == ~Pv~Q AND ~(PvQ) == ~P&~Q
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

-- Determine Tautology -- true when the expression always evaluates to 1 -- needs P v ~P
determineTautology :: Expression -> Bool
determineTautology(K l r) = determineTautology(l) && determineTautology(r)          -- if and, then both sides must equal 
determineTautology(A l r)                                                           -- if or, the only one side needs to equal 1
  | notAnd(l) && notAnd(r) = checkEquivalance(negateExp(l), r) || checkEquivalance(l, negateExp(r)) -- negate one side and check equivalance
  | otherwise = determineTautology(l) || determineTautology(r)
determineTautology x = False

-- determine if the expression is not an and expression
notAnd :: Expression -> Bool
notAnd(K r l) = False
notAnd _ = True

-- Check if the characters in an or statement are the same
checkEquivalance :: (Expression, Expression) -> Bool
checkEquivalance(Var r, Var l) = r == l                                                       -- P = P
checkEquivalance((N r), (N l)) = checkEquivalance(r,l)                                        -- ~P = ~P
checkEquivalance((K l r), Var x) = checkEquivalance(l, Var x) || checkEquivalance(r, Var x)   -- (P & Q), P -- P=P || P=Q
checkEquivalance(Var x, (K l r)) = checkEquivalance(l, Var x) || checkEquivalance(r, Var x)   -- P, (P & Q) -- P=P || P=Q
checkEquivalance((K l1 r1), (A l2 r2)) = checkEquivalance(l1, l2) || checkEquivalance(l1, r2) 
                                      || checkEquivalance(r1, l2) || checkEquivalance(r1, r2) -- (P & Q), (P v K) -- P=P ||P=Q || P=K || Q=K
checkEquivalance _ = False
