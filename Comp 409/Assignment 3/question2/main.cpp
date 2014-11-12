#include <omp.h>
#include <stdio.h>
#include <string.h>
#include <map>
#include <vector>
#include <iostream>
#include <regex>
#include <chrono>
#include "gen.cpp"

using namespace std;

enum State {
    STATE0, STATE1, STATE2, STATE3, STATE4
};

State state0(char *string);

State state1(char *string);

State state2(char *string);

State state3(char *string);

State getNextState(State state, char *string, int remaining);

void printThreadOutputs(std::vector<std::map<State, State>> &outputs);

bool match(char *str, int optimistic_thread = 0);

template<typename Functor>
void benchmark(Functor func);

int main(int argc, char *argv[]) {
    printf("Starting\n");

    char *str = buildString();
    cout << "Checking string of length: " << strlen(str) << endl;

    auto func = [str]() {
        cout << "Match: " << match(str, 3) << endl;
    };
    benchmark(func);
    return 0;
}

bool match(char *str, int optimistic_thread) {
    int thread_count = optimistic_thread + 1;
    int thread_id;
    int str_len = (int) strlen(str);
    std::vector<std::map<State, State>> outputs((unsigned long) thread_count);
    State current_state = STATE0;
    omp_set_num_threads(thread_count);

#pragma omp parallel private(thread_id)
    {
        // Obtain thread number and thread count
        thread_id = omp_get_thread_num();
        //Separate the string smartly(Master thread need a string 4x longer than optimistics threads
        // as they have to check for each starting state)
        // e.g. For a string of length 14, master thread will read 8 characters and the others only 2
        int part = thread_count + 3;
        int remaining, begin;

        if (thread_id == 0) { //Master
            remaining = str_len * 4 / part;
            printf("Master thread reading string of size %d\n", remaining);
            current_state = getNextState(STATE0, str, remaining);
        } else { //Other threads
            begin = (str_len * (4 + thread_id - 1)) / part;
            remaining = str_len / part;
            if (thread_id == thread_count - 1) { // If this is the last thread
                // Last thread need to finish the string
                // If the string length is not divisible by part then it might skip some characters
                remaining = str_len - begin;
            }
            printf("Thread %d reading string of size %d starting at %d\n", thread_id, remaining, begin);
            std::map<State, State> map;
            char *thread_str = str + begin; //Get the string pointer
            map[STATE0] = getNextState(STATE0, thread_str, remaining);
            map[STATE1] = getNextState(STATE1, thread_str, remaining);
            map[STATE2] = getNextState(STATE2, thread_str, remaining);
            map[STATE3] = getNextState(STATE3, thread_str, remaining);
            map[STATE4] = STATE4;
            outputs[thread_id] = map;
        }
    }
    printThreadOutputs(outputs);

    for (int i = 1; i != outputs.size(); ++i) {
        std::cout << "Move: " << current_state << " => " << outputs[i][current_state] << std::endl;
        current_state = outputs[i][current_state];
        if (current_state == STATE4) {
            return false;
        }
    }
    std::cout << "====================================================" << std::endl;
    return true;
}

State getNextState(State state, char *string, int remaining) {
    State current_state = state;
    char *current_string = string;
    for (int i = 0; i < remaining; i++) {
        switch (current_state) {
            case STATE0:
                current_state = state0(current_string);
                break;
            case STATE1:
                current_state = state1(current_string);
                break;
            case STATE2:
                current_state = state2(current_string);
                break;
            case STATE3:
                current_state = state3(current_string);
                break;
            default:
                return STATE4;
        }
        current_string++;
    }
    return current_state;
}

State state0(char *string) {
    char c = *string;
    if (c == 'a') {
        return STATE1;
    } else {
        return STATE4;
    }
}

State state1(char *string) {
    char c = *string;
    if (c == 'a') {
        return STATE1;
    } else if (c == 'b') {
        return STATE2;
    } else {
        return STATE4;
    }
}

State state2(char *string) {
    char c = *string;

    if (c == 'b') {
        return STATE2;
    } else if (c == 'c' || c == 'd') {
        return STATE3;
    } else {
        return STATE4;
    }
}


State state3(char *string) {
    char c = *string;

    if (c == 'a') {
        return STATE1;
    } else if (c == 'c' || c == 'd') {
        return STATE3;
    } else {
        return STATE4;
    }
}

void printThreadOutputs(std::vector<std::map<State, State>> &outputs) {
    std::cout << "====================================================" << std::endl;
    for (int i = 0; i != outputs.size(); ++i) {
        if (i != 0) {
            std::cout << "---------------------------------------------------" << std::endl;
        }
        printf("Thread %d:\n", i);
        for (std::map<State, State>::const_iterator it = outputs[i].begin();
             it != outputs[i].end(); ++it) {
            std::cout << "\t" << it->first << " => " << it->second << std::endl;
        }
    }
    std::cout << "====================================================" << std::endl;
}

template<typename Functor>
void benchmark(Functor func) {
    auto time_begin = chrono::high_resolution_clock::now();
    func();
    auto time_end = chrono::high_resolution_clock::now();
    auto elapsed = std::chrono::duration_cast<std::chrono::milliseconds>(time_end - time_begin).count();
    cout << "Time elapsed: " << elapsed << endl;
}
