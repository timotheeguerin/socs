#include "ReviewCollectionIterator.h"
using namespace std;

ReviewCollectionIterator:: ReviewCollectionIterator(vector<string>::const_iterator posStart,
                                                    vector<string>::const_iterator posEnd,
                                                    vector<string>::const_iterator negStart,
                                                    vector<string>::const_iterator negEnd, bool isPositive)
{
    positiveStart = posStart;
    positiveEnd = posEnd;
    negativeStart = negStart;
    negativeEnd = negativeEnd;
    positive = isPositive;

}
//needed to assign iterator at the beginning
ReviewCollectionIterator& ReviewCollectionIterator::operator=(const ReviewCollectionIterator& other)
{
    positiveStart = other.positiveStart;
    positiveEnd = other.positiveEnd;
    negativeStart = other.negativeStart;
    negativeEnd = other.negativeEnd;
    positive = other.positive;
}
//e.g. the above method becomes
//ReviewCollectionIterator& ReviewCollectionIterator::operator=(const ReviewCollectionIterator& other)
//in the ReviewCollectionIterator.cc file since it is outside the class { } there.

//needed to check whether iterators are equal or not
bool ReviewCollectionIterator::operator==(const ReviewCollectionIterator& other)
{
    return (positiveStart == other.positiveStart
        &&  positiveEnd == other.positiveEnd
        && negativeStart == other.negativeStart
        && negativeEnd == other.negativeEnd
        && positive == other.positive);
}

bool ReviewCollectionIterator::operator!=(const ReviewCollectionIterator& other)
{
    return !(positiveStart == other.positiveStart
        &&  positiveEnd == other.positiveEnd
        && negativeStart == other.negativeStart
        && negativeEnd == other.negativeEnd
        && positive == other.positive);
}

//for prefix. e.g. ++current
ReviewCollectionIterator& ReviewCollectionIterator::operator++()
{

    if(positive)
    {
        ++positiveStart;
        if(negativeStart != negativeEnd)
        {
            positive = false;
        }
        else
        {
            ++positiveStart;
        }
        return (*this);
    }
    else
    {
        ++negativeStart;
        if(positiveStart != positiveEnd)
        {
            positive = true;
        }

        return (*this);
    }

}

//for postfix e.g. current++
ReviewCollectionIterator& ReviewCollectionIterator::operator++(int)
{

    ReviewCollectionIterator& temp = (*this);

    if(positive)
    {
        positiveStart ++;
        if(negativeStart != negativeEnd)
        {
            positive = false;
        }
        return temp;
    }
    else
    {
        negativeStart ++;
        if(positiveStart != positiveEnd)
        {
            positive = true;
        }

        return temp;
    }


}
//to get the value of
string ReviewCollectionIterator::operator*()
{
    if(positive)
    {
        return (*positiveStart);
    }
    else
    {
        return (*negativeStart);
    }

}
