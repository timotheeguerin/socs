#ifndef MOVIERECOMMENDER_H_INCLUDED
#define MOVIERECOMMENDER_H_INCLUDED
#include <list>
#include <vector>
#include <fstream>
#include <algorithm>
#include <functional>
struct RatingInformation
{
    int userId, movieId, rating;

};

struct CPred : public std::unary_function<RatingInformation, bool>
{
    CPred(const int u)
            :user(u)
    {
    }

    void setUser(int u)
    {
        user = u;
    }

    bool operator()(const RatingInformation rating) const
    {
            return (rating.userId == user);
    }
    int user;
};


//typedef std::list<RatingInformation>::iterator InputIterator;


std::list<RatingInformation> readFromFile(char* fileName);

template<class InputIterator>
void print(InputIterator start, InputIterator end);

void transform(std::vector<RatingInformation>& ratings, int shift);
int dotProduct(const std::vector<RatingInformation>& ratings1, const std::vector<RatingInformation>& ratings);

template<class InputIterator>
bool containsMovie(InputIterator start, InputIterator end, int movieId);

template<class InputIterator>
std::vector<RatingInformation> getSubset(InputIterator user1Start,InputIterator user1Finish, InputIterator user2Start, InputIterator user2Finish);

int computeSimilarity(std::list<RatingInformation> ratings, int user1, int user2);

bool compare(RatingInformation r1, RatingInformation r2);
std::list<RatingInformation> usercopy(std::list<RatingInformation> from, int userId);
#endif // MOVIERECOMMENDER_H_INCLUDED
