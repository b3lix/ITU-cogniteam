# Flask related imports
from flask import Blueprint

bp = Blueprint("food", __name__)

# App related imports
from app.food import routes
