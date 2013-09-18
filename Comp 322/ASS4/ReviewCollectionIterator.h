#ifndef REVIEW_COLLECTION_ITERATOR_H
#define REVIEW_COLLECTION_ITERATOR_H

#include <vector>
#include <string>
#include <iostream>

using namespace std;

class ReviewCollectionIterator
{
 public:
  ReviewCollectionIterator(vector<string>::const_iterator posStart, vector<string>::const_iterator posEnd, vector<string>::const_iterator negStart, vector<string>::const_iterator negEnd, bool isPositive);
  //needed to assign iterator at the beginning
  ReviewCollectionIterator& operator=(const ReviewCollectionIterator& other);
  //e.g. the above method becomes
  //ReviewCollectionIterator& ReviewCollectionIterator::operator=(const ReviewCollectionIterator& other)
  //in the ReviewCollectionIterator.cc file since it is outside the class { } there.

  //needed to check whether iterators are equal or not
  bool operator==(const ReviewCollectionIterator& other);
  bool operator!=(const ReviewCollectionIterator& other);

  //for prefix. e.g. ++current
  ReviewCollectionIterator& operator++();

  //for postfix e.g. current++
  ReviewCollectionIterator& operator++(int);

  //to get the value of
  string operator*();

 private:
    vector<string>::
    vector<string>::const_iterator positiveStart;
    vector<string>::const_iterator positiveEnd;
    vector<string>::const_iterator negativeStart;
    vector<string>::const_iterator negativeEnd;
    bool positive;
};

#endif
