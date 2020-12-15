curl -XPUT "localhost:9200/projectindex?pretty" -H 'Content-Type: application/json' -d '
{
	"mappings": {
		"properties": {
			"summary": {
				"type": "text"
			},
			"cast": {
				"type": "text"
			},
			"keywords": {
				"type": "text"
			},
			"year": {
				"type": "integer"
			},
			"director": {
				"type": "text"
			},
			"genre": {
				"type": "text"
			},
			"rating": {
				"type": "float"
			},
			"title": {
				"type": "text"
			}
		}
	}
}'

for file in json/*.json
do
curl -H 'Content-Type: application/json' -XPOST "localhost:9200/projectindex/_doc/?pretty" --data-binary "@${file}"
done
