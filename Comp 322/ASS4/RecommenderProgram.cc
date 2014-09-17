#include <iostream>
#include "SimpleRecommender.h"
#include "CollaborativeRecommender.h"
#include "ReviewCollection.h"
#include <sstream>

using namespace std;
int main()
{
    //LOAD THE REVIEWS
    vector<ReviewCollection> revColllections;
    for(int i = 0; i < 100; i++)
    {
        stringstream ss;
        ss << "reviews_" << i << ".txt";
        ReviewCollection rc(ss.str(), i);

        revColllections.push_back(rc);
    }

    while(1)
    {

        int choice2 =0;
        cout << "What do you want to do: " << endl;
        cout << "1-Get recommendation " << endl;
        cout << "2-Read review " << endl;
        cin >> choice2;

        if(choice2 == 1)
        {
            Recommender * rec;

            int choice;
            cout << "Which recommender: " << endl;
            cout << "1-Simple recommender " << endl;
            cout << "2-Collaborative recommender " << endl;
            cin >> choice;

            if(choice == 1)
            {
                rec = new SimpleRecommender();
            }
            else if(choice == 2)
            {
                rec = new CollaborativeRecommender();
            }
            else
            {
                    return 0;
            }


            string moviefile, ratingfile;
            /*cout << "Input a Movie file: ";
            cin >> moviefile;
            cout << "Input a Ratings file: ";
            cin >> ratingfile;*/
            moviefile = "Sample.txt";
            ratingfile = "TestFile.txt";
            rec->loadMovieListFromFile(moviefile);
            rec->loadRatingsFromFile(ratingfile);

            while(1)
            {
                int userid = 0;
                cout << "Input user id: ";
                cin >> userid;
                if(userid < 0)
                    return 0;
                vector<string> recv = rec->makeRecommendations(userid,5);
                if(recv.size() == 0)
                {
                    cout << "No recomendations found!" << endl;
                }
                else
                {
                    cout << "Recomendtions: " << endl;
                    for(unsigned int i = 0; i < recv.size(); i++)
                        cout << recv[i] << endl;

                }
            }
        }
        else if(choice2 == 2)
        {
            int movieId;
            int positive;
            cout << "Input a Movie id:";
            cin >> movieId;
            cout << "Positive review: 1 - mixed: 0 - negative review: -1? ";
            cin >> positive;

            if(movieId <0 || movieId > 99)
                return 0;

            if(positive == 0)
            {
                cout << "-------------------------------------------" << endl;
                cout << "               REVIEWS  MIXED              " << endl;
                cout << "-------------------------------------------" << endl;
                for(ReviewCollectionIterator it = revColllections[movieId].begin(); it != revColllections[movieId].end(); it++)
                {
                    cout << (*it) << endl;
                }

            }
            else if(positive == 1)
            {
                cout << "-------------------------------------------" << endl;
                cout << "               REVIEWS  POSITIVE           " << endl;
                cout << "-------------------------------------------" << endl;
                for(vector<string>::const_iterator it = revColllections[movieId].positiveIterStart(); it != revColllections[movieId].positiveIterEnd(); it++)
                {
                   cout << (*it) << endl;
                }
            }
            else if(positive == -1)
            {
                cout << "-------------------------------------------" << endl;
                cout << "               REVIEWS  NEGATIVE           " << endl;
                cout << "-------------------------------------------" << endl;
                for(vector<string>::const_iterator it = revColllections[movieId].negativeIterStart(); it != revColllections[movieId].negativeIterEnd(); it ++)
                {

                   cout << (*it) << endl;
                }
            }
            else
                return 0;
            cout << "-------------------------------------------" << endl;
        }
        else
        {
                return 0;
        }


    }


}
