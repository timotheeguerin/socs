#include "MovieInformation.h"

using namespace std;

MovieInformation::MovieInformation(std::string name, int id) : m_name(name), m_id(id)
{

}


std::string MovieInformation::getName()
{
    return m_name;
}

int MovieInformation:: getId()
{
    return m_id;
}
