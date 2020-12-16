printf "Query no. 1: \n"
curl -XGET "localhost:9200/projectindex/_search?pretty" -H 'Content-Type: application/json' -d'
{
	"query":{
		"bool": {
			"must": [
				{"match": {"keywords": "animals"}}
			],
			"filter": [
				{ "range": {"year": {"gte": 1950}}}
			]
		}
	},
	"_source": {
		"includes": [
			"title", "year"
		]
	},
	"sort": [
	{
		"year": {"order": "asc"}
	}
	]
}'

printf "Query no. 2:\n"
curl -XGET "localhost:9200/projectindex/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "size":0,
  "query":{
    "bool":{
      "must": [
         { "match":{ "genre": "adventure"}}
      ]
    }
    },
    "aggs":{
      "cast":{
        "terms":{
         "field": "cast"
        }
      }
    }
}'

printf "Query no. 3:\n"
curl -XGET "localhost:9200/projectindex/_search?pretty" -H 'Content-Type: application/json' -d'
{
    "size" : 0,
    "query" : {
      "bool" : {
        "minimum_should_match": 1,
        "should":[
        {"match":{"keywords":"crime"}},
        {"match":{"keywords":"mafia"}},
        {"match":{"keywords":"murder"}},
        {"match":{"keywords":"homicide"}}
        ]
      }
    },

    "aggs":{
      "year":{
        "terms":{
          "field": "year"
        }
      }
    }
}'

printf "Query no. 4:\n"
curl -XGET "localhost:9200/projectindex/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query":{
    "bool":{
      "must":[
      {
         "match_phrase_prefix":{ "keywords": "corrupt"}
      },
    {
    "bool":{
      "should":[
            {"match_phrase":{ "keywords": "europe"}},
            {"match_phrase":{ "keywords": "united states"}},
        {"match_phrase_prefix":{ "keywords": "politi"}}
          ]
         }
      }
    ]
  }
},
  "_source":{
    "includes": [
    "title",
    "year"
    ]
  },
    "sort": [
    {
      "year":{
        "order":"asc"
       }
      }
    ]
  }'
