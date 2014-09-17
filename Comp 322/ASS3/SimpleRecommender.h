#ifndef SIMPLE_RECOMMENDER_H
#define SIMPLE_RECOMMENDER_H
#include <string>
#include <list>
#include <vector>
#include <map>
#include <algorithm>
#include <fstream>
#include <iostream>

#include "RatingInformation.h"
#include "MovieInformation.h"

using namespace std;

class CheckMatching
{
    private :
        vector<int> movieSeenId;
    public :
        CheckMatching(vector<int> vec)
        {
            movieSeenId = vec;
        }
        bool operator()(const MovieInformation& mi )
        {
            for(vector<int>::iterator it = movieSeenId.begin(); it != movieSeenId.end(); it++)
            {
                if((*it) == mi.getId())
                    return true;
            }
            return false;
        }
};




class SimpleRecommender
{
    private :
       //private property that stores a list of all the ratings in the entire system.
        //recall that each RatingInformation contains: userid, movieid, rating
        list<RatingInformation> ratings;

        //private property that stores a list of all the Movies in the system
        //this is a mapping from movie id to movie name.
        vector<MovieInformation> movies;

        //Hint: The following methods are not required, but might be useful
        //int getMovieId(string movieName);


    public :

        //generatePrediction takes as input an int representing a userid, and a string representing the movieName
        //to get a recommendation for. It uses a simple algorithm to first get ALL the ratings for the given
        //movie. It then returns the average of these ratings.
        double generatePrediction(int userid, string movieName);

        //This method takes as input an int representing a userid and n representing the number of movies to suggest.
        //It then, based on the generatePrediction method above, comes up with the top n movies that have not already been
        //rated by the user with id userid. It returns a vector<string> of the movie names
        vector<string> makeRecommendations(int userid, int n);

        //This method takes as input a string filename where each line contains one movie name.
        //It initializes movies based on this. It should give each movie an id corresponding with the line number in the file
        //e.g. the first movie gets id 0, 2nd gets id 1, etc. See Sample.txt for an example
        void loadMovieListFromFile(string filename);

        //This method takes as input a string filename and initializes ratings based on the file. See TestFile.txt for an example
        //You should be able to adapt the solutions to A2 for this question.
        void loadRatingsFromFile(string filename);

        void printMovies();
        void printRatings();
};

class SortMovies
{
    private :
        int userid;
        SimpleRecommender* sr;
    public :
        SortMovies(int number, SimpleRecommender* rec) : sr(rec)
        {
            userid = number;
        }
        bool operator()(const MovieInformation& mi1, const MovieInformation& mi2)
        {
            return (sr->generatePrediction(userid, mi1.getName()) > sr->generatePrediction(userid, mi2.getName()));
        }
};

#endif
