#ifndef MOVIE_INFORMATION_H
#define MOVIE_INFORMATION_H

#include <string>

using namespace std;
class MovieInformation
{
  private :
    string name;
    int movieId;

  public :
    MovieInformation();
    MovieInformation(string name, int id);
  string getName() const;
  int getId() const;
};

#endif
