#include <algorithm>
#include <iostream>
#include <fstream>
#include <sstream>
#include <iostream>
#include <string>
#include <vector>

#include "ad_block_client.h"

using namespace std;

static char *getFileBytes(const char *fileName);
bool shouldBlockURL(const std::string &currentPageDomain, const std::string &urlToCheck);

static AdBlockClient client = []() -> AdBlockClient {
    AdBlockClient adClient;

    char *buffer = getFileBytes("./filters/filter.dat");

    if (!adClient.deserialize(buffer))
    {
        delete[] buffer;
        throw "Failed to deserialize the filter";
    }

    return adClient;
}();

static char *getFileBytes(const char *filename)
{
    ifstream in(filename, ios::in | ios::binary | ios::ate);

    if (in)
    {
        size_t size = in.tellg();
        in.seekg(0, ios::beg);

        char *data = new char[size + 1];
        in.read(data, size);
        data[size] = EOF;

        return data;
    }

    return nullptr;
}

bool shouldBlockURL(const std::string &currentPageDomain, const std::string &urlToCheck)
{
    return client.matches(urlToCheck.c_str(), FONoFilterOption, currentPageDomain.c_str());
}
