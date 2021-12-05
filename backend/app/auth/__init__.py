# Projekt ITU
# Autori:
#   xnosko06 (Matúš Nosko)

# Flask related imports
from flask import Blueprint

bp = Blueprint("auth", __name__)

# App related imports
from app.auth import routes
