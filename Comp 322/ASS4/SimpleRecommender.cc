#include "SimpleRecommender.h"

double SimpleRecommender :: generatePrediction(int userid, string movieName)
{
    int movieId  = getMovieId(movieName);
    int total = 0;
    int count = 0;
    for(list<RatingInformation>::iterator it = ratings.begin(); it != ratings.end(); it++)
    {
        if((*it).movieId == movieId)
        {
            total += (*it).rating;
            count ++;
        }
    }

    if(count != 0)
    {
        return total / count;
    }
    else return 0;

}

