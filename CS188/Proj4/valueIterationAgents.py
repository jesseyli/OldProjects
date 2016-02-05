# valueIterationAgents.py
# -----------------------
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


import mdp, util

from learningAgents import ValueEstimationAgent
import collections
import time
import pdb


class AsynchronousValueIterationAgent(ValueEstimationAgent):
    """
        * Please read learningAgents.py before reading this.*

        An AsynchronousValueIterationAgent takes a Markov decision process
        (see mdp.py) on initialization and runs cyclic value iteration
        for a given number of iterations using the supplied
        discount factor.
    """
    def __init__(self, mdp, discount = 0.9, iterations = 1000):
        """
          Your cyclic value iteration agent should take an mdp on
          construction, run the indicated number of iterations,
          and then act according to the resulting policy. Each iteration
          updates the value of only one state, which cycles through
          the states list. If the chosen state is terminal, nothing
          happens in that iteration.

          Some useful mdp methods you will use:
              mdp.getStates()
              mdp.getPossibleActions(state)
              mdp.getTransitionStatesAndProbs(state, action)
              mdp.getReward(state)
              mdp.isTerminal(state)
        """

        self.mdp = mdp
        self.discount = discount
        self.iterations = iterations
        self.values = collections.defaultdict(float)
        states = self.mdp.getStates()
        for state in states:
            self.values[state] = 0

        length = len(states)
        start = time.time()
        for i in range(iterations):
            state = states[i % length]
            if not mdp.isTerminal(state):
                best_total = -float('inf')
                actions = mdp.getPossibleActions(state)
                for action in actions:
                    total = self.computeQValueFromValues(state, action)
                    if total > best_total:
                        best_total = total
                self.values[state] = best_total

    def getValue(self, state):
        """
          Return the value of the state (computed in __init__).
        """
        return self.values[state]

    def computeQValueFromValues(self, state, action):
        """
          Compute the Q-value of action in state from the
          value function stored in self.values.
        """
        total = 0
        for sPrime in self.mdp.getTransitionStatesAndProbs(state, action):
                    total += sPrime[1]*(self.mdp.getReward(state) + self.discount*self.getValue(sPrime[0]))
        return total

    def computeActionFromValues(self, state):
        """
          The policy is the best action in the given state
          according to the values currently stored in self.values.

          You may break ties any way you see fit.  Note that if
          there are no legal actions, which is the case at the
          terminal state, you should return None.
        """
        actions = self.mdp.getPossibleActions(state)
        if actions:
            best = (-float('inf'), None)
            for action in actions:
                value = 0
                for sPrime in self.mdp.getTransitionStatesAndProbs(state, action):
                    value += self.values[sPrime[0]]*sPrime[1]
                if value > best[0]:
                    best = (value, action)
            return best[1]
        return None

    def getPolicy(self, state):
        return self.computeActionFromValues(state)

    def getAction(self, state):
        "Returns the policy at the state (no exploration)."
        return self.computeActionFromValues(state)

    def getQValue(self, state, action):
        return self.computeQValueFromValues(state, action)


class PrioritizedSweepingValueIterationAgent(AsynchronousValueIterationAgent):
    """
        * Please read learningAgents.py before reading this.*

        A PrioritizedSweepingValueIterationAgent takes a Markov decision process
        (see mdp.py) on initialization and runs prioritized sweeping value iteration
        for a given number of iterations using the supplied parameters.
    """
    def __init__(self, mdp, discount = 0.9, iterations = 100, theta = 1e-5):
        """
          Your prioritized sweeping value iteration agent should take an mdp on
          construction, run the indicated number of iterations,
          and then act according to the resulting policy.
        """
        self.mdp = mdp
        self.discount = discount
        self.iterations = iterations
        self.values = collections.defaultdict(float)
        states = self.mdp.getStates()
        predecessors = {}
        for state in states:
            self.values[state] = 0
            predecessors[state] = set()

        for state in states:
            for action in mdp.getPossibleActions(state):
                for sPrime in mdp.getTransitionStatesAndProbs(state, action):
                    if sPrime[1] > 0:
                        predecessors[sPrime[0]].add(state)
        
        queue = util.PriorityQueue()
        
        for state in states:
            if not mdp.isTerminal(state):
                best_total = -float('inf')
                actions = mdp.getPossibleActions(state)
                for action in actions:
                    total = self.computeQValueFromValues(state, action)
                    if total > best_total:
                        best_total = total
                diff = abs(self.values[state] - best_total)
                queue.push(state,-diff)
        start = time.time()
        for i in range(self.iterations):
            if queue.isEmpty():
                break
            s = queue.pop()
            if not mdp.isTerminal(s):
                best_total = -float('inf')
                actions = mdp.getPossibleActions(s)
                for action in actions:
                    total = self.computeQValueFromValues(s, action)
                    if total > best_total:
                        best_total = total
                self.values[s] = best_total
            for p in predecessors[s]:
                best_total = -float('inf')
                actions = mdp.getPossibleActions(p)
                for action in actions:
                    total = self.computeQValueFromValues(p, action)
                    if total > best_total:
                        best_total = total
                diff = abs(self.values[p] - best_total)
                if diff > theta:
                    queue.update(p, -diff)


