#ifndef MOVIEINFORMATION_H_INCLUDED
#define MOVIEINFORMATION_H_INCLUDED

#include <iostream>

class MovieInformation
{
    public:
        MovieInformation(std::string name, int id);
        std::string getName();
        int getId();


    private:
        std::string m_name;
        int m_id;
};


#endif
