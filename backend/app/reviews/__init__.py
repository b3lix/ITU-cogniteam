# Flask related imports
from flask import Blueprint

bp = Blueprint("reviews", __name__)

# App related imports
from app.reviews import routes
