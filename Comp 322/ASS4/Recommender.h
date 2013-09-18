#ifndef RECOMMENDER_H
#define RECOMMENDER_H
#include <iostream>
#include <string>
#include <vector>
#include <list>
#include <fstream>
#include <algorithm>
#include <sstream>
#include "RatingInformation.cc"
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



class Recommender
{
  public :
    virtual double generatePrediction(int userid, string movieName) = 0;

    vector<string> makeRecommendations(int userid, int n);
    void loadMovieListFromFile(string filename);
    void loadRatingsFromFile(string filename);


    //optional but maybe useful for debugging:
    void printMovies();
    void printRatings();


  protected :
    list<RatingInformation> ratings;
    vector<MovieInformation> movies;

    //Feel free to add any methods that you think are shared between both recommenders
    //For example:
    int getMovieId(string movieName);
};


class SortMovies
{
    private :
        int userid;
        Recommender* sr;
    public :
        SortMovies(int number, Recommender* rec) : sr(rec)
        {
            userid = number;
        }
        bool operator()(const MovieInformation& mi1, const MovieInformation& mi2)
        {
            return (sr->generatePrediction(userid, mi1.getName()) > sr->generatePrediction(userid, mi2.getName()));
        }
};
#endif
