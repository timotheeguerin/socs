#ifndef MOVIE_INFORMATION_H
#define MOVIE_INFORMATION_H

#include <string>
using namespace std;
class MovieInformation
{
private :
    //private property name represents the name of the movie
  string name;

  //private property movidId represents the id of a movie
  int movieId;

public :

    //constructor takes as input a name and id and initializes the MovieInformation object accordingly
    MovieInformation();
    MovieInformation(string moviename, int id);

  //getName returns the name property
  string getName() const;

  //getId returns the id property
  int getId() const;
};

#endif
