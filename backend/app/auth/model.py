# Projekt ITU
# Autori:
#   xnosko06 (Matúš Nosko)

# Standard library imports
from werkzeug.security import generate_password_hash, check_password_hash

# App related imports
from app import session
from app.entities.user import User

def create_user(username: str, password: str) -> User:
    """Add user to database

    :param username:    Username
    :param password:    Password

    :return:            True if user was created, False otherwise
    """
    user: User = User.query.filter_by(username = username).first()

    # Check if user with same username already exists
    if user is not None:
        return None

    user = User(username=username, password=generate_password_hash(password))
    session.add(user)
    session.commit()
    session.refresh(user)
    return user

def authenticate(username: str, password: str) -> User:
    """Verify user credentials

    :param username:    Username
    :param password:    Password

    :return:            True if credentials are correct, False otherwise
    """
    user: User = User.query.filter_by(username = username).first()
    if user is None or not check_password_hash(user.password, password):
        return None
    return user
