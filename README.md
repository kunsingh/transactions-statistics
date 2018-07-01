## General Description

API is supposed to calculate real-time statistics summary based on transaction data records received for the last 60 seconds. Calculation of statistic is made in constant time and memory (O(1)).


## API endpoints

#### POST /transactions

This endpoint is called every time a new transaction happened.

Request:

    {
    	"amount": 12.3,
    	"timestamp": 1478192204000
    }
where:
 - amount: transaction amount 
 - timestamp: transaction time in epoch in millis in UTC time zone (this is not current
timestamp)

Response: Empty body with either 201 or 204.
 - 201 - in case of success
 - 204 - if transaction is older than 60 seconds
 
where:
 - amount: double specifying the amount 
 - time: long specifying unix epoch time

#### GET /statistics

This is the main endpoint of this task, this endpoint have to execute in constant time and
memory (O(1)). It returns the statistic based on the transactions which happened in the last 60
seconds.

Response:

    {
    	"sum": 1000,
    	"avg": 100,
    	"max": 200,
    	"min": 50,
    	"count": 10
    }

where:
 - sum: double specifying total sum of transaction values in the last 60 seconds
 - avg: double specifying average of all transaction values in the last 60 seconds
 - max: double specifying highest transaction value in the last 60 seconds
 - max: double specifying lowest transaction value in the last 60 seconds
 - count: long specifying total number of transactions happened in the last 60 seconds
