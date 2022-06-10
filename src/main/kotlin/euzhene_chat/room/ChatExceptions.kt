package euzhene_chat.room

class MemberAlreadyExistsException : Exception("There is already a member with the same username")
class UserNotExistsException : Exception("There is no such user with your given login")
class ParameterNotFoundException : Exception("There is not enough parameters to continue")
class UsernameNotProvidedException : Exception("There is no username passed")
class LoginIsAlreadyTakenException :
    Exception("You cannot use this login as it's already been taken")

class PasswordNotCorrectException : Exception("Password is not correct")