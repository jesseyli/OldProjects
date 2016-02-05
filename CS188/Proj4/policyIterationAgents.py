# policyIterationAgents.py
# ------------------------
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
import numpy as np
import pdb

from learningAgents import ValueEstimationAgent

class PolicyIterationAgent(ValueEstimationAgent):
    """
        * Please read learningAgents.py before reading this.*

        A PolicyIterationAgent takes a Markov decision process
        (see mdp.py) on initialization and runs policy iteration
        for a given number of iterations using the supplied
        discount factor.
    """
    def __init__(self, mdp, discount = 0.9, iterations = 20):
        """
          Your policy iteration agent should take an mdp on
          construction, run the indicated number of iterations
          and then act according to the resulting policy.

          Some useful mdp methods you will use:
              mdp.getStates()
              mdp.getPossibleActions(state)
              mdp.getTransitionStatesAndProbs(state, action)
              mdp.getReward(state, action, nextState)
              mdp.isTerminal(state)
        """
        self.mdp = mdp
        self.discount = discount
        self.iterations = iterations
        states = self.mdp.getStates()
        # initialize policy arbitrarily
        self.policy = {}
        for state in states:
            if self.mdp.isTerminal(state):
                self.policy[state] = None
            else:
                self.policy[state] = self.mdp.getPossibleActions(state)[0]
        # initialize policyValues dict
        self.policyValues = {}
        for state in states:
            self.policyValues[state] = 0

        for i in range(self.iterations):
            # step 1: call policy evaluation to get state values under policy, updating self.policyValues
            self.runPolicyEvaluation()
            # step 2: call policy improvement, which updates self.policy
            self.runPolicyImprovement()

    def runPolicyEvaluation(self):
        """ Run policy evaluation to get the state values under self.policy. Should update self.policyValues.
        Implement this by solving a linear system of equations using numpy. """
        N = len(self.policy)
        R = np.zeros((N,1))
        i = 0
        T = np.zeros((N,N))
        I = np.eye(N)
        states = []
        for state in self.policyValues:
            R[i][0] = self.mdp.getReward(state)
            states.append(state)
            i += 1
        for i in range(N):
            if not self.mdp.isTerminal(states[i]):
                for sPrime in self.mdp.getTransitionStatesAndProbs(states[i], self.policy[states[i]]):
                    T[i][states.index(sPrime[0])] = sPrime[1]
        V = np.linalg.solve((I-self.discount*T), R)
        i = 0
        for state in states:
            self.policyValues[state] = V[i][0]
            i += 1

    def runPolicyImprovement(self):
        """ Run policy improvement using self.policyValues. Should update self.policy. """
        for state in self.policy:
            if not self.mdp.isTerminal(state):
                actions = self.mdp.getPossibleActions(state)
                best_total = self.computeQValueFromValues(state, actions[0])
                best_action = actions[0]
                for action in self.mdp.getPossibleActions(state):
                    total = self.computeQValueFromValues(state, action)
                    if total > best_total:
                        best_total = total
                        best_action = action
                self.policy[state] = best_action
        #pdb.set_trace()




    def computeQValueFromValues(self, state, action):
        """
          Compute the Q-value of action in state from the
          value function stored in self.policyValues.
        """
        total = 0
        for sPrime in self.mdp.getTransitionStatesAndProbs(state, action):
                    total += sPrime[1]*(self.mdp.getReward(state) + self.discount*self.getValue(sPrime[0]))
        return total

    def getValue(self, state):
        return self.policyValues[state]

    def getQValue(self, state, action):
        return self.computeQValueFromValues(state, action)

    def getPolicy(self, state):
        return self.policy[state]

    def getAction(self, state):
        return self.policy[state]
