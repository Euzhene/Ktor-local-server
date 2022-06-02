package euzhene_chat.room

class MemberAlreadyExistsException : Exception("There is already a member with the same username")
class UserNotExistsException:Exception("There is no such user with your given login and password")