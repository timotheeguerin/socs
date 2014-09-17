#ifndef REVIEW_COLLECTION_H
#define REVIEW_COLLECTION_H
#include <vector>
#include <string>
#include "ReviewCollectionIterator.h"
#include <fstream>
#include <sstream>

#include <iostream>

class ReviewCollection
{
  private :
    int movieId;
    vector<string> positiveReviews;
    vector<string> negativeReviews;


  public :
    ReviewCollection(string filename, int id);
    ReviewCollectionIterator begin();
    ReviewCollectionIterator end();
    vector<string>::const_iterator positiveIterStart();
    vector<string>::const_iterator positiveIterEnd();
    vector<string>::const_iterator negativeIterStart();
    vector<string>::const_iterator negativeIterEnd();
    int getId();
    int size();
};

#endif
