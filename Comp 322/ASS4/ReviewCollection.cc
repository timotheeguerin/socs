#include "ReviewCollection.h"

ReviewCollection:: ReviewCollection(string filename, int id)
{
    movieId = id;
    ifstream file(filename.c_str(), fstream::in);

    if(file.fail())
    {
        return;
    }

    string line;

    while(getline(file, line))
    {
        int rating = 0;
        string comment;
        stringstream ss(line);
        ss >> rating;
        stringstream ss2;
        while(!ss.eof())
        {
            string buf;
            ss >> buf;
            ss2 << buf<< " " ;
        }
        comment = ss2.str();
        cout << "cmt: " << comment << endl;
        if(rating > 0)
        {
            positiveReviews.push_back(comment);
        }
        else if(rating < 0)
        {
            negativeReviews.push_back(comment);
        }
    }
}

ReviewCollectionIterator ReviewCollection:: begin()
{
    ReviewCollectionIterator rc(positiveReviews.begin(), positiveReviews.end(), negativeReviews.begin(), negativeReviews.end(), true);
    return rc;
}

ReviewCollectionIterator ReviewCollection:: end()
{
    bool endPos= false;
    if(positiveReviews.size() > negativeReviews.size())
        endPos =  true;
    ReviewCollectionIterator rc(positiveReviews.end(), positiveReviews.end(), negativeReviews.end(), negativeReviews.end(), endPos);
    return rc;
}

vector<string>::const_iterator ReviewCollection::positiveIterStart()
{
    return positiveReviews.begin();
}

vector<string>::const_iterator ReviewCollection::positiveIterEnd()
{
    return positiveReviews.end();
}

vector<string>::const_iterator ReviewCollection::negativeIterStart()
{
    return negativeReviews.begin();
}

vector<string>::const_iterator ReviewCollection::negativeIterEnd()
{
    return negativeReviews.end();
}

int ReviewCollection:: getId()
{
    return movieId;
}

int ReviewCollection::size()
{
    return positiveReviews.size() + negativeReviews.size();
}
