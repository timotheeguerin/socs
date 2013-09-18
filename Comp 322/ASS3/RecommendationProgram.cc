#include <iostream>
#include "SimpleRecommender.h"

using namespace std;
int main()
{
    string moviefile, ratingfile;
    cout << "Input a Movie file: ";
    cin >> moviefile;
    cout << "Input a Ratings file: ";
    cin >> ratingfile;


    SimpleRecommender simpleRec;
    simpleRec.loadMovieListFromFile(moviefile);
    simpleRec.loadRatingsFromFile(ratingfile);

    int userid = 0;
    do
    {
        cout << "Input user id: ";
        cin >> userid;
            vector<string> rec = simpleRec.makeRecommendations(userid,5);
        if(rec.size() == 0)
        {
            cout << "No recomendations found!" << endl;
        }
        else
        {
            cout << "Recomendtions: " << endl;
            for(int i = 0; i < rec.size(); i++)
                cout << rec[i] << endl;
            return 0;

        }

    } while(userid != -1);


}
