# Projekt ITU
# Autori:
#   xnosko06 (Matúš Nosko)

# Flask related imports
from flask_login.utils import logout_user
from flask_pydantic import validate
from flask_login import login_user, login_required, current_user

# App related imports
from app import make_response
from app.auth import bp
from app.auth.model import authenticate, create_user
from app.auth.schemas import RegisterModel, LoginModel
from app.entities.user import User

# Register usuer endpoint
@bp.route("/register", methods=["POST"])
@validate()
def register(body: RegisterModel):
    created: User = create_user(body.username, body.password)

    if created == None:
        return make_response(409, data={"message": "Uživateľ s týmto menom už existuje"})

    return make_response(200)

# Login user endpoint
@bp.route("/login", methods=["POST"])
@validate()
def login(body: LoginModel):
    user: User = authenticate(body.username, body.password)
    
    if user == None:
        return make_response(401, data={"message": "Uživateľ s týmito prihlasovacími údajmi neexistuje"})

    login_user(user)
    return make_response(200)

# Logout user endpoint
@bp.route("/logout", methods=["POST"])
def logout():
    logout_user()
    return make_response(200)

# User info endpoint
@bp.route("/info", methods=["GET"])
@login_required
def info():
    return make_response(200, data={
        "username": current_user.username
    })