# logicPlan.py
# ------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


"""
In logicPlan.py, you will implement logic planning methods which are called by
Pacman agents (in logicAgents.py).
"""

import util
import sys
import logic
import game

pacman_str = 'P'
ghost_pos_str = 'G'
ghost_east_str = 'GE'
pacman_alive_str = 'PA'

class PlanningProblem:
    """
    This class outlines the structure of a planning problem, but doesn't implement
    any of the methods (in object-oriented terminology: an abstract class).

    You do not need to change anything in this class, ever.
    """

    def getStartState(self):
        """
        Returns the start state for the planning problem.
        """
        util.raiseNotDefined()

    def getGhostStartStates(self):
        """
        Returns a list containing the start state for each ghost.
        Only used in problems that use ghosts (FoodGhostPlanningProblem)
        """
        util.raiseNotDefined()
        
    def getGoalState(self):
        """
        Returns goal state for problem. Note only defined for problems that have
        a unique goal state such as PositionPlanningProblem
        """
        util.raiseNotDefined()

def tinyMazePlan(problem):
    """
    Returns a sequence of moves that solves tinyMaze.  For any other maze, the
    sequence of moves will be incorrect, so only use this for tinyMaze.
    """
    from game import Directions
    s = Directions.SOUTH
    w = Directions.WEST
    return  [s, s, w, s, w, w, s, w]

def sentence1():
    """Returns a logic.Expr instance that encodes that the following expressions are all true.
    
    A or B
    (not A) if and only if ((not B) or C)
    (not A) or (not B) or C
    """
    A = logic.Expr('A')
    B = logic.Expr('B')
    C = logic.Expr('C')
    expr1 = A | B
    expr2 = (~A) % ((~B)|C)
    expr3 = logic.disjoin([~A,~B,C])
    return logic.conjoin([expr1,expr2,expr3])


def sentence2():
    """Returns a logic.Expr instance that encodes that the following expressions are all true.
    
    C if and only if (B or D)
    A implies ((not B) and (not D))
    (not (B and (not C))) implies A
    (not D) implies C
    """
    A = logic.Expr('A')
    B = logic.Expr('B')
    C = logic.Expr('C')
    D = logic.Expr('D')
    expr1 = C % (B | D)
    expr2 = A >> ((~B)&(~D))
    expr3 = (~(B&(~C))) >> A
    expr4 = (~D) >> C
    return logic.conjoin([expr1,expr2,expr3,expr4])

def sentence3():
    """Using the symbols WumpusAlive[1], WumpusAlive[0], WumpusBorn[0], and WumpusKilled[0],
    created using the logic.PropSymbolExpr constructor, return a logic.PropSymbolExpr
    instance that encodes the following English sentences (in this order):

    The Wumpus is alive at time 1 if and only if the Wumpus was alive at time 0 and it was
    not killed at time 0 or it was not alive and time 0 and it was born at time 0.

    The Wumpus cannot both be alive at time 0 and be born at time 0.

    The Wumpus is born at time 0.
    """

    WumpusAlive0 = logic.PropSymbolExpr('WumpusAlive',0)
    WumpusAlive1 = logic.PropSymbolExpr('WumpusAlive',1)
    WumpusBorn0 = logic.PropSymbolExpr('WumpusBorn',0)
    WumpusKilled0 = logic.PropSymbolExpr('WumpusKilled',0)
    expr1 = WumpusAlive1 % ((WumpusAlive0 & ~WumpusKilled0) | (~WumpusAlive0 & WumpusBorn0))
    expr2 = ~(WumpusAlive0 & WumpusBorn0)
    expr3 = WumpusBorn0
    return logic.conjoin([expr1,expr2,expr3])

def findModel(sentence):
    """Given a propositional logic sentence (i.e. a logic.Expr instance), returns a satisfying
    model if one exists. Otherwise, returns False.
    """ 
    return logic.pycoSAT(logic.to_cnf(sentence))

