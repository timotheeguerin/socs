#ifndef COLLABORATIVE_RECOMMENDER_H
#define COLLABORATIVE_RECOMMENDER_H
#include <iostream>
#include <string>
#include <vector>
#include <list>
#include <algorithm>
#include "SimpleRecommender.h"
#include "RatingInformation.cc"
#include "MovieInformation.h"
#include "Recommender.h"

using namespace std;

class CollaborativeRecommender : public Recommender
{
    private :
      //can add private helper methods if you like
      //Hint: computeSimilarity can be adapted from the A2 solutions
      int computeSimilarity(int user1, int user2);

    //int getMovieId(string movieName);

    public :
      double generatePrediction(int userid, string movieName);

};

#endif
