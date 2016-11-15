# Project description

Price-processor project is backed on Spring Boot and Spring batch. 
It is responsible for parsing and aggregating data provided in csv files. 
Result is also saved into csv file. Project is using in-memory h2 database, which is responsible for storing data used during input processing.

## Project input

There are three inputs in a project. They have default value in project. Moreover, they can be parametrized for example
using command line (--property_name=property_value)

 - currenciesPath: specify location of csv containing currencies, default:currencies.csv
 - dataPath: specify location of csv containing information about transactions, default:currencies.csv
 - matchingPath: specify location of csv containing information about matching data, default:matchings.csv

## Project workflow

Batch job contains three steps responsible for changing input into output:

 - loading information about currencies into db
 - loading information about transaction and translating its price into currency specified (property called currency)
 - querying information about transactions and aggregating them.
 
 
## Project output

Output is saved into file specified by resultPath property. By default its is /tmp/products/top_products.csv

