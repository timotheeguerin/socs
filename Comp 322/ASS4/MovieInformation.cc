#include "MovieInformation.h"

#include "MovieInformation.h"

using namespace std;

MovieInformation::MovieInformation()
{

}

MovieInformation::MovieInformation(std::string name, int id) : name(name), movieId(id)
{

}


std::string MovieInformation::getName() const
{
    return name;
}

int MovieInformation:: getId() const
{
    return movieId;
}