def atLeastOne(literals) :
    """
    Given a list of logic.Expr literals (i.e. in the form A or ~A), return a single 
    logic.Expr instance in CNF (conjunctive normal form) that represents the logic 
    that at least one of the literals in the list is true.
    >>> A = logic.PropSymbolExpr('A');
    >>> B = logic.PropSymbolExpr('B');
    >>> symbols = [A, B]
    >>> atleast1 = atLeastOne(symbols)
    >>> model1 = {A:False, B:False}
    >>> print logic.pl_true(atleast1,model1)
    False
    >>> model2 = {A:False, B:True}
    >>> print logic.pl_true(atleast1,model2)
    True
    >>> model3 = {A:True, B:True}
    >>> print logic.pl_true(atleast1,model2)
    True
    """
    return logic.disjoin(literals)


def atMostOne(literals) :
    """
    Given a list of logic.Expr literals, return a single logic.Expr instance in 
    CNF (conjunctive normal form) that represents the logic that at most one of 
    the expressions in the list is true.
    """
    notliterals = [~literal for literal in literals]
    clauses = []
    for i in range(0,len(literals)-1):
        for j in range(i+1, len(literals)):
            clause = [notliterals[i]] + [notliterals[j]]
            clauses.append(logic.disjoin(clause))
    return logic.conjoin(clauses)



def exactlyOne(literals) :
    """
    Given a list of logic.Expr literals, return a single logic.Expr instance in 
    CNF (conjunctive normal form)that represents the logic that exactly one of 
    the expressions in the list is true.
    """
    if len(literals) == 1:
        return literals[0]
    return logic.conjoin([logic.disjoin(literals),atMostOne(literals)])

def extractActionSequence(model, actions):
    """
    Convert a model in to an ordered list of actions.
    model: Propositional logic model stored as a dictionary with keys being
    the symbol strings and values being Boolean: True or False
    Example:
    >>> model = {"North[3]":True, "P[3,4,1]":True, "P[3,3,1]":False, "West[1]":True, "GhostScary":True, "West[3]":False, "South[2]":True, "East[1]":False}
    >>> actions = ['North', 'South', 'East', 'West']
    >>> plan = extractActionSequence(model, actions)
    >>> print plan
    ['West', 'South', 'North']
    """
    sequence = {}
    for k in model:
        if (logic.PropSymbolExpr.parseExpr(k)[0] in actions) and model[k]:
            sequence[logic.PropSymbolExpr.parseExpr(k)[1]] = logic.PropSymbolExpr.parseExpr(k)[0]
    return [sequence[k] for k in sorted(sequence, key=lambda x:int(x))]


def pacmanSuccessorStateAxioms(x, y, t, walls_grid):
    """
    Successor state axiom for state (x,y,t) (from t-1), given the board (as a 
    grid representing the wall locations).
    Current <==> (previous position at time t-1) & (took action to move to x, y)
    """

    literals = []
    if not walls_grid[x][y + 1]:
        literals.append(logic.PropSymbolExpr(pacman_str, x, y + 1, t-1)&logic.PropSymbolExpr('South', t-1))
    if not walls_grid[x][y - 1]:
        literals.append(logic.PropSymbolExpr(pacman_str, x, y - 1, t-1)&logic.PropSymbolExpr('North', t-1))
    if not walls_grid[x + 1][y]:
        literals.append(logic.PropSymbolExpr(pacman_str, x + 1, y, t-1)&logic.PropSymbolExpr('West', t-1))
    if not walls_grid[x - 1][y]:
        literals.append(logic.PropSymbolExpr(pacman_str, x - 1, y, t-1)&logic.PropSymbolExpr('East', t-1))
    expression = logic.PropSymbolExpr(pacman_str, x, y, t) % logic.disjoin(literals)
    return logic.to_cnf(expression)
    

