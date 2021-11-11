function transform(input) {
    var message = JSON.parse(input)
    if (message && message.name){
        message.name = 'Transform_name: ' + message.name
        return JSON.stringify(message)
    }

    return message
}
