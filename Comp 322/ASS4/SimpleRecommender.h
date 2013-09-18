#ifndef SIMPLE_RECOMMENDER_H
#define SIMPLE_RECOMMENDER_H
#include <string>
#include <vector>
#include <list>
#include "SimpleRecommender.h"
#include "RatingInformation.cc"
#include "MovieInformation.h"
#include "Recommender.h"

using namespace std;

class SimpleRecommender : public Recommender
{
  private :
    //can add private helper methods if you like
    
    public :
    double generatePrediction(int userid, string movieName);
};

#endif
