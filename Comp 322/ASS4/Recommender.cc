#include "Recommender.h"

using namespace std;

double Recommender::generatePrediction(int userid, string movieName)
{
    return 0;
}

vector<string> Recommender::makeRecommendations(int userid, int n)
{
    vector<int> movieSeenId;

    //Get the list of movie seen
    for(list<RatingInformation>::iterator it = ratings.begin(); it != ratings.end(); it++)
    {
        if((*it).userId == userid)
        {
            movieSeenId.push_back((*it).movieId);
        }
    }

    vector<MovieInformation> movieUnSeen(movies.size() - movieSeenId.size());

    CheckMatching matchId(movieSeenId);
    remove_copy_if(movies.begin(), movies.end(), movieUnSeen.begin(), matchId);

    vector<string> choice;
    SortMovies sortMovies(userid, this);
    sort(movieUnSeen.begin(), movieUnSeen.end(), sortMovies);
    int count = 0;
    for(vector<MovieInformation>::iterator it = movieUnSeen.begin(); it != movieUnSeen.end(); it++)
    {
        if(count >= n)
            break;
        choice.push_back((*it).getName());
        count ++;
    }

    return choice;


}


void Recommender :: loadMovieListFromFile(string filename)
{
    ifstream file(filename.c_str(), fstream::in);
    if(file.fail())
    {
        cout << "Error when opening file " << filename << endl;
        return;
    }
    int count = 0;
    string name;

    while(getline(file, name))
    {
        MovieInformation mi(name, count);
        movies.push_back(mi);
        count ++;

    }
}

void Recommender :: loadRatingsFromFile(string filename)
{
    ifstream file(filename.c_str(), fstream::in);
    if(file.fail())
    {
        cout << "Error when opening file " << filename << endl;
        return;
    }

    string line = "";
    while(getline(file, line))
    {
        //check empty line

        if(line.length() > 0)
        {
            stringstream ss(line);

            RatingInformation ri;
            ss >> ri.userId >> ri.movieId >> ri.rating;
            ratings.push_back(ri);
        }
    }

}



int Recommender :: getMovieId(string movieName)
{
    int res = -1;
    for(std::vector<MovieInformation>::iterator it = movies.begin(); it != movies.end(); it++)
    {
        if((*it).getName() == movieName)
        {
            res = (*it).getId();
        }
    }
    return res;
}


void Recommender :: printMovies()
{

    cout << "--------MOVIES:---------" << endl;
    for(vector<MovieInformation>::iterator it = movies.begin(); it != movies.end(); it++)
    {
        cout << (*it).getId() << " - " << (*it).getName() << endl;
    }
}

void Recommender :: printRatings()
{

    cout << "--------RATINGS:---------" << endl;
    for(list<RatingInformation>::iterator it = ratings.begin(); it != ratings.end(); it++)
    {
        cout << "UserId is " << (*it).userId << " ,MovieId is " << (*it).movieId << " ,Rating is " << (*it).rating << endl;
    }
}
