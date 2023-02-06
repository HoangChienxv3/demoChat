# demoChat
# server
1. login
    api: host/authenticate
    body:
    {
        "username":"admin",
        "password":"123456"
    }
    response:
    {
        "jwttoken": "token"
    }
2. connect websocket
    url: 
    ws://host/websocket/token
    body:
    {
        "type": "CREATE_ROOM",
        "content": "",
        "idRoom": "",
        "idPartner": ""
    }
    repsonse:
    {
        "type": "CREATE_ROOM",
        "content": "successful room creation",
        "sender": {
            "id": 1,
            "userName": "admin"
        },
        "room": {
            "id": "9odDM6ouY0W4",
            "name": ""
        }
    }