def positionLogicPlan(problem):
    """
    Given an instance of a PositionPlanningProblem, return a list of actions that lead to the goal.
    Available actions are game.Directions.{NORTH,SOUTH,EAST,WEST}
    Note that STOP is not an available action.
    """
    walls = problem.walls
    width, height = problem.getWidth(), problem.getHeight()
    start = problem.getStartState()
    goal = problem.getGoalState()

    start_axiom = [logic.PropSymbolExpr(pacman_str,start[0],start[1],0)]
    for x in range(1,width+1):
        for y in range(1,height+1):
            if not walls[x][y] and (x != start[0] or y != start[1]):
                start_axiom.append(~logic.PropSymbolExpr(pacman_str,x,y,0))
    start_axiom.append(exactlyOne([logic.PropSymbolExpr('East',0), 
                                logic.PropSymbolExpr('West',0),
                                logic.PropSymbolExpr('South',0),
                                logic.PropSymbolExpr('North',0)]))
    for t in range(1,51):
        start_axiom.append(exactlyOne([logic.PropSymbolExpr('East',t), 
                            logic.PropSymbolExpr('West',t),
                            logic.PropSymbolExpr('South',t),
                            logic.PropSymbolExpr('North',t)]))
        for x in range(1,width+1):
            for y in range(1,height+1):
                if not walls[x][y]:
                    start_axiom.append(pacmanSuccessorStateAxioms(x,y,t,walls))
        start_axiom.append(pacmanSuccessorStateAxioms(goal[0],goal[1],t+1,walls))
        start_axiom.append(logic.PropSymbolExpr(pacman_str,goal[0],goal[1],t+1))
        model = logic.pycoSAT(logic.conjoin(start_axiom))
        if model:
            directions = [game.Directions.NORTH, game.Directions.SOUTH,game.Directions.EAST,game.Directions.WEST]
            actions = extractActionSequence(model, directions)
            return actions
        start_axiom.pop()
        start_axiom.pop()


def foodLogicPlan(problem):
    """
    Given an instance of a FoodPlanningProblem, return a list of actions that help Pacman
    eat all of the food.
    Available actions are game.Directions.{NORTH,SOUTH,EAST,WEST}
    Note that STOP is not an available action.
    """
    walls = problem.walls
    width, height = problem.getWidth(), problem.getHeight()
    start, foodGrid = problem.getStartState()[0],problem.getStartState()[1]
    start_axiom = [logic.PropSymbolExpr(pacman_str,start[0],start[1],0)]
    for x in range(1,width+1):
        for y in range(1,height+1):
            if not walls[x][y] and (x != start[0] or y != start[1]):
                start_axiom.append(~logic.PropSymbolExpr(pacman_str,x,y,0))
    start_axiom.append(exactlyOne([logic.PropSymbolExpr('East',0), 
                                logic.PropSymbolExpr('West',0),
                                logic.PropSymbolExpr('South',0),
                                logic.PropSymbolExpr('North',0)]))
    for t in range(1,51):
        start_axiom.append(exactlyOne([logic.PropSymbolExpr('East',t), 
                            logic.PropSymbolExpr('West',t),
                            logic.PropSymbolExpr('South',t),
                            logic.PropSymbolExpr('North',t)]))
        position_t = []
        move_t = []
        for x in range(1,width+1):
            for y in range(1,height+1):
                if not walls[x][y]:
                    move_t.append(pacmanSuccessorStateAxioms(x,y,t,walls))
        start_axiom += move_t

        pops = 0
        for x in range(1,width+1):
            for y in range(1,height+1):
                visit = []
                if foodGrid[x][y]:
                    for i in range(1,t+1):
                        visit.append(logic.PropSymbolExpr(pacman_str,x,y,i))
                    start_axiom.append(atLeastOne(visit))
                    pops += 1

        model = logic.pycoSAT(logic.conjoin(start_axiom))
        if model:
            directions = [game.Directions.NORTH,game.Directions.SOUTH,game.Directions.EAST,game.Directions.WEST]
            actions = extractActionSequence(model, directions)
            return actions
        for i in range(0,pops):
            start_axiom.pop()

def ghostPositionSuccessorStateAxioms(x, y, t, ghost_num, walls_grid):
    """
    Successor state axiom for patrolling ghost state (x,y,t) (from t-1).
    Current <==> (causes to stay) | (causes of current)
    GE is going east, ~GE is going west 
    """
    pos_str = ghost_pos_str+str(ghost_num)
    east_str = ghost_east_str+str(ghost_num)
    disjoin = []
    if not walls_grid[x-1][y]:
        disjoin.append(logic.PropSymbolExpr(pos_str,x - 1,y, t-1) & logic.PropSymbolExpr(east_str,t-1))
    if not walls_grid[x+1][y]: 
        disjoin.append(logic.PropSymbolExpr(pos_str,x + 1,y, t-1) & ~logic.PropSymbolExpr(east_str,t-1))
    if walls_grid[x-1][y] and walls_grid[x+1][y]:
        disjoin.append(logic.PropSymbolExpr(pos_str, x, y, t-1))
    disjoin = logic.disjoin(disjoin)
    return logic.to_cnf(~logic.PropSymbolExpr(pos_str, x, y, t) | disjoin) & logic.to_cnf(~disjoin|logic.PropSymbolExpr(pos_str, x, y, t))

