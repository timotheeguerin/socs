#include "CollaborativeRecommender.h"



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

bool compare(RatingInformation r1, RatingInformation r2)
{
    return (r1.movieId < r2.movieId);
}


int CollaborativeRecommender::computeSimilarity(int user1, int user2)
{
    list<RatingInformation> ratings_user1 = usercopy(ratings, user1);
    list<RatingInformation> ratings_user2 = usercopy(ratings, user2);


    vector<RatingInformation> overlap1 = getSubset(ratings_user1.begin(), ratings_user1.end(), ratings_user2.begin(), ratings_user2.end());
    vector<RatingInformation> overlap2 = getSubset(ratings_user2.begin(), ratings_user2.end(), ratings_user1.begin(), ratings_user1.end());

    transform(overlap1, -5);
    transform(overlap2, -5);

    sort(overlap1.begin(), overlap1.end(), compare);
    sort(overlap2.begin(), overlap2.end(), compare);

    return dotProduct(overlap1, overlap2);

}


double CollaborativeRecommender :: generatePrediction(int userid, string movieName)
{
    int movieId = getMovieId(movieName);
    std::list<int> users;
    for(std::list<RatingInformation>::iterator it = ratings.begin(); it != ratings.end(); it++)
    {
        if((*it).movieId == movieId)
            users.push_back((*it).userId);
    }

    //If none of the users rated that movie, you should return 5 (the average middle rating)
    if(users.empty())
    {
        return 5;
    }
    //Get the best user
    int bestSim = 0;
    int bestUser = 0;

    for(std::list<int>::iterator it = users.begin(); it != users.end(); it++)
    {
        int sim = computeSimilarity(userid, (*it));

        cout << "dot: " << sim << endl;
        if(sim > bestSim && (*it) != userid)
        {
            bestSim= sim;
            bestUser = (*it);
        }
    }

    //Get his rating of the movie

    for(std::list<RatingInformation>::iterator it = ratings.begin(); it != ratings.end(); it++)
    {
        if((*it).userId == bestUser && (*it).movieId == movieId)
        {
            return (*it).rating;
        }

    }
    return 0;



}

