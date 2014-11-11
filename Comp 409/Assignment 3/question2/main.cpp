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

State state4(char *string);

State getNextState(State state, char *string, int remaining);

int main(int argc, char *argv[]) {
    printf("Starting\n");
    int thread_count, thread_id;
    char *str = buildString();
//    char *str = "aabcabdabdccabdcabdabdabcabdabdabcdabccc";
    int str_len = (int) strlen(str);
    std::vector<std::map<State, State>> outputs;
    cout << "Will check: " << endl;
//    printString(str);
    cout << endl;
    cout << "Length: " << strlen(str) << endl;
    State current_state;

    omp_set_num_threads(4);
#pragma omp parallel
    {
        //Only the main thread resize
        if (omp_get_thread_num() == 0) {
            outputs.resize((unsigned long) omp_get_num_threads());
        }
    }
    auto time_begin = chrono::high_resolution_clock::now();
#pragma omp parallel private(thread_count, thread_id)
    {
        // Obtain thread number and thread count
        thread_id = omp_get_thread_num();
        thread_count = omp_get_num_threads();
        int part = thread_count + 4;
        int begin = (thread_id * str_len) / thread_count;
        int end = ((thread_id + 1) * str_len) / thread_count;
        int remaining = end - begin;
        printf("Thread %d will take care of zone %d to %d\n", thread_id, begin, end);

        /* Only master thread does this */
        if (thread_id == 0) {
            remaining = str_len * 4 / part;
            current_state = getNextState(STATE0, str, remaining);
        } else { //Other threads
            remaining = str_len / part;
            begin = (str_len * (4 + thread_id - 1)) / part;
            printf("Thread %d begin:  %d\n", thread_id, begin);
            std::map<State, State> map;
            char *thread_str = str + begin;
            map[STATE0] = getNextState(STATE0, thread_str, remaining);
            map[STATE1] = getNextState(STATE1, thread_str, remaining);
            map[STATE2] = getNextState(STATE2, thread_str, remaining);
            map[STATE3] = getNextState(STATE3, thread_str, remaining);
            map[STATE4] = STATE4;
            outputs[thread_id] = map;
        }
    }
    for (int i = 0; i != outputs.size(); ++i) {
        cout << "ede" << endl;
        printf("Thread %d:\n", i);
        for (std::map<State, State>::const_iterator it = outputs[i].begin();
             it != outputs[i].end(); ++it) {
            std::cout << it->first << "=>" << it->second << std::endl;
        }
        std::cout << "------------------" << std::endl;
    }
//    cout << "Match: " << std::regex_match(str, std::regex("^(a+b+(c|d)+)+$")) << endl;
    for (int i = 1; i != outputs.size(); ++i) {
        std::cout << "Move: " << current_state << " => " << outputs[i][current_state] << std::endl;
        current_state = outputs[i][current_state];
        if (current_state == STATE4) {
            std::cout << "Not a string" << std::endl;
            return 0;
        }
    }
    std::cout << "This string match" << std::endl;

    auto time_end = chrono::high_resolution_clock::now();
    auto elapsed = std::chrono::duration_cast<std::chrono::milliseconds>(time_end - time_begin).count();
    cout << "Time elapsed: " << elapsed << endl;
    return 0;
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