def ghostDirectionSuccessorStateAxioms(t, ghost_num, blocked_west_positions, blocked_east_positions):
    """
    Successor state axiom for patrolling ghost direction state (t) (from t-1).
    west or east walls.
    Current <==> (causes to stay) | (causes of current)
    """

    pos_str = ghost_pos_str+str(ghost_num)
    east_str = ghost_east_str+str(ghost_num)
    at_west_block = []
    at_east_block = []
    for west_block in blocked_west_positions:
        at_west_block.append(logic.PropSymbolExpr(pos_str,west_block[0], west_block[1],t))
    for east_block in blocked_east_positions:
        at_east_block.append(logic.PropSymbolExpr(pos_str,east_block[0], east_block[1],t))
    at_west_block = atLeastOne(at_west_block)
    at_east_block = atLeastOne(at_east_block)
    at_both = at_east_block & at_west_block
    prev = logic.PropSymbolExpr(east_str, t-1)
    return logic.to_cnf(logic.PropSymbolExpr(east_str, t) % ((~at_east_block & prev) 
                                                    | (at_both & ~prev)
                                                    | (at_west_block & ~at_east_block)))


def pacmanAliveSuccessorStateAxioms(x, y, t, num_ghosts):
    """
    Successor state axiom for patrolling ghost state (x,y,t) (from t-1).
    Current <==> (causes to stay) | (causes of current)
    """
    ghost_strs = [ghost_pos_str+str(ghost_num) for ghost_num in xrange(num_ghosts)]
    killed_by_ghost = []
    new_location = logic.PropSymbolExpr(pacman_str, x, y, t)
    for ghost in ghost_strs:
        killed_by_ghost.append(logic.PropSymbolExpr(ghost, x, y, t) & new_location)
        killed_by_ghost.append(logic.PropSymbolExpr(ghost, x, y, t-1) & new_location)
    return logic.to_cnf((~logic.PropSymbolExpr(pacman_alive_str, t)) % ((atLeastOne(killed_by_ghost) | ~logic.PropSymbolExpr(pacman_alive_str, t-1))))

