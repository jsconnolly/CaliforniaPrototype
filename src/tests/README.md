#QA

This ReadMe is a quick overview of the QA testing code

##Automated Continuous Tests

API tests can be found in <a href='https://github.com/HOTB-Software/CaliforniaPrototype/tree/master/src/tests/server'>this directory</a>.

All automated-continuous tests are written in python and run on a continuous basis. They all pull constants, functions, and references from the APIWrapper.py file, which serves to encapsulate the tests. This serves two purposes:

1) It minimizes code duplication and allows more modularity 

2) It allows for single changes in the APIWrapper to impact all other tests at once (For example, changing the base URL for calls)


##Selenium Tests

Selenium tests can be found in <a href='https://github.com/HOTB-Software/CaliforniaPrototype/tree/master/src/tests/web'>this directory</a>.
To run create or edit Selenium tests, you must first install the software needed. Follow these steps for isntallation:

1) Download python from https://www.python.org/

2) Include the Python folder in your path ex “C:\User\Python”

3) Include scripts in the path  as well ex. “C:\Python\Scripts”

4) Open up command line and type in exactly the following

  a) Pip
  
  b) Pip install selenium
  
5) Download web drivers from http://selenium-python.readthedocs.io/installation.html

  a) Chrome, Edge, Firefox & safari web drivers can all be downloaded
  
6) Place web drivers into selenium folder & include in path ex. “C:\Python\selenium”

