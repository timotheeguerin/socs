#include <iostream>
#include "MovieRecommender.h"

using namespace std;

list<RatingInformation> readFromFile(char* fileName)
{
    list<RatingInformation> ratingInfos;

    ifstream file(fileName, fstream::in);
    if(file.fail())
    {
        cout << "Error when opening file " << fileName << endl;
        return ratingInfos;
    }


    while(!file.eof())
    {
        RatingInformation ri;
        file >> ri.userId >> ri.movieId >> ri.rating;
        ratingInfos.push_back(ri);
    }

    return ratingInfos;
}

template<class InputIterator>
void print(InputIterator start, InputIterator end)
{
    for(InputIterator it = start; it != end; it++)
    {
        cout << "UserId is " << (*it).userId << " ,MovieId is " << (*it).movieId << " ,Rating is " << (*it).rating << endl;
    }
}

void transform(vector<RatingInformation>& ratings, int shift)
{
    for(vector<RatingInformation>::iterator it = ratings.begin(); it != ratings.end(); it++)
    {
        (*it).rating += shift;
    }
}

int dotProduct(const vector<RatingInformation>& ratings1, const vector<RatingInformation>& ratings2)
{
    vector<RatingInformation>::const_iterator it1 = ratings1.begin();
    vector<RatingInformation>::const_iterator it2 = ratings2.begin();
    int product = 0;

    while(it1 != ratings1.end() && it2 != ratings2.end())
    {
        product += (*it1).rating * (*it2).rating;
        it1 ++;
        it2 ++;

    }

    return product;

}

template<class InputIterator>
bool containsMovie(InputIterator start, InputIterator end, int movieId)
{
    for(InputIterator it = start; it != end; it++)
    {
        if((*it).movieId == movieId)
            return true;
    }

    return false;
}

template<class InputIterator>
std::vector<RatingInformation> getSubset(InputIterator user1Start,InputIterator user1Finish, InputIterator user2Start, InputIterator user2Finish)
{

    std::vector<RatingInformation> result;
    for(InputIterator it = user1Start; it != user1Finish; it++)
    {

        if(containsMovie(user2Start,user2Finish, (*it).movieId))
        {
            RatingInformation rating = (*it);
            result.push_back(rating);
        }
    }

    return result;

}

bool compare(RatingInformation r1, RatingInformation r2)
{
    return (r1.movieId < r2.movieId);
}

std::list<RatingInformation> usercopy(std::list<RatingInformation> from, int userId)
{
    std::list<RatingInformation> to;
    for(std::list<RatingInformation>::iterator it = from.begin(); it != from.end(); it++)
    {
        if((*it).userId == userId)
            to.push_back(*it);
    }
    return to;
}

int computeSimilarity(list<RatingInformation> ratings, int user1, int user2)
{
    cout << "compute: 1" << endl;
    list<RatingInformation> ratings_user1 = usercopy(ratings, user1);
    list<RatingInformation> ratings_user2 = usercopy(ratings, user2);

    cout << "copy 1: " << ratings_user1.size() << endl;
    cout << "copy 2: " << ratings_user2.size() << endl;

    vector<RatingInformation> overlap1 = getSubset(ratings_user1.begin(), ratings_user1.end(), ratings_user2.begin(), ratings_user2.end());
    vector<RatingInformation> overlap2 = getSubset(ratings_user2.begin(), ratings_user2.end(), ratings_user1.begin(), ratings_user1.end());
    cout << "compute: 4" << endl;
    transform(overlap1, -5);
    transform(overlap2, -5);

    sort(overlap1.begin(), overlap1.end(), compare);
    sort(overlap2.begin(), overlap2.end(), compare);

    return dotProduct(overlap1, overlap2);

}