def foodGhostLogicPlan(problem):
    """
    Given an instance of a FoodGhostPlanningProblem, return a list of actions that help Pacman
    eat all of the food and avoid patrolling ghosts.
    Ghosts only move east and west. They always start by moving East, unless they start next to
    and eastern wall. 
    Available actions are game.Directions.{NORTH,SOUTH,EAST,WEST}
    Note that STOP is not an available action.
    """

    walls = problem.walls
    width, height = problem.getWidth(), problem.getHeight()
    not_walls = []
    for x in range(1,width+1):
        for y in range(1,height+1):
            if not walls[x][y]:
                not_walls += [(x,y)]
    ghosts = problem.getGhostStartStates()
    start, foodGrid = problem.getStartState()[0],problem.getStartState()[1]
    start_axiom = [logic.PropSymbolExpr(pacman_str,start[0],start[1],0)]
    start_axiom.append(logic.PropSymbolExpr(pacman_alive_str,0))
    blocked_west_positions = []
    blocked_east_positions = []
    ghost_num = 0
    num_ghosts = len(ghosts)
    reachable = {}
    all_reachable = []

    ghost_y = []
    for ghost in ghosts:
        ghost_start = ghost.getPosition()
        x = ghost_start[0] + 1
        y = ghost_start[0] - 1
        ghost_y.append(ghost_start[1])
        start_axiom.append(logic.PropSymbolExpr(ghost_pos_str+str(ghost_num),ghost_start[0],ghost_start[1],0))
        if not walls[ghost_start[0]+1][ghost_start[1]]:
            start_axiom.append(logic.PropSymbolExpr(ghost_pos_str+str(ghost_num),ghost_start[0]+1,ghost_start[1],1))
            start_axiom.append(logic.PropSymbolExpr(ghost_east_str+str(ghost_num),0))
        elif not walls[ghost_start[0]-1][ghost_start[1]]:
            start_axiom.append(logic.PropSymbolExpr(ghost_pos_str+str(ghost_num),ghost_start[0]-1,ghost_start[1],1))
            start_axiom.append(~logic.PropSymbolExpr(ghost_east_str+str(ghost_num),0))
        else:        
            start_axiom.append(logic.PropSymbolExpr(ghost_pos_str+str(ghost_num),ghost_start[0],ghost_start[1],1))
            start_axiom.append(logic.PropSymbolExpr(ghost_east_str+str(ghost_num),0))
        for space in not_walls:
                if (space[0] != ghost_start[0] or space[1] != ghost_start[1]):
                        start_axiom.append(~logic.PropSymbolExpr(ghost_pos_str+str(ghost_num),space[0],space[1],0))
        reachable[ghost_num] = [(ghost_start[0],ghost_start[1])]
        all_reachable.append((ghost_start[0],ghost_start[1]))
        while not walls[x][ghost_start[1]]:
            reachable[ghost_num].append((x,ghost_start[1]))
            all_reachable.append((x,ghost_start[1]))
            x += 1
        while not walls[y][ghost_start[1]]:
            reachable[ghost_num].append((y,ghost_start[1]))
            all_reachable.append((y,ghost_start[1]))
            y -= 1
        ghost_num += 1
    for space in not_walls:
        if walls[space[0]-1][space[1]]: 
            blocked_west_positions.append((space[0],space[1]))
        if walls[space[0]+1][space[1]]:
            blocked_east_positions.append((space[0],space[1]))
        if (space[0] != start[0] or space[1] != start[1]):
            start_axiom.append(~logic.PropSymbolExpr(pacman_str,space[0],space[1],0))
    start_axiom.append(exactlyOne([logic.PropSymbolExpr('East',0), 
                                logic.PropSymbolExpr('West',0),
                                logic.PropSymbolExpr('South',0),
                                logic.PropSymbolExpr('North',0)]))
    for t in range(1,51):
        pops = 0
        start_axiom.append(exactlyOne([logic.PropSymbolExpr('East',t), 
                            logic.PropSymbolExpr('West',t),
                            logic.PropSymbolExpr('South',t),
                            logic.PropSymbolExpr('North',t)]))
        position_t = []
        for space in not_walls:
            start_axiom.append(pacmanSuccessorStateAxioms(space[0],space[1],t,walls))
            if space in all_reachable:
                start_axiom.append(pacmanAliveSuccessorStateAxioms(space[0], space[1], t, num_ghosts))
            for ghost_num in xrange(num_ghosts):
                if space in reachable[ghost_num]:
                    start_axiom.append(ghostPositionSuccessorStateAxioms(space[0], space[1], t, ghost_num, walls))
                    start_axiom.append(ghostDirectionSuccessorStateAxioms(t, ghost_num, blocked_west_positions, blocked_east_positions))
                else:
                    start_axiom.append(~logic.PropSymbolExpr(ghost_pos_str+str(ghost_num),space[0],space[1],t))
        for x in range(1,width+1):
            for y in range(1,height+1):
                visit = []
                if foodGrid[x][y]:
                    for i in range(1,t+1):
                        visit.append(logic.PropSymbolExpr(pacman_str,x,y,i))
                    start_axiom.append(atLeastOne(visit))
                    pops += 1
        model = logic.pycoSAT(logic.conjoin(start_axiom))
        if model:
            directions = [game.Directions.NORTH,game.Directions.SOUTH,game.Directions.EAST,game.Directions.WEST]
            actions = extractActionSequence(model, directions)
            return actions
        for i in range(0,pops):
            start_axiom.pop()

# Abbreviations
plp = positionLogicPlan
flp = foodLogicPlan
fglp = foodGhostLogicPlan

# Some for the logic module uses pretty deep recursion on long expressions
sys.setrecursionlimit(100000)
    