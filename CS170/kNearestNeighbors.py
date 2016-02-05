import numpy
from numpy  import *
import math
import sys
from __future__ import division
from scipy.spatial import distance


def kNearestNeighbors(k, trainFeats, trainLabels, valFeats, valLabels, testFeats):
	""" input: image of a digit
		output: class in the set{0, 1, 2, ..., 9}

		image of digit is a 28 x 28 pixel image 
		each pixel is a feature: 28^2 = 784 features
		value of a feature = intensity (of that corresponding pixel), normalized to be in the range 0..1

		dataset split into training, validation, and test sets & class of each image in the training and validation sets is provided
	"""
	### for valFeats testing ###
	# error = 0
	# for i in range(len(valLabels)):
	#	classified = str(classify(valFeats[i], trainFeats, trainLabels, k))
	# 	print("CLASSIFIED: " + classified + " correct Label: " + str(valLabels[i]))
	# 	if classified != int(valLabels[i]):
	# 		error += 1
	# print("K : " + str(k) + "   ERROR: " + str(float(error/len(valLabels))))
	# return float(error/len(valLabels))

	### for testFeats testing ###
	answers = []
	for i in range(len(testFeats)):
		print("CLASSIFIED: " + str(classify(testFeats[i], trainFeats, trainLabels, k)))
		answers.append(classify(testFeats[i], trainFeats, trainLabels, k))
	return answers


def classify(digitData, feats, labels, k):
	digitData = digitData.strip().split(",")
	digitData = [float(n) for n in digitData]
	classes = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
	indices = []
	minDist = []
	for i in range(k):
		indices.append(None)
		minDist.append(float('inf'))
	for i in range(len(feats)): #need i to return valLabels[i]
		euclid = 0
		if type(feats[i]) != list:
			feats[i] = feats[i].strip().split(",")
			feats[i] = [float(n) for n in feats[i]]
		euclid = distance.euclidean(digitData, feats[i])
		currMax = max(minDist)
		if euclid <= currMax:
			index = minDist.index(currMax)
			minDist[index] = euclid
			indices[index] = i
	for i in range(len(indices)):
		classes[int(labels[indices[i]])] += 1
	return classes.index(max(classes))


if __name__ == '__main__':
	answers = []
	trainFeats = open(sys.argv[1]).readlines()
	trainLabels = open(sys.argv[2]).readlines()
	valFeats = open(sys.argv[3]).readlines()
	valLabels = open(sys.argv[4]).readlines()
	testFeats = open(sys.argv[5]).readlines()
	# answers.append(kNearestNeighbors(1, trainFeats, trainLabels, valFeats, valLabels, testFeats))
	# print("")
	# answers.append(kNearestNeighbors(2, trainFeats, trainLabels, valFeats, valLabels, testFeats))
	# print("")
	# answers.append(kNearestNeighbors(5, trainFeats, trainLabels, valFeats, valLabels, testFeats))
	# print("")
	# answers.append(kNearestNeighbors(10, trainFeats, trainLabels, valFeats, valLabels, testFeats))
	# print("")
	# answers.append(kNearestNeighbors(25, trainFeats, trainLabels, valFeats, valLabels, testFeats))
	print("ANSWERS:" + str(answers))
	print("TESTFEATURES: " + str(kNearestNeighbors(1, trainFeats, trainLabels, valFeats, valLabels, testFeats)))



	
