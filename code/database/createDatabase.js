db.users.insertOne({
	"_id":"1", 
	"name":"yang222", 
	"password":"1111", 
	"qq":"2837240931", 
	"weixin":"yangruiheng", 
	"email":"yangruiheng1@126.com"});

db.targets.insertOne({
	"_id":"1", 
	"creatorID":"1", 
	"targetImage":"532q3532432rer325", 
	"location":21234});

db.contents.insertOne({
	"_id":"1",
	"targetID":"1",
	"modelID":"1",
	"textID":"1",
	"musicID":"1",
	"videoID":"1"});

db.comments.insertOne({
	"_id":"1",
	"userID":"1",
	"type":"text",
	"targetID":"1",
	"contentID":"1",
	"text":"Hey!",
	"soundID":"1"});