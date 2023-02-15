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
    
    - type : 
    CHAT,
    CREATE_ROOM,
    GET_LIST_ROOM_OF_USER,
    GET_LIST_USER_IN_ROOM,
    DELETE_ROOM,
    UPDATE_ROOM,
    JOIN_ROOM,
    DELETE_USER_ROOM,
    LEAVE_ROOM,
    ERROR
    
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
    
